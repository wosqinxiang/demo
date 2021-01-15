/**
 * <p>Title: WxCtidServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年9月19日
 * @version 1.0
 */
package com.ahdms.ctidservice.service.impl;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.dao.PersonInfoDao;
import com.ahdms.ap.model.PersonInfo;
import com.ahdms.ap.utils.UUIDGenerator;
import com.ahdms.api.model.AuthCtidValidDateReturnBean;
import com.ahdms.api.model.CtidMessage;
import com.ahdms.ctidservice.bean.IdCardInfoBean;
import com.ahdms.ctidservice.bean.UserCardInfoBean;
import com.ahdms.ctidservice.constants.CtidConstants;
import com.ahdms.ctidservice.constants.ErrorCodeConstants;
import com.ahdms.ctidservice.db.dao.CtidInfosMapper;
import com.ahdms.ctidservice.db.dao.CtidinfoMapper;
import com.ahdms.ctidservice.db.model.CtidInfos;
import com.ahdms.ctidservice.db.model.Ctidinfo;
import com.ahdms.ctidservice.service.CtidPCManageService;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.service.WxCtidService;
import com.ahdms.ctidservice.util.CalculateHashUtils;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.jf.client.JFClient;
import com.ahdms.jf.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Title: WxCtidServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author qinxiang
 * @date 2019年9月19日
 */
@Service
public class WxCtidServiceImpl implements WxCtidService {
    private static final Logger LOG = LoggerFactory.getLogger(WxCtidServiceImpl.class);

    @Autowired
    private CtidPCManageService ctidPCManageService;

    @Autowired
    private PersonInfoDao personDao;

    @Autowired
    private CtidInfosMapper ctidInfosMapper;

    @Autowired
    private CtidinfoMapper infoMapper;

    @Autowired
    private TokenCipherService tokenCipherService;

    @Autowired
    private JFClient jfClient;

