/**
 * Created on 2020年5月8日 by liuyipin
 */
package com.ahdms.billing.service;

import java.util.List;
import java.util.Map;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.ProviderInfo;

/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public interface ProviderService {

	GridModel<ProviderInfo> queryProviderPageList(Map object, int page, int rows, Object object2);

	Result<String> addProvider(ProviderInfo provider);

	Result<String> eidtProvider(ProviderInfo provider);

	Result<List<ProviderInfo>> getProvider();

	Result<List<ProviderInfo>> getProviderByServiceId(String serviceId);

}

