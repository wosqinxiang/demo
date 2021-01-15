package com.ahdms.ctidservice.config;

import java.security.PublicKey;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ctidservice.bean.IdCardInfoBean;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.util.AESUtil;
import com.ahdms.ctidservice.util.CalculateHashUtils;
import com.ahdms.ctidservice.util.OneWayEncryptUtil;
import com.ahdms.ctidservice.util.RSAUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Component
public class RabbitMqSender {
	Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);
	
	@Resource(name="ctidRabbitmqTemplate")
	private AmqpTemplate rabbitTemplate;
	
	@Value("${rabbitmq.queue.authRecord}")
	private String queueName;
	
	@Value("${rabbitmq.fanoutexchange.websocket}")
	private String fanoutexchange;
	
	@Value("${publicKey}")
	private String publicKey;
	
	@Value("${rabbitmq.queue.queueBtou}")
	private String btQueueName;
	
	@Autowired
	private TokenCipherService cipherService;
	
	public void sendTopicWS(String serialNum,String socketId,String result){
		try {
			logger.info(">>>{}发送消息:{}",fanoutexchange,socketId);
			JSONObject json = new JSONObject();
			json.put("serialNum", serialNum);
			json.put("socketId", socketId);
			json.put("result", result);
			rabbitTemplate.convertAndSend(fanoutexchange,"", json.toString());
		} catch (Exception e) {
			logger.error("消息发送失败！",e);
		}
	}
	
	public void send(AuthRecord authRecord){   
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		
		String name = authRecord.getName();
		String idCard = authRecord.getIdcard();
		
		String encodeIdCardInfo = cipherService.encodeIdCardInfo(name, idCard);
		String calculateHash = CalculateHashUtils.calculateHash(idCard.getBytes());
		
		authRecord.setName(encodeIdCardInfo);
		authRecord.setIdcard(calculateHash);
		authRecord.setAuthObject(authRecord.getAuthObject() == null ? Contents.AUTH_OBJECT_SELF : authRecord.getAuthObject());
		
		String json = JSONObject.fromObject(authRecord,config).toString();
		this.rabbitTemplate.convertAndSend(queueName,json);
		
		try {
			String aesKeyStr = AESUtil.genKeyAES();
			PublicKey serverPublicKey = RSAUtil.string2PublicKey(publicKey);
	        //用Server公钥加密AES秘钥
	        byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), serverPublicKey);
			String encryptName = OneWayEncryptUtil.encrypt( aesKeyStr,name);
			String encryptIdcard = OneWayEncryptUtil.encrypt( aesKeyStr,idCard);
			String encryptPic = OneWayEncryptUtil.encrypt( aesKeyStr, authRecord.getPic());
			authRecord.setName(encryptName);
			authRecord.setIdcard(encryptIdcard);
			authRecord.setPic(encryptPic);
			JSONObject jsonObject = JSONObject.fromObject(authRecord,config);
			jsonObject.put("aesKey", RSAUtil.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
			String redisData = jsonObject.toString();
			
			rabbitTemplate.convertAndSend(btQueueName,redisData);
			
//			jedisCluster.lpush(DateUtils.getNowTime(DateUtils.DATE_SMALL_STR), redisData);
		} catch (Exception e) {
			logger.error("认证数据存入redis失败！"+e.getMessage());
		}
	}
	
	public void sendAuthRecord(AuthRecord authRecord){  
		authRecord.setAuthObject(authRecord.getAuthObject() == null ? Contents.AUTH_OBJECT_SELF : authRecord.getAuthObject());
		
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		
		this.rabbitTemplate.convertAndSend(queueName, JSONObject.fromObject(authRecord,config).toString()); 
		
		try {
			String envelopeData = authRecord.getName();
			IdCardInfoBean idCardInfo = cipherService.decodeIdCardInfo(envelopeData);
			String aesKeyStr = AESUtil.genKeyAES();
			PublicKey serverPublicKey = RSAUtil.string2PublicKey(publicKey);
	        //用Server公钥加密AES秘钥
	        byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), serverPublicKey);
			String encryptName = OneWayEncryptUtil.encrypt( aesKeyStr,idCardInfo.getCardName());
			String encryptIdcard = OneWayEncryptUtil.encrypt( aesKeyStr,idCardInfo.getCardNum());
			String encryptPic = OneWayEncryptUtil.encrypt( aesKeyStr, authRecord.getPic());
			authRecord.setName(encryptName);
			authRecord.setIdcard(encryptIdcard);
			authRecord.setPic(encryptPic);
			JSONObject jsonObject = JSONObject.fromObject(authRecord,config);
			jsonObject.put("aesKey", RSAUtil.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
			String redisData = jsonObject.toString();
			
			rabbitTemplate.convertAndSend(btQueueName,redisData);
//			jedisCluster.lpush(DateUtils.getNowTime(DateUtils.DATE_SMALL_STR), redisData);
		} catch (Exception e) {
			logger.error("认证数据存入redis失败！"+e.getMessage());
		}
	}
	
}
