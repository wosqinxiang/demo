package com.ahdms.ap.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

public class AuthRecordVo implements Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID，不超过64字符随机数")
	private String id;
	@ApiModelProperty(value = "信息来源(1:微信 2：支付宝 3：生态app 4：其他)")
	private Integer infoSource;
	@ApiModelProperty(value = "姓名")
	private String name;
	@ApiModelProperty(value = "身份证")
	private String idcard;
	@ApiModelProperty(value = "认证模式(1：在线  2:离线 3：便捷 4：api)")
	private Integer authType;
	@ApiModelProperty(value = "使用网证模式（ 1:0x06(网证)  2：0x40（二项信息）  3：0x42（二项信息+人脸））")
	private Integer ctidType;
	@ApiModelProperty(value = "认证结果(0：成功 1：失败）")
	private Integer authResult;
	@ApiModelProperty(value = "服务账号")
	private String serverAccount;
	@ApiModelProperty(value = "创建时间（离线认证的签名时间）")
	private Date createTime;
	@ApiModelProperty(value = "业务流水号")
	private String serialNum;
	@ApiModelProperty(value = "人像信息")
	private String pic;
	@ApiModelProperty(value = "签名值（仅离线模式）")
	private String signData;
	@ApiModelProperty(value = "app唯一码")
	private String openid;
	@ApiModelProperty(value = "认证通过后，生成的二维码数据")
	private String twoBarCodeData;


	@ApiModelProperty(value = "认证发起方")
	private String CertificationInitiator;

	@ApiModelProperty(value = "认证对象")
	private Integer authObject;


	@ApiModelProperty(value = "二维码是否有效")
	private String twoBarCodeValidate;

	@ApiModelProperty(value = "户籍所在地")
	private String domicilePlace;



	public String getDomicilePlace() {
		return domicilePlace;
	}

	public void setDomicilePlace(String domicilePlace) {
		this.domicilePlace = domicilePlace;
	}


	public String getCertificationInitiator() {
		return CertificationInitiator;
	}

	public void setCertificationInitiator(String certificationInitiator) {
		CertificationInitiator = certificationInitiator;
	}

	public Integer getAuthObject() {
		return authObject;
	}

	public void setAuthObject(Integer authObject) {
		this.authObject = authObject;
	}

	public String getTwoBarCodeValidate() {
		return twoBarCodeValidate;
	}

	public void setTwoBarCodeValidate(String twoBarCodeValidate) {
		this.twoBarCodeValidate = twoBarCodeValidate;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSignData() {
		return signData;
	}

	public void setSignData(String signData) {
		this.signData = signData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getInfoSource() {
		return infoSource;
	}

	public void setInfoSource(Integer infoSource) {
		this.infoSource = infoSource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard == null ? null : idcard.trim();
	}

	public Integer getAuthType() {
		return authType;
	}

	public void setAuthType(Integer authType) {
		this.authType = authType;
	}

	public Integer getCtidType() {
		return ctidType;
	}

	public void setCtidType(Integer ctidType) {
		this.ctidType = ctidType;
	}

	public Integer getAuthResult() {
		return authResult;
	}

	public void setAuthResult(Integer authResult) {
		this.authResult = authResult;
	}

	public String getServerAccount() {
		return serverAccount;
	}

	public void setServerAccount(String serverAccount) {
		this.serverAccount = serverAccount == null ? null : serverAccount.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum == null ? null : serialNum.trim();
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic == null ? null : pic.trim();
	}

	public String getTwoBarCodeData() {
		return twoBarCodeData;
	}

	public void setTwoBarCodeData(String twoBarCodeData) {
		this.twoBarCodeData = twoBarCodeData;
	}
}