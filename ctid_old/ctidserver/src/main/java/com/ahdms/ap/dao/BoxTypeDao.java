package com.ahdms.ap.dao;

import java.util.List;
import java.util.Map;

import com.ahdms.ap.model.BoxType;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface BoxTypeDao {
    int deleteByPrimaryKey(String id);

    int insert(BoxType record);

    int insertSelective(BoxType record);

    BoxType selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BoxType record);

    int updateByPrimaryKey(BoxType record);

	PageList<BoxType> pageQuery(Map<String, Object> param, PageBounds pageBounds);

	List<BoxType> selectAll();
}