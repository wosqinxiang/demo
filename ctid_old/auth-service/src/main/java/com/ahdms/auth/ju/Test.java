package com.ahdms.auth.ju;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Base64;

public class Test {
	public static void main(String[] args) throws Exception {
		
//		
		 InputStream in = null;
		 byte[] data = null;
		 try {
		     in = new FileInputStream("E:\\mine\\ewm1.jpeg");
		     data = new byte[in.available()];
		     in.read(data);
		     in.close();
		 } catch (IOException e) {
		     e.printStackTrace();
		 }
		
		 String m_id= "20200509161945ed6dd77792084c3eac071bf699d8a280";
		
		String param = "name=陆斌&card=430527199405017213&img=" + new String(Base64.getEncoder().encode(data)) + "&m_id="+m_id;//�����Ա�
//		String param = "name=覃翔&card=430523199011157277&m_id="+m_id;//����Ա�
//		String url = "http://122.115.50.243/verify/rr/rrhy_two";//�����ַ
		String url = "http://122.115.50.243/verify/rr/rrhy_three";//�����ַ
		String strKey = "F]1u.,}C}7%j]!A4z*Yj,RD^RM`TIq^(!5Ih&p}cnT^</H&B*RdhT:GTb76*O]O(wgwhUCXCJXJX/?Pqx8&F}kxh+s>'jfzUf[5|W'GAdUFhGOBq)a1wrdOTsAYRxV2hqqi'`(`-K)C&.~S=$r?z`r$Y?)&mFZC5_=D<-Q:*/D=z<p?g$a?hcD5qZGbdO--S6_?&E0I`/l?oLsc\\FfdE5a&rCY:m\\j_wQlEza OiSw?x>cF;EwKg]hLnXw\\yMc=v?vAaHzOm[[?x3c5n?a?fa%?/?v`}@sYkJvKh?x9n`?Jc\\qFc?x7m?h?a?f&;*l?v?jXxPrWpNa4a?vOq_eYjOs/kQ1G=D`%(I:THL5cl?.?a?h(i?s2g0[@yLbA.Yo?x?vX.H/JtFqaMImHyRw?xOx?g?g3c?g?g?v5yMo`\\MpMp@eSiVb@b?x?vYjSv?j?x:m$sEjbXL/Lp@/a FzRo[qL[\\t?x";//�����ַ���

		param = AES_Encrypt.parseByte2HexStr(AES_Encrypt.enCrypt(param, strKey));
		param = "param=" + param;
		System.out.println(param);
		Long beginTime = new Date().getTime();
		System.out.println(getURLByPost(url,param));
		Long totalConsumedTime=new Date().getTime()-beginTime;
		System.out.println(totalConsumedTime);

}

	/**
     * post��ʽ����http����
     * @param urlStr
     * @param params   
     * @return
     * @throws Exception
     */
    public static String getURLByPost(String urlStr,String params)throws Exception{
        URL url=new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        printWriter.write(params);
        printWriter.flush();        
        BufferedReader in = null; 
        StringBuilder sb = new StringBuilder(); 
        try{   
            in = new BufferedReader( new InputStreamReader(conn.getInputStream(),"UTF-8") ); 
            String str = null;  
            while((str = in.readLine()) != null) {  
                sb.append( str );   
            }   
         } catch (Exception ex) { 
            throw ex; 
         } finally{  
          try{ 
              conn.disconnect();
              if(in!=null){
                  in.close();
              }
              if(printWriter!=null){
                  printWriter.close();
              }
          }catch(IOException ex) {   
              throw ex; 
          }   
         }   
         return sb.toString(); 
    }

}
