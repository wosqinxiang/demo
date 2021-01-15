package com.ahdms.dao;

import com.ahdms.bean.model.SvsUser;
import com.ahdms.bean.rsp.SvsUserPageRspVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qinxiang
 * @date 2021-01-03 9:55
 */
@Mapper
public interface ISvsUserMapper extends BaseMapper<SvsUser> {
    IPage<SvsUserPageRspVo> selectPageUser(IPage<SvsUserPageRspVo> page);
}
