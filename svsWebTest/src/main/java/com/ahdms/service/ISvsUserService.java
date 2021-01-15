package com.ahdms.service;

import com.ahdms.bean.req.SvsUserPageReqVo;
import com.ahdms.bean.req.SvsUserReqVo;
import com.ahdms.bean.rsp.SvsUserPageRspVo;
import com.ahdms.bean.rsp.SvsUserRspVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author qinxiang
 * @date 2021-01-03 9:55
 */
public interface ISvsUserService {
    void addUser(SvsUserReqVo userReqVo) throws Exception;

    SvsUserRspVo queryUser(String id);

    IPage<SvsUserPageRspVo> pageUser(SvsUserPageReqVo reqVo);

    void deleteUser(String id);

    void updateUser(SvsUserReqVo userReqVo) throws Exception;
}
