/**
 * Created on 2019年12月19日 by liuyipin
 */
package com.ahdms.ap.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ahdms.ap.dao.AuthBizDao;
import com.ahdms.ap.model.AuthBiz;
import com.ahdms.ap.service.WechatService;
import com.ahdms.ap.utils.DateUtils;
import com.ahdms.ap.vo.TemplateDataVo;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisCluster;

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
public class WechatPushJob  implements SimpleJob {

	@Autowired
	private AuthBizDao authBizDao;

	@Value("${wechat.push.period}")
	private int period;

	@Value("${wechat.push.overdue.templateId}")
	private String templateId;

	@Autowired
	private WechatService wechatService;
	
	@Resource(name="ctidJedis")
	private JedisCluster jedisCluster;

	Logger logger = LoggerFactory.getLogger(WechatPushJob.class);

	@Override
	public void execute(ShardingContext content) {
		//查询远程认证中被认证方未认证的中转信息（已过期）
		long l = new Date().getTime() - period*60*60*1000;

		Date d = DateUtils.TimestampToDate(l);
		List<AuthBiz> lists = authBizDao.queryOverDue(d);
		for (AuthBiz authBiz : lists) {
			//			List<TemplateDataVo> keywords = new ArrayList<TemplateDataVo>();
			////			keywords.add(new TemplateDataVo("Idcard", "")); 
			//			keywords.add(new TemplateDataVo("phrase3", " "));
			//			keywords.add(new TemplateDataVo("time2", DateUtils.format(authBiz.getCreateTime())));
			//			keywords.add(new TemplateDataVo("time5", " "));
			//			keywords.add(new TemplateDataVo("name4", " "));
			//			keywords.add(new TemplateDataVo("name1", "认证过期"));
			List<TemplateDataVo> keywords = new ArrayList<TemplateDataVo>();  
			keywords.add(new TemplateDataVo("thing1", "远程认证")); 
			keywords.add(new TemplateDataVo("date2", DateUtils.format(authBiz.getCreateTime()))); 
			keywords.add(new TemplateDataVo("phrase3", "认证过期"));  
			String pushResult = wechatService.pushOneUser("", jedisCluster.get(authBiz.getSerialNum()), templateId, keywords);
			if (!JSONObject.fromObject(pushResult).get("errcode").equals(0)) {
				logger.error("消息推送失败" + authBiz.getSerialNum());
			} 
			authBiz.setIsCallback(0);
			authBizDao.updateByPrimaryKey(authBiz);
		}
	} 
}

