/**
 * Created on 2017年8月28日 by liuyipin
 */
package com.ahdms.ap.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.time.LocalDate;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahdms.ctidservice.common.Base64Utils;
import com.alibaba.druid.util.StringUtils; 

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
public class UpLoadFileUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpLoadFileUtil.class);
	/** 
	 * Description: 向FTP服务器上传文件 
	 * @Version1.0  
	 * @param url FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username FTP登录账号 
	 * @param password FTP登录密码 
	 * @param path FTP服务器保存目录 
	 * @param filename 上传到FTP服务器上的文件名 
	 * @param input 输入流 
	 * @return 成功返回true，否则返回false 
	 */  
	public static boolean uploadFile(String url,int port,String username, String password, String path, String filename, InputStream input) {  
	    boolean success = false;  
	    FTPClient ftp = new FTPClient();  
	    try {  
	        int reply;  
	        ftp.connect(url, port);//连接FTP服务器  
	        //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
	        ftp.login(username, password);//登录  
	        ftp.setFileType(ftp.BINARY_FILE_TYPE); 
//	        ftp.setFileTransferMode(ftp.STREAM_TRANSFER_MODE);
	        reply = ftp.getReplyCode();  
	        if (!FTPReply.isPositiveCompletion(reply)) {  
	            ftp.disconnect();  
	            return success;  
	        }  
	        ftp.enterLocalPassiveMode(); 
            ftp.setRemoteVerificationEnabled(false);
	        boolean b = ftp.changeWorkingDirectory(path);  
	        if(!b){
	        	ftp.makeDirectory(path);
	        	ftp.changeWorkingDirectory(path);
	        }
	        boolean bl = ftp.storeFile(filename, input);           
	        LOGGER.debug("==================文件上传结果=============：{}"+bl);
	        input.close();  
	        ftp.logout();  
	        success = true;  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (ftp.isConnected()) {  
	            try {  
	                ftp.disconnect();  
	            } catch (IOException ioe) {  
	            }  
	        }  
	    }  
	    return success;  
	}
	
	 /** 
     * Description: 从FTP服务器下载文件 
     * @param host FTP服务器hostname 
     * @param port FTP服务器端口 
     * @param username FTP登录账号 
     * @param password FTP登录密码 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param localPath 下载后保存到本地的路径 
     * @return 
     */   
	 public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
	            String fileName, String localPath) {
	        boolean result = false;
	        FTPClient ftp = new FTPClient();
	        try {
	            int reply;
	            ftp.connect(host, port);
	            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
	            ftp.login(username, password);// 登录
	            ftp.setFileType(ftp.BINARY_FILE_TYPE);
		        ftp.setFileTransferMode(ftp.STREAM_TRANSFER_MODE);
	            reply = ftp.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(reply)) {
	                ftp.disconnect();
	                return result;
	            }

		        ftp.enterLocalPassiveMode(); 
	            ftp.setRemoteVerificationEnabled(false);
	            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
	            FTPFile[] fs = ftp.listFiles();
	            for (FTPFile ff : fs) {
	                if (ff.getName().equals(fileName)) {
	                    File localFile = new File(localPath + "/" + ff.getName());

	                    OutputStream is = new FileOutputStream(localFile);
	                    ftp.retrieveFile(ff.getName(), is);
	                    is.close();
	                }
	            }

	            ftp.logout();
	            result = true;
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (ftp.isConnected()) {
	                try {
	                    ftp.disconnect();
	                } catch (IOException ioe) {
	                }
	            }
	        }
	        return result;
	    }


	 public static boolean testConn(String ip,int port, String username, String password) throws NumberFormatException, SocketException, IOException {
	        FTPClient ftp = new FTPClient();
	        boolean bool = false;
	            int reply;
	            // 连接FTP服务器
	            ftp.connect(ip, port);
	            ftp.login(username, password);// 登录
	            reply = ftp.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(reply)) {
	                ftp.disconnect();
	                return bool;
	            }
	            bool = true;
	        return bool;
	    }
	 
	public static void main(String[] args) throws FileNotFoundException {
//		Base64ToImage(s, "F:\\log\\1.jpg"); 
		FileInputStream in1 = new FileInputStream("F:/log/111.png"); 
		uploadFile("121.40.140.67", 21, "ygw", "ygw123", "/ctidPic"+"/"+LocalDate.now().toString(), "1113.jpg", in1);
		
		downloadFile("121.40.140.67", 21, "ygw", "ygw123", "/ctidPic"+"/"+LocalDate.now().toString(), "1113.jpg", "F:/log");
//		
//		System.out.println(PicUtil.ImageToBase64ByLocal("F:/log/113.png"));
		
		
		 
	}
	

	public static boolean Base64ToImage(String imgStr,String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
		 
		if (StringUtils.isEmpty(imgStr)) // 图像数据为空
			return false;
 
		Base64Utils decoder = new Base64Utils();
		try {
			// Base64解码
			byte[] b = decoder.decode(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
 
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
 
			return true;
		} catch (Exception e) {
			return false;
		}
 
	}
 
 
  
}