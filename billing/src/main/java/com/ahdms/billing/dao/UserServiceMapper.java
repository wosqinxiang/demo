package com.ahdms.billing.dao;

import com.ahdms.billing.model.ServiceSumCount;
import com.ahdms.billing.model.UserService;

import java.util.List;

import com.ahdms.billing.model.UserServiceForName;
import com.ahdms.billing.model.report.UserServiceForChannel;
import com.ahdms.billing.vo.UserServiceDetailVO;

import org.apache.ibatis.annotations.Param;

public interface UserServiceMapper {

    int deleteByPrimaryKey(String id);

    int insert(UserService record);

    int insertSelective(UserService record);

    UserService selectByPrimaryKey(String id);

    List<UserService> selectByUserId(String user_info_id);

    List<UserServiceForName> queryByUserId(String user_info_id);

    List<UserService> queryServiceDetailByUserId(@Param("userId") String userId,
                                                 @Param("typeId") String typeId);

    int updateByPrimaryKeySelective(UserService record);

    int updateByPrimaryKey(UserService record);

    List<UserService> findAll();

    List<UserServiceForChannel> findUserServiceForChannel();

    List<ServiceSumCount> selectUserServiceCountByUserId(String user_id);

    List<ServiceSumCount> selectUserServiceCountByServiceId(String user_id,String service_id);

	UserService selectByUserIdAndServiceId(@Param("userInfoId")String userInfoId,  @Param("serviceId")String serviceId);

	List<UserServiceDetailVO> queryByUserIdAndType(@Param("userInfoId")String userId,@Param("typeId") String typeId);

    List<UserService> findAllOmpUser();
}