package com.ahdms.ap.service;

import com.ahdms.ap.model.PersonInfo;
import com.ahdms.ap.vo.WxMiniProgramVo;

import java.util.List;

/**
 * WxAuthLoginService class
 *
 * @author hetao
 * @date 2019/08/01
 */
public interface WxAuthLoginService {
    /**
     * 微信小程序授权接口
     *
     * @param code 小程序临时登录凭证
     * @param appId 小程序ID
     * @param secretKey 小程序密钥
     * @param ip 
     * @return WxMiniProgramVo
     */
    WxMiniProgramVo wxAuthLogin(String code, String appId, String secretKey, String ip);

    /**
     * 根据openid查找用户信息
     *
     * @param openid 用户登录唯一标识码
     * @return List
     */
    List<PersonInfo>  selectByOpenId(String openid);
    PersonInfo  selectByOpenID(String openid);

	WxMiniProgramVo oldwxAuthLogin(String code, String appId, String secretKey);
}