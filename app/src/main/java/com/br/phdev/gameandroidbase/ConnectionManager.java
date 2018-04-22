/*
 * Copyright (C) 2018 Paulo Henrique Gonçalves Bacelar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.br.phdev.gameandroidbase;

import com.br.phdev.gameandroidbase.cmp.listeners.OnConnectListener;
import com.br.phdev.gameandroidbase.cmp.listeners.OnConnectReadListener;
import com.br.phdev.gameandroidbase.cmp.listeners.OnConnectionWriteListener;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe responsavel por conexões do jogo.
 */
public class ConnectionManager {

    public enum ConnectionType {HOST, CLIENT}

    private Connection connection;

    private OnConnectReadListener onConnectReadListener;


    public ConnectionManager(ConnectionType connectionType) {
        if (connectionType == ConnectionType.HOST) {
            this.connection = new Server();
        } else {
            this.connection = new Client();
        }
    }

    public OnConnectionWriteListener setOnConnectReadListener(OnConnectReadListener onConnectReadListener) {
        this.onConnectReadListener = onConnectReadListener;
        return connection;
    }

    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.connection.setOnConnectListener(onConnectListener);
    }

    public void start() {
        Thread connectionThread;
        connectionThread = new Thread(this.connection);
        connectionThread.start();
    }

    private abstract class Connection implements OnConnectionWriteListener, Runnable {

        OnConnectListener onConnectListener;
        void setOnConnectListener(OnConnectListener onConnectListener) {
            this.onConnectListener = onConnectListener;
        }
    }

    private class Server extends Connection {

        private ServerSocket serverSocket;
        private Socket socket;
        private int port = 5000;

        private PrintWriter printWriter;
        private Scanner scanner;

        @Override
        public void run() {
            try {
                this.serverSocket = new ServerSocket(port);
                GameLog.debug(this, "AGUARDANDO CONEXÃO");
                this.socket = this.serverSocket.accept();
                super.onConnectListener.onConnect();
                this.printWriter = new PrintWriter(this.socket.getOutputStream());
                this.scanner = new Scanner(this.socket.getInputStream());
                String msg = "";
                while ((msg = this.scanner.nextLine()) != null) {
                    ConnectionManager.this.onConnectReadListener.read(msg);
                }

            } catch (Exception e) {
                GameLog.error(this, e);
            }
        }

        @Override
        public void write(String msg) {
            this.printWriter.write(msg);
            this.printWriter.flush();
        }
    }

    private class Client extends Connection {

        private Socket socket;
        private int port = 5000;
        private String host = "192.168.2.115";

        private PrintWriter printWriter;
        private Scanner scanner;

        @Override
        public void run() {
            try {
                this.socket = new Socket(host, port);
                super.onConnectListener.onConnect();
                this.printWriter = new PrintWriter(this.socket.getOutputStream());
                this.scanner = new Scanner(this.socket.getInputStream());
                String msg = "";
                while ((msg = scanner.nextLine()) != null) {
                    ConnectionManager.this.onConnectReadListener.read(msg);
                }
            } catch (Exception e) {
                GameLog.error(this, e);
            }
        }

        @Override
        public void write(String msg) {
            this.printWriter.write(msg);
            this.printWriter.flush();
        }
    }

}
