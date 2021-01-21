package com.ahdms.billing.service.impl.dubbo;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.ahdms.billing.config.omp.RedisKey;
import com.ahdms.billing.model.WhiteIp;
import com.ahdms.billing.service.WhiteIpService;
import com.ahdms.jf.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ahdms.billing.dao.ServiceLogMapper;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.model.ServiceLog;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.service.impl.DubboSelectUserImpl;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.jf.service.JFClientService;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;

import redis.clients.jedis.JedisCluster;

@Service
public class JFClientServiceImpl implements JFClientService {

    private static final Logger logger = LoggerFactory.getLogger(JFClientServiceImpl.class);

    @Autowired
    private ServiceLogMapper serviceLogMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private WhiteIpService whiteIpService;

    private final String LIMIT_PREFIX = "limit_";

    private final String LUA_LIMIT_SCRIPT = "local key = KEYS[1]\n" +
            "local limit = tonumber(ARGV[1])\n" +
            "local current = tonumber(redis.call('get', key) or \"0\")\n" +
            "if current + 1 > limit then\n" +
            "   return 0\n" +
            "else\n" +
            "   redis.call(\"INCRBY\", key,\"1\")\n" +
            "   redis.call(\"expire\", key,\"1\")\n" +
            "   return 1\n" +
            "end";


    @Autowired
    private JedisCluster jedisCluster;

    DefaultRedisScript<Number> redisLUAScript;
    StringRedisSerializer argsSerializer;
    StringRedisSerializer resultSerializer;

    @PostConstruct
    public void initLUA() {
        redisLUAScript = new DefaultRedisScript<>();
        redisLUAScript.setScriptText(LUA_LIMIT_SCRIPT);
        redisLUAScript.setResultType(Number.class);

        argsSerializer = new StringRedisSerializer();
        resultSerializer = new StringRedisSerializer();
    }

    @Autowired
    private DubboSelectUserImpl userService;

    @Override
    public JFResult<JFInfoModel> jf(JFReqDTO jfReqDTO) {
        Map<String, String> params = userService.getUserIdAndServiceAccount(jfReqDTO.getKey(), jfReqDTO.getType());
        String userId = params.get("userId");
        String userServiceAccount = params.get("userServiceAccount");
        if (StringUtils.isBlank(userId)) {
            return JFResult.success(JFResultEnum.NOACCOUNT, null);
        }
        if(JFChannelEnum.API == jfReqDTO.getChannel()){
            boolean whiteIpFlag = whiteIpService.checkUserIdAndIp(userId, jfReqDTO.getIp());
            if(!whiteIpFlag){
                return JFResult.success(JFResultEnum.ERROR_IP, null);
            }
        }
        return jf(userId,userServiceAccount,jfReqDTO.getMode(),jfReqDTO.getChannel());
    }

    @Override
    public JFResult<JFInfoModel> jf(String key, JFServiceEnum mode, JFChannelEnum channel, Integer type) {

        Map<String, String> params = userService.getUserIdAndServiceAccount(key, type);
        String userId = params.get("userId");
        String userServiceAccount = params.get("userServiceAccount");
        if (StringUtils.isBlank(userId)) {
            return JFResult.success(JFResultEnum.NOACCOUNT, null);
        }
        return jf(userId,userServiceAccount,mode,channel);
    }

    @Override
    public void send(String serviceAccount, String serviceCode, String channelCode, int code, String message, String serialNum,
                     String specialCode, String comment, String typeId) {
        try {
            ServiceLog serviceLog = new ServiceLog();

            //根据渠道编码，服务编码， 查询客户名称
            UserInfo userInfo = userInfoMapper.selectByServiceAccount(serviceAccount);
            if (null == userInfo) {
                return;
            }
            String newServiceCode = getMode(userInfo.getId(),serviceCode);

            serviceLog.setId(UUIDGenerator.getUUID());
            serviceLog.setMessage(message);
            serviceLog.setChannelEncode(channelCode);
            serviceLog.setOperationtime(new Date());
            serviceLog.setResult(code);
            serviceLog.setServiceEncode(newServiceCode);
            serviceLog.setTypeId(typeId);
            serviceLog.setSpecialCode(specialCode);
            serviceLog.setSerialNum(serialNum);
            serviceLog.setComment(comment);
            serviceLog.setUsername(userInfo.getUsername());

            serviceLogMapper.insertSelective(serviceLog);

            //服务次数减一
            String countKey = RedisKey.JF + newServiceCode;
            Long decr = jedisCluster.hincrBy(RedisKey.getHashKeys(userInfo.getId()), RedisKey.getFieldCount(countKey), -1);
            if (decr <= 0) { //修改为不可用
                jedisCluster.hset(RedisKey.getHashKeys(userInfo.getId()), RedisKey.getFieldUsable(countKey), JFResultEnum.NOCOUNT.getCode());
            }
        } catch (Exception e) {
            logger.error("业务日志入库失败.." + e.getMessage());
        }
    }

