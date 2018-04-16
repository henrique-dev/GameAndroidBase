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

import android.graphics.Canvas;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;

/**
 * Classe responsavel pela criação de efeitos do tipo fade-in / fade-out.
 * @version 1.0
 */
public class FadeEffect extends Effect implements ClickEffect {

    public static final int FADE_IN = 1;
    public static final int FADE_OUT = 2;

    /**
     * Estado que define o tipo do efeito.
     */
    private boolean fadeIn;

    /**
     * Estado que define o tipo do efeito.
     */
    private boolean fadeOut;

    /**
     * Taxa de variação do atributo de transparencia.
     */
    private int speed;

    /**
     * Valores originais para serem usados como ponto de restauração depois do efeito.
     */
    private int originalAlpha, originalType;

    /**
     * Cria o efeito do tipo fade, podendo ser de saida ou de entrada.
     */
    public FadeEffect() {
        super();
        this.speed = 20;
        this.fadeOut = true;
        this.originalType = FADE_OUT;
    }

    /**
     * Cria o efeito do tipo fade, podendo ser de saida ou de entrada.
     * @param entity entidade para ser aplicado o efeito.
     * @param fadeType subtipo de efeito.
     * @param actionListener escuta para o evento para ser executado apos o efeito.
     */
    public FadeEffect(Entity entity, int fadeType, ActionListener actionListener) {
        super(entity, actionListener);
        //this.originalAlpha = entity.getDefaultPaint().getAlpha();
        this.speed = 20;
        this.originalType = fadeType;
        if (fadeType == FADE_IN) {
            this.fadeIn = true;
            entity.getDefaultPaint().setAlpha(0);
            this.originalAlpha = 255;
        }
        else if (fadeType == FADE_OUT) {
            entity.getDefaultPaint().setAlpha(255);
            this.originalAlpha = 0;
            this.fadeOut = true;
        }
    }

    /**
     * Redefine a velocidade do efeito.
     * @param speed velocidade do efeito. (1 ate 8).
     */
    public void setSpeed(int speed) {
        if (speed <= 0)
            this.speed = 255 / 2;
        else if (speed > 8)
            this.speed = 255 / 9;
        else
            this.speed = 255 / speed + 1;
    }

    @Override
    public void setActionListener(ActionListener listener) {
        super.setActionListener(listener);
    }

    @Override
    public void setEntity(Entity entity) {
        super.setEntity(entity);
        this.originalAlpha = entity.getDefaultPaint().getAlpha();
    }

    @Override
    public void update() {
        if (super.running) {
            if (this.fadeIn) {
                int alpha = super.entity.getDefaultPaint().getAlpha();
                alpha += this.speed;
                if (alpha > 255) {
                    alpha = 255;
                    super.actionListener.actionPerformed(super.event);
                    super.entity.getDefaultPaint().setAlpha(alpha);
                    this.stop();
                } else
                    super.entity.getDefaultPaint().setAlpha(alpha);
            } else if (this.fadeOut) {
                int alpha = super.entity.getDefaultPaint().getAlpha();
                alpha -= this.speed;
                if (alpha < 0) {
                    alpha = 0;
                    super.actionListener.actionPerformed(super.event);
                    super.entity.getDefaultPaint().setAlpha(alpha);
                    this.stop();
                } else
                    super.entity.getDefaultPaint().setAlpha(alpha);
            }
            GameLog.error(this, "ATUALIZANDO");
        }
    }

    @Override
    public void stop() {
        this.running = false;
        super.entity.getDefaultPaint().setAlpha(this.originalAlpha);
        if (originalType == FADE_IN) {
            this.fadeOut = false;
            this.fadeIn = true;
        } else {
            this.fadeIn = false;
            this.fadeOut = true;
        }
        super.event = null;
    }

}