    @Override
    public CtidResult downCtid(String openID, String cardName, String cardNum, String cardStart, String cardEnd,
                               String authCode,String ip) {
        try {
            cardNum = cardNum.toUpperCase();

            JFResult<JFInfoModel> jfResult = jfClient.jf("", JFServiceEnum.CTID_DOWN, JFChannelEnum.WX_DOWNCTID,ip);
            if (!JFResultEnum.SUCCESS.getCode().equals(jfResult.getCode())) {
                return CtidResult.error(jfResult.getMessage());
            }
            JFInfoModel jfData = jfResult.getData();
            String serverAccount = jfData.getUserServiceAccount();
            String specialCode = jfData.getSpecialCode();

            CtidResult<CtidMessage> downCtidResult = ctidPCManageService.downCtidOX10(authCode, cardName, cardNum,
                    cardStart, cardEnd, specialCode);

            jfClient.send(serverAccount, JFServiceEnum.CTID_DOWN, JFChannelEnum.WX_DOWNCTID, downCtidResult.getCode(), downCtidResult.getMessage(), UUIDGenerator.getSerialId(), specialCode, serverAccount);

            if (0 == downCtidResult.getCode()) {
                // 查询二项信息+openID是否存在，不存在则保存
                PersonInfo person = personDao.selectByOpenID(openID);
                CtidMessage data = downCtidResult.getData();
                String ctidInfo = data.getCtidInfo();
                Date date = new Date();

                String encodeIdCardInfo = tokenCipherService.encodeIdCardInfo(cardName, cardNum);
                String cardNumHash = CalculateHashUtils.calculateHash(cardNum.getBytes());

                if (null == person) {
                    person = new PersonInfo(UUIDGenerator.getSerialId(), Contents.INFO_SOURCE_WECHAT, openID, date,
                            cardNumHash, null, encodeIdCardInfo, null, Contents.IS_CTID_TRUE, null);
                    personDao.insertSelective(person);
                } else {
                    person.setName(encodeIdCardInfo);
                    person.setIdcard(cardNumHash);
//					person.setCtidInfo(ctidInfo);
                    person.setIsCtid(Contents.IS_CTID_TRUE);
                    personDao.updateByPrimaryKeySelective(person);
                }
                // 下载成功
                CtidInfos ctidInfos = new CtidInfos();
                String id = UUIDGenerator.getUUID();
                ctidInfos.setId(id);

                AuthCtidValidDateReturnBean ctidNumAndValidDate = ctidPCManageService.getCtidNumAndValidDate(ctidInfo, "");
                String ctidNum = ctidNumAndValidDate.getCtidNum();
                String ctidValidDate = ctidNumAndValidDate.getCtidValidDate();
                CtidInfos selectByCardNum = ctidInfosMapper.selectByCardNum(cardNumHash);
                if (selectByCardNum != null) {
                    selectByCardNum.setCardStartDate(cardStart);
                    selectByCardNum.setCardEndDate(cardEnd);
                    selectByCardNum.setCreateDate(date);
                    ctidInfosMapper.updateByPrimaryKeySelective(selectByCardNum);

                    Ctidinfo ci = infoMapper.selectByUserIdAndAppId(selectByCardNum.getId(), CtidConstants.WX_APPID);
                    if (ci != null) {
                        ci.setCtidDownTime(date);
                        ci.setCtidInfo(data.getCtidInfo());
                        ci.setCtidStatus(data.getCtidStatus());
                        ci.setCtidNum(ctidNum);
                        ci.setCtidValidDate(ctidValidDate);
                        infoMapper.updateByPrimaryKeySelective(ci);
                    } else {
                        ci = new Ctidinfo();
                        ci.setId(UUIDGenerator.getUUID());
                        ci.setAppId(CtidConstants.WX_APPID);
                        ci.setCtidDownTime(date);
                        ci.setCtidInfo(data.getCtidInfo());
                        ci.setUserId(selectByCardNum.getId());
                        ci.setCtidStatus(data.getCtidStatus());
                        ci.setCtidNum(ctidNum);
                        ci.setCtidValidDate(ctidValidDate);
                        infoMapper.insertSelective(ci);
                    }

                    id = selectByCardNum.getId();
                } else {
                    ctidInfos.setCardName(encodeIdCardInfo);
                    ctidInfos.setCardNum(cardNumHash);
                    ctidInfos.setCardStartDate(cardStart);
                    ctidInfos.setCardEndDate(cardEnd);
                    ctidInfos.setCreateDate(date);
                    // 入库
                    ctidInfosMapper.insertSelective(ctidInfos);
                    Ctidinfo ci = new Ctidinfo();
                    ci.setId(UUIDGenerator.getUUID());
                    ci.setAppId(CtidConstants.WX_APPID);
                    ci.setCtidDownTime(date);
                    ci.setCtidInfo(data.getCtidInfo());
                    ci.setUserId(id);
                    ci.setCtidStatus(data.getCtidStatus());
                    ci.setCtidNum(ctidNum);
                    ci.setCtidValidDate(ctidValidDate);
                    infoMapper.insertSelective(ci);
                }
            }
            return downCtidResult;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return CtidResult.error("网证下载失败！请重试！");
    }

    @Override
    public CtidResult<UserCardInfoBean> getCardInfo(String openID) {
        UserCardInfoBean userCardInfo = new UserCardInfoBean();
        try {

            PersonInfo person = personDao.selectByOpenID(openID);
            if (null != person) {
                String cardName = person.getName();
                String cardNum = person.getIdcard();
                String cardStart = "";
                String cardEnd = "";
                IdCardInfoBean idCardInfo = tokenCipherService.decodeIdCardInfo(cardName);
                CtidInfos ctidInfos = ctidInfosMapper.selectByCardNum(cardNum);
                if (null != ctidInfos) {
                    String decodeIdCardInfoStr = ctidInfos.getCardName();
                    idCardInfo = tokenCipherService.decodeIdCardInfo(decodeIdCardInfoStr);
                    cardStart = ctidInfos.getCardStartDate();
                    cardEnd = ctidInfos.getCardEndDate();
                }
                userCardInfo.setCardName(idCardInfo.getCardName());
                userCardInfo.setCardNum(idCardInfo.getCardNum());
                userCardInfo.setCardStart(cardStart);
                userCardInfo.setCardEnd(cardEnd);

                if (StringUtils.isNotBlank(userCardInfo.getCardName()) && StringUtils.isNotBlank(userCardInfo.getCardNum())) {
                    return CtidResult.ok(userCardInfo);
                }
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return CtidResult.error("身份信息获取失败！请重试！");
    }

    @Override
    public CtidResult<AuthCtidValidDateReturnBean> getCtidValidDate(String openID) {
        try {
            PersonInfo person = personDao.selectByOpenID(openID);
            if (null != person) {
                String cardNum = person.getIdcard();

                CtidInfos ctidInfos = ctidInfosMapper.selectByCardNum(cardNum);
                if (null != ctidInfos) {
                    Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidInfos.getId(), CtidConstants.WX_APPID);
                    if (null != ctidinfo) {
                        Date nowDate = new Date();
                        String ctidValidDate = ctidinfo.getCtidValidDate();
                        Date ctidDownTime = ctidinfo.getCtidDownTime();
                        String ctidNum = ctidinfo.getCtidNum();

                        String cardEndDateStr = ctidInfos.getCardEndDate();
                        Date validDate = new SimpleDateFormat("yyyyMMdd").parse(ctidValidDate);
                        int status = 0;
                        if (nowDate.after(validDate)) { // 说明网证数据已经过期
                            CtidResult<String> authCtidExpire = authCtidExpire(cardEndDateStr, ctidDownTime);
                            int code = authCtidExpire.getCode();
                            if (code == 1) {
                                return CtidResult.error("获取网证信息失败!");
                            } else if (code == ErrorCodeConstants.AUTH_USERCARD_EXPIRE_CODE) {
                                status = 2;
                            } else if (code == ErrorCodeConstants.AUTH_CTID_EXPIRE_CODE) {
                                status = 1;
                            }
                        }
                        AuthCtidValidDateReturnBean bean = new AuthCtidValidDateReturnBean(ctidNum, ctidValidDate, status);
                        return CtidResult.ok(bean);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return CtidResult.error("网证信息获取失败！请重试！");
    }

    private CtidResult<String> authCtidExpire(String cardEndDateStr, Date ctidDownTime) {
        try {
            Date nowDate = new Date();
            if (!"00000000".equals(cardEndDateStr)) {
                Date cardEndDate = new SimpleDateFormat("yyyyMMdd").parse(cardEndDateStr);
                if (nowDate.after(cardEndDate)) {
                    return CtidResult.error(ErrorCodeConstants.AUTH_USERCARD_EXPIRE_CODE,
                            ErrorCodeConstants.AUTH_USERCARD_EXPIRE_MESSAGE);
                }
            }
            Calendar ca = Calendar.getInstance();
            ca.setTime(nowDate);
            ca.add(Calendar.MONTH, 6);
            if (ca.getTime().after(ctidDownTime)) {
                return CtidResult.error(ErrorCodeConstants.AUTH_CTID_EXPIRE_CODE,
                        ErrorCodeConstants.AUTH_CTID_EXPIRE_MESSAGE);
            }
            return CtidResult.ok("success");
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return CtidResult.error("服务器内部错误");
    }

    @Override
    public CtidResult getQrCodeData(String openID) {
        try {
            PersonInfo person = personDao.selectByOpenID(openID);
            if (null != person) {
                String cardNum = person.getIdcard();
                CtidInfos ctidInfos = ctidInfosMapper.selectByCardNum(cardNum);
                if (null != ctidInfos) {
                    Ctidinfo ctidinfo = infoMapper.selectByUserIdAndAppId(ctidInfos.getId(), CtidConstants.WX_APPID);
                    if (null != ctidinfo) {
                        String ctid = ctidinfo.getCtidInfo();
                        CtidResult createCtidCode = ctidPCManageService.createCtidCode(ctid, "");
                        return createCtidCode;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return CtidResult.error("网证二维码数据获取失败！请重试！");
    }

    @Override
    public CtidResult<PersonInfo> checkUserInfo(String openID) {
        try {

            PersonInfo person = personDao.selectByOpenID(openID);
            if (null != person) {
                String cardName = person.getName();
                String cardNum = person.getIdcard();
                IdCardInfoBean idCardInfo = tokenCipherService.decodeIdCardInfo(cardName);
                CtidInfos ctidInfos = ctidInfosMapper.selectByCardNum(cardNum);
                if (null != ctidInfos) {
                    String decodeIdCardInfoStr = ctidInfos.getCardName();
                    idCardInfo = tokenCipherService.decodeIdCardInfo(decodeIdCardInfoStr);
                }
                person.setName(idCardInfo.getCardName());
                person.setIdcard(idCardInfo.getCardNum());
            }
            return CtidResult.ok(person);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return CtidResult.error("服务器内部错误");

    }

}
