package com.ahdms.billing.service.impl;

import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.config.omp.RedisKey;
import com.ahdms.billing.dao.*;
import com.ahdms.billing.model.*;
import com.ahdms.billing.service.IOmpApiService;
import com.ahdms.billing.service.UserInfoService;
import com.ahdms.billing.utils.DateUtils;
import com.ahdms.billing.utils.MD5Utils;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.vo.SpecialLineVO;
import com.ahdms.billing.vo.omp.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qinxiang
 * @date 2020-07-24 14:11
 */
@Service
@Transactional
public class OmpApiServiceImpl implements IOmpApiService {
    private final static Logger logger = LoggerFactory.getLogger(OmpApiServiceImpl.class);
    private static String JF = "JF:";

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoMapper userInfoDao;

    @Autowired
    private UserServiceMapper userServiceMapper;

    @Autowired
    private UserServiceRecordMapper userServiceRecordMapper;

    @Autowired
    private ServiceLogMapper serviceLogMapper;

    @Autowired
    private ServiceInfoMapper serviceInfoMapper;

    @Autowired
    private ProviderInfoMapper providerInfoMapper;

    @Autowired
    private SpecialLineInfoMapper specialLineInfoMapper;

    @Override
    public Result<PageResult<ServiceLogVo>> getLogs(ServiceLogPageReqVo serviceLogPageReqVo) {

        PageHelper.startPage(serviceLogPageReqVo.getPageNum(), serviceLogPageReqVo.getPageSize());
        List<ServiceLogVo> records = serviceLogMapper.getLogs(serviceLogPageReqVo);
        PageInfo<ServiceLogVo> pageInfo = new PageInfo<>(records);
        PageResult<ServiceLogVo> pageResult = new PageResult<>();
        pageResult.setPageCount(pageInfo.getPages());
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalCount(pageInfo.getTotal());
        pageResult.setRecords(records);
        return Result.ok(pageResult);
    }

    @Override
    public Result<List<CustomerProductRspVo>> getCustPro() {
        List<UserService> all = userServiceMapper.findAllOmpUser();
        List<CustomerProductRspVo> result = new ArrayList<>();
        for (UserService userService : all) {
            CustomerProductRspVo rsp = new CustomerProductRspVo();
            rsp.setCustomerId(userService.getUserInfoId());
            rsp.setExpireTime(userService.getEndTime());
            rsp.setProductCode(userService.getServiceId());
            rsp.setRemainCount(userService.getServiceCount());
            ServiceInfo serviceInfo = userService.getServiceInfo();
            rsp.setProductId(serviceInfo.getId());
            result.add(rsp);
        }
        return Result.ok(result);
    }

