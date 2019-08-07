package com.xpq.cs.config.ftp;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * ftp操作文件业务
 * @date 2018年11月21日 @time 下午3:09:02
 * @author xiepeiqi
 */
@Component
public class FTPService {
	
	private static final Logger logger = LoggerFactory.getLogger(FTPService.class);

	private static ObjectPool<FTPClient> ftpClientPool;

	@Autowired
	private FTPConfig ftpConfig;

	@PostConstruct
	private synchronized void createPoolConfig() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMaxTotal(ftpConfig.getMaxSize());
		poolConfig.setMinIdle(ftpConfig.getInitailSize());
		ftpClientPool = new GenericObjectPool<>(new FTPClientPooledObjectFactory(ftpConfig), poolConfig);
	}

	/**
	 * 目录或文件路径转码
	 * @param path
	 * @return
	 * @throws UnsupportedEncodingException
	 * @date 2018年11月21日 @time 上午9:20:41
	 * @author xiepeiqi
	 */
	private static String encodingPath(String path) throws UnsupportedEncodingException {
		// FTP协议里面，规定文件名编码为iso-8859-1，所以要将中文目录名或中文文件名转码
		return new String(path.replaceAll("//", "/").getBytes("GBK"), "ISO-8859-1");
	}
	
	/**
	 * 获取根目录的文件夹文件数
	 * @return
	 * @author xiepeiqi @date 2019年4月4日
	 */
	public Integer getRootSize() {
		FTPClient ftpClient = null;
		try {

			ftpClient = ftpClientPool.borrowObject();
			int length = ftpClient.listNames().length;
			return length ;
		}catch(FTPConnectionClosedException e){
			logger.error("连接不上FTP服务器，可能FTP服务器未开启或者连接配置错误,FTP服务数据上传失败: [{}]",e );
		}catch (Exception e) {
			logger.error("FTP服务获取根目录的文件夹文件数失败:[{}]" , e);
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将流数据上传到FTP服务并保存为文件
	 * @param file
	 * @param pathName
	 * @param fileName
	 * @return
	 * @date 2018年11月20日 @time 下午5:24:32
	 * @author xiepeiqi
	 */
	public Boolean uploadFile(InputStream file, String pathName, String fileName) {
		if (file==null) {
			return false;
		}

		FTPClient ftpClient = null;
		try {
			ftpClient = ftpClientPool.borrowObject();
			ftpClient.makeDirectory(encodingPath(pathName));
			ftpClient.changeWorkingDirectory(encodingPath(pathName));

			boolean storeFile = ftpClient.storeFile(encodingPath(fileName), file);
			return storeFile;

		}catch(FTPConnectionClosedException e){
			logger.error("FTP服务将流数据上传到FTP服务并保存为文件上传失败, pathName:[{}], fileName:[{}],error: [{}]",pathName,fileName , e);
		}catch (Exception e) {
			logger.error("FTP服务将流数据上传到FTP服务并保存为文件上传失败, pathName:[{}], fileName:[{}],error: [{}]",pathName,fileName , e);
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
				if (file!=null ) {
					file.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 将流数据批量上传到FTP服务并保存为文件
	 * （这个还没有优化好，建议不用，否则会出问题的迟早会出问题）
	 * @param files
	 * @param pathName
	 * @param fileNames
	 * @return
	 * @date 2018年11月20日 @time 下午5:24:45
	 * @author xiepeiqi
	 */
	public Boolean uploadFiles(InputStream[] files, String pathName, String[] fileNames) {
		FTPClient ftpClient = null;
		try {
			pathName = encodingPath(pathName);

			ftpClient = ftpClientPool.borrowObject();
			ftpClient.makeDirectory(pathName);
			ftpClient.changeWorkingDirectory(pathName);

			Integer i = 0;
			for (InputStream file : files) {
				String fileName = fileNames[i++];
				fileName = encodingPath(fileName);

				logger.info("FTP文件上传: " +new String(pathName.getBytes("iso-8859-1"),"gbk")  + "/" + new String(fileName.getBytes("iso-8859-1"),"gbk"));
				boolean storeFile = ftpClient.storeFile(fileName, file);
				if (file!=null ) {
					file.close();
				}
				if (storeFile) {
					return false;
				}
			}

		} catch (Exception e) {
			logger.error("FTP服务将流数据批量上传到FTP服务并保存为文件上传失败: [{}]" , e);
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * 获取FTP服务指定文件路径下所有文件夹
	 * @param pathName
	 * @return
	 * @author xiepeiqi @date 2019年4月25日
	 */
	public FTPFile[] getFTPDirList(String pathName) {
		FTPClient ftpClient = null;
		FTPFile[] listDirectories =null;
		try {
			ftpClient = ftpClientPool.borrowObject();
			listDirectories = ftpClient.listDirectories(pathName);
			return listDirectories;
		} catch (Exception e) {
			logger.error("获取FTP服务指定文件路径下所有文件夹失败,pathName:[{}] ,error: [{}]",pathName , e);
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取FTP服务指定文件路径下所有文件列表
	 * @param pathName FTP文件路径
	 * @return 文件列表
	 * @date 2018年11月20日 @time 下午5:31:34
	 * @author xiepeiqi
	 */
	public FTPFile[] getFTPFileList(String pathName) {
		FTPClient ftpClient = null;
		FTPFile[] listFiles =null;
		try {
			ftpClient = ftpClientPool.borrowObject();
			ftpClient.changeWorkingDirectory(encodingPath(pathName));
			listFiles = ftpClient.listFiles();
			return listFiles;
		} catch (Exception e) {
			logger.error("获取FTP服务指定文件路径下所有文件列表失败,pathName:[{}], error:[{}] ",pathName , e);
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取FTP服务指定文件
	 * @param pathName FTP文件路径
	 * @param fileName 指定文件名
	 * @return
	 * @date 2018年11月20日 @time 下午5:30:19
	 * @author xiepeiqi
	 */
	public FTPFile getFTPFlie(String pathName, String fileName) {
		FTPFile[] files = getFTPFileList(pathName);
		if (null != files) {
			for (FTPFile file : files) {
				if (file.getName().equals(fileName)) {
					return file;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取FTP服务指定文件的输入流
	 * @param pathName
	 * @param fileName
	 * @return
	 * @author xiepeiqi @date 2019年4月26日
	 */
	public byte[] getFTPFlieBytes(String pathName, String fileName) {
		FTPClient ftpClient = null;
		FTPFile[] files =null;
		try {
			ftpClient = ftpClientPool.borrowObject();
			ftpClient.changeWorkingDirectory(encodingPath(pathName));
			files = ftpClient.listFiles();
			if (null != files) {
				for (FTPFile file : files) {
					if (file.getName().equals(fileName)) {
						byte[] byteArray = IOUtils.toByteArray(ftpClient.retrieveFileStream(file.getName()));
			            ftpClient.completePendingCommand();//这行代码不加后面的运行全部失败
						return byteArray;
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取FTP服务指定文件的输入流失败,pathName:[{}], fileName:[{}],error: [{}]",pathName,fileName , e);
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将FTP的json文本文件转化成String
	 * @param ftpFile FTP文件
	 * @return
	 * @date 2018年11月20日 @time 下午5:29:59
	 * @author xiepeiqi
	 */
	public String textDataToString(FTPFile ftpFile) {
		if (null == ftpFile) {
			return null;
		}
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpClientPool.borrowObject();
			InputStream inputStream = ftpClient.retrieveFileStream(ftpFile.getName());
			byte[] byteArray = IOUtils.toByteArray(inputStream);
			if (inputStream!=null ) {
				inputStream.close();
			}
			ftpClient.completePendingCommand();// 这行代码不加后面的运行全部失败
			String string = new String(byteArray);
			return string;
		} catch (Exception e) {
			logger.error("将FTP的json文本文件转化成String失败: [{}]" , e);
			return null;
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 删除FTP文件
	 * @param filePath 欲删除的FTP文件完整路径
	 * @return
	 * @date 2018年11月20日 @time 下午5:28:39
	 * @author xiepeiqi
	 */
	public Boolean deleteFile(String filePath) {

		if (null == filePath) {
			return false;
		}
		FTPClient ftpClient = null;
		try {
			filePath = encodingPath(filePath);
			ftpClient = ftpClientPool.borrowObject();

			boolean deleteFile = ftpClient.deleteFile(filePath);
			return deleteFile;
		} catch (Exception e) {
			logger.error("删除FTP文件失败: filePath:{}, error: [{}]" ,filePath, e);
			return false;
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 批量删除FTP文件
	 * @param filePaths  欲删除的FTP文件完整路径的集合
	 * @return
	 * @date 2018年11月20日 @time 下午5:33:04
	 * @author xiepeiqi
	 */
	public Boolean deleteFiles(List<String> filePaths) {
		if (null == filePaths) {
			return false;
		}
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpClientPool.borrowObject();
			for (String filePath : filePaths) {
				filePath = encodingPath(filePath);
				ftpClient.deleteFile(filePath);
			}
			return true;
		} catch (Exception e) {
			logger.error("删除FTP文件失败: [{}]" , e);
			return false;
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *  删除FTP服务文件夹
	 * @param pathName 欲删除的FTP文件夹路径
	 * @return
	 * @date 2018年11月20日 @time 下午5:33:26
	 * @author xiepeiqi
	 */
	public Boolean removeDirectory(String pathName) {

		if (null == pathName) {
			return false;
		}
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpClientPool.borrowObject();
			pathName = encodingPath(pathName);
			if (ftpClient.removeDirectory(pathName)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("FTP 删除文件夹失败: [{}]" , e);
			return false;
		} finally {
			try {
				ftpClientPool.returnObject(ftpClient);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
