package com.ahdms.ctidservice.db.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component; 


@Component
public class CTIDProperties {
 
	@Value("${mysqldb.datasource.url}")
	private String url;
	@Value("${mysqldb.datasource.username}")
	private String username;
	@Value("${mysqldb.datasource.password}")
	private String password;
	 
//	@Value("${sv.server.host}")
//	private String host;
//	@Value("${sv.server.port}")
//	private String port;
	@Value("${svtool.source.keyValue}")
	private String keyValue;
	@Value("${svtool.source.svIp}")
	private String svIp; 
	@Value("${svtool.source.keyIndex}")
	private String keyIndex;
	@Value("${svtool.source.svSubject}")
	private String svSubject;
	
	@Value("${svtool.source.svPort}")
	private int svPort;
	
	@Value("${project.version}")
	private String version;
	 
	@Value("${ftp.url}")
    private String ftpUrl;

    @Value("${ftp.username}")
    private String ftpUserName;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Value("${ftp.pic.location}")
    private String ftpLoc; 
    
    @Value("${ctid.pic.location}")
    private String location;
    
    @Value("${ctid.export.location}")
    private String exportLocation;
    
    @Value("${ftp.authrecordbackup.location}")
    private String authbackupLocation;
    
	public String getAuthbackupLocation() {
        return authbackupLocation;
    }
    public void setAuthbackupLocation(String authbackupLocation) {
        this.authbackupLocation = authbackupLocation;
    }
    public String getFtpUrl() {
        return ftpUrl;
    }
    public void setFtpUrl(String ftpUrl) {
        this.ftpUrl = ftpUrl;
    }
    public String getFtpUserName() {
        return ftpUserName;
    }
    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }
    public String getFtpPassword() {
        return ftpPassword;
    }
    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }
    public String getFtpLoc() {
        return ftpLoc;
    }
    public void setFtpLoc(String ftpLoc) {
        this.ftpLoc = ftpLoc;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSvSubject() {
		return svSubject;
	}
	public void setSvSubject(String svSubject) {
		this.svSubject = svSubject;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public String getSvIp() {
		return svIp;
	}
	public void setSvIp(String svIp) {
		this.svIp = svIp;
	}
	public String getKeyIndex() {
		return keyIndex;
	}
	public void setKeyIndex(String keyIndex) {
		this.keyIndex = keyIndex;
	}
    public String getExportLocation() {
        return exportLocation;
    }
    public void setExportLocation(String exportLocation) {
        this.exportLocation = exportLocation;
    }
	public int getSvPort() {
		return svPort;
	}
	public void setSvPort(int svPort) {
		this.svPort = svPort;
	}
   
	 
}
