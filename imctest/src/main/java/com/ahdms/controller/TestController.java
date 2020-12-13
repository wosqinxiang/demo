package com.ahdms.controller;

import com.ahdms.bean.*;
import com.ahdms.service.IImcCertDnService;
import com.ahdms.service.IImcCertExtService;
import com.ahdms.service.IImcCertTempalteService;
import com.ahdms.service.IImcStandardExtMenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 14:20
 */
@RestController
@Api("imc测试")
public class TestController {

    @Autowired
    private IImcStandardExtMenuService standardExtMenuService;

    @Autowired
    private IImcCertExtService certExtService;

    @Autowired
    private IImcCertDnService certDnService;

    @Autowired
    private IImcCertTempalteService certTempalteService;

    @GetMapping("standardExt/list/menu")
    public List<StandardExtRspVo> standardExtListMenu(){
        return standardExtMenuService.listMenu();
    }

    @GetMapping("customerExt/list/menu")
    public List<CertExtensionRspVo> customerExtListMenu(){
        return certExtService.listMenu();
    }

    @GetMapping("certDn/list/menu")
    public List<CertDnRspVo> certDnExtListMenu(){
        return certDnService.list();
    }

    @PostMapping("template")
    public void addTemplate(@RequestBody CertTemplateReqVo certTemplateReqVo){
        certTempalteService.addTemplate(certTemplateReqVo);
    }

    @GetMapping("tempalte")
    public CertTemplateRspVo info(@RequestParam String templateId){
        return certTempalteService.info(templateId);
    }

}
