package com.ahdms.ctidservice.web;

import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.ctidservice.bean.vo.ApiAuthReqVo;
import com.ahdms.ctidservice.service.IApiAuthService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qinxiang
 * @date 2021-01-05 10:26
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
@Api("api-auth")
public class ApiAuthController {

    @Autowired
    private IApiAuthService apiAuthService;

    @RequestMapping("api/auth")
    public CtidResult auth(@RequestBody ApiAuthReqVo reqVo){
        CtidAuthReturnBean returnBean = apiAuthService.auth(reqVo.getCardName(),reqVo.getCardNum(),reqVo.getPhotoData());
        return CtidResult.ok(reqVo);
    }

}
