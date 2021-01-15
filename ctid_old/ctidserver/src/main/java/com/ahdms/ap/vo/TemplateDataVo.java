/**
 * Created on 2019年12月10日 by liuyipin
 */
package com.ahdms.ap.vo;

import lombok.Data;

/**
 * @Title 
 * @Description 设置推送的文字和颜色
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@Data
public class TemplateDataVo {
    //字段值例如：keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
    private String value;//依次排下去
//    private String color;//字段颜色（微信官方已废弃，设置没有效果）
    private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TemplateDataVo(String key, String value) {
		super();
		this.value = value;
		this.key = key;
	}

	public TemplateDataVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
//	private String thing14;
//	private String date3;
//	private String date4;
//	private String thing11;
//	private String thing15;
//	public String getThing14() {
//		return thing14;
//	}
//	public void setThing14(String thing14) {
//		this.thing14 = thing14;
//	}
//	public String getDate3() {
//		return date3;
//	}
//	public void setDate3(String date3) {
//		this.date3 = date3;
//	}
//	public String getDate4() {
//		return date4;
//	}
//	public void setDate4(String date4) {
//		this.date4 = date4;
//	}
//	public String getThing11() {
//		return thing11;
//	}
//	public void setThing11(String thing11) {
//		this.thing11 = thing11;
//	}
//	public String getThing15() {
//		return thing15;
//	}
//	public void setThing15(String thing15) {
//		this.thing15 = thing15;
//	}
//	public TemplateDataVo() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	public TemplateDataVo(String thing14, String date3, String date4, String thing11, String thing15) {
//		super();
//		this.thing14 = thing14;
//		this.date3 = date3;
//		this.date4 = date4;
//		this.thing11 = thing11;
//		this.thing15 = thing15;
//	}
    
} 

