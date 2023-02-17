package com.c0rdination.openwidgets.util.base.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public abstract class BaseServer extends Thread {
    ServerSocket serverSocket = null;
    int port = 0;
    int soTimeOut = 0;
    int backlog = 0;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public int getPort() {
        return port;
    }

    public int getSoTimeOut() {
        return soTimeOut;
    }

    public int getBacklog() {
        return backlog;
    }

    public abstract void onException(Exception var1);

    public abstract void onPreStarted();

    public abstract void onStarted(int var1);

    public abstract void onConnected(Socket var1);

    public abstract void onPreClosed();

    public abstract void onClosed();

    public boolean isClosed() {
        return serverSocket.isClosed();
    }

    public BaseServer(int port, int soTimeOut, int backlog) {
        this.port = port;
        this.soTimeOut = soTimeOut;
        this.backlog = backlog;
    }

    public void close() throws IOException {
        onPreClosed();
        serverSocket.close();
        onClosed();
    }

    public void run() {
        onPreStarted();

        try {
            serverSocket = new ServerSocket(port, backlog);
            serverSocket.setSoTimeout(soTimeOut);
            onStarted(serverSocket.getLocalPort());
        } catch (Exception var3) {
            onException(var3);
            return;
        }

        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                onConnected(socket);
            } catch (Exception var4) {
                if (var4 instanceof SocketException && var4.getMessage().equals("Socket closed"))
                    break;

                onException(var4);
            }
        }

        try {
            close();
        } catch (Exception var2) {
            onException(var2);
        }
    }
}
