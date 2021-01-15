package com.ahdms.ctidservice.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ahdms.ctidservice.bean.dto.FaceverifyBean;
import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.util.BaiduApiUtils;
import com.ahdms.ctidservice.vo.CtidResult;

//@RestController
//@RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
public class BaiduApiController {
	Logger logger = LoggerFactory.getLogger(BaiduApiController.class);
	
	@Autowired
	private BaiduApiUtils baiduApiUtils;
	
	@Value("${face.liveness.score:0.95}")
	private Double score;
	
	@RequestMapping(value={"/V215/wxctid/faceVerify","/wxctid/faceVerify"})
	public CtidResult faceVerify(@RequestBody FaceverifyBean faceverifyBean){
		if(faceverifyBean != null && StringUtils.isNotBlank(faceverifyBean.getImage())){
			faceverifyBean.setImage_type("BASE64");
			faceverifyBean.setFace_field("quality");
			List<FaceverifyBean> list = new ArrayList<>();
			list.add(faceverifyBean);
			return baiduApiUtils.getFaceLiveness(list);
		}
		return CtidResult.error("参数错误！请重试！");
	}
	
//	@RequestMapping("yqfk/ocrCarNum")
	public CtidResult ocrCarNum(@RequestParam String carPicData){
		if(StringUtils.isNotBlank(carPicData)){
			
			String carNum = baiduApiUtils.licensePlate(carPicData);
			if(null != carNum){
				return CtidResult.ok(carNum);
			}else{
				return CtidResult.error("车辆照片有误！未识别！");
			}
		}
		return CtidResult.error("参数错误！请重试！");
	}
	
//	@RequestMapping("yqfk/ocrCarPic")
	public CtidResult ocrCarNum(@RequestParam MultipartFile carPic){
		if(null != carPic){
			try {
				String carPicData = Base64Utils.encode(carPic.getBytes());
				return ocrCarNum(carPicData);
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}
		return CtidResult.error("参数错误！请重试！");
	}
	
//	@RequestMapping("yqfk/searchPlace")
	public CtidResult searchPlace(@RequestParam String keyword){
		if(StringUtils.isNotBlank(keyword)){
			List<String> searchPlace = baiduApiUtils.searchPlace(keyword);
			if(searchPlace != null){
				return CtidResult.ok(searchPlace);
			}
		}
		return CtidResult.error("未查询到数据");
	}
	
//	@RequestMapping("yqfk/searchPlaceName")
	public CtidResult searchPlaceName(@RequestParam String keyword){
		if(StringUtils.isNotBlank(keyword)){
			List<String> search = baiduApiUtils.search(keyword);
			if(search != null){
				return CtidResult.ok(search);
			}
		}
		return CtidResult.error("未查询到数据");
	}
	
}
