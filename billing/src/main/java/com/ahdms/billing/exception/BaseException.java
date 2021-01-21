package com.ahdms.billing.exception;

/**
 * Created by yeqiang on 11/8/16.
 */
public class BaseException extends Exception {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 6274519668110285359L;

    public BaseException(Object error) {
        super(error.toString());
    }
}
