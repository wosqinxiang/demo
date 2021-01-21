package com.ahdms.billing.dao;

import java.util.List;
import java.util.Map;

import com.ahdms.billing.model.BoxType;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface BoxTypeDao {
    int deleteByPrimaryKey(String id);

    int insert(BoxType record);

    int insertSelective(BoxType record);

    BoxType selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BoxType record);

    int updateByPrimaryKey(BoxType record);

	List<BoxType> selectAll();

	List<BoxType> pageQuery(Map<String, Object> param);
	
	BoxType selectByBoxNum(String boxNum);
}