/**
 * <p>Title: SfxxBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月29日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.vo;

import lombok.Data;

/**
 * <p>Title: SfxxBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年7月29日  
 */
@Data
public class SfxxBean {

	//姓名
	private String name;
	//身份证
	private String idCard;
	//身份证有效期开始
	private String startDate;
	//身份证有效期结束
	private String endDate;
    
}
