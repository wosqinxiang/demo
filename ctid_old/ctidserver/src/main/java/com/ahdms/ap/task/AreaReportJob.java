///**
// * Created on 2019年9月30日 by liuyipin
// */
//package com.ahdms.ap.task;
//
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.ahdms.ap.dao.AreaReportMapper;
//import com.ahdms.ap.dao.CompanyMapper;
//import com.ahdms.ap.dao.CountRecordMapper;
//import com.ahdms.ap.model.AreaReport;
//import com.ahdms.ap.model.CompanyReport;
//import com.ahdms.ap.utils.DateUtils;
//import com.ahdms.ap.utils.UUIDGenerator;
//import com.ahdms.ap.vo.AreaReportVO;
//import com.ahdms.ap.vo.CountSearchVO;
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//
///**
// * @Title
// * @Description
// * @Copyright
// *            <p>
// *            Copyright (c) 2015
// *            </p>
// * @Company
// *          <p>
// *          迪曼森信息科技有限公司 Co., Ltd.
// *          </p>
// * @author liuyipin
// * @version 1.0
// * @修改记录
// * @修改序号，修改日期，修改人，修改内容
// */
//
//@Component
//public class AreaReportJob implements SimpleJob {
//
//    Logger logger = LoggerFactory.getLogger(AreaReportJob.class);
//    @Autowired
//    CountRecordMapper countRecordMapper;
//    @Autowired
//    CompanyMapper companyMapper;
//    @Autowired
//    AreaReportMapper areaReportMapper;
//    List<CompanyReport> comReportList = new LinkedList<CompanyReport>();
//
//    @Override
//    public void execute(ShardingContext context) {
//        logger.info("区域统计记录" + DateUtils.getNowTime());
//        try {
//            long t1 = System.currentTimeMillis();
//            // 获得企业信息集合
//            // 遍历企业查询数据
//            // 导入数据
//            Date date = DateUtils.getNowDate();
//            String yesterdayStr = DateUtils.getYesterday(date);
//            Date yesterday = DateUtils.parse(yesterdayStr,DateUtils.DATE_SMALL_STR);
//            int orderNum = 0;
//            CountSearchVO cs = new CountSearchVO();
//            List<AreaReportVO> areaList = companyMapper.queryAreaCompanyCount(0);
//            for (AreaReportVO c : areaList) {
//                cs.setCompanytype("0");
//                cs.setCompanyArea(c.getCompanyarea());
//                cs.setCountDate(yesterday);
//                // 区域企业复工总数
//                int countWorkAll = countRecordMapper.countWorkAll(cs);
//                    orderNum++;
//                    AreaReport cr = new AreaReport();
//                    // 区域当天
//                    int countWorkToday = countRecordMapper.countWorkToday(cs);
//                    cs.setCountDate(null);
//                    // 统计企业复工人员来源 来自武汉市的人数
//                    int countFromWUHAN = countRecordMapper.countFromWUHAI(cs);
//    
//                    // 统计企业复工人员来源 来自湖北省的人数
//                    int countFromHUBEI = countRecordMapper.countFromHUBEI(cs);
//    
//                    // 统计企业复工人员来源 来自内蒙省外的人数
//                    int countFromNotNEIMENG = countRecordMapper.countFromNotNEIMENG(cs);
//    
//                    // 统计企业复工人员来源 来自内蒙省内的人数
//                    int countFromNEIMENG = countRecordMapper.countFromNEIMENG(cs);
//    
//                    // 统计企业复工人员来源 来自包头市外的人数
//                    int countFromNotBAOTOU = countRecordMapper.countFromNotBAOTOU(cs);
//    
//                    // 统计企业复工人员来源 来自包头市内的人数
//                    int countFromBAOTOU = countRecordMapper.countFromBAOTOU(cs);
//
//                    cr.setId(UUIDGenerator.getUUID());
//                    cr.setCountdate(yesterday);
//                    cr.setCompanytype(c.getCompanytype());
//                    cr.setCompanyarea(c.getCompanyarea());
//                    cr.setCountallcompany(c.getCountallareacompany());
//                    cr.setCountstartwork(c.getCountareastartwork());
//                    cr.setCountunwork(c.getCountareastartwork());
//                    //隔离期人数
//                    cr.setInsulatecount(c.getCountareainsulate());
//                    //计划复工数
//                    cr.setPlanworkcount(c.getCountareaplanwork());
////                    cr.setCountall(c.getCountareaall());
//                    //复工人员总数-目前在岗职工数
//                    cr.setTodaycount(countWorkToday);
//                    cr.setCountall(countWorkAll);
//                    cr.setIncity(countFromBAOTOU);
//                    cr.setOutsidecity(countFromNotBAOTOU);
//                    cr.setInprovience(countFromNEIMENG);
//                    cr.setOutsideprovience(countFromNotNEIMENG);
//                    cr.setHubeicount(countFromHUBEI);
//                    cr.setWuhancount(countFromWUHAN);
//                    cr.setRemark("");
//                    cr.setOrdernum(orderNum);//序号
//                    areaReportMapper.insertSelective(cr);
////                    comReportList.add(cr);
//            }
//            // 查询待归档日志并归档
//             long t2 = System.currentTimeMillis();
//             logger.info("总共："+orderNum+"条记录，统计"+yesterdayStr+"的数据，耗时："+(t2-t1)+"ms");
//        } catch (Exception e) {
//            logger.error("统计失败 - {}", e.getMessage());
//        }
//    }
//}
