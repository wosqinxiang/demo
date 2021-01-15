package com.ahdms.ctidservice.util.ctid;

import java.util.LinkedHashMap;
import java.util.Map;

import com.ahdms.ctidservice.Exception.ApiException;
import com.ahdms.ctidservice.vo.ReservedDataEntity;
import com.ahdms.ctidservice.vo.SfxxBean;

import net.sf.json.JSONObject;

public class ReservedDataUtils {
	
	public static ReservedDataEntity getReservedDataEntity(byte[] userData) throws ApiException{
		ReservedDataEntity reservedData = new ReservedDataEntity();
		reservedData.setsFXX(getSFXXBean(userData));
		reservedData.setwZXX(new ReservedDataEntity.WZXXBean());
		reservedData.setzP(new ReservedDataEntity.ZPBean());
		return reservedData;
	}
	
	public static SfxxBean getSfxxBean(byte[] userData) throws ApiException{
		SfxxBean sfxx = new SfxxBean();
		try {
			byte[] nameBytes = new byte[45];
			byte[] dnBytes = new byte[18];
			byte[] startBytes = new byte[8];
			byte[] endBytes = new byte[8];
			System.arraycopy(userData, 0, nameBytes, 0, 45);
			System.arraycopy(userData, 45, dnBytes, 0, 18);
			System.arraycopy(userData, 45 + 18, startBytes, 0, 8);
			System.arraycopy(userData, 45 + 18 + 8, endBytes, 0, 8);
			
			String idName = new String(nameBytes);
			String idNumber = new String(dnBytes);
			String validFrom = new String(startBytes);
			String validEnd = new String(endBytes);
			sfxx.setName(idName.trim());
			sfxx.setIdCard(idNumber.trim().toUpperCase());
			sfxx.setStartDate(validFrom.trim());
			sfxx.setEndDate(validEnd.trim());
			
		} catch (Exception e) {
			throw new ApiException("解密身份证信息失败");
		}
		return sfxx;
	}
	
	
	public static ReservedDataEntity.SFXXBean getSFXXBean(byte[] userData) throws ApiException{
		ReservedDataEntity.SFXXBean sfxxBean = new ReservedDataEntity.SFXXBean();
		try {
			byte[] nameBytes = new byte[45];
			byte[] dnBytes = new byte[18];
			byte[] startBytes = new byte[8];
			byte[] endBytes = new byte[8];
			System.arraycopy(userData, 0, nameBytes, 0, 45);
			System.arraycopy(userData, 45, dnBytes, 0, 18);
			System.arraycopy(userData, 45 + 18, startBytes, 0, 8);
			System.arraycopy(userData, 45 + 18 + 8, endBytes, 0, 8);
			
			String idName = new String(nameBytes);
			String idNumber = new String(dnBytes);
			String validFrom = new String(startBytes);
			String validEnd = new String(endBytes);
			sfxxBean.setxM(idName.trim());
			sfxxBean.setgMSFZHM(idNumber.trim().toUpperCase());
			sfxxBean.setyXQQSRQ(validFrom.trim());
			sfxxBean.setyXQJZRQ(validEnd.trim());
			
		} catch (Exception e) {
			throw new ApiException("解密身份证信息失败");
		}
		return sfxxBean;
	}
	
	public static String downReservedData(byte[] userData) {
		byte[] nameBytes = new byte[45];
		byte[] dnBytes = new byte[18];
		byte[] startBytes = new byte[8];
		byte[] endBytes = new byte[8];
		System.arraycopy(userData, 0, nameBytes, 0, 45);
		System.arraycopy(userData, 45, dnBytes, 0, 18);
		System.arraycopy(userData, 45 + 18, startBytes, 0, 8);
		System.arraycopy(userData, 45 + 18 + 8, endBytes, 0, 8);
		
		String idName = new String(nameBytes);
		String idNumber = new String(dnBytes);
		String validFrom = new String(startBytes);
		String validEnd = new String(endBytes);
		
		Map<String,String> map = new LinkedHashMap<>();
		map.put("xM", idName);
		map.put("gMSFZHM", idNumber);
		map.put("yXQQSRQ", validFrom);
		map.put("yXQJZRQ", validEnd);
		
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}
	
	public static ReservedDataEntity getReservedDataEntity(String xM,String gMSFZHM,String yXQQSRQ,String yXQJZRQ){
		ReservedDataEntity reservedData = new ReservedDataEntity();
		ReservedDataEntity.SFXXBean sfxx = new ReservedDataEntity.SFXXBean();
		sfxx.setxM(xM);
		sfxx.setgMSFZHM(gMSFZHM);
		sfxx.setyXQQSRQ(yXQQSRQ);
		sfxx.setyXQJZRQ(yXQJZRQ);
		reservedData.setsFXX(sfxx);
		reservedData.setwZXX(new ReservedDataEntity.WZXXBean());
		reservedData.setzP(new ReservedDataEntity.ZPBean());
		return reservedData;
	}
	
	public static ReservedDataEntity getReservedDataEntity(String sfxxStr){
		JSONObject sfxxJO = JSONObject.fromObject(sfxxStr);
		String xM = sfxxJO.getString("xM");
		String gMSFZHM = sfxxJO.getString("gMSFZHM");
		String yXQQSRQ = sfxxJO.getString("yXQQSRQ");
		String yXQJZRQ = sfxxJO.getString("yXQJZRQ");
		ReservedDataEntity reservedData = new ReservedDataEntity();
		ReservedDataEntity.SFXXBean sfxx = new ReservedDataEntity.SFXXBean();
		sfxx.setxM(xM);
		sfxx.setgMSFZHM(gMSFZHM);
		sfxx.setyXQQSRQ(yXQQSRQ);
		sfxx.setyXQJZRQ(yXQJZRQ);
		reservedData.setsFXX(sfxx);
		reservedData.setwZXX(new ReservedDataEntity.WZXXBean());
		reservedData.setzP(new ReservedDataEntity.ZPBean());
		return reservedData;
	}
	
}
