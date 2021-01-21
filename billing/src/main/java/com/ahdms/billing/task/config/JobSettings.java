package com.ahdms.billing.task.config;

public class JobSettings {
	
	 private static final long serialVersionUID = -6532210090618686688L;
	    
	    private String jobName;
	    
	    private String jobType;
	    
	    private String jobClass;
	    
	    private String cron;
	    
	    private int shardingTotalCount;
	    
	    private String jobParameter;
	    
	    private int monitorPort = -1;
	    
	    private String description;

		public String getJobName() {
			return jobName;
		}

		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

		public String getJobType() {
			return jobType;
		}

		public void setJobType(String jobType) {
			this.jobType = jobType;
		}

		public String getJobClass() {
			return jobClass;
		}

		public void setJobClass(String jobClass) {
			this.jobClass = jobClass;
		}

		public String getCron() {
			return cron;
		}

		public void setCron(String cron) {
			this.cron = cron;
		}

		public int getShardingTotalCount() {
			return shardingTotalCount;
		}

		public void setShardingTotalCount(int shardingTotalCount) {
			this.shardingTotalCount = shardingTotalCount;
		}

		public String getJobParameter() {
			return jobParameter;
		}

		public void setJobParameter(String jobParameter) {
			this.jobParameter = jobParameter;
		}

		public int getMonitorPort() {
			return monitorPort;
		}

		public void setMonitorPort(int monitorPort) {
			this.monitorPort = monitorPort;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	    
}
