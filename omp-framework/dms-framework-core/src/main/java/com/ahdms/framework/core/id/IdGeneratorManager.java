package com.ahdms.framework.core.id;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
public interface IdGeneratorManager {
    /**
     * 生成ID
     * idName
     *
     * @param idName ID name 对应的ID在application 范围内要唯一
     * @param global 是否全局唯一
     * @return ID
     */
    String generateId(String idName, boolean global);

    /**
     * 生成ID
     * idName
     *
     * @param idName ID name 对应的ID在application 范围内要唯一
     * @return ID
     */
    String generateId(String idName);

}
