package com.ahdms.svs.client;

import com.ahdms.svs.client.bean.TwaSvrSignRspVo;
import com.ahdms.svs.client.config.SvsServerUrlConfig;
import com.ahdms.svs.client.result.ApiResult;
import com.ahdms.svs.client.util.HttpExecuteUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qinxiang
 * @date 2020-12-24 14:42
 */
public class SvsServiceClient {

    private String account;

    private String serverUrl;

    private SvsServiceClient(String account,String serverUrl){
        this.account = account;
        this.serverUrl = serverUrl;
    }

    public static SvsServiceClient getInstance(String account,String serverUrl){
        return new SvsServiceClient(account,serverUrl);
    }

    /**
     * 数字签名接口
     * @param inData 待签名原文
     * @return Base64编码的签名值
     */
    public ApiResult<String> signData(String inData){
        Map<String,String> params = new HashMap<>();
        params.put("inData",inData);
        ApiResult apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.SIGN_DATA_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 验证数字签名接口
     * @param base64EncodeCert Base64 编码的可信标识
     * @param base64SignValue Base64编码的签名值
     * @param inData 待签名原文
     * @param verifyLevel 可信标识验证级别(默认值为0)
     * @return
     */
    public ApiResult<Boolean> verifyData(String base64EncodeCert,String base64SignValue,String inData,int verifyLevel){
        Map<String,Object> params = new HashMap<>();
        params.put("base64EncodeCert",base64EncodeCert);
        params.put("base64SignValue",base64SignValue);
        params.put("inData",inData);
        params.put("verifyLevel",verifyLevel);
        ApiResult<Boolean> apiResult = HttpExecuteUtils.execute(this.serverUrl, SvsServerUrlConfig.VERIFY_DATA_PATH,params,this.account);
        return apiResult;
    }

    /**
     * PKCS7签名（消息签名）接口
     * @param inData 待签名原文
     * @param originalText 签名值是否附加原文属性(0.是，1.否)，
     * @param irls 签名值是否附加撤销列表(0.是，1.否)
     * @return
     */
    public ApiResult<String> pkcs7SignData(String inData,int originalText,int irls){
        Map<String,Object> params = new HashMap<>();
        params.put("originalText",originalText);
        params.put("inData",inData);
        params.put("irls",irls);
        ApiResult<String> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.PKCS7_SIGN_DATA_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 验证PKCS7签名（消息签名）接口
     * @param base64Pkcs7SignData Base64编码的PKCS7签名值
     * @param inData 待签名的数据原文
     * @param originalText 签名值是否附加原文属性(0.是，1.否)
     * @param irls 签名值是否附加撤销列表(0.是，1.否)
     * @return
     */
    public ApiResult<Boolean> pkcs7VerifyData(String base64Pkcs7SignData,String inData,int originalText,int irls){
        Map<String,Object> params = new HashMap<>();
        params.put("base64Pkcs7SignData",base64Pkcs7SignData);
        params.put("inData",inData);
        params.put("originalText",originalText);
        params.put("irls",irls);
        ApiResult<Boolean> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.PKCS7_VERIFY_DATA_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 数字信封加密接口
     * @param base64EncodeCert Base64编码的可信标识
     * @param inData 待加密的原文数据
     * @return 加密后的密文(base64编码后的)
     */
    public ApiResult<String> encryptEnvelope(String base64EncodeCert,String inData){
        Map<String,Object> params = new HashMap<>();
        params.put("base64EncodeCert",base64EncodeCert);
        params.put("inData",inData);
        ApiResult<String> apiResult = HttpExecuteUtils.execute(this.serverUrl, SvsServerUrlConfig.ENCRYPT_ENVELOPE_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 数字信封解密接口
     * @param base64EnvelopeData 加密后的密文(base64编码后的)
     * @return
     */
    public ApiResult<String> decryptEnvelope(String base64EnvelopeData){
        Map<String,Object> params = new HashMap<>();
        params.put("base64EnvelopeData",base64EnvelopeData);
        ApiResult<String> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.DECRYPT_ENVELOPE_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 对称加密接口
     * @param inData 待加密原文数据
     * @return
     */
    public ApiResult<String> encryptData(String inData){
        Map<String,Object> params = new HashMap<>();
        params.put("inData",inData);
        ApiResult<String> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.ENCRYPT_DATA_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 对称解密接口
     * @param inData 待解密数据
     * @return
     */
    public ApiResult<String> decryptData(String inData){
        Map<String,Object> params = new HashMap<>();
        params.put("inData",inData);
        ApiResult<String> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.DECRYPT_DATA_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 获取可信标识信息接口
     * @return
     */
    public ApiResult<String> certInfo(){
        Map<String,Object> params = new HashMap<>();
        ApiResult<String> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.CERT_INFO_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 生产服务端随机数
     * @return
     */
    public ApiResult<String> svrGenRnd(){
        Map<String,Object> params = new HashMap<>();
        ApiResult<String> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.GEN_RANDOM_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 单向认证服务端验签
     * @param base64SignValue Base64编码的签名值
     * @param base64EncodeCert Base64 编码的可信标识
     * @param clientRandom 客户端随机数
     * @param serverRandom 服务端随机数
     * @param verifyLevel 可信标识验证级别(默认值为0)
     * @return
     */
    public ApiResult<Boolean> owaSvrVerify(String base64SignValue,String base64EncodeCert,String clientRandom,
                                          String serverRandom,Integer verifyLevel){
        Map<String,Object> params = new HashMap<>();
        params.put("base64SignValue",base64SignValue);
        params.put("base64EncodeCert",base64EncodeCert);
        params.put("clientRandom",clientRandom);
        params.put("serverRandom",serverRandom);
        params.put("verifyLevel",verifyLevel);
        ApiResult<Boolean> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.OWA_VERIFY_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 双向认证服务端签名接口
     * @param clientRandom 客户端随机数
     * @return
     */
    public ApiResult<TwaSvrSignRspVo> twaSvrSign(String clientRandom){
        Map<String,Object> params = new HashMap<>();
        params.put("clientRandom",clientRandom);
        ApiResult<TwaSvrSignRspVo> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.TWA_SIGN_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 双向认证服务端验签接口
     * @param base64SignValue Base64编码的签名值
     * @param base64EncodeCert Base64 编码的可信标识
     * @param clientRandom 客户端随机数
     * @param serverRandom 服务端随机数
     * @param verifyLevel 可信标识验证级别(默认值为0)
     * @return
     */
    public ApiResult<Boolean> twaSvrVerify(String base64SignValue,String base64EncodeCert,String clientRandom,
                                          String serverRandom,Integer verifyLevel){
        Map<String,Object> params = new HashMap<>();
        params.put("base64SignValue",base64SignValue);
        params.put("base64EncodeCert",base64EncodeCert);
        params.put("clientRandom",clientRandom);
        params.put("serverRandom",serverRandom);
        params.put("verifyLevel",verifyLevel);
        ApiResult<Boolean> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.TWA_VERIFY_PATH,params,this.account);
        return apiResult;
    }

    /**
     * 验证可信标识有效性接口
     * @param base64EncodeCert Base64 编码的可信标识
     * @return
     */
    public ApiResult<Boolean> verifyCert(String base64EncodeCert){
        Map<String,Object> params = new HashMap<>();
        params.put("base64EncodeCert",base64EncodeCert);
        ApiResult<Boolean> apiResult = HttpExecuteUtils.execute(this.serverUrl,SvsServerUrlConfig.VERIFY_CERT_PATH,params,this.account);
        return apiResult;
    }

}
