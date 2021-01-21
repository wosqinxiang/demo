package com.ahdms.billing.model;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String authority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
