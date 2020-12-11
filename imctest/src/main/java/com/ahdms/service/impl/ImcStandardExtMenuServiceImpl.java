package com.ahdms.service.impl;

import com.ahdms.bean.StandardExtMenu;
import com.ahdms.dao.IImcStandardExtMenuMapper;
import com.ahdms.framework.core.commom.util.BeanUtils;
import com.ahdms.model.ImcStandardExtMenu;
import com.ahdms.service.IImcStandardExtMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 14:35
 */
@Service
public class ImcStandardExtMenuServiceImpl implements IImcStandardExtMenuService {

    @Autowired
    private IImcStandardExtMenuMapper standardExtMenuMapper;

    @Override
    public List<StandardExtMenu> listMenu() {
        List<StandardExtMenu> result = new ArrayList<>();
        List<ImcStandardExtMenu> imcStandardExtMenus = standardExtMenuMapper
                .selectList(new LambdaQueryWrapper<ImcStandardExtMenu>()
                        .isNull(ImcStandardExtMenu::getParentId));
        imcStandardExtMenus.forEach(standardExtMenu -> {
            StandardExtMenu standardExt = BeanUtils.copy(standardExtMenu,StandardExtMenu.class);
            if(0 == standardExt.getHasChild()){
                List<ImcStandardExtMenu> childs =standardExtMenuMapper.selectList(
                        new LambdaQueryWrapper<ImcStandardExtMenu>()
                                .eq(ImcStandardExtMenu::getParentId,standardExt.getId()));
                List<StandardExtMenu> copy = BeanUtils.copy(childs, StandardExtMenu.class);
                standardExt.setChildMenu(copy);
            }
            result.add(standardExt);
        });
        return result;
    }
}
