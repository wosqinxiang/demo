package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.vo.omp.*;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-07-24 14:10
 */
public interface IOmpApiService {

    Result<PageResult<ServiceLogVo>> getLogs(ServiceLogPageReqVo serviceLogPageReqVo);

    Result<List<CustomerProductRspVo>> getCustPro();

    Result<String> pushCustomerInfo(CustomerInfoReqVo customerInfoReqVo);

    Result<String> pushCustomerOrder(CustomerOrderReqVo customerInfoReqVo);

    Result<String> pushProductInfo(ProductInfoReqVo productInfoReqVo);
}
