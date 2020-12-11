package com.ahdms.framework.stream.client;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/16 15:38
 */
public class BindingMetadataHolder {

    private List<BindingMetadata> bindingMetadatas = new ArrayList<>();

    public void addBindingMetadata(String prefix, String group, String bindingName) {
        this.bindingMetadatas.add(new BindingMetadata(prefix, group, bindingName));
    }

    public List<BindingMetadata> getBindingMetadatas() {
        return bindingMetadatas;
    }

    public static class BindingMetadata {
        private String prefix;
        private String group;
        private String bindingName;

        public BindingMetadata(String prefix, String group, String bindingName) {
            this.prefix = prefix;
            this.group = group;
            this.bindingName = bindingName;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getGroup() {
            return group;
        }

        public String getBindingName() {
            return bindingName;
        }
    }
}
