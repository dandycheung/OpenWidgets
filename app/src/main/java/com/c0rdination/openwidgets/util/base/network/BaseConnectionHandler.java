package com.c0rdination.openwidgets.util.base.network;

public abstract class BaseConnectionHandler {
    public BaseConnectionHandler() {
    }

    public abstract void onException(BaseClient var1, Exception var2);

    public abstract void onPreConnected(BaseClient var1);

    public abstract void onConnected(BaseClient var1);

    public abstract void onPacketReceive(BaseClient var1, String var2);

    public abstract void onPreDisconnected(BaseClient var1);

    public abstract void onDisconnected(BaseClient var1);
}
