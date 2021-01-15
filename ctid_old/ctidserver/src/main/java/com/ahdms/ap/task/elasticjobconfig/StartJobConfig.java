package com.ahdms.ap.task.elasticjobconfig;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.ahdms.ap.task.ArchiveAuthRecordJob;
import com.ahdms.ap.task.UpdateTimesJob;
import com.ahdms.ap.task.WechatPushJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.config.LiteJobConfigurationGsonFactory;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;

@Component
@Configuration
public class StartJobConfig {

	@Autowired
	private CoordinatorRegistryCenter zkRegCenter;

//	@Value("${ctid.backup.jobtime}")
//	private String backupJobTime;
//	@Value("${ctid.export.jobtime}")
//	private String exportJobTime;
//	@Value("${ctid.job.start}")
//	private boolean jobStart;
	@Value("${archive.log.period}")
	private int period;
	@Value("${archive.log.cron}")
	private String archiveCron;
	@Value("${update.server.task.time}")
	private String aliarmJobCorn;
	@Value("${redis.device.count}")
	private String deviceCount;
	@Value("${wechat.push.period.time}")
	private String wpushJobCorn;
	
//	@Value("${company.report.corn}")
//    private String companyReportCorn;
//	@Value("${company.startwork.corn}")
//    private String companyStartWorkCorn;
//	@Value("${area.report.corn}")
//    private String areaReportCorn;
	
	@Autowired
	UpdateTimesJob updateTimesJob;
	@Autowired
	ArchiveAuthRecordJob archiveAuthRecordJob;
	@Autowired
	WechatPushJob wechatPushJob;
//	@Autowired
//	CompanyReportJob companyReportJob;
//	@Autowired
//	CompanyStartWorkJob companyStartWorkJob;
//	@Autowired
//	AreaReportJob areaReportJob;
	
	@PostConstruct
	public void init() {
		addJob("jobArchiveAuthRecord", archiveCron, ArchiveAuthRecordJob.class, "" + period, archiveAuthRecordJob);
		addJob("jobUpdateTimes", aliarmJobCorn, UpdateTimesJob.class, deviceCount, updateTimesJob);
		
		addJob("wechatpushjob", wpushJobCorn, WechatPushJob.class, "", wechatPushJob);  
		
//		String companyReportCorn = "0 30 0 * * ?";
//		addJob("companyReportJob", companyReportCorn, CompanyReportJob.class, "", companyReportJob);
//		String companyStartWorkCorn ="0 1 0 * * ?";
//		addJob("companyStartWorkJob", companyStartWorkCorn, CompanyStartWorkJob.class, "", companyStartWorkJob);
//		String areaReportCorn ="0 5 0 * * ?";
//		addJob("areaReportJob", areaReportCorn, AreaReportJob.class, "", areaReportJob);
	}

	public void addJob(String jobName, String cron, Class jobClass, String jobParametr, SimpleJob job) {
		// 定义作业核心配置
		JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 1)
				.jobParameter(jobParametr).build();
		// 定义SIMPLE类型配置
		SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig,
				jobClass.getCanonicalName());
		// 定义Lite作业根配置
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
		new SpringJobScheduler(job, zkRegCenter, simpleJobRootConfig).init();
		// new JobScheduler(zkRegCenter, simpleJobRootConfig).init();
	}

	public void updateJob(String jobName, String cron, String jobParametr) {
		JobSettings jobSettings = getJobSettings(jobName);
		if (null != jobSettings) {
			jobSettings.setCron(cron);
			updateJobSettings(jobSettings);
		}
	}

	public void deleteJob(String jobName) {
		zkRegCenter.remove("/" + jobName);
	}

	public JobSettings getJobSettings(final String jobName) {
		JobSettings result = new JobSettings();
		JobNodePath jobNodePath = new JobNodePath(jobName);
		LiteJobConfiguration liteJobConfig = LiteJobConfigurationGsonFactory
				.fromJson(zkRegCenter.get(jobNodePath.getConfigNodePath()));
		if (liteJobConfig == null) {
			return null;
		}
		buildSimpleJobSettings(jobName, result, liteJobConfig);
		return result;
	}

	private void buildSimpleJobSettings(final String jobName, final JobSettings result,
			final LiteJobConfiguration liteJobConfig) {
		result.setJobName(jobName);
		result.setJobType(liteJobConfig.getTypeConfig().getJobType().name());
		result.setJobClass(liteJobConfig.getTypeConfig().getJobClass());
		result.setShardingTotalCount(liteJobConfig.getTypeConfig().getCoreConfig().getShardingTotalCount());
		result.setCron(liteJobConfig.getTypeConfig().getCoreConfig().getCron());
		// result.setShardingItemParameters(liteJobConfig.getTypeConfig().getCoreConfig().getShardingItemParameters());
		result.setJobParameter(liteJobConfig.getTypeConfig().getCoreConfig().getJobParameter());
		// result.setMonitorExecution(liteJobConfig.isMonitorExecution());
		// result.setMaxTimeDiffSeconds(liteJobConfig.getMaxTimeDiffSeconds());
		result.setMonitorPort(liteJobConfig.getMonitorPort());
		// result.setFailover(liteJobConfig.getTypeConfig().getCoreConfig().isFailover());
		// result.setMisfire(liteJobConfig.getTypeConfig().getCoreConfig().isMisfire());
		// result.setJobShardingStrategyClass(liteJobConfig.getJobShardingStrategyClass());
		result.setDescription(liteJobConfig.getTypeConfig().getCoreConfig().getDescription());
		// result.setReconcileIntervalMinutes(liteJobConfig.getReconcileIntervalMinutes());
	}

	public void updateJobSettings(final JobSettings jobSettings) {
		JobNodePath jobNodePath = new JobNodePath(jobSettings.getJobName());
		zkRegCenter.update(jobNodePath.getConfigNodePath(),
				LiteJobConfigurationGsonFactory.toJsonForObject(jobSettings));
	}

	// @Bean
	public LiteJobConfiguration createJobConfiguration(String jobName, String cron, String jobClass) {
		// 定义作业核心配置
		JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 1)
				.jobParameter("abcdefgdasda").build();
		// 定义SIMPLE类型配置
		SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, jobClass);
		// 定义Lite作业根配置
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();

		return simpleJobRootConfig;
	}

}
