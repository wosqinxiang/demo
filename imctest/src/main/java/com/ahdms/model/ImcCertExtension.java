package com.ahdms.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author qinxiang ImcStandardExtMenu
 * @date 2020-12-11 14:05
 */
@TableName("imc_cert_extension")
@Data
public class ImcCertExtension {

    @TableId
    private Integer id;

    private String name;
    private String label;
    private String oid;
    private String description;
    private Integer isDefault;
    private Integer isRequired;

}
