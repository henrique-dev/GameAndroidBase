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
package com.br.phdev.gameandroidbase.cmp.effect;

import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;

/**
 * Classe pai de todas as classes que forneçam efeitos visuais para entidades.
 * @version 1.0
 */
public abstract class Effect implements Animation {

    public static final int FADE_IN_OUT = 0;
    public static final int FLASHING = 1;

    /**
     * Escuta para eventos que ocoreem após o efeito.
     */
    protected ActionListener actionListener;

    protected Event event;

    /**
     * Entidade que consome o efeito.
     */
    protected Entity entity;

    /**
     * Estado do efeito.
     */
    protected boolean running;

    /**
     * Cria um novo efeito.
     */
    protected Effect() {

    }

    /**
     * Cria um novo efeito.
     * @param entity entidade que ira consumir o efeito.
     * @param actionListener escuta contendo os eventos que ocorrerão após o efeito.
     */
    protected Effect(Entity entity, ActionListener actionListener) {
        if (entity == null)
            throw new Error("A entidade não pode ser nula.");
        this.entity = entity;
        this.actionListener = actionListener;
    }

    /**
     * Redefine a entidade para ser aplicado o efeito.
     * @param entity entidade do efeito.
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Redefine a escuta para o evento a ser executado apos o efeito.
     * @param actionListener escuta contendo o evento.
     */
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * Inicia o efeito.
     */
    public void start(Event event) {
        this.running = true;
        this.event = event;
    }

    /**
     * Encerra o efeito e reseta seus atributos.
     */
    public abstract void stop();

}