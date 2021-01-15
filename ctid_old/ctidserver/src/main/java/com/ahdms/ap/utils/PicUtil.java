/**
 * Created on 2019年9月20日 by liuyipin
 */
package com.ahdms.ap.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;

import com.ahdms.ctidservice.common.Base64Utils;

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
public class PicUtil {

	public  static boolean Base64ToImage(String imgStr,String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片

		if (StringUtils.isEmpty(imgStr)) // 图像数据为空
			return false;
 
		try {
			// Base64解码
			byte[] b = Base64Utils.decode(imgStr);
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
			e.printStackTrace();
			return false;
		}

	}

	public static String ImageToBase64ByLocal(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理


		InputStream in = null;
		byte[] data = null;

		// 读取图片字节数组
		try {
		    File f = new File(imgFile);
		    if(f.exists()){
		        in = new FileInputStream(imgFile);
		    }
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码 

		return Base64Utils.encode(data);// 返回Base64编码过的字节数组字符串
	}
}

