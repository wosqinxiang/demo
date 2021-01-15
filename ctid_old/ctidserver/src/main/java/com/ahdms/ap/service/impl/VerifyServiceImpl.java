/**
 * Created on 2019年8月2日 by liuyipin
 */
package com.ahdms.ap.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.jf.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.common.HttpResponseBody;
import com.ahdms.ap.config.FastDFSTool;
import com.ahdms.ap.config.websocket.MyHandler;
import com.ahdms.ap.dao.AuthBizDao;
import com.ahdms.ap.dao.AuthCountDao;
import com.ahdms.ap.dao.AuthRecordDao;
import com.ahdms.ap.dao.BoxInfoDao;
import com.ahdms.ap.dao.PersonInfoDao;
import com.ahdms.ap.model.AuthBiz;
import com.ahdms.ap.model.AuthCount;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ap.model.BoxInfo;
import com.ahdms.ap.model.PersonInfo;
import com.ahdms.ap.model.ServerAccount;
import com.ahdms.ap.service.VerifyService;
import com.ahdms.ap.service.WechatService;
import com.ahdms.ap.utils.DateUtils;
import com.ahdms.ap.utils.UUIDGenerator;
import com.ahdms.ap.vo.AuthResponseVo;
import com.ahdms.ap.vo.OfflineParamVO;
import com.ahdms.ap.vo.OnlineVO;
import com.ahdms.ap.vo.SignResponseVo;
import com.ahdms.ap.vo.TemplateDataVo;
import com.ahdms.ctidservice.bean.IdCardInfoBean;
import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.config.RabbitMqSender;
import com.ahdms.ctidservice.db.dao.CtidInfosMapper;
import com.ahdms.ctidservice.db.model.CtidInfos;
import com.ahdms.ctidservice.service.ApiAuthService;
import com.ahdms.ctidservice.service.AuthCtidService;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.util.CalculateHashUtils;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.jf.client.JFClient;
import com.wangpos.wopensdk.model.ReturnData;
import com.wangpos.wopensdk.tools.WPosOpenRequest;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisCluster;

/**
 * @Title
 * @Description
 * @Copyright
 *            <p>
 *            Copyright (c) 2015
 *            </p>
 * @Company
 *          <p>
 *          迪曼森信息科技有限公司 Co., Ltd.
 *          </p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VerifyServiceImpl implements VerifyService {
	private static final Logger LOGGER = LoggerFactory.getLogger(VerifyServiceImpl.class);

	private static final String key = null;

//	@Autowired
//	private CacheService cacheService;

	@Autowired
	private AuthCountDao countDao;

	@Autowired
	private PersonInfoDao personDao;

	@Autowired
	private AuthRecordDao recordDao;

	@Autowired
	private AuthBizDao abdao;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApiAuthService apiAuthService;

	@Resource(name="ctidJedis")
	private JedisCluster jedisCluster;

	@Autowired
	private AuthCtidService authCtidService;

	@Autowired
	private FastDFSTool FastDFSTool;

	@Value("${redis.device.count}")
	private String deviceCount;

	@Value("${transfer.service.signUrl}")
	private String signUrl;

	@Value("${device.verify.max.seize}")
	private int maxSize;

	@Value("${ctid.pic.location}")
	private String location;

	@Value("${ftp.url}")
	private String ftpUrl;

	@Value("${ftp.username}")
	private String userName;

	@Value("${ftp.password}")
	private String password;

	@Value("${ftp.pic.location}")
	private String ftpLoc;

	@Autowired
	private RabbitMqSender mqSend;

	@Autowired
	private CtidInfosMapper ctidDao;

	@Autowired
	private TokenCipherService cipherService;

	@Value("${transfer.skip.url}")
	private String skipurl;

	@Autowired
	private MyHandler handler;

	@Autowired
	private WechatService wechatService;

	@Value("${wechat.push.templateId}")
	private String templateId;

	@Value("${wechat.push.period}")
	private	int pushPeriod;

	@Value("${ftf.serveraccount.account}")
	private String ftfAccount;
	// @Autowired
	// private HttpAsync HttpAsync; 
	@Value("${wpos.verify.AppID}") 
	private String appid;
	@Value("${wpos.verify.Secret}") 
	private String secret;
	@Value("${wpos.verify.TOKEN}") 
	private String token;
	@Value("${wpos.verify.bizCode}") 
	private String bizCode;
	@Value("${wpos.verify.deviceEn}") 
	private String deviceEn;
	@Value("${wpos.verify.serviceKey}") 
	private String serviceKey;

	private WPosOpenRequest wPosOpenRequest;
	
	@Autowired
	private JFClient jfClient;
	
//	@Autowired
//	private BoxInfoDao boxInfoDao;

	@Override
	public OnlineVO simpleVerify(String name, String iDcard, String facePic, String devicName, String password,String ip)
			throws Exception {
		OnlineVO vo = new OnlineVO();
		// 查询服务账号是否存在，不存在则返回错误
//		ServerAccount account = cacheService.selectServer(devicName, password);
//		if (null == account) {
//			LOGGER.error("服务账号信息有误。");
//			throw new Exception("服务账号信息有误。");
//		}
		// 判断账户是否超过使用次数
//		checkAuthCount(devicName);
		
		//计费查询
		JFServiceEnum jfServiceEnum = StringUtils.isBlank(facePic)?JFServiceEnum.CARD_AUTH:JFServiceEnum.CARD_FACE_AUTH;
		JFResult<JFInfoModel> jf = jfClient.jf(devicName,password, jfServiceEnum,JFChannelEnum.API,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		String serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		CtidResult<CtidAuthReturnBean> result = auth(name, iDcard, facePic,specialCode);
		if (result.getCode() == 1) {
			throw new Exception("认证服务器异常！" + result.getMessage());
		}
		CtidAuthReturnBean authData = result.getData();
		String authMode = authData.getAuthMode();
		String authResult = authData.getAuthResult();
		String authDesc = authData.getAuthDesc();
		String authNum = authData.getAuthNum();
		String token = authData.getToken();

		LOGGER.debug("-------------------authDesc-------------------: {}" + authDesc);
		// 保存业务信息
		int authResults = Contents.AUTH_RESULT_FALSE;
		int ctidType = 0;
		int isCtid = 1;
		if ("0x06".equals(authMode)) {
			// 有网证
			isCtid = Contents.IS_CTID_TRUE;
			// 网证服务返回该用户有网证信息
			ctidType = Contents.CTID_TYPE_ONE;
		} else if ("0x40".equals(authMode)) {
			// 两项
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_TWO;
			facePic = "";
		} else if ("0x42".equals(authMode)) {
			// 两项+人像
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_THREE;
		}
		if (authResult.equals("true")) {
			authResults = Contents.AUTH_RESULT_TRUE;
			// t2:查询二项信息+openID是否存在，不存在则保存

			String calculateHash = CalculateHashUtils.calculateHash(iDcard.getBytes());
			PersonInfo person = personDao.selectByIdcard(calculateHash);
			if (null == person) {
				String encodeIdCardInfo = cipherService.encodeIdCardInfo(name, iDcard);
				person = new PersonInfo(UUIDGenerator.getSerialId(), Contents.INFO_SOURCE_OTHER, null, new Date(),
						calculateHash, null, encodeIdCardInfo, null, isCtid, picToFtp(facePic));
				personDao.insertSelective(person);
			} else {
				if(person.getIsCtid().equals(1)) {
					person.setIsCtid(isCtid);
					personDao.updateByPrimaryKeySelective(person);
				}

			}
		}
//		String serialNum = getRandomString(32);
		String serialNum = authData.getBsn();

		// 从网证服务返回值中获取认证模式，认证结果，插入一条认证记录
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), Contents.INFO_SOURCE_OTHER, name, iDcard,
				Contents.AUTH_TYPE_SIMPLE, ctidType, authResults, serverAccount, new Date(), serialNum,
				facePic, null, null, null, authDesc, Contents.AUTH_OBJECT_SELF, null);
		// recordDao.insertSelective(ar);
		mqSend.send(ar);
		
		//发送计费业务日志
		jfClient.send(serverAccount, jfServiceEnum, JFChannelEnum.API, authResults, authDesc,serialNum,specialCode,devicName);

		
		vo.setAuthResult(authResults);
		vo.setSerialNum(serialNum);
		/*
		 * vo.setIdcard(iDcard); vo.setName(name);
		 */
		vo.setAuthNum(authNum);
		vo.setVerifyDate(DateUtils.format(ar.getCreateTime()));
		vo.setServerName(serverAccount);
		vo.setAuthDesc(authDesc);
		vo.setToken(token); 
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> onlineVerify(String name, String iDcard, String facePic, int type, String openID,
			String serialNum, String location,String ip) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// t1:查询对应序列号的服务账号信息
		boolean b = true;
		String s = jedisCluster.get(serialNum);
		if (null == s) {
			jedisCluster.set(serialNum, "true", "NX", "EX", 1800000);
		} else {
			LOGGER.error("该二维码已被使用，请重新扫码。");
			throw new Exception("该二维码已被使用，请重新扫码。");
		}
		AuthBiz ab = abdao.queryBySerial(serialNum);
		if (null == ab) {
			LOGGER.error("serialNum错误，非系统生成！");
			throw new Exception("无效二维码！");
		}
		// 判断账户是否超过使用次数
		String serverAccount = ab.getServerAccount();
