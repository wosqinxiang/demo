package com.ahdms.framework.core.copier;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class BeanCopierKey {
    private final Class<?> source;
    private final Class<?> target;
    private final boolean useConverter;
    private final boolean nonNull;
}
