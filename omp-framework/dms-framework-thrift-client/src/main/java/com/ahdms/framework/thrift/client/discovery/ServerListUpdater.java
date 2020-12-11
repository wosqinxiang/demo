package com.ahdms.framework.thrift.client.discovery;

public interface ServerListUpdater {

    public interface UpdateAction {
        void doUpdate();
    }

    void start(UpdateAction updateAction);

    void stop();

}
