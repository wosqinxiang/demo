package com.ahdms.service.impl;

import com.ahdms.bean.StandardExtMenu;
import com.ahdms.bean.StandardExtRspVo;
import com.ahdms.dao.IImcStandardExtMenuMapper;
import com.ahdms.dao.IImcTempalteExtensionChooseMapper;
import com.ahdms.framework.core.commom.util.BeanUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.model.ImcStandardExtMenu;
import com.ahdms.model.ImcTempalteExtensionChoose;
import com.ahdms.service.IImcStandardExtMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 14:35
 */
@Service
public class ImcStandardExtMenuServiceImpl implements IImcStandardExtMenuService {

    @Autowired
    private IImcStandardExtMenuMapper standardExtMenuMapper;

    @Autowired
    private IImcTempalteExtensionChooseMapper tempalteExtensionChooseMapper;

    @Override
    public List<StandardExtRspVo> listMenu() {
        List<StandardExtRspVo> result = new ArrayList<>();
        List<ImcStandardExtMenu> imcStandardExtMenus = standardExtMenuMapper
                .selectList(new LambdaQueryWrapper<ImcStandardExtMenu>()
                        .isNull(ImcStandardExtMenu::getParentId));
        imcStandardExtMenus.forEach(standardExtMenu -> {
            StandardExtRspVo standardExt = BeanUtils.copy(standardExtMenu,StandardExtRspVo.class);
            if(0 == standardExt.getHasChild()){
                List<ImcStandardExtMenu> childs =standardExtMenuMapper.selectList(
                        new LambdaQueryWrapper<ImcStandardExtMenu>()
                                .eq(ImcStandardExtMenu::getParentId,standardExt.getId()));
                List<StandardExtRspVo> copy = BeanUtils.copy(childs, StandardExtRspVo.class);
                standardExt.setChildItems(copy);
            }
            result.add(standardExt);
        });
        return result;
    }

    @Override
    public List<StandardExtRspVo> getStandardExt(String templateId) {

        List<ImcTempalteExtensionChoose> imcTempalteExtensionChooses = tempalteExtensionChooseMapper.selectList(
                new LambdaQueryWrapper<ImcTempalteExtensionChoose>()
                .eq(ImcTempalteExtensionChoose::getTemplateId, templateId)
                .eq(ImcTempalteExtensionChoose::getExtType, 1));

        List<StandardExtRspVo> standardExtMenus = listMenu();
        standardExtMenus.forEach(standardExtMenu -> {
            imcTempalteExtensionChooses.forEach(choose -> {
                if(choose.getExtKey().equals(standardExtMenu.getItemKey())){
                    standardExtMenu.setItemValue(0);
                    standardExtMenu.setCriticalValue(choose.getCriticalValue());
                    if(StringUtils.isNotBlank(choose.getChildValues())){
                        String[] childKeys = StringUtils.split(choose.getChildValues(), ",");
                        List<StandardExtRspVo> childMenus = standardExtMenu.getChildItems();
                        childMenus.forEach(childMenu -> {
                            if(contain(childKeys,childMenu.getItemKey())){
                                standardExtMenu.setItemValue(0);
                            }
                        });
                    }
                }
            });
        });
        return standardExtMenus;
    }

    private boolean contain(String[] arr,String id){
        return Arrays.stream(arr).anyMatch(id::equals);
    }
}
