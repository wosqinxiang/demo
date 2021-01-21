package com.ahdms.billing.dao;

import java.util.List;
import java.util.Map;

import com.ahdms.billing.model.AdminUserInfo;
import com.ahdms.billing.vo.AdminUserInfoVO;

public interface AdminUserInfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(AdminUserInfo record);

    int insertSelective(AdminUserInfo record);

    AdminUserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AdminUserInfo record);

    int updateByPrimaryKey(AdminUserInfo record);

    List<AdminUserInfo> findAll();

	AdminUserInfo querySysUserByUserName(String userName);

	List<AdminUserInfoVO>  querySysUserPageList(Map<String, Object> map);

	int querySuperAdmin(); 

	String selectById(String userId);

	void lockUser(String userName);

	AdminUserInfoVO selectAllByKey(String id); 
 
}