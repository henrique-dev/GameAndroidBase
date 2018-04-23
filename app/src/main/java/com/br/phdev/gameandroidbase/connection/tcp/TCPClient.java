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
import com.br.phdev.gameandroidbase.connection.Connection;
import com.br.phdev.gameandroidbase.connection.ConnectionConfiguration;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Connection {

    private int port;
    private String host;

    public TCPClient(ConnectionConfiguration connectionConfiguration) {
        this.host = connectionConfiguration.getHostIP();
        this.port = connectionConfiguration.getPort();
    }

    @Override
    public void run() {
        try {
            super.socket = new Socket(this.host, this.port);
            super.onConnectListener.onConnect();
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
        return this;
    }

    @Override
    synchronized public void disconnect() {
        GameLog.debugr(this, super.connected + " HERE");

        if (!super.connected)
            return;
        try {
            if (super.printWriter != null)
                super.printWriter.close();
            if (super.scanner != null)
                super.scanner.close();
            if (super.socket != null)
                super.socket.close();
            super.onConnectListener.onDisconnect();
        } catch (Exception e) {
            GameLog.error(this, e);
        }
        super.connected = false;
    }
}
