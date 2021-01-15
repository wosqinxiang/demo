/**
 * <p>Title: CtidManageServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年8月14日
 * @version 1.0
 */
package com.ahdms.ctidservice.service.impl;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.dao.PersonInfoDao;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ap.model.PersonInfo;
import com.ahdms.api.model.AuthCtidValidDateReturnBean;
import com.ahdms.api.model.CreateCodeRequestReturnBean;
import com.ahdms.api.model.ValidateCodeRequestReturnBean;
import com.ahdms.api.model.*;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidMessage;
import com.ahdms.ctidservice.bean.*;
import com.ahdms.ctidservice.bean.dto.*;
import com.ahdms.ctidservice.common.*;
import com.ahdms.ctidservice.config.RabbitMqSender;
import com.ahdms.ctidservice.constants.CtidConstants;
import com.ahdms.ctidservice.constants.ErrorCodeConstants;
import com.ahdms.ctidservice.db.dao.CtidInfosMapper;
import com.ahdms.ctidservice.db.dao.CtidinfoMapper;
import com.ahdms.ctidservice.db.dao.KeyInfoMapper;
import com.ahdms.ctidservice.db.model.CtidInfos;
import com.ahdms.ctidservice.db.model.Ctidinfo;
import com.ahdms.ctidservice.db.model.KeyInfo;
import com.ahdms.ctidservice.service.*;
import com.ahdms.ctidservice.util.*;
import com.ahdms.ctidservice.util.ctid.ReservedDataUtils;
import com.ahdms.ctidservice.vo.*;
import com.ahdms.identification.utils.TypeUtils;
import com.ahdms.jf.client.JFClient;
import com.ahdms.jf.model.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: CtidManageServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author qinxiang
 * @date 2019年8月14日
 */
@Service
public class CtidManageServiceImpl implements CtidManageService {
    Logger logger = LoggerFactory.getLogger(CtidManageServiceImpl.class);

    private final static String TOKEN_KEY = "PID_TOKEN";

    @Autowired
    private CtidPCManageService ctidPCManageService;

    @Autowired
    private CtidPidInfoService ctidPidInfoService;

    @Autowired
    private TokenCipherService cipherSer;

    @Autowired
    private DownCtidService downCtidService;

    @Autowired
    private AuthCtidService authCtidService;

    @Autowired
    private AuthIdCardService authIdCardService;

    @Autowired
    private CreateCodeService createCodeService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private PersonInfoDao personDao;

    @Autowired
    private CtidInfosMapper ctidInfosMapper;

    @Autowired
    private CtidinfoMapper infoMapper;

    @Autowired
    private KeyInfoMapper keyInfoMapper;

    @Autowired
    private RabbitMqSender mqSend;

    @Autowired
    private CtidLogService ctidLogService;

    @Autowired
    private RedisOpsClient redisOpsClient;

    @Autowired
    private JFClient jfClient;

    @Autowired
    private CtidPhoneService ctidPhoneService;

    @Autowired
    private CtidRspUtils ctidRspUtils;

    @Autowired
    private VerifySignService verifySignService;

