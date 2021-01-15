package com.ahdms.controller;

import com.ahdms.bean.req.*;
import com.ahdms.bean.rsp.TwaSvrSignRspVo;
import com.ahdms.context.HeaderConstants;
import com.ahdms.result.ApiResult;
import com.ahdms.service.ISvsClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author qinxiang
 * @date 2020-12-16 18:12
 */
@RestController
@Api("svsClient")
@RequestMapping("/api/svs")
public class SvsClientController {

    @Autowired
    private ISvsClientService svsClientService;

    @PostMapping("/signData")
    @ApiOperation(value = "数字签名接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> signData(@RequestBody @Validated @NotNull SignDataReqVo signDataReqVo) throws Exception {
        String signData = svsClientService.signData(signDataReqVo);
        return ApiResult.success(signData);
    }

    @PostMapping("/verifyData")
    @ApiOperation(value = "验证数字签名接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<Boolean> verifyData(@RequestBody @Validated @NotNull VerifyDataReqVo verifyDataReqVo) throws Exception {
        boolean verifyData = svsClientService.verifyData(verifyDataReqVo);
        return ApiResult.success(verifyData);
    }

    @PostMapping("pkcs7/signData")
    @ApiOperation(value = "PKCS7签名（消息签名）接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> pkcs7SignData(@RequestBody @Validated @NotNull Pkcs7SignDataReqVo signDataReqVo) throws Exception {
        String verifyData = svsClientService.pkcs7SignData(signDataReqVo);
        return ApiResult.success(verifyData);
    }

    @PostMapping("pkcs7/verifyData")
    @ApiOperation(value = "验证PKCS7签名（消息签名）接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<Boolean> pkcs7VerifyData(@RequestBody @Validated @NotNull Pkcs7VerifyDataReqVo verifyDataReqVo) throws Exception {
        boolean verifyData = svsClientService.pkcs7VerifyData(verifyDataReqVo);
        return ApiResult.success(verifyData);
    }

    @PostMapping("decryptEnvelope")
    @ApiOperation(value = "数字信封解密接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> decryptEnvelope(@RequestBody @Validated @NotNull DecryptEnvelopeReqVo decryptEnvelopeReqVo) throws Exception {
        String verifyData = svsClientService.decryptEnvelope(decryptEnvelopeReqVo);
        return ApiResult.success(verifyData);
    }

    @PostMapping("/encryptEnvelope")
    @ApiOperation(value = "数字信封加密接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> encryptEnvelope(@RequestBody @Validated @NotNull EncryptEnvelopeReqVo encryptEnvelopeReqVo) throws Exception {
        String verifyData = svsClientService.encryptEnvelope(encryptEnvelopeReqVo);
        return ApiResult.success(verifyData);
    }

    @PostMapping("/genEncryptKey")
    @ApiOperation(value = "生成对称加密密钥接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> genEncryptKey() throws Exception {
        String genEncryptKey = svsClientService.genEncryptKey();
        return ApiResult.success(genEncryptKey);
    }

    @PostMapping("/encryptData")
    @ApiOperation(value = "对称加密接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> encryptData(@RequestBody @Validated @NotNull EncryptDataReqVo reqVo) throws Exception {
        String encryptData = svsClientService.encryptData(reqVo);
        return ApiResult.success(encryptData);
    }

    @PostMapping("/encryptDatas")
    @ApiOperation(value = "批量对称加密接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<Map<String, String>> encryptDatas(@RequestBody List<String> params) throws Exception {
        Map<String, String> encryptData = svsClientService.encryptDatas(params);
        return ApiResult.success(encryptData);
    }

    @PostMapping("/decryptData")
    @ApiOperation(value = "对称解密接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> decryptData(@RequestBody @Validated @NotNull EncryptDataReqVo reqVo) throws Exception {
        String decryptData = svsClientService.decryptData(reqVo);
        return ApiResult.success(decryptData);
    }

    @PostMapping("/decryptDatas")
    @ApiOperation(value = "批量对称解密接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<Map<String, String>> decryptDatas(@RequestBody List<String> params) throws Exception {
        Map<String, String> decryptData = svsClientService.decryptDatas(params);
        return ApiResult.success(decryptData);
    }

    @PostMapping("/svrGenRnd")
    @ApiOperation(value = "生产随机数")
    public ApiResult<String> genRandom(@RequestBody SvrGenRndReqVo reqVo) throws Exception {
        String signData = svsClientService.svrGenRnd(reqVo.getLength());
        return ApiResult.success(signData);
    }

    @PostMapping("/owaSvrVerify")
    @ApiOperation(value = "单向认证服务端验签", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<Boolean> owaSvrVerify(@RequestBody @Validated @NotNull OwaSvrVerifyReqVo verifyReqVo) throws Exception {
        boolean result = svsClientService.owaSvrVerify(verifyReqVo);
        return ApiResult.success(result);
    }

    @PostMapping("/twaSvrSign")
    @ApiOperation(value = "双向认证服务端签名接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<TwaSvrSignRspVo> twaSvrSign(@RequestBody @Validated @NotNull TwaSvrSignReqVo svrSignReqVo) throws Exception {
        TwaSvrSignRspVo result = svsClientService.twaSvrSign(svrSignReqVo);
        return ApiResult.success(result);
    }

    @PostMapping("/twaSvrVerify")
    @ApiOperation(value = "双向认证服务端验签接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<Boolean> twaSvrVerify(@RequestBody @Validated @NotNull TwaSvrVerifyReqVo svrVerifyReqVo) throws Exception {
        boolean result = svsClientService.twaSvrVerify(svrVerifyReqVo);
        return ApiResult.success(result);
    }

    @PostMapping("/certInfo")
    @ApiOperation(value = "获取可信标识信息接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> certInfo() throws Exception {
        String result = svsClientService.certInfo();
        return ApiResult.success(result);
    }

    @PostMapping("/verifyCert")
    @ApiOperation(value = "验证可信标识有效性接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<Boolean> verifyCert(@RequestBody @Validated @NotNull VerifyCertReqVo reqVo) throws Exception {
        Boolean result = svsClientService.verifyCert(reqVo.getBase64EncodeCert());
        return ApiResult.success(result);
    }

    @PostMapping("/genRandomSignData")
    @ApiOperation(value = "单向/双向认证客户端签名(模拟)", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<String> genRandomSignData(@RequestBody @Validated @NotNull GenRandomSignDataReqVo reqVo) throws Exception {
        String result = svsClientService.genRandomSignData(reqVo);
        return ApiResult.success(result);
    }

}
