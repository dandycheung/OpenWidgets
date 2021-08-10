package ru.fazziclay.openwidgets.net;

import java.io.*;
import java.net.Socket;

public abstract class Client extends Thread {
    private static final int MAX_SEND_PACKET_TIMEOUT = 5;

    String host;
    int port;

    Socket client;
    BufferedReader in;
    BufferedWriter out = null;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        start();
    }


    @Override
    public void run() {
        try {
            client = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

            onConnected();
            while (true) {
                String response = in.readLine();

                if (response == null) {
                    return;
                }

                response = response.replace("\\n", "\n");

                int packetId;
                String data = "";
                try {
                    packetId = Integer.parseInt("" + response.charAt(0) + response.charAt(1) + response.charAt(2) + response.charAt(3));
                    if (response.length() > 4) data = response.substring(4);
                } catch (Exception e) {
                    client.close();
                    return;
                }

                if (packetId == Packets.PACKET_DISCONNECTED) {
                    onDisconnected(data);
                    client.close();
                    return;
                }

                onPacketReceive(packetId, data);
            }

        } catch (IOException e) {
            onError(e);
        }
    }

    public abstract void onPacketReceive(int packetId, String data);

    public abstract void onDisconnected(String reason);

    public abstract void onConnected();

    public abstract void onError(Exception exception);


    public void send(int packetId, String msg) {
        final boolean[] isSent = {false};
        long startTime = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            try {
                out.write(Packets.toStringPacket(packetId) + msg.replace("\n", "\\n") + "\n");
                out.flush();
            } catch (IOException ignored) {}
            isSent[0] = true;
        });
        thread.start();
        while (!isSent[0]) {
            if (System.currentTimeMillis() - startTime > MAX_SEND_PACKET_TIMEOUT*1000) break;
        }
    }
}