//package com.ahdms.ctidservice.web.controller;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.Semaphore;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.ahdms.api.model.ApplyReturnBean;
//import com.ahdms.api.model.CtidMessage;
//import com.ahdms.ctidservice.bean.DownCtidRequestReturnBean;
//import com.ahdms.ctidservice.bean.IdCardInfoBean;
//import com.ahdms.ctidservice.common.Base64Utils;
//import com.ahdms.ctidservice.common.FileUtils;
//import com.ahdms.ctidservice.common.UUIDGenerator;
//import com.ahdms.ctidservice.config.RabbitMqSender;
//import com.ahdms.ctidservice.constants.ErrorCodeConstants;
//import com.ahdms.ctidservice.db.dao.CtidInfosMapper;
//import com.ahdms.ctidservice.db.dao.CtidinfoMapper;
//import com.ahdms.ctidservice.db.model.CtidInfos;
//import com.ahdms.ctidservice.db.model.Ctidinfo;
//import com.ahdms.ctidservice.service.AuthCtidService;
//import com.ahdms.ctidservice.service.CreateCodeService;
//import com.ahdms.ctidservice.service.CtidPCManageService;
//import com.ahdms.ctidservice.service.DownCtidService;
//import com.ahdms.ctidservice.service.TokenCipherService;
//import com.ahdms.ctidservice.service.ValidateCodeService;
//import com.ahdms.ctidservice.service.VerifySignService;
//import com.ahdms.ctidservice.service.core.PKCS7Tool;
//import com.ahdms.ctidservice.util.CalculateHashUtils;
//import com.ahdms.ctidservice.util.PCControlApiUtils;
//import com.ahdms.ctidservice.util.ctid.ReservedDataUtils;
//import com.ahdms.ctidservice.vo.CtidResult;
//import com.ahdms.ctidservice.vo.ReservedDataEntity;
//import com.ahdms.ctidservice.vo.ReservedDataEntity.SFXXBean;
//
//import net.sf.json.JSONObject;
//
////@RestController
////@RequestMapping(value = "testApi", method = { RequestMethod.POST, RequestMethod.GET })
//public class TestApiController {
//
//	Logger logger = LoggerFactory.getLogger(TestApiController.class);
//	@Autowired
//	private CtidPCManageService ctidPCManageService;
//
//	@Autowired
//	private VerifySignService verifySignService;
//
//	@Autowired
//	private TokenCipherService cipherSer;
//
//	@Autowired
//	private DownCtidService downCtidService;
//
//	@Autowired
//	private AuthCtidService authCtidService;
//
//	@Autowired
//	private CreateCodeService createCodeService;
//
//	@Autowired
//	private ValidateCodeService validateCodeService;
//
//	@Autowired
//	private CtidInfosMapper ctidInfosMapper;
//
//	@Autowired
//	private CtidinfoMapper infoMapper;
//
//	@Autowired
//	private RabbitMqSender mqSend;
//
//	@Autowired
//	private PKCS7Tool p7Tool;
//
//	@Autowired
//	private PCControlApiUtils pcUtil;
//	
//	
//	@RequestMapping("getUserData")
//	public CtidResult getUserData(@RequestParam String name,
//			@RequestParam String idCard,@RequestParam String start,@RequestParam String end) {
//		byte[] nameBytes = new byte[45];
//		byte[] dnBytes = idCard.toUpperCase().getBytes();
//		byte[] startBytes = start.getBytes();
//		byte[] endBytes = end.getBytes();
//		System.arraycopy(name.getBytes(), 0, nameBytes, 0, name.getBytes().length);
//		
//		byte[] userData = new byte[45 + 18 + 8 + 8];
//		System.arraycopy(nameBytes, 0, userData, 0, 45);
//		System.arraycopy(dnBytes, 0, userData, 45, 18);
//		System.arraycopy(startBytes, 0, userData, 45 + 18, 8);
//		System.arraycopy(endBytes, 0, userData, 45 + 18 + 8, 8);
//		
//		try {
//			byte[] encodeLocal = cipherSer.encode(new String(userData));
//			return CtidResult.ok(Base64Utils.encode(encodeLocal));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return CtidResult.error("服务器内部错误");
//	}
//
//	@RequestMapping("testDownApply")
//	public CtidResult testDownApply() {
//		return downCtidService.downCtidApply("0x10");
//	}
//
//	@RequestMapping("testDownRequest")
//	public CtidResult testDownRequest(@RequestParam String bsn,@RequestParam String userDataStr) throws Exception {
//		String appId = "0003";
////		String userDataStr = "MIIBqAIBADGCATYwggEyAgEAMIGiMIGPMSQwIgYDVQQDDBtJRENUSURJbnN0aXR1dGlvbmFsZW50aXR5aWQxEjAQBgNVBAsMCWlkY3RpZDEyMzExMC8GCgmSJomT8ixkARkWIUZkQVFXS0JDWTI2RjY1aDNTVnpBb3pZM3lZbkNTYVBaSjETMBEGCgmSJomT8ixkARkWA1RJRDELMAkGA1UEBhMCQ04CDgD+129Hu79hpPj1P1m6MAsGCSqBHM9VAYItAwR7MHkCIF3YJol1pIr0dQueohbqxln6ySmuPkYa15AKmg1pzWDSAiEAr4r/ClK99NANFHSQX0FNZ/Wx06f1mGCKhPH536RfHaEEIOokZEJMMJ9BrEUmtLOZp6xGsljT5J3QmDDzwl944bKBBBBFA7j/rjVeKaaUXxyWd1bfMGkGCiqBHM9VBgEEAgEwCQYHKoEcz1UBaIBQ1Q2OQly8LBpHFu8RYp30neseK3EY9BQBT2z70sMCnNIl6/Wln6GdUu9iiZqFbWncrGTBCUeIKItjboAyZIVLlFU+ihe6Z/O0KOuP8vsAT9s=";
//		String vCode = "vAFNSUlCUndZS0tvRWN6MVVHQVFRQ0E2Q0NBVGN3Z2dFekFnRUFNWUhTTUlIUEFnRUFNRDB3TVRFTE1Ba0dBMVVFQmhNQ1EwNHhFVEFQQmdOVkJBb1RDRWRNUTFSSlJEQXhNUTh3RFFZRFZRUURFd1pIVEVOQk1ERUNDSDk2d1NyaVVDblBNQTBHQ1NxQkhNOVZBWUl0QXdVQUJId3dlZ0loQVBRRVJvTDM5VjgvYWttR0l4SUZ4VUxMR3RpUmJ5U1g1MmZ6QkxzRmlaanlBaUVBMGpGZ2tDMERrdnpjZS9EVGRKaDIzb25WUVhPSzFoSmhtL2tYKzBMQXA4VUVJRkhCcHlFaUd6UFFsQklVTGlMVUdZRndyTlhFcDVmTXRUOTQ2a1cvOVo0ZkJCQzFKbE5uNktVRCs3dW9oYnZYS2hocU1Ga0dDaXFCSE05VkJnRUVBZ0V3Q1FZSEtvRWN6MVVCYUlCQWVXN096cHgyZ3hDTEJIblZkRmdycmM2MFkyS09PQ29iOUp0c2Nhc3paYlQ0RWhncm40QnFlL2xFMk1HUHV6Y3Z6SlZuRWVyWEtNQmlEbUxIVVNBN3N3PT0AAA==";
//		String idCheck = "MIIB+QYKKoEcz1UGAQQCA6CCAekwggHlAgEAMYHRMIHOAgEAMD0wMTELMAkGA1UEBhMCQ04xETAPBgNVBAoTCEdMQ1RJRDAxMQ8wDQYDVQQDEwZHTENBMDECCH96wSriUCnPMA0GCSqBHM9VAYItAwUABHsweQIhAMDurY7IMFGmjYo7u/s1cIK61Ysw406UF7B7gmDo59f1AiB5veelJtBIrnJadgY5AdPawjbI1upaCv4YcahilLIxBgQg9JUwg8yfJtscZmtIW5J2IKy3MJWNSA27pXlKnCNrnzYEEM3LjCjdmjVSotLh+uJ77y4wggEKBgoqgRzPVQYBBAIBMAkGByqBHM9VAWiAgfB3CAzq3I+sG0X97Btnd8wW/wdgnCUkNTp6kFWHY32EvT31oSJK6o/clRG2vj7yo0gIt7rB3tVEd+CzVoM+V2u9zbCr0U/caPhsgAsLNUeE8kskZNxoGWEuZcgDQGh8k+r/Ufq9jg4r33HfuuRV//gXcQleVOPHjiNFsYUiSaNRsMBuX3XTBwnRSZvTEUf54KEDxu53zelXnIb05Vj0xBmXjCMxNZeQpX5Ci4+xqqtUEkmp4Vujdq9Q5wedTJKdqDkKF8Wakx30Wtef7OBHM0PvMBhWdqDbWG6h50/MUQZkpM7Mln7wBUS8v3lJpNSwpkg=";
//
//		SFXXBean sfxxBean = null;
//		// 根据参数判断如何取
//		if (StringUtils.isNotBlank(userDataStr)) {
//			try {
//				// 解密用户数据，提取身份信息
//				byte[] userData = cipherSer.decode(Base64Utils.decode(userDataStr));
//				// 加密身份保留数据四项
//				sfxxBean = ReservedDataUtils.getSFXXBean(userData);
//			} catch (Exception e) {
//				return CtidResult.error("用户信息解密失败！下载失败！");
//			}
//		}
//
//		if (null == sfxxBean) {
//			return CtidResult.error("用户信息为空！下载失败！");
//		}
//
//		String authApplyRetainData = p7Tool.encodeLocal(JSONObject.fromObject(sfxxBean).toString());
//		CtidResult<CtidMessage> downCtidRequest = downCtidService.downCtidRequest(bsn, "0x10", null, idCheck, vCode,
//				authApplyRetainData);
//
//		if (0 == downCtidRequest.getCode()) {
//			// 下载成功
//			CtidInfos ctidInfos = new CtidInfos();
//			CtidMessage data = downCtidRequest.getData();
//			String id = UUIDGenerator.getUUID();
//			ctidInfos.setId(id);
//			String ctidInfo = data.getCtidInfo();
//
//			String idCardHash = CalculateHashUtils.calculateHash(sfxxBean.getgMSFZHM().getBytes());
//			CtidInfos selectByCardNum = ctidInfosMapper.selectByCardNum(idCardHash);
//			Date date = new Date();
//			// 计算得到网证过期时间
//			if (selectByCardNum != null) {
//				selectByCardNum.setCardStartDate(sfxxBean.getyXQQSRQ());
//				selectByCardNum.setCardEndDate(sfxxBean.getyXQJZRQ());
//				selectByCardNum.setCreateDate(date);
//				ctidInfosMapper.updateByPrimaryKeySelective(selectByCardNum);
//
//				Ctidinfo ci = infoMapper.selectByUserIdAndAppId(selectByCardNum.getId(), appId);
//				if (ci != null) {
//					ci.setCtidDownTime(date);
//					ci.setCtidInfo(data.getCtidInfo());
//					ci.setCtidStatus(data.getCtidStatus());
//					infoMapper.updateByPrimaryKeySelective(ci);
//				} else {
//					ci = new Ctidinfo();
//					ci.setId(UUIDGenerator.getUUID());
//					ci.setAppId(appId);
//					ci.setCtidDownTime(date);
//					ci.setCtidInfo(data.getCtidInfo());
//					ci.setUserId(selectByCardNum.getId());
//					ci.setCtidStatus(data.getCtidStatus());
//					infoMapper.insertSelective(ci);
//				}
//
//				id = selectByCardNum.getId();
//			} else {
//				IdCardInfoBean idCard = new IdCardInfoBean(sfxxBean.getxM(), sfxxBean.getgMSFZHM());
//				byte[] encode = cipherSer.encode(JSONObject.fromObject(idCard).toString());
//				String encode2 = Base64Utils.encode(encode);
//				System.out.println(encode2.length()+">>>"+encode2);
//				ctidInfos.setCardName(encode2);
//				ctidInfos.setCardNum(idCardHash);
//				ctidInfos.setCardStartDate(sfxxBean.getyXQQSRQ());
//				ctidInfos.setCardEndDate(sfxxBean.getyXQJZRQ());
//				ctidInfos.setCreateDate(new Date());
//				// 入库
//				ctidInfosMapper.insertSelective(ctidInfos);
//				Ctidinfo ci = new Ctidinfo();
//				ci.setId(UUIDGenerator.getUUID());
//				ci.setAppId(appId);
//				ci.setCtidDownTime(date);
//				ci.setCtidInfo(data.getCtidInfo());
//				ci.setUserId(id);
//				ci.setCtidStatus(data.getCtidStatus());
//				infoMapper.insertSelective(ci);
//			}
//
//			DownCtidRequestReturnBean dcrrBean = new DownCtidRequestReturnBean();
//			dcrrBean.setCtidIndex(id);
//			dcrrBean.setCtidInfo(ctidInfo);
//			return CtidResult.ok(dcrrBean);
//		}
//		return downCtidRequest;
//	}
//
//	@RequestMapping("testDown")
//	public CtidResult testDown(@RequestParam String userDataStr) throws Exception {
//		CtidResult<ApplyReturnBean> downCtidApply = downCtidService.downCtidApply("0x10");
//		String bsn = downCtidApply.getData().getBusinessSerialNumber();
//		return testDownRequest(bsn,userDataStr);
//	}
//
//	private final Semaphore permit = new Semaphore(10, true);
//
//	@RequestMapping("testAuthApply")
//	public CtidResult testAuthApply() {
//		try {
//            permit.acquire();
//            return authCtidService.authCtidApply("0x06","");
//        }catch (Exception e){
//        	logger.error(e.getMessage(),e);
//        }finally {
//            permit.release();
//        }
//		return CtidResult.error("并发达上限！");
//	}
//	
//	public static String faceData;
//	
//	static {
//		try {
//			faceData = Base64Utils.encode(FileUtils.toByteArray("E:/mine/qinxiang.jpg"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@RequestMapping("/testCardAuth")
//	public CtidResult<Map<String, String>> testCardAuth() throws Exception{
//		String businessSerialNumber = "";
//		String authMode = "0x42";
//		CtidResult<Map<String, String>> authCtidRequest = new CtidResult<Map<String, String>>();
//		try {
//			CtidResult<ApplyReturnBean> authCtidApply = authCtidService.authCtidApply(authMode,"");
//			if(authCtidApply.getCode() != 0){
//				authCtidRequest.setCode(1);
//				authCtidRequest.setMessage(authCtidApply.getMessage());
//			}else{
//				ApplyReturnBean data = authCtidApply.getData();
//				businessSerialNumber = data.getBusinessSerialNumber();
//				//将认证码和随机数发给PC端服务器
//				ReservedDataEntity reservedDataEntity = ReservedDataUtils.getReservedDataEntity("覃翔", "430523199011157277", "", "");
//				String authApplyRetainData = p7Tool.encodeLocal(JSONObject.fromObject(reservedDataEntity).toString());
//				authCtidRequest = authCtidService.authCtidRequest(businessSerialNumber, authMode, faceData, "", "", authApplyRetainData,"");
//			}
//			
//		} catch (Exception e) {
//		}
//		return authCtidRequest;
//	}
//	
//	@RequestMapping("/test40Auth")
//	public CtidResult<Map<String, String>> test40Auth() throws Exception{
//		String businessSerialNumber = "";
//		String authMode = "0x40";
//		CtidResult<Map<String, String>> authCtidRequest = new CtidResult<Map<String, String>>();
//		try {
//			CtidResult<ApplyReturnBean> authCtidApply = authCtidService.authCtidApply(authMode,"");
//			if(authCtidApply.getCode() != 0){
//				authCtidRequest.setCode(1);
//				authCtidRequest.setMessage(authCtidApply.getMessage());
//			}else{
//				ApplyReturnBean data = authCtidApply.getData();
//				businessSerialNumber = data.getBusinessSerialNumber();
//				//将认证码和随机数发给PC端服务器
//				ReservedDataEntity reservedDataEntity = ReservedDataUtils.getReservedDataEntity("覃翔", "430523199011157277", "", "");
//				String authApplyRetainData = p7Tool.encodeLocal(JSONObject.fromObject(reservedDataEntity).toString());
//				authCtidRequest = authCtidService.authCtidRequest(businessSerialNumber, authMode, "", "", "", authApplyRetainData,"");
//			}
//			
//		} catch (Exception e) {
//		}
//		return authCtidRequest;
//	}
//	
//	@RequestMapping("testAuthRequest")
//	public CtidResult testAuthRequest(@RequestParam String bsn) {
//		try {
//			String idCheck = "BTCCAyoGCiqBHM9VBgEEAgOgggMaMIIDFgIBADGB0TCBzgIBADA9MDExCzAJBgNVBAYTAkNOMREwDwYDVQQKEwhHTENUSUQwMTEPMA0GA1UEAxMGR0xDQTAxAgh/esEq4lApzzANBgkqgRzPVQGCLQMFAAR7MHkCIQDGUpqzUFp7R1yB7QJZq8jRbfL+Zv5tnuAx9pHR7wnqTwIgGZjEXGehomRzA1eDn42kxXuPezHk4lgkIUCLEhSImLAEIAwHhfb00UcLjbWBi1qKJILVl3qLsi896UlZQg59/yMnBBANJj5JYc/xlEXGcxYOTSofMIICOwYKKoEcz1UGAQQCATAJBgcqgRzPVQFogIICIG24XRXZb8ERN+IXakFd9+nWfCb0RM4H0Rq+F8EJsOXYDn3UDnPDcj92htcMI4/LSF/AjsYLSbuzfiguNJcs0kUoqmHKCDmbalZyZHojeRZsfAQwnb9CRcuAKIQbfW+ozG3/4pT4pFdpw3teryEDsJLsymIgxPqYtuXZuUg1T0xBujntIIRdBVvsnPzXp4xPKzI4JCjeykrfyiME36qpu6JOQNxBOpyBJe+tE+3w63/c14Pks9bnvsfLzR27gKPDprK82IhEaGqHxSove6aVi70ygavX7aJuCkeSNBuq5OLBZHQ34JquvRIOcxMbt+VUbcWTjHhtpY2AoIvFHnT1UoVQTA0Kzm8wBiwh1zVBvV0CkvYYRC3PD7ajvySlOu17GKFJDs3Lr1qmvINPk+m0I56EPb0OkqFA7IaGDABjdjZP66X0YFPED7c3E/Z/B6dvlhZUj/F8vA2mcqDHZUMBo5e9XWwS1jYgin78U7EpeiGsQUVveHLOilTIvsGYq8sPtSX37dSXiWfDvgHTXipWGmlrjiu57SdjnChtkQbBuWaL+jmWyriBYmxRD1sAeV7ZFg8timp+MNDhQFPiZCs2fOkeTz5XF0v/potRX5JhQFDW6ugBN8hHVQT49S7TtX3bGDGavbjmkpnGYo787OmciUTNhwHZ98YD/XcSxcz1IBsnI61Fyp6zSMhZOo79T+ReubAUEeY9xkb8//jBJ2rdI0A=";
//			String mode = "0x06";
////			faceData = Base64Utils.encode(FileUtils.toByteArray(faceData));
//
//			CtidResult<Map<String, String>> authCtidRequest = authCtidService.authCtidRequest(bsn, mode, faceData,
//					idCheck, null, null,"");
//			return authCtidRequest;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return CtidResult.error("服务器内部错误");
//	}
//
//	@RequestMapping("testAuth")
//	public CtidResult testAuth() {
//		CtidResult<ApplyReturnBean> authCtidApply = authCtidService.authCtidApply("0x06","");
//		if(authCtidApply.getCode() != 0){
//			return authCtidApply;
//		}
//		String bsn = authCtidApply.getData().getBusinessSerialNumber();
//		return testAuthRequest(bsn);
//	}
//	
//	@RequestMapping("testQrCodeApply")
//	public CtidResult testQrCodeApply(){
////		String applyData = pcUtil.getQRCodeApplyData(CtidConstants.CREATE_QRCODE_TYPE);
//		String applyData = "MIIByAYKKoEcz1UGAQQCA6CCAbgwggG0AgEAMYHRMIHOAgEAMD0wMTELMAkGA1UEBhMCQ04xETAPBgNVBAoTCEdMQ1RJRDAxMQ8wDQYDVQQDEwZHTENBMDECCH96wSriUCnPMA0GCSqBHM9VAYItAwUABHsweQIgEHQff+ez4nvdPodTx5z88XurRCN6tpef73Bmc3xbbi8CIQCeyaw6Y7O+8FxKhVKd/egRr6XtlY4zruOfm2RflSuLlQQgm28/ZLqaDUV4UsvF+K41WmdrECWafL3YrYmfKPccig0EEKTdvDfhzDOg4sfy46vrnDgwgdoGCiqBHM9VBgEEAgEwCQYHKoEcz1UBaICBwLAYfjJkSqbC0bHDGUkdFsC5qMjvc/2PJdgwQNrNKvgyh61TM3TWLigiy9/YrUE9Q1eNLyXMmKpRz4SIdUbjjfh3uk7RaNePpxvQYWM59l/wJtlxdRGOWoOubC/ww7FQYY3zyzNY7B3nyFluGiIztxEiipcWgMQk/wp6hNvbS52osnzOafrsTfPuH1RJV/gKoCUTYl7+IJ+BAg2JDKxLUwdtw7Sg1LvMaaOmhUHXp7lBbFn64cgqb/+WDy2tpDWDDw==";
//		return createCodeService.createCodeApply(applyData);
//	}
//	
//	@RequestMapping("testQrCodeRequest")
//	public CtidResult testQrCodeRequest(@RequestParam String bsn,@RequestParam String ctidInfo,
//			@RequestParam String randomNum){
//		String checkData = pcUtil.getReqQRCodeData(randomNum, ctidInfo);
//		return createCodeService.createCodeRequest(bsn, checkData, 1);
//	}
//	
//	@RequestMapping("testQrCode")
//	public CtidResult testQrCode(@RequestParam String ctidInfo){
//		try {
////			String applyData = pcUtil.getQRCodeApplyData(CtidConstants.CREATE_QRCODE_TYPE);
//			String applyData = "MIIByAYKKoEcz1UGAQQCA6CCAbgwggG0AgEAMYHRMIHOAgEAMD0wMTELMAkGA1UEBhMCQ04xETAPBgNVBAoTCEdMQ1RJRDAxMQ8wDQYDVQQDEwZHTENBMDECCH96wSriUCnPMA0GCSqBHM9VAYItAwUABHsweQIgEHQff+ez4nvdPodTx5z88XurRCN6tpef73Bmc3xbbi8CIQCeyaw6Y7O+8FxKhVKd/egRr6XtlY4zruOfm2RflSuLlQQgm28/ZLqaDUV4UsvF+K41WmdrECWafL3YrYmfKPccig0EEKTdvDfhzDOg4sfy46vrnDgwgdoGCiqBHM9VBgEEAgEwCQYHKoEcz1UBaICBwLAYfjJkSqbC0bHDGUkdFsC5qMjvc/2PJdgwQNrNKvgyh61TM3TWLigiy9/YrUE9Q1eNLyXMmKpRz4SIdUbjjfh3uk7RaNePpxvQYWM59l/wJtlxdRGOWoOubC/ww7FQYY3zyzNY7B3nyFluGiIztxEiipcWgMQk/wp6hNvbS52osnzOafrsTfPuH1RJV/gKoCUTYl7+IJ+BAg2JDKxLUwdtw7Sg1LvMaaOmhUHXp7lBbFn64cgqb/+WDy2tpDWDDw==";
//
//			CtidResult<ApplyReturnBean> createCodeApply = createCodeService.createCodeApply(applyData);
//			if(createCodeApply.getCode() == 0){
//				ApplyReturnBean data = createCodeApply.getData();
//				String bsn = data.getBusinessSerialNumber();
//				String randomNumber = data.getRandomNumber();
//				String checkData = pcUtil.getReqQRCodeData(randomNumber, ctidInfo);
//				return createCodeService.createCodeRequest(bsn, checkData, 0);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return CtidResult.error("服务器内部错误");
//	}
//
//	private CtidResult authCtidExpire(String cardEndDateStr, Date ctidDownTime) {
//		try {
//			Date nowDate = new Date();
//			if (!"00000000".equals(cardEndDateStr)) {
//				Date cardEndDate = new SimpleDateFormat("yyyyMMdd").parse(cardEndDateStr);
//				if (nowDate.after(cardEndDate)) {
//					return CtidResult.error(ErrorCodeConstants.AUTH_USERCARD_EXPIRE_CODE,
//							ErrorCodeConstants.AUTH_USERCARD_EXPIRE_MESSAGE);
//				}
//			}
//			Calendar ca = Calendar.getInstance();
//			ca.setTime(nowDate);
//			ca.add(Calendar.MONTH, 6);
//			if (ca.getTime().after(ctidDownTime)) {
//				return CtidResult.error(ErrorCodeConstants.AUTH_CTID_EXPIRE_CODE,
//						ErrorCodeConstants.AUTH_CTID_EXPIRE_MESSAGE);
//			}
//			return CtidResult.ok("success");
//		} catch (ParseException e) {
//			logger.error(e.getMessage(), e);
//		}
//		return CtidResult.error("服务器内部错误");
//	}
//	
//	public static void main(String[] args) {
//		long m1 = System.currentTimeMillis();
//		long pos = 1000*60*60*24;
//		long start = 0l;
//		
//	}
//}
