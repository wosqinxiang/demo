package com.ahdms.auth.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

public class ImageUtils {
	
	public static void main(String[] args) {
		byte[] fileToByte = FileUtils.fileToByte("E:/mine/a.jpg");
		System.out.println(isImage(Base64Utils.encode(fileToByte)));
	}
	
	public static boolean isImage(String imgBase64Str) {
        if (StringUtils.isEmpty(imgBase64Str)) {
            return false;
        } else {
            ByteArrayInputStream byteArrayInputStream = null;
            try {
                byteArrayInputStream = new ByteArrayInputStream(Base64Utils.decode(imgBase64Str));
                BufferedImage bufImg = ImageIO.read(byteArrayInputStream);
                if (bufImg == null) {
                    return false;
                }
                bufImg = null;
            } catch(Exception e){
            	return false;
            } finally {
                if (byteArrayInputStream != null) {
                    try {
						byteArrayInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        }
        return true;
    }

}
