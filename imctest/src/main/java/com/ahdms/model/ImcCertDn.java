package com.ahdms.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author qinxiang  ImcCertExtension
 * @date 2020-12-11 14:02
 */
@TableName("imc_cert_dn")
@Data
public class ImcCertDn {
    @TableId
    private Integer id;
    private String name;
    private Integer isRequired;
    private String defaultValue;
    private String description;

}
