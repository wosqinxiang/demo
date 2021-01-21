package com.ahdms.billing.dao;

import java.util.List;
import java.util.Map;

import com.ahdms.billing.model.BoxInfo;
import com.ahdms.billing.model.BoxInfos;
import com.ahdms.billing.model.BoxType;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface BoxInfoDao {
    int deleteByPrimaryKey(String boxId);

    int insert(BoxInfo record);

    int insertSelective(BoxInfo record);

    BoxInfo selectByPrimaryKey(String boxId);

    int updateByPrimaryKeySelective(BoxInfo record);

    int updateByPrimaryKey(BoxInfo record);

	int selectByNUM(String boxId, String boxNum);

	int selectByType(String typeId);

	List<BoxInfos> pageQuery(Map<String, Object> param);
	
}