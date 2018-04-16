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
package com.br.phdev.gameandroidbase.cmp.listeners.events;

/**
 * Classe responsavel pela criação de eventos lançados pelo teclado.
 */
public class KeyboardEvent {

    public static final int KEY_A = 0;
    public static final int KEY_B = 1;
    public static final int KEY_C = 2;
    public static final int KEY_D = 3;
    public static final int KEY_E = 4;
    public static final int KEY_F = 5;
    public static final int KEY_G = 6;
    public static final int KEY_H = 7;
    public static final int KEY_I = 8;
    public static final int KEY_J = 9;
    public static final int KEY_K = 10;
    public static final int KEY_L = 11;
    public static final int KEY_M = 12;
    public static final int KEY_N = 13;
    public static final int KEY_O = 14;
    public static final int KEY_P = 15;
    public static final int KEY_Q = 16;
    public static final int KEY_R = 17;
    public static final int KEY_S = 18;
    public static final int KEY_T = 19;
    public static final int KEY_U = 20;
    public static final int KEY_V = 21;
    public static final int KEY_X = 22;
    public static final int KEY_W = 23;
    public static final int KEY_Y = 24;
    public static final int KEY_Z = 25;
    public static final int KEY_SPACE = 26;
    public static final int KEY_BACKSPACE = 27;

    /**
     * Keycode lançado pela tecla do teclado.
     */
    public final int keyCode;

    /**
     * Cria um novo evento de teclado.
     * @param keyCode keycode lançado pela tecla do teclado.
     */
    public KeyboardEvent(int keyCode) {
        this.keyCode = keyCode;
    }

}
