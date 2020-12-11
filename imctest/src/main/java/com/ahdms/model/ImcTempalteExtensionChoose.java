package com.ahdms.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 14:06
 */
@Data
@TableName("imc_tempalte_extension_choose")
public class ImcTempalteExtensionChoose {

    @TableId
    private Integer id;

    private String templateId;
    private Integer extType;
    private String extKey;
    private Integer criticalValue;
    private Integer clientValue;
    private Integer isServer;
    private String serverValue;
    private Integer calculation;
    private String childValues;

}
