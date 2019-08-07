package com.xpq.cs.config.ftp;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ftp配置
 * @date 2018年11月21日 @time 下午3:06:39
 * @author xiepeiqi
 */
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FTPConfig {

	private String host;
	private Integer port;
	private String userName;
	private String password;
	private Integer initailSize;
	private Integer maxSize;
	private String encoding;
	
	@Override
	public String toString() {
		return "FTPConfig [host=" + host + ", port=" + port + ", userName=" + userName + ", password=" + password
				+ ", initailSize=" + initailSize + ", maxSize=" + maxSize + ", encoding=" + encoding + "]";
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getInitailSize() {
		return initailSize;
	}

	public void setInitailSize(Integer initailSize) {
		this.initailSize = initailSize;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	

}
