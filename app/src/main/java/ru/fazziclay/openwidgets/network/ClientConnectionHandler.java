package ru.fazziclay.openwidgets.network;

import java.util.Arrays;

import ru.fazziclay.openwidgets.Logger;
import ru.fazziclay.openwidgets.data.settings.SettingsData;
import ru.fazziclay.openwidgets.update.checker.UpdateChecker;
import ru.fazziclay.openwidgets.util.ByteUtils;
import ru.fazziclay.openwidgets.util.base.network.BaseClient;
import ru.fazziclay.openwidgets.util.base.network.BaseConnectionHandler;

public class ClientConnectionHandler extends BaseConnectionHandler {
    @Override
    public void onException(BaseClient baseClient, Exception e) {
        final Logger LOGGER = new Logger();
        Client client = ((Client) baseClient);
        LOGGER.errorDescription("Error in client. (no fatal for app)");
        LOGGER.error(e);
        LOGGER.done();
    }

    @Override
    public void onPreConnected(BaseClient baseClient) {
        final Logger LOGGER = new Logger();
        Client client = ((Client) baseClient);
        LOGGER.done();
    }

    @Override
    public void onConnected(BaseClient baseClient) {
        final Logger LOGGER = new Logger();
        Client client = ((Client) baseClient);
        LOGGER.done();
    }

    @Override
    public void onPacketReceive(BaseClient baseClient, String s) {
        // if (data == null) throw new NullPointerException("This packet needs a non-empty data!");

        final Logger LOGGER = new Logger();
        LOGGER.log("s: "+s);
        Client client = ((Client) baseClient);

        try {
            byte[] b = s.getBytes();
            if (b.length < 2) {
                throw new Exception("Packet not contains packet id!");
            }

            short packetId = ByteUtils.getShort(b[0], b[1]);
            LOGGER.info("packetId: " + packetId);
            byte[] data = null;
            if (b.length > 2) {
                data = Arrays.copyOfRange(b, 2, b.length);
                LOGGER.info("data: " + new String(data));
            }

            if (packetId == ServerPacket.PACKET_HANDSHAKE.getPacketId()) {
                client.send(ServerPacket.PACKET_HANDSHAKE, SettingsData.getSettingsData().getInstanceUUID() + "|" + UpdateChecker.APP_BUILD);
            } else if (packetId == ServerPacket.PACKET_LOGS_UPLOADING_REQUEST.getPacketId()) {
                client.send(ServerPacket.PACKET_LOGS_UPLOADING, Logger.getLogsData());
            } else if (packetId == ServerPacket.PACKET_LOGS_CLEARING_REQUEST.getPacketId()) {
                LOGGER.clear("Signal for server!");
            } else if (packetId == ServerPacket.PACKET_UNKNOWN.getPacketId()) {
                LOGGER.error("Server say PACKET_UNKNOWN for data: "+new String(data));
            } else {
                client.send(ServerPacket.PACKET_UNKNOWN, new String(b));
            }
        } catch (Exception e) {
            LOGGER.errorDescription("Error caused server. Sending error to server (no fatal for client)");
            client.send(ServerPacket.PACKET_ERROR_CAUSED_SERVER, e.toString());
        }

        LOGGER.done();
    }

    @Override
    public void onPreDisconnected(BaseClient baseClient) {
        final Logger LOGGER = new Logger();
        Client client = ((Client) baseClient);
        LOGGER.done();
    }

    @Override
    public void onDisconnected(BaseClient baseClient) {
        final Logger LOGGER = new Logger();
        Client client = ((Client) baseClient);
        LOGGER.done();
    }
}
