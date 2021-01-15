/**
 * Created on 2019年8月5日 by liuyipin
 */
package com.ahdms.ap.service;

import java.util.List;

import com.ahdms.ap.common.GridModel;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ap.vo.AuthRecordVo;
import com.ahdms.ap.vo.CtidVO;
import com.ahdms.ap.vo.PersonInfoVO;

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
public interface ServerService {

	int login(String account, String password);

	GridModel<AuthRecord> recordsList(String infoSource, String IDcard, int page, int rows);

	AuthRecordVo getRecord(String recordId) throws Exception;

	PersonInfoVO getIdCard(String openId);

	CtidVO getCtidInfo(String iDcard);

	AuthRecordVo getRecordBySerialNum(String serialNum, String openId) throws Exception;

}

