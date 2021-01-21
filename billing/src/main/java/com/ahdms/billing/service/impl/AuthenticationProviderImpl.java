package com.ahdms.billing.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ahdms.billing.dao.AdminUserInfoMapper;
import com.ahdms.billing.model.User;

import redis.clients.jedis.JedisCluster;

/**
 * Created by yeqiang on 11/14/16.
 */
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationProviderImpl.class);

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JedisCluster jediscluster;
	
	@Autowired
	AdminUserInfoMapper adminDao;
	
	private static String preKey = "JF_";

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		// 用户名：principal
		// 密码：credentials
		logger.debug("try to auth user:{}", token.getPrincipal());
		if (!StringUtils.isEmpty(token.getPrincipal()) && !StringUtils.isEmpty(token.getCredentials())) {  
			User u = (User) userDetailsService.loadUserByUsername((String) token.getPrincipal());
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if(encoder.matches( (CharSequence) token.getCredentials(), u.getPassword())) {
				//			if (CipherTool.md5((String) token.getCredentials(), u.getSalt()).equals(u.getPassword())) {
				token = new UsernamePasswordAuthenticationToken(u.getUsername(), token.getCredentials(),
						u.getAuthorities());
				jediscluster.set(preKey+(String) token.getPrincipal(), "0");
				logger.debug("userName:{} login ok", token.getPrincipal());
				logger.debug("pwd:{} login ok" + token.getCredentials());
				return token;
			}else {
				String key = preKey+(String) token.getPrincipal();
				if(jediscluster.exists(key)){
					jediscluster.incr(key);
				}else{
					jediscluster.set(key, "1");
				}
				jediscluster.expire(key, 1800);
				if(Integer.parseInt(jediscluster.get(key)) > 4) {
					adminDao.lockUser((String) token.getPrincipal());
				}
			}
		}
		logger.debug("user:{} login fail", token.getPrincipal());
		return null;// 登陆失败应该返回null，不能返回原先的token，返回不为null则认为登陆成功。。。。
	}

	@Override
	public boolean supports(Class<?> clazz) {
		if (clazz.equals(UsernamePasswordAuthenticationToken.class)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String e = encoder.encode("123456");
		System.out.println(e);
		System.out.println(encoder.matches("123456", e)); 
	}
}



