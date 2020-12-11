package com.ahdms.framework.mybatis.config;

import com.ahdms.framework.core.commom.util.DmsContextUtils;
import com.ahdms.framework.mybatis.util.BIdUtils;

/**
 * MetaObject 数据来源
 *
 * @author Katrel.zhou
 */
public interface IMetaObjectDataSources {

    /**
     * 操作者
     *
     * @return 账号id
     */
    default Object getOperator() {
        return DmsContextUtils.getUserId();
    }

    /**
     * 获取授权后透传的角色id
     *
     * @return 角色id
     */
    default Object getOwner() {
        return DmsContextUtils.getRole();
    }

    /**
     * 获取业务id
     *
     * @return 业务id
     */
    default Object getBizId() {
        return BIdUtils.getBId();
    }

}
