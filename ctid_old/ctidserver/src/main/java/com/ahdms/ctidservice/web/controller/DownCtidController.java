/**
 * <p>Title: DownCtidController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年7月29日
 * @version 1.0
 */
package com.ahdms.ctidservice.web.controller;

import com.ahdms.ctidservice.bean.CtidRequestBean;
import com.ahdms.ctidservice.common.IpUtils;
import com.ahdms.ctidservice.constants.CtidConstants;
import com.ahdms.ctidservice.db.model.CtidLog;
import com.ahdms.ctidservice.service.CtidLogService;
import com.ahdms.ctidservice.service.CtidManageService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: DownCtidController 网证下载接口</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年7月29日
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class DownCtidController {
    Logger logger = LoggerFactory.getLogger(DownCtidController.class);

    @Autowired
    private CtidManageService ctidManageServcie;

    @Autowired
    private CtidLogService ctidLogService;

    /**
     *
     * <p>Title: 网证下载申请接口</p>
     * <p>Description: 网证下载申请接口,返回业务流水号以及随机数</p>
     *
     */
    @RequestMapping("/ctid/downCtidApply")
    public CtidResult downCtidApply(@RequestBody CtidRequestBean dcaBean) {

        return ctidManageServcie.downCtidApply(dcaBean);

    }

    /**
     *
     * <p>Title: 网证下载请求</p>
     * <p>Description: 网证下载请求</p>
     * @param dcaBean
     * @return
     */
    @RequestMapping("/ctid/downCtidRequest")
    public CtidResult downCtidRequest(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageServcie.downloadCtidRequest(dcaBean,ipAddress);
        return getCtidResult(dcaBean, ipAddress, result);
    }

    /**
     *
     * <p>Title: </p>
     * <p>Description: 通过PC端控件网证下载</p>
     * @param dcaBean
     * @return
     */
    @RequestMapping("/ctid/downCtidInfo")
    public CtidResult downCtidInfo(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageServcie.downCtidInfo(dcaBean,ipAddress);
        return getCtidResult(dcaBean, ipAddress, result);
    }

    private CtidResult getCtidResult(@RequestBody CtidRequestBean dcaBean, String ipAddress, CtidResult result) {
        final Object sfxxBean = result.getData();
        if (result.getCode() != 0 && result.getCode() != CtidConstants.CTID_RESULT_REVOKED_CODE) {
            result.setData(null);
        }

        ctidLogService.insertSelective(dcaBean, result, ipAddress, CtidLog.DOWNLOAD_TYPE, sfxxBean);

        return result;
    }

    /**
     * 保存网证编号以及过期时间
     * @param dcaBean
     * @return
     */
    @RequestMapping("/ctid/saveCtidNum")
    public CtidResult saveCtidNum(@RequestBody CtidRequestBean dcaBean) {

        return ctidManageServcie.saveCtidNum(dcaBean);
    }

}
