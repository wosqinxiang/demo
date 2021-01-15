/**
 * Created on 2019年12月10日 by liuyipin
 */
package com.ahdms.ap.vo;

import java.util.Map;

import lombok.Data;

/**
 * @Title 
 * @Description 小程序推送所需数据
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@Data
public class WxMssVo {
    private String touser;//用户openid
    private String template_id;//模版id
    private String page = "pages/index/index";//默认跳到小程序首页
    private String form_id;//收集到的用户formid
//    private String emphasis_keyword = "keyword1.DATA";//放大那个推送字段
    private Map<String, TemplateDataVo> data;//推送文字
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getForm_id() {
		return form_id;
	}
	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}
	public Map<String, TemplateDataVo> getData() {
		return data;
	}
	public void setData(Map<String, TemplateDataVo> data) {
		this.data = data;
	}
    
    
} 
