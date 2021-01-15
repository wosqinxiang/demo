/**
 * <p>Title: CreateCodeApplyData.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 * @version 1.0  
*/
package com.ahdms.auth.model;


/**
 * <p>Title: CreateCodeApplyData</p>  
 * <p>Description: 二维码赋码申请请求数据格式</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 */
public class CreateCodeApplyData {
	
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
		private String applyData; //申请数据通过“官方身份认证控件接口定义文档”中的 getApplyData 接口生成；
		private String custNum; //客户号
		public String getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getApplyData() {
			return applyData;
		}
		public void setApplyData(String applyData) {
			this.applyData = applyData;
		}
		public String getCustNum() {
			return custNum;
		}
		public void setCustNum(String custNum) {
			this.custNum = custNum;
		}
		
    }

}
