package com.ahdms.billing.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ahdms.billing.dao.AdminUserInfoMapper;
import com.ahdms.billing.dao.AuthorityDao;
import com.ahdms.billing.model.AdminUserInfo;
import com.ahdms.billing.model.Authority;
import com.ahdms.billing.model.User;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    AuthorityDao authorityDao;
    @Autowired
    AdminUserInfoMapper sysUserDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername="+userName);
        AdminUserInfo sysUser = sysUserDao.querySysUserByUserName(userName);
        if (null == sysUser) {
            logger.error("该用户名 username="+userName+"查找不到");
            throw new UsernameNotFoundException("该用户名 username="+userName+"查找不到");
        }
        List<Authority> authorityList = authorityDao.queryByuserId(sysUser.getId());
        return new User(sysUser, authorityList);
    }
}
