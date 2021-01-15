/**
 * <p>Title: WxCtidController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年9月19日
 * @version 1.0
 */
package com.ahdms.ctidservice.web.controller;

import com.ahdms.ap.exception.tokenErrorException;
import com.ahdms.ap.model.AccessToken;
import com.ahdms.ap.model.PersonInfo;
import com.ahdms.ap.utils.IpUtil;
import com.ahdms.ap.utils.SortUtils;
import com.ahdms.ap.utils.TypeUtils;
import com.ahdms.api.model.AuthCtidValidDateReturnBean;
import com.ahdms.ctidservice.bean.UserCardInfoBean;
import com.ahdms.ctidservice.bean.WxDownCtidRequestBean;
import com.ahdms.ctidservice.bean.dto.WxCtidInputDTO;
import com.ahdms.ctidservice.bean.dto.WxImageInputDTO;
import com.ahdms.ctidservice.common.IpUtils;
import com.ahdms.ctidservice.common.RedisOpsClient;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.service.WxCtidService;
import com.ahdms.ctidservice.util.WxFaceApiUtils;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.util.SM3Digest;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: WxCtidController
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author qinxiang
 * @date 2019年9月19日
 */
@RestController
@RequestMapping(value = "V216", method = {RequestMethod.GET, RequestMethod.POST})
public class WxCtidController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WxCtidController.class);

    @Autowired
    private WxCtidService wxCtidService;

    @Autowired
    private TokenCipherService cipherService;

    @Autowired
    private RedisOpsClient redisOpsClient;

    @Autowired
    private WxFaceApiUtils wxFaceApiUtils;

    @RequestMapping("/wxctid/downCtid")
    public CtidResult downCtid(@RequestBody WxDownCtidRequestBean reqVO, HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cardName", reqVO.getCardName());
            map.put("cardNum", reqVO.getCardNum());
            map.put("cardStart", reqVO.getCardStart());
            map.put("cardEnd", reqVO.getCardEnd());
            map.put("authCode", reqVO.getAuthCode());
            map.put("token", reqVO.getToken());
            map.put("reqTime", String.valueOf(reqVO.getReqTime()));
            AccessToken checkUser = checkUser(reqVO.getToken(), request, reqVO.getReqTime(), map,
                    reqVO.getSignResult());
            if (checkUser != null) {
                String openID = checkUser.getOpenId();
                String cardName = reqVO.getCardName();
                String cardNum = reqVO.getCardNum();
                String cardStart = reqVO.getCardStart();
                String cardEnd = reqVO.getCardEnd();
                String authCode = reqVO.getAuthCode();
                CtidResult downCtid = wxCtidService.downCtid(openID, cardName, cardNum, cardStart, cardEnd, authCode, IpUtils.getIpAddress(request));
                return downCtid;
            } else {
                return CtidResult.error("请求参数错误！");
            }
        } catch (tokenErrorException e) {
            return CtidResult.tokenerror(e.getMessage());
        } catch (Exception e) {
            return CtidResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/wxctid/getCardInfo")
    public CtidResult<UserCardInfoBean> getCardInfo(HttpServletRequest request, @RequestBody WxCtidInputDTO reqVO) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", reqVO.getToken());
            map.put("reqTime", String.valueOf(reqVO.getReqTime()));
            AccessToken checkUser = checkUser(reqVO.getToken(), request, reqVO.getReqTime(), map,
                    reqVO.getSignResult());
            if (checkUser != null) {
                return wxCtidService.getCardInfo(checkUser.getOpenId());
            } else {
                return CtidResult.error("请求参数错误！");
            }
        } catch (tokenErrorException e) {
            return CtidResult.tokenerror(e.getMessage());
        } catch (Exception e) {
            return CtidResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/checkUserInfo")
    public CtidResult<PersonInfo> checkUserInfo(HttpServletRequest request, @RequestBody WxCtidInputDTO reqVO) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", reqVO.getToken());
            map.put("reqTime", String.valueOf(reqVO.getReqTime()));
            AccessToken checkUser = checkUser(reqVO.getToken(), request, reqVO.getReqTime(), map,
                    reqVO.getSignResult());
            if (checkUser != null) {
                return wxCtidService.checkUserInfo(checkUser.getOpenId());
            } else {
                return CtidResult.error("请求参数错误！");
            }
        } catch (tokenErrorException e) {
            return CtidResult.tokenerror(e.getMessage());
        } catch (Exception e) {
            return CtidResult.error(e.getMessage());
        }
    }

    @RequestMapping("/wxctid/getCtidValidDate")
    public CtidResult<AuthCtidValidDateReturnBean> getCtidValidDate(HttpServletRequest request,
                                                                    @RequestBody WxCtidInputDTO reqVO) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", reqVO.getToken());
            map.put("reqTime", String.valueOf(reqVO.getReqTime()));
            AccessToken checkUser = checkUser(reqVO.getToken(), request, reqVO.getReqTime(), map,
                    reqVO.getSignResult());
            if (checkUser != null) {
                return wxCtidService.getCtidValidDate(checkUser.getOpenId());
            } else {
                return CtidResult.error("请求参数错误！");
            }
        } catch (tokenErrorException e) {
            return CtidResult.tokenerror(e.getMessage());
        } catch (Exception e) {
            return CtidResult.error(e.getMessage());
        }
    }

    @RequestMapping("/wxctid/getQrCodeData")
    public CtidResult getQrCodeData(HttpServletRequest request, @RequestBody WxCtidInputDTO reqVO) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("token", reqVO.getToken());
            map.put("reqTime", String.valueOf(reqVO.getReqTime()));
            AccessToken checkUser = checkUser(reqVO.getToken(), request, reqVO.getReqTime(), map,
                    reqVO.getSignResult());
            if (checkUser != null) {
                return wxCtidService.getQrCodeData(checkUser.getOpenId());
            } else {
                return CtidResult.error("请求参数错误！");
            }
        } catch (tokenErrorException e) {
            return CtidResult.tokenerror(e.getMessage());
        } catch (Exception e) {
            return CtidResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getWxImage", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取微信小程序活体检测后图片", notes = "获取微信小程序活体检测后图片")
    public CtidResult getPicToken(HttpServletRequest request, @RequestBody WxImageInputDTO wxImageDTO) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("verifyResult", wxImageDTO.getVerifyResult());
            map.put("token", wxImageDTO.getToken());
            map.put("reqTime", String.valueOf(wxImageDTO.getReqTime()));
            AccessToken checkUser = checkUser(wxImageDTO.getToken(), request, wxImageDTO.getReqTime(), map,
                    wxImageDTO.getSignResult());
            if (checkUser != null) {
                String imageBase64 = wxFaceApiUtils.getImageBase64(wxImageDTO.getVerifyResult());
                if (null == imageBase64) {
                    return CtidResult.error("图片获取失败");
                } else {
                    return CtidResult.ok(imageBase64);
                }
            } else {
                return CtidResult.error("请求参数错误！");
            }
        } catch (Exception e) {
            return CtidResult.error(e.getMessage());
        }
    }

    private AccessToken checkUser(String tokenid, HttpServletRequest request, Long reqTime, Map<String, Object> map,
                                  String signResult) throws tokenErrorException, Exception {
        Date d = new Date();
        // SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
        // long l;
        try {
            long l = d.getTime() - reqTime;
            if (l > 300000) {
                throw new Exception("请求时间有误。");
            }
        } catch (ParseException e1) {
            throw new Exception("请求时间有误。");
        }

        String token = redisOpsClient.get(tokenid);
        if (StringUtils.isBlank(token)) {
            throw new tokenErrorException("无对应用户信息。");
        }
        AccessToken accessToken = cipherService.decodeAccess(token);
        if (null == accessToken) {
            throw new Exception("用户信息解密失败，请重试。");
        }
        map.put("serialNum", accessToken.getSerialNum());
        String result = SortUtils.formatUrlParam(map, "utf-8", false);
        SM3Digest sm3 = new SM3Digest();
        byte[] digit = sm3.sm3Digest(result.getBytes());

        String finalResult = TypeUtils.byteToHex(digit);

        if (!finalResult.equals(signResult)) {
            throw new Exception("用户请求信息有误，请重试");
        }
        String ip = IpUtil.getClinetIpByReq(request);
        LOGGER.debug("本次请求IP地址:{}" + ip);
        LOGGER.debug("获取token时的IP地址:{}" + accessToken.getIp());

        // if(!ip.equals(accessToken.getIp()) ){
        // throw new Exception("用户IP信息有变更，请重新登录。");
        // }

        return accessToken;

    }

}
