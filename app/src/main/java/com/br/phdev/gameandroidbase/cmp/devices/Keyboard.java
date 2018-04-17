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
package com.br.phdev.gameandroidbase.cmp.devices;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.GameParameters;
import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.listeners.ClickListener;
import com.br.phdev.gameandroidbase.cmp.utils.Text;
import com.br.phdev.gameandroidbase.cmp.effect.FadeEffect;
import com.br.phdev.gameandroidbase.cmp.effect.FlashEffect;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.KeyboardListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.listeners.events.KeyboardEvent;
import com.br.phdev.gameandroidbase.cmp.window.Button;
import com.br.phdev.gameandroidbase.cmp.window.Formable;
import com.br.phdev.gameandroidbase.cmp.window.GridLayout;
import com.br.phdev.gameandroidbase.cmp.window.Layout;
import com.br.phdev.gameandroidbase.cmp.window.WindowEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Teclado utilizado para entidades que contenham entrada de texto.
 */
public final class Keyboard extends WindowEntity implements Formable, ActionListener{

    /**
     * Layout do teclado.
     */
    private Layout layout;

    /**
     * Teclas do teclado.
     */
    private List<Key> buttonKeys;

    /**
     * Estado ativo do teclado no contexto.
     */
    private boolean keyboardOn;

    /**
     * Valores originais para serem restaurados caso uma entidade consumida pelo teclado seja modificada.
     */
    private Rect originalArea; private Paint originalPaint;

    /**
     * Estado de modificação da entidade consumida pelo teclado.
     */
    private boolean modified;

    /**
     * Entidade a ser consumida pelo teclado.
     */
    private Entity entity;

    /**
     * Cria um teclado.
     */
    public Keyboard() {
        super(new Rect(
                0,
                GameParameters.getInstance().screenSize.centerY() + GameParameters.getInstance().screenSize.centerY()/4,
                GameParameters.getInstance().screenSize.width(),
                GameParameters.getInstance().screenSize.height()));
    }

