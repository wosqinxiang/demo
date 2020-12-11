package com.ahdms.framework.core.context;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
public interface HeaderConstants {

	String DMS_TOKEN = "x-dms-token";
	String DMS_USER_ID = "x-dms-user";
	String DMS_ROLE_ID = "x-dms-role";
	String DMS_USER_AGENT = "x-dms-ua";
	String DMS_CLIENT_IP = "x-dms-ip";

	String AUTH_TOKEN = "x-auth-token";
	String USER_ID = "x-user-id";
	String ROLE_ID = "x-role-id";
	String USER_AGENT = "x-client-ua";
	String CLIENT_IP = "x-client-ip";
	String REQUEST_ID = "x-request-id";
}
