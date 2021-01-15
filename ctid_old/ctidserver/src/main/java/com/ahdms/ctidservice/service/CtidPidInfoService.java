package com.ahdms.ctidservice.service;

import com.ahdms.ctidservice.db.model.CtidPidInfos;

/**
 * @author qinxiang
 * @date 2020-12-02 13:37
 */
public interface CtidPidInfoService {

    void insertOrUpdate(String userInfosId,String pid,String speciallineCode);

    CtidPidInfos selectByPid(String pid);

    CtidPidInfos selectByPidAndCode(String pid,String speciallineCode);

    CtidPidInfos selectByCtidInfosIdAndPid(String ctidInfosId, String pid);
}
