/**
 * <p>Title: ApiAuthController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年8月7日
 * @version 1.0
 */
package com.ahdms.ctidservice.web.controller;

import com.ahdms.ctidservice.Exception.ApiException;
import com.ahdms.ctidservice.bean.ApiAuthRequestBean;
import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.common.FileUtils;
import com.ahdms.ctidservice.service.ApiAuthService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>Title: ApiAuthController</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年8月7日
 */
@RestController
@RequestMapping(value = "/api", method = {RequestMethod.POST, RequestMethod.GET})
public class ApiAuthController {

    @Autowired
    private ApiAuthService apiAuthService;

    @RequestMapping("/auth")
    public CtidResult auth(@RequestBody ApiAuthRequestBean authRequestBean
    ) throws ApiException {
        CtidResult result = new CtidResult();
        if (null == authRequestBean) {
            return CtidResult.error("参数错误!");
        }
        String authMode = "0x40";
        if (StringUtils.isNotBlank(authRequestBean.getPhotoData())) {
            authMode = "0x42";
        }
        result = apiAuthService.auth(authRequestBean.getCardName(), authRequestBean.getCardNum(), authRequestBean.getPhotoData(), null);
        return result;
    }

    @RequestMapping("/auth1")
    public CtidResult auth1(@RequestParam(required = true) String cardName,
                            @RequestParam(required = true) String cardNum,
                            @RequestParam(required = false) String photoData
    ) throws ApiException {
        CtidResult result = new CtidResult();

        String authMode = "0x40";
        if (StringUtils.isNotBlank(photoData)) {
            authMode = "0x42";
            try {
                photoData = Base64Utils.encode(FileUtils.toByteArray("E:/mine/" + photoData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        result = apiAuthService.auth(authMode, cardName, cardNum, photoData);
        return result;
    }

}
