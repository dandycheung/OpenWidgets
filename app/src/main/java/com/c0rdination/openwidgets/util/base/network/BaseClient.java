package com.c0rdination.openwidgets.util.base.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class BaseClient extends Thread {
    protected Socket socket = null;
    protected String host = "localhost";
    protected int port = 0;
    protected int soTimeOut = 0;
    protected BaseConnectionHandler connectionHandler;
    protected BufferedReader inputStream;
    protected PrintWriter outputStream;

    public BaseClient(String host, int port, int soTimeOut, BaseConnectionHandler connectionHandler) {
        this.host = host;
        this.port = port;
        this.soTimeOut = soTimeOut;
        this.connectionHandler = connectionHandler;
    }

    public void close() throws IOException {
        connectionHandler.onPreDisconnected(this);
        socket.close();
        inputStream.close();
        outputStream.close();
        connectionHandler.onDisconnected(this);
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void send(String data) {
        outputStream.write(data.replace("\\", "\\\\").replace("\n", "\\n") + "\n");
        outputStream.flush();
    }

    public void run() {
        try {
            connectionHandler.onPreConnected(this);
            if (socket == null)
                socket = new Socket(host, port);

            socket.setSoTimeout(soTimeOut);
            if (inputStream == null)
                inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (outputStream == null)
                outputStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            connectionHandler.onConnected(this);
        } catch (Exception var3) {
            connectionHandler.onException(this, var3);
            return;
        }

        while (socket.isConnected()) {
            try {
                String received = inputStream.readLine();
                if (received == null)
                    break;

                received = received.replace("\\n", "\n").replace("\\\\", "\\");
                connectionHandler.onPacketReceive(this, received);
            } catch (IOException var4) {
                if (!(var4 instanceof SocketException) || !var4.getMessage().equals("Socket closed"))
                    connectionHandler.onException(this, var4);

                break;
            }
        }

        try {
            close();
        } catch (Exception var2) {
            connectionHandler.onException(this, var2);
        }
    }
}
