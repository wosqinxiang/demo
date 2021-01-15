/**
 * Created on 2019年10月30日 by liuyipin
 */
package com.ahdms.ap.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.csource.fastdfs.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component; 


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
public class FastDFSTool { 

	@Value("${fsdfs.address}")
	private String dfsAddress ;

	//	 private String DFS_DOMAIN = dfsAddress;// 域名一旦确定，几乎不变，写死吧。
	private static final Logger logger = LoggerFactory.getLogger(FastDFSTool.class);
	private static Pattern pattern1 = Pattern.compile("http://[^/]+/(head)/(.*)");
	private static Pattern pattern2 = Pattern.compile("http://[^/]+/(other)/(.*)");
	private static Pattern pattern3 = Pattern.compile("http://[^/]+/(group\\d+)/(.*)");
	
	@Autowired
	private FastDFSPool pool;

	public String upload(String filePath) throws Exception {
		StorageClient sc = null;
		try {
			sc = pool.getStorageClient();
			if (sc != null) {
				String[] results = sc.upload_file(filePath, null, null);
				if (results == null) {
					logger.debug("upload fail:" + sc.getErrorCode());
				} else {
					logger.debug("group_name:" + results[0] + "\tremote_filename:" + results[1]);
					return dfsAddress + results[0] + "/" + results[1];
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			pool.releaseStorageClient(sc);
		}
		return null;
	}

	/**
	 * 上传数据流到文件系统
	 *
	 * @param filebuf 文件数据流
	 * @param fileExt 文件扩展名
	 * @return 文件系统的url地址
	 * @throws Exception
	 */
	public String upload(byte[] filebuf, String fileExt) throws Exception {
		StorageClient sc = null;
		try {
			sc = pool.getStorageClient();
			if (sc != null) {
				String[] results = sc.upload_file(filebuf, 0, filebuf.length, fileExt, null);
				if (results == null) {
					logger.debug("upload fail:" + sc.getErrorCode());
				} else {
					logger.debug("group_name:" + results[0] + "\tremote_filename:" + results[1]);
					return dfsAddress + results[0] + "/" + results[1];
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			pool.releaseStorageClient(sc);
		}
		return null;
	}

	/**
	 * 从文件系统下载一个文件到本地
	 *
	 * @param url           文件系统url地址
	 * @param localFileName 下载到本地的文件绝对路径，包括文件名
	 * @throws Exception
	 */
	public void download(String url, String localFileName) throws Exception {
		StorageClient sc = null;
		try {
			sc = pool.getStorageClient();
			if (sc != null) {
				Matcher m = null ;
				if(url.contains("head")){
					m =  pattern1.matcher(url); 
				}
				if(url.contains("other")){
					m =  pattern2.matcher(url); 
				}
				if(url.contains("group")){
					m =  pattern3.matcher(url); 
				}
				if (m.find()) { 
					String groupName = m.group(1);
					String remoteFileName = m.group(2);
					System.out.println("start to download..");
					int affect = sc.download_file(groupName, remoteFileName, localFileName);
					logger.info("download files:{} [{}bytes]", url, affect);
				}/*else if(m2.find()) {
					String groupName = m2.group(1);
					String remoteFileName = m2.group(2);
					System.out.println("start to download..");
					int affect = sc.download_file(groupName, remoteFileName, localFileName);
					logger.info("download files:{} [{}bytes]", url, affect);
				}else if(m3.find()) {
					String groupName = m3.group(1);
					String remoteFileName = m3.group(2);
					System.out.println("start to download..");
					int affect = sc.download_file(groupName, remoteFileName, localFileName);
					logger.info("download files:{} [{}bytes]", url, affect);
				}*/
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			pool.releaseStorageClient(sc);
		}
	}

	/**
	 * 下载文件到本进程内存中
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public byte[] download(String url) throws Exception {
		StorageClient sc = null;
		try {
			sc = pool.getStorageClient();
			if (sc != null) {
				Matcher m = null ;
				if(url.contains("head")){
					m =  pattern1.matcher(url); 
				}
				if(url.contains("other")){
					m =  pattern2.matcher(url); 
				}
				if(url.contains("group")){
					m =  pattern3.matcher(url); 
				}
				if (m.find()) {
					String groupName = m.group(1);
					String remoteFileName = m.group(2);
					logger.info("download files:{} to memory", url);
					return sc.download_file(groupName, remoteFileName);
				}/*else if (m2.find()) {
					String groupName = m2.group(1);
					String remoteFileName = m2.group(2);
					logger.info("download files:{} to memory", url);
					return sc.download_file(groupName, remoteFileName);
				}else if (m3.find()) {
					String groupName = m3.group(1);
					String remoteFileName = m3.group(2);
					logger.info("download files:{} to memory", url);
					return sc.download_file(groupName, remoteFileName);
				}*/
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			pool.releaseStorageClient(sc);
		}
		return null;
	}

	public void delete(String url) throws Exception {
		StorageClient sc = null;
		try {
			sc = pool.getStorageClient();
			if (sc != null) {
				Matcher m = null ;
				if(url.contains("head")){
					m =  pattern1.matcher(url); 
				}
				if(url.contains("other")){
					m =  pattern2.matcher(url); 
				}
				if(url.contains("group")){
					m =  pattern3.matcher(url); 
				}
				if (m.find()) {
					String groupName = m.group(1);
					String remoteFileName = m.group(2);
					int affect = sc.delete_file(groupName, remoteFileName);
					logger.info("delete files:{} [return:{}]", url, affect);
				}/*else if (m2.find()) {
					String groupName = m2.group(1);
					String remoteFileName = m2.group(2);
					int affect = sc.delete_file(groupName, remoteFileName);
					logger.info("delete files:{} [return:{}]", url, affect);
				}else if (m3.find()) {
					String groupName = m3.group(1);
					String remoteFileName = m3.group(2);
					int affect = sc.delete_file(groupName, remoteFileName);
					logger.info("delete files:{} [return:{}]", url, affect);
				}*/
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			pool.releaseStorageClient(sc);
		}
	}
}

