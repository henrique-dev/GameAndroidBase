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
package com.br.phdev.gameandroidbase.cmp.window;

import android.graphics.Color;
import android.graphics.Rect;

import com.br.phdev.gameandroidbase.cmp.WindowEntity;
import com.br.phdev.gameandroidbase.cmp.devices.Keyboard;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.KeyboardListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.listeners.events.KeyboardEvent;
import com.br.phdev.gameandroidbase.cmp.utils.Text;

public class TextField extends WindowEntity implements KeyboardListener {

    private Keyboard keyboard;

    public TextField() {
        super(new Rect(), new Text(""));
        super.defaultPaint.setColor(Color.WHITE);
    }

    public TextField(String text) {
        super(new Rect(), new Text(text));
        super.defaultPaint.setColor(Color.WHITE);
    }

    public void setKeyboard(final Keyboard keyboard) {
        super.addListener(0, new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                keyboard.registerListener(TextField.this);
                keyboard.setKeyboardOn(true);
            }
        });
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        if (keyboardEvent.keyCode == KeyboardEvent.KEY_BACKSPACE) {
            String currentText = super.entityText.toString();
            if (currentText.length() > 0)
                super.entityText.setText(currentText.substring(0, currentText.length()-1));
        } else{
            String currentText = super.entityText.toString();
            super.entityText.setText(currentText + Keyboard.getChar(keyboardEvent.keyCode));
        }
    }

    public void addActionListener(ActionListener actionListener) {
        super.addListener(actionListener);
    }

}
