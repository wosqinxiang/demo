package com.ahdms.framework.mybatis.handler;

import com.ahdms.framework.core.commom.util.DateUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * 自定义扩展 LocalDate 的处理器
 *
 * <p>
 * 目标： 不处理时区
 * java    LocalDate
 * mysql date
 * </p>
 *
 * @author Katrel.zhou
 */
public class DmsLocalDateTypeHandler extends LocalDateTypeHandler {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, DateUtils.formatDateStr(parameter));
    }
}
