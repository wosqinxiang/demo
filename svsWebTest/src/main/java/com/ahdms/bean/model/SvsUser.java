package com.ahdms.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2021-01-03 9:52
 */
@TableName("svs_user")
@Data
public class SvsUser {

    @TableId
    private String id;
    private String account;
    private String info;
    private String whiteIp;
    private String svsConfigId;

}
