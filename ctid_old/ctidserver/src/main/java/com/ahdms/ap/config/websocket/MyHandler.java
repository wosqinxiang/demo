/**
 * Created on 2019年8月7日 by liuyipin
 */
package com.ahdms.ap.config.websocket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;




/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@Component
public class MyHandler extends TextWebSocketHandler {
	//在线用户列表
	private static final Map<String, WebSocketSession> users;
	private static final Logger LOGGER = LoggerFactory.getLogger(MyHandler.class);

	static {
		users = new HashMap<>();
	}
	//新增socket
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		long l1 = System.currentTimeMillis();
		LOGGER.info("成功建立连接"+LocalDateTime.now());
		String ID = session.getUri().toString().split("ID=")[1];
		System.out.println(ID);
		LOGGER.info("成功建立连接"+LocalDateTime.now()+"ID="+ID);
		if (ID != null) {
			users.put(ID, session);
			session.sendMessage(new TextMessage("成功建立socket连接"));
			System.out.println(ID);
			System.out.println(session);
		}
		System.out.println("当前在线人数："+users.size());
		System.out.println("建立连接时间："+(System.currentTimeMillis()-l1));
	}

	//接收socket信息
	@Override
	public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
		try{
			System.out.println("-----接收socket信息----------" );
			/*JSONObject jsonobject = JSONObject.fromObject(webSocketMessage.getPayload());   
			System.out.println(jsonobject.get("id"));
			System.out.println(jsonobject.get("message")+":来自"+(String)webSocketSession.getAttributes().get("WEBSOCKET_USERID")+"的消息");
			sendMessageToUser(jsonobject.get("id")+"",new TextMessage("服务器收到了，hello!"));*/
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 发送信息给指定用户
	 * @param clientId
	 * @param message
	 * @return
	 */
	public boolean sendMessageToUser(String clientId, TextMessage message) {
		if (users.get(clientId) == null) 
		{
//			System.out.println("用户不在线"+clientId);
			LOGGER.error("用户不在线"+clientId);
			return false;
			}
		WebSocketSession session = users.get(clientId);
		System.out.println("sendMessage:" + session);
		if (!session.isOpen()) {
//			System.out.println("session 已关闭" );
			LOGGER.error("session 已关闭");
			return false;
		}
		try {
			session.sendMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("socket发送信息失败");
			return false;
		}
		return true;
	}

	/**
	 * 广播信息
	 * @param message
	 * @return
	 */
	public boolean sendMessageToAllUsers(TextMessage message) {
		boolean allSendSuccess = true;
		Set<String> clientIds = users.keySet();
		WebSocketSession session = null;
		for (String clientId : clientIds) {
			try {
				session = users.get(clientId);
				if (session.isOpen()) {
					session.sendMessage(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
				allSendSuccess = false;
			}
		}

		return allSendSuccess;
	}


	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

		String ID = session.getUri().toString().split("ID=")[1];
		LOGGER.info("-----移除用户id：{}"+ID);
		if (session.isOpen()) {
			session.close();
			LOGGER.info("-----关闭session----------" );
		}
		LOGGER.error("连接出错");
		users.remove(ID);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//		Integer client = getClientId(session);
		String ID = session.getUri().toString().split("ID=")[1];
		LOGGER.info("连接已关闭：" + status+"   id="+ID);
		users.remove(ID);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 获取用户标识
	 * @param session
	 * @return
	 */
	private Integer getClientId(WebSocketSession session) {
		try {
			Integer clientId = (Integer) session.getAttributes().get("WEBSOCKET_USERID");
			return clientId;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 关闭连接
	 * @param clientId
	 * @param message
	 * @return
	 */
	public boolean closeConn(String clientId) {
		if (users.get(clientId) == null) return false;
		WebSocketSession session = users.get(clientId);
		LOGGER.info("sendMessage:" + session); 
		try {
			if (session.isOpen()) {
				session.close();
				users.remove(clientId);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	} 
}

