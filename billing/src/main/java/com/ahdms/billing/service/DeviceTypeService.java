/**
 * Created on 2019年12月16日 by liuyipin
 */
package com.ahdms.billing.service;

import java.util.List;
import java.util.Map;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.model.BoxType;
import com.ahdms.billing.vo.BoxTypeVO;


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
public interface DeviceTypeService {

	void addBoxType(BoxType boxType) throws Exception;

	void editBoxType(BoxType boxType) throws Exception;

	void delBoxType(String typeId) throws Exception;

	GridModel<BoxTypeVO> selectBoxType(Map<String, Object> param, int page, int rows);

	List<BoxType> getType();

}

