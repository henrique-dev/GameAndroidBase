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

import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;

/**
 * Interface responsavel por ações de clique, com especificação em soltar e pressionar.
 * @version 1.0
 */
public interface ClickListener extends ActionListener {

    int PRESSED_PERFORMED = 1;
    int RELEASE_PERFORMED = 2;

    /**
     * Metodo para quando o botão for pressionado.
     * @param event evento de quem despacha a ação.
     */
    void pressedPerformed(Event event);

    /**
     * Metodo para quando o botão for solto após pressionado.
     * @param event evento de quem despacha a ação.
     */
    void releasePerformed(Event event);

}
