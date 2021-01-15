package com.ahdms.ctidservice.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.common.FileUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class MatrixToImageWriter {
	 private static final int BLACK = 0xFF000000;
	    private static final int WHITE = 0xFFFFFFFF;

	    private MatrixToImageWriter() {
	    }

	    public static BufferedImage toBufferedImage(BitMatrix matrix) {
	        int width = matrix.getWidth();
	        int height = matrix.getHeight();
	        BufferedImage image = new BufferedImage(width, height,
	                BufferedImage.TYPE_INT_RGB);
	        for (int x = 0; x < width; x++) {
	            for (int y = 0; y < height; y++) {
	                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
	            }
	        }
	        
	        return image;
	    }

	    public static void writeToFile(BitMatrix matrix, String format, File file)
	            throws IOException {
	        BufferedImage image = toBufferedImage(matrix);
	        if (!ImageIO.write(image, format, file)) {
	            throw new IOException("Could not write an image of format "
	                    + format + " to " + file);
	        }
	    }

	    public static void writeToStream(BitMatrix matrix, String format,
	            OutputStream stream) throws IOException {
	        BufferedImage image = toBufferedImage(matrix);
	        if (!ImageIO.write(image, format, stream)) {
	            throw new IOException("Could not write an image of format " + format);
	        }
	    }
	    
	    public static String getQrCodeData(String text,int width,int height,String format) throws Exception {
	    	try {
				Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
				hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码

				BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
				        BarcodeFormat.QR_CODE, width, height, hints);
				BufferedImage bufferedImage = toBufferedImage(bitMatrix);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write( bufferedImage, "jpg", baos );
				baos.flush();
				//使用toByteArray()方法转换成字节数组
				byte[] imageInByte = baos.toByteArray();
				baos.close();
				return Base64Utils.encode(imageInByte);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
	    	return null;
	    }
	    
	    public static String getQrCodeData(String text) throws Exception {
	    	return getQrCodeData(text,280,280,"jpg");
	    }

}
