package com.ahdms.ctidservice.service.impl;

import com.ahdms.ctidservice.bean.CtidRequestBean;
import com.ahdms.ctidservice.common.IpUtils;
import com.ahdms.ctidservice.common.UUIDGenerator;
import com.ahdms.ctidservice.constants.CtidConstants;
import com.ahdms.ctidservice.db.dao.CtidLogMapper;
import com.ahdms.ctidservice.db.model.CtidLog;
import com.ahdms.ctidservice.service.CtidLogService;
import com.ahdms.ctidservice.util.JsonUtils;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.ctidservice.vo.SfxxBean;
import com.ahdms.util.SM4Utils;
import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class CtidLogServiceImpl implements CtidLogService {

    private static final Logger logger = LoggerFactory.getLogger(CtidLogServiceImpl.class);

    @Autowired
    private CtidLogMapper ctidLogMapper;

    SM4Utils sm4 = new SM4Utils("AHdms520", "ahdms", false);

    @Override
    @Async("ctidExecutor")
    public void insertAuthCtidLog(String ret, String appId, String mode, String ipAddress,String bsn) {
        CtidLog ctidLog = new CtidLog();
        ctidLog.setId(UUIDGenerator.getUUID());
        ctidLog.setIp(ipAddress);
        ctidLog.setCreateTime(new Date());
        ctidLog.setType(CtidLog.AUTH_TYPE);
        ctidLog.setMode(mode);
        ctidLog.setAppId(appId);
        ctidLog.setCtidBsn(bsn);
        JSONObject jsonObject = JSONObject.fromObject(ret);
        int result = JsonUtils.getInt(jsonObject,"result");
        ctidLog.setResult(result);
        if(1 == result){
            String errorDesc = JsonUtils.getString(jsonObject,"message");
            ctidLog.setErrorDesc(errorDesc);
        }
        ctidLogMapper.insertSelective(ctidLog);

    }

    @Override
    @Async("ctidExecutor")
    public void insertSelective(CtidRequestBean dcaBean, CtidResult result, String ipAddress, int type) {
        try {
            CtidLog ctidLog = getCtidLog(dcaBean, result, ipAddress, type);
            ctidLogMapper.insertSelective(ctidLog);
        } catch (Exception e) {
            logger.error(">>>>操作日志入库失败..." + e.getMessage());
        }
    }

    private CtidLog getCtidLog(CtidRequestBean dcaBean, CtidResult result, String ipAddress, int type) {
        JSONObject biz = JSONObject.fromObject(dcaBean.getBizPackage());
        String mode = JsonUtils.getString(biz, "mode");
        String bsn = JsonUtils.getString(biz, "bsn");
        String appId = JsonUtils.getString(biz, "appId");

        return new CtidLog(UUIDGenerator.getUUID(), appId, type, mode, result.getCode(), result.getMessage(), new Date(), ipAddress, bsn);
    }


    @Override
    @Async("ctidExecutor")
    public void insertSelective(CtidRequestBean dcaBean, CtidResult result, String ipAddress, int type, Object obj) {
        CtidLog ctidLog = getCtidLog(dcaBean, result, ipAddress, type);

        if (CtidConstants.CTID_RESULT_SUCCESS_CODE != result.getCode() && CtidConstants.CTID_RESULT_REVOKED_CODE != result.getCode()) {
            SfxxBean data = (SfxxBean) obj;
            if (null != data) {
                encryptSfxx(data, ctidLog);
            }
        }

        ctidLogMapper.insertSelective(ctidLog);
    }

    @Override
    @Async("ctidExecutor")
    public void insertAuthCardLog(String appId, String businessSerialNumber, String authMode, int code, String getxM,
                                  String gMSFZHM, HttpServletRequest request, String message) {
        CtidLog ctidLog = new CtidLog(UUIDGenerator.getUUID(), appId, CtidLog.AUTH_TYPE, authMode, code, message, new Date(), IpUtils.getIpAddress(request), businessSerialNumber);

        if (code != 0) {

            SfxxBean sfxx = new SfxxBean();
            sfxx.setName(getxM);
            sfxx.setIdCard(gMSFZHM);
            encryptSfxx(sfxx, ctidLog);

        }
        ctidLogMapper.insertSelective(ctidLog);
    }

    @Override
    @Async("ctidExecutor")
    public void insertAuthCardLog(String appId, String businessSerialNumber, String authMode, int code,
                                  SfxxBean sfxxBean, HttpServletRequest request, String message) {
        CtidLog ctidLog = new CtidLog(UUIDGenerator.getUUID(), appId, CtidLog.AUTH_TYPE, authMode, code, message, new Date(), IpUtils.getIpAddress(request), businessSerialNumber);

        if (code != 0) {
            encryptSfxx(sfxxBean, ctidLog);
        }
        ctidLogMapper.insertSelective(ctidLog);
    }

    private void encryptSfxx(SfxxBean sfxxBean, CtidLog ctidLog) {
        try {
            String idCard = sfxxBean.getIdCard();
            String last4Code = idCard.substring((idCard.length() - 4) < 0 ? 0 : (idCard.length() - 4));
            String encryptData = sm4.encryptData_ECB(JSON.toJSONString(sfxxBean));
            StringBuilder stb = new StringBuilder(ctidLog.getErrorDesc()).append(">>").append(last4Code).append(">>>>").append(encryptData);
            ctidLog.setErrorDesc(stb.toString());
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        CtidLogServiceImpl c = new CtidLogServiceImpl();
        //身份信息比对失败>>9519>>>>Iaw4JNfW9PcuvPPZHSGmuRMCsl8LAkIxuojwcNErASMymSg4btTauaLaWvq+ebqsKJHl6DVENmNbQBSvsOuygxhsUl27KnCEqzO6NFT4A1kOLrOMGq2BOKxKD6/ABxT3
        SM4Utils sm4 = new SM4Utils("AHdms520", "ahdms", false);
        String s = "a0Ffg5crjVcC5nQLBI09SAccbquARkqgl/xrDwcjawuElltLr20b2Djc17pbctv+HJmamOGr/nWBwTv2a5p0+jPtYPtqDuIfTqdqnUZBUWxkld9IlfqNhCncqw6nQ0wS";
        String decryptData_ECB = sm4.decryptData_ECB(s);
        System.out.println(decryptData_ECB);
    }

}
