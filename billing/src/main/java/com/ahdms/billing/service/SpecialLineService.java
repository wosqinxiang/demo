package com.ahdms.billing.service;

import java.util.List;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.SpecialLineInfo;
import com.ahdms.billing.vo.SpecialLineInfoVO;
import com.ahdms.billing.vo.SpecialLineVO;

public interface SpecialLineService {

	Result addSpecialLine(SpecialLineInfo specialLineInfo);

	Result addSpecialLineService(SpecialLineInfoVO specialLineInfoVO);

	Result<List<SpecialLineVO>> findSpecialLine(String providerId,String name, String code);

	Result deleteSpecialLine(String id);

	Result findSpecialLineService(String id);

	Result deleteSpecialLineService(String id);

	Result findSpecial(String providerId, String serviceId);

	Result updateSpecialLine(SpecialLineInfo specialLineInfo);

}
