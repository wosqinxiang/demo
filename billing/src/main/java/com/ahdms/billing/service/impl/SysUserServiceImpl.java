package com.ahdms.billing.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.HttpResponseBody;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.AdminUserInfoMapper;
import com.ahdms.billing.dao.AuthorityDao;
import com.ahdms.billing.exception.BaseException;
import com.ahdms.billing.exception.ErrorCode;
import com.ahdms.billing.model.AdminUserInfo;
import com.ahdms.billing.model.Authority;
import com.ahdms.billing.service.SysUserService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.utils.Util;
import com.ahdms.billing.vo.AdminUserInfoVO;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import redis.clients.jedis.JedisCluster;


@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

	private final static Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
	@Autowired
	AdminUserInfoMapper sysUserDao;
	@Autowired
	AuthorityDao authorityDao; 
	
	@Autowired
	JedisCluster jediscluster;

	@Override
	public boolean queryAdminUserInfoNameExist(String userName) throws BaseException {
		boolean flag = false;
		if (Util.isEmpty(userName)) {
			throw new BaseException(ErrorCode.ADD_USER_USERNAME_NULL);
		} else {
			AdminUserInfo sysUser = sysUserDao.querySysUserByUserName(userName);
			if (null != sysUser) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public AdminUserInfoVO getUserByUserId(String id) throws BaseException {
		AdminUserInfoVO sysUser = sysUserDao.selectAllByKey(id);
		if (null == sysUser) {
			throw new BaseException("user is not found");
		}
		return sysUser;
	}

	@Override
	public AdminUserInfo getLoginUser() throws BaseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if ("anonymousUser".equals(userName)) {
			throw new BaseException("userName is anonymousUser");
		}
		AdminUserInfo sysUser = sysUserDao.querySysUserByUserName(userName);
		if (null == sysUser) {
			throw new BaseException("username " + userName + " is not found");
		}
		return sysUser;
	} 


	@Override
	public GridModel<AdminUserInfoVO> querySysUserPageList(Map<String, Object> map, int page, int rows, Object object) {
		 GridModel<AdminUserInfoVO> gridModel = new GridModel<AdminUserInfoVO>();
	        Page  pages = PageHelper.startPage(page, rows);
	        List<AdminUserInfoVO> list = sysUserDao.querySysUserPageList(map); 
//	        PageInfo<AdminUserInfo> pageInfo = new PageInfo<>(list);
	        gridModel.setPage(pages.getPageNum());
	        gridModel.setRecords((int) pages.getTotal());
	        gridModel.setTotal(pages.getPages());
	        gridModel.setRows(list);
	        
//		PageBounds pageBounds = new PageBounds(page, rows);
//		PageList<AdminUserInfo> PageList = sysUserDao.querySysUserPageList(map, pageBounds); 
//		GridModel<AdminUserInfo> girdModel = new GridModel<AdminUserInfo>();
//		girdModel.setPage(PageList.getPaginator().getPage());
//		girdModel.setRecords(PageList.getPaginator().getTotalCount());
//		girdModel.setTotal(PageList.getPaginator().getTotalPages());
//		girdModel.setRows(PageList);
		return gridModel;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Result<String> createUser(AdminUserInfo adminUserInfo, String role) throws BaseException {
		Result<String> hb = new Result<String>();
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);
		if (queryAdminUserInfoNameExist(adminUserInfo.getUsername())) {
			hb.setMessage("用户名已存在");
			throw new BaseException("用户名已存在");
		}
		try {
			sysUserDao.insertSelective(adminUserInfo);
			Authority authority = new Authority();
			authority.setAuthority(role);
			authority.setUserId(adminUserInfo.getId());
			authority.setId(UUIDGenerator.getUUID());
			authorityDao.insert(authority); 
			hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			hb.setMessage(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return hb;
	}

	@Override
	public Result<String> delUser(String userId) throws BaseException {
		Result<String> hb = new Result<>();
		String authrity = sysUserDao.selectById(userId);
		if(authrity.equals(BasicConstants.ROLE_SUPER_ADMIN)) {
			if(sysUserDao.querySuperAdmin()>1) {
				sysUserDao.deleteByPrimaryKey(userId);
				authorityDao.deleteByUserId(userId);
			}else {
				throw new BaseException("最后一名超级管理员，不可删除！");
			}
		}else {
			sysUserDao.deleteByPrimaryKey(userId);
			authorityDao.deleteByUserId(userId);
		}
		hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		return hb;
	}

	@Override
	public int queryAdminIsLock(String userName) {
		AdminUserInfo admininfo = sysUserDao.querySysUserByUserName(userName);
		if(null != admininfo && admininfo.getIsLock() == 1) { 
			return 1;
		}else {
			return 0;
		}
	}

	@Override
	public Result<String> unlockUser(String userId) {
		Result<String> hb = new Result<>(); 
		AdminUserInfo admininfo = sysUserDao.selectByPrimaryKey(userId); 
		admininfo.setIsLock(0);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		admininfo.setPassword(encoder.encode("000000")); //默认密码 
		sysUserDao.updateByPrimaryKeySelective(admininfo);
		jediscluster.del(admininfo.getUsername());
		hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		return hb;
	}

	@Override
	public Result<String> EditUserPassWord(String oldPassword, String newPassword) throws Exception {
		Result<String> hb = new Result<>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		AdminUserInfo admin = getLoginUser();
		if(!encoder.matches(oldPassword , admin.getPassword())) {
			throw new Exception("ERROR PASSWORD!");
		}else{
			admin.setPassword(encoder.encode(newPassword));
			sysUserDao.updateByPrimaryKeySelective(admin);
		}
		hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		hb.setData("密码修改成功，请重新登录！");
		return hb;
	}

	@Override
	public AdminUserInfoVO showLoginUser() throws BaseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if ("anonymousUser".equals(userName)) {
			throw new BaseException("userName is anonymousUser");
		}
		AdminUserInfo sysUser = sysUserDao.querySysUserByUserName(userName);
		if (null == sysUser) {
			throw new BaseException("username " + userName + " is not found");
		}
		AdminUserInfoVO avo = new AdminUserInfoVO();
		List<Authority> list = authorityDao.queryByuserId(sysUser.getId());
		avo.setUsername(sysUser.getUsername());
		avo.setAuthority(list.get(0).getAuthority());
		avo.setIsLock(sysUser.getIsLock());
		
		return avo;
		
	}


}
