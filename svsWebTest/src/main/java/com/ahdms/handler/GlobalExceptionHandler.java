package com.ahdms.handler;

import static com.ahdms.code.ApiCode.*;

import com.ahdms.exception.*;
import com.ahdms.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qinxiang
 * @date 2020-12-04 14:29
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ApiResult handleException(Exception e){
        log.error(e.getMessage(),e);

        if(e instanceof ApiException){
            return new ApiResult(((ApiException) e).getCode(),e.getMessage(),null);
        }

        if(e instanceof SVS_PrivatekeyAccessRightException){
            return ApiResult.error(CORE_PK_ACCESS_ERROR);
        }
        if(e instanceof SVS_InvalidParameterException){
            return ApiResult.error(CORE_REQ_ARGS_ERROR);
        }
        if(e instanceof SVS_NotFoundCertException){
            return ApiResult.error(CORE_CERT_SERIAL_ERROR);
        }
        if(e instanceof SVS_GenerateRandomException){
            return ApiResult.error(CORE_GEN_RANDOM_ERROR);
        }
        if(e instanceof SVS_ServerConnectException){
            return ApiResult.error(CORE_CONNECT_SVS_ERROR);
        }

        if(e instanceof SVS_SignDataException){
            return ApiResult.error(SIGN_SIGN_DATA_ERROR);
        }
        if(e instanceof SVS_SignedDataEncodeException){
            return ApiResult.error(SIGN_DATA_ENCODE_ERROR);
        }
        if(e instanceof SVS_SignatureException){
            return ApiResult.error(SIGN_DATA_INVALID_ERROR);
        }
        if(e instanceof SVS_CertNotTrustException){
            return ApiResult.error(SIGN_CERT_TRUST_ERROR);
        }
        if(e instanceof SVS_CertExpiredException){
            return ApiResult.error(SIGN_CERT_EXPIRED_ERROR);
        }
        if(e instanceof SVS_CertCancelException){
            return ApiResult.error(SIGN_CERT_CANCEL_ERROR);
        }
        if(e instanceof SVS_CheckIRLException){
            return ApiResult.error(SIGN_CHECK_IRL_ERROR);
        }
        if(e instanceof SVS_CertIneffectiveException){
            return ApiResult.error(SIGN_CERT_INEFFECTIVE_ERROR);
        }
        if(e instanceof SVS_CertException){
            return ApiResult.error(SIGN_CERT_DEFAULT_ERROR);
        }
        if(e instanceof SVS_NotFoundOriginalTextException){
            return ApiResult.error(SIGN_PKCS7_DATA_ERROR);
        }
        if(e instanceof SVS_VerifyPKCS7SignDataException){
            return ApiResult.error(SIGN_PKCS7_VERIFY_ERROR);
        }
        if(e instanceof SVS_NotFoundPKMException){
            return ApiResult.error(SIGN_CERT_PKM_ERROR);
        }
        if(e instanceof SVS_GetIRLException){
            return ApiResult.error(SIGN_CERT_IRL_ERROR);
        }
        if(e instanceof SVS_VerifyDataException){
            return ApiResult.error(SIGN_VERIFY_DATA_ERROR);
        }


        if(e instanceof SVS_EncryptEnvelopeException){
            return ApiResult.error(ENC_ENCRYPT_DATA_ERROR);
        }
        if(e instanceof SVS_EnvelopeEncodeException){
            return ApiResult.error(ENC_ENVELOPE_ENCODE_ERROR);
        }
        if(e instanceof SVS_DecryptEnvelopeException){
            return ApiResult.error(ENC_DECRYPT_DATA_ERROR);
        }
        if(e instanceof SVS_EncryptBytesException){
            return ApiResult.error(ENCRYPT_DATA_ERROR);
        }
        if(e instanceof SVS_DecryptBytesException){
            return ApiResult.error(DECRYPT_DATA_ERROR);
        }

        if(e instanceof Base64Exception){
            return ApiResult.error(CORE_BASE64_DECODE_ERROR);
        }

        return ApiResult.error(CORE_DEFAULT_ERROR);
    }


    /**
     * 处理所有接口数据验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ApiResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        return ApiResult.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }



}
