/**
 * <p>Title: CreateCodeRequestData.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.vo;

/**
 * <p>Title: CreateCodeRequestData</p>  
 * <p>Description: 二维码赋码请求数据格式</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 */
public class CreateCodeRequestData {

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
		private String bizSerialNum; //业务流水号
		private String checkData; //请求核验数据 通过 “官方身份认证控件接口定义文档” 中的 getReqQRCodeData接口生成；
		private Integer isPic;  //是否生成图片 1-二维码码图流；目前只支持1;  0 不生成图片
		private String custNum; //客户号
		
		public String getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getBizSerialNum() {
			return bizSerialNum;
		}
		public void setBizSerialNum(String bizSerialNum) {
			this.bizSerialNum = bizSerialNum;
		}
		public String getCheckData() {
			return checkData;
		}
		public void setCheckData(String checkData) {
			this.checkData = checkData;
		}
		public Integer getIsPic() {
			return isPic;
		}
		public void setIsPic(Integer isPic) {
			this.isPic = isPic;
		}
		public String getCustNum() {
			return custNum;
		}
		public void setCustNum(String custNum) {
			this.custNum = custNum;
		}
		
    }
}
