package com.ahdms.ctidservice.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	public static byte[] toByteArray(String filename) throws IOException {  

		File f = new File(filename);  
		if (!f.exists()) {  
			throw new FileNotFoundException(filename);  
		}  

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());  
		BufferedInputStream in = null;  
		try {  
			in = new BufferedInputStream(new FileInputStream(f));  
			int buf_size = 1024;  
			byte[] buffer = new byte[buf_size];  
			int len = 0;  
			while (-1 != (len = in.read(buffer, 0, buf_size))) {  
				bos.write(buffer, 0, len);  
			}  
			return bos.toByteArray();  
		} catch (IOException e) {  
			e.printStackTrace();  
			throw e;  
		} finally {  
			try {  
				in.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
			bos.close();  
		}  
	} 
	
	public static byte[] toByteArray(InputStream is) throws IOException {  


		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		BufferedInputStream in = null;  
		try {  
			in = new BufferedInputStream(is);  
			int buf_size = 1024;  
			byte[] buffer = new byte[buf_size];  
			int len = 0;  
			while (-1 != (len = in.read(buffer, 0, buf_size))) {  
				bos.write(buffer, 0, len);  
			}  
			return bos.toByteArray();  
		} catch (IOException e) {  
			e.printStackTrace();  
			throw e;  
		} finally {  
			try {  
				in.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
			bos.close();  
		}  
	}
	
	public static void bytesToFile(String fileName, byte[] data) {
		try {  
			File file = new File(fileName);  
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream in = new FileOutputStream(file); 

			in.write(data, 0, data.length);  
			in.close(); 

		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}
}
