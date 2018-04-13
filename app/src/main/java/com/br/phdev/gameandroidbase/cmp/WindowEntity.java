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
package com.br.phdev.gameandroidbase.cmp;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.cmp.effect.ClickEffect;
import com.br.phdev.gameandroidbase.cmp.effect.Effect;
import com.br.phdev.gameandroidbase.cmp.effect.FadeEffect;
import com.br.phdev.gameandroidbase.cmp.effect.FlashEffect;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.ClickListener;
import com.br.phdev.gameandroidbase.cmp.listeners.Listener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.utils.Text;

import java.util.ArrayList;

/**
 * Classe base responsavel por todas as entidades que sejam objetos da janela como menus, botões, etc.
 * @version 1.0
 */
public abstract class WindowEntity extends Entity {

    protected final int ACTION_TYPE_ON_CLICK = 0;
    protected final int ACTION_TYPE_ON_RELEASE = 1;

    /**
     * Efeito de quando clicado padrão do botão.
     */
    protected int DEFAULT_CLICK_EFFECT = Effect.FLASHING;

    protected int FIRE_ACTION_TYPE = ACTION_TYPE_ON_RELEASE;

    /**
     * Efeito de clique da entidade.
     */
    protected Effect clickEffect;

    /**
     * Efeito de animação/loop da entidade.
     */
    protected Effect loopEffect;

    /**
     * Lista de escutas da entidade para acionar eventos.
     */
    protected ArrayList<Listener> listeners;

    /**
     * Texto a ser vinculado com a entidade.
     */
    protected Text entityText;


    private boolean clicked;
    private boolean onArea;
    private boolean clickedOnArea;

    /**
     * Cria uma entidade para janela.
     */
    protected WindowEntity() {
        super();
        this.listeners = new ArrayList<>();
    }

    /**
     * Cria uma entidade para janela.
     * @param area area da entidade.
     */
    protected WindowEntity(Rect area) {
        super(area);
        this.listeners = new ArrayList<>();
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
        this.listeners.add(listener);
    }

    /**
     * Aciona as escutas do botão.
     * @param evt evento lançado.
     */
    protected void fireActionListeners(Event evt) {
        for (Listener listener : this.listeners)
            if (listener instanceof ActionListener)
                ((ActionListener)listener).actionPerformed(evt);
    }

    protected void fireListeners(Event evt, int type) {
        try {
            for (Listener listener : this.listeners)
                if (type == ActionListener.ACTION_PERFORMED && listener instanceof ActionListener) {
                    ((ActionListener) listener).actionPerformed(evt);
                } else {
                    if (type == ClickListener.PRESSED_PERFORMED && listener instanceof ClickListener)
                        ((ClickListener) listener).pressedPerformed(evt);
                    else if (type == ClickListener.RELEASE_PERFORMED && listener instanceof ClickListener)
                        ((ClickListener) listener).releasePerformed(evt);
                }
        } catch (Exception e) {
            GameLog.error(this, e.getMessage());
        }
    }

    /**
     * Redefine o efeito de clique do botão com efeitos padrões.
     * @param clickEffect efeito de clique para o botão.
     */
    public void changeClickEffect(int clickEffect) {
        DEFAULT_CLICK_EFFECT = clickEffect;
        if (clickEffect == Effect.FADE_IN_OUT) {
            this.clickEffect = new FadeEffect(this, FadeEffect.FADE_OUT, new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    fireActionListeners(evt);
                    clicked = false;
                }
            });
        } else if (clickEffect == Effect.FLASHING) {
            this.clickEffect = new FlashEffect(this, new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    fireActionListeners(evt);
                    clicked = false;
                }
            });
        }
    }

    /**
     * Redefine o efeito de clique do botão com efeitos customizados.
     * @param effect efeito de clique para o botão.
     */
    public void setClickEffect(ClickEffect effect) {
        this.setClickEffect((Effect) effect);
        this.clickEffect.setEntity(this);
        this.clickEffect.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                WindowEntity.this.fireListeners(evt, ActionListener.ACTION_PERFORMED);
                WindowEntity.this.clicked = false;
            }
        });
    }

    @Override
    public void update() {
        if (clickEffect != null)
            this.clickEffect.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        canvas.clipRect(super.area);
        canvas.drawRect(super.area, super.defaultPaint);
        if (this.entityText != null) {
            this.entityText.draw(canvas);
        }
        canvas.restoreToCount(savedState);
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.clicked)
            return true;

        int action = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        if (haveCollision(x, y, this)) {

            Event event = new Event((int)x, (int)y);

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    this.fireListeners(event, ClickListener.PRESSED_PERFORMED);
                    this.onArea = true;
                    this.clickedOnArea = true;
                    if (FIRE_ACTION_TYPE == ACTION_TYPE_ON_CLICK) {
                        this.clickEffect.start(event);
                        this.clicked = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (this.onArea && this.clickedOnArea) {
                        if (FIRE_ACTION_TYPE == ACTION_TYPE_ON_RELEASE) {
                            this.clickEffect.start(event);
                            this.clicked = true;
                        }
                        this.fireListeners(event, ClickListener.RELEASE_PERFORMED);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (this.clickedOnArea)
                        this.onArea = true;
                    break;
            }
        } else {
            this.onArea = false;
            if (this.clickedOnArea)
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        this.clickedOnArea = false;
                        break;
                    case MotionEvent.ACTION_DOWN:
                        this.clickedOnArea = false;
                        break;
                }
        }
        return true;
    }

    /*
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
                    WindowEntity.this.clicked = true;
                    WindowEntity.this.clickEffect.start();
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
    */
}
