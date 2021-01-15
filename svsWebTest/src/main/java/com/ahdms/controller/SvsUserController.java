package com.ahdms.controller;

import com.ahdms.bean.model.SvsUser;
import com.ahdms.bean.req.SvsUserPageReqVo;
import com.ahdms.bean.req.SvsUserReqVo;
import com.ahdms.bean.rsp.SvsUserPageRspVo;
import com.ahdms.bean.rsp.SvsUserRspVo;
import com.ahdms.framework.core.commom.page.PageResult;
import com.ahdms.framework.core.commom.util.PageUtils;
import com.ahdms.result.ApiResult;
import com.ahdms.service.ISvsUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author qinxiang
 * @date 2021-01-03 9:48
 */
@RestController
@RequestMapping("/api/user")
@Api("svsUser")
public class SvsUserController {

    @Autowired
    private ISvsUserService userService;

    @PostMapping("account")
    @ApiOperation(value = "新增用户信息", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult addUser(@RequestBody SvsUserReqVo userReqVo) throws Exception{
        userService.addUser(userReqVo);
        return ApiResult.success("success");
    }

    @PatchMapping("account")
    @ApiOperation(value = "修改用户信息", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult updateUser(@RequestBody SvsUserReqVo userReqVo) throws Exception{
        userService.updateUser(userReqVo);
        return ApiResult.success("success");
    }

    @GetMapping("account")
    @ApiOperation(value = "根据ID查询用户信息", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResult<SvsUserRspVo> queryUser(@RequestParam String id) throws Exception{
        SvsUserRspVo svsUserRspVo = userService.queryUser(id);
        return ApiResult.success(svsUserRspVo);
    }

    @PostMapping("account/page")
    @ApiOperation(value = "分页查询用户信息", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult<PageResult<SvsUserPageRspVo>> pageUser(@RequestBody SvsUserPageReqVo reqVo) throws Exception{
        IPage<SvsUserPageRspVo> svsUserIPage = userService.pageUser(reqVo);
        return ApiResult.success(PageUtils.toPageResult(svsUserIPage));
    }

    @DeleteMapping("account")
    @ApiOperation(value = "根据ID删除用户信息", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ApiResult deleteUser(@RequestParam String id) throws Exception{
        userService.deleteUser(id);
        return ApiResult.success("success");
    }

}
