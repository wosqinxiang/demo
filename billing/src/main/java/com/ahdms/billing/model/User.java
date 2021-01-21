/**
 * Created on 2017年8月2日 by luotao
 */
package com.ahdms.billing.model;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
/**
 * 
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class User implements UserDetails {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    String id;
    String username;

    String password;
    String salt;

    boolean enabled;
    private List<Authority> authorities;

    public User(AdminUserInfo sysUser, List<Authority> authorityList) {
        this.id = sysUser.getId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        this.salt = sysUser.getSalt();
        this.enabled = 0 == sysUser.getIsLock() ? true : false;
        this.authorities = authorityList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object rhs) {
        return rhs instanceof User ? this.username.equals(((User) rhs).username) : false;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}
