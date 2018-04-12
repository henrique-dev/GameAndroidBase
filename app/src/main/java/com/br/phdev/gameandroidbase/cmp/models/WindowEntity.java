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
package com.br.phdev.gameandroidbase.cmp.models;

import android.graphics.Rect;

import com.br.phdev.gameandroidbase.cmp.effect.Effect;
import com.br.phdev.gameandroidbase.cmp.listeners.Listener;
import com.br.phdev.gameandroidbase.cmp.utils.Text;

/**
 * Classe base responsavel por todas as entidades que sejam objetos da janela como menus, botões, etc.
 * @version 1.0
 */
public abstract class WindowEntity extends Entity {

    /**
     * Efeito de clique da entidade.
     */
    protected Effect clickEffect;

    /**
     * Efeito de animação/loop da entidade.
     */
    protected Effect loopEffect;

    /**
     * Escuta da entidade para acionar eventos.
     */
    protected Listener listener;

    /**
     * Texto a ser vinculado com a entidade.
     */
    protected Text entityText;

    /**
     * Cria uma entidade para janela.
     */
    protected WindowEntity() {
        super();
    }

    /**
     * Cria uma entidade para janela.
     * @param area area da entidade.
     */
    protected WindowEntity(Rect area) {
        super(area);
    }

    /**
     * Redefine o efeito de clique associado a entidade.
     * @param effect efeito de clique para a entidade.
     */
    protected void setClickEffect(Effect effect) {
        this.clickEffect = effect;
    }

    /**
     * Redefine o efeito de loop/animação associado a entidade.
     * @param effect efeito de loop para a entidade.
     */
    protected void setLoopEffect(Effect effect) {
        this.loopEffect = effect;
    }

    /**
     * Retorna o {@link Text} vinculado a entidade.
     * @return {@link Text} da entidade.
     */
    public Text getEntityText() {
        return this.entityText;
    }

    /**
     * Redefine o {@link Text} vinculado a entidade.
     * @param entityText {@link Text} para entidade.
     */
    public void setEntityText(Text entityText) {
        this.entityText = entityText;
    }

    /**
     * Redefine a escuta de eventos da entidade.
     * @param listener escuta para entidade.
     */
    protected void addListener(Listener listener) {
        this.listener = listener;
    }
}
