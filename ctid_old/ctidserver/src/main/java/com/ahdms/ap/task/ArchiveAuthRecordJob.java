/**
 * Created on 2019年9月30日 by liuyipin
 */
package com.ahdms.ap.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahdms.ap.dao.AuthRecordArchiveDao;
import com.ahdms.ap.dao.AuthRecordDao;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ap.utils.DateUtils;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

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
public class ArchiveAuthRecordJob implements SimpleJob{


	Logger logger = LoggerFactory.getLogger(ArchiveAuthRecordJob.class);
	@Autowired
	AuthRecordDao authRecordDao;
	@Autowired
	AuthRecordArchiveDao authRecordArchiveDao;
	
	@Override
	public void execute(ShardingContext context)  { 
		logger.info("归档认证记录" + DateUtils.getNowTime());
		try {
			long t1 = System.currentTimeMillis();
			// 查询待归档日志并归档
			String period = context.getJobParameter();
			LocalDate time = LocalDate.now().minusMonths(Integer.parseInt(period));
			List<AuthRecord> authRecords = authRecordDao.selectArchive(time); 
			if(authRecords.size() != 0){
				Map<Integer,List<AuthRecord>> map = batchList(authRecords, 500);
				for (int i = 1; i<map.size()+1; i++) {
					List<AuthRecord> ars = map.get(i); 
					authRecordArchiveDao.insertArchive(ars);
				} 
			}
			long t2 = System.currentTimeMillis();
			logger.info("总共："+authRecords.size()+"条记录，备份耗时："+(t2-t1)+"ms");
			//删除已归档日志
			authRecordDao.delArchive(time);
			long t3 = System.currentTimeMillis();
			logger.info("总共："+authRecords.size()+"条记录，备份并删除已归档日志耗时："+(t3-t1)+"ms");
		}  catch (Exception e) {
			logger.error("备份SQL失败 - {}", e.getMessage());
		}
	}
	
	public Map<Integer,List<AuthRecord>> batchList(List<AuthRecord> list, int batchSize){
        Map<Integer,List<AuthRecord>> itemMap = new HashMap<>();
        itemMap.put(1, new ArrayList<AuthRecord>());
        for(AuthRecord e : list){
            List<AuthRecord> batchList= itemMap.get(itemMap.size());
            if(batchList.size() == batchSize){//当list满足批次数量，新建一个list存放后面的数据
                batchList = new ArrayList<AuthRecord>();
                itemMap.put(itemMap.size()+1, batchList);
            }
            batchList.add(e);
        }
        return itemMap;
    } 
}


