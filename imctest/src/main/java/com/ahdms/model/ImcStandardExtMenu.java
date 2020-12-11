package com.ahdms.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author qinxiang ImcTempalteExtensionChoose
 * @date 2020-12-11 14:06
 */
@Data
@TableName("imc_standard_ext_menu")
public class ImcStandardExtMenu {

    @TableId
    private Integer id;

    private String extKey;
    private String name;
    private String oid;
    private Integer hasChild;
    private Integer parentId;
    private Integer isCritical;
}
