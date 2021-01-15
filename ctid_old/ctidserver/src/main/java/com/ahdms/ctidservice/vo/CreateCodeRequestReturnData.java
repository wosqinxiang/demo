/**
 * <p>Title: CreateCodeRequestReturnData.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.vo;

/**
 * <p>Title: CreateCodeRequestReturnData</p>  
 * <p>Description: 二维码赋码请求应答数据格式</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 */
public class CreateCodeRequestReturnData {
	
	private BizPackageBean bizPackage;
    private String sign;
    
    public BizPackageBean getBizPackage() {
		return bizPackage;
	}

	public void setBizPackage(BizPackageBean bizPackage) {
		this.bizPackage = bizPackage;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}


	public static class BizPackageBean {
    	
		private String timeStamp; //时间戳  YYYYMMddHHmmssSSS
		private Boolean success;  //申请结果
		private String errorCode; //异常码  正常为 0
		private String errorDesc; //错误信息描述
		private String codeContent; //二维码内容（base64 编码）
		private String imgStream; //二维码图片流 isPic=1时，返回二维码图片流
		private Integer width; //位图边长 isPic=1时，返回图片位图边长
		public String getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public Boolean getSuccess() {
			return success;
		}
		public void setSuccess(Boolean success) {
			this.success = success;
		}
		public String getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorDesc() {
			return errorDesc;
		}
		public void setErrorDesc(String errorDesc) {
			this.errorDesc = errorDesc;
		}
		public String getCodeContent() {
			return codeContent;
		}
		public void setCodeContent(String codeContent) {
			this.codeContent = codeContent;
		}
		public String getImgStream() {
			return imgStream;
		}
		public void setImgStream(String imgStream) {
			this.imgStream = imgStream;
		}
		public Integer getWidth() {
			return width;
		}
		public void setWidth(Integer width) {
			this.width = width;
		}
		
    }
}
