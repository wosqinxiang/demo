package com.ahdms.framework.idgen;

import com.ahdms.framework.core.commom.util.CollectionUtils;
import com.ahdms.framework.core.commom.util.ObjectUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.exception.FrameworkException;
import com.ahdms.framework.core.id.IdGeneratorManager;
import com.ahdms.framework.idgen.config.IdGenProperties;
import com.ahdms.framework.idgen.constant.GenerateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/14 13:59
 */
@Slf4j
public class MixStrategyGeneratorManager implements IdGeneratorManager, InitializingBean {

    private IdGenProperties idGenProperties;
    private List<IdGenerator> idGenerators;
    private Map<String, IdGenProperties.IdProperty> idProperties = new LinkedHashMap<>();

    public MixStrategyGeneratorManager(IdGenProperties idGenProperties, List<IdGenerator> idGenerators) {
        Assert.notNull(idGenerators, "IdGenerator list cannot be null");
        this.idGenProperties = idGenProperties;
        this.idGenerators = idGenerators;
    }

    @Override
    public String generateId(String idName) {
        return generateId(idName, false);
    }

    @Override
    public String generateId(String idName, boolean global) {
        IdGenProperties.IdProperty idProperty = loadIdProperties(idName);
        for (IdGenerator idGenerator : idGenerators) {
            if (!idGenerator.supports(idProperty.getStrategy())) {
                continue;
            }
            return idGenerator.generateId(idProperty, global);
        }
        return null;
    }

    private IdGenProperties.IdProperty loadIdProperties(String idName) {
        FrameworkException.throwOnFalse(StringUtils.isNotBlank(idName), "Id name cloud not be blank");
        IdGenProperties.IdProperty idProperty = idProperties.get(idName);
        FrameworkException.throwOnFalse(ObjectUtils.isNotNull(idProperty),
                StringUtils.format("Id with key '{}' not found, please defined it with property key 'dms.idgen.idProperties.${idName}'", idName));
        return idProperty;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdGenProperties.IdProperty> properties = this.idGenProperties.getIdProperties();
        if (CollectionUtils.isEmpty(properties)) {
            log.warn("No id generate properties defined, please defined it with property key 'dms.idgen.idProperties.${idName}'");
            return;
        }
        properties.forEach(idProperty -> {
            FrameworkException.throwOnFalse(ObjectUtils.isNotNull(idProperty.getStrategy()),
                    StringUtils.format("Id with key '{}' must defined the strategy, please defined it with property key 'dms.idgen.idProperties.{}.strategy'", idProperty.getName(), idProperty.getName()));
            boolean isLegality = Stream.of(GenerateStrategy.values()).anyMatch(v -> ObjectUtils.nullSafeEquals(v, idProperty.getStrategy()));
            FrameworkException.throwOnFalse(isLegality,
                    StringUtils.format("Id with key '{}' defined error of strategy, only support LOCAL, PIECEMEAL and CONSECUTIVE."));
            FrameworkException.throwOnFalse(ObjectUtils.isNotNull(idProperty.getMaxValue()),
                    StringUtils.format("Id with key '{}' must defined the max value, please defined it with property key 'dms.idgen.idProperties.{}.maxValue'", idProperty.getName(), idProperty.getName()));
            FrameworkException.throwOnFalse(idProperty.getMaxValue().longValue() > 1,
                    StringUtils.format("Idgen max value must be large than or equals to 1, illegal Idgen key {}.", idProperty.getName()));
            FrameworkException.throwOnFalse((ObjectUtils.isNull(idProperty.getMinValue()) || idProperty.getMinValue().longValue() > 1),
                    StringUtils.format("Idgen min value must be large than or equals to 1, illegal Idgen key {}.", idProperty.getName()));
            idProperties.putIfAbsent(idProperty.getName(), idProperty);
        });
    }
}
