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
public class InputMenuScene extends Scene {

    static final int HOST = 0;
    static final int CLIENT = 1;

    private int type;

    private Label label_port;
    private Label label_ip;
    private TextField textField_port;
    private TextField textField_ip;
    private Button button_send;

    public InputMenuScene(float x, float y, float width, float height, int type) {
        super(x, y, width, height);
        this.type = type;
    }

    @Override
    public void init() {
        float defaultTextSize = getWidth() / 14;
        Window window = new Window(getX(), getY(), getWidth(), getHeight());
        window.setLayout(new ListLayout(ListLayout.VERTICAL_ALIGNMENT));
        window.getDefaultPaint().setColor(Color.WHITE);
        getDeviceManager().initKeyboard();

        if (type == HOST) {
            label_port = new Label("Porta para o servidor:");
            label_port.setTextSize(defaultTextSize);
            window.add(label_port);

            textField_port = new TextField("5000");
            textField_port.setTextSize(defaultTextSize);
            textField_port.setKeyboard(getDeviceManager().getKeyboard());
            window.add(textField_port);

            window.add(new Label());
            window.add(new Label());
            window.add(new Label());

            button_send = new Button("Iniciar");
            button_send.setTextSize(defaultTextSize);
            button_send.setColor(Color.GREEN);
            button_send.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    int port = Integer.parseInt(textField_port.getText());
                    ConnectionConfiguration cc = new ConnectionConfiguration(port, ConnectionConfiguration.TCP);
                    getConnectionManager().set(cc);
                    getParentBoard().safeAddScene(new ChatScene(getX(), getY(), getWidth(), getHeight(), cc));
                    setVisible(false);
                    setActive(false);
                }
            });
            window.add(button_send);

        } else {
            label_ip = new Label("Ip do servidor:");
            label_ip.setTextSize(defaultTextSize);
            window.add(label_ip);

            textField_ip = new TextField("192.168.2.115");
            textField_ip.setTextSize(defaultTextSize);
            textField_ip.setKeyboard(getDeviceManager().getKeyboard());
            window.add(textField_ip);

            label_port = new Label("Porta do servidor:");
            label_port.setTextSize(defaultTextSize);
            window.add(label_port);

            textField_port = new TextField("5000");
            textField_port.setTextSize(defaultTextSize);
            textField_port.setKeyboard(getDeviceManager().getKeyboard());
            window.add(textField_port);

            window.add(new Label());

            button_send = new Button("Conectar");
            button_send.setTextSize(defaultTextSize);
            button_send.setColor(Color.GREEN);
            button_send.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    String ip = textField_ip.getText();
                    int port = Integer.parseInt(textField_port.getText());
                    ConnectionConfiguration cc = new ConnectionConfiguration(ip, port, ConnectionConfiguration.TCP);
                    getConnectionManager().set(cc);
                    getParentBoard().safeAddScene(new ChatScene(getX(), getY(), getWidth(), getHeight(), cc));
                    setVisible(false);
                    setActive(false);
                }
            });
            window.add(button_send);
        }
        add(window);
        setVisible(true);
        setActive(true);
    }

    @Override
    public void finalizeScene() {

    }
}