    private JFResult<JFInfoModel> jf(String userId,String userServiceAccount,JFServiceEnum mode, JFChannelEnum channel){
        try {
            //根据用户Id查询所有
            Map<String, String> hgetAll = jedisCluster.hgetAll(RedisKey.getHashKeys(userId));
            //判断用户是否被禁用
            String accountUsable = hgetAll.get(RedisKey.USABLE);
            if ("1".equals(accountUsable)) { //用户不可用
                return JFResult.success(JFResultEnum.UNABLE, null);
            } else {
                //判断相应渠道的服务模式是否可用
                String jfKey = RedisKey.JF + getMode(userId,mode);
                //判断此模式是否可用
                String usable = hgetAll.get(RedisKey.getFieldUsable(jfKey));
                if (JFResultEnum.SUCCESS.equals(usable)) {
                    String limit = hgetAll.get(RedisKey.getFieldLimit(jfKey));
                    List<String> keys = Collections.singletonList(LIMIT_PREFIX + userId + jfKey);
                    List<String> args = Collections.singletonList(limit);
                    Number number = (Number) jedisCluster.eval(LUA_LIMIT_SCRIPT, keys, args);

                    if (number.intValue() == 1) {
                        //查询供应商编码
                        String supplier = hgetAll.get(RedisKey.getFieldSupplier(jfKey));
                        JFInfoModel data = new JFInfoModel();
                        data.setUserServiceAccount(userServiceAccount);
                        data.setSpecialCode(supplier);
                        return JFResult.success(JFResultEnum.SUCCESS, data);
                    } else {
                        return JFResult.success(JFResultEnum.NOQPS, null);
                    }
                } else if (JFResultEnum.NOCOUNT.equals(usable)) { //次数已用完
                    return JFResult.success(JFResultEnum.NOCOUNT, null);
                } else if (JFResultEnum.UNABLE.equals(usable)) {  //服务被禁用
                    return JFResult.success(JFResultEnum.UNABLE, null);
                } else if (JFResultEnum.EXPIRE.equals(usable)) {  //服务已过期
                    return JFResult.success(JFResultEnum.EXPIRE, null);
                } else {  //无此服务
                    return JFResult.success(JFResultEnum.NOACCOUNT, null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JFResult.success(JFResultEnum.SUCCESS, new JFInfoModel(userServiceAccount,""));
    }

    @Value("${omp.product.id.cardAuth:1290924200327917570}")
    private String cardAuthId;

    @Value("${omp.product.id.ctidAuth:1290924429026537473}")
    private String ctidAuthId;

    @Value("${omp.product.id.faceAuth:1290924364643971074}")
    private String cardFaceAuthId;

    private String getMode(String userId, String serviceCode) {
        //判断是否是 中台的客户
        String isOmp = jedisCluster.hget(RedisKey.getHashKeys(userId), RedisKey.OMP);
        if (RedisKey.OMP.equals(isOmp)) {
            if (serviceCode.equals(JFServiceEnum.CARD_AUTH.getMode())) {
                return cardAuthId;
            } else if (serviceCode.equals(JFServiceEnum.CTID_AUTH.getMode())) {
                return ctidAuthId;
            } else if (serviceCode.equals(JFServiceEnum.CARD_FACE_AUTH.getMode())) {
                return cardFaceAuthId;
            }
        }
        return serviceCode;
    }

    private String getMode(String userId, JFServiceEnum mode) {
        //判断是否是 中台的客户
        String isOmp = jedisCluster.hget(RedisKey.getHashKeys(userId), RedisKey.OMP);
        if (RedisKey.OMP.equals(isOmp)) {
            if (mode == JFServiceEnum.CARD_AUTH) {
                return cardAuthId;
            } else if (mode == JFServiceEnum.CTID_AUTH) {
                return ctidAuthId;
            } else if (mode == JFServiceEnum.CARD_FACE_AUTH) {
                return cardFaceAuthId;
            }
        }
        return mode.getMode();
    }

}
