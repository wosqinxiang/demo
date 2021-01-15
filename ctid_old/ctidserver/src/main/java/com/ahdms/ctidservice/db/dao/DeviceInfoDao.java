/**
 * Created on 2018年7月9日 by liuyipin
 */
package com.ahdms.ctidservice.db.dao;

import java.util.Map;

import com.ahdms.ctidservice.db.model.DeviceModel;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

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

public interface DeviceInfoDao {
    int insert(DeviceModel record);

    int insertSelective(DeviceModel record);

	PageList<DeviceModel> pageQuery(Map<String, Object> queryMap, PageBounds pageBounds);

	void update(Map<String, Object> map);

	void delDevice(String id);

	DeviceModel selectByID(String id);

	DeviceModel selectByAppid(String appId);

	DeviceModel selectByName(String name); 
}

