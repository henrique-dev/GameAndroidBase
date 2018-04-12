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
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.models.WindowEntity;
import com.br.phdev.gameandroidbase.cmp.utils.Text;

/**
 * Classe responsavel pela criação de botões para janelas.
 * @version 1.0
 */
public class Button extends WindowEntity {

    /**
     * Efeito de quando clicado padrão do botão.
     */
    private int DEFAULT_CLICK_EFFECT = Effect.FLASHING;

    /**
     * Estado ativo do clicado do botão.
     */
    private boolean clicked = false;

    /**
     * Cria um botão em uma area.
     * @param x posição no eixo x do botão.
     * @param y posição no eixo y do botão.
     * @param width largura do botão.
     * @param height altura do botão.
     */
    public Button(int x, int y, int width, int height) {
        super(new Rect(x, y, x + width, y + height));
        this.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    /**
     * Cria um botão em uma area.
     * @param area area para o botão.
     */
    public Button(Rect area) {
        super(area);
        this.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    /**
     * Cria um botão contendo texto em uma area.
     * @param area area para o botão.
     * @param buttonText texto a ser exibido no botão.
     */
    public Button(Rect area, String buttonText) {
        super(area);
        super.entityText = new Text(this, buttonText);
        this.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    /**
     * Cria um botão contendo um texto em uma area.
     * @param area area para o botão.
     * @param buttonText {@link Text} para o botão.
     */
    public Button(Rect area, Text buttonText) {
        super(area);
        super.entityText = buttonText;
        this.changeClickEffect(DEFAULT_CLICK_EFFECT);
    }

    /**
     * Cria um botão contendo um texto.
     * Usado para colocar a entidade em um layout, e deixar ela definir a area para o botão.
     * @param textButton texto a ser exibido no botão.
     */
    public Button(String textButton) {
        super(new Rect());
        super.entityText = new Text(this, textButton);
        this.changeClickEffect(DEFAULT_CLICK_EFFECT);
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
     * Redefine o efeito de clique do botão com efeitos padrões.
     * @param clickEffect efeito de clique para o botão.
     */
    public void changeClickEffect(int clickEffect) {
        DEFAULT_CLICK_EFFECT = clickEffect;
        if (clickEffect == Effect.FADE_IN_OUT) {
            super.clickEffect = new FadeEffect(this, FadeEffect.FADE_OUT, new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    Button.this.fire(evt);
                    Button.this.clicked = false;
                }
            });
        } else if (clickEffect == Effect.FLASHING) {
            super.clickEffect = new FlashEffect(this, new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    Button.this.clicked = false;
                    Button.this.fire(evt);
                }
            });
        }
    }

    /**
     * Redefine o efeito de clique do botão com efeitos customizados.
     * @param effect efeito de clique para o botão.
     */
    public void setClickEffect(ClickEffect effect) {
        super.setClickEffect((Effect) effect);
        super.clickEffect.setEntity(this);
        super.clickEffect.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                Button.this.fire(evt);
                Button.this.clicked = false;
            }
        });
    }

    /**
     * Aciona as escutas do botão.
     * @param evt evento lançado.
     */
    private void fire(Event evt) {
        if (super.listener != null)
            ((ActionListener)listener).actionPerformed(evt);
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

    @Override
    public void update() {
        if (clickEffect != null)
            super.clickEffect.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        canvas.clipRect(super.area);
        canvas.drawRect(super.area, super.defaultPaint);
        if (super.entityText != null) {
            super.entityText.draw(canvas);
        }

        canvas.restoreToCount(savedState);
    }

    // private float px = 0, py = 0; // *ativar para arrastar o componente conforme o arraste do toque na tela.

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.clicked)
            return false;

        int action = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        if (haveCollision(x, y, this)) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    this.clicked = true;
                    super.clickEffect.start();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // move(this, x - px , y - py); // *ativar para arrastar o componente conforme o arraste do toque na tela.
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            // px = x; // *ativar para arrastar o componente conforme o arraste do toque na tela.
            // py = y; // *ativar para arrastar o componente conforme o arraste do toque na tela.
        }
        return true;
    }
}
