package com.ahdms.billing.dao;

import java.util.List;
import java.util.Map;

import com.ahdms.billing.model.Authority;


public interface AuthorityDao {
    public int insert(Authority auth);

    public Authority queryById(String id);

    public List<Authority> queryByuserId(String userId);

    public void deleteByUserIdAndAuthority(Map<String, String> map);

    public void deleteByUserId(String userId);
    
}
