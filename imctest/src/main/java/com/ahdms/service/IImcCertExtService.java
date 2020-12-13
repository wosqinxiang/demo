package com.ahdms.service;

import com.ahdms.bean.CertExtensionRspVo;
import com.ahdms.bean.CustomerExtRspVo;
import com.ahdms.bean.StandardExtMenu;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 15:19
 */
public interface IImcCertExtService {
    List<CertExtensionRspVo> listMenu();

    List<CustomerExtRspVo> getCustomerExts(String templateId);
}
