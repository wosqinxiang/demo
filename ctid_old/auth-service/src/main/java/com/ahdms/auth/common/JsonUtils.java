package com.ahdms.auth.common;

import com.ahdms.auth.model.ReservedDataEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author qinxiang
 * @date 2020-09-02 13:34
 */
public class JsonUtils {

    public static String bean2Json(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WRITE_MAP_NULL_FEATURES,SerializerFeature.SortField,SerializerFeature.QuoteFieldNames);
    }

    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        return JSON.parseObject(jsonStr, objClass, Feature.OrderedField);
    }

    public static void main(String[] args) {
        ReservedDataEntity.SFXXBean sfxx = new ReservedDataEntity.SFXXBean();
        System.out.println(JSON.toJSONString(sfxx));
    }

}
