package com.ahdms.svs.client;

import com.ahdms.svs.client.bean.TwaSvrSignRspVo;
import com.ahdms.svs.client.result.ApiResult;

import java.util.Map;
import java.util.Set;

/**
 * @author qinxiang
 * @date 2020-12-28 11:35
 */
public abstract class SecurityEngineDeal {

    public String account;

    public String serverUrl;

    public SecurityEngineDeal(String account,String serverUrl){
        this.account = account;
        this.serverUrl = serverUrl;
    }

    public static SecurityEngineDeal getInstance(String account,String serverUrl){

        return new DefaultSecurityEngineDeal(account,serverUrl);
    }

    /**
     * 数字签名接口
     * @param inData 待签名原文
     * @return Base64编码的签名值
     */
    public abstract ApiResult<String> signData(String inData);

    /**
     * 验证数字签名接口
     * @param base64EncodeCert Base64 编码的可信标识
     * @param base64SignValue Base64编码的签名值
     * @param inData 待签名原文
     * @param verifyLevel 可信标识验证级别(默认值为0)
     * @return
     */
    public abstract ApiResult<Boolean> verifyData(String base64EncodeCert,String base64SignValue,String inData,int verifyLevel);

    /**
     * PKCS7签名（消息签名）接口
     * @param inData 待签名原文
     * @param originalText 签名值是否附加原文属性(0.是，1.否)，
     * @param irls 签名值是否附加撤销列表(0.是，1.否)
     * @return
     */
    public abstract ApiResult<String> pkcs7SignData(String inData,int originalText,int irls);

    /**
     * 验证PKCS7签名（消息签名）接口
     * @param base64Pkcs7SignData Base64编码的PKCS7签名值
     * @param inData 待签名的数据原文
     * @param originalText 签名值是否附加原文属性(0.是，1.否)
     * @param irls 签名值是否附加撤销列表(0.是，1.否)
     * @return
     */
    public abstract ApiResult<Boolean> pkcs7VerifyData(String base64Pkcs7SignData,String inData,int originalText,int irls);

    /**
     * 数字信封加密接口
     * @param base64EncodeCert Base64编码的可信标识
     * @param inData 待加密的原文数据
     * @return 加密后的密文(base64编码后的)
     */
    public abstract ApiResult<String> encryptEnvelope(String base64EncodeCert,String inData);

    /**
     * 数字信封解密接口
     * @param base64EnvelopeData 加密后的密文(base64编码后的)
     * @return
     */
    public abstract ApiResult<String> decryptEnvelope(String base64EnvelopeData);

    /**
     * 对称加密接口
     * @param inData 待加密原文数据
     * @return
     */
    public abstract ApiResult<String> encryptData(String inData);

    /**
     * 批量加密接口
     * @param inDatas 待加密的数据集合
     * @return
     */
    public abstract ApiResult<Map<String,String>> encryptDatas(Set<String> inDatas);

    /**
     * 批量加密接口
     * @param inDatas 待加密的数据集合
     * @return
     */
    public abstract ApiResult<Map<String,String>> encryptDatas(String ...inDatas);

    /**
     * 对称解密接口
     * @param inData 待解密数据
     * @return
     */
    public abstract ApiResult<String> decryptData(String inData);

    /**
     * 批量对称解密接口
     * @param inDatas 待解密的数据集合
     * @return
     */
    public abstract ApiResult<Map<String,String>> decryptDatas(Set<String> inDatas);

    /**
     * 批量对称解密接口
     * @param inDatas 待解密的数据集合
     * @return
     */
    public abstract ApiResult<Map<String,String>> decryptDatas(String ...inDatas);

    /**
     * 获取可信标识信息接口
     * @return
     */
    public abstract ApiResult<String> certInfo();

    /**
     * 生产服务端随机数
     * @return
     */
    public abstract ApiResult<String> svrGenRnd(int length);

    /**
     * 单向认证服务端验签
     * @param base64SignValue Base64编码的签名值
     * @param base64EncodeCert Base64 编码的可信标识
     * @param clientRandom 客户端随机数
     * @param serverRandom 服务端随机数
     * @param verifyLevel 可信标识验证级别(默认值为0)
     * @return
     */
    public abstract ApiResult<Boolean> owaSvrVerify(String base64SignValue,String base64EncodeCert,String clientRandom,
                                           String serverRandom,Integer verifyLevel);

    /**
     * 双向认证服务端签名接口
     * @param clientRandom 客户端随机数
     * @return
     */
    public abstract ApiResult<TwaSvrSignRspVo> twaSvrSign(String clientRandom);

    /**
     * 双向认证服务端验签接口
     * @param base64SignValue Base64编码的签名值
     * @param base64EncodeCert Base64 编码的可信标识
     * @param clientRandom 客户端随机数
     * @param serverRandom 服务端随机数
     * @param verifyLevel 可信标识验证级别(默认值为0)
     * @return
     */
    public abstract ApiResult<Boolean> twaSvrVerify(String base64SignValue,String base64EncodeCert,String clientRandom,
                                           String serverRandom,Integer verifyLevel);

    /**
     * 验证可信标识有效性接口
     * @param base64EncodeCert Base64 编码的可信标识
     * @return
     */
    public abstract ApiResult<Boolean> verifyCert(String base64EncodeCert);

}
