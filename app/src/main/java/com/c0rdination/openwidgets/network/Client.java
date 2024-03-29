package com.c0rdination.openwidgets.network;

import com.c0rdination.openwidgets.AppConfig;
import com.c0rdination.openwidgets.Logger;
import com.c0rdination.openwidgets.util.ByteUtils;
import com.c0rdination.openwidgets.util.base.network.BaseClient;
import com.c0rdination.openwidgets.util.base.network.BaseConnectionHandler;

public class Client extends BaseClient {
    public static String[] host = null;
    public static Client client = null;

    public static void connectToServer() {
        try {
            if (host == null)
                host = AppConfig.SERVER_HOST.split(":"); // InternetUtils.parseTextPage(AppConfig.SERVER_HOST_URL).split(":");

            if (client == null || client.socket == null || client.isClosed()) {
                client = new Client(host[0], Integer.parseInt(host[1]), 0, new ClientConnectionHandler());
                client.start();
            }
        } catch (Exception e) {
            final Logger LOGGER = new Logger();
            LOGGER.error(e);
        }
    }

    public Client(String host, int port, int soTimeOut, BaseConnectionHandler connectionHandler) {
        super(host, port, soTimeOut, connectionHandler);
    }

    public void send(ServerPacket packetId, String data) {
        if (data == null)
            data = "";

        data = new String(ByteUtils.getBytes(packetId.getPacketId())) + data;
        super.send(data);
    }
}