    @Override
    public String authCtidRequest(Map<String, String> params) {
        try {
            String appId = params.get("appID");
            String signStr = params.get("sign");
            // 数字验签
            if (StringUtils.isNotBlank(signStr) && StringUtils.isNotBlank(appId)) {
                boolean verifySign = verifySignService.verifySign(appId, signStr, params);
                if (!verifySign) {
                    return getDefaultAuthErrorString("请求消息签名验证失败");
                }
            }
            String _authMode = params.get("mode");
            if (StringUtils.isBlank(_authMode)) {
                return getDefaultAuthErrorString("认证模式不能为空！mode:" + _authMode);
            }
            String authMode = _authMode.toLowerCase();
            String businessSerialNumber = params.get("bsn"); // 网上凭证文件中的数据
            String photoData = params.get("faceData"); // 照片数据
            String authCode = params.get("vCode"); // 认证码数据
            String userDataStr = params.get("userData"); // 身份证信息
            String idcardAuthData = params.get("idCheck"); // id验证数据
            String id = params.get("ctidIndex"); // 用户ID

            String encodeReservedData = null;
            if (StringUtils.isNotBlank(userDataStr)) {
                try {
                    // 解密用户数据，提取身份信息
                    byte[] userData = cipherSer.decode(Base64Utils.decode(userDataStr));
                    // 加密身份保留数据四项
                    ReservedDataEntity reservedDataAuth = ReservedDataUtils.getReservedDataEntity(userData);
                    // 加密保留数据
//					String reservedDataStr = JSONObject.fromObject(reservedDataAuth).toString();
//					logger.debug("保留数据:" + reservedDataStr);
//					encodeReservedData = p7Tool.encodeLocal(reservedDataStr);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return getDefaultAuthErrorString("解密身份证信息失败");
                }
            }

            CtidResult<CtidAuthReturnBean> authCtidRequest = authCtidService.authCtidRequest(businessSerialNumber,
                    authMode, photoData, idcardAuthData, authCode, encodeReservedData,"");

            Map<String, String> map = new HashMap<String, String>();
            CtidInfos ctidInfos = ctidInfosMapper.selectByPrimaryKey(id);
            if (0 == authCtidRequest.getCode()) {
                CtidAuthReturnBean data = authCtidRequest.getData();
                if ("true".equals(data.getAuthResult())) {
                    map.put("code", "0");
                    map.put("result", "0");
                    map.put("pid", data.getPid());
                    // pid入库
                    try {

                        if (ctidInfos != null && StringUtils.isBlank(ctidInfos.getPid())) {
                            ctidInfos.setPid(data.getPid());
                            ctidInfosMapper.updateByPrimaryKeySelective(ctidInfos);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String codeStr = data.getAuthCode();
                    int code = AuthResultFinder.findAuthResultCode(codeStr);
                    if (code == ErrorCodeConstants.AUTH_CTID_EXPIRE_CODE) {
                        Date nowDate = new Date();
                        String cardEndDateStr = ctidInfos.getCardEndDate();
                        if (!"00000000".equals(cardEndDateStr)) {
                            Date cardEndDate = new SimpleDateFormat("yyyyMMdd").parse(cardEndDateStr);
                            if (nowDate.after(cardEndDate)) {
                                code = ErrorCodeConstants.AUTH_USERCARD_EXPIRE_CODE;
                            }
                        }
                    }
                    map.put("code", "" + code);
                    map.put("result", "1");
                    map.put("message", data.getAuthDesc());
                }
            } else {
                map.put("code", "1");
                map.put("result", "1");
                map.put("message", authCtidRequest.getMessage());
            }

            try {
                AuthRecord authRecord = new AuthRecord();

                authRecord.setId(UUIDGenerator.getSerialId());
                authRecord.setAuthType(CtidConstants.AUTH_RECORD_TYPE_SDK);
                authRecord.setAuthResult(Integer.parseInt(map.get("result")));
                authRecord.setCreateTime(new Date());
                authRecord.setCtidType(Contents.CTID_TYPE_ONE);
                authRecord.setIdcard(null == ctidInfos ? "":ctidInfos.getCardNum());
                authRecord.setName(null == ctidInfos ? "":ctidInfos.getCardName());
                authRecord.setPic(photoData);
                authRecord.setAuthDesc(map.get("message"));
                authRecord.setServerAccount(appId);
                authRecord.setSerialNum(businessSerialNumber);

                mqSend.sendAuthRecord(authRecord);

            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }

            // 返回签名的结果
            String signedRet = getSignedRet(map);
            return signedRet;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return getDefaultAuthErrorString("服务器繁忙！请重试！");
    }


    @Override
    public String getDefaultAuthErrorString(String message) {
        return getAuthErrorString("" + AuReturn.AUTH_ERROR_UNKNOWN, message);
    }

    private String getAuthErrorString(String code, String errMsg) {
        Map<String, String> retParams = SignAssistUtils.getErrorDownloadContent(code, errMsg);

        return getSignedRet(retParams);
    }

    private String getSignedRet(Map<String, String> retParams) {
        // 构造返回值
        String source = SignAssistUtils.getSignContent(retParams);
        // 签名
        byte[] sign = null;
        try {
            sign = cipherSer.dataSign(source.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String retSign = TypeUtils.binaryToHexString(sign);
        retParams.put("sign", retSign);
        String data = SignAssistUtils.mapToJsonString(retParams);
        logger.info("返回数据: " + data);
        return SignAssistUtils.mapToJsonString(retParams);
    }

    @Override
    public CtidResult downCtidApply(CtidRequestBean dcaBean) {
        // 验签
        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String organizeId = keyInfo.getOrganizeId();
            String bizPackageStr = dcaBean.getBizPackage();
            DownCtidApplyBean bizPackage = JsonUtils.json2Bean(bizPackageStr, DownCtidApplyBean.class);

            // 发起网证下载申请，去CTID平台下载网证
            CtidResult downCtidApply = downCtidService.downCtidApply(bizPackage.getAuthMode(), organizeId);
            if (downCtidApply.getCode() == 0) {
                return CtidResult.successSign(downCtidApply.getData(), key);
            }
            return downCtidApply;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("error");
    }

    @Override
    public CtidResult downloadCtidRequest(CtidRequestBean dcaBean,String ip) {
        CtidResult result = new CtidResult<>();
        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String sdkCode = keyInfo.getSdkCode();


            logger.debug("下载请求数据:" + dcaBean.getBizPackage());
            DownCtidRequestBean bizPackage = JsonUtils.json2Bean(dcaBean.getBizPackage(), DownCtidRequestBean.class);

            String bsn = bizPackage.getBsn();
            String appId = bizPackage.getAppId();
//            String ctidIndex = bizPackage.getCtidIndex();
            String userDataStr = bizPackage.getUserData();
            String userDataJson = bizPackage.getUserDataJson();

            //获取身份信息
            CtidResult<SfxxBean> sfxxBeanCtidResult = getSfxxBean(userDataStr, userDataJson);
            if (sfxxBeanCtidResult.getCode() != 0) {
                return sfxxBeanCtidResult;
            }
            SfxxBean sfxxBean = sfxxBeanCtidResult.getData();

            JFServiceEnum jfServiceEnum = jfServiceEnum(bizPackage.getMode());
            //调用计费接口

            JFResult<JFInfoModel> jf = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), jfServiceEnum, JFChannelEnum.APP_SDK,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jf.getMessage(),keyInfo.getAppId());
                return CtidResult.error(jf.getMessage());
            }
            JFInfoModel jfData = jf.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();
            CtidResult<CtidMessage> downCtidRequest = downCtidService.downCtidRequest(bsn, bizPackage.getMode(),
                    bizPackage.getFaceData(), bizPackage.getIdCheck(), bizPackage.getvCode(), sfxxBean.getName(), sfxxBean.getIdCard(), sfxxBean.getStartDate(), sfxxBean.getEndDate(), specialCode);

            jfClient.send(serverAccount, jfServiceEnum, JFChannelEnum.APP_SDK, downCtidRequest.getCode(), downCtidRequest.getMessage(), bsn, specialCode, keyInfo.getAppId());

            if (0 == downCtidRequest.getCode()) {
                // 下载成功
                CtidMessage data = downCtidRequest.getData();
                String id = insertCtid(sfxxBean, data.getGrsfbs(),specialCode);
                //返回新对象
                CtidResponseVo ctidResponseVo = ctidRspUtils.getCtidRsp(sfxxBean.getIdCard(), sdkCode, data.getCtidInfo(), bsn, id);
                return CtidResult.successSign(ctidResponseVo, key);
            } else if (2 == downCtidRequest.getCode()) { //网证未开通或者已吊销
                //保存用户信息
                String ctidIndex = insertCtid(sfxxBean,null,specialCode);
                result.setCode(downCtidRequest.getCode());
                result.setMessage(downCtidRequest.getMessage());
                //返回流水号
                CtidResponseVo ctidResponseVo = ctidRspUtils.getCtidRspBsn(ctidIndex, bsn);
                result.setData(JsonUtils.bean2Json(ctidResponseVo));
                return result;
            } else {
                result.setCode(downCtidRequest.getCode());
                result.setMessage(downCtidRequest.getMessage());

                result.setData(sfxxBean);
                return result;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("下载失败!");
    }

    private JFServiceEnum jfServiceEnum(String mode) {
        if (CtidConstants.DOWN_0X13_MODE.equals(mode)) {
            return JFServiceEnum.CTID_DOWN_0X13;
        }
        if (CtidConstants.DOWN_0X14_MODE.equals(mode)) {
            return JFServiceEnum.CTID_DOWN_0X14;
        }
        return JFServiceEnum.CTID_DOWN;
    }

    private CtidResult<KeyInfo> validSignData(CtidRequestBean dcaBean) {
        try {
            String sign = dcaBean.getSign();
            String bizPackageStr = dcaBean.getBizPackage();
            JSONObject jsonObject = JSONObject.fromObject(bizPackageStr);
            String appId = jsonObject.getString("appId");
            String appPackage = null;
            try {
                appPackage = jsonObject.getString("appPackage");
            } catch (Exception e) {
//				logger.error(e.getMessage());
            }
            KeyInfo keyInfo = null;
            if (appPackage == null) {
                keyInfo = keyInfoMapper.selectByAppId(appId);
            } else {
                keyInfo = keyInfoMapper.selectByAppIdAndAppPackage(appId, appPackage);
            }
            if (null != keyInfo) {
                String key = keyInfo.getSecretKey();
                boolean verify = MD5Utils.verify(bizPackageStr, key, sign);
                if (verify) {
                    return CtidResult.ok(keyInfo);
                } else {
                    logger.error("MD5验证失败！");
                    return CtidResult.error("请求数据验签失败!");
                }
            } else {
                logger.error("AppId与APP包名不匹配！");
                return CtidResult.error("AppId与APP包名不匹配！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return CtidResult.error("请求数据验签失败!");
    }

    @Override
    public CtidResult authCtidApply(CtidRequestBean dcaBean) {
        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            String key = validSignResult.getData().getSecretKey();
            String organizeId = validSignResult.getData().getOrganizeId();
            AuthCtidApplyBean biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), AuthCtidApplyBean.class);
            // 查找对应的 密钥
            String authMode = biz.getAuthMode();
            CtidResult authCtidApply = authCtidService.authCtidApply(authMode, organizeId);

            if (authCtidApply.getCode() == 0) {
                return CtidResult.successSign(authCtidApply.getData(), key);
            }
            return authCtidApply;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("认证申请失败！");
    }

    @Override
    public CtidResult authCtidRequest(CtidRequestBean dcaBean,String ip) {
        CtidResult result = new CtidResult();
        try {
            // 验签
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String sdkCode = keyInfo.getSdkCode();

            AuthCtidRequestBean biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), AuthCtidRequestBean.class);

            String appId = biz.getAppId();
            String bsn = biz.getBsn();
            String ctidIndex = biz.getCtidIndex();
            String faceData = biz.getFaceData();
            String idCheck = biz.getIdCheck();
            String mode = biz.getMode();

            CtidInfos ctidInfos = ctidInfosMapper.selectByPrimaryKey(ctidIndex);

            // 通过ctidIndex 以及 appId 查询数据库网证是否过期
//            Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidIndex, appId);
            //核验网证有效期
//            CtidResult authCtidExpire = validateCtid(ctidinfo, ctidInfos);
//            if (authCtidExpire != null) {
//                return authCtidExpire;
//            }

            //调用计费接口
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), JFServiceEnum.CTID_AUTH, JFChannelEnum.APP_SDK,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();
            CtidResult<CtidAuthReturnBean> authCtidRequest = authCtidService.authCtidRequest(bsn, mode, faceData,
                    idCheck, null, null, specialCode);
//            Map<String, String> map = new HashMap<String, String>();
            if (0 == authCtidRequest.getCode()) {
                CtidAuthReturnBean data = authCtidRequest.getData();
                if ("true".equals(data.getAuthResult())) {
                    // pid入库
                    String pid = data.getPid();
                    ctidPidInfoService.insertOrUpdate(ctidIndex,pid,specialCode);
//
                    //认证完后返回
                    CtidResponseVo ctidRspPid = ctidRspUtils.getCtidRspPid(ctidIndex,pid, sdkCode, null, bsn,specialCode);
                    ctidRspPid.setPid(pid);
                    result = CtidResult.successSign(ctidRspPid, key);
                } else {
                    String authCode = data.getAuthCode();
                    int code = AuthResultFinder.findAuthResultCode(authCode);
                    result = CtidResult.error(code, data.getAuthDesc());
                }
            } else {
                result = CtidResult.error(authCtidRequest.getMessage());
            }
            try {
                AuthRecord authRecord = new AuthRecord();

                authRecord.setId(UUIDGenerator.getSerialId());
                authRecord.setAuthType(CtidConstants.AUTH_RECORD_TYPE_SDK);
                authRecord.setAuthResult(0 == result.getCode() ? 0 : 1);
                authRecord.setCreateTime(new Date());
                authRecord.setCtidType(Contents.CTID_TYPE_ONE);
                authRecord.setIdcard(ctidInfos != null ? ctidInfos.getCardNum() : "");
                authRecord.setName(ctidInfos != null ? ctidInfos.getCardName() : "");
                authRecord.setPic(faceData);
                authRecord.setAuthDesc(result.getMessage());
                authRecord.setServerAccount(keyInfo.getServerAccount());
                authRecord.setSerialNum(bsn);

                mqSend.sendAuthRecord(authRecord);
            } catch (Exception e) {
                logger.error(e.getMessage(), "认证记录入库失败!");
            }

            jfClient.send(serverAccount, JFServiceEnum.CTID_AUTH, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), bsn, specialCode, keyInfo.getAppId());

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return CtidResult.error("服务器异常!请重试!");
    }

    private CtidResult validateCtid(Ctidinfo ctidinfo, CtidInfos ctidInfos) throws ParseException {
        if (null != ctidinfo) {
            Date nowDate = new Date();
            String ctidValidDate = ctidinfo.getCtidValidDate();
            Date ctidDownTime = ctidinfo.getCtidDownTime();
            String cardEndDateStr = ctidInfos.getCardEndDate();
            if (StringUtils.isNotBlank(ctidValidDate)) {
                Date validDate = new SimpleDateFormat("yyyyMMdd").parse(ctidValidDate);
                if (nowDate.after(validDate)) { // 说明网证数据已经过期
                    // 判断网证过期的原因 身份证过期 网证下载超过6个月
                    // 是否是身份证过期
                    CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                    if (authCtidExpire.getCode() != 0) {
                        return authCtidExpire;
                    }
                }
            } else {
                CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                if (authCtidExpire.getCode() != 0) {
                    return authCtidExpire;
                }
            }
        }
        return null;
    }

    @Override
    public CtidResult createCodeApply(CtidRequestBean dcaBean) {
        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String organizeId = keyInfo.getOrganizeId();
            String key = keyInfo.getSecretKey();

            CreateCodeApplyBean bean = JsonUtils.json2Bean(dcaBean.getBizPackage(), CreateCodeApplyBean.class);

            String applyData = bean.getApplyData();
            CtidResult result = createCodeService.createCodeApply(applyData, organizeId);
            if (0 == result.getCode()) {
                return CtidResult.successSign(result.getData(), key);
            }
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("服务器异常!请重试!");
    }

    @Override
    public CtidResult createCodeRequest(CtidRequestBean dataBean,String ip) {

        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dataBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String organizeId = keyInfo.getOrganizeId();
            String appId = keyInfo.getAppId();

            CreateCodeRequestBean bean = JsonUtils.json2Bean(dataBean.getBizPackage(), CreateCodeRequestBean.class);

            String bsn = bean.getBsn();
            String checkData = bean.getCheckData();

            //调用计费系统
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), JFServiceEnum.CTID_QRCODE_CREATE, JFChannelEnum.APP_SDK,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),appId);
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();

            CtidResult<CreateCodeRequestReturnBean> result = createCodeService.createCodeRequest(bsn, checkData, CtidConstants.CODE_IS_PIC, specialCode);

            jfClient.send(serverAccount, JFServiceEnum.CTID_QRCODE_CREATE, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), bsn, specialCode, appId);

            if (0 == result.getCode()) {
                return CtidResult.successSign(result.getData(), key);
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CtidResult.error("服务器异常!请重试!");
    }

    @Override
    public CtidResult validateCodeApply(CtidRequestBean dataBean) {
        try {

            CtidResult<KeyInfo> validSignResult = validSignData(dataBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String organizeId = keyInfo.getOrganizeId();
            ValidateCodeApplyBean bean = JsonUtils.json2Bean(dataBean.getBizPackage(), ValidateCodeApplyBean.class);

            String applyData = bean.getApplyData();
            Integer authMode = bean.getMode();

            CtidResult<ApplyReturnBean> result = validateCodeService.validateCodeApply(applyData, authMode, organizeId);
            if (0 == result.getCode()) {
                return CtidResult.successSign(result.getData(), key);
            }
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("服务器异常!请重试!");
    }

    @Override
    public CtidResult validateCodeRequest(CtidRequestBean dataBean,String ip) {

        try {

            CtidResult<KeyInfo> validSignResult = validSignData(dataBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String appId = keyInfo.getAppId();
            String sdkCode = keyInfo.getSdkCode();

            ValidateCodeRequestBean bean = JsonUtils.json2Bean(dataBean.getBizPackage(), ValidateCodeRequestBean.class);

            String bsn = bean.getBsn();
            String faceData = bean.getFaceData();
            String idCheck = bean.getIdCheck();
            Integer mode = bean.getMode();
            String vCode = bean.getvCode();

            //调用计费系统
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), JFServiceEnum.CTID_QRCODE_AUTH, JFChannelEnum.APP_SDK,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),appId);
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();

            CtidResult<ValidateCodeRequestReturnBean> result = validateCodeService.validateCodeRequest(bsn, mode, vCode,
                    faceData, idCheck, specialCode);

            jfClient.send(serverAccount, JFServiceEnum.CTID_QRCODE_AUTH, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), bsn, specialCode, appId);

            if (0 == result.getCode()) {
                ValidateCodeRequestReturnBean data = result.getData();
                CtidResponseVo ctidRsp = ctidRspUtils.getCtidRspPid(null,data.getPid(), sdkCode, null, bsn,specialCode);
                return CtidResult.successSign(ctidRsp, key);
            }
            return result;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("服务器异常!请重试!");
    }

    @Override
    public CtidResult saveCtidNum(CtidRequestBean dcaBean) {
        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            logger.info("保存网证编号请求数据:" + dcaBean.getBizPackage());
            SaveCtidNumBean bizPackage = JsonUtils.json2Bean(dcaBean.getBizPackage(), SaveCtidNumBean.class);

            String appId = bizPackage.getAppId();
            String ctidIndex = bizPackage.getCtidIndex();
            String ctidNum = bizPackage.getCtidNum();
            String ctidValidDate = bizPackage.getCtidValidDate();

            Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidIndex, appId);
            if (ctidinfo != null) {
                ctidinfo.setCtidNum(ctidNum);
                ctidinfo.setCtidValidDate(ctidValidDate);
                infoMapper.updateByPrimaryKeySelective(ctidinfo);
                logger.info("保存网证编号成功:");
                return CtidResult.ok("success");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("保存信息失败!");
    }

    private CtidResult authCtidExpire(String cardEndDateStr, Date ctidDownTime) {
        try {
            Date nowDate = new Date();
            if (!"00000000".equals(cardEndDateStr)) {
                Date cardEndDate = new SimpleDateFormat("yyyyMMdd").parse(cardEndDateStr);
                if (nowDate.after(cardEndDate)) {
                    return CtidResult.error(ErrorCodeConstants.AUTH_USERCARD_EXPIRE_CODE,
                            ErrorCodeConstants.AUTH_USERCARD_EXPIRE_MESSAGE);
                }
            }
            Calendar ca = Calendar.getInstance();
            ca.setTime(nowDate);
            ca.add(Calendar.MONTH, 6);
            if (ca.getTime().after(ctidDownTime)) {
                return CtidResult.error(ErrorCodeConstants.AUTH_CTID_EXPIRE_CODE,
                        ErrorCodeConstants.AUTH_CTID_EXPIRE_MESSAGE);
            }
            return CtidResult.ok("success");
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("服务器内部错误");
    }

    @Override
    public CtidResult authCtidValidDate(CtidRequestBean dcaBean) {
        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            AuthCtidValidDateRequestBean biz = (AuthCtidValidDateRequestBean) JSONObject
                    .toBean(JSONObject.fromObject(dcaBean.getBizPackage()), AuthCtidValidDateRequestBean.class);
            String appId = biz.getAppId();
            String ctidIndex = biz.getCtidIndex();
            // 通过ctidIndex 以及 appId 查询数据库网证是否过期
            Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidIndex, appId);
            if (null != ctidinfo) {
                Date nowDate = new Date();
                String ctidValidDate = ctidinfo.getCtidValidDate();
                Date ctidDownTime = ctidinfo.getCtidDownTime();
                String ctidNum = ctidinfo.getCtidNum();

                CtidInfos ctidInfos = ctidInfosMapper.selectByPrimaryKey(ctidIndex);
                String cardEndDateStr = ctidInfos.getCardEndDate();
                Date validDate = new SimpleDateFormat("yyyyMMdd").parse(ctidValidDate);
                int status = 0;
                if (nowDate.after(validDate)) { // 说明网证数据已经过期
                    CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                    int code = authCtidExpire.getCode();
                    if (code == 1) {
                        return CtidResult.error("获取网证信息失败!");
                    } else if (code == ErrorCodeConstants.AUTH_USERCARD_EXPIRE_CODE) {
                        status = 2;
                    } else if (code == ErrorCodeConstants.AUTH_CTID_EXPIRE_CODE) {
                        status = 1;
                    }
                }

                AuthCtidValidDateReturnBean bean = new AuthCtidValidDateReturnBean(ctidNum, ctidValidDate, status);
                String signData = JSONObject.fromObject(bean).toString();
                String md5Sign = MD5Utils.md5(signData, key);
                return CtidResult.success(signData, md5Sign);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("获取网证信息失败!");
    }

    @Override
    public CtidResult authCard(CtidRequestBean dcaBean, HttpServletRequest request) {
        try {
            CtidResult result = new CtidResult();


            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String organizeId = keyInfo.getOrganizeId();
            AuthCardInputDTO biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), AuthCardInputDTO.class);
            String faceData = biz.getFaceData();
            String userDataStr = biz.getUserData();
            String appId = keyInfo.getAppId();
            SfxxBean sfxx = new SfxxBean();
            if (StringUtils.isNotBlank(userDataStr)) {
                try {
                    // 解密用户数据，提取身份信息
                    byte[] userData = cipherSer.decode(Base64Utils.decode(userDataStr));
                    // 加密身份保留数据四项
                    sfxx = ReservedDataUtils.getSfxxBean(userData);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return CtidResult.error("用户数据解析异常");
                }
            } else {
                return CtidResult.error("用户数据为空");
            }

            String authMode = CtidConstants.AUTH_0X40_MODE;
            JFServiceEnum jfService = JFServiceEnum.CARD_AUTH;
            if (StringUtils.isNotBlank(faceData)) {
                authMode = CtidConstants.AUTH_0X42_MODE;
                jfService = JFServiceEnum.CARD_FACE_AUTH;
            }

            //调计费接口
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), jfService, JFChannelEnum.APP_SDK,IpUtils.getIpAddress(request));
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();

            String serialId = UUIDGenerator.getSerialId();

            //发起认证请求
            CtidResult<CtidAuthReturnBean> authCtidRequest = authIdCardService.auth(sfxx.getName(), sfxx.getIdCard(), faceData, specialCode);
            if (0 == authCtidRequest.getCode()) {
                CtidAuthReturnBean data = authCtidRequest.getData();
                serialId = data.getBsn();
                if ("true".equals(data.getAuthResult())) {
                    String cardNum = sfxx.getIdCard();
                    String cardNumHash = CalculateHashUtils.calculateHash(cardNum.getBytes());

                    CtidInfos ctidInfos = ctidInfosMapper.selectByCardNum(cardNumHash);
//					result = CtidResult.ok(data.get("authCode"));
                    if (ctidInfos != null) {
                        if (StringUtils.isBlank(ctidInfos.getBid())) {
                            String encryptCardNumHash = cipherSer.encryptCTID(CalculateHashUtils.calculateHash1(cardNum.getBytes()));
                            ctidInfos.setBid(encryptCardNumHash);
                            ctidInfosMapper.updateByPrimaryKeySelective(ctidInfos);
                        }
                    } else {
                        ctidInfos = new CtidInfos();
                        String encryptCardNumHash = cipherSer.encryptCTID(CalculateHashUtils.calculateHash1(cardNum.getBytes()));
                        ctidInfos.setId(UUIDGenerator.getUUID());
                        ctidInfos.setCardName(cipherSer.encodeIdCardInfo(sfxx.getName(), cardNum));
                        ctidInfos.setCardNum(cardNumHash);
                        ctidInfos.setCreateDate(new Date());
                        ctidInfos.setBid(encryptCardNumHash);
                        ctidInfosMapper.insertSelective(ctidInfos);
                    }
//					AuthCtidRequestReturnBean acrrb = new AuthCtidRequestReturnBean();
                    String token = UUIDGenerator.getUUID();
                    redisOpsClient.set(token, ctidInfos.getBid(), CTIDConstans.TOKEN_EXPIRE_SECONDS);

                    String md5Sign = MD5Utils.md5(token, key);
                    result = CtidResult.success(token, md5Sign);

                } else {
                    String authCode = data.getAuthCode();
                    int code = AuthResultFinder.findAuthResultCode(authCode);
                    result = CtidResult.error(code, data.getAuthDesc());
                }
            } else {
                result.setCode(1);
                result.setMessage(authCtidRequest.getMessage());
            }

            try {
                AuthRecord authRecord = new AuthRecord();

                authRecord.setId(UUIDGenerator.getSerialId());
                authRecord.setAuthType(CtidConstants.AUTH_RECORD_TYPE_SDK);
                authRecord.setAuthResult(0 == result.getCode() ? 0 : 1);
                authRecord.setCreateTime(new Date());
                authRecord.setCtidType(StringUtils.isNotBlank(faceData) ? Contents.CTID_TYPE_THREE : Contents.CTID_TYPE_TWO);
                authRecord.setIdcard(sfxx.getIdCard());
                authRecord.setName(sfxx.getName());
                authRecord.setPic(faceData);
                authRecord.setAuthDesc(result.getMessage());
                authRecord.setServerAccount(keyInfo.getServerAccount());
                authRecord.setSerialNum(serialId);

                mqSend.send(authRecord);
            } catch (Exception e) {
                logger.error(e.getMessage(), "认证记录入库失败!");
            }
            jfClient.send(serverAccount, jfService, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), serialId, specialCode, keyInfo.getAppId());


            ctidLogService.insertAuthCardLog(appId, serialId, authMode, result.getCode(), sfxx, request, result.getMessage());


            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return CtidResult.error("认证失败！请重试！");
    }

    @Override
    public CtidResult v2AuthCard(CtidRequestBean dcaBean, HttpServletRequest request) {
        try {
            CtidResult result = new CtidResult();

            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String sdkCode = keyInfo.getSdkCode();
            AuthCardInputDTO biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), AuthCardInputDTO.class);

            String faceData = biz.getFaceData();
            String userDataStr = biz.getUserData();
            String userDataJson = biz.getUserDataJson();
            String appId = keyInfo.getAppId();

            //获取身份信息
            CtidResult<SfxxBean> sfxxBeanCtidResult = getSfxxBean(userDataStr, userDataJson);
            if (sfxxBeanCtidResult.getCode() != 0) {
                return sfxxBeanCtidResult;
            }
            SfxxBean sfxx = sfxxBeanCtidResult.getData();

            String authMode = CtidConstants.AUTH_0X40_MODE;
            JFServiceEnum jfService = JFServiceEnum.CARD_AUTH;
            if (StringUtils.isNotBlank(faceData)) {
                authMode = CtidConstants.AUTH_0X42_MODE;
                jfService = JFServiceEnum.CARD_FACE_AUTH;
            }

            //调计费接口
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), jfService, JFChannelEnum.APP_SDK,IpUtils.getIpAddress(request));
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();

            String serialId = UUIDGenerator.getSerialId();
            //发起认证请求
            CtidResult<CtidAuthReturnBean> authCtidRequest = authIdCardService.auth(sfxx.getName(), sfxx.getIdCard(), faceData, specialCode);
            if (0 == authCtidRequest.getCode()) {
                CtidAuthReturnBean data = authCtidRequest.getData();
                serialId = data.getBsn();
                if ("true".equals(data.getAuthResult())) {
                    //二项信息入库
                    String ctidIndex = insertCtid(sfxx,null,specialCode);

                    CtidResponseVo ctidRsp = ctidRspUtils.getCtidRsp(sfxx.getIdCard(), sdkCode, null, data.getBsn(), ctidIndex);
                    result = CtidResult.successSign(ctidRsp, key);
                } else {
                    String authCode = data.getAuthCode();
                    int code = AuthResultFinder.findAuthResultCode(authCode);
                    result = CtidResult.error(code, data.getAuthDesc());
                }
            } else {
                result.setCode(1);
                result.setMessage(authCtidRequest.getMessage());
            }

            try {
                AuthRecord authRecord = new AuthRecord();

                authRecord.setId(UUIDGenerator.getSerialId());
                authRecord.setAuthType(CtidConstants.AUTH_RECORD_TYPE_SDK);
                authRecord.setAuthResult(0 == result.getCode() ? 0 : 1);
                authRecord.setCreateTime(new Date());
                authRecord.setCtidType(StringUtils.isNotBlank(faceData) ? Contents.CTID_TYPE_THREE : Contents.CTID_TYPE_TWO);
                authRecord.setIdcard(sfxx.getIdCard());
                authRecord.setName(sfxx.getName());
                authRecord.setPic(faceData);
                authRecord.setAuthDesc(result.getMessage());
                authRecord.setServerAccount(keyInfo.getServerAccount());
                authRecord.setSerialNum(serialId);

                mqSend.send(authRecord);
            } catch (Exception e) {
                logger.error(e.getMessage(), "认证记录入库失败!");
            }
            jfClient.send(serverAccount, jfService, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), serialId, specialCode, keyInfo.getAppId());


            ctidLogService.insertAuthCardLog(appId, serialId, authMode, result.getCode(), sfxx, request, result.getMessage());


            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return CtidResult.error("认证失败！请重试！");
    }

    public CtidResult<SfxxBean> getSfxxBean(String userDataStr, String userDataJson) {
        SfxxBean sfxx = null;
        try {
            if (StringUtils.isNotBlank(userDataStr)) {
                // 解密用户数据，提取身份信息
                byte[] userData = cipherSer.decode(Base64Utils.decode(userDataStr));
                // 获取身份数据
                sfxx = ReservedDataUtils.getSfxxBean(userData);
            }

            if (StringUtils.isNotBlank(userDataJson)) {
                String userJson = new String(cipherSer.decode(Base64Utils.decode(userDataJson)));
                sfxx = JsonUtils.json2Bean(userJson, SfxxBean.class);
            }

            if (null == sfxx) {
                return CtidResult.error("用户信息为空！下载失败！");
            } else {
                return CtidResult.ok(sfxx);
            }
        } catch (Exception e) {
            return CtidResult.error("用户数据解析异常");
        }
    }

    @Override
    public CtidResult downCtidInfo(CtidRequestBean dcaBean,String ip) {
        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String organizeId = keyInfo.getOrganizeId();

            logger.debug("下载请求数据:" + dcaBean.getBizPackage());
            DownCtidInfoInputDTO bizPackage = JsonUtils.json2Bean(dcaBean.getBizPackage(), DownCtidInfoInputDTO.class);

            String appId = bizPackage.getAppId();
            String ctidIndex = bizPackage.getCtidIndex();
            String userDataStr = bizPackage.getUserData();
            Integer type = bizPackage.getType();
            String vCode = bizPackage.getvCode();

            String authCode = null;
            try {
                byte[] decode = cipherSer.decode(Base64Utils.decode(vCode));
                authCode = new String(decode);
            } catch (Exception e1) {
                logger.error("网证认证码解密失败！下载失败！");
                return CtidResult.error("网证认证码解密失败！下载失败！");
            }

            SfxxBean sfxxBean = null;
            // 根据参数判断如何取
            if (1 == type) {
                if (StringUtils.isNotBlank(userDataStr)) {
                    try {
                        // 解密用户数据，提取身份信息
                        byte[] userData = cipherSer.decode(Base64Utils.decode(userDataStr));
                        // 加密身份保留数据四项
                        sfxxBean = ReservedDataUtils.getSfxxBean(userData);
                    } catch (Exception e) {
                        logger.error("用户信息解密失败！下载失败！");
                        return CtidResult.error("用户信息解密失败！下载失败！");
                    }
                }
            } else if (2 == type) {
                CtidInfos ctidInfos = ctidInfosMapper.selectByPrimaryKey(ctidIndex);
                if (ctidInfos != null) {
                    sfxxBean = new SfxxBean();
                    IdCardInfoBean idCardInfo = cipherSer.decodeIdCardInfo(ctidInfos.getCardName());
                    sfxxBean.setName(idCardInfo.getCardName());
                    sfxxBean.setIdCard(idCardInfo.getCardNum());
                    sfxxBean.setStartDate(ctidInfos.getCardStartDate());
                    sfxxBean.setEndDate(ctidInfos.getCardEndDate());
                }
            } else if (3 == type) {
                if (StringUtils.isNotBlank(userDataStr)) {
                    try {
                        // 解密用户数据，提取身份信息
                        byte[] userData = cipherSer.decode(Base64Utils.decode(userDataStr));
                        // 加密身份保留数据四项
                        sfxxBean = ReservedDataUtils.getSfxxBean(userData);
                    } catch (Exception e) {
                        logger.error("用户信息解密失败！下载失败！");
                        return CtidResult.error("用户信息解密失败！下载失败！");
                    }
                }
            }

            if (null == sfxxBean) {
                return CtidResult.error("用户信息为空！下载失败！");
            }

            //调用计费接口
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), JFServiceEnum.CTID_DOWN, JFChannelEnum.APP_SDK,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();
            CtidResult<CtidMessage> downCtidRequest = ctidPCManageService.downCtidInfo(authCode, sfxxBean, specialCode);

            jfClient.send(serverAccount, JFServiceEnum.CTID_DOWN, JFChannelEnum.APP_SDK, downCtidRequest.getCode(), downCtidRequest.getMessage(), UUIDGenerator.getSerialId(), specialCode, keyInfo.getAppId());

            if (0 == downCtidRequest.getCode()) {
                // 下载成功
                String id = insertCtid(sfxxBean, downCtidRequest.getData().getGrsfbs(),specialCode);

                DownCtidRequestReturnBean dcrrBean = new DownCtidRequestReturnBean();
                dcrrBean.setCtidIndex(id);
                dcrrBean.setCtidInfo("");
                String string = JSONObject.fromObject(dcrrBean).toString();
                logger.info("下载请求成功响应数据:" + string);
                String md5Sign = MD5Utils.md5(string, key);
                return CtidResult.success(string, md5Sign);
            }
            return downCtidRequest;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("下载失败!");
    }

    private String insertCtid(SfxxBean sfxxBean, String pid,String specialCode) throws Exception {
        CtidInfos ctidInfos = new CtidInfos();
        String id = UUIDGenerator.getUUID();
        ctidInfos.setId(id);

        String idCardHash = CalculateHashUtils.calculateHash(sfxxBean.getIdCard().getBytes());
        CtidInfos selectByCardNum = ctidInfosMapper.selectByCardNum(idCardHash);

        if (selectByCardNum != null) {
            if(StringUtils.isNotBlank(pid)){
                ctidPidInfoService.insertOrUpdate(selectByCardNum.getId(),pid,specialCode);
            }
            return selectByCardNum.getId();
        } else {
            // 入库
            String cardName = cipherSer.encodeIdCardInfo(sfxxBean.getName(), sfxxBean.getIdCard());

            ctidInfos.setCardName(cardName);
            ctidInfos.setCardNum(idCardHash);
            ctidInfos.setCardStartDate(sfxxBean.getStartDate());
            ctidInfos.setCardEndDate(sfxxBean.getEndDate());
            ctidInfos.setPid(StringUtils.isNotBlank(pid) ? pid : null);
            ctidInfos.setCreateDate(new Date());
            ctidInfosMapper.insertSelective(ctidInfos);
            ctidPidInfoService.insertOrUpdate(ctidInfos.getId(),pid,specialCode);
        }
        return ctidInfos.getId();
    }

    @Override
    public CtidResult authCtidInfo(CtidRequestBean dcaBean,String ip) {
        CtidResult result = new CtidResult();
        try {
            // 验签
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            String organizeId = keyInfo.getOrganizeId();
            AuthCtidInfoInputDTO biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), AuthCtidInfoInputDTO.class);

            String appId = biz.getAppId();
            String ctidIndex = biz.getCtidIndex();
            String faceData = biz.getFaceData();

            // 通过ctidIndex 以及 appId 查询数据库网证是否过期
            Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidIndex, CtidConstants.WX_APPID);
            CtidInfos ctidInfos = ctidInfosMapper.selectByPrimaryKey(ctidIndex);
            if (null != ctidinfo) {
                Date nowDate = new Date();
                String ctidValidDate = ctidinfo.getCtidValidDate();
                Date ctidDownTime = ctidinfo.getCtidDownTime();
                String cardEndDateStr = ctidInfos.getCardEndDate();
                if (StringUtils.isNotBlank(ctidValidDate)) {
                    Date validDate = new SimpleDateFormat("yyyyMMdd").parse(ctidValidDate);
                    if (nowDate.after(validDate)) { // 说明网证数据已经过期
                        // 判断网证过期的原因 身份证过期 网证下载超过6个月
                        // 是否是身份证过期
                        CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                        if (authCtidExpire.getCode() != 0) {
                            return authCtidExpire;
                        }
                    }
                } else {
                    CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                    if (authCtidExpire.getCode() != 0) {
                        return authCtidExpire;
                    }
                }
            } else {
                return CtidResult.error("网证索引不存在,网证认证失败！");
            }

            //调用计费接口
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), JFServiceEnum.CTID_AUTH, JFChannelEnum.APP_SDK,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();

            CtidResult<Map<String, String>> authCtidRequest = ctidPCManageService.authCtid0X06(faceData, ctidinfo.getCtidInfo(), specialCode);

            if (0 == authCtidRequest.getCode()) {
                Map<String, String> data = authCtidRequest.getData();
                if ("true".equals(data.get("authResult"))) {
                    // pid入库
                    String pid = data.get("pid");
                    if (ctidInfos != null && StringUtils.isBlank(ctidInfos.getPid())) {
                        ctidInfos.setPid(pid);
                        ctidInfosMapper.updateByPrimaryKeySelective(ctidInfos);
                    }
                    AuthCtidRequestReturnBean acrrb = new AuthCtidRequestReturnBean();
                    acrrb.setPid(pid);
                    String signData = JSONObject.fromObject(acrrb).toString();
                    String md5Sign = MD5Utils.md5(signData, key);
                    result = CtidResult.success(signData, md5Sign);
                } else {
                    String authCode = data.get("authCode");
                    int code = AuthResultFinder.findAuthResultCode(authCode);
                    result = CtidResult.error(code, data.get("authDesc"));
                }
            } else {
                result = CtidResult.error(authCtidRequest.getMessage());
            }
            String serialNum = UUIDGenerator.getSerialId();
            try {
                AuthRecord authRecord = new AuthRecord(UUIDGenerator.getSerialId(), null,
                        ctidInfos.getCardName(), ctidInfos.getCardNum(), CtidConstants.AUTH_RECORD_TYPE_SDK, Contents.CTID_TYPE_ONE,
                        0 == result.getCode() ? 0 : 1, keyInfo.getServerAccount(), new Date(), serialNum, faceData, null, null, null, result.getMessage(), null, null);

                mqSend.sendAuthRecord(authRecord);
            } catch (Exception e) {
                logger.error(e.getMessage(), "认证记录发送消息失败！");
            }

            jfClient.send(serverAccount, JFServiceEnum.CTID_AUTH, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), serialNum, specialCode, keyInfo.getAppId());

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return CtidResult.error("服务器异常!请重试!");
    }

    @Override
    public CtidResult createQrCode(CtidRequestBean dataBean,String ip) {
        try {
            CtidResult<KeyInfo> validSignResult = validSignData(dataBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String organizeId = keyInfo.getOrganizeId();
            String key = keyInfo.getSecretKey();

            CreateQrCodeInputDTO bean = JsonUtils.json2Bean(dataBean.getBizPackage(), CreateQrCodeInputDTO.class);

            String ctidIndex = bean.getCtidIndex();
            Ctidinfo ctidinfoBean = infoMapper.selectByUserIdAndAppId(ctidIndex, CtidConstants.WX_APPID);
            if (ctidinfoBean != null) {
                CtidResult<CreateCodeRequestReturnBean> result = ctidPCManageService.createCtidCode(ctidinfoBean.getCtidInfo(), organizeId);
                if (result.getCode() == 0) {
                    String codeContent = result.getData().getCodeContent();

                    String data = MatrixToImageWriter.getQrCodeData(codeContent, 280, 280, "jpg");
                    String md5Sign = MD5Utils.md5(data, key);
                    return CtidResult.success(data, md5Sign);
                } else {
                    return result;
                }
            } else {
                return CtidResult.error("网证标识查询不到网证数据！");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("服务器异常!请重试!");
    }

    @Override
    public CtidResult authCtidRequestNew(CtidRequestBean dcaBean,String ip) {
        CtidResult result = new CtidResult();
        try {
            // 验签
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();

            AuthCtidRequestBean biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), AuthCtidRequestBean.class);

            String appId = biz.getAppId();
            String bsn = biz.getBsn();
            String ctidIndex = biz.getCtidIndex();
            String faceData = biz.getFaceData();
            String idCheck = biz.getIdCheck();
            String mode = biz.getMode();
            String userDataStr = biz.getUserData();
            String vCode = biz.getvCode();

            // 通过ctidIndex 以及 appId 查询数据库网证是否过期
            Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidIndex, appId);
            CtidInfos ctidInfos = ctidInfosMapper.selectByPrimaryKey(ctidIndex);
            if (null != ctidinfo) {
                Date nowDate = new Date();
                String ctidValidDate = ctidinfo.getCtidValidDate();
                Date ctidDownTime = ctidinfo.getCtidDownTime();
                String cardEndDateStr = ctidInfos.getCardEndDate();
                if (StringUtils.isNotBlank(ctidValidDate)) {
                    Date validDate = new SimpleDateFormat("yyyyMMdd").parse(ctidValidDate);
                    if (nowDate.after(validDate)) { // 说明网证数据已经过期
                        // 判断网证过期的原因 身份证过期 网证下载超过6个月
                        // 是否是身份证过期
                        CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                        if (authCtidExpire.getCode() != 0) {
                            return authCtidExpire;
                        }
                    }
                } else {
                    CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                    if (authCtidExpire.getCode() != 0) {
                        return authCtidExpire;
                    }
                }
            }

            String encodeReservedData = null;

            //调用计费接口
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), JFServiceEnum.CTID_AUTH, JFChannelEnum.APP_SDK,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();
            CtidResult<CtidAuthReturnBean> authCtidRequest = authCtidService.authCtidRequest(bsn, mode, faceData,
                    idCheck, vCode, encodeReservedData, specialCode);
            Map<String, String> map = new HashMap<String, String>();
            if (0 == authCtidRequest.getCode()) {
                CtidAuthReturnBean data = authCtidRequest.getData();
                if ("true".equals(data.getAuthResult())) {
                    // pid入库
                    String pid = data.getPid();
                    if (ctidInfos != null && StringUtils.isBlank(ctidInfos.getPid())) {
                        ctidInfos.setPid(pid);
                        ctidInfosMapper.updateByPrimaryKeySelective(ctidInfos);
                    }
//					AuthCtidRequestReturnBean acrrb = new AuthCtidRequestReturnBean();
                    String token = UUIDGenerator.getUUID();
                    redisOpsClient.set(token, pid, CTIDConstans.TOKEN_EXPIRE_SECONDS);
//					acrrb.setToken(token);

                    String md5Sign = MD5Utils.md5(token, key);
                    result = CtidResult.success(token, md5Sign);
                } else {
                    String authCode = data.getAuthCode();
                    int code = AuthResultFinder.findAuthResultCode(authCode);
                    result = CtidResult.error(code, data.getAuthDesc());
                }
            } else {
                result = CtidResult.error(authCtidRequest.getMessage());
            }
            try {
                AuthRecord authRecord = new AuthRecord(UUIDGenerator.getSerialId(), null,
                        ctidInfos.getCardName(), ctidInfos.getCardNum(), CtidConstants.AUTH_RECORD_TYPE_SDK, Contents.CTID_TYPE_ONE,
                        0 == result.getCode() ? 0 : 1, serverAccount, new Date(), bsn, faceData, null, null, null, result.getMessage(), null, null);

                mqSend.sendAuthRecord(authRecord);
            } catch (Exception e) {
                logger.error(e.getMessage(), "认证记录入库失败!");
            }

            jfClient.send(serverAccount, JFServiceEnum.CTID_AUTH, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), bsn, specialCode, keyInfo.getAppId());

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return CtidResult.error("服务器异常!请重试!");
    }

    @Override
    public CtidResult getPid(String token) {

        try {
            String pid = redisOpsClient.get(token);
            if (StringUtils.isNotBlank(pid)) {
//                redisOpsClient.del(token);
                return CtidResult.ok(pid);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return CtidResult.error("token已失效");
    }

    @Override
    public CtidResult applyCtidInfo(CtidRequestBean dcaBean,String ip) {
        CtidResult result = new CtidResult();
        try {
            // 验签
            CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
            if (0 != validSignResult.getCode()) {
                return validSignResult;
            }
            KeyInfo keyInfo = validSignResult.getData();
            String key = keyInfo.getSecretKey();
            AuthCtidInfoInputDTO biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), AuthCtidInfoInputDTO.class);

            String appId = biz.getAppId();
            String ctidIndex = biz.getCtidIndex();
            String faceData = biz.getFaceData();

            // 通过ctidIndex 以及 appId 查询数据库网证是否过期
            Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidIndex, CtidConstants.WX_APPID);
            CtidInfos ctidInfos = ctidInfosMapper.selectByPrimaryKey(ctidIndex);
            if (null != ctidinfo) {
                Date nowDate = new Date();
                String ctidValidDate = ctidinfo.getCtidValidDate();
                Date ctidDownTime = ctidinfo.getCtidDownTime();
                String cardEndDateStr = ctidInfos.getCardEndDate();
                if (StringUtils.isNotBlank(ctidValidDate)) {
                    Date validDate = new SimpleDateFormat("yyyyMMdd").parse(ctidValidDate);
                    if (nowDate.after(validDate)) { // 说明网证数据已经过期
                        // 判断网证过期的原因 身份证过期 网证下载超过6个月
                        // 是否是身份证过期
                        CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                        if (authCtidExpire.getCode() != 0) {
                            return authCtidExpire;
                        }
                    }
                } else {
                    CtidResult authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                    if (authCtidExpire.getCode() != 0) {
                        return authCtidExpire;
                    }
                }
            } else {
                return CtidResult.error("网证索引不存在,网证认证失败！");
            }

            //调用计费接口
            JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), JFServiceEnum.CTID_AUTH, JFChannelEnum.APP_SDK,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();
            CtidResult<Map<String, String>> authCtidRequest = ctidPCManageService.authCtid0X06(faceData, ctidinfo.getCtidInfo(), specialCode);

            if (0 == authCtidRequest.getCode()) {
                Map<String, String> data = authCtidRequest.getData();
                if ("true".equals(data.get("authResult"))) {
                    // pid入库
                    String pid = data.get("pid");
                    if (ctidInfos != null && StringUtils.isBlank(ctidInfos.getPid())) {
                        ctidInfos.setPid(pid);
                        ctidInfosMapper.updateByPrimaryKeySelective(ctidInfos);
                    }
                    String token = UUIDGenerator.getUUID();
                    redisOpsClient.set(token, pid, CTIDConstans.TOKEN_EXPIRE_SECONDS);


                    String md5Sign = MD5Utils.md5(token, key);
                    result = CtidResult.success(token, md5Sign);
                } else {
                    String authCode = data.get("authCode");
                    int code = AuthResultFinder.findAuthResultCode(authCode);
                    result = CtidResult.error(code, data.get("authDesc"));
                }
            } else {
                result = CtidResult.error(authCtidRequest.getMessage());
            }
            String serialNum = UUIDGenerator.getSerialId();
            try {
                AuthRecord authRecord = new AuthRecord();

                authRecord.setId(UUIDGenerator.getSerialId());
                authRecord.setAuthType(CtidConstants.AUTH_RECORD_TYPE_SDK);
                authRecord.setAuthResult(0 == result.getCode() ? 0 : 1);
                authRecord.setCreateTime(new Date());
                authRecord.setCtidType(Contents.CTID_TYPE_ONE);
                authRecord.setIdcard(ctidInfos.getCardNum());
                authRecord.setName(ctidInfos.getCardName());
                authRecord.setPic(faceData);
                authRecord.setAuthDesc(result.getMessage());
                authRecord.setServerAccount(keyInfo.getServerAccount());
                authRecord.setSerialNum(serialNum);

                mqSend.sendAuthRecord(authRecord);
            } catch (Exception e) {
                logger.error(e.getMessage(), "认证记录发送消息失败！");
            }

            jfClient.send(serverAccount, JFServiceEnum.CTID_AUTH, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), serialNum, specialCode, keyInfo.getAppId());

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return CtidResult.error("服务器异常!请重试!");
    }

    @Override
    public CtidResult wxApplyCtid(WxApplyCtidInputDTO params,String ip) {

        String openId = params.getOpenId();
        String faceData = params.getFaceData();
        String appId = params.getAppId();

        CtidResult result = new CtidResult();
        PersonInfo person = personDao.selectByOpenID(openId);
        if (null != person) {
            String cardNum = person.getIdcard();
            CtidInfos ctidInfos = ctidInfosMapper.selectByCardNum(cardNum);
            if (null != ctidInfos) {
                Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidInfos.getId(), CtidConstants.WX_APPID);
                String ctidInfo = ctidinfo.getCtidInfo();
                //发起网证认证

                //调用计费接口
                JFResult<JFInfoModel> jfResult = jfClient.jf(appId, JFServiceEnum.CTID_AUTH, JFChannelEnum.WX_SDK,ip);
                if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                    logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),appId);
                    return CtidResult.error(jfResult.getMessage());
                }
                JFInfoModel jfData = jfResult.getData();
                String serverAccount = jfData.getUserServiceAccount();
                String specialCode = jfData.getSpecialCode();
                CtidResult<Map<String, String>> authCtid0X06 = ctidPCManageService.authCtid0X06(faceData, ctidInfo, specialCode);
                if (0 == authCtid0X06.getCode()) {
                    Map<String, String> data = authCtid0X06.getData();
                    if ("true".equals(data.get("authResult"))) {
                        // pid入库
                        String pid = data.get("pid");
                        if (ctidInfos != null && StringUtils.isBlank(ctidInfos.getPid())) {
                            ctidInfos.setPid(pid);
                            ctidInfosMapper.updateByPrimaryKeySelective(ctidInfos);
                        }
                        String token = UUIDGenerator.getUUID();
                        redisOpsClient.set(token, pid, CTIDConstans.TOKEN_EXPIRE_SECONDS);

                        result = CtidResult.ok(token);
                    } else {
                        String authCode = data.get("authCode");
                        int code = AuthResultFinder.findAuthResultCode(authCode);
                        result = CtidResult.error(code, data.get("authDesc"));
                    }
                } else {
                    result = CtidResult.error(authCtid0X06.getMessage());
                }
                String serialNum = UUIDGenerator.getSerialId();
                try {
                    AuthRecord authRecord = new AuthRecord();

                    authRecord.setId(UUIDGenerator.getSerialId());
                    authRecord.setAuthType(CtidConstants.AUTH_RECORD_TYPE_SDK);
                    authRecord.setAuthResult(0 == result.getCode() ? 0 : 1);
                    authRecord.setCreateTime(new Date());
                    authRecord.setCtidType(Contents.CTID_TYPE_ONE);
                    authRecord.setIdcard(ctidInfos.getCardNum());
                    authRecord.setName(ctidInfos.getCardName());
                    authRecord.setPic(faceData);
                    authRecord.setAuthDesc(result.getMessage());
                    authRecord.setServerAccount("微信SDK");
                    authRecord.setSerialNum(serialNum);

                    mqSend.sendAuthRecord(authRecord);
                } catch (Exception e) {
                    logger.error(e.getMessage(), "认证记录发送消息失败！");
                }
                jfClient.send(serverAccount, JFServiceEnum.CTID_AUTH, JFChannelEnum.WX_SDK, result.getCode(), result.getMessage(), serialNum, specialCode, appId);
                return result;
            }
        }
        return CtidResult.error("未下载网证，请先下载网证。");
    }

    @Override
    public CtidResult wxAuthCard(WxAuthCardInputDTO data, HttpServletRequest request) {
        try {
            CtidResult result = new CtidResult();
            String openId = data.getOpenId();
            String wxAppId = data.getWxAppId();
            String faceData = data.getFaceData();
            String cardName = data.getCardName().trim();
            String cardNum = data.getCardNum().trim();
            JFServiceEnum serviceEnum = JFServiceEnum.CARD_AUTH;
            String authMode = CtidConstants.AUTH_0X40_MODE;

            if (StringUtils.isNotBlank(faceData)) {
                authMode = CtidConstants.AUTH_0X42_MODE;
                serviceEnum = JFServiceEnum.CARD_FACE_AUTH;
            }
            JFResult<JFInfoModel> jfResult = jfClient.jf(wxAppId, serviceEnum, JFChannelEnum.WX_SDK,IpUtils.getIpAddress(request));
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();
            CtidResult<CtidAuthReturnBean> auth = authIdCardService.auth(data.getCardName(), data.getCardNum(), faceData, specialCode);
            if (0 == auth.getCode()) {
                CtidAuthReturnBean mapData = auth.getData();
                if ("true".equals(mapData.getAuthResult())) {
//				result = CtidResult.ok(mapData.get("authCode"));
                    String cardNumHash = CalculateHashUtils.calculateHash(cardNum.getBytes());

                    CtidInfos ctidInfos = ctidInfosMapper.selectByCardNum(cardNumHash);
//				result = CtidResult.ok(data.get("authCode"));
                    if (ctidInfos != null) {
                        if (StringUtils.isBlank(ctidInfos.getBid())) {
                            String encryptCardNumHash = cipherSer.encryptCTID(CalculateHashUtils.calculateHash1(cardNum.getBytes()));
                            ctidInfos.setBid(encryptCardNumHash);
                            ctidInfosMapper.updateByPrimaryKeySelective(ctidInfos);
                        }
                    } else {
                        ctidInfos = new CtidInfos();
                        String encryptCardNumHash = cipherSer.encryptCTID(CalculateHashUtils.calculateHash1(cardNum.getBytes()));
                        ctidInfos.setId(UUIDGenerator.getUUID());
                        ctidInfos.setCardName(cipherSer.encodeIdCardInfo(cardName, cardNum));
                        ctidInfos.setCardNum(cardNumHash);
                        ctidInfos.setCreateDate(new Date());
                        ctidInfos.setBid(encryptCardNumHash);
                        ctidInfosMapper.insertSelective(ctidInfos);
                    }
                    String token = UUIDGenerator.getUUID();
                    redisOpsClient.set(token, ctidInfos.getBid(), CTIDConstans.TOKEN_EXPIRE_SECONDS);

                    result = CtidResult.ok(token);
                } else {
                    String authCode = mapData.getAuthCode();
                    int code = AuthResultFinder.findAuthResultCode(authCode);
                    result = CtidResult.error(code, mapData.getAuthDesc());
                }

            } else {
                result = CtidResult.error(auth.getMessage());
            }
            String serialId = UUIDGenerator.getSerialId();
            try {
                AuthRecord authRecord = new AuthRecord();

                authRecord.setId(UUIDGenerator.getSerialId());
                authRecord.setAuthType(CtidConstants.AUTH_RECORD_WX_SDK);
                authRecord.setAuthResult(0 == result.getCode() ? 0 : 1);
                authRecord.setCreateTime(new Date());
                authRecord.setCtidType(StringUtils.isNotBlank(faceData) ? Contents.CTID_TYPE_THREE : Contents.CTID_TYPE_TWO);
                authRecord.setIdcard(cardNum);
                authRecord.setName(cardName);
                authRecord.setPic(faceData);
                authRecord.setAuthDesc(result.getMessage());
                authRecord.setServerAccount(wxAppId);
                authRecord.setSerialNum(serialId);

                mqSend.send(authRecord);
            } catch (Exception e) {
                logger.error(e.getMessage(), "认证记录入库失败!");
            }
            jfClient.send(serverAccount, serviceEnum, JFChannelEnum.WX_SDK, result.getCode(), result.getMessage(), serialId, specialCode, wxAppId);

            try {
                ctidLogService.insertAuthCardLog(wxAppId, serialId, authMode, result.getCode(), cardName, cardNum, request, result.getMessage());
            } catch (Exception e) {

            }
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CtidResult.error("认证失败！请重试！");
    }

    @Override
    public CtidResult sendSmsCode(CtidRequestBean dcaBean) {
        // 验签
        CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
        if (0 != validSignResult.getCode()) {
            return validSignResult;
        }
        KeyInfo keyInfo = validSignResult.getData();
        String key = keyInfo.getSecretKey();
        PhoneSmsCodeInputDTO biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), PhoneSmsCodeInputDTO.class);
        //核验手机号格式
        if (!ValidateUtils.validateMobile(biz.getMobile())) {
            return CtidResult.error("手机号格式不正确");
        }
        CtidResult send = ctidPhoneService.send(biz.getBsn(), biz.getMobile(), null);
        if (send.getCode() == 0) {
            return CtidResult.successSign(send.getData(), key);
        } else {
            return send;
        }
    }

    @Override
    public CtidResult authSmsCode(CtidRequestBean dcaBean,String ip) {
        // 验签
        CtidResult<KeyInfo> validSignResult = validSignData(dcaBean);
        if (0 != validSignResult.getCode()) {
            return validSignResult;
        }
        KeyInfo keyInfo = validSignResult.getData();
        String appId = keyInfo.getAppId();
        String sdkCode = keyInfo.getSdkCode();
        PhoneSmsCodeInputDTO biz = JsonUtils.json2Bean(dcaBean.getBizPackage(), PhoneSmsCodeInputDTO.class);
        //核验手机号格式
        if (!ValidateUtils.validateMobile(biz.getMobile())) {
            return CtidResult.error("手机号格式不正确");
        }
        //核验手机验证码
        if (!ValidateUtils.validateSmsCode(biz.getSmsCode())) {
            return CtidResult.error("手机验证码不正确");
        }

        //调用计费接口
        JFResult<JFInfoModel> jfResult = jfClient.jf(StringUtils.join(",",keyInfo.getAppId(),keyInfo.getAppPackage()), JFServiceEnum.CTID_DOWN_SEND, JFChannelEnum.APP_SDK,ip);
        if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
            logger.error("调用计费系统失败>>>{},key>>{}",jfResult.getMessage(),keyInfo.getAppId());
            return CtidResult.error(jfResult.getMessage());
        }
        JFInfoModel jfData = jfResult.getData();
        String serverAccount = jfData.getUserServiceAccount();
        String specialCode = jfData.getSpecialCode();
        CtidResult<CtidMessage> result = ctidPhoneService.auth(biz.getBsn(), biz.getMobile(), biz.getSmsCode(), specialCode);
        //发送消息到计费系统
        jfClient.send(serverAccount, JFServiceEnum.CTID_DOWN_SEND, JFChannelEnum.APP_SDK, result.getCode(), result.getMessage(), biz.getBsn(), specialCode, appId);

        if (result.getCode() == 0) {
            CtidMessage ctidMessage = result.getData();
            CtidResponseVo ctidRsp = ctidRspUtils.getCtidRspPid(null,ctidMessage.getGrsfbs(), sdkCode, ctidMessage.getCtidInfo(), biz.getBsn(),specialCode);
            return CtidResult.successSign(ctidRsp, keyInfo.getSecretKey());
        } else {
            return result;
        }
    }

}
