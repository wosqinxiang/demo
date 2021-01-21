package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.KeyInfo;

public interface KeyInfoService {

	Result add(KeyInfo keyInfo);

	Result selectAll();

	Result update(KeyInfo keyInfo);

	Result delete(int id);

}
