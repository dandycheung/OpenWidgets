package ru.fazziclay.openwidgets.net;

public class Packets {
    public static final int PACKET_DISCONNECTED = 0;
    public static final int PACKET_HANDSHAKE = 1;
    public static final int PACKET_LOGS_UPLOADING = 2;
    public static final int PACKET_LOGS_UPLOADING_SUCCESSES = 3;
    public static final int PACKET_LOGS_UPLOADING_ERROR = 4;
    public static final int PACKET_LOGS_CLEARING_REQUEST = 5;

    public static String toStringPacket(int packet) {
        if (packet > 9999) {
            return "000"+PACKET_DISCONNECTED+"BUG FROM Packets.toStringPacket(): packet > 9999(max supported digits)";
        }

        if (packet < 0) {
            return "000"+PACKET_DISCONNECTED+"BUG FROM Packets.toStringPacket(): packet < 0(min supported digits)";
        }

        int length = String.valueOf(packet).length();
        StringBuilder prefix = new StringBuilder();

        if (length == 3) {
            prefix.append("0");
        }

        if (length == 2) {
            prefix.append("00");
        }

        if (length == 1) {
            prefix.append("000");
        }

        return prefix.toString()+packet;
    }
}
