package ${currentPackage};

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <B>说明：${dbTableInfo.comment} 控制器</B><BR>
 *
 * @author ${config.author}
 * @version 1.0.0
 * @since ${date}
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/${controllerMappingHyphen}")
@Api("${dbTableInfo.comment} 控制器")
public class ${upperModelName}Controller {
    @Autowired
    private I${upperModelName}Service ${lowerModelName}Service;
}
