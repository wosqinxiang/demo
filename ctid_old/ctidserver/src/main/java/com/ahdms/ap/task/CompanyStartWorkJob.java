///**
// * Created on 2019年9月30日 by liuyipin
// */
//package com.ahdms.ap.task;
//
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.PersistJobDataAfterExecution;
//import org.quartz.SchedulerException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//
//import com.ahdms.ap.dao.AuthRecordArchiveDao;
//import com.ahdms.ap.dao.AuthRecordDao;
//import com.ahdms.ap.dao.CompanyMapper;
//import com.ahdms.ap.dao.CompanyReportMapper;
//import com.ahdms.ap.dao.CountRecordMapper;
//import com.ahdms.ap.model.AuthRecord;
//import com.ahdms.ap.model.Company;
//import com.ahdms.ap.model.CompanyReport;
//import com.ahdms.ap.service.CompanyService;
//import com.ahdms.ap.utils.DateUtils;
//import com.ahdms.ap.utils.UUIDGenerator;
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
//public class CompanyStartWorkJob implements SimpleJob {
//
//    Logger logger = LoggerFactory.getLogger(CompanyStartWorkJob.class);
//    @Autowired
//    CompanyMapper companyMapper;
//    List<CompanyReport> comReportList = new LinkedList<CompanyReport>();
//
//    @Override
//    public void execute(ShardingContext context) {
//        logger.info("企业开工状态修改定时任务" + DateUtils.getNowTime());
//        try {
//             long t1 = System.currentTimeMillis();
//             companyMapper.updateCompanyStartWork();
//             long t2 = System.currentTimeMillis();
//             logger.info("耗时："+(t2-t1)+"ms");
//        } catch (Exception e) {
//            logger.error("统计失败 - {}", e.getMessage());
//        }
//    }
//}
