/**
 * Created on 2019年11月25日 by liuyipin
 */
package com.ahdms.ap.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

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
public class SortUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SortUtils.class);

	/**
	 * @param paraMap 参数
	 * @param encode 编码
	 * @param isLower 是否小写
	 * @return
	 */
	public static String formatUrlParam(Map<String, Object> map2, String encode, boolean isLower) {
		String params = "";
		Map<String, Object> map = map2;
		
		try {
			List<Map.Entry<String, Object>> itmes = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
			
			//对所有传入的参数按照字段名从小到大排序
			//Collections.sort(items); 默认正序
			//可通过实现Comparator接口的compare方法来完成自定义排序
			Collections.sort(itmes, new Comparator<Map.Entry<String, Object>>() {
				@Override
				public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
					// TODO Auto-generated method stub
					return (o1.getKey().toString().compareTo(o2.getKey()));
				}
			});
			
			//构造URL 键值对的形式
			StringBuffer sb = new StringBuffer();
			for (Map.Entry<String, Object> item : itmes) {
				if (StringUtils.isNotBlank(item.getKey())) {
					String key = item.getKey();
//					System.out.println("---参数key----"+key);
//					LOGGER.info("---参数key----"+key);
					String val =  (String)item.getValue();
//					System.out.println("---参数value----"+val);
//					LOGGER.info("---参数value----"+val);
					val = URLEncoder.encode(val, encode);
					if (isLower) {
						sb.append(key.toLowerCase() + "=" + val);
					} else {
						sb.append(key + "=" + val);
					}
					sb.append("&");
				}
			}
			
			params = sb.toString();
			if (!params.isEmpty()) {
				params = params.substring(0, params.length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return params;
	}
} 

