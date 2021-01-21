package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.ChannelInfo;
import com.ahdms.billing.model.ServiceInfo;

public interface ChannelInfoService {

    Result<Object> addChannel(ChannelInfo channelInfo);

    Result<ChannelInfo> getChannelById(String id);

    Result<ChannelInfo> selectByChannelName(String channelName);

    Result<ChannelInfo> selectByChannelEncode(String channelEncode);

    Result<ChannelInfo> updateChannel(ChannelInfo channelInfo);

    Result<ChannelInfo> deleteChannelById(String id);

    Result<Object> selectLikeChannelName(int page, int size, String channelName);

    Result<Object> selectLikeChannelEncode(int page, int size, String channelEncode);

    Result<Object> findAll(int page, int size);
}
