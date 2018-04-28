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

import com.br.phdev.gameandroidbase.connection.listeners.OnConnectStatusListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnReadTCPListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnReadUDPListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnServerDiscovery;
import com.br.phdev.gameandroidbase.connection.listeners.OnWriteTCPListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnWriteUDPListener;
import com.br.phdev.gameandroidbase.connection.tcp.TCPClient;
import com.br.phdev.gameandroidbase.connection.tcp.TCPConnection;
import com.br.phdev.gameandroidbase.connection.tcp.TCPServer;
import com.br.phdev.gameandroidbase.connection.udp.UDPClient;
import com.br.phdev.gameandroidbase.connection.udp.UDPConnection;
import com.br.phdev.gameandroidbase.connection.udp.UDPServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsavel por conexões do jogo.
 */
public class ConnectionManager {

    private Thread connectionThreadTCP;
    private Thread connectionThreadUDP;

    private String serverHost;
    private List<String> clientHosts = new ArrayList<>();

    private TCPConnection tcpConnection;
    private UDPConnection udpConnection;

    /**
     * TCP
     */

    public void createClientTCPConnection(String serverHost, int port) {
        this.serverHost = serverHost;
        this.tcpConnection = new TCPClient(serverHost, port);
    }

    public void createClientTCPConnection(int port, int attempts) {
        this.tcpConnection = new TCPClient(port, attempts, this.onServerDiscovery);
    }

    public void createServerTCPConnection(int port, boolean autoConnect) {
        this.tcpConnection = new TCPServer(port, autoConnect, this.clientHosts);
    }

    public OnWriteTCPListener getOnWriteTCPListener() {
        return this.tcpConnection;
    }

    public void setOnReadTCPListener(OnReadTCPListener onReadTCPListener) {
        this.tcpConnection.setOnReadListener(onReadTCPListener);
    }

    public void setOnConnectStatusTCPListener(OnConnectStatusListener onConnectStatusListener) {
        this.tcpConnection.setOnConnectionStatusListener(onConnectStatusListener);
    }

    /**
     * UDP
     */

    public void createClientUDPConnection(int port) {
        //String host = ((TCPClient)tcpConnection).getServerIP();
        //GameLog.debug(this, "CRIANDO CLIENT UDP -> SERVER IP: " + host + " // porta: " + port);
        this.udpConnection = new UDPClient(this.serverHost, port);
    }

    public void createServerUDPConnection(int port) {
        //GameLog.debug(this, "CRIANDO SERVER UDP -> CLIENTE IP: " + host + " // porta: " + port);
        this.udpConnection = new UDPServer(port, this.clientHosts);
    }

    public OnWriteUDPListener getOnWriteUDPListener() {
        return this.udpConnection;
    }

    public void setOnReadUDPListener(OnReadUDPListener onReadUDPListener) {
        this.udpConnection.setOnReadListener(onReadUDPListener);
    }

    public void setOnConnectStatusUDPListener(OnConnectStatusListener onConnectStatusListener) {
        this.udpConnection.setOnConnectionStatusListener(onConnectStatusListener);
    }

    public void connectTCP() {
        this.connectionThreadTCP = new Thread(this.tcpConnection);
        this.connectionThreadTCP.start();
    }

    public void disconnectTCP() {
        if (this.tcpConnection != null) {
            this.tcpConnection.disconnect();
            try {
                this.connectionThreadTCP.join();
            } catch (Exception e) {
                GameLog.error(this, e);
            }
        }

    }

    public void connectUDP() {
        this.connectionThreadUDP = new Thread(this.udpConnection);
        this.connectionThreadUDP.start();
    }

    public void disconnectUDP() {
        if (this.udpConnection != null) {
            this.udpConnection.disconnect();
            try {
                this.connectionThreadUDP.join();
            } catch (Exception e) {
                GameLog.error(this, e);
            }
        }
    }

    public boolean isServer() {
        if (tcpConnection != null)
            return (tcpConnection instanceof TCPServer);
        else
            return (udpConnection instanceof UDPServer);
    }

    private OnServerDiscovery onServerDiscovery = new OnServerDiscovery() {

        @Override
        public void onDiscovery(String host) {
            serverHost = host;
        }
    };

}
