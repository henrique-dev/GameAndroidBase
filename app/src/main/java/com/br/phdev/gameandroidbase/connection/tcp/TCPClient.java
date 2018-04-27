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
package com.br.phdev.gameandroidbase.connection.tcp;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.connection.ConnectionConfiguration;
import com.br.phdev.gameandroidbase.connection.OnDiscoveryAndConnectListener;

import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Enumeration;
import java.util.Scanner;

public class TCPClient extends TCPConnection {

    private int port;
    private String host;
    private boolean sConnecting;

    private OnDiscoveryAndConnectListener onDiscoveryAndConnectListener;

    public TCPClient(ConnectionConfiguration connectionConfiguration) {
        this.host = connectionConfiguration.getHostIP();
        this.port = connectionConfiguration.getPort();
        this.onDiscoveryAndConnectListener = connectionConfiguration.getOnDiscoveryAndConnectListener();
    }

    @Override
    public void run() {
        try {
            this.sConnecting = true;

            super.socket = new Socket();

            if (this.onDiscoveryAndConnectListener != null) {
                /*
                NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                InetAddress host[] = new InetAddress[2];
                {
                    int counter = 0;
                    while (addresses.hasMoreElements()) {
                        host[counter] = addresses.nextElement();
                        counter++;
                    }
                }

                GameLog.debug(this, "IP: " + host[1].getHostAddress());
                GameLog.debug(this, "Subnet Mask ipv4: " + networkInterface.getInterfaceAddresses().get(1).getNetworkPrefixLength());

                int cidr = networkInterface.getInterfaceAddresses().get(1).getNetworkPrefixLength();

                if (cidr == 24) {
                    StringBuilder stringBuilder = new StringBuilder();
                    int counter = 0;
                    for (char c : host[1].getHostAddress().toCharArray()) {
                        if (counter == 3)
                            break;
                        if (c == '.')
                            counter++;
                        stringBuilder.append(c);
                    }

                    String networkip = stringBuilder.toString();
                    GameLog.debug(this, "Network IP: " + networkip);

                    for (int i = 1; i < 255; i++) {
                        try {
                            String ip = networkip + "." + i;
                            GameLog.debug(this, "Tentando conectar no ip: " + ip);
                            super.socket = new Socket();
                            super.socket.connect(new InetSocketAddress(ip, 5000), 250);
                            if (super.socket.isConnected()) {
                                break;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
                */



                String networkip = "192.168.2";
                for (int i = 1; i < 255; i++) {
                    try {
                        String ip = networkip + "." + i;
                        GameLog.debug(this, "Tentando conectar no ip: " + ip);
                        super.socket = new Socket();
                        super.socket.connect(new InetSocketAddress(ip, 5000), 250);
                        if (super.socket.isConnected())
                            break;
                        Thread.sleep(2000);
                    } catch (Exception e) {

                    }
                }
                if (socket.isConnected()) {
                    GameLog.debug(this, "CONECTOU");
                    this.onDiscoveryAndConnectListener.onConnect();
                }
                else
                    return;
            } else {
                SocketAddress socketAddress = new InetSocketAddress(host, port);
                super.socket.connect(socketAddress);
                super.onConnectListener.onConnect();
            }

            super.printWriter = new PrintWriter(super.socket.getOutputStream());
            super.scanner = new Scanner(super.socket.getInputStream());
            super.connected = true;
            String msg = "";
            while ((msg = super.scanner.nextLine()) != null) {
                if (msg.equals("exit"))
                    break;
                super.onConnectReadListener.read(msg);
            }
        } catch (Exception e) {
            GameLog.error(this, e);
        } finally {
            this.disconnect();
        }
    }

    @Override
    public void write(String msg) {
        super.printWriter.write(msg);
        super.printWriter.flush();
    }

    @Override
    public Runnable connect() {
        GameLog.debug(this, "CONECTOU");
        return this;
    }

    @Override
    synchronized public void disconnect() {
        if (!super.connected && !this.sConnecting)
            return;
        GameLog.debugr(this, "DESCONECTANDO");
        this.sConnecting = false;
        try {
            if (super.printWriter != null)
                super.printWriter.close();
            if (super.scanner != null)
                super.scanner.close();
            if (super.socket != null && !this.socket.isClosed())
                super.socket.close();
            super.socket = null;
            super.onConnectListener.onDisconnect();
        } catch (Exception e) {
            GameLog.error(this, e);
        }
        super.connected = false;
    }
}
