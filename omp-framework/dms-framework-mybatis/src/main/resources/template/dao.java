package ${currentPackage};

import org.apache.ibatis.annotations.Mapper;
import com.ahdms.framework.mybatis.mapper.IMapper;
import ${config.entityPackage}.${upperModelName};

/**
 * <B>说明：${dbTableInfo.comment}</B><BR>
 *
 * @author ${config.author}
 * @version 1.0.0.
 * @date ${date}
 */
@Mapper
public interface I${upperModelName}${fileSuffix} extends IMapper<${upperModelName}> {
}
