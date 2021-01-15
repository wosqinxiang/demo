package com.ahdms.ctidservice.util;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;

/**
 * @author qinxiang
 * @date 2020-09-02 13:34
 */
public class JsonUtils {

    public static String bean2Json(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        try{
            return JSON.parseObject(jsonStr, objClass);
        }catch (Exception e){
            try {
                return objClass.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static String getString(JSONObject jsonObject,String key){
        try {
            return jsonObject.getString(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getInt(JSONObject jsonObject,String key){
        try {
            return jsonObject.getInt(key);
        } catch (Exception e) {
            return 1;
        }
    }

}
