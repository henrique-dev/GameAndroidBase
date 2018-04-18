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

import com.br.phdev.gameandroidbase.cmp.devices.Keyboard;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.KeyboardListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.listeners.events.KeyboardEvent;
import com.br.phdev.gameandroidbase.cmp.utils.Text;

/**
 * Classe responsavel por criação de campos de texto, para receber alguma entrada de dados.
 */
public class TextField extends WindowEntity implements KeyboardListener {

    /**
     * Instancia do teclado do jogo.
     */
    private Keyboard keyboard;

    /**
     * Cria um campo de texto.
     * Usado para colocar a entidade em um layout, e deixar ela definir a area para o campo de texto.
     */
    public TextField() {
        super(new Rect(), new Text(""));
        super.defaultPaint.setColor(Color.WHITE);
    }

    /**
     * Cria um campo de texto contendo um texto.
     * Usado para colocar a entidade em um layout, e deixar ela definir a area para o campo de texto.
     * @param text
     */
    public TextField(String text) {
        super(new Rect(), new Text(text));
        super.defaultPaint.setColor(Color.WHITE);
    }

    public String getText() {
        return entityText.toString();
    }

    public void setText(String text) {
        this.entityText.setText(text);
    }

    /**
     * Pega e registra a instancia do teclado do jogo.
     * @param keyboard
     */
    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
        super.addListener(0, new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                TextField.this.keyboard.registerListener(TextField.this);
                TextField.this.keyboard.setKeyboardOn(true);
            }
        });
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        int keyCode = keyboardEvent.keyCode;
        if (keyCode == KeyboardEvent.KEY_BACKSPACE) {
            String currentText = super.entityText.toString();
            if (currentText.length() > 0)
                super.entityText.setText(currentText.substring(0, currentText.length()-1));
        } else{
            String currentText = super.entityText.toString();
            super.entityText.setText(currentText + Keyboard.getChar(keyCode));
        }
    }

    /**
     * Redefine a escuta para eventos de ação para o campo de texto..
     * @param actionListener nova escuta pra o campo de texto.
     */
    public void addActionListener(ActionListener actionListener) {
        super.addListener(actionListener);
    }

}
