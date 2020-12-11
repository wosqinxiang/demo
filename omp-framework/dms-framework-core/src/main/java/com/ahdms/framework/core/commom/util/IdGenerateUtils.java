package com.ahdms.framework.core.commom.util;

import com.ahdms.framework.core.commom.Holder;
import com.ahdms.framework.core.exception.FrameworkException;
import com.ahdms.framework.core.id.IdGeneratorManager;

import java.time.format.DateTimeFormatter;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/15 10:24
 */
public class IdGenerateUtils {
    public static final DateTimeFormatter LONG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter SHORT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");
    public static final DateTimeFormatter LONG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    public static final String IG_GENERATOR_BEAN_NAME = "mixStrategyGeneratorManager";
    public static final char DEFAULT_SEQUENCE_PAD_CHAR = '0';

    public static final String REQUEST_ID_PREFIX_KEY = "REQUESTID_";
    public static final int DEFAULT_REQUEST_ID_LENGTH_SEQ = 10;

    private static final Holder<IdGeneratorManager> idGeneratorHolder = new Holder<>();


    /**
     * 生成Id
     *
     * @param idName
     * @return
     */
    public static String generateId(String idName) {
        return getGenerator().generateId(idName);
    }

    /**
     * Id = prefix + id
     *
     * @param idName
     * @param prefix
     * @return
     */
    public static String generateId(String idName, String prefix) {
        return prefix + generateId(idName);
    }

    /**
     * @param idName
     * @param length 序列号的长度
     * @return
     */
    public static String generateId(String idName, int length) {
        return StringUtils.leftPad(generateId(idName), length, getPaddingChar());
    }

    /**
     * @param idName
     * @param prefix ID前缀
     * @param length 序列号的长度
     * @return
     */
    public static String generateId(String idName, String prefix, int length) {
        return prefix + generateId(idName, length);
    }


    /**
     * prefix + yyyymmdd + id
     *
     * @param idName
     * @param prefix
     * @param length
     * @return
     */
    public static String generateIdWithDate(String idName, String prefix, int length) {
        return prefix + DateUtils.getCurrentDateTime(LONG_DATE_FORMATTER) + generateId(idName, length);
    }

    /**
     * prefix + yymmdd + id
     *
     * @param idName
     * @param prefix
     * @param length
     * @return
     */
    public static String generateIdWithShortDate(String idName, String prefix, int length) {
        return prefix + DateUtils.getCurrentDateTime(SHORT_DATE_FORMATTER) + generateId(idName, length);
    }

    /**
     * yyyymmdd + id
     *
     * @param idName
     * @param length
     * @return
     */
    public static String generateIdWithDate(String idName, int length) {
        return DateUtils.getCurrentDateTime(LONG_DATE_FORMATTER) + generateId(idName, length);
    }

    /**
     * yymmdd + id
     *
     * @param idName
     * @param length
     * @return
     */
    public static String generateIdWithShortDate(String idName, int length) {
        return DateUtils.getCurrentDateTime(SHORT_DATE_FORMATTER) + generateId(idName, length);
    }

    /**
     * prefix + yyyymmddHHmmss + id
     *
     * @param idName
     * @param prefix
     * @param length
     * @return
     */
    public static String generateIdWithDateTime(String idName, String prefix, int length) {
        return prefix + DateUtils.getCurrentDateTime(LONG_DATETIME_FORMATTER) + generateId(idName, length);
    }

    /**
     * prefix + yymmddHHmmss + id
     *
     * @param idName
     * @param prefix
     * @param length
     * @return
     */
    public static String generateIdWithShortDateTime(String idName, String prefix, int length) {
        return prefix + DateUtils.getCurrentDateTime(SHORT_DATETIME_FORMATTER) + generateId(idName, length);
    }

    /**
     * yyyymmddHHmmss + id
     *
     * @param idName
     * @param length
     * @return
     */
    public static String generateIdWithDateTime(String idName, int length) {
        return DateUtils.getCurrentDateTime(LONG_DATETIME_FORMATTER) + generateId(idName, length);
    }

    /**
     * yymmddHHmmss + id
     *
     * @param idName
     * @param length
     * @return
     */
    public static String generateIdWithShortDateTime(String idName, int length) {
        return DateUtils.getCurrentDateTime(SHORT_DATETIME_FORMATTER) + generateId(idName, length);
    }

    /**
     * global unique id
     *
     * @param idName
     * @return
     */
    public static String generateGlobalId(String idName) {
        return getGenerator().generateId(idName, true);
    }

    /**
     * global unique id
     * padding the left side of the ID with 0 until the specified length
     *
     * @param idName idName
     * @param length id length after padding
     * @return
     */
    public static String generateGlobalId(String idName, int length) {
        return StringUtils.leftPad(generateGlobalId(idName), length, getPaddingChar());
    }

    /**
     * @param idName
     * @param length
     * @return
     */
    public static String generateGlobalIdWithShortDateTime(String idName, int length) {
        return DateUtils.getCurrentDateTime(SHORT_DATETIME_FORMATTER) + generateGlobalId(idName, length);
    }

    /**
     * @param idName
     * @param prefix
     * @param length
     * @return
     */
    public static String generateGlobalIdWithShortDateTime(String idName, String prefix, int length) {
        return prefix + generateGlobalIdWithShortDateTime(idName, length);
    }

    private static IdGeneratorManager getGenerator() {
        if (!idGeneratorHolder.isEmpty()) {
            return idGeneratorHolder.get();
        }
        IdGeneratorManager idGeneratorManager = BeanLoaderUtils.getSpringBean(IG_GENERATOR_BEAN_NAME, IdGeneratorManager.class);
        FrameworkException.throwOnFalse(ObjectUtils.isNotNull(idGeneratorManager), "No bean \"" + IG_GENERATOR_BEAN_NAME + "\" found in spring application context.");
        idGeneratorHolder.set(idGeneratorManager);
        return idGeneratorManager;
    }


    public static char getPaddingChar() {
        return DEFAULT_SEQUENCE_PAD_CHAR;
    }

}
