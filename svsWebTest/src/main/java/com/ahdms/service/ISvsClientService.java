package com.ahdms.service;

import com.ahdms.bean.req.*;
import com.ahdms.bean.rsp.TwaSvrSignRspVo;

import java.util.List;
import java.util.Map;

/**
 * @author qinxiang
 * @date 2020-12-16 18:07
 */
public interface ISvsClientService {
    String signData(SignDataReqVo signDataReqVo) throws Exception;

    String svrGenRnd(Integer length) throws Exception;

    boolean owaSvrVerify(OwaSvrVerifyReqVo verifyReqVo) throws Exception;

    TwaSvrSignRspVo twaSvrSign(TwaSvrSignReqVo svrSignReqVo) throws Exception;

    boolean twaSvrVerify(TwaSvrVerifyReqVo svrVerifyReqVo) throws Exception;

    boolean verifyData(VerifyDataReqVo verifyDataReqVo) throws Exception;

    String pkcs7SignData(Pkcs7SignDataReqVo signDataReqVo) throws Exception;

    boolean pkcs7VerifyData(Pkcs7VerifyDataReqVo verifyDataReqVo) throws Exception;

    String encryptEnvelope(EncryptEnvelopeReqVo encryptEnvelopeReqVo) throws Exception;

    String decryptEnvelope(DecryptEnvelopeReqVo decryptEnvelopeReqVo) throws Exception;

    String certInfo() throws Exception;

    Boolean verifyCert(String certBase64Str) throws Exception;

    String genRandomSignData(GenRandomSignDataReqVo reqVo) throws Exception;

    String encryptData(EncryptDataReqVo reqVo) throws Exception;

    String decryptData(EncryptDataReqVo reqVo) throws Exception;

    String genEncryptKey() throws Exception;

    Map<String, String> encryptDatas(List<String> params) throws Exception;

    Map<String, String> decryptDatas(List<String> params) throws Exception;
}
