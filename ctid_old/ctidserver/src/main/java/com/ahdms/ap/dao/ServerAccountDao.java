package com.ahdms.ap.dao;

import java.util.List;

import com.ahdms.ap.model.ServerAccount; 

public interface ServerAccountDao {
    int deleteByPrimaryKey(String id);

    int insert(ServerAccount record);

    int insertSelective(ServerAccount record);

    ServerAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ServerAccount record);

    int updateByPrimaryKey(ServerAccount record);

	ServerAccount selectServer(String name, String password);

	ServerAccount selectByName(String serverAccount);

	List<ServerAccount> select();
}