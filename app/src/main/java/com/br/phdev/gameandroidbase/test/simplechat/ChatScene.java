package com.br.phdev.gameandroidbase.test.simplechat;

import android.graphics.Color;

import com.br.phdev.gameandroidbase.Scene;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.window.Button;
import com.br.phdev.gameandroidbase.cmp.window.Label;
import com.br.phdev.gameandroidbase.cmp.window.ListLayout;
import com.br.phdev.gameandroidbase.cmp.window.TextField;
import com.br.phdev.gameandroidbase.cmp.window.Window;
import com.br.phdev.gameandroidbase.connection.ConnectionConfiguration;
import com.br.phdev.gameandroidbase.connection.listeners.OnConnectStatusListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnReadTCPListener;
import com.br.phdev.gameandroidbase.connection.listeners.OnWriteTCPListener;

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
public class ChatScene extends Scene implements OnReadTCPListener, OnConnectStatusListener {

    private ConnectionConfiguration cc;

    private Label label_receivedmsg;
    private Label label_sendmsg;

    private TextField textField_receivedmsg;
    private TextField textField_sendmsg;

    private Button button_send;

    Window connectedWindow;
    Window notconnectedWindow;

    private OnWriteTCPListener onConnectionWriteListener;

    public ChatScene(float x, float y, float width, float height, ConnectionConfiguration cc) {
        super(x, y, width, height);
        this.cc = cc;
    }

    @Override
    public void init() {
        connectedWindow = new Window(getX(), getY(), getWidth(), getHeight());
        connectedWindow.setLayout(new ListLayout(ListLayout.VERTICAL_ALIGNMENT));
        float defaultTextSize = getWidth() / 14;
        connectedWindow.getDefaultPaint().setColor(Color.WHITE);
        getDeviceManager().initKeyboard();

        //getConnectionManager().set(cc);
        //this.onConnectionWriteListener = getConnectionManager().setOnReadListenerTCP(this);
        //getConnectionManager().setOnConnectionStatusListenerTCP(this);
        getConnectionManager().connectTCP();

        label_receivedmsg = new Label("Mensagem recebida:");
        label_receivedmsg.setTextSize(defaultTextSize);
        connectedWindow.add(label_receivedmsg);

        textField_receivedmsg = new TextField();
        textField_receivedmsg.setTextSize(defaultTextSize);
        connectedWindow.add(textField_receivedmsg);

        label_sendmsg = new Label("Mensagem a enviar:");
        label_sendmsg.setTextSize(defaultTextSize);
        connectedWindow.add(label_sendmsg);

        textField_sendmsg = new TextField();
        textField_sendmsg.setTextSize(defaultTextSize);
        textField_sendmsg.setKeyboard(getDeviceManager().getKeyboard());
        connectedWindow.add(textField_sendmsg);

        connectedWindow.add(new Label());

        button_send = new Button("Enviar mensagem");
        button_send.setTextSize(defaultTextSize);
        button_send.setColor(Color.GREEN);
        button_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                String msg = textField_sendmsg.getText();
                onConnectionWriteListener.writeTCP(msg);
            }
        });
        connectedWindow.add(button_send);

        connectedWindow.setVisible(false);
        connectedWindow.setActive(false);
        add(connectedWindow);



        notconnectedWindow = new Window(getX(), getY(), getWidth(), getHeight());
        notconnectedWindow.setLayout(new ListLayout(ListLayout.VERTICAL_ALIGNMENT));
        notconnectedWindow.getDefaultPaint().setColor(Color.WHITE);

        Label loadingLabel;
        if (cc.isServer())
            loadingLabel = new Label("Esperando alguem conectar...");
        else
            loadingLabel = new Label("Conectando...");
        loadingLabel.setTextSize(defaultTextSize);
        notconnectedWindow.add(loadingLabel);

        add(notconnectedWindow);


        setActive(true);
        setVisible(true);
    }

    @Override
    public void finalizeScene() {

    }

    @Override
    public void readTCP(String msg) {
        textField_receivedmsg.setText(msg);
    }

    @Override
    public void onConnect() {
        connectedWindow.setVisible(true);
        connectedWindow.setActive(true);
        notconnectedWindow.setVisible(false);
        notconnectedWindow.setActive(false);
    }

    @Override
    public void onDisconnect() {
        new SimpleChatBoard(getX(), getY(), getWidth(), getHeight());
    }
}
