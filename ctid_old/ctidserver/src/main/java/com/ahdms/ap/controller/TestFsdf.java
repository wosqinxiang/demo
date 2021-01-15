/**
 * Created on 2019年11月1日 by liuyipin
 */
package com.ahdms.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.ap.config.FastDFSTool;
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
@RestController
@RequestMapping("/tttt")
public class TestFsdf {
	
	@Autowired
	private FastDFSTool FastDFSTool;
	
 
	@RequestMapping(value = "/serverLogin", method = RequestMethod.POST)
	public void serverLogin() throws Exception {
//		 String s = FastDFSTool.upload("C:/Users/liuyipin/Pictures/picture/760050.jpg");
		 byte[] b = FastDFSTool.download("http://172.16.16.90:8888/head/M00/00/00/rBABaV2_m_aAZuHZAAKUBNu28SE002.jpg");
//		 byte[] b = FastDFSTool.download("http://172.16.16.100:8888/group1/M00/05/26/rBABvl2wCb6ARejNAAAf3fzfSPE226.jpg");
		 
		 System.out.println(Base64Utils.encode(b));
//		 System.out.println(s);
	}
	
	
}

