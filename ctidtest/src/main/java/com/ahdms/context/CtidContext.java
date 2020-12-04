package com.ahdms.context;

import com.ahdms.model.User;
import lombok.Builder;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-04 14:40
 */
@Data
@Builder
public class CtidContext {

    private User user;
    private String ip;

}
