package com.ahdms.billing.vo;

import com.ahdms.billing.model.UserInfo;

/**
 * @author qinxiang
 * @date 2020-12-01 9:44
 */
public class UserInfoRspVo extends UserInfo {

    private String whiteIp;

    public static UserInfoRspVo convert(UserInfo userInfo,String ips){
        UserInfoRspVo userInfoRspVo = new UserInfoRspVo();
        userInfoRspVo.setWhiteIp(ips);
        userInfoRspVo.setId(userInfo.getId());
        userInfoRspVo.setServiceAccount(userInfo.getServiceAccount());
        userInfoRspVo.setServicePwd(userInfo.getServicePwd());
        userInfoRspVo.setStatus(userInfo.getStatus());
        userInfoRspVo.setUsername(userInfo.getUsername());
        userInfoRspVo.setUserAccount(userInfo.getUserAccount());
        return userInfoRspVo;
    }

    public String getWhiteIp() {
        return whiteIp;
    }

    public void setWhiteIp(String whiteIp) {
        this.whiteIp = whiteIp;
    }
}
