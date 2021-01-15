package com.ahdms.config.svs;

import com.ahdms.bean.model.SvsConfig;
import com.ahdms.bean.model.SvsUser;
import com.ahdms.dao.ISvsConfigMapper;
import com.ahdms.dao.ISvsUserMapper;
import com.ahdms.framework.core.commom.util.CollectionUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinxiang
 * @date 2021-01-03 14:17
 */
@Component
public class SvsConfigCache implements InitializingBean {

    @Autowired
    private ISvsUserMapper userMapper;

    @Autowired
    private ISvsConfigMapper configMapper;

    public Map<String, SvsConfig> svsCache = new ConcurrentHashMap<>();

    public List<String> accounts = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    public List<String> getAccounts(){
        return accounts;
    }

    public SvsConfig get(String account){
        return svsCache.get(account);
    }

    public boolean put(String account,SvsConfig svsConfig){
        svsCache.put(account,svsConfig);
        accounts.add(account);
        return true;
    }

    public void remove(String account){
        svsCache.remove(account);
        accounts.remove(account);
    }

    public boolean checkAccount(String account){
        if(CollectionUtils.isEmpty(accounts)){
            return false;
        }
        if(StringUtils.isNotBlank(account)){
            return accounts.stream().anyMatch(account::equals);
        }
        return false;
    }

    public void init(){
        List<SvsUser> svsUsers = userMapper.selectList(null);
        if(CollectionUtils.isNotEmpty(svsUsers)){
            svsUsers.forEach(svsUser -> {
                String account = svsUser.getAccount();
                SvsConfig svsConfig = configMapper.selectById(svsUser.getSvsConfigId());
                svsCache.put(account,svsConfig);
                accounts.add(account);
            });
        }
    }
}
