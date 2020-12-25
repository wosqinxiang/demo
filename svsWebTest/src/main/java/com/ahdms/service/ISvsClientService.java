package com.ahdms.service;

import com.ahdms.bean.req.*;
import com.ahdms.bean.rsp.TwaSvrSignRspVo;

/**
 * @author qinxiang
 * @date 2020-12-16 18:07
 */
public interface ISvsClientService {
    String signData(SignDataReqVo signDataReqVo) throws Exception;

    String svrGenRnd() throws Exception;

    boolean owaSvrVerify(OwaSvrVerifyReqVo verifyReqVo) throws Exception;

    TwaSvrSignRspVo twaSvrSign(TwaSvrSignReqVo svrSignReqVo) throws Exception;

    boolean twaSvrVerify(TwaSvrVerifyReqVo svrVerifyReqVo) throws Exception;

    boolean verifyData(VerifyDataReqVo verifyDataReqVo) throws Exception;

    String pkcs7SignData(Pkcs7SignDataReqVo signDataReqVo) throws Exception;

    boolean pkcs7VerifyData(Pkcs7VerifyDataReqVo verifyDataReqVo) throws Exception;

    String encryptEnvelope(EncryptEnvelopeReqVo encryptEnvelopeReqVo) throws Exception;

    String decryptEnvelope(DecryptEnvelopeReqVo decryptEnvelopeReqVo) throws Exception;

    String certInfo(String certSerialNumber) throws Exception;

    Boolean verifyCert(String certBase64Str) throws Exception;

    String genRandomSignData(GenRandomSignDataReqVo reqVo) throws Exception;

    String encryptData(EncryptDataReqVo reqVo);

    String decryptData(EncryptDataReqVo reqVo);

    String genEncryptKey() throws Exception;
}
