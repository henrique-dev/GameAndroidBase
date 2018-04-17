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
package com.br.phdev.gameandroidbase.cmp.listeners;

import com.br.phdev.gameandroidbase.cmp.listeners.events.KeyboardEvent;
import com.br.phdev.gameandroidbase.cmp.devices.Keyboard;

/**
 * Interface implementada por entidades que queiram receber entrada de dados pelo {@link Keyboard}.
 */
public interface KeyboardListener extends Listener {

    /**
     * Quando uma tecla do toclado é pressionada.
     * @param keyboardEvent keycode da tecla pressionada.
     */
    void keyPressed(KeyboardEvent keyboardEvent);

}
