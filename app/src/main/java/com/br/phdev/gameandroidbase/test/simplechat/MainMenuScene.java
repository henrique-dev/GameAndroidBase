package com.br.phdev.gameandroidbase.test.simplechat;

import android.graphics.Color;

import com.br.phdev.gameandroidbase.Scene;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.window.Button;
import com.br.phdev.gameandroidbase.cmp.window.ListLayout;
import com.br.phdev.gameandroidbase.cmp.window.Window;

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
public class MainMenuScene extends Scene {

    private Button button_host;
    private Button button_client;

    public MainMenuScene(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void init() {
        Window window = new Window(getX(), getY(), getWidth(), getHeight());
        window.setLayout(new ListLayout(ListLayout.VERTICAL_ALIGNMENT));
        float defaultTextSize = getWidth() / 10;

        button_host = new Button("Servidor");
        button_host.setTextSize(defaultTextSize);
        button_host.setColor(Color.GREEN);
        button_host.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                getParentBoard().safeAddScene(new InputMenuScene(getX(), getY(), getWidth(), getHeight(), InputMenuScene.HOST));
                setVisible(false);
                setActive(false);
            }
        });
        window.add(button_host);

        button_client = new Button("Cliente");
        button_client.setTextSize(defaultTextSize);
        button_client.setColor(Color.GREEN);
        button_client.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                getParentBoard().safeAddScene(new InputMenuScene(getX(), getY(), getWidth(), getHeight(), InputMenuScene.CLIENT));
                setActive(false);
                setVisible(false);
            }
        });
        window.add(button_client);

        add(window);

        setActive(true);
        setVisible(true);
    }

    @Override
    public void finalizeScene() {

    }
}
