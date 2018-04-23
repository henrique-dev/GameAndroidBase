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
package com.br.phdev.gameandroidbase.connection;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public abstract class Connection implements Runnable, OnConnectionWriteListener {

    protected Socket socket;

    protected PrintWriter printWriter;
    protected Scanner scanner;

    protected boolean connected;

    protected OnConnectReadListener onConnectReadListener;
    protected OnConnectStatusListener onConnectListener;

    public void setOnConnectListener(OnConnectStatusListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }

    public void setOnConnectReadListener(OnConnectReadListener onConnectReadListener) {
        this.onConnectReadListener = onConnectReadListener;
    }

    public OnConnectReadListener getOnConnectReadListener() {
        return this.onConnectReadListener;
    }

    public abstract Runnable connect();

    public abstract void disconnect();

}
