package com.ahdms.billing.service;

import java.util.Map;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.HttpResponseBody;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.exception.BaseException;
import com.ahdms.billing.model.AdminUserInfo;
import com.ahdms.billing.vo.AdminUserInfoVO;


public interface SysUserService { 
    /**
               * 用户名是否被使用
     * 
     * @创建人 luotao
     * @创建时间 2017年9月7日 @创建目的【】 @修改目的【修改人：，修改时间：】
     * @param userName
     * @return
     * @throws Exception
     */
    boolean queryAdminUserInfoNameExist(String userName) throws BaseException;

    AdminUserInfoVO getUserByUserId(String id) throws BaseException;

	AdminUserInfo getLoginUser() throws BaseException; 

	GridModel<AdminUserInfoVO> querySysUserPageList(Map<String, Object> map, int page, int rows, Object object);

	Result<String> createUser(AdminUserInfo adminUserInfo, String role) throws BaseException;

	Result<String> delUser(String userId) throws BaseException;

	int queryAdminIsLock(String userName);

	Result<String> unlockUser(String userId);

	Result<String> EditUserPassWord(String oldPassword, String newPassword) throws Exception ;

	AdminUserInfoVO showLoginUser() throws BaseException;

    

}
