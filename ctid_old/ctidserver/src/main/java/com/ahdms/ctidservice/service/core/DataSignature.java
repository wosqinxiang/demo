//package com.ahdms.ctidservice.service.core;
//
//import java.io.UnsupportedEncodingException;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import com.fri.ctid.security.bjca.Api;
//
//import cn.org.bjca.client.exceptions.ApplicationNotFoundException;
//import cn.org.bjca.client.exceptions.CommonClientException;
//import cn.org.bjca.client.exceptions.InitException;
//import cn.org.bjca.client.exceptions.ParameterTooLongException;
//import cn.org.bjca.client.exceptions.SVSConnectException;
//import cn.org.bjca.client.security.SecurityEngineDeal;
//
//
///**
// * Created by on 2018/3/21.
// * 签名数据
// */
//@Component
//public class DataSignature {
//
//    private static String certID = "rzfw01";
//    
//    @Value("${ctid.svs.profile.path}")
//    private String configUrl;
//    
//    @PostConstruct
//    private void init(){
//    	Api.initConnection(configUrl);
//    }
//    
//    
//    //签名(北京CA)
//    public String signDataByP7DetachForJit(String inData) throws Exception{
//    	SecurityEngineDeal sed = SecurityEngineDeal.getInstance("SVSQm");
//    	String signData=null;
//		try {
//			signData = sed.signDataByP7DetachForJit(inData.getBytes());
//		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
//		} catch (SVSConnectException e) {
////			e.printStackTrace();
//		}  catch (ParameterTooLongException e) {
////			e.printStackTrace();
//		} catch (CommonClientException e) {
////			e.printStackTrace();
//		}catch (NullPointerException e) {
////			e.printStackTrace();
//			return null;
//		}
//        return signData;
//    }
//    
//    public String signDataByP7DetachForJitWithCount(String source,Integer count) throws Exception{
//    	String signData=null;
//    	if(count==null || count<=0){
//    		count = 1;
//    	}
//    	for(int i=1;i<=count;i++){
//    		if(signData==null){
//    			signData = signDataByP7DetachForJit(source);
//    		}else{
//    			break;
//    		}
//    	}
//    	return signData;
//    }
//
//    //验签方法(北京CA)
//    public boolean encryptEnvelopeCa(byte[] data ,byte [] sign) throws Exception {
//        boolean verifySignRes = Api.p7SignVerify(data,sign);
//        return verifySignRes;
//    }
//
//}