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
import com.br.phdev.gameandroidbase.connection.ConnectionConfiguration;
import com.br.phdev.gameandroidbase.connection.OnConnectStatusListener;
import com.br.phdev.gameandroidbase.connection.OnReadListener;
import com.br.phdev.gameandroidbase.connection.OnWriteListener;
import com.br.phdev.gameandroidbase.connection.tcp.TCPClient;
import com.br.phdev.gameandroidbase.connection.tcp.TCPServer;
import com.br.phdev.gameandroidbase.connection.udp.UDPClient;
import com.br.phdev.gameandroidbase.connection.udp.UDPServer;

/**
 * Classe responsavel por conexões do jogo.
 */
public class ConnectionManager {

    private Thread connectionThreadTCP;
    private Thread connectionThreadUDP;

    private Connection connectionTCP;
    private Connection connectionUDP;

    public void set(ConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration.getType() == ConnectionConfiguration.TCP) {
            if (connectionConfiguration.isServer()) {
                this.connectionTCP = new TCPServer(connectionConfiguration);
            } else {
                this.connectionTCP = new TCPClient(connectionConfiguration);
            }
        } else if (connectionConfiguration.getType() == ConnectionConfiguration.UDP) {
            if (connectionConfiguration.isServer()) {
                this.connectionUDP = new UDPServer(connectionConfiguration);
            } else {
                this.connectionUDP = new UDPClient(connectionConfiguration);
            }
        }

    }

    public OnWriteListener setOnReadListenerTCP(OnReadListener onReadListener) {
        this.connectionTCP.setOnConnectReadListener(onReadListener);
        return this.connectionTCP;
    }

    public void setOnConnectionStatusListenerTCP(OnConnectStatusListener onConnectStatusListener) {
        this.connectionTCP.setOnConnectionStatusListener(onConnectStatusListener);
    }

    public OnWriteListener setOnReadListenerUDP(OnReadListener onReadListener) {
        this.connectionUDP.setOnConnectReadListener(onReadListener);
        return this.connectionUDP;
    }

    public void setOnConnectionStatusListenerUDP(OnConnectStatusListener onConnectStatusListener) {
        this.connectionUDP.setOnConnectionStatusListener(onConnectStatusListener);
    }

    public OnWriteListener getOnReadListener() {
        return this.connectionTCP;
    }

    public void connectTCP() {
        this.connectionThreadTCP = new Thread(this.connectionTCP);
        this.connectionThreadTCP.start();
    }

    public void disconnectTCP() {
        if (this.connectionTCP != null) {
            this.connectionTCP.disconnect();
            try {
                this.connectionThreadTCP.join();
            } catch (Exception e) {
                GameLog.error(this, e);
            }
        }
    }

    public void connectUDP() {
        this.connectionThreadUDP = new Thread(this.connectionUDP);
        this.connectionThreadUDP.start();
    }

    public void disconnectUDP() {
        if (this.connectionUDP != null) {
            this.connectionUDP.disconnect();
            try {
                this.connectionThreadUDP.join();
            } catch (Exception e) {
                GameLog.error(this, e);
            }
        }
    }

    public boolean isServer() {
        if (connectionTCP != null)
            return (connectionTCP instanceof TCPServer);
        else
            return (connectionUDP instanceof UDPServer);
    }

}
