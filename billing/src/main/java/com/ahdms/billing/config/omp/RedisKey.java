package com.ahdms.billing.config.omp;

/**
 * @author qinxiang
 * @date 2020-08-07 8:52
 */
public class RedisKey {

    public static final String STATUS = "STATUS";

    public static final String USABLE = "USABLE";

    public static final String LIMIT = "LIMIT";

    public static final String SUPPLIER = "SUPPLIER";

    public static final String COUNT = "COUNT";

    public static final String JF = "JF:";

    public static final String SPLIT = ":";

    public static final String OMP = "OMP";

    public static String getHashKeys(String value){
        return JF+value;
    }

    public static String getFieldLimit(String preKey) {
        return getKey(preKey, LIMIT);
    }

    public static String getFieldSupplier(String preKey) {
        return getKey(preKey, SUPPLIER);
    }

    public static String getFieldUsable(String preKey) {
        return getKey(preKey, USABLE);
    }

    public static String getFieldStatus(String preKey) {
        return getKey(preKey, STATUS);
    }

    public static String getFieldCount(String preKey) {
        return getKey(preKey, COUNT);
    }

    private static String getKey(String preKey, String value) {
        return preKey + SPLIT + value;
    }


}
