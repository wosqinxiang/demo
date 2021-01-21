package com.ahdms.billing.model.report;

import com.ahdms.billing.model.UserService;

public class ServiceLogForUserServiceData extends UserService {
    private int count;
    private String channelEncode;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getChannelEncode() {
        return channelEncode;
    }

    public void setChannelEncode(String channelEncode) {
        this.channelEncode = channelEncode;
    }
}