//		checkAuthCount(serverAccount);

		if (StringUtils.isBlank(location) && serialNum.substring(0, 2).equals("05")) {
			LOGGER.error("需要获取位置信息。");
			throw new Exception("需要获取位置信息。");
		}
		
		//计费查询
		JFServiceEnum jfServiceEnum = StringUtils.isBlank(facePic)?JFServiceEnum.CARD_AUTH:JFServiceEnum.CARD_FACE_AUTH;
		JFResult<JFInfoModel> jf = jfClient.jf(serverAccount, jfServiceEnum,JFChannelEnum.API,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();

		CtidResult<CtidAuthReturnBean> cresult = auth(name, iDcard, facePic,specialCode);
		if (cresult.getCode() == 1) {
			throw new Exception("认证服务器异常！" + cresult.getMessage());
		}
		CtidAuthReturnBean authData = cresult.getData();
		String authMode = authData.getAuthMode();
		String authResult = authData.getAuthResult();
		String authDesc = authData.getAuthDesc();

		LOGGER.debug("-------------------authDesc-------------------: {}" + authDesc);

		// t4:保存业务信息
		int authResults = Contents.AUTH_RESULT_FALSE;
		int ctidType = 0;
		int isCtid = 1;
		if ("0x06".equals(authMode)) {
			// 有网证
			isCtid = Contents.IS_CTID_TRUE;
			// 网证服务返回该用户有网证信息
			ctidType = Contents.CTID_TYPE_ONE;
		} else if ("0x40".equals(authMode)) {
			// 两项
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_TWO;
		} else if ("0x42".equals(authMode)) {
			// 两项+人像
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_THREE;
		}
		if (authResult.equals("true")) {
			authResults = Contents.AUTH_RESULT_TRUE;
			// t2:查询二项信息+openID是否存在，不存在则保存
			PersonInfo person = personDao.selectByOpenID(openID);
			String encodeIdCardInfo = cipherService.encodeIdCardInfo(name, iDcard);
			String calculateHash = CalculateHashUtils.calculateHash(iDcard.getBytes());
			if (null == person) {
				person = new PersonInfo(UUIDGenerator.getSerialId(), type, openID, new Date(), calculateHash, null,
						encodeIdCardInfo, null, isCtid, picToFtp(facePic));
				personDao.insertSelective(person);
			} else {
				if(person.getIsCtid().equals(1)) {
					person.setIsCtid(isCtid);
					personDao.updateByPrimaryKeySelective(person);
				}
			}
		} else {
			b = false;
		}
		String bsn = authData.getBsn();
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), type, name, iDcard, Contents.AUTH_TYPE_ONLINE,
				ctidType, authResults, ab.getServerDesc(), new Date(), bsn, facePic, null, openID, location,
				authDesc, Contents.AUTH_OBJECT_SELF, null);
		// recordDao.insertSelective(ar);

		mqSend.send(ar);
		
		//发送计费业务日志
		jfClient.send(serverAccount, jfServiceEnum, JFChannelEnum.API, authResults, authDesc,bsn,specialCode,serverAccount);

		// t5:将结果通过回调地址返回
		OnlineVO vo = new OnlineVO();
		vo.setAuthResult(authResults);
		vo.setSerialNum(serialNum);
		vo.setIdcard(iDcard);
		vo.setName(name);
		vo.setPic(facePic);
		vo.setVerifyDate(DateUtils.format(ar.getCreateTime()));
		vo.setServerName(ab.getServerDesc());
		if (serialNum.substring(0, 2).equals("05")) {
			vo.setLocation(location);
		}
		String result = JSONObject.fromObject(vo).toString();
		if (ab.getUrl().subSequence(0, ab.getUrl().indexOf(":")).equals("https")) {
			httpsRequestToString(ab.getUrl(), "POST", result);
		} else {
			httpRequest(ab.getUrl(), "POST", result);
		}
		// httpRequest(ab.getUrl(), "POST", result);
		// 修改回调信息
		// HttpAsync.httpPost(ab.getUrl() , result);
		ab.setIsCallback(0);
		abdao.updateByPrimaryKey(ab);
		map.put("result", b);
		map.put("authDesc", authDesc);
		return map;
	}

	private static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod(requestMethod);
			conn.connect();
			// 往服务器端写内容 也就是发起http请求需要带的参数
			if (null != outputStr) {
				OutputStream os = conn.getOutputStream();
				os.write(("&result=" + outputStr).getBytes("utf-8"));
				os.close();
			}

			// 读取服务器端返回的内容
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	private static String httpsRequestToString(String path, String method, String body) {
		if (path == null || method == null) {
			return null;
		}
		String response = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		HttpsURLConnection conn = null;
		try {
			// 创建SSLConrext对象，并使用我们指定的信任管理器初始化
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			TrustManager[] tm = { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			} };
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上面对象中得到SSLSocketFactory
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(path);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			// 设置请求方式（get|post）
			conn.setRequestMethod(method);

			// 有数据提交时
			if (null != body) {
				OutputStream outputStream = conn.getOutputStream();
				// outputStream.write(body.getBytes("UTF-8"));
				outputStream.write(("&result=" + body).getBytes("utf-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			response = buffer.toString();
		} catch (Exception e) {

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			try {
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
			} catch (IOException execption) {

			}
		}
		return response;
	}

	@Override
	public SignResponseVo offlineVerify(String name, String idCard, String facePic, String openID, String serialNum,
			int type, String signUrl,String ip) throws Exception {
		// String s = jedisCluster.get(serialNum);
		// if(null == s){
		// jedisCluster.set(serialNum, "true", "NX", "EX", 1800000);
		// }else{
		// throw new Exception("该二维码已被使用，请重新扫码。");
		// }
		SignResponseVo vo = new SignResponseVo();

		serialNum = duplicateCheckSerialNum(serialNum);
		
		//调用计费接口  (宝盒认证)
		String boxNum = getDeviceNo(serialNum);
		LOGGER.info(">>>>宝盒编号为:"+boxNum);
//		BoxInfo boxInfo = boxInfoDao.selectByBoxNum(boxNum);
		JFServiceEnum jfServiceEnum = StringUtils.isBlank(facePic)?JFServiceEnum.CARD_AUTH:JFServiceEnum.CARD_FACE_AUTH;
		JFResult<JFInfoModel> jf = jfClient.jf(boxNum, jfServiceEnum,JFChannelEnum.BOX,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		String serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();

		CtidResult<CtidAuthReturnBean> result = auth(name, idCard, facePic,specialCode);
		if (result.getCode() == 1) {
			throw new Exception("认证服务器异常！" + result.getMessage());
		}
		CtidAuthReturnBean authData = result.getData();
		String authMode = authData.getAuthMode();
		String authResult = authData.getAuthResult();
		String authDesc = authData.getAuthDesc();


		// 认证结果 默认失败
		int authResulti = Contents.AUTH_RESULT_FALSE;
		// 认证模式
		int ctidType = 0;
		int isCtid = 1;
		if ("0x06".equals(authMode)) {
			// 有网证，网证服务返回该用户有网证信息
			isCtid = Contents.IS_CTID_TRUE;
			ctidType = Contents.CTID_TYPE_ONE;
		} else if ("0x40".equals(authMode)) {
			// 两项
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_TWO;
		} else if ("0x42".equals(authMode)) {
			// 两项+人像
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_THREE;
		}
		if ("true".equals(authResult)) {
			// 认证通过后，认证结果设为0 成功
			authResulti = Contents.AUTH_RESULT_TRUE;
		}

		String signData = null;
		Date signdate = null;
		if (authResulti == 0) {
			// 认证通过后，查询个人信息是否存在，不存在则添加
			getPersonInfo(name, idCard, facePic, openID, isCtid, type);

			// 调用中转服务（最终调用的是密码机）接口签名
			ResponseEntity<HttpResponseBody> responseEntitySign = signByTs(name, idCard, serialNum, signUrl);
			LOGGER.info("signByTs end {}", new Date());
			if (responseEntitySign.getStatusCode() == HttpStatus.OK) {
				HttpResponseBody<LinkedHashMap> responseBodySign = responseEntitySign.getBody();
				LinkedHashMap data = responseBodySign.getData();
				LOGGER.debug("data: {}", data);
				String twoBarCodeData = data.get("twoBarCodeData").toString();
				LOGGER.debug("verify ---->twoBarCodeData: {}", twoBarCodeData);
				signData = data.get("signData").toString();
				LOGGER.debug("signData: {}", signData);
				String signTime = data.get("signTime").toString();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
				signdate = sdf1.parse(signTime);

				if ("undefined".equals(signData) || "".equals(signData) || null == signData) {
					throw new Exception("签名失败！签名值异常！");
				}

				// 最后返回待生成二维码的原始数据
				vo.setTwoBarCodeData(twoBarCodeData);
				vo.setAuthResult(authResulti);
				vo.setAuthDesc(authDesc);
			} else {
				throw new RuntimeException("签名服务异常！");
			}
		} else {
			vo.setAuthResult(authResulti);
			vo.setAuthDesc(authDesc);
		}

		if (null == signdate) {
			signdate = new Date();
		}
		String bsn = authData.getBsn();
		// 从网证服务返回值中获取认证模式，认证结果，插入一条认证记录
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), type, name, idCard, Contents.AUTH_TYPE_OFFLINE,
				ctidType, authResulti, getDeviceNo(serialNum), signdate, bsn, facePic, signData, openID, null,
				authDesc, Contents.AUTH_OBJECT_SELF, null);
		// recordDao.insertSelective(ar);
		mqSend.send(ar);
		
		//发送计费业务日志
		try {
			jfClient.send(serverAccount, jfServiceEnum, JFChannelEnum.BOX, authResulti, authDesc,bsn,specialCode,boxNum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.debug("signTime/authDate -- Date: {}", signdate);
		vo.setAuthDate(DateUtils.format(signdate));

		return vo;
	}

	private String getDeviceNo(String serialNum) {
		String deviceTypeNum = serialNum.substring(0, 2);
		if (Contents.CERT_BOX.equals(deviceTypeNum)) {
			// source 认证盒
			return serialNum.substring(2, 26);
		} else if (Contents.HAND_SET.equals(deviceTypeNum)) {
			// source PDA86手持机
			return serialNum.substring(2, 17);
		} else if (Contents.PDA_MOBILE_AUTH.equals(deviceTypeNum)) {
			// source 安卓手持机
			return serialNum.substring(2, 18);
		}
		return "";
	}

	private PersonInfo getPersonInfo(String name, String idCard, String facePic, String openID, Integer isCtid,
			Integer type) {
		PersonInfo person = personDao.selectByOpenID(openID);
		if (!name.equals("") && !idCard.equals("")) {
			String encodeIdCardInfo = cipherService.encodeIdCardInfo(name, idCard);
			String calculateHash = CalculateHashUtils.calculateHash(idCard.getBytes());
			if (null == person) {
				person = new PersonInfo(UUIDGenerator.getSerialId(), type, openID, new Date(), calculateHash, null,
						encodeIdCardInfo, null, isCtid, picToFtp(facePic));
				personDao.insertSelective(person);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("insert person: {}", person.getOpenid());
				}
			} else {
				// 存在的话判断用户名，身份证是否为已有的，不是则更新
				IdCardInfoBean info = cipherService.decodeIdCardInfo(person.getName());
				if (!name.equals(info.getCardName()) || !idCard.equals(info.getCardNum())) {
					person.setIsCtid(isCtid);
					person.setName(encodeIdCardInfo);
					person.setIdcard(calculateHash);
					person.setPic(picToFtp(facePic));
					personDao.updateByPrimaryKeySelective(person);
				} else {
					if(person.getIsCtid().equals(1)) {
						person.setIsCtid(isCtid);
						personDao.updateByPrimaryKeySelective(person);
					}
				}

			}
		}
		return person;
	}

	private ResponseEntity<HttpResponseBody> signByTs(String name, String idCard, String serialNum, String signUrl) {
		// 认证通过，则调用中转服务接口签名
		OfflineParamVO opVo = new OfflineParamVO();
		opVo.setIdNum(idCard);
		opVo.setName(name);
		opVo.setSerialNum(serialNum);

		HttpEntity<OfflineParamVO> entity = new HttpEntity<>(opVo);
		return restTemplate.exchange(signUrl, HttpMethod.POST, entity, HttpResponseBody.class);
	}

	@Override
	public String generateSerialNum(String deviceName, String password, String url, int location, String skipUrl)
			throws Exception {
		OnlineVO vo = new OnlineVO();
		// 查询服务账号是否存在，不存在则返回错误
//		ServerAccount account = cacheService.selectServer(deviceName, password);
//		if (null == account) {
//			LOGGER.error("服务账号信息有误。");
//			throw new Exception("服务账号信息有误。");
//		}

		// 保存回调信息，设备信息与对应随机数
		AuthBiz authBiz = new AuthBiz();
		authBiz.setId(UUIDGenerator.getSerialId());
		authBiz.setInfoSource(Contents.AUTH_TYPE_ONLINE);
		authBiz.setIsCallback(Contents.CALL_BACK_FALSE);
		authBiz.setServerAccount(deviceName);
		authBiz.setCreateTime(DateUtils.getNowDate());
		authBiz.setServerDesc(deviceName);
		authBiz.setAuthType(Contents.AUTH_BIZ_TYPE_ONLINE);

		StringBuilder stringBuilder = new StringBuilder();
		String serialNum = "";
		// TODO:判断账户类型是否需要获取位置信息，需要的话serialnum修改为05开头
		if (location == Contents.SERVER_LOCATION_TRUE) {
			serialNum = stringBuilder.append("05").append(getRandomString(30)).toString();
		} else {
			serialNum = stringBuilder.append("01").append(getRandomString(30)).toString();
		}
		authBiz.setSerialNum(serialNum);
		if (url.subSequence(0, url.indexOf(":")).equals("https")) {
			try {
				URL iurl = new URL(url);
				url = "http://" + iurl.getHost() + ":8091" + iurl.getPath();
			} catch (Exception e) {
				LOGGER.error("回调地址有误。");
				throw new Exception("回调地址有误。");
			}
		}
		authBiz.setUrl(url);
		abdao.insertSelective(authBiz);
		StringBuilder stringBuilderSkip = new StringBuilder();
		String ret = stringBuilderSkip.append(skipUrl).append(serialNum).toString();

		return ret;
	}

	private void checkAuthCount(String deviceName) throws Exception {
		Map<String, String> results = jedisCluster.hgetAll(deviceCount);
		String result = results.get(deviceName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowdate = sdf.format(new Date());
		Map<String, String> map = new HashMap<String, String>();
		if (null == result) {
			AuthCount count = countDao.selectByServer(deviceName);
			if (null == count) {
				AuthCount acount = new AuthCount(UUIDGenerator.getSerialId(), deviceName, nowdate, 1);
				countDao.insert(acount);
				map.put(deviceName, nowdate + "+" + "1");
				jedisCluster.hmset(deviceCount, map);
			} else if (!count.getNowDay().equals(nowdate)) {
				count.setCount(1);
				count.setNowDay(nowdate);
				countDao.updateByPrimaryKeySelective(count);
				map.put(deviceName, nowdate + "+" + "1");
				jedisCluster.hmset(deviceCount, map);
			} else if (count.getCount() >= maxSize) {
				map.put(deviceName, nowdate + "+" + count.getCount());
				jedisCluster.hmset(deviceCount, map);
				LOGGER.error("该服务当日使用次数已达到上限。");
				throw new Exception("该服务当日使用次数已达到上限。");
			} else {// if (count.getCount() < 100 &&
				// count.getNowDay().equals(nowdate)) {
				count.setCount(count.getCount() + 1);
				count.setNowDay(nowdate);
				countDao.updateByPrimaryKeySelective(count);
				map.put(deviceName, nowdate + "+" + count.getCount());
				jedisCluster.hmset(deviceCount, map);
			}
		} else {
			String date = result.substring(0, result.indexOf("+"));
			int count = Integer.parseInt(result.substring(result.indexOf("+") + 1));
			if (!date.equals(nowdate)) {
				map.put(deviceName, nowdate + "+" + "1");
				jedisCluster.hmset(deviceCount, map);
				// jedisCluster.set(deviceName, nowdate+"+"+"1");
			} else if (count >= maxSize) {
				LOGGER.error("该服务当日使用次数已达到上限。");
				throw new Exception("该服务当日使用次数已达到上限。");
			} else {
				map.put(deviceName, nowdate + "+" + (count + 1));
				jedisCluster.hmset(deviceCount, map);
				// jedisCluster.set(deviceName, nowdate+"+"+(count + 1));
			}
		}

	}

	/**
	 * 生成字符随机数
	 *
	 * @param length
	 * @return
	 */
	private String getRandomString(int length) {
		// 定义一个字符串（A-Z，a-z，0-9）即62位；
		String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
		// 由Random生成随机数
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		// 长度为几就循环几次
		for (int i = 0; i < length; ++i) {
			// 产生0-61的数字
			int number = random.nextInt(62);
			// 将产生的数字通过length次承载到sb中
			sb.append(str.charAt(number));
		}
		// 将承载的字符转换成字符串
		return sb.toString();
	}

	private ResponseEntity<HttpResponseBody> authByCtid(String cardName, String cartNum, String photoData,
			String authUrl) {
		AuthResponseVo aVo = new AuthResponseVo();
		HttpEntity<AuthResponseVo> entity = new HttpEntity<>(aVo);
		aVo.setCardName(cardName);
		aVo.setCardNum(cartNum);
		aVo.setPhotoData(photoData);

		return restTemplate.exchange(authUrl, HttpMethod.POST, entity, HttpResponseBody.class);
	}

	private CtidResult<CtidAuthReturnBean> auth(String name, String iDcard, String facePic, String serverId) {
		CtidResult result = new CtidResult();
		if (null == name || null == iDcard) {
			LOGGER.debug("======================name: " + name + "idcard: " + iDcard + "facepic" + facePic);
			return CtidResult.error("参数错误!");
		}
		result = apiAuthService.auth(name, iDcard, facePic,serverId);
		return result;
	}

	@Override
	public boolean addUser(String name, String iDcard, String facePic, String openID, int type,String ip) throws Exception {

		boolean b = false;
		PersonInfo p = personDao.selectByOpenID(openID);
		if (null == p) {
			
			//调用计费接口  (用户注册)
			JFServiceEnum jfServiceEnum = StringUtils.isBlank(facePic)?JFServiceEnum.CARD_AUTH:JFServiceEnum.CARD_FACE_AUTH;
			JFResult<JFInfoModel> jf = jfClient.jf(JFConstants.SFSK_APPLET, jfServiceEnum,JFChannelEnum.WX_REGISTER,ip);
			if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
				throw new Exception(jf.getMessage());
			}
			JFInfoModel jfData = jf.getData();
			String serverAccount = jfData.getUserServiceAccount();
			String specialCode = jfData.getSpecialCode();

			CtidResult<CtidAuthReturnBean> result = auth(name, iDcard, facePic,specialCode);
			if (result.getCode() == 1) {
				throw new Exception("认证服务器异常！" + result.getMessage());
			}
			CtidAuthReturnBean authData = result.getData();
			String authMode = authData.getAuthMode();
			String authResult = authData.getAuthResult();
			String authDesc = authData.getAuthDesc();


			LOGGER.debug("-------------------authDesc-------------------: {}" + authDesc);

			// 保存业务信息
			int authResults = Contents.AUTH_RESULT_FALSE;
			int ctidType = 0;
			int isCtid = 1;
			if ("0x06".equals(authMode)) {
				// 有网证
				isCtid = Contents.IS_CTID_TRUE;
				// 网证服务返回该用户有网证信息
				ctidType = Contents.CTID_TYPE_ONE;
			} else if ("0x40".equals(authMode)) {
				// 两项
				isCtid = Contents.IS_CTID_FALSE;
				ctidType = Contents.CTID_TYPE_TWO;
			} else if ("0x42".equals(authMode)) {
				// 两项+人像
				isCtid = Contents.IS_CTID_FALSE;
				ctidType = Contents.CTID_TYPE_THREE;
			}
			if (authResult.equals("true")) {
				authResults = Contents.AUTH_RESULT_TRUE;

				String encodeIdCardInfo = cipherService.encodeIdCardInfo(name, iDcard);
				String calculateHash = CalculateHashUtils.calculateHash(iDcard.getBytes());

				p = new PersonInfo(UUIDGenerator.getSerialId(), type, openID, new Date(), calculateHash, null,
						encodeIdCardInfo, null, isCtid, picToFtp(facePic));
				personDao.insertSelective(p);
				b = true;

			} else {
				LOGGER.error("用户信息有误！");
			}
//			String serialNum = getRandomString(32);
			String bsn = authData.getBsn();
			// 从网证服务返回值中获取认证模式，认证结果，插入一条认证记录
			AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), type, name, iDcard, Contents.AUTH_TYPE_ADDUSER,
					ctidType, authResults, null, new Date(), bsn, facePic, null, openID, null, authDesc,
					Contents.AUTH_OBJECT_SELF, null);
			// recordDao.insertSelective(ar);
			mqSend.send(ar);
			
			//发送计费业务日志
			jfClient.send(serverAccount, jfServiceEnum, JFChannelEnum.WX_REGISTER, authResults, authDesc,bsn,specialCode,serverAccount);
			
		} else {
			LOGGER.error("用户信息已存在！");
			throw new Exception("用户信息已存在！");
		}
		return b;
	}

	@Override
	public SignResponseVo offlineVerifyByCtid(String ctid, String facePic, String openID, String serialNum,
			Integer type, String businessSerialNumber,String ip) throws Exception {
		// String s = jedisCluster.get(serialNum);
		// if(null == s){
		// jedisCluster.set(serialNum, "true", "NX", "EX", 1800000);
		// }else{
		// throw new Exception("该二维码已被使用，请重新扫码。");
		// }
		SignResponseVo vo = new SignResponseVo();

		serialNum = duplicateCheckSerialNum(serialNum);
		
		//调用计费接口  (宝盒认证)
		String boxNum = getDeviceNo(serialNum);
		LOGGER.info(">>>>宝盒编号为:"+boxNum);
		JFResult<JFInfoModel> jf = jfClient.jf(boxNum, JFServiceEnum.CTID_AUTH,JFChannelEnum.BOX,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		String serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		// TODO:修改网证调用方式，并获取返回的四项信息
		CtidResult<CtidAuthReturnBean> result = authCtidService.authCtidRequest(businessSerialNumber, "0x06", facePic, ctid, null, null,specialCode);
		LOGGER.debug("ctid auth message -------------------------------- : {}", result.getMessage());
		if (result.getCode() == 1) {
			throw new Exception("认证服务器异常！" + result.getMessage());
		}
		CtidAuthReturnBean authData = result.getData();
		LOGGER.debug("ctid auth authData -------------------------------- : {}", authData);
		String authMode = authData.getAuthMode();
		LOGGER.debug("ctid auth authMode: {}", authMode);
		String authResult = authData.getAuthResult();
		LOGGER.debug("ctid auth authResult: {}", authResult);
		String authDesc = authData.getAuthDesc();
		LOGGER.debug("ctid auth authDesc: {}", authDesc);
		String pid = authData.getPid();
		String name = "";
		String idCard = "";
		CtidInfos ctids = ctidDao.selectByPid(pid);
		if (null != ctids) {
			IdCardInfoBean info = cipherService.decodeIdCardInfo(ctids.getCardName());
			name = info.getCardName();
			idCard = info.getCardNum();
		}
		// 认证结果 默认失败
		int authResulti = Contents.AUTH_RESULT_FALSE;
		// 认证模式
		int ctidType = Contents.CTID_TYPE_ONE;
		int isCtid = Contents.IS_CTID_TRUE;

		if ("true".equals(authResult)) {
			// 认证通过后，认证结果设为0 成功
			authResulti = Contents.AUTH_RESULT_TRUE;
		}
		String signData = null;
		Date signdate = null;
		if (authResulti == 0) {
			// 认证通过后，查询个人信息是否存在，不存在则添加
			PersonInfo p = getPersonInfo(name, idCard, facePic, openID, isCtid, type);
			if (name.equals("") || idCard.equals("")) {
				if (null != p) {
					IdCardInfoBean info = cipherService.decodeIdCardInfo(p.getName());
					name = info.getCardName();
					idCard = info.getCardNum();
				}
			}

			// 调用中转服务（最终调用的是密码机）接口签名
			ResponseEntity<HttpResponseBody> responseEntitySign = signByTs(name, idCard, serialNum, signUrl);
			LOGGER.info("signByTs end {}", new Date());
			if (responseEntitySign.getStatusCode() == HttpStatus.OK) {
				HttpResponseBody<LinkedHashMap> responseBodySign = responseEntitySign.getBody();
				LinkedHashMap data = responseBodySign.getData();
				LOGGER.debug("data: {}", data);
				String twoBarCodeData = data.get("twoBarCodeData").toString();
				LOGGER.debug("verify ---->twoBarCodeData: {}", twoBarCodeData);
				signData = data.get("signData").toString();
				LOGGER.debug("signData: {}", signData);
				String signTime = data.get("signTime").toString();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
				signdate = sdf1.parse(signTime);

				if ("undefined".equals(signData) || "".equals(signData) || null == signData) {
					throw new Exception("签名失败！签名值异常！");
				}

				// 最后返回待生成二维码的原始数据
				vo.setTwoBarCodeData(twoBarCodeData);
				vo.setAuthResult(authResulti);
				vo.setAuthDesc(authDesc);
			} else {
				throw new RuntimeException("签名服务异常！");
			}
		} else {
			PersonInfo person = personDao.selectByOpenID(openID);
			if (null != person) {
				IdCardInfoBean info = cipherService.decodeIdCardInfo(person.getName());
				name = info.getCardName();
				idCard = info.getCardNum();
			}
			vo.setAuthResult(authResulti);
			vo.setAuthDesc(authDesc);
		}

		if (null == signdate) {
			signdate = new Date();
		}
		String bsn = authData.getBsn();
		// 从网证服务返回值中获取认证模式，认证结果，插入一条认证记录
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), type, name, idCard, Contents.AUTH_TYPE_OFFLINE,
				ctidType, authResulti, getDeviceNo(serialNum), signdate, bsn, facePic, signData, openID, null,
				authDesc, Contents.AUTH_OBJECT_SELF, null);
		// recordDao.insertSelective(ar);
		mqSend.send(ar);
		
		//发送计费业务日志
		try {
			jfClient.send(serverAccount, JFServiceEnum.CTID_AUTH, JFChannelEnum.BOX, authResulti, authDesc,bsn,specialCode,boxNum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.debug("signTime/authDate -- Date: {}", signdate);
		vo.setAuthDate(DateUtils.format(signdate));

		return vo;
	}

	private String duplicateCheckSerialNum(String serialNum) throws Exception {
		/*
		 * 根据身份宝盒流水号修改，流水号长度：24字节，48个字符规则（字节）：1个字节设备类型+12个字节设备号5
		 * 个字节的随机数（开机后固定）+6个字节的随机数（服务端产生）
		 */
		LOGGER.debug("身份宝盒初始固定流水号 {}", serialNum);
		String deviceNum = serialNum.substring(0, 2);
		if (deviceNum.equals(Contents.CERT_BOX)) {
			StringBuilder stringBuilder = new StringBuilder();
			serialNum = stringBuilder.append(serialNum).append(getRandomString(12)).toString();
			LOGGER.debug("身份宝盒服务器更改后流水号 {}", serialNum);
		} else { // 手持机业务流水号查重
			AuthRecord ar = recordDao.selectBySerialNum(serialNum);
			if (null != ar) {
				throw new Exception("该二维码已被使用，请重新扫码。");
			}
		}
		return serialNum;
	}

	@Override
	public Map<String, Object> onlineVerifyByCtid(String ctid, String facePic, String location, String openID,
			String serialNum, Integer type, String businessSerialNumber,String ip) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// t1:查询对应序列号的服务账号信息
		boolean b = true;
		String s = jedisCluster.get(serialNum);
		if (null == s) {
			jedisCluster.set(serialNum, "true", "NX", "EX", 1800000);
		} else {
			LOGGER.error("该二维码已被使用，请重新扫码。");
			throw new Exception("该二维码已被使用，请重新扫码。");
		}
		AuthBiz ab = abdao.queryBySerial(serialNum);
		if (null == ab) {
			LOGGER.error("serialNum错误，非系统生成！");
			throw new Exception("无效二维码！");
		}
		// 判断账户是否超过使用次数
		String serverAccount = ab.getServerAccount();
//		checkAuthCount(serverAccount);

		if (StringUtils.isBlank(location) && serialNum.substring(0, 2).equals("05")) {
			LOGGER.error("需要获取位置信息。");
			throw new Exception("需要获取位置信息。");
		}
		
		//调用计费接口
		JFResult<JFInfoModel> jf = jfClient.jf(serverAccount, JFServiceEnum.CTID_AUTH,JFChannelEnum.API,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		// TODO:修改网证调用方式，并获取返回的四项信息
		CtidResult<CtidAuthReturnBean> cresult = authCtidService.authCtidRequest(businessSerialNumber, "0x06", facePic, ctid, null, null,specialCode);
		if (cresult.getCode() == 1) {
			throw new Exception("认证服务器异常！" + cresult.getMessage());
		}
		CtidAuthReturnBean authData = cresult.getData();
		String authMode = authData.getAuthMode();
		String authResult = authData.getAuthResult();
		String authDesc = authData.getAuthDesc();
		LOGGER.debug("-------------------authDesc-------------------: {}" + authDesc);

		String pid = authData.getPid();
		String name = "";
		String idCard = "";
		CtidInfos ctids = ctidDao.selectByPid(pid);

		// t4:保存业务信息
		int authResults = Contents.AUTH_RESULT_FALSE;
		int ctidType = Contents.CTID_TYPE_ONE;
		int isCtid = Contents.IS_CTID_TRUE;
		if (null != ctids) {
			IdCardInfoBean info = cipherService.decodeIdCardInfo(ctids.getCardName());
			name = info.getCardName();
			idCard = info.getCardNum();
		}
		// t2:查询二项信息+openID是否存在，不存在则保存
		PersonInfo person = personDao.selectByOpenID(openID);
		if (authResult.equals("true")) {
			authResults = Contents.AUTH_RESULT_TRUE;
			PersonInfo p = getPersonInfo(name, idCard, facePic, openID, isCtid, type);
			if (name.equals("") || idCard.equals("")) {
				IdCardInfoBean info = cipherService.decodeIdCardInfo(p.getName());
				name = info.getCardName();
				idCard = info.getCardNum();
			}
		} else {
			b = false;
			if (null != person) {
				IdCardInfoBean info = cipherService.decodeIdCardInfo(person.getName());
				name = info.getCardName();
				idCard = info.getCardNum();
			}
		}
		String bsn = authData.getBsn();
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), type, name, idCard, Contents.AUTH_TYPE_ONLINE,
				ctidType, authResults, ab.getServerDesc(), new Date(), bsn, facePic, null, openID, location,
				authDesc, Contents.AUTH_OBJECT_SELF, null);
		// recordDao.insertSelective(ar);
		mqSend.send(ar);
		
		//发送计费业务日志
		jfClient.send(serverAccount, JFServiceEnum.CTID_AUTH, JFChannelEnum.API, authResults, authDesc,bsn,specialCode,serverAccount);
				
		
		// t5:将结果通过回调地址返回
		OnlineVO vo = new OnlineVO();
		vo.setAuthResult(authResults);
		vo.setSerialNum(serialNum);
		vo.setIdcard(idCard);
		vo.setName(name);
		vo.setPic(facePic);
		vo.setVerifyDate(DateUtils.format(ar.getCreateTime()));
		vo.setServerName(ab.getServerDesc());
		if (serialNum.substring(0, 2).equals("05")) {
			vo.setLocation(location);
		}
		String result = JSONObject.fromObject(vo).toString();
		if (ab.getUrl().subSequence(0, ab.getUrl().indexOf(":")).equals("https")) {
			httpsRequestToString(ab.getUrl(), "POST", result);
		} else {
			httpRequest(ab.getUrl(), "POST", result);
		}
		ab.setIsCallback(0);
		abdao.updateByPrimaryKey(ab);
		map.put("result", b);
		map.put("authDesc", authDesc);
		return map;
	}

	private String picToFtp(String facePic) {
		String uploadIdCard = "";
		if (StringUtils.isNotBlank(facePic)) {
			try {
				uploadIdCard = FastDFSTool.upload(Base64Utils.decode(facePic), "jpg");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error("-----图片上传失败-----");
				e.printStackTrace();
			}
		}
		return uploadIdCard;
	}

	@Override
	public HttpResponseBody<String> userLogin(String name, String iDcard, String facePic, String openID, int type,
			String ip) throws Exception {

		HttpResponseBody<String> hb = new HttpResponseBody<String>();
		
		//调用计费接口  (用户登录)
		JFServiceEnum jfServiceEnum = StringUtils.isBlank(facePic)?JFServiceEnum.CARD_AUTH:JFServiceEnum.CARD_FACE_AUTH;
		JFResult<JFInfoModel> jf = jfClient.jf(JFConstants.SFSK_APPLET, jfServiceEnum,JFChannelEnum.WX_LOGIN,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		String serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();

		CtidResult<CtidAuthReturnBean> result = auth(name, iDcard, facePic,specialCode);
		if (result.getCode() == 1) {
			throw new Exception("认证服务器异常！" + result.getMessage());
		}
		CtidAuthReturnBean authData = result.getData();
		String authMode = authData.getAuthMode();
		String authResult = authData.getAuthResult();
		String authDesc = authData.getAuthDesc();


		LOGGER.debug("-------------------authDesc-------------------: {}" + authDesc);

		// 保存业务信息
		int authResults = Contents.AUTH_RESULT_FALSE;
		int ctidType = 0;
		int isCtid = 1;
		if ("0x06".equals(authMode)) {
			// 有网证
			isCtid = Contents.IS_CTID_TRUE;
			// 网证服务返回该用户有网证信息
			ctidType = Contents.CTID_TYPE_ONE;
		} else if ("0x40".equals(authMode)) {
			// 两项
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_TWO;
		} else if ("0x42".equals(authMode)) {
			// 两项+人像
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_THREE;
		}
		if (authResult.equals("true")) {
			authResults = Contents.AUTH_RESULT_TRUE;

			String uuid = UUIDGenerator.getUUID();
			String token = cipherService.encodeAccess(openID, LocalDateTime.now().toString(), ip, uuid.substring(16));
			if (StringUtils.isBlank(token)) {
				hb.setCode(1);
				hb.setMessage("获取token失败，请重新登录");
			} else {
				jedisCluster.set(uuid.substring(0, 16), token);
				jedisCluster.expire(uuid.substring(0, 16), 1800);
				hb.setData(uuid);
				hb.setCode(0);
				hb.setMessage("获取token成功");
			}

		} else {
			LOGGER.error("用户信息验证失败，请重新登录！");
			hb.setCode(1);
			hb.setMessage("用户信息验证失败，请重新登录！");
		}
//		String serialNum = getRandomString(32);
		String bsn = authData.getBsn();
		// 从网证服务返回值中获取认证模式，认证结果，插入一条认证记录
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), type, name, iDcard, Contents.AUTH_TYPE_USERLOGIN,
				ctidType, authResults, null, new Date(), bsn, facePic, null, openID, null, authDesc,
				Contents.AUTH_OBJECT_SELF, null);
		// recordDao.insertSelective(ar);
		mqSend.send(ar);
		
		//发送计费业务日志
		jfClient.send(serverAccount, jfServiceEnum, JFChannelEnum.WX_LOGIN, authResults, authDesc,bsn,specialCode,serverAccount);
				

		return hb;
	}

	@Override
	public Map<String, Object> FTOFVerify(String name, String idcard, String openID, String pic, String serialNum,String ip)
			throws Exception { 
		//认证发起人openid
		String otherOpenid = jedisCluster.get(serialNum);

		Map<String, Object> map = new HashMap<>();
		// t1:查询对应序列号的中转信息
		boolean b = true;
		AuthBiz ab = abdao.queryBySerial(serialNum);
		if (null == ab) {
			LOGGER.error("serialNum错误，非系统生成！");
			throw new Exception("无效二维码！");
		}
		if(openID.equals(otherOpenid)){
			LOGGER.error("不能对自己进行认证。");
			throw new Exception("不能对自己进行认证！");
		}
		// 查询序列号对应的socket连接用户，并将改序列号状态变成已使用
		String socketId = jedisCluster.get(otherOpenid);
		String s = jedisCluster.get(serialNum+"_sock");
		if (null == s) {
			jedisCluster.set(serialNum+"_sock", "true");
		} else {
			LOGGER.error("该二维码已被使用，请重新扫码。");
			throw new Exception("该二维码已被使用，请重新扫码。");
		}
		
		//调用计费接口  (面对面认证)
		JFServiceEnum jfServiceEnum = StringUtils.isBlank(pic)?JFServiceEnum.CARD_AUTH:JFServiceEnum.CARD_FACE_AUTH;
		JFResult<JFInfoModel> jf = jfClient.jf(JFConstants.SFSK_APPLET, jfServiceEnum,JFChannelEnum.WX_FTOFVerify,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		String serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		// 调取网证服务验证
		CtidResult<CtidAuthReturnBean> cresult = auth(name, idcard, pic,specialCode);
		if (cresult.getCode() == 1) {
			throw new Exception("认证服务器异常！" + cresult.getMessage());
		}
		CtidAuthReturnBean authData = cresult.getData();
		String authMode = authData.getAuthMode();
		String authResult = authData.getAuthResult();
		String authDesc = authData.getAuthDesc();


		LOGGER.debug("-------------------authDesc-------------------: {}" + authDesc);

		// t4:保存业务信息
		int authResults = Contents.AUTH_RESULT_FALSE;
		int ctidType = 0;
		int isCtid = 1;
		String ctidTypeName = "";
		if ("0x06".equals(authMode)) {
			// 有网证
			isCtid = Contents.IS_CTID_TRUE;
			// 网证服务返回该用户有网证信息
			ctidType = Contents.CTID_TYPE_ONE;
			ctidTypeName = "网证认证";
		} else if ("0x40".equals(authMode)) {
			// 两项
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_TWO;
			ctidTypeName = "实名认证";
		} else if ("0x42".equals(authMode)) {
			// 两项+人像
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_THREE;
			ctidTypeName = "实名+实人认证";
		}
		if (authResult.equals("true")) {
			authResults = Contents.AUTH_RESULT_TRUE;
			// t2:查询二项信息+openID是否存在，不存在则保存
			PersonInfo person = personDao.selectByOpenID(openID);
			String encodeIdCardInfo = cipherService.encodeIdCardInfo(name, idcard);
			String calculateHash = CalculateHashUtils.calculateHash(idcard.getBytes());
			if (null == person) {
				person = new PersonInfo(UUIDGenerator.getSerialId(), 1, openID, new Date(), calculateHash, null,
						encodeIdCardInfo, null, isCtid, picToFtp(pic));
				personDao.insertSelective(person);
			} else {
				if(person.getIsCtid().equals(1)) {
					person.setIsCtid(isCtid);
					personDao.updateByPrimaryKeySelective(person);
				}
			}
		} else {
			b = false;
		}

		// 记录被认证人认证记录信息
		String otherArId = UUIDGenerator.getSerialId();
		String bsn = authData.getBsn();
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), 1, name, idcard, Contents.AUTH_TYPE_FTF, ctidType,
				authResults, ab.getServerDesc(), new Date(), bsn, pic, null, openID, null, authDesc,
				Contents.AUTH_OBJECT_SELF, otherArId);
		// recordDao.insertSelective(ar);
		mqSend.send(ar);
		
		//发送计费业务日志
		jfClient.send(serverAccount, jfServiceEnum, JFChannelEnum.WX_FTOFVerify, authResults, authDesc,bsn,specialCode,serverAccount);

		// 记录认证发起人认证记录信息 
		PersonInfo otherPerson = personDao.selectByOpenID(otherOpenid);
		if (null != otherPerson) {
			IdCardInfoBean idCardInfoBean = cipherService.decodeIdCardInfo(otherPerson.getName());
			AuthRecord otherar = new AuthRecord(otherArId, 1, idCardInfoBean.getCardName(), idCardInfoBean.getCardNum(),
					Contents.AUTH_TYPE_FTF, ctidType, authResults, ab.getServerDesc(), new Date(), serialNum, pic, null,
					otherOpenid, null, authDesc, Contents.AUTH_OBJECT_OTHER, ar.getId());
			mqSend.send(otherar);
		}

		// t5:将结果通过回调地址返回
		OnlineVO vo = new OnlineVO();
		vo.setAuthResult(authResults);
		vo.setSerialNum(serialNum);
		vo.setIdcard(idcard);
		vo.setName(name);
		vo.setVerifyDate(DateUtils.format(ar.getCreateTime()));
		vo.setServerName(ab.getServerDesc()); 
		String result = JSONObject.fromObject(vo).toString();

		/*boolean sendresult = handler.sendMessageToUser(socketId, new TextMessage(result));
		if (sendresult) {
			ab.setIsCallback(0);
			abdao.updateByPrimaryKey(ab);
		}else {
			LOGGER.error("socket消息推送失败");
		}*/

		mqSend.sendTopicWS(serialNum,socketId, result);

		//推送通知 
		List<TemplateDataVo> keywords = new ArrayList<TemplateDataVo>();  
		keywords.add(new TemplateDataVo("thing1", ctidTypeName));
		keywords.add(new TemplateDataVo("date3", DateUtils.format(ar.getCreateTime())));
		keywords.add(new TemplateDataVo("date2", DateUtils.format(ab.getCreateTime())));
		keywords.add(new TemplateDataVo("name5", "*"+name.substring(1))); 
		if (b) {
			keywords.add(new TemplateDataVo("phrase4", "认证成功")); 
		} else {
			keywords.add(new TemplateDataVo("phrase4", "认证失败")); 
		}
		String pushResult = wechatService.pushOneUser("", otherOpenid, templateId, keywords);
		if (!JSONObject.fromObject(pushResult).get("errcode").equals(0)) {
			LOGGER.error("消息推送失败");
		}

		map.put("result", b);
		map.put("authDesc", authDesc);
		return map;
	}

	@Override
	public String getWechatSerialNum(String socketId, String openid, int authType) throws Exception {
		// 保存回调信息，设备信息与对应随机数
//		ServerAccount account = cacheService.selectServerByName(ftfAccount);
//		if(null == account) {
//			LOGGER.error("服务账号信息有误。");
//			throw new Exception("服务账号信息有误。");
//		}
		String serialNum = "";
		StringBuffer sb = new StringBuffer();
		AuthBiz authBiz = new AuthBiz();
		authBiz.setId(UUIDGenerator.getSerialId());
		authBiz.setInfoSource(Contents.INFO_SOURCE_WECHAT);
		authBiz.setIsCallback(Contents.CALL_BACK_FALSE);
		authBiz.setServerAccount(ftfAccount);
		authBiz.setCreateTime(DateUtils.getNowDate());
		authBiz.setServerDesc(ftfAccount);
		if (authType == 1) {
			authBiz.setAuthType(Contents.AUTH_BIZ_TYPE_FTF);
			serialNum = sb.append("06").append(getRandomString(30)).toString();
			jedisCluster.set(openid, socketId);
			jedisCluster.expire(openid, 3600);
		} else {
			serialNum = sb.append("07").append(getRandomString(30)).toString();
			authBiz.setAuthType(Contents.AUTH_BIZ_TYPE_TRANSPORT);
		}
		authBiz.setCreateTime(DateUtils.getNowDate());
		authBiz.setSerialNum(serialNum); 
		jedisCluster.set(serialNum, openid);

		abdao.insertSelective(authBiz);
		StringBuilder stringBuilderSkip = new StringBuilder();
		String ret = stringBuilderSkip.append(skipurl).append(serialNum).toString();
		return ret;
	}

	@Override
	public Map<String, Object> longDistanceVerify(String name, String idcard, String openID, String pic,
			String serialNum,String ip) throws Exception {
		Map<String, Object> map = new HashMap<>();
		//认证发起人openid
		String otherOpenid = jedisCluster.get(serialNum);
		// t1:查询对应序列号的中转信息
		boolean b = true;
		AuthBiz ab = abdao.queryBySerial(serialNum);
		if (null == ab) {
			LOGGER.error("serialNum错误，非系统生成,或者链接已认证");
			throw new Exception("无效链接！");
		}else {
			if(System.currentTimeMillis() - ab.getCreateTime().getTime() > pushPeriod*60*60*1000) {
				LOGGER.error("链接已过期。");
				throw new Exception("无效链接！");
			}
		}
		if(openID.equals(otherOpenid)){
			LOGGER.error("不能对自己进行认证。");
			throw new Exception("不能对自己进行认证！");
		}
		// 改序列号状态变成已使用
		String s = jedisCluster.get(serialNum+"_long");
		if (null == s) {
			jedisCluster.set(serialNum+"_long", "done");
		} else {
			LOGGER.error("该链接已验证完成。");
			throw new Exception("该链接已验证完成。");
		}
		
		//调用计费接口  (远程分享认证)
		JFServiceEnum jfServiceEnum = StringUtils.isBlank(pic)?JFServiceEnum.CARD_AUTH:JFServiceEnum.CARD_FACE_AUTH;
		JFResult<JFInfoModel> jf = jfClient.jf(JFConstants.SFSK_APPLET, jfServiceEnum,JFChannelEnum.WX_LDVerify,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		String serverAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();
		// 调取网证服务验证
		CtidResult<CtidAuthReturnBean> cresult = auth(name, idcard, pic,specialCode);
		if (cresult.getCode() == 1) {
			throw new Exception("认证服务器异常！" + cresult.getMessage());
		}
		CtidAuthReturnBean authData = cresult.getData();
		String authMode = authData.getAuthMode();
		String authResult = authData.getAuthResult();
		String authDesc = authData.getAuthDesc();

		LOGGER.debug("-------------------authDesc-------------------: {}" + authDesc);
		// t4:保存业务信息
		int authResults = Contents.AUTH_RESULT_FALSE;
		int ctidType = 0;
		int isCtid = 1;
		String ctidTypeName = "";
		if ("0x06".equals(authMode)) {
			// 有网证
			isCtid = Contents.IS_CTID_TRUE;
			// 网证服务返回该用户有网证信息
			ctidType = Contents.CTID_TYPE_ONE;
			ctidTypeName = "网证认证";
		} else if ("0x40".equals(authMode)) {
			// 两项
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_TWO;
			ctidTypeName = "实名认证";
		} else if ("0x42".equals(authMode)) {
			// 两项+人像
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_THREE;
			ctidTypeName = "实名+实人认证";
		}
		if (authResult.equals("true")) {
			authResults = Contents.AUTH_RESULT_TRUE;
			// t2:查询二项信息+openID是否存在，不存在则保存
			PersonInfo person = personDao.selectByOpenID(openID);
			String encodeIdCardInfo = cipherService.encodeIdCardInfo(name, idcard);
			String calculateHash = CalculateHashUtils.calculateHash(idcard.getBytes());
			if (null == person) {
				person = new PersonInfo(UUIDGenerator.getSerialId(), 1, openID, new Date(), calculateHash, null,
						encodeIdCardInfo, null, isCtid, picToFtp(pic));
				personDao.insertSelective(person);
			} else {
				if(person.getIsCtid().equals(1)) {
					person.setIsCtid(isCtid);
					personDao.updateByPrimaryKeySelective(person);
				}
			}
		} else {
			b = false;
		}

		// 记录被认证人认证记录信息
		String otherArId = UUIDGenerator.getSerialId();
		String bsn = authData.getBsn();
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), 1, name, idcard, Contents.AUTH_TYPE_TRANSPORT,
				ctidType, authResults, ab.getServerDesc(), new Date(), bsn, pic, null, openID, null, authDesc,
				Contents.AUTH_OBJECT_SELF, otherArId); 
		mqSend.send(ar);
		
		//发送计费业务日志
		jfClient.send(serverAccount, jfServiceEnum, JFChannelEnum.WX_LDVerify, authResults, authDesc,bsn,specialCode,serverAccount);
		
		// 记录认证发起人认证记录信息
		PersonInfo otherPerson = personDao.selectByOpenID(otherOpenid);
		if (null != otherPerson) {
			IdCardInfoBean idCardInfoBean = cipherService.decodeIdCardInfo(otherPerson.getName());
			AuthRecord otherar = new AuthRecord(otherArId, 1, idCardInfoBean.getCardName(), idCardInfoBean.getCardNum(),
					Contents.AUTH_TYPE_TRANSPORT, ctidType, authResults, ab.getServerDesc(), new Date(), serialNum, pic,
					null, otherOpenid, null, authDesc, Contents.AUTH_OBJECT_OTHER, ar.getId());
			mqSend.send(otherar);
		}

		// t5:将结果通过回调地址返回

		List<TemplateDataVo> keywords = new ArrayList<TemplateDataVo>();  
		keywords.add(new TemplateDataVo("thing1", ctidTypeName));
		keywords.add(new TemplateDataVo("date3", DateUtils.format(ar.getCreateTime())));
		keywords.add(new TemplateDataVo("date2", DateUtils.format(ab.getCreateTime())));
		keywords.add(new TemplateDataVo("name5", "*"+name.substring(1))); 
		if (b) {
			keywords.add(new TemplateDataVo("phrase4", "认证成功")); 
		} else {
			keywords.add(new TemplateDataVo("phrase4", "认证失败")); 
		}
		String pushResult = wechatService.pushOneUser("", otherOpenid, templateId, keywords);
		if (!JSONObject.fromObject(pushResult).get("errcode").equals(0)) {
			LOGGER.error("消息推送失败");
		}
		ab.setIsCallback(0);
		abdao.updateByPrimaryKey(ab);
		map.put("result", b);
		map.put("authDesc", authDesc);
		return map;
	}

	@Override
	public boolean SerialNumIsValid(String serialNum) {
		// 判断面对面二维码是否还有效
		AuthBiz ab = abdao.queryBySerial(serialNum);
		if (null == ab) {
			LOGGER.error("无效链接");
			return false; 
		}else {
			String s = jedisCluster.get(serialNum+"_long");
			if (null != s) { 
				LOGGER.error("该链接已完成认证"); 
				return false; 
			}
			return true; 
		}

	}

	@Override
	public void addAuthRecord(String name, String idcard, String openID, String pic,
			String serialNum, Integer authType, String failDesc,String ip)  throws Exception{

		// t1:查询对应序列号的中转信息
		if(authType == 1 || authType == 8 || authType == 9 || authType == 11) { 

			AuthBiz ab = abdao.queryBySerial(serialNum);
			if(null == ab) {
				throw new Exception("链接已失效。");
			}  
			if(authType == 1) {
				AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), 1, name, idcard, authType,
						null, 1, ab.getServerDesc(), new Date(), serialNum, pic, null, openID, null, failDesc,
						Contents.AUTH_OBJECT_SELF, null); 
				mqSend.send(ar);
			}
			if(authType == 11) {  
				AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), 1, name, idcard, authType,
						null, 1, ab.getServerDesc(), new Date(), serialNum, pic, null, openID, null, failDesc,
						Contents.AUTH_OBJECT_SELF, null);
				mqSend.send(ar);
				OnlineVO vo = new OnlineVO();
				vo.setAuthResult(1);
				vo.setSerialNum(serialNum);
				vo.setIdcard(idcard);
				vo.setName(name);
				vo.setVerifyDate(DateUtils.format(ar.getCreateTime()));
				vo.setServerName(ab.getServerDesc());  
				String result = JSONObject.fromObject(vo).toString();

				wPosOpenRequest = new WPosOpenRequest(appid, secret, token);
				wPosOpenRequest.postResourceServ(result.getBytes());
				HashMap<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("device_en", ab.getServerAccount());  
				LOGGER.info("设备号："+ab.getServerAccount());
				String pustJson="{\n" +
						"      \"icon\":\"http://wpos.qlogo.cn/a.jpg\"  " +
						"     ,\"title\":\"会员中心\"" +
						"     ,\"content\":\"XXX注册了会员\"" +
						"   ,   \"ext\":" + result +"   \n" +"    }" ;

				LOGGER.info(pustJson); 
				paramMap.put("data", pustJson);  
				paramMap.put("biz_code", bizCode);
				// 定义旺POS开放接口对应的serviceKey  
				ReturnData returnData = wPosOpenRequest.requestOpenApi(serviceKey, paramMap);
				LOGGER.error(returnData.getStatus()+"--"+returnData.getData()); 
			}

			if(authType == 8 || authType == 9) {  
				String otherOpenid = jedisCluster.get(serialNum);
				if(openID.equals(otherOpenid)){
					LOGGER.error("不能对自己进行认证。");
					throw new Exception("不能对自己进行认证！");
				} 
				String otherArId = UUIDGenerator.getSerialId();
				AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), 1, name, idcard, authType,
						null, 1, ab.getServerDesc(), new Date(), serialNum, pic, null, openID, null, failDesc,
						Contents.AUTH_OBJECT_SELF, otherArId); 
				mqSend.send(ar);

				PersonInfo otherPerson = personDao.selectByOpenID(otherOpenid);
				if (null != otherPerson) {
					IdCardInfoBean idCardInfoBean = cipherService.decodeIdCardInfo(otherPerson.getName());
					AuthRecord otherar = new AuthRecord(otherArId, 1, idCardInfoBean.getCardName(), idCardInfoBean.getCardNum(),
							authType, null, 1, ab.getServerDesc(), new Date(), serialNum, pic,
							null, otherOpenid, null, failDesc, Contents.AUTH_OBJECT_OTHER, ar.getId());
					mqSend.send(otherar);

				} 
			} 
			ab.setIsCallback(0);
			abdao.updateByPrimaryKey(ab);
		}else if(authType == 2) {
			AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), 1, name, idcard, authType,
					null, 1, getDeviceNo(serialNum), new Date(), serialNum, pic, null, openID, null, failDesc,
					Contents.AUTH_OBJECT_SELF, null); 
			mqSend.send(ar);
		} 

	}

	@Override
	public String getWPOSSerialNum(String wposEN) { 
		String serialNum = "";
		StringBuffer sb = new StringBuffer();
		AuthBiz authBiz = new AuthBiz();
		authBiz.setId(UUIDGenerator.getSerialId());
		authBiz.setInfoSource(Contents.INFO_SOURCE_WECHAT);
		authBiz.setIsCallback(Contents.CALL_BACK_FALSE);
		authBiz.setServerAccount(wposEN);
		authBiz.setCreateTime(DateUtils.getNowDate());
		authBiz.setServerDesc("旺pos"); 
		authBiz.setAuthType(Contents.AUTH_BIZ_TYPE_WPOS);
		serialNum = sb.append("08").append(getRandomString(30)).toString();  
		authBiz.setCreateTime(DateUtils.getNowDate());
		authBiz.setSerialNum(serialNum);  

		abdao.insertSelective(authBiz);
		StringBuilder stringBuilderSkip = new StringBuilder();
		String ret = stringBuilderSkip.append(skipurl).append(serialNum).toString();
		return ret;
	}

	@Override
	public Map<String, Object> WPOSVerify(String name, String iDcard, String facePic, Integer type, String openID,
			String serialNum,String ip) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// t1:查询对应序列号的服务账号信息
		boolean b = true;
		String s = jedisCluster.get(serialNum);
		if (null == s) {
			jedisCluster.set(serialNum, "true", "NX", "EX", 1800000);
		} else {
			LOGGER.error("该二维码已被使用，请重新扫码。");
			throw new Exception("该二维码已被使用，请重新扫码。");
		}
		AuthBiz ab = abdao.queryBySerial(serialNum);
		if (null == ab) {
			LOGGER.error("serialNum错误，非系统生成！");
			throw new Exception("无效二维码！");
		}
		
		// 判断账户是否超过使用次数
		String serverAccount = ab.getServerAccount();
