package ru.fazziclay.openwidgets.util.base.network;

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
        this.connectionHandler.onPreDisconnected(this);
        this.socket.close();
        this.inputStream.close();
        this.outputStream.close();
        this.connectionHandler.onDisconnected(this);
    }

    public boolean isClosed() {
        return this.socket.isClosed();
    }

    public void send(String data) {
        this.outputStream.write(data.replace("\\", "\\\\").replace("\n", "\\n") + "\n");
        this.outputStream.flush();
    }

    public void run() {
        try {
            this.connectionHandler.onPreConnected(this);
            if (this.socket == null) {
                this.socket = new Socket(this.host, this.port);
            }

            this.socket.setSoTimeout(this.soTimeOut);
            if (this.inputStream == null) {
                this.inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            }

            if (this.outputStream == null) {
                this.outputStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())));
            }

            this.connectionHandler.onConnected(this);
        } catch (Exception var3) {
            this.connectionHandler.onException(this, var3);
            return;
        }

        while(this.socket.isConnected()) {
            try {
                String received = this.inputStream.readLine();
                if (received == null) {
                    break;
                }

                received = received.replace("\\n", "\n").replace("\\\\", "\\");
                this.connectionHandler.onPacketReceive(this, received);
            } catch (IOException var4) {
                if (!(var4 instanceof SocketException) || !var4.getMessage().equals("Socket closed")) {
                    this.connectionHandler.onException(this, var4);
                }
                break;
            }
        }

        try {
            this.close();
        } catch (Exception var2) {
            this.connectionHandler.onException(this, var2);
        }
    }
}