    @Override
    public Result<String> pushCustomerInfo(CustomerInfoReqVo customerInfoReqVo) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(customerInfoReqVo.getCustomerId() + "");
        userInfo.setUserAccount(customerInfoReqVo.getUsername()); //用户登录账户
        userInfo.setUsername(customerInfoReqVo.getCompanyName());//用户企业名称
        userInfo.setServiceAccount(customerInfoReqVo.getSecretId());//服务账号
        userInfo.setServicePwd(customerInfoReqVo.getSecretKey());
        userInfo.setStatus(0);
        userInfo.setUserPwd(MD5Utils.stringToMD5("000000"));
        userInfo.setType(1);
        //根据Id查询
        UserInfo userInfoSelect = userInfoDao.selectByPrimaryKey(customerInfoReqVo.getCustomerId() + "");
        if(userInfoSelect == null){
            return userInfoService.addUser(userInfo,null);
        }else{
            userInfoDao.updateByPrimaryKeySelective(userInfo);
        }
        return Result.ok("success");
    }

    /**
     * 添加客户订单
     *
     * @param customerInfoReqVo
     * @return
     */
    @Override
    @Transactional
    public Result<String> pushCustomerOrder(CustomerOrderReqVo customerInfoReqVo) {
        //1.添加客户服务
        UserService userService = convert(customerInfoReqVo);
        insertOrUpdateUserService(userService);
        //2.添加订单记录
        UserServiceRecord userServiceRecord = convert(userService);
        userServiceRecordMapper.insertSelective(userServiceRecord);
        return Result.ok("添加订单成功!");
    }

    @Override
    public Result<String> pushProductInfo(ProductInfoReqVo productInfoReqVo) {
        //添加产品信息
        ServiceInfo serviceInfo = new ServiceInfo();
//        serviceInfo.setServiceEncode(productInfoReqVo.getProductCode());
        //将产品ID当产品编码存入数据库
        serviceInfo.setServiceEncode(productInfoReqVo.getProductId() + "");
        serviceInfo.setServiceName(productInfoReqVo.getProductName());
        serviceInfo.setId(productInfoReqVo.getProductId() + "");
        serviceInfo.setServiceType("1");
        serviceInfo.setSpecialLineCode(productInfoReqVo.getDependCode());
        ServiceInfo selectByPrimaryKey = serviceInfoMapper.selectByPrimaryKey(productInfoReqVo.getProductId() + "");
        if(selectByPrimaryKey == null){
            serviceInfoMapper.insertSelective(serviceInfo);
        }else{
            serviceInfoMapper.updateByPrimaryKeySelective(serviceInfo);
        }

        //添加供应商信息
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setId(productInfoReqVo.getProviderCode());
        providerInfo.setProviderName(productInfoReqVo.getProviderName());
        ProviderInfo providerInfo1 = providerInfoMapper.selectByPrimaryKey(productInfoReqVo.getProviderCode());
        if(providerInfo1 == null){
            providerInfoMapper.insertSelective(providerInfo);
        }else{
            providerInfoMapper.updateByPrimaryKeySelective(providerInfo);
        }

        //添加依赖方信息
        if(StringUtils.isNotBlank(productInfoReqVo.getDependCode())){
            SpecialLineInfo specialLineInfo = new SpecialLineInfo();
            specialLineInfo.setCode(productInfoReqVo.getDependCode());
            specialLineInfo.setName(productInfoReqVo.getDependName());
            specialLineInfo.setProviderId(providerInfo.getId());
            specialLineInfo.setId(UUIDGenerator.getUUID());
            SpecialLineVO specialLineVO = specialLineInfoMapper.selectByCode(productInfoReqVo.getDependCode());
            if(specialLineVO == null){
                specialLineInfoMapper.insertSelective(specialLineInfo);
            }else{
                specialLineInfo.setId(specialLineVO.getId());
                specialLineInfoMapper.updateByPrimaryKeySelective(specialLineInfo);
            }
        }
        return Result.ok("success");
    }

    public UserService convert(CustomerOrderReqVo customerInfoReqVo) {
        UserService userService = new UserService();
        userService.setId(UUIDGenerator.getUUID());
        userService.setServiceCount(customerInfoReqVo.getUseCount()); //产品次数

        ServiceInfo serviceInfo = serviceInfoMapper.selectByPrimaryKey(customerInfoReqVo.getProductId()+"");
        userService.setSpecialCode(serviceInfo.getSpecialLineCode()); //产品依赖方编码
        userService.setTps(20);
        userService.setServiceId(customerInfoReqVo.getProductId()+""); //产品ID
        userService.setUserInfoId(customerInfoReqVo.getCustomerId() + ""); //客户ID
        userService.setIsExpired(0);
        userService.setCreateTime(new Date());
        userService.setEndTime(DateUtils.parse(customerInfoReqVo.getExpireTime(),DateUtils.DATE_FULL_STR));
        userService.setServiceCount(customerInfoReqVo.getUseCount());
        userService.setSpecialCode(serviceInfo.getSpecialLineCode());

        return userService;
    }

    public UserServiceRecord convert(UserService userService) {
        UserServiceRecord userServiceRecord = new UserServiceRecord();
        userServiceRecord.setUserService(userService.getId());
        userServiceRecord.setCount(userService.getServiceCount());
        userServiceRecord.setCreateTime(userService.getCreateTime());
        userServiceRecord.setEndTime(userService.getEndTime());
        userServiceRecord.setOperationUser("omp-user");//预留 管理系统管理员
        userServiceRecord.setTps(userService.getTps());
        userServiceRecord.setId(UUIDGenerator.getUUID());
        userServiceRecord.setCreateTime(new Date());
//        userServiceRecord.setSpecialCode(userService.getSpecialCode());
//        userServiceRecord.setProviderId(userService.getProvider());
        return userServiceRecord;
    }

    public void insertOrUpdateUserService(UserService userService) {
            //根据用户ID,渠道编码,服务编码 判断此服务是否已有
            UserService _userService = userServiceMapper.selectByUserIdAndServiceId(userService.getUserInfoId(),userService.getServiceId());
            if(null != _userService){
                _userService.setEndTime(userService.getEndTime());
                _userService.setIsExpired(0);
                _userService.setSpecialCode(userService.getSpecialCode());
                _userService.setProvider(userService.getProvider());
                _userService.setServiceCount(_userService.getServiceCount() + userService.getServiceCount());
                _userService.setTps(userService.getTps());
                userServiceMapper.updateByPrimaryKeySelective(_userService);
            }else{
                userService.setIsExpired(0);
                userServiceMapper.insertSelective(userService);
            }

            //信息存入redis
            insertToRedis(userService);

    }

    public void insertToRedis(UserService userService){
        UserInfo user = userInfoDao.selectByPrimaryKey(userService.getUserInfoId());

        String hgetKey = RedisKey.getHashKeys(userService.getUserInfoId());
        // 服务信息添加至redis
        String fieldPrekey = new StringBuffer(JF).append(userService.getServiceId()).toString();
        Map<String, String> results = jedisCluster.hgetAll(hgetKey);
        results.put(RedisKey.getFieldUsable(fieldPrekey), "0");
        results.put(RedisKey.getFieldLimit(fieldPrekey), userService.getTps().toString());

        if(StringUtils.isNotBlank(userService.getSpecialCode())){
            results.put(RedisKey.getFieldSupplier(fieldPrekey), userService.getSpecialCode());
        }
        results.put(RedisKey.USABLE, user.getStatus().toString());
        //添加个key 判断是由中台添加的订单
        results.put(RedisKey.OMP,RedisKey.OMP);

        String counts = jedisCluster.hget(hgetKey, RedisKey.getFieldCount(fieldPrekey));
        if (null != counts) {
            results.put(RedisKey.getFieldCount(fieldPrekey), Integer.toString(Integer.parseInt(counts) + userService.getServiceCount()));
        } else {
            results.put(RedisKey.getFieldCount(fieldPrekey), userService.getServiceCount().toString());
        }

        jedisCluster.hmset(hgetKey, results);
    }

    private String getKey(UserInfo user, UserService userService) {
        StringBuffer key = new StringBuffer(); // 拼装redis key
        key.append(JF).append(userService.getServiceId());
        return key.toString();
    }
}
