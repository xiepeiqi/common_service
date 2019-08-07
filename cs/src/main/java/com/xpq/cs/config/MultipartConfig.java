package com.xpq.cs.config;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

/**
 * 文件上传配置
 * @author xiepeiqi @date 2019年8月6日
 */
@Configuration
public class MultipartConfig {
	
	/**
	 * 文件上传临时路径
	 * @return
	 * @author xiepeiqi @date 2019年8月6日
	 */
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir") + "/tmp/data";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        /**
         * 设置location是为了解决临时文件夹引起的文件传输失败问题
         */
        factory.setLocation(location);
        factory.setMaxFileSize(DataSize.ofMegabytes(250));//250M
        factory.setMaxRequestSize(DataSize.ofMegabytes(260));//260M
        return factory.createMultipartConfig();
    }

}
