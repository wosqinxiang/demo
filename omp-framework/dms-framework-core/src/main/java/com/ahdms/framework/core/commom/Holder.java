package com.ahdms.framework.core.commom;
/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
public class Holder<T> {
    private T object;

    public Holder(){}

    public Holder(T object) {
        this.object = object;
    }

    public T get() {
        return this.object;
    }

    public void set(T object) {
        this.object = object;
    }

    public boolean isEmpty() {
        return null == this.object;
    }
}
