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

import com.br.phdev.gameandroidbase.connection.Connection;
import com.br.phdev.gameandroidbase.connection.tcp.TCPConnection;
import com.br.phdev.gameandroidbase.connection.ConnectionConfiguration;
import com.br.phdev.gameandroidbase.connection.OnConnectStatusListener;
import com.br.phdev.gameandroidbase.connection.OnConnectionReadListener;
import com.br.phdev.gameandroidbase.connection.OnConnectionWriteListener;
import com.br.phdev.gameandroidbase.connection.tcp.TCPClient;
import com.br.phdev.gameandroidbase.connection.tcp.TCPServer;
import com.br.phdev.gameandroidbase.connection.udp.UDPClient;
import com.br.phdev.gameandroidbase.connection.udp.UDPServer;

/**
 * Classe responsavel por conexões do jogo.
 */
public class ConnectionManager {

    private Thread connectionThread;
    private Connection connection;

    public void set(ConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration.getType() == ConnectionConfiguration.TCP) {
            if (connectionConfiguration.isServer()) {
                this.connection = new TCPServer(connectionConfiguration);
            } else {
                this.connection = new TCPClient(connectionConfiguration);
            }
        } else if (connectionConfiguration.getType() == ConnectionConfiguration.UDP) {
            if (connectionConfiguration.isServer()) {
                this.connection = new UDPServer(connectionConfiguration);
            } else {
                this.connection = new UDPClient(connectionConfiguration);
            }
        }

    }

    public OnConnectionWriteListener setOnConnectReadListener(OnConnectionReadListener onConnectReadListener) {
        this.connection.setOnConnectReadListener(onConnectReadListener);
        return this.connection;
    }

    public void setOnConnectiotStatusListener(OnConnectStatusListener onConnectListener) {
        this.connection.setOnConnectionStatusListener(onConnectListener);
    }

    public void connect() {
        this.connectionThread = new Thread(this.connection);
        this.connectionThread.start();
    }

    public void disconnect() {
        if (this.connection != null) {
            this.connection.disconnect();
            try {
                this.connectionThread.join();
            } catch (Exception e) {
                GameLog.error(this, e);
            }
        }
    }

}
