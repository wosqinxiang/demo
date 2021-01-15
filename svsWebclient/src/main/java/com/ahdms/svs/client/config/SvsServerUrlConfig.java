package com.ahdms.svs.client.config;

/**
 * @author qinxiang
 * @date 2020-12-24 14:54
 */
public class SvsServerUrlConfig {
    /**
     * 数字签名
     */
    public static final String SIGN_DATA_PATH = "api/svs/signData";
    /**
     * 验证数字签名
     */
    public static final String VERIFY_DATA_PATH = "api/svs/verifyData";
    /**
     * PKCS7签名（消息签名）
     */
    public static final String PKCS7_SIGN_DATA_PATH = "api/svs/pkcs7/signData";
    /**
     * 验证PKCS7签名（消息签名）
     */
    public static final String PKCS7_VERIFY_DATA_PATH = "api/svs/pkcs7/verifyData";

    public static final String ENCRYPT_ENVELOPE_PATH = "api/svs/encryptEnvelope";

    public static final String DECRYPT_ENVELOPE_PATH = "api/svs/decryptEnvelope";

    public static final String CERT_INFO_PATH = "api/svs/certInfo";

    public static final String VERIFY_CERT_PATH = "api/svs/verifyCert";

    public static final String GEN_RANDOM_PATH = "api/svs/svrGenRnd";

    public static final String OWA_VERIFY_PATH = "api/svs/owaSvrVerify";

    public static final String TWA_SIGN_PATH = "api/svs/twaSvrSign";

    public static final String TWA_VERIFY_PATH = "api/svs/twaSvrVerify";

    public static final String ENCRYPT_DATA_PATH = "api/svs/encryptData";

    public static final String DECRYPT_DATA_PATH = "api/svs/decryptData";

    public static final String ENCRYPT_DATAS_PATH = "api/svs/encryptDatas";

    public static final String DECRYPT_DATAS_PATH = "api/svs/decryptDatas";

}
