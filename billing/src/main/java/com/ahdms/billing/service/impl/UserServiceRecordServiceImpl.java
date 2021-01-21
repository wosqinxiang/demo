package com.ahdms.billing.service.impl;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ServiceInfoMapper;
import com.ahdms.billing.dao.SpecialLineInfoMapper;
import com.ahdms.billing.dao.UserServiceRecordMapper;
import com.ahdms.billing.model.ServiceInfo;
import com.ahdms.billing.model.UserServiceRecord;
import com.ahdms.billing.service.UserServiceRecordService;
import com.ahdms.billing.vo.SpecialLineVO;
import com.ahdms.billing.vo.UserServiceRecordVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class UserServiceRecordServiceImpl implements UserServiceRecordService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceServiceImpl.class);
    @Autowired
    UserServiceRecordMapper userServiceRecordMapper;
    
    @Autowired
	private SpecialLineInfoMapper specialMapper;
    
    @Autowired
    private ServiceInfoMapper serviceInfoMapper;
    
    @Override
    public int addUserServiceRecord(UserServiceRecord userServiceRecord) {
        int resultCode;
        try {
            resultCode=userServiceRecordMapper.insert(userServiceRecord);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultCode=-1;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return resultCode;
    }

    @Override
    public Result<List<UserServiceRecordVO>> selectByUserId(String userId) {
        List<UserServiceRecordVO> userInfo= userServiceRecordMapper.selectByUserId(userId);
        for(UserServiceRecordVO vo : userInfo){
        	SpecialLineVO sl = specialMapper.selectByCode(vo.getSpecialName());
        	if(sl != null){
        		vo.setProviderName(sl.getProviderName());
            	vo.setSpecialName(sl.getName());
        	}
        	ServiceInfo si = serviceInfoMapper.queryByServiceEncode(vo.getServiceName());
        	if(si != null){
        		vo.setServiceName(si.getServiceName());
            	vo.setTypeName(si.getTypeName());
        	}
        }
        return Result.success(userInfo);
    }

    @Override
    public Result<List<UserServiceRecord>> queryServiceHistoryRecord(String userId) {
        Result<List<UserServiceRecord>> result=new Result<>();
        List<UserServiceRecord> userInfo= userServiceRecordMapper.queryServiceHistoryRecord(userId);
        result.setData(userInfo);
        return result;
    }
}