    /**
     * Carrega o teclado para que esteja totalmente acessivel.
     */
    public void loadComponents() {
        this.buttonKeys = new ArrayList<>();
        GridLayout gridLayout = new GridLayout(3 ,10);
        gridLayout.set(this);
        gridLayout.setSpaceH(1);
        gridLayout.setSpaceV(1);
        this.layout = gridLayout;
        super.setFireActionType(ACTION_TYPE_ON_CLICK);
        super.addListener(0, new KeyboardListener() {
            @Override
            public void keyPressed(KeyboardEvent keyboardEvent) {
                GameLog.debug(this, "Nenhum entidade registrada no teclado! Key: " + keyboardEvent.keyCode);
            }
        });
        int keyCode[] = {16, 23, 4, 17, 19, 24, 20, 8, 14, 15, 0, 18, 3, 5,
                6, 7, 9, 10, 11, 25, 22, 2, 21, 1, 13, 12, 26, 27};
        for (int i=0; i<28; i++) {
            final Key button = new Key(keyCode[i]);

            //FlashEffect flashEffect = new FlashEffect();
            //flashEffect.setSpeed(1);
            //button.setClickEffect(flashEffect);
            //button.setTextSize(GameParameters.getInstance().screenSize.width() / 20);
            //button.setFireActionType(ACTION_TYPE_ON_CLICK);
            //button.setEdgeSize(1);

            //final KeyboardEvent keyboardEvent = new KeyboardEvent(keyCode[i]);
            /*
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    ((KeyboardListener)Keyboard.super.getListener(0)).keyPressed(keyboardEvent);
                }
            });
            */
            button.addActionListener(this);

            button.setColor(Color.GREEN);
            this.buttonKeys.add(button);
            this.layout.format();
        }
        final FadeEffect fadeEffect1 = new FadeEffect(this, FadeEffect.FADE_OUT, new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                Keyboard.this.keyboardOn = false;
                if (Keyboard.this.modified) {
                    Keyboard.this.modified = false;
                    Keyboard.this.entity.getDefaultPaint().setAlpha(Keyboard.this.originalPaint.getAlpha());
                    Keyboard.this.entity.setArea(Keyboard.this.originalArea);
                }
            }
        });
        super.effects.add(0, fadeEffect1);

        final FadeEffect fadeEffect2 = new FadeEffect(this, FadeEffect.FADE_IN, new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                Keyboard.this.keyboardOn = true;
                if (Keyboard.this.entity.getY() > Keyboard.this.getY()) {
                    Keyboard.this.modified = true;
                    Keyboard.this.entity.getDefaultPaint().setAlpha(200);
                    Keyboard.this.entity.setY(Keyboard.this.getY() - Keyboard.this.entity.getHeight());
                }
            }
        });
        super.effects.add(1, fadeEffect2);
    }

    /**
     * Registra uma entidade no teclado.
     * @param keyboardListener entidade com entrada de dados.
     */
    public void registerListener(KeyboardListener keyboardListener) {
        super.addListener(0, keyboardListener);
        this.entity = (Entity)keyboardListener;
        this.originalArea = new Rect(this.entity.getArea());
        this.originalPaint = new Paint(this.entity.getDefaultPaint());
    }

    /**
     * Verifica se o teclado esta ativo.
     * @return estado ativo do teclado.
     */
    public boolean isKeyboardOn() {
        return keyboardOn;
    }

    /**
     * Ativa/Desativa o teclado.
     * @param keyboardOn true para ativar. false para desativar.
     */
    public void setKeyboardOn(boolean keyboardOn) {
        if (keyboardOn) {
            super.effects.get(1).start(new Event(0, 0));
            this.keyboardOn = true;
        }
        else
            super.effects.get(0).start(new Event(0,0));
    }

    @Override
    public Entity get(int index) {
        return this.buttonKeys.get(index);
    }

    @Override
    public int size() {
        return this.buttonKeys.size();
    }

    /**
     * Retorna o char atribuido a determinado keycode do teclado.
     * @param keyCode keycode do {@link KeyboardEvent}.
     * @return charactere atribuido ao keycode.
     */
    public static char getChar(int keyCode) {
        switch (keyCode) {
            case KeyboardEvent.KEY_A:
                return 'A';
            case KeyboardEvent.KEY_B:
                return 'B';
            case KeyboardEvent.KEY_C:
                return 'C';
            case KeyboardEvent.KEY_D:
                return 'D';
            case KeyboardEvent.KEY_E:
                return 'E';
            case KeyboardEvent.KEY_F:
                return 'F';
            case KeyboardEvent.KEY_G:
                return 'G';
            case KeyboardEvent.KEY_H:
                return 'H';
            case KeyboardEvent.KEY_I:
                return 'I';
            case KeyboardEvent.KEY_J:
                return 'J';
            case KeyboardEvent.KEY_K:
                return 'K';
            case KeyboardEvent.KEY_L:
                return 'L';
            case KeyboardEvent.KEY_M:
                return 'M';
            case KeyboardEvent.KEY_N:
                return 'N';
            case KeyboardEvent.KEY_O:
                return 'O';
            case KeyboardEvent.KEY_P:
                return 'P';
            case KeyboardEvent.KEY_Q:
                return 'Q';
            case KeyboardEvent.KEY_R:
                return 'R';
            case KeyboardEvent.KEY_S:
                return 'S';
            case KeyboardEvent.KEY_T:
                return 'T';
            case KeyboardEvent.KEY_U:
                return 'U';
            case KeyboardEvent.KEY_V:
                return 'V';
            case KeyboardEvent.KEY_X:
                return 'X';
            case KeyboardEvent.KEY_W:
                return 'W';
            case KeyboardEvent.KEY_Y:
                return 'Y';
            case KeyboardEvent.KEY_Z:
                return 'Z';
            case KeyboardEvent.KEY_SPACE:
                return ' ';
            default:
                return '?';
        }
    }

    @Override
    public void update() {
        if (!super.visible)
            return;
        super.update();

        for (Entity ent : this.buttonKeys)
            if (ent.isActive())
                ent.update();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!super.visible)
            return;
        int savedState = canvas.save();

        canvas.drawRect(super.area, super.defaultPaint);
        for (Entity ent : this.buttonKeys)
            if (ent.isVisible())
                ent.draw(canvas);
        if (this.modified)
            this.entity.draw(canvas);

        canvas.restoreToCount(savedState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!super.visible)
            return false;

        float x = motionEvent.getX();
        float y = motionEvent.getY();

        if (haveCollision(x, y, this)) {
            for (Key key : this.buttonKeys) {
                if (haveCollision(x, y, key))
                    key.onTouchEvent(motionEvent);
                    //key.clickEffect.start(new KeyboardEvent(key.getKeyCode()));
            }
            return true;
        } else
            if (!haveCollision(motionEvent.getX(), motionEvent.getY(), this.entity))
                this.setKeyboardOn(false);
        return false;
    }

    @Override
    public void actionPerformed(Event evt) {
        ((KeyboardListener)this.entity).keyPressed((KeyboardEvent)evt);
    }

    private class Key extends Button {

        private int keyCode;

        Key(int keyCode) {
            super(new Rect(), new Text(Keyboard.getChar(keyCode) + ""));
            this.keyCode = keyCode;
            FlashEffect flashEffect = new FlashEffect();
            flashEffect.setSpeed(1);
            setClickEffect(flashEffect);
            setTextSize(GameParameters.getInstance().screenSize.width() / 20);
            setEdgeSize(1);

        }

        public int getKeyCode() {
            return keyCode;
        }

        public void setKeyCode(int keyCode) {
            this.keyCode = keyCode;
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
                        this.fireListeners(new KeyboardEvent(keyCode), ClickListener.PRESSED_PERFORMED);
                        this.onArea = true;
                        this.clickedOnArea = true;
                        if (clickEffect != null)
                            this.clickEffect.start(new KeyboardEvent(keyCode));
                        else
                            this.fireListeners(new KeyboardEvent(keyCode), ActionListener.ACTION_PERFORMED);
                        this.clicked = true;
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
    }
}
