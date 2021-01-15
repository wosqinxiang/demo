package com.ahdms.ctidservice.common;

import com.ahdms.ctidservice.bean.CtidResponseVo;
import com.ahdms.ctidservice.db.dao.CtidInfosMapper;
import com.ahdms.ctidservice.db.model.CtidInfos;
import com.ahdms.ctidservice.db.model.CtidPidInfos;
import com.ahdms.ctidservice.service.CtidPidInfoService;
import com.ahdms.ctidservice.util.CalculateHashUtils;
import com.ahdms.ctidservice.util.IdGenerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author qinxiang
 * @date 2020-09-03 13:52
 */
@Component
public class CtidRspUtils {

    private final static String TOKEN_KEY = "PID_TOKEN";

    @Autowired
    private IdGenerUtils idGenerUtils;

    @Autowired
    private RedisOpsClient redisOpsClient;

    @Autowired
    private CtidInfosMapper ctidInfosMapper;

    @Autowired
    private CtidPidInfoService ctidPidInfoService;

    /**
     * 根据身份证号码明文 返回ctid响应
     * @param idCard 身份证号码明文
     * @param appCode 身份认证平台为APP签发的APPCODE
     * @param ctidInfo 网证数据
     * @param bsn 业务流水号
     * @return
     */
    public CtidResponseVo getCtidRsp(String idCard,String appCode,String ctidInfo,String bsn,String ctidIndex){
        String idCardHash = CalculateHashUtils.calculateHash(idCard.getBytes());

        return getCtidRspIdHash(idCardHash,appCode,ctidInfo,bsn,ctidIndex);
    }

    /**
     * 根据身份证号码Hash 返回ctid响应
     * @param idCardHash 身份证号码HASH
     * @param appCode 身份认证平台为APP签发的APPCODE
     * @param ctidInfo 网证数据
     * @param bsn 业务流水号
     * @return
     */
    public CtidResponseVo getCtidRspIdHash(String idCardHash,String appCode,String ctidInfo,String bsn,String ctidIndex){
        CtidResponseVo ctidRsp = new CtidResponseVo();
        ctidRsp.setBsn(bsn);
        ctidRsp.setCtidInfo(ctidInfo);
        ctidRsp.setToken(bsn);
        ctidRsp.setCtidIndex(ctidIndex);
        //计算BID
        String bid = idGenerUtils.encryptBId(idCardHash, appCode);
        //保存bid到redis
        redisOpsClient.set(bsn,bid,CTIDConstans.TOKEN_EXPIRE_SECONDS);

        //获取BID'即 sdkBid
        String sdkBid = idGenerUtils.encrypt2BId(bid);
        ctidRsp.setSdkBid(sdkBid);
        return ctidRsp;
    }

    /**
     * 根据PID(公安一所CTID平台认证返回) 返回ctid响应
     * @param pid 公安一所CTID平台认证返回
     * @param appCode 身份认证平台为APP签发的APPCODE
     * @param ctidInfo 网证数据
     * @param bsn 业务流水号
     * @return
     */
    public CtidResponseVo getCtidRspPid(String ctidInfosId,String pid,String appCode,String ctidInfo,String bsn,String speciallineCode){
        CtidPidInfos ctidPidInfos = ctidPidInfoService.selectByCtidInfosIdAndPid(ctidInfosId,pid);

        if(ctidPidInfos != null){
            CtidInfos ctidInfos = ctidInfosMapper.selectByPrimaryKey(ctidPidInfos.getCtidInfosId());
            return getCtidRspIdHash(ctidInfos.getCardNum(),appCode,ctidInfo,bsn,ctidInfos.getId());
        } else{
            //网证开通返回PID，
            String ctidIndex = redisOpsClient.get("BSN_"+bsn);
            if(StringUtils.isNotBlank(ctidIndex)){
                CtidInfos ctid = ctidInfosMapper.selectByPrimaryKey(ctidIndex);
                if(ctid != null){
                    ctidPidInfoService.insertOrUpdate(ctidIndex,pid,speciallineCode);
                    return getCtidRspIdHash(ctid.getCardNum(),appCode,ctidInfo,bsn,ctid.getId());
                }
            }
        }
        return new CtidResponseVo();
    }

    /**
     * 下载0x13 当未开通时返回 bsn
     * @param ctidIndex
     * @param bsn
     * @return
     */
    public CtidResponseVo getCtidRspBsn(String ctidIndex,String bsn){
        CtidResponseVo ctidResponseVo = new CtidResponseVo();
        ctidResponseVo.setBsn(bsn);
        ctidResponseVo.setCtidIndex(ctidIndex);

        redisOpsClient.set("BSN_"+bsn,ctidIndex,3000);

        return ctidResponseVo;
    }


}
