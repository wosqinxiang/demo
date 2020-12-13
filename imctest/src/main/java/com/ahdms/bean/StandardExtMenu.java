package com.ahdms.bean;

import com.ahdms.framework.core.commom.util.JsonUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-10 16:10
 */
@Data
public class StandardExtMenu {

    private Integer id;
    private String extKey;
    private String name;
    private String oid;
    private Integer hasChild;
    private Integer parentId;
    private Integer isCritical;
    private Integer criticalValue;

    private Integer KeyValue;
    private List<StandardExtMenu> childMenu;

}
