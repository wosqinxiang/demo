package com.ahdms.ctidservice.service;

import java.util.Map;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.ctidservice.vo.CtidResult;

public interface AuthIdCardService {
	
	CtidResult<CtidAuthReturnBean> auth(String name, String idCard, String faceData, String serverId);
	
}
