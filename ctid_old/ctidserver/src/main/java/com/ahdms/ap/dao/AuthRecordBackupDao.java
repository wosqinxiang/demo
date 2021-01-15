package com.ahdms.ap.dao;

import java.util.List;

import com.ahdms.ap.model.AuthRecordBackup;

public interface AuthRecordBackupDao {
    
    int insertSelective(AuthRecordBackup record);
    
    int clearAuthRecordBackupTable();

	List<AuthRecordBackup> queryBackupData(Integer days);
}