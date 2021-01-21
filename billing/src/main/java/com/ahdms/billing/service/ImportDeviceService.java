/**
 * Created on 2019年11月27日 by liuyipin
 */
package com.ahdms.billing.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.model.BoxInfo;
import com.ahdms.billing.vo.BoxInfoVO;

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
public interface ImportDeviceService {

	void importFile(MultipartFile file, String typeId) throws Exception;

	void addBox(BoxInfo boxInfo) throws Exception;

	void editBox(BoxInfo boxInfo) throws Exception;

	void deleteBox(String boxNum) throws Exception;

	GridModel<BoxInfoVO> selectBox(Map<String, Object> param, int page, int rows);

}

