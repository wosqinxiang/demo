package com.ahdms.ap.dao;

import java.util.Map;

import com.ahdms.ap.model.BoxInfo;
import com.ahdms.ap.model.BoxInfos;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface BoxInfoDao {
    int deleteByPrimaryKey(String boxId);

    int insert(BoxInfo record);

    int insertSelective(BoxInfo record);

    BoxInfo selectByPrimaryKey(String boxId);

    int updateByPrimaryKeySelective(BoxInfo record);

    int updateByPrimaryKey(BoxInfo record);

	PageList<BoxInfos> pageQuery(Map<String, Object> param, PageBounds pageBounds);

	int selectByNUM(String boxId, String boxNum);

	int selectByType(String typeId);

	BoxInfo selectByBoxNum(String boxNum);
}