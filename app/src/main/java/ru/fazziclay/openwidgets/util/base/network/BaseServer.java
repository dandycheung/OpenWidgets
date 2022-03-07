package ru.fazziclay.openwidgets.util.base.network;

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
        return this.serverSocket;
    }

    public int getPort() {
        return this.port;
    }

    public int getSoTimeOut() {
        return this.soTimeOut;
    }

    public int getBacklog() {
        return this.backlog;
    }

    public abstract void onException(Exception var1);

    public abstract void onPreStarted();

    public abstract void onStarted(int var1);

    public abstract void onConnected(Socket var1);

    public abstract void onPreClosed();

    public abstract void onClosed();

    public boolean isClosed() {
        return this.serverSocket.isClosed();
    }

    public BaseServer(int port, int soTimeOut, int backlog) {
        this.port = port;
        this.soTimeOut = soTimeOut;
        this.backlog = backlog;
    }

    public void close() throws IOException {
        this.onPreClosed();
        this.serverSocket.close();
        this.onClosed();
    }

    public void run() {
        this.onPreStarted();

        try {
            this.serverSocket = new ServerSocket(this.port, this.backlog);
            this.serverSocket.setSoTimeout(this.soTimeOut);
            this.onStarted(this.serverSocket.getLocalPort());
        } catch (Exception var3) {
            this.onException(var3);
            return;
        }

        while(!this.serverSocket.isClosed()) {
            try {
                Socket socket = this.serverSocket.accept();
                this.onConnected(socket);
            } catch (Exception var4) {
                if (var4 instanceof SocketException && var4.getMessage().equals("Socket closed")) {
                    break;
                }

                this.onException(var4);
            }
        }

        try {
            this.close();
        } catch (Exception var2) {
            this.onException(var2);
        }

    }
}
