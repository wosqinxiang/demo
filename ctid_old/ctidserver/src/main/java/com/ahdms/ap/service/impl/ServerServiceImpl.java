/**
 * Created on 2019年8月5日 by liuyipin
 */
package com.ahdms.ap.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.common.GridModel;
import com.ahdms.ap.config.FastDFSTool;
import com.ahdms.ap.dao.AuthRecordDao;
import com.ahdms.ap.dao.PersonInfoDao;
import com.ahdms.ap.dao.ServerAccountDao;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ap.model.PersonInfo;
import com.ahdms.ap.model.ServerAccount;
import com.ahdms.ap.service.ServerService;
import com.ahdms.ap.utils.Gb2312Utils;
import com.ahdms.ap.utils.IdcardInfoExtractor;
import com.ahdms.ap.vo.AuthRecordVo;
import com.ahdms.ap.vo.CtidVO;
import com.ahdms.ap.vo.PersonInfoVO;
import com.ahdms.ctidservice.bean.IdCardInfoBean;
import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.db.dao.CtidinfoMapper;
import com.ahdms.ctidservice.db.model.Ctidinfo;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.util.CalculateHashUtils;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

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
@Service
public class ServerServiceImpl implements ServerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerServiceImpl.class);

	@Autowired
	private ServerAccountDao serverDao;

	@Autowired
	private AuthRecordDao recordDao;

	@Autowired
	private PersonInfoDao pdao;


	@Autowired
	private CtidinfoMapper ctidDao;

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
	private FastDFSTool FastDFSTool;


	@Autowired
	private TokenCipherService cipherService;
	@Override
	public int login(String account, String password) {
		ServerAccount i = serverDao.selectServer(account, password);
		if(null != i){
			return 0;
		}
		else{
			return 1;
		}
	}

	@Override
	public GridModel<AuthRecord> recordsList(String infoSource, String IDcard, int page, int rows) {
		String orderString = "";
		PageBounds pageBounds = new PageBounds(page, rows, Order.formString(orderString));
		GridModel<AuthRecord> girdModel = new GridModel<>();
		String calculateHash = CalculateHashUtils.calculateHash(IDcard.getBytes()); 
		PageList<AuthRecord> list = recordDao.select(calculateHash,pageBounds);
		for (AuthRecord authRecord : list) {
			//			if(null != authRecord.getName() && !"".equals( authRecord.getName())){
			/*IdCardInfoBean info = cipherService.decodeIdCardInfo(authRecord.getName());
				if(null != info){*/
			authRecord.setName(null);
			authRecord.setIdcard(null);
			//				}
			//			} 

		}
		girdModel.setRows(list);
		return girdModel;
		//		if(infoSource.equals(Integer.toString(Contents.INFO_SOURCE_APP))){
		//		}else{
		//			if(list.size() > 5){
		//				return list.subList(0, 4);
		//			}else{
		//				return list.subList(0, list.size());
		//			}
		//		}

	}

	@Override
	public AuthRecordVo getRecord(String recordId) throws Exception {

		AuthRecord authRecord = recordDao.selectByPrimaryKey(recordId);
		AuthRecord otherAuthRecord = null;
		if(authRecord.getAuthType() == Contents.AUTH_TYPE_TRANSPORT || authRecord.getAuthType() == Contents.AUTH_TYPE_FTF){
			otherAuthRecord = recordDao.selectByPrimaryKey(authRecord.getCertifiedId());
		}
		AuthRecordVo authRecordVo = new AuthRecordVo();

		String serialNum = authRecord.getSerialNum();
		String name = authRecord.getName();
		String signData = authRecord.getSignData();

		if (null != authRecord &&  null == otherAuthRecord) {
			IdCardInfoBean info = cipherService.decodeIdCardInfo(authRecord.getName()); 
			authRecordVo.setId(authRecord.getId());
			authRecordVo.setAuthResult(authRecord.getAuthResult());
			authRecordVo.setAuthType(authRecord.getAuthType());
			authRecordVo.setCreateTime(authRecord.getCreateTime());
			authRecordVo.setCtidType(authRecord.getCtidType());
			authRecordVo.setIdcard(info.getCardNum().substring(0, 8)+"********"+info.getCardNum().substring(16));
			authRecordVo.setInfoSource(authRecord.getInfoSource());
			authRecordVo.setName(info.getCardName().substring(0,1)+"*"+info.getCardName().substring(info.getCardName().length()-1));
			authRecordVo.setOpenid(authRecord.getOpenid());
			authRecordVo.setSerialNum(serialNum);
			authRecordVo.setAuthObject(authRecord.getAuthObject());
			IdcardInfoExtractor ie = new IdcardInfoExtractor(info.getCardNum());   
			authRecordVo.setDomicilePlace(ie.getCity());
			String facePic = authRecord.getPic();

			if(null != facePic &&  !"".equals(facePic)){ 
				byte[] face = FastDFSTool.download(facePic);

				authRecordVo.setPic(Base64Utils.encode(face));
			}
			authRecordVo.setServerAccount(authRecord.getServerAccount());

			if (null != signData) {
				authRecordVo.setSignData(signData);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String signTime = sdf.format(authRecord.getCreateTime());
				LOGGER.debug("记录详情 ---> CreateTime {}", authRecord.getCreateTime());
				LOGGER.debug("记录详情 ---> signTime {}", signTime);

				String deviceTypeNum = serialNum.substring(0, 2);
				name = info.getCardName();
				if (Contents.CERT_BOX.equals(deviceTypeNum)) {
					try {
						// 如果是身份宝盒认证，姓名作gb2312转换
						name = Gb2312Utils.gb2312eecode(name);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				String twoBarCodeData = getTwoBarCodeData(signData, serialNum,
						info.getCardNum(), signTime, name);
				LOGGER.debug("记录详情 ---> twoBarCodeData {}", twoBarCodeData);

				if(authRecord.getCreateTime().before(initDateByDay())){
					authRecordVo.setTwoBarCodeValidate("1");
					authRecordVo.setTwoBarCodeData("此二维码已失效");
				}else{
					authRecordVo.setTwoBarCodeValidate("0");
					authRecordVo.setTwoBarCodeData(twoBarCodeData);
				}


			}
		}else{
			IdCardInfoBean otherinfo = cipherService.decodeIdCardInfo(otherAuthRecord.getName());  
			IdCardInfoBean info = cipherService.decodeIdCardInfo(authRecord.getName()); 
			authRecordVo.setId(otherAuthRecord.getId());
			authRecordVo.setAuthResult(otherAuthRecord.getAuthResult());
			authRecordVo.setAuthType(otherAuthRecord.getAuthType());
			authRecordVo.setCreateTime(otherAuthRecord.getCreateTime());
			authRecordVo.setCtidType(otherAuthRecord.getCtidType());
			authRecordVo.setIdcard(otherinfo.getCardNum().substring(0, 8)+"********"+otherinfo.getCardNum().substring(16));
			authRecordVo.setInfoSource(otherAuthRecord.getInfoSource());
			authRecordVo.setName(otherinfo.getCardName().substring(0,1)+"*"+otherinfo.getCardName().substring(otherinfo.getCardName().length()-1));
			authRecordVo.setOpenid(otherAuthRecord.getOpenid());
			authRecordVo.setSerialNum(serialNum);
			authRecordVo.setAuthObject(authRecord.getAuthObject());
			IdcardInfoExtractor ie = new IdcardInfoExtractor(otherinfo.getCardNum());   
			authRecordVo.setDomicilePlace(ie.getCity());
			String facePic = otherAuthRecord.getPic();
			if(authRecord.getAuthObject().equals(Contents.AUTH_OBJECT_SELF)) {
				authRecordVo.setCertificationInitiator(otherinfo.getCardName());
			}else {
				authRecordVo.setCertificationInitiator(info.getCardName());
			}

			if(null != facePic &&  !"".equals(facePic)){
				/* String localdec = facePic.substring(facePic.lastIndexOf("/")+1) ;
				UpLoadFileUtil.downloadFile(ftpUrl, 21, userName, password, facePic.substring(0, facePic.lastIndexOf("/")), 
						localdec, location);
				String pic = PicUtil.ImageToBase64ByLocal(location+"/"+localdec);
				//删除本地暂存的图片
				File file=new File(location+"/"+localdec);
				if(file.exists() && file.isFile()){
					file.delete(); 
				} */
				byte[] face = FastDFSTool.download(facePic);

				authRecordVo.setPic(Base64Utils.encode(face));
			}
			authRecordVo.setServerAccount(otherAuthRecord.getServerAccount());
		}

		return authRecordVo;
	}

	private String getTwoBarCodeData(String signData, String serialNum, String idNum, String signTime, String name) {

		String s = new StringBuilder().append(signData).append(serialNum).append(idNum).append(signTime).append(name).toString();

		String twoBarCodeData;
		if (Contents.CERT_BOX.equals(serialNum.substring(0,2))) {
			String twoBarCodeDataLength = Integer.toHexString(s.length());
			twoBarCodeData = new StringBuilder().append(twoBarCodeDataLength).append(s).toString();
		} else {
			twoBarCodeData = s;
		}

		return twoBarCodeData;
	}

	@Override
	public PersonInfoVO getIdCard(String openId) {
		PersonInfoVO vo = new PersonInfoVO();
		PersonInfo p = pdao.selectByOpenID(openId);
		if(null != p){
			/*	Ctidinfo ctid = ctidDao.selectByCardNum(p.getIdcard());
			if(null != ctid){
				vo.setIsCtid(Contents.IS_CTID_TRUE);
			}else{
				vo.setIsCtid(Contents.IS_CTID_FALSE);
			}*/

			IdCardInfoBean info = cipherService.decodeIdCardInfo(p.getName()); 
			vo.setIsCtid(p.getIsCtid());
			vo.setOpenid(p.getOpenid());
			vo.setIdcard(info.getCardNum());
			vo.setName(info.getCardName());
		}else{
			return null;
		}
		return vo;
	}

	@Override
	public CtidVO getCtidInfo(String iDcard) {
		// TODO Auto-generated method stub
		CtidVO vo = new CtidVO();
		Ctidinfo ctid = ctidDao.selectByCardNum(iDcard);
		if(null == ctid){
			return null;
		}else{
			vo.setCtidNum(ctid.getCtidNum());
			vo.setCtidValidDate(ctid.getCtidValidDate());
		}
		return vo;
	}


	private Date initDateByDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	} 

	@Override
	public AuthRecordVo getRecordBySerialNum(String serialNum, String openId) throws Exception {
		AuthRecord authRecord = recordDao.getRecordBySerialNum(openId,serialNum);
		if(null == authRecord){
			throw new Exception("认证记录不存在。");
		} 
		AuthRecordVo authRecordVo = new AuthRecordVo();

		serialNum = authRecord.getSerialNum();
		String name = authRecord.getName();
		String signData = authRecord.getSignData();

		if (null!=authRecord ) {
			IdCardInfoBean info = cipherService.decodeIdCardInfo(authRecord.getName()); 
			authRecordVo.setId(authRecord.getId());
			authRecordVo.setAuthResult(authRecord.getAuthResult());
			authRecordVo.setAuthType(authRecord.getAuthType());
			authRecordVo.setCreateTime(authRecord.getCreateTime());
			authRecordVo.setCtidType(authRecord.getCtidType());
			authRecordVo.setIdcard(info.getCardNum());
			authRecordVo.setInfoSource(authRecord.getInfoSource());
			authRecordVo.setName(info.getCardName());
			authRecordVo.setOpenid(authRecord.getOpenid());
			authRecordVo.setSerialNum(serialNum);
			authRecordVo.setAuthObject(authRecord.getAuthObject());
			IdcardInfoExtractor ie = new IdcardInfoExtractor(info.getCardNum());   
			authRecordVo.setDomicilePlace(ie.getCity());
			String facePic = authRecord.getPic();

			if(null != facePic &&  !"".equals(facePic)){
				/* String localdec = facePic.substring(facePic.lastIndexOf("/")+1) ;
				UpLoadFileUtil.downloadFile(ftpUrl, 21, userName, password, facePic.substring(0, facePic.lastIndexOf("/")), 
						localdec, location);
				String pic = PicUtil.ImageToBase64ByLocal(location+"/"+localdec);
				//删除本地暂存的图片
				File file=new File(location+"/"+localdec);
				if(file.exists() && file.isFile()){
					file.delete(); 
				} */
				byte[] face = FastDFSTool.download(facePic);

				authRecordVo.setPic(Base64Utils.encode(face));
			}
			authRecordVo.setServerAccount(authRecord.getServerAccount());

			if (null != signData) {
				authRecordVo.setSignData(signData);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String signTime = sdf.format(authRecord.getCreateTime());
				LOGGER.debug("记录详情 ---> CreateTime {}", authRecord.getCreateTime());
				LOGGER.debug("记录详情 ---> signTime {}", signTime);

				String deviceTypeNum = serialNum.substring(0, 2);
				name = info.getCardName();
				if (Contents.CERT_BOX.equals(deviceTypeNum)) {
					try {
						// 如果是身份宝盒认证，姓名作gb2312转换
						name = Gb2312Utils.gb2312eecode(name);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}

				String twoBarCodeData = getTwoBarCodeData(signData, serialNum,
						info.getCardNum(), signTime, name);
				LOGGER.debug("记录详情 ---> twoBarCodeData {}", twoBarCodeData);

				if(authRecord.getCreateTime().before(initDateByDay())){
					authRecordVo.setTwoBarCodeValidate("1");
					authRecordVo.setTwoBarCodeData("此二维码已失效");
				}else{
					authRecordVo.setTwoBarCodeValidate("0");
					authRecordVo.setTwoBarCodeData(twoBarCodeData);
				}


			}
			if(null != authRecord.getAuthObject() && authRecord.getAuthObject().equals(Contents.AUTH_OBJECT_SELF)) {
				AuthRecord otherAuthRecord = recordDao.selectByPrimaryKey(authRecord.getCertifiedId());
				IdCardInfoBean otherinfo = cipherService.decodeIdCardInfo(otherAuthRecord.getName());  
				authRecordVo.setCertificationInitiator(otherinfo.getCardName());
			}else if(null != authRecord.getAuthObject() && authRecord.getAuthObject().equals(Contents.AUTH_OBJECT_OTHER)) {
				authRecordVo.setCertificationInitiator(info.getCardName());
			}
		} 

		return authRecordVo;
	} 
}



