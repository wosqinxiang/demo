package com.ahdms.billing.model.report;

import com.ahdms.billing.model.UserService;

public class UserServiceForChannel extends UserService {
    private int sumCount;

    public int getSumCount() {
        return sumCount;
    }

    public void setSumCount(int sumCount) {
        this.sumCount = sumCount;
    }
}
