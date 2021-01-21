package com.ahdms.billing.service.impl;

import com.ahdms.billing.dao.WhiteIpMapper;
import com.ahdms.billing.model.WhiteIp;
import com.ahdms.billing.service.WhiteIpService;
import com.ahdms.billing.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author qinxiang
 * @date 2020-11-27 10:56
 */
@Service
public class WhiteIpServiceImpl implements WhiteIpService {

    @Autowired
    private WhiteIpMapper whiteIpMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUserWhiteIps(String userId, String ips) {
        if (StringUtils.isNotBlank(ips)) {
            String[] ipsArr = StringUtils.split(ips, ",");
            for (String ip : ipsArr) {
                WhiteIp whiteIp = new WhiteIp(UUIDGenerator.getUUID(), userId, ip);
                whiteIpMapper.insertSelective(whiteIp);
            }
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserWhiteIps(String userId, String ips) {
        if(StringUtils.isNotBlank(ips)){
            whiteIpMapper.deleteAllByUserId(userId);
            addUserWhiteIps(userId, ips);
        }
        return 0;
    }

    @Override
    public WhiteIp selectByUserIdAndIp(String userId, String ip) {
        List<String> whiteIps = whiteIpMapper.queryAllByUserId(userId);
        if (whiteIps == null || whiteIps.size() == 0) {
            return new WhiteIp();
        }
        return whiteIpMapper.selectByUserIdAndIp(userId, ip);
    }

    @Override
    public boolean checkUserIdAndIp(String userId, String ip) {
        List<String> whiteIps = whiteIpMapper.queryAllByUserId(userId);
        if(whiteIps == null || whiteIps.size() == 0 ){
            return true;
        }
        return whiteIps.stream().anyMatch(ip::equals);
//        return Optional.ofNullable(whiteIps)
//                .map(whiteIps1 -> whiteIps1.stream().anyMatch(ip::equals)).orElse(true);
    }

    @Override
    public List<String> selectByUserId(String userId) {
        return whiteIpMapper.queryAllByUserId(userId);
    }
}
