package com.ahdms.service;

import com.ahdms.bean.CertDnRspVo;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 16:55
 */
public interface IImcCertDnService {
    List<CertDnRspVo> list();

    List<CertDnRspVo> getDnRsp(String dnIds);
}
