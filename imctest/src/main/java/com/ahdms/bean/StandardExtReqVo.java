package com.ahdms.bean;

import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 16:02
 */
@Data
public class StandardExtReqVo {

    private Integer id;
    private String extKey;
    private String name;
    private Integer criticalValue;
    private String childValues;
}
