package com.ahdms.billing.service;

import com.ahdms.billing.model.WhiteIp;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-11-27 10:56
 */
public interface WhiteIpService {

    int addUserWhiteIps(String userId,String ips);

    int updateUserWhiteIps(String userId,String ips);

    WhiteIp selectByUserIdAndIp(String userId,String ip);

    List<String> selectByUserId(String userId);

    boolean checkUserIdAndIp(String userId,String ip);

}
