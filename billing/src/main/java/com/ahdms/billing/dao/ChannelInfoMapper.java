package com.ahdms.billing.dao;

import com.ahdms.billing.model.ChannelInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ChannelInfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(ChannelInfo record);

    int insertSelective(ChannelInfo record);

    ChannelInfo selectByPrimaryKey(String id);

    ChannelInfo queryByChannelName(@Param("channelName")String channelName);

    ChannelInfo queryByChannelEncode(@Param("channelCode")String channelEncode);

    int updateByPrimaryKeySelective(ChannelInfo record);

    int updateByPrimaryKey(ChannelInfo record);

    List<ChannelInfo> selectLikeChannelName(@Param("channelName")String channelName);

    List<ChannelInfo> selectLikeChannelEncode(@Param("channelCode")String channelEncode);

    List<ChannelInfo> findAll();
}