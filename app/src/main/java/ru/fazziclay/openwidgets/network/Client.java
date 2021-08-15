package ru.fazziclay.openwidgets.network;

import ru.fazziclay.fazziclaylibs.ByteUtils;
import ru.fazziclay.fazziclaylibs.network.BaseClient;
import ru.fazziclay.fazziclaylibs.network.BaseConnectionHandler;
import ru.fazziclay.openwidgets.Logger;

public class Client extends BaseClient {
    public static final String SERVER_HOST_URL = "https://raw.githubusercontent.com/FazziCLAY/OpenWidgets/dev/1.4.2/server.host";

    public static String[] host = null;
    public static Client client = null;

    public static void connectToServer() {
        final Logger LOGGER = new Logger();

        try {
            // TODO: 15.08.2021 change hardcoded to parse from github
            if (host == null) host = new String[]{"192.168.43.207", "4003"};//host = InternetUtils.parseTextPage(SERVER_HOST_URL).split(":");
            if (client == null || client.socket == null || client.isClosed()) {
                client = new Client(host[0], Integer.parseInt(host[1]), 0, new ClientConnectionHandler());
                client.start();
                LOGGER.log("No connected before. Connecting...");
            } else {
                LOGGER.info("Before connected.");
            }
        } catch (Exception e) {
            LOGGER.error("Error for parse host | connecting to host. e="+e.toString());
        }

        LOGGER.done();
    }

    public Client(String host, int port, int soTimeOut, BaseConnectionHandler connectionHandler) {
        super(host, port, soTimeOut, connectionHandler);
    }

    public void send(ServerPacket packetId, String data) {
        if (data == null) data = "";
        data = new String(ByteUtils.getBytes(packetId.getPacketId())) + data;
        super.send(data);
    }
}