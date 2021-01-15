package com.ahdms.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qinxiang
 * @date 2020-12-21 9:29
 */
@Getter
@AllArgsConstructor
public enum ApiCode {
    CORE_DEFAULT_ERROR("CORE10000","服务器出错"),
    CORE_REQ_ARGS_ERROR("CORE10001","无效的请求参数"),
    CORE_PK_ACCESS_ERROR("CORE10002","获取私钥使用权限失败"),
    CORE_CONNECT_SVS_ERROR("CORE10003","连接签名验签服务器出错"),
    CORE_CERT_SERIAL_ERROR("CORE10004","可信标识序列号有误"),
    CORE_GEN_RANDOM_ERROR("CORE10005","产生随机数失败"),
    CORE_BASE64_DECODE_ERROR("CORE10006","Base64解码出错"),
    CORE_ACCOUNT_ERROR("CORE10007","服务账号有误"),

    SIGN_SIGN_DATA_ERROR("SIGN10001","签名失败"),
    SIGN_DATA_ENCODE_ERROR("SIGN10002","签名值编码错误"),
    SIGN_DATA_INVALID_ERROR("SIGN10003","签名值无效"),
    SIGN_PKCS7_SIGN_ERROR("SIGN10004","PKCS7签名失败"),
    SIGN_PKCS7_DATA_ERROR("SIGN10005","找不到被签名原文"),
    SIGN_PKCS7_VERIFY_ERROR("SIGN10006","验证PKCS7签名失败"),
    SIGN_VERIFY_DATA_ERROR("SIGN10007","数字签名验证失败"),
    SIGN_CERT_TRUST_ERROR("SIGN10008","无效可信标识"),
    SIGN_CERT_EXPIRED_ERROR("SIGN10009","可信标识超过有效期"),
    SIGN_CERT_INEFFECTIVE_ERROR("SIGN10010","可信标识未生效"),
    SIGN_CERT_DEFAULT_ERROR("SIGN10011","验证可信标识未知错误"),
    SIGN_CERT_CANCEL_ERROR("SIGN10012","可信标识已作废"),
    SIGN_CHECK_IRL_ERROR("SIGN10013","可信标识验证撤销列表失败"),
    SIGN_CERT_PKM_ERROR("SIGN10014","找不到可信标识颁发机构公钥矩阵"),
    SIGN_CERT_IRL_ERROR("SIGN10015","获取撤销列表失败"),
    SIGN_CERT_TYPE_ERROR("SIGN10016","可信标识类型错误"),

    ENC_ENCRYPT_DATA_ERROR("ENC10001","数字信封加密失败"),
    ENC_ENVELOPE_ENCODE_ERROR("ENC10002","数字信封编码格式错误"),
    ENC_DECRYPT_DATA_ERROR("ENC10003","数字信封解密失败"),
    ENCRYPT_DATA_ERROR("ENC10004","加密失败"),
    DECRYPT_DATA_ERROR("ENC10005","解密失败"),


    MANAGE_USER_ACCOUNT_REPEAT("MGE10001","服务账号已存在"),
    ;

    private String code;
    private String message;
}
