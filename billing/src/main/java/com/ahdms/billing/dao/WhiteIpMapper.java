package com.ahdms.billing.dao;

import com.ahdms.billing.model.WhiteIp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-11-27 10:46
 */
public interface WhiteIpMapper {
    WhiteIp selectByPrimaryKey(String id);

    int deleteByPrimaryKey(String id);

    int insertSelective(WhiteIp whiteIp);

    int updateByPrimaryKeySelective(WhiteIp whiteIp);

    List<String> queryAllByUserId(String userId);

    void deleteAllByUserId(String userId);

    WhiteIp selectByUserIdAndIp(@Param("userId") String userId, @Param("ip")String ip);
}
