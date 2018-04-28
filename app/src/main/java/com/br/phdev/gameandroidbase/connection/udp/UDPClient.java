package com.br.phdev.gameandroidbase.connection.udp;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.connection.ConnectionConfiguration;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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
public class UDPClient extends UDPConnection{

    private int port;
    private String serverIP;
    private boolean running;

    public UDPClient(ConnectionConfiguration connectionConfiguration) {
        this.port = connectionConfiguration.getPort();
        this.serverIP = connectionConfiguration.getHostIP();
    }

    public UDPClient(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    public UDPClient(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            super.socket = new DatagramSocket();
            //super.socket.connect(InetAddress.getByName(this.serverIP), this.port);
            super.socket = new DatagramSocket(this.port);
            this.running = true;
            this.connected = true;
            //super.onConnectStatusListener.onConnect();
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, 0, 1024);

            while(this.running) {
                super.socket.receive(packet);
                String msg = new String(packet.getData());
                if (msg.trim().equals("EXIT")) {
                    writeUDP("EXIT");
                    this.running = false;
                    break;
                }
                super.onReadListener.readUDP(msg.trim());
                super.onReadListener.readUDP(packet.getData());
            }
        } catch (Exception e) {
            GameLog.error(this, e);
        } finally {
            this.disconnect();
        }
    }

    @Override
    public void writeUDP(String msg) {
        try {
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName(this.serverIP), this.port);
            super.socket.send(packet);
        } catch (IOException e) {
            GameLog.error(this, e);
        }
    }

    @Override
    public void writeUDP(byte[] buffer) {
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(this.serverIP), this.port);
            super.socket.send(packet);
        } catch (IOException e) {
            GameLog.error(this, e);
        }
    }

    @Override
    public Runnable connect() {
        return null;
    }

    @Override
    synchronized public void disconnect() {
        if (!super.connected)
            return;
        this.running = false;
        try {
            if (super.socket != null)
                super.socket.close();
            if (super.onConnectStatusListener != null)
                super.onConnectStatusListener.onDisconnect();
        } catch (Exception e) {
            GameLog.error(this, e);
        }
    }
}
