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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.cmp.effect.ClickEffect;
import com.br.phdev.gameandroidbase.cmp.effect.Effect;
import com.br.phdev.gameandroidbase.cmp.effect.FadeEffect;
import com.br.phdev.gameandroidbase.cmp.effect.FlashEffect;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.ClickListener;
import com.br.phdev.gameandroidbase.cmp.listeners.Listener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.models.WindowEntity;
import com.br.phdev.gameandroidbase.cmp.utils.Text;

/**
 * Classe responsavel pela criação de botões para janelas.
 * @version 1.0
 */
public class Button extends WindowEntity {

    /**
     * Cria um botão em uma area.
     * @param x posição no eixo x do botão.
     * @param y posição no eixo y do botão.
     * @param width largura do botão.
     * @param height altura do botão.
     */
    public Button(int x, int y, int width, int height) {
        super(new Rect(x, y, x + width, y + height));
        super.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    /**
     * Cria um botão em uma area.
     * @param area area para o botão.
     */
    public Button(Rect area) {
        super(area);
        super.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    /**
     * Cria um botão contendo texto em uma area.
     * @param area area para o botão.
     * @param buttonText texto a ser exibido no botão.
     */
    public Button(Rect area, String buttonText) {
        super(area);
        super.entityText = new Text(this, buttonText);
        super.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    /**
     * Cria um botão contendo um texto em uma area.
     * @param area area para o botão.
     * @param buttonText {@link Text} para o botão.
     */
    public Button(Rect area, Text buttonText) {
        super(area);
        super.entityText = buttonText;
        super.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    /**
     * Cria um botão contendo um texto.
     * Usado para colocar a entidade em um layout, e deixar ela definir a area para o botão.
     * @param textButton texto a ser exibido no botão.
     */
    public Button(String textButton) {
        super(new Rect());
        super.entityText = new Text(this, textButton);
        super.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    @Override
    public void setArea(Rect area) {
        super.setArea(area);
        if (super.entityText != null) {
            super.entityText.setArea(new Rect(area));
        }
    }

    /**
     * Redefine o texto a ser exibido no botão.
     * @param text texto a ser exibido.
     */
    public void setText(String text) {
        super.entityText.setText(text);
    }

    /**
     * Retorna a {@link String} do texto do botão.
     * @return {@link String} do texto.
     */
    public String getText() {
        return super.entityText.toString();
    }

    /**
     * Redefine o tamanho da fonte do texto a ser exibido no botão.
     * @param size tamanho da fonte.
     */
    public void setTextSize(float size) {
        super.entityText.setTextSize(size);
        //super.entityText.setTextSizeAdjusted(false);
    }

    /**
     * Redefine a cor do botão.
     * @param color cor para o botao.
     */
    @Deprecated
    public void setColor(int color) {
        super.defaultPaint.setColor(color);
    }

    /**
     * Retorna a cor atual do botão.
     * @return cor do botão.
     */
    @Deprecated
    public int getColor() {
        return super.defaultPaint.getColor();
    }

    /**
     * Redefine a escuta para o botão.
     * @param listener nova escuta pra o botão.
     */
    public void addActionListener(ActionListener listener) {
        super.addListener(listener);
    }

    public void addClickListener(ClickListener listener) {
        super.addListener(listener);
    }
}
