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
package com.br.phdev.gameandroidbase;

import com.br.phdev.gameandroidbase.cmp.devices.Keyboard;

/**
 * Gerenciador de dispositivos variados do jogo.
 * Existentes: teclado.
 */
public class DeviceManager {

    /**
     * Teclado utilizado para entrada de dados no jogo.
     */
    private Keyboard keyboard;

    /**
     * Cria o gerenciador.
     */
    DeviceManager() {
        this.keyboard = new Keyboard();
    }

    /**
     * Inicializa o teclado.
     * Apenas sua dimensão é criada no construtor.
     * Para usa-lo, deve-se chamar esta função antes.
     */
    public void initKeyboard() {
        this.keyboard.loadComponents();
    }

    /**
     * Retorna o teclado.
     * @return teclado do jogo.
     */
    public Keyboard getKeyboard() {
        return this.keyboard;
    }

}