//		checkAuthCount(serverAccount);
		//调用计费接口
		JFServiceEnum jfServiceEnum = StringUtils.isBlank(facePic)?JFServiceEnum.CARD_AUTH:JFServiceEnum.CARD_FACE_AUTH;
		JFResult<JFInfoModel> jf = jfClient.jf(serverAccount, jfServiceEnum,JFChannelEnum.API,ip);
		if(!JFResultEnum.SUCCESS.getCode().equals(jf.getCode())){
			throw new Exception(jf.getMessage());
		}
		JFInfoModel jfData = jf.getData();
		String userServiceAccount = jfData.getUserServiceAccount();
		String specialCode = jfData.getSpecialCode();

		CtidResult<CtidAuthReturnBean> cresult = auth(name, iDcard, facePic,specialCode);
		if (cresult.getCode() == 1) {
			throw new Exception("认证服务器异常！" + cresult.getMessage());
		}
		CtidAuthReturnBean authData = cresult.getData();
		String authMode = authData.getAuthMode();
		String authResult = authData.getAuthResult();
		String authDesc = authData.getAuthDesc();
		String token = authData.getToken();
		LOGGER.debug("-------------------authDesc-------------------: {}" + authDesc);

		// t4:保存业务信息
		int authResults = Contents.AUTH_RESULT_FALSE;
		int ctidType = 0;
		int isCtid = 1;
		if ("0x06".equals(authMode)) {
			// 有网证
			isCtid = Contents.IS_CTID_TRUE;
			// 网证服务返回该用户有网证信息
			ctidType = Contents.CTID_TYPE_ONE;
		} else if ("0x40".equals(authMode)) {
			// 两项
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_TWO;
		} else if ("0x42".equals(authMode)) {
			// 两项+人像
			isCtid = Contents.IS_CTID_FALSE;
			ctidType = Contents.CTID_TYPE_THREE;
		}
		if (authResult.equals("true")) {
			authResults = Contents.AUTH_RESULT_TRUE;
			// t2:查询二项信息+openID是否存在，不存在则保存
			PersonInfo person = personDao.selectByOpenID(openID);
			String encodeIdCardInfo = cipherService.encodeIdCardInfo(name, iDcard);
			String calculateHash = CalculateHashUtils.calculateHash(iDcard.getBytes());
			if (null == person) {
				person = new PersonInfo(UUIDGenerator.getSerialId(), type, openID, new Date(), calculateHash, null,
						encodeIdCardInfo, null, isCtid, picToFtp(facePic));
				personDao.insertSelective(person);
			} else {
				if(person.getIsCtid().equals(1)) {
					person.setIsCtid(isCtid);
					personDao.updateByPrimaryKeySelective(person);
				}
			}
		} else {
			b = false;
		}
		String bsn = authData.getBsn();
		AuthRecord ar = new AuthRecord(UUIDGenerator.getSerialId(), type, name, iDcard, Contents.AUTH_TYPE_WPOS,
				ctidType, authResults, ab.getServerAccount(), new Date(), bsn, facePic, null, openID, null,
				authDesc, Contents.AUTH_OBJECT_SELF, null);
		// recordDao.insertSelective(ar);

		mqSend.send(ar);

		//发送计费业务日志
		jfClient.send(userServiceAccount, jfServiceEnum, JFChannelEnum.API, authResults, authDesc,bsn,specialCode,serverAccount);

		// t5:将结果通过回调地址返回
		OnlineVO vo = new OnlineVO();
		vo.setAuthResult(authResults);
		vo.setSerialNum(serialNum);
		vo.setIdcard(iDcard);
		vo.setName(name);
		//		vo.setPic(facePic);
		vo.setVerifyDate(DateUtils.format(ar.getCreateTime()));
		vo.setServerName(ab.getServerDesc());  
		vo.setCtidType(ctidType);
		String result = JSONObject.fromObject(vo).toString();
		wPosOpenRequest = new WPosOpenRequest(appid, secret, token);
		wPosOpenRequest.postResourceServ(result.getBytes());
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("device_en", ab.getServerAccount());  
		LOGGER.info("设备号："+ab.getServerAccount());
		String pustJson="{\n" +
				"      \"icon\":\"http://wpos.qlogo.cn/a.jpg\"  " +
				"     ,\"title\":\"会员中心\"" +
				"     ,\"content\":\"XXX注册了会员\"" +
				"   ,   \"ext\":" + result +"   \n" +"    }" ;

		LOGGER.info(pustJson); 
		paramMap.put("data", pustJson);  
		paramMap.put("biz_code", bizCode);
		// 定义旺POS开放接口对应的serviceKey  
		ReturnData returnData = wPosOpenRequest.requestOpenApi(serviceKey, paramMap);
		LOGGER.error(returnData.getStatus()+"--"+returnData.getData()); 
		ab.setIsCallback(0);
		abdao.updateByPrimaryKey(ab);
		map.put("result", b);
		map.put("authDesc", authDesc);


		return map;
	}

}
