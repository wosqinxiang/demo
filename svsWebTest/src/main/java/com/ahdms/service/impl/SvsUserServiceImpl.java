package com.ahdms.service.impl;

import com.ahdms.bean.model.SvsConfig;
import com.ahdms.bean.model.SvsUser;
import com.ahdms.bean.req.SvsUserPageReqVo;
import com.ahdms.bean.req.SvsUserReqVo;
import com.ahdms.bean.rsp.SvsUserPageRspVo;
import com.ahdms.bean.rsp.SvsUserRspVo;
import com.ahdms.code.ApiCode;
import com.ahdms.config.svs.SvsConfigCache;
import com.ahdms.config.svs.SvsToolUtils;
import com.ahdms.dao.ISvsUserMapper;
import com.ahdms.exception.ApiException;
import com.ahdms.service.ISvsConfigService;
import com.ahdms.service.ISvsUserService;
import com.ahdms.util.UUIDUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ahdms.code.ApiCode.MANAGE_USER_ID_ERROR;

/**
 * @author qinxiang
 * @date 2021-01-03 9:57
 */
@Service
public class SvsUserServiceImpl implements ISvsUserService {

    @Autowired
    private ISvsUserMapper svsUserMapper;

    @Autowired
    private ISvsConfigService svsConfigService;

    @Autowired
    private SvsConfigCache svsConfigCache;

    @Autowired
    private SvsToolUtils svsToolUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(SvsUserReqVo userReqVo) throws Exception{
        Integer count = svsUserMapper.selectCount(
                new LambdaQueryWrapper<SvsUser>().eq(SvsUser::getAccount, userReqVo.getAccount()));
        if(count != null && count != 0){
            throw new ApiException(ApiCode.MANAGE_USER_ACCOUNT_REPEAT);
        }

        SvsConfig svsConfig = new SvsConfig(userReqVo.getId(),userReqVo.getSvsIp(),userReqVo.getSvsPort(),userReqVo.getSvsKeyIndex(),userReqVo.getSvsKeyValue(),userReqVo.getSvsSerialNumber(),userReqVo.getSvsEncryptKey());
        svsConfigService.addSvsConfig(svsConfig);

        SvsUser svsUser = new SvsUser();
        svsUser.setAccount(userReqVo.getAccount());
        svsUser.setId(UUIDUtils.getUUID());
        svsUser.setSvsConfigId(svsConfig.getId());
        svsUser.setInfo(userReqVo.getInfo());
        svsUser.setWhiteIp(userReqVo.getWhiteIp());
        svsUserMapper.insert(svsUser);

        svsConfigCache.put(svsUser.getAccount(),svsConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SvsUserReqVo userReqVo) throws Exception {
        deleteUser(userReqVo.getId());
        addUser(userReqVo);
        svsToolUtils.removeSVTool(userReqVo.getAccount());
    }

    @Override
    public SvsUserRspVo queryUser(String id) {
        SvsUser svsUser = svsUserMapper.selectById(id);
        if(svsUser == null){
            throw new ApiException(MANAGE_USER_ID_ERROR);
        }
        SvsUserRspVo rspVo = new SvsUserRspVo();
        rspVo.setId(svsUser.getId());
        rspVo.setAccount(svsUser.getAccount());
        rspVo.setInfo(svsUser.getInfo());

        SvsConfig svsConfig = svsConfigService.selectById(svsUser.getSvsConfigId());
        rspVo.setSvsIp(svsConfig.getIp());
        rspVo.setSvsKeyIndex(svsConfig.getKeyIndex());
        rspVo.setSvsKeyValue(svsConfig.getKeyValue());
        rspVo.setSvsPort(svsConfig.getPort());
        rspVo.setSvsSerialNumber(svsConfig.getSerialNumber());
        rspVo.setSvsEncryptKey(svsConfig.getEncryptKey());
        return rspVo;
    }

    @Override
    public IPage<SvsUserPageRspVo> pageUser(SvsUserPageReqVo reqVo) {
        IPage<SvsUserPageRspVo> page = new Page<>(reqVo.getPageNum(),reqVo.getPageSize());
        IPage<SvsUserPageRspVo> svsUserIPage = svsUserMapper.selectPageUser(page);
        return svsUserIPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String id) {
        SvsUser svsUser = svsUserMapper.selectById(id);
        if(svsUser == null){
            throw new ApiException(MANAGE_USER_ID_ERROR);
        }
        svsConfigService.deleteById(svsUser.getSvsConfigId());
        svsUserMapper.deleteById(id);
        svsConfigCache.remove(svsUser.getAccount());
        svsToolUtils.removeSVTool(svsUser.getAccount());
    }
}
