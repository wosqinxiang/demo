package com.ahdms.auth.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	
	public static void main(String[] args) throws IOException {
		System.out.println(Base64Utils.encode(toByteArray("E:/mine/abcd.cer")));
	}
	
	/**
	 * 读取文件byte[]
	 * @创建人 hexin
	 * @创建时间 2019年8月14日
	 * @创建目的【】
	 * @修改目的【修改人：，修改时间：】
	 * @param filename 文件路径（带文件名）
	 * @return
	 * @throws IOException
	 */
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
	
	public static byte[] fileToByte(String filePath){
		FileInputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			in = new FileInputStream(filePath);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			out.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return out.toByteArray();
		
	}
	
	public static File byte2File(byte[] buf, String filePath, String fileName)  
    {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try  
        {  
            File dir = new File(filePath);  
            if (!dir.exists() && dir.isDirectory())  
            {  
                dir.mkdirs();  
            }  
            file = new File(filePath + File.separator + fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(buf);  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        finally  
        {  
            if (bos != null)  
            {  
                try  
                {  
                    bos.close();  
                }  
                catch (IOException e)  
                {  
                    e.printStackTrace();  
                }  
            }  
            if (fos != null)  
            {  
                try  
                {  
                    fos.close();  
                }  
                catch (IOException e)  
                {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return file;
    }

}
