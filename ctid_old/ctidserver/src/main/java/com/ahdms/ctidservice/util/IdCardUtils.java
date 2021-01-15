package com.ahdms.ctidservice.util;

public class IdCardUtils {
	
	private static int[] w = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
	
	private static String[] value = {"1","0","X","9","8","7","6","5","4","3","2"};
	
	public static boolean verifyIdCard(String idCardNum){
		if(idCardNum == null || !idCardNum.matches("[\\d]{18}")){
			return false;
		}
		
		int sum = 0;
		for(int i=0;i<=16;i++){
			int ai = Integer.parseInt(idCardNum.charAt(i)+"");
			sum += ai * w[i];
		}
		int m = sum % 11;
		if(value[m].equals(idCardNum.charAt(17)+"")){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println("AA2c327130d97d4b55bb150602fc2ba478".substring(2));
	}

}
