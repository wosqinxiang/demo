package com.ahdms.billing.task.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ahdms.billing.task.FindExpiredJob;
import com.ahdms.billing.task.ReportUserCountJob;
import com.ahdms.billing.task.SyncCountJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.config.LiteJobConfigurationGsonFactory;
import com.dangdang.ddframe.job.lite.internal.storage.JobNodePath;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;

@Component
public class StartJobConfig {

	@Autowired
	private CoordinatorRegistryCenter zkRegCenter;
	
	@Autowired
	private FindExpiredJob findExpiredJob;
	
	@Autowired
	private SyncCountJob syncCountJob;
	
	@Autowired
	private ReportUserCountJob reportCountJob;
	
	@Value("${service.find.expired.time}")
	private String findExpiredJobCorn;
	
	@Value("${service.sync.count.time}")
	private String syncCountJobCorn;
	
	@Value("${service.report.count.time}")
	private String reportCountJobCorn;

	
	@PostConstruct
	public void init() {
		addJob("findExpiredJob", findExpiredJobCorn, findExpiredJob.getClass(), "", findExpiredJob);
		
		addJob("syncCountJob", syncCountJobCorn, syncCountJob.getClass(), "", syncCountJob);
		
		addJob("reportCountJob", reportCountJobCorn, ReportUserCountJob.class, "", reportCountJob);
	}

	public void addJob(String jobName, String cron, Class jobClass, String jobParametr, SimpleJob job) {
		JobSettings jobSettings = getJobSettings(jobName);
		if(null != jobSettings){
			jobSettings.setCron(cron);
			updateJobSettings(jobSettings);
			// 定义作业核心配置
			JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 1)
					.jobParameter(jobParametr).build();
			// 定义SIMPLE类型配置
			SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig,
					jobClass.getCanonicalName());
			// 定义Lite作业根配置
			LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
			new SpringJobScheduler(job, zkRegCenter, simpleJobRootConfig).init();
		}else{
			// 定义作业核心配置
			JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 1)
					.jobParameter(jobParametr).build();
			// 定义SIMPLE类型配置
			SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig,
					jobClass.getCanonicalName());
			// 定义Lite作业根配置
			LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
			new SpringJobScheduler(job, zkRegCenter, simpleJobRootConfig).init();
		}
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
