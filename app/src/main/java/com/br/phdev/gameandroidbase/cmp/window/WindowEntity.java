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
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.cmp.Controllable;
import com.br.phdev.gameandroidbase.cmp.Entity;
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
public abstract class WindowEntity extends Entity implements Controllable {

    protected final int ACTION_TYPE_ON_CLICK = 0;
    protected final int ACTION_TYPE_ON_RELEASE = 1;

    /**
     * Efeito de quando clicado padrão do botão.
     */
    protected int DEFAULT_CLICK_EFFECT = Effect.FLASHING;

    protected int fireActionType = ACTION_TYPE_ON_RELEASE;

    /**
     * Efeito de clique da entidade.
     */
    protected Effect clickEffect;

    /**
     * Lista de efeitos diversos para a entidade.
     */
    protected ArrayList<Effect> effects;

    /**
     * Lista de escutas da entidade para acionar eventos.
     */
    protected ArrayList<Listener> listeners;

    /**
     * Texto a ser vinculado com a entidade.
     */
    protected Text entityText;

    /**
     * Atributos referentes a borda da entidade.
     */
    private boolean edgeVisible; private Paint edgePaint; private int edgeSize;

    /**
     * Estado de selecionado da entidade.
     */
    protected boolean clicked;

    /**
     * Estado de sobre a area da entidade.
     */
    protected boolean onArea;

    /**
     * Estado de clicado sobre a area da entidade.
     */
    protected boolean clickedOnArea;

    /**
     * Cria uma entidade para janela.
     */
    protected WindowEntity() {
        super();
        this.effects = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.edgeVisible = true;
        this.edgePaint = new Paint();
        this.edgeSize = 5;
        super.visible = true;
        super.active = true;
    }

    /**
     * Cria uma entidade para janela.
     * @param area area da entidade.
     */
    protected WindowEntity(Rect area) {
        super(area);
        this.effects = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.edgeVisible = true;
        this.edgePaint = new Paint();
        this.edgeSize = 5;
        super.visible = true;
        super.active = true;
    }

    /**
     * Cria uma entidade para janela.
     * @param area area da entidade.
     * @param entityText texto a ser exibido com a entidade.
     */
    protected WindowEntity(Rect area, Text entityText) {
        super(area);
        this.effects = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.entityText = entityText;
        this.entityText.setEntity(this);
        this.edgeVisible = true;
        this.edgePaint = new Paint();
        this.edgeSize = 5;
        super.visible = true;
        super.active = true;
    }

    @Override
    public void setArea(Rect area) {
        super.setArea(area);
        if (this.entityText != null) {
            this.entityText.setArea(new Rect(area));
        }
    }

    /**
     * Retorna o tamanho da borda da entidade.
     * @return tamanho da borda.
     */
    public int getEdgeSize() {
        return edgeSize;
    }

    /**
     * Redefine o tamanho da borda da entidade.
     * @param edgeSize tamanho para a borda.
     */
    public void setEdgeSize(int edgeSize) {
        this.edgeSize = edgeSize;
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
     * Adiciona umaa escuta de eventos para a entidade.
     * @param listener escuta para entidade.
     */
    protected void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    /**
     * Adiciona uma escuta de eventos para a entidade em determinada posição da lista.
     * @param index posição na lista.
     * @param listener escuta para a entidade.
     */
    protected void addListener(int index, Listener listener) {
        this.listeners.add(index, listener);
    }

    /**
     * Retorna uma escuta da entidade de acordo com a posição repassada.
     * @param index posição d aescuta na lista.
     * @return escuta da entidade.
     */
    protected Listener getListener(int index) {
        return this.listeners.get(index);
    }

    /**
     * Retorna o tipo de execução das esccutas de ação da entidade. Quando pressionada ou despressionada.
     * @return tipo de execução  das escutas de ação.
     */
    protected int getFireActionType() {
        return fireActionType;
    }

    /**
     * Redefine o tipo de execução das escutas de ação da entidade.
     * ACTION_TYPE_ON_CLICK para executar a ação quando pressionada.
     * ACTION_TYPE_ON_RELEASE para executar a ação quando despressionada.
     * @param fireActionType ACTION_TYPE_ON_RELEASE ou ACTION_TYPE_ON_CLICK.
     */
    protected void setFireActionType(int fireActionType) {
        this.fireActionType = fireActionType;
    }

    /**
     * Executa as escutas de acordo com o tipo repassado.
     * @param evt evento lançado de quem ativou a escuta.
     * @param type ActionListener.ACTION_PERFORMED, ClickListener.PRESSED_PERFORMED ou ClickListener.RELEASE_PERFORMED.
     */
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
    protected void changeClickEffect(int clickEffect) {
        DEFAULT_CLICK_EFFECT = clickEffect;
        if (clickEffect == Effect.FADE_IN_OUT) {
            this.clickEffect = new FadeEffect(this, FadeEffect.FADE_OUT, new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    //fireActionListeners(evt);
                    fireListeners(evt, ActionListener.ACTION_PERFORMED);
                    clicked = false;
                }
            });
        } else if (clickEffect == Effect.FLASHING) {
            this.clickEffect = new FlashEffect(this, new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    //fireActionListeners(evt);
                    fireListeners(evt, ActionListener.ACTION_PERFORMED);
                    clicked = false;
                }
            });
        }
    }

    /**
     * Redefine o efeito de clique do botão com efeitos customizados.
     * @param effect efeito de clique para o botão.
     */
    protected void setClickEffect(ClickEffect effect) {
        this.clickEffect = (Effect)effect;
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
        if (this.clickEffect != null)
            this.clickEffect.update();
        for (Effect effect : this.effects)
            effect.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();

        canvas.clipRect(super.area);
        canvas.drawRect(super.area, super.defaultPaint);
        if (this.entityText != null) {
            this.entityText.draw(canvas);
        }
        if (this.edgeVisible) {
            canvas.drawRect(super.area.left, super.area.top, super.area.right, super.area.top + this.edgeSize, this.edgePaint);
            canvas.drawRect(super.area.right - this.edgeSize, super.area.top, super.area.right, super.area.bottom, this.edgePaint);
            canvas.drawRect(super.area.left, super.area.bottom - this.edgeSize, super.area.right, super.area.bottom, this.edgePaint);
            canvas.drawRect(super.area.left, super.area.top, super.area.left + this.edgeSize, super.area.bottom, this.edgePaint);
        }
        /*
        for (Effect effect : this.effects)
            effect.draw(canvas);
            */

        canvas.restoreToCount(savedState);
    }


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
                    this.fireListeners(new Event(), ClickListener.PRESSED_PERFORMED);
                    this.onArea = true;
                    this.clickedOnArea = true;
                    if (fireActionType == ACTION_TYPE_ON_CLICK) {
                        if (clickEffect != null)
                            this.clickEffect.start(new Event());
                        else
                            this.fireListeners(new Event(), ActionListener.ACTION_PERFORMED);
                        this.clicked = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (this.onArea && this.clickedOnArea) {
                        if (fireActionType == ACTION_TYPE_ON_RELEASE) {
                            this.clicked = true;
                            if (clickEffect != null)
                                this.clickEffect.start(new Event());
                            else {
                                this.fireListeners(new Event(), ActionListener.ACTION_PERFORMED);
                                this.clicked = false;
                            }
                        }
                        this.fireListeners(new Event(), ClickListener.RELEASE_PERFORMED);
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

    @Override
    public boolean keyBackPressed() {
        return false;
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
