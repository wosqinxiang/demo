package com.ahdms.billing.controller;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.config.omp.RedisKey;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.dao.UserServiceMapper;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.UserService;
import com.ahdms.billing.task.FindExpiredJob;
import com.ahdms.billing.task.ReportUserCountJob;
import com.ahdms.billing.task.SyncCountJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class TestReportController {

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private UserInfoMapper userInfoDao;

    @Autowired
    private UserServiceMapper userServiceMapper;

    @Autowired
    private ReportUserCountJob job;

    @Autowired
    private FindExpiredJob findJob;

    @Autowired
    private SyncCountJob syncCountJob;

    @RequestMapping("testDataReportJob")
    public Result testJob(@RequestParam String date) {
        job.execute(date);
        return Result.ok("success");
    }

    @RequestMapping("/testExpiredJob")
    public Result testExpiredJob() {
        findJob.execute(null);
        return Result.ok("success");
    }

    @RequestMapping("/testSyncCountJob")
    public Result testSyncCountJob() {
        syncCountJob.execute(null);
        return Result.ok("success");
    }

    @Value("${omp.product.id.cardAuth:1290924200327917570}")
    private String cardAuthId;

    @Value("${omp.product.id.ctidAuth:1290924429026537473}")
    private String ctidAuthId;

    @Value("${omp.product.id.faceAuth:1290924364643971074}")
    private String cardFaceAuthId;

    @RequestMapping(value = "testSyncDataToRedis", method = {RequestMethod.GET, RequestMethod.POST})
    public Result testDataReport() {
        //1. 获取所有的客户服务
        List<UserService> all = userServiceMapper.findAll();
        for (UserService userService : all) {
            insertToRedis(userService);
        }
        return Result.ok("success");
    }

    private boolean equalsServiceCode(String serviceCode){
        if(serviceCode.equals(cardAuthId) || serviceCode.equals(ctidAuthId) || serviceCode.equals(cardFaceAuthId)){
            return true;
        }
        return false;
    }

    public void insertToRedis(UserService userService) {
        UserInfo user = userInfoDao.selectByPrimaryKey(userService.getUserInfoId());
        String hgetKey = RedisKey.getHashKeys(userService.getUserInfoId());
        // 服务信息添加至redis
        String fieldPrekey = new StringBuffer(RedisKey.JF).append(userService.getServiceId()).toString();
        Map<String, String> results = jedisCluster.hgetAll(hgetKey);
        results.put(RedisKey.getFieldUsable(fieldPrekey), "0");
        results.put(RedisKey.getFieldLimit(fieldPrekey), userService.getTps().toString());
        if (StringUtils.isNotBlank(userService.getSpecialCode())) {
            results.put(RedisKey.getFieldSupplier(fieldPrekey), userService.getSpecialCode());
        }
        results.put(RedisKey.USABLE, user.getStatus().toString());
        //添加个key 判断是由中台添加的订单
        if(equalsServiceCode(userService.getServiceId())){
            results.put(RedisKey.OMP,RedisKey.OMP);
        }
        results.put(RedisKey.getFieldCount(fieldPrekey), userService.getServiceCount().toString());

        jedisCluster.hmset(hgetKey, results);
    }


}
