/**
 * Created on 2019年8月2日 by liuyipin
 */
package com.ahdms.ap.service;

import java.util.Map;

import com.ahdms.ap.common.HttpResponseBody;
import com.ahdms.ap.vo.OnlineVO;
import com.ahdms.ap.vo.SignResponseVo;

/**
 * @author liuyipin
 * @version 1.0
 * @Title
 * @Description
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public interface VerifyService {

    OnlineVO simpleVerify(String name, String iDcard, String facePic, String devicName, String password,String ip) throws Exception;

    Map<String, Object> onlineVerify(String name, String iDcard, String facePic, int type, String openID, String serialNum, String location,String ip) throws Exception;


    /**
     * 离线认证
     *
     * @param name      姓名
     * @param idCard    身份证号
     * @param facePic   人像信息
     * @param openID    用户登录唯一标识码
     * @param serialNum 业务流水号
     * @param signUrl   中转服务签名接口地址
     * @param type 数据来源
     * @return SignResponseVo
     */
    SignResponseVo offlineVerify(String name, String idCard, String facePic, String openID, String serialNum, int type, String signUrl,String ip) throws Exception;

    /**
     * 流水号长度：32个字符
     * 规则（字符）：01+ （30个字符的随机串）
     *
     * @param deviceName 设备账户
     * @param password   密码
     * @param url        回调地址
     * @param location 
     * @param skipUrl    中转页面跳转url
     * @return
     */
    String generateSerialNum(String deviceName, String password, String url, int location, String skipUrl) throws Exception;

    boolean addUser(String name, String iDcard, String facePic, String openID, int type,String ip) throws Exception;

	SignResponseVo offlineVerifyByCtid(String ctid, String facePic, String openID, String serialNum, Integer type, String businessSerialNumber,String ip) throws Exception;

	Map<String, Object> onlineVerifyByCtid(String ctid, String facePic, String location, String openID, String serialNum,
			Integer type, String businessSerialNumber,String ip) throws Exception;

	HttpResponseBody<String> userLogin(String name, String upperCase, String facePic, String openID, int type, String ip) throws Exception;

	Map<String, Object> FTOFVerify(String name, String idcard, String openID, String pic, String serialNum,String ip) throws Exception;

	String getWechatSerialNum(String socketId, String openid, int authType ) throws Exception;

	Map<String, Object> longDistanceVerify(String name, String idcard, String openID, String pic, String serialNum,String ip) throws Exception;

	boolean SerialNumIsValid(String serialNum);

	void addAuthRecord(String name, String idcard, String openID, String pic, String serialNum ,Integer authType, String failDesc,String ip) throws Exception;

	String getWPOSSerialNum(String wposEN);

	Map<String, Object> WPOSVerify(String name, String upperCase, String pic, Integer type, String openID,
			String serialNum,String ip) throws Exception;
}

