package com.br.phdev.gameandroidbase.connection.udp;

import com.br.phdev.gameandroidbase.connection.listeners.OnConnectStatusListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnReadTCPListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnReadUDPListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnWriteTCPListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnWriteUDPListener;

import java.net.DatagramSocket;

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
public abstract class UDPConnection implements Runnable, OnWriteUDPListener {

    DatagramSocket socket;

    protected boolean connected;

    protected OnReadUDPListener onReadListener;
    protected OnConnectStatusListener onConnectStatusListener;

    public void setOnConnectionStatusListener(OnConnectStatusListener onConnectListener) {
        this.onConnectStatusListener = onConnectListener;
    }

    public void setOnReadListener(OnReadUDPListener onReadListener) {
        this.onReadListener = onReadListener;
    }

    public OnWriteUDPListener getOnWriteListener() {
        return this;
    }

    public boolean isConnected() {
        return connected;
    }

    public abstract Runnable connect();

    public abstract void disconnect();

}
