package com.ahdms.ctidservice.contants;

public class QrCodeErrorCodeFinder {
	
	public static int findCode(String errorCode){
		int code = 1;
		switch (errorCode) {
		case "0101012008":
			code = ErrorCodeConstants.CREATE_QRCODE_NONE_CODE;
			break;
		case "0101012009":
			code = ErrorCodeConstants.CREATE_QRCODE_CANCEL_CODE;
			break;
		case "0101012010":
			code = ErrorCodeConstants.CREATE_QRCODE_BLOCK_CODE;
			break;
		case "0101012011":
			code = ErrorCodeConstants.CREATE_QRCODE_EXPIRY_CODE;
			break;
		default:
			code = ErrorCodeConstants.CREATE_QRCODE_DEFAULT_CODE;
			break;
		}
		return code;
	}
	
}
