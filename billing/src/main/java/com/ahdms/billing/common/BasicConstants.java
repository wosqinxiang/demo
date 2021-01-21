/**
 * Created on 2020年1月13日 by liuyipin
 */
package com.ahdms.billing.common;
/**
 * @Title 常量
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class BasicConstants {
    public static final String JF_REDIS_KEY = "JF_REDIS_KEY";
	//返回状态码
	public static final int INTRETURN_CODE_SUCCESS 		= 0; //成功
	public static final int INTRETURN_CODE_ERROR 			= 1; //失败
	
	public static final String QUEUE_TEST = "QUEUE_TEST_001";
 
    // 用户类型：1.后台超级管理用户，2.普通管理员
    public static final Integer RA_USERTYPE_ADMIN = 1;
    public static final Integer RA_USERTYPE_GENERAL = 2; 
    
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN"; // 管理员
    public static final String ROLE_NORMAL_ADMIN = "ROLE_NORMAL_ADMIN"; // 审计员
     
    // 用户状态 0：在用 1：停用
    public static final Integer USER_ENABLED = 0;
    public static final Integer USER_ENABLED_DISABLE = 1;
    
    public static final int SERVICE_IS_EXPIRED = 1; //用户服务已过期
    
    public static final int COUNT_SERVICE_ALL = 1;//1.按服务类型统计所有
//    public static final int COUNT_SERVICE_USER = 2;//2.按服务类型统计用户的使用次数
    public static final int COUNT_CHANNEL_ALL = 2;//3.按渠道类型统计所有
//    public static final int COUNT_CHANNEL_USER = 4;//4.按渠道类型统计用户的使用次数
     
    
}

