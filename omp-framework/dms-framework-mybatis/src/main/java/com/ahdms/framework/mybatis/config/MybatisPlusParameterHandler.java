package com.ahdms.framework.mybatis.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.BeanUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * 全局参数填充
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
public class MybatisPlusParameterHandler extends DefaultParameterHandler {

	private final TypeHandlerRegistry typeHandlerRegistry;
	private final MappedStatement mappedStatement;
	private final Object parameterObject;
	private final BoundSql boundSql;
	private final Configuration configuration;

	public MybatisPlusParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		super(mappedStatement, processBatch(mappedStatement, parameterObject), boundSql);
		this.mappedStatement = mappedStatement;
		this.configuration = mappedStatement.getConfiguration();
		this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}

	/**
	 * 批量（填充主键 ID）
	 *
	 * @param ms              MappedStatement
	 * @param parameterObject 插入数据库对象
	 * @return ignore
	 */
	protected static Object processBatch(MappedStatement ms, Object parameterObject) {
		//检查 parameterObject
		if (null == parameterObject
			|| ReflectionKit.isPrimitiveOrWrapper(parameterObject.getClass())
			|| parameterObject.getClass() == String.class) {
			//todo 这里需要处理下类型判断,逻辑删除还会进入这里,但SqlCommandType为UPDATE
			return null;
		}
		// 全局配置是否配置填充器
		MetaObjectHandler metaObjectHandler = GlobalConfigUtils.getMetaObjectHandler(ms.getConfiguration());
		boolean isFill = false;
		boolean isInsert = false;
		/* 只处理插入或更新操作 */
		if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
			isFill = true;
			isInsert = true;
		} else if (ms.getSqlCommandType() == SqlCommandType.UPDATE &&
			metaObjectHandler != null && metaObjectHandler.openUpdateFill()) {
			isFill = true;
		}
		if (isFill) {
			Collection<Object> parameters = getParameters(parameterObject);
			if (null != parameters) {
				List<Object> objList = new ArrayList<>();
				for (Object parameter : parameters) {
					TableInfo tableInfo = TableInfoHelper.getTableInfo(parameter.getClass());
					if (null != tableInfo) {
						objList.add(populateKeys(metaObjectHandler, tableInfo, ms, parameter, isInsert));
					} else {
						/*
						 * 非表映射类不处理
						 */
						objList.add(parameter);
					}
				}
				return objList;
			} else {
				TableInfo tableInfo = null;
				if (parameterObject instanceof Map) {
					Map<Object, Object> map = (Map) parameterObject;
					if (map.containsKey(Constants.ENTITY)) {
						Object et = map.get(Constants.ENTITY);
						if (et != null) {
							if (et instanceof Map) {
								Map<Object, Object> realEtMap = (Map) et;
								if (realEtMap.containsKey(Constants.MP_OPTLOCK_ET_ORIGINAL)) {
									Object entry = realEtMap.get(Constants.MP_OPTLOCK_ET_ORIGINAL);
									tableInfo = TableInfoHelper.getTableInfo(entry.getClass());
									Object fillEdEt = populateKeys(metaObjectHandler, tableInfo, ms, et, isInsert);
									realEtMap.put(Constants.MP_OPTLOCK_ET_ORIGINAL, fillEdEt);
									return parameterObject;
								}
							} else {
								tableInfo = TableInfoHelper.getTableInfo(et.getClass());
								Object fillEdEt = populateKeys(metaObjectHandler, tableInfo, ms, et, isInsert);
								map.put(Constants.ENTITY, fillEdEt);
								return parameterObject;
							}
						}
					} else if (map.containsKey(Constants.COLLECTION)) {
						// TODO L.cm 2019-09-09 08:54 添加集合类型的填充
						Object coll = map.get(Constants.COLLECTION);
						if (coll instanceof Collection) {
							Collection collection = (Collection) coll;
							List<Object> filledEtList = new ArrayList<>();
							for (Object et : collection) {
								if (et == null) {
									continue;
								}
								Class<?> etClass = et.getClass();
								// 如果集合里的类型是普通类型，直接跳出循环
								if (BeanUtils.isSimpleValueType(etClass)) {
									break;
								}
								if (tableInfo == null) {
									tableInfo = TableInfoHelper.getTableInfo(etClass);
								}
								if (tableInfo == null) {
									break;
								}
								Object fillEdEt = populateKeys(metaObjectHandler, tableInfo, ms, et, isInsert);
								filledEtList.add(fillEdEt);
							}
							// 如果集合有数据，则重新组装
							if (!filledEtList.isEmpty()) {
								map.put(Constants.COLLECTION, filledEtList);
								return map;
							}
						}
					}
				} else {
					tableInfo = TableInfoHelper.getTableInfo(parameterObject.getClass());
				}
				return populateKeys(metaObjectHandler, tableInfo, ms, parameterObject, isInsert);
			}
		}
		return parameterObject;
	}

	/**
	 * 处理正常批量插入逻辑
	 * <p>
	 * org.apache.ibatis.session.defaults.DefaultSqlSession$StrictMap 该类方法
	 * wrapCollection 实现 StrictMap 封装逻辑
	 * </p>
	 *
	 * @param parameter 插入数据库对象
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected static Collection<Object> getParameters(Object parameter) {
		Collection<Object> parameters = null;
		if (parameter instanceof Collection) {
			parameters = (Collection) parameter;
		} else if (parameter instanceof Map) {
			Map parameterMap = (Map) parameter;
			if (parameterMap.containsKey("collection")) {
				parameters = (Collection) parameterMap.get("collection");
			} else if (parameterMap.containsKey("list")) {
				parameters = (List) parameterMap.get("list");
			} else if (parameterMap.containsKey("array")) {
				parameters = Arrays.asList((Object[]) parameterMap.get("array"));
			}
		}
		return parameters;
	}

	/**
	 * 自定义元对象填充控制器
	 *
	 * @param tableInfo                 数据库表反射信息
	 * @param ms                        MappedStatement
	 * @param parameterObject           插入数据库对象
	 * @return Object
	 */
	protected static Object populateKeys(MetaObjectHandler metaObjectHandler, TableInfo tableInfo,
										 MappedStatement ms, Object parameterObject, boolean isInsert) {
		if (null == tableInfo) {
			/* 不处理 */
			return parameterObject;
		}
		/* 自定义元对象填充控制器 */
		MetaObject metaObject = ms.getConfiguration().newMetaObject(parameterObject);
		// 填充主键
		if (isInsert && !StringUtils.isEmpty(tableInfo.getKeyProperty())
			&& null != tableInfo.getIdType() && tableInfo.getIdType().getKey() >= 3) {
			Object idValue = metaObject.getValue(tableInfo.getKeyProperty());
			/* 自定义 ID */
			if (StringUtils.checkValNull(idValue)) {
				if (tableInfo.getIdType() == IdType.ID_WORKER) {
					metaObject.setValue(tableInfo.getKeyProperty(), IdWorker.getId());
				} else if (tableInfo.getIdType() == IdType.ID_WORKER_STR) {
					metaObject.setValue(tableInfo.getKeyProperty(), IdWorker.getIdStr());
				} else if (tableInfo.getIdType() == IdType.UUID) {
					metaObject.setValue(tableInfo.getKeyProperty(), IdWorker.get32UUID());
				}
			}
		}
		if (metaObjectHandler != null) {
			if (isInsert && metaObjectHandler.openInsertFill()) {
				// 插入填充
				metaObjectHandler.insertFill(metaObject);
			} else if (!isInsert) {
				// 更新填充
				metaObjectHandler.updateFill(metaObject);
			}
		}
		return metaObject.getOriginalObject();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setParameters(PreparedStatement ps) {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else {
						MetaObject metaObject = configuration.newMetaObject(parameterObject);
						value = metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					JdbcType jdbcType = parameterMapping.getJdbcType();
					if (value == null && jdbcType == null) {
						jdbcType = configuration.getJdbcTypeForNull();
					}
					try {
						typeHandler.setParameter(ps, i + 1, value, jdbcType);
					} catch (TypeException | SQLException e) {
						throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
					}
				}
			}
		}
	}
}
