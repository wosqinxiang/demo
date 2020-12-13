package com.ahdms.service;

import com.ahdms.bean.StandardExtMenu;
import com.ahdms.bean.StandardExtRspVo;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 14:34
 */
public interface IImcStandardExtMenuService {
    List<StandardExtRspVo> listMenu();

    List<StandardExtRspVo> getStandardExt(String templateId);
}
