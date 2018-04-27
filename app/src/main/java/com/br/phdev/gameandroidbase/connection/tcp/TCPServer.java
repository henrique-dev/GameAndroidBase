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

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

public class TCPServer extends TCPConnection {

    private int port;
    private boolean ssAccepting;
    private ServerSocket serverSocket;

    public TCPServer(ConnectionConfiguration connectionConfiguration) {
        this.port = connectionConfiguration.getPort();
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.ssAccepting = true;
            GameLog.debug(this, "AGUARDANDO CONEXÃO");
            super.socket = serverSocket.accept();
            super.onConnectListener.onConnect();
            super.printWriter = new PrintWriter(super.socket.getOutputStream());
            super.scanner = new Scanner(super.socket.getInputStream());
            super.connected = true;
            String msg = "";
            while ((msg = super.scanner.nextLine()) != null) {
                if (msg.equals("EXIT"))
                    break;
                super.onConnectReadListener.read(msg);
            }
        } catch (Exception e) {
            if (!(e instanceof SocketException && !this.ssAccepting))
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
            super.onConnectListener.onDisconnect();
        } catch (Exception e) {
            GameLog.error(this, e);
        }
        super.connected = false;
    }
}
