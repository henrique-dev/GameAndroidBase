package com.br.phdev.gameandroidbase.connection;

import com.br.phdev.gameandroidbase.connection.tcp.TCPClient;
import com.br.phdev.gameandroidbase.connection.tcp.TCPConnection;
import com.br.phdev.gameandroidbase.connection.tcp.TCPServer;
import com.br.phdev.gameandroidbase.connection.udp.UDPClient;
import com.br.phdev.gameandroidbase.connection.udp.UDPConnection;
import com.br.phdev.gameandroidbase.connection.udp.UDPServer;

import java.util.ArrayList;
import java.util.List;

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
 */
public abstract class Connection {

    protected String myIP;
    protected int port;

    protected TCPConnection tcpConnection;
    protected UDPConnection udpConnection;

    public abstract Runnable connectUsingUDP();
    public abstract Runnable connectUsingTCP();

    public abstract TCPConnection getTcpConnection();

    public abstract UDPConnection getUdpConnection();

    public int getPort() {
        return port;
    }

}
