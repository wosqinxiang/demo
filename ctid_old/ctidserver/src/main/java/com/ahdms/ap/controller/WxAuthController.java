package com.ahdms.ap.controller;

import com.ahdms.ap.common.HttpResponseBody;
import com.ahdms.ap.model.PersonInfo;
import com.ahdms.ap.service.WxAuthLoginService;
import com.ahdms.ap.utils.IpUtil;
import com.ahdms.ap.vo.WxMiniProgramVo;
import com.ahdms.ctidservice.util.WxFaceApiUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * WxAuthController class
 *
 * @author hetao
 * @date 2019/08/01
 */
@RestController
@RequestMapping(value = "/")
public class WxAuthController {

    @Value("${wx.mini.program.appId}")
    private String appId;
    @Value("${wx.mini.program.secretKey}")
    private String secretKey;
    @Resource
    private WxAuthLoginService wxAuthLoginService;
    
    @Autowired
    private WxFaceApiUtils wxFaceApiUtils;

    @RequestMapping(value = "/V216/wxLoginAuth", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "微信小程序授权接口",
            notes = "由小程序调微信后台，接收code传入可信身份认证后台，后台通过code+appid+secretKey去微信后台换取openid和session_key返回给小程序")
    public WxMiniProgramVo wxSmallRoutineLogin(HttpServletRequest request) {
        //拿到微信小程序传过来的code
        String code = request.getParameter("code");
        
        String ip = IpUtil.getClinetIpByReq(request);

        return wxAuthLoginService.wxAuthLogin(code, appId, secretKey, ip);
    }

    @RequestMapping(value = "/test/checkUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "查询账户信息", notes = "查询openID对应账户信息（姓名、身份证号码）")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "openID", value = "用户微信识别码", required = true, dataType = "string")})
    public HttpResponseBody checkUserInfo(@RequestParam("openID") String openID){
        // 接收前端（小程序）传参：openID，根据openID查询对应的用户信息（姓名，身份证号码）
        // 将查询结果返回给前端
        HttpResponseBody ret = new HttpResponseBody();
        PersonInfo personInfos = wxAuthLoginService.selectByOpenID(openID);
        if (personInfos==null){
            ret.setMessage("openID对应账户信息为空");
        } else {
            ret.setMessage("openID对应账户信息不为空");
        }
        ret.setData(personInfos);

        return  ret;
    }
    
    @RequestMapping(value = "/wxLoginAuth", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "微信小程序授权接口",
            notes = "由小程序调微信后台，接收code传入可信身份认证后台，后台通过code+appid+secretKey去微信后台换取openid和session_key返回给小程序")
    public WxMiniProgramVo oldwxSmallRoutineLogin(HttpServletRequest request) {
        //拿到微信小程序传过来的code
        String code = request.getParameter("code");
        
//        String ip = IpUtil.getClinetIpByReq(request);

        return wxAuthLoginService.oldwxAuthLogin(code, appId, secretKey);
    }
    
    @RequestMapping(value = "/V216/getPicToken", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取微信小程序图片的token",
            notes = "获取微信小程序图片的token")
    public HttpResponseBody getPicToken(@RequestParam Integer status) {
    	HttpResponseBody ret = new HttpResponseBody();
    	String accessToken = wxFaceApiUtils.getAccessToken(status);
    	if(null == accessToken){
    		ret.setCode(1);
    		ret.setMessage("token获取失败");
    	}else{
    		ret.setCode(0);
    		ret.setData(accessToken);
    	}
        return ret;
    }
    
}
