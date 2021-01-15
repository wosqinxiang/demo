package com.ahdms.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qinxiang
 * @date 2021-01-03 9:50
 */
@TableName("svs_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SvsConfig {

    @TableId
    private String id;
    private String ip;
    private Integer port;
    private Integer keyIndex;
    private String keyValue;
    private String serialNumber;
    private String encryptKey;

}
