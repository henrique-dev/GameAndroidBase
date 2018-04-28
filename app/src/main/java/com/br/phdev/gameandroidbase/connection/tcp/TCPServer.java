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

import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Scanner;

public class TCPServer extends TCPConnection {

    private int port;
    private boolean ssAccepting;
    private ServerSocket serverSocket;
    private List<String> clientHosts;

    private boolean broadcastRunning;

    public TCPServer(int port, boolean autoConnect, List<String> clientHosts) {
        this.port = port;
        super.autoConnect = autoConnect;
        this.clientHosts = clientHosts;
    }

    @Override
    public void run() {
        try {
            if (super.autoConnect) {
                this.broadcastRunning = true;
                Thread broadcastReceiver = new Thread() {
                    @Override
                    public void run() {
                        try {
                            DatagramSocket socket = new DatagramSocket(5002);
                            while (broadcastRunning) {
                                byte[] buffer = new byte[24];
                                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                                socket.receive(packet);
                                GameLog.debug(this, "SERVER AUTOCONNECT: " + packet.getAddress().getHostAddress() + " MANDOU BROADCAST");
                                InetAddress address = packet.getAddress();
                                addClientHost(address.getHostAddress());
                                //int port = packet.getPort();
                                packet = new DatagramPacket(buffer, buffer.length, address, 5001);
                                socket.send(packet);
                            }
                            socket.close();
                            socket = null;
                        } catch (Exception e) {
                            GameLog.error(this, e);
                        }
                    }
                };
                broadcastReceiver.start();
            }

            this.serverSocket = new ServerSocket(this.port);
            this.ssAccepting = true;
            GameLog.debug(this, "AGUARDANDO CONEXÃO");
            super.socket = serverSocket.accept();

            this.broadcastRunning = false;

            super.onConnectStatusListener.onConnect();
            super.printWriter = new PrintWriter(super.socket.getOutputStream());
            super.scanner = new Scanner(super.socket.getInputStream());
            super.connected = true;
            String msg = "";
            while ((msg = super.scanner.nextLine()) != null) {
                if (msg.equals("EXIT"))
                    break;
                super.onReadListener.readTCP(msg);
            }
        } catch (Exception e) {
            if (!(e instanceof SocketException && !this.ssAccepting))
                GameLog.error(this, e);
        } finally {
            this.disconnect();
        }
    }

    private void addClientHost(String clientHost) {
        for (String host : this.clientHosts) {
            if (clientHost.equals(host))
                return;
        }
        this.clientHosts.add(clientHost);
    }

    @Override
    public void writeTCP(String msg) {
        GameLog.debug(this, "WRITE: " + msg);
        super.printWriter.write(msg);
        super.printWriter.flush();
    }

    @Override
    public Runnable connect() {
        return this;
    }

    @Override
    synchronized public void disconnect() {
        if (!super.connected && !this.ssAccepting)
            return;
        this.ssAccepting = false;
        try {
            if (super.printWriter != null)
                super.printWriter.close();
            if (super.scanner != null)
                super.scanner.close();
            if (super.socket != null && !this.socket.isClosed())
                    super.socket.close();
            if (this.serverSocket != null)
                this.serverSocket.close();
            super.onConnectStatusListener.onDisconnect();
        } catch (Exception e) {
            GameLog.error(this, e);
        }
        super.connected = false;
    }
}
