package com.ahdms.ctidservice.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.ahdms.ap.config.FastDFSTool;
import com.ahdms.ap.config.websocket.MyHandler;
import com.ahdms.ap.dao.AuthBizDao;
import com.ahdms.ap.dao.AuthRecordDao;
import com.ahdms.ap.model.AuthBiz;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ctidservice.common.Base64Utils;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

@Component
public class RabbitMqReceiver {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
	
	@Autowired
	private AuthRecordDao authRecordDao;

	@Autowired
	private FastDFSTool FastDFSTool;
	
	@Autowired
	private MyHandler socketHandler;
	
	@Autowired
	private AuthBizDao abdao;

	@RabbitListener(queues = "${rabbitmq.queue.authRecord}",containerFactory="ctidFactory")
	public void process(String jsonStr) {
		try {
			String[] dateFormats = new String[] { "yyyy-MM-dd HH:mm:ss" };
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));
			AuthRecord record = (AuthRecord) JSONObject.toBean(JSONObject.fromObject(jsonStr), AuthRecord.class);
			String facePic = record.getPic();
			if(StringUtils.isNotBlank(facePic)){
				record.setPic(picToFtp(facePic));
			}
			authRecordDao.insertSelective(record);
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@RabbitListener(queues = "${rabbitmq.fanoutName.websocket}",containerFactory="ctidFactory")
	public void test(String jsonStr) {
		logger.info("消费消息>>>"+jsonStr);
		JSONObject json = JSONObject.fromObject(jsonStr);
		String serialNum = json.getString("serialNum");
		String socketId = json.getString("socketId");
		String result = json.getString("result");
		boolean sendresult = socketHandler.sendMessageToUser(socketId, new TextMessage(result));
		if (sendresult) {
			AuthBiz ab = abdao.queryBySerial(serialNum);
			ab.setIsCallback(0);
			abdao.updateByPrimaryKey(ab);
		}
	}

	private String picToFtp(String facePic) throws Exception {
		String uploadIdCard = FastDFSTool.upload(Base64Utils.decode(facePic), "jpg");
		return uploadIdCard;
	}

}
