/**
 * Created on 2019年9月30日 by liuyipin
 */
package com.ahdms.ap.dao;

import java.util.List;

import com.ahdms.ap.model.AuthRecord;

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
public interface  AuthRecordArchiveDao {

	void insertArchive(List<AuthRecord> authRecords);

}

