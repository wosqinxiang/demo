package com.ahdms.ctidservice.db.dao;

import com.ahdms.ctidservice.db.model.CtidPidInfos;
import org.apache.ibatis.annotations.Param;

/**
 * @author qinxiang
 * @date 2020-11-26 16:51
 */
public interface CtidPidInfosMapper {

    CtidPidInfos selectByPrimaryKey(String id);

    CtidPidInfos queryByPidAndSpeciallineCode(@Param("pid")String pid,@Param("speciallineCode")String speciallineCode);

    CtidPidInfos queryByPid(String pid);

    int insertSelective(CtidPidInfos ctidPidInfos);

    int updateByPrimaryKeySelective(CtidPidInfos ctidPidInfos);

    CtidPidInfos selectByUserIdAndPid(@Param("ctidInfosId")String ctidInfosId, @Param("pid")String pid);
}
