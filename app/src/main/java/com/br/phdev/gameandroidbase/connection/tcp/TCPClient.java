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
import com.br.phdev.gameandroidbase.connection.listeners.OnServerDiscovery;

import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class TCPClient extends TCPConnection {

    private int port;
    private OnServerDiscovery onServerDiscovery;
    private String serverIP;
    private boolean sConnecting;

    private int attempts;
    private boolean serverFound = false;
    private DatagramSocket socketAutoDiscovery;

    public String getServerIP() {
        return serverIP;
    }

    public int getPort() {
        return port;
    }

    public TCPClient(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
        this.serverFound = !(super.autoConnect = false);
    }

    public TCPClient(int port, int attempts, OnServerDiscovery onServerDiscovery) {
        this.port = port;
        super.autoConnect = true;
        this.attempts = attempts;
        this.onServerDiscovery = onServerDiscovery;
    }

    @Override
    public void run() {
        try {
            this.sConnecting = true;

            if (super.autoConnect) {
                this.socketAutoDiscovery = new DatagramSocket(5001);
                Thread broadcastSender = new Thread() {
                    @Override
                    public void run() {
                        try {
                            socketAutoDiscovery.setBroadcast(true);
                            String broadcastMsg = "PengaPong";
                            NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
                            InetAddress broadcastAddress = networkInterface.getInterfaceAddresses().get(1).getBroadcast();
                            int currentAttempt = 0;
                            while ((currentAttempt++ < TCPClient.this.attempts) && !serverFound) {
                                socketAutoDiscovery.send(new DatagramPacket(broadcastMsg.getBytes(), broadcastMsg.getBytes().length, broadcastAddress, 5002));
                                sleep(100);
                            }
                        } catch (Exception e) {
                            GameLog.error(this, e);
                        }
                    }
                };
                Thread waitingServerSignal = new Thread() {
                    @Override
                    public void run() {
                        try {
                            //socket.setBroadcast(false);
                            byte[] buffer = new byte[24];
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                            socketAutoDiscovery.receive(packet);
                            GameLog.debug(this, "SERVER AUTOCONNECT: " + packet.getAddress().getHostAddress() + " SERVIDOR ENCONTRADO -> " + packet.getAddress().getHostAddress());
                            serverFound = true;
                            InetAddress address = packet.getAddress();
                            serverIP = address.getHostAddress();
                            onServerDiscovery.onDiscovery(serverIP);
                        } catch (Exception e) {
                            GameLog.error(this, e);
                        }
                    }
                };

                broadcastSender.start();
                waitingServerSignal.start();
                broadcastSender.join();
                socketAutoDiscovery.close();
                socketAutoDiscovery = null;
            }

            if (serverFound) {
                super.socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(serverIP, port);
                super.socket.connect(socketAddress);
                super.onConnectStatusListener.onConnect();

                super.printWriter = new PrintWriter(super.socket.getOutputStream());
                super.scanner = new Scanner(super.socket.getInputStream());
                super.connected = true;
                String msg = "";
                while ((msg = super.scanner.nextLine()) != null) {
                    if (msg.equals("exit"))
                        break;
                    super.onReadListener.readTCP(msg);
                }
            }
            GameLog.debug(this, "NÃO CONECTOU");
        } catch (Exception e) {
            GameLog.error(this, e);
        } finally {
            this.disconnect();
        }

    }

    @Override
    public void writeTCP(String msg) {
        GameLog.debug(this, "WRITE: " + msg);
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
        if (!super.connected && !this.sConnecting) {
            GameLog.debugr(this, "JA DESCONECTOU");
            return;
        }
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
            super.onConnectStatusListener.onDisconnect();
        } catch (Exception e) {
            GameLog.error(this, e);
        }
        super.connected = false;
    }
}
