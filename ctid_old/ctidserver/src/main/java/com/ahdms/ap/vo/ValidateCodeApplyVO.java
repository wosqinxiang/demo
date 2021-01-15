/**
 * Created on 2019年8月14日 by liuyipin
 */
package com.ahdms.ap.vo;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class ValidateCodeApplyVO {
	
	String applyData;
	
	int authMode;

	public String getApplyData() {
		return applyData;
	}

	public void setApplyData(String applyData) {
		this.applyData = applyData;
	}

	public int getAuthMode() {
		return authMode;
	}

	public void setAuthMode(int authMode) {
		this.authMode = authMode;
	}

}

