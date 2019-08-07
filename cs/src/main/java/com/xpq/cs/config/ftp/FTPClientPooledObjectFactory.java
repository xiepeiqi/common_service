package com.xpq.cs.config.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ftp客户端连接池工厂
 * @author xiepeiqi @date 2019年8月6日
 */
@Component
public class FTPClientPooledObjectFactory implements PooledObjectFactory<FTPClient> {
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FTPConfig ftpConfig;

    public FTPClientPooledObjectFactory(FTPConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpConfig.getHost(), ftpConfig.getPort());

            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.warn("FTP服务连接失败 : " + reply);
                return null;
            }

            boolean loginResult = ftpClient.login(ftpConfig.getUserName(),ftpConfig.getPassword());
            if (!loginResult) {
                logger.info("FTP服务登录失败");
            }

            ftpClient.setBufferSize(1024);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding(ftpConfig.getEncoding());
            ftpClient.enterLocalPassiveMode();
            return new DefaultPooledObject<>(ftpClient);
        }catch (Exception e) {
            logger.error(e.toString());

            if (ftpClient.isAvailable()) {
                ftpClient.disconnect();
            }
            throw e;
        }
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        FTPClient ftpClient = getObject(p);
        if (null != ftpClient && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            }catch (IOException e) {
                logger.error("登出FTP服务失败: " ,e);
            }finally {
                ftpClient.disconnect();
            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        FTPClient ftpClient = getObject(p);
        if (null == ftpClient || !ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.sendNoOp();
        } catch (Exception e) {
            logger.error("验证 FTPClient 失败: ", e);
        }
        return false;
    }

    @Override
    public void activateObject(PooledObject<FTPClient> p) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<FTPClient> p) throws Exception {
    }

    private FTPClient getObject(PooledObject<FTPClient> p) {
        if (p == null || p.getObject() == null) {
            return null;
        }
        return p.getObject();
    }
}
