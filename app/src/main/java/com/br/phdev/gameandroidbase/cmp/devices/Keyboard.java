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

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.GameParameters;
import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.effect.FadeEffect;
import com.br.phdev.gameandroidbase.cmp.effect.FlashEffect;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.ClickListener;
import com.br.phdev.gameandroidbase.cmp.listeners.KeyboardListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.listeners.events.KeyboardEvent;
import com.br.phdev.gameandroidbase.cmp.utils.Text;
import com.br.phdev.gameandroidbase.cmp.window.Button;
import com.br.phdev.gameandroidbase.cmp.window.Formable;
import com.br.phdev.gameandroidbase.cmp.window.Window;
import com.br.phdev.gameandroidbase.cmp.window.WindowEntity;

import java.util.ArrayList;

/**
 * Teclado utilizado para entidades que contenham entrada de texto.
 */
public final class Keyboard extends WindowEntity implements ActionListener{

    private Window letters;
    private Window numbers;
    private Window currentKeyboardType;

    private boolean loaded;

    /**
     * Teclas do teclado.
     */
    private ArrayList<WindowEntity> letterKeys;
    private ArrayList<WindowEntity> numberKeys;

    /**
     * Estado ativo do teclado no contexto.
     */
    private boolean keyboardOn;

    /**
     * Valores originais para serem restaurados caso uma entidade consumida pelo teclado seja modificada.
     */
    private RectF originalArea; private Paint originalPaint;

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
        super(new RectF(
                0,
                GameParameters.getInstance().screenSize.centerY() + GameParameters.getInstance().screenSize.centerY()/4,
                GameParameters.getInstance().screenSize.width(),
                GameParameters.getInstance().screenSize.height()));
    }

    /**
     * Carrega o teclado para que esteja totalmente acessivel.
     */
    public void loadComponents() {
        if (loaded)
            return;

        super.setFireActionType(ACTION_TYPE_ON_CLICK);
        super.addListener(0, new KeyboardListener() {
            @Override
            public void keyPressed(KeyboardEvent keyboardEvent) {
                GameLog.debug(this, "Nenhum entidade registrada no teclado! Key: " + keyboardEvent.keyCode);
            }
        });

        setKeys();
        addKeyboardEffect();

        this.loaded = true;
    }

    private void setKeys() {

        this.addLetterKeys();
        this.addNumberKeys();

        this.currentKeyboardType = this.letters;

    }

    private void addLetterKeys() {

        this.letterKeys = new ArrayList<>();
        this.letters = new Window(super.getX(), super.getY(), super.getWidth(), super.getHeight());

        float keyHeight = super.getHeight() / 4;
        float keyWidth = super.getWidth() / 10;

        int letterKeyCode[] = {16, 23, 4, 17, 19, 24, 20, 8, 14, 15, 0, 18, 3, 5,
                6, 7, 9, 10, 11, 25, 22, 2, 21, 1, 13, 12, 26, 27};


        for (int i = 0; i < 10; i++) {
            Key key = new Key(letterKeyCode[i]);
            key.addActionListener(this);
            key.setColor(Color.GREEN);
            key.setArea(new RectF(i * keyWidth, super.getY(), (i + 1) * keyWidth, super.getY() + keyHeight));
            this.letterKeys.add(key);

        }

        for (int i = 0; i < 9; i++) {
            Key key = new Key(letterKeyCode[i + 10]);
            key.addActionListener(this);
            key.setColor(Color.GREEN);
            key.setArea(new RectF(
                    keyWidth / 2 + i * keyWidth,
                    super.getY() + keyHeight,
                    keyWidth / 2 + (i + 1) * keyWidth,
                    super.getY() + (keyHeight * 2)));
            this.letterKeys.add(key);
        }

        for (int i = 0; i < 7; i++) {
            Key key = new Key(letterKeyCode[i + 19]);
            key.addActionListener(this);
            key.setColor(Color.GREEN);
            key.setArea(new RectF(
                    keyWidth / 2 + (i * keyWidth),
                    super.getY() + (keyHeight * 2),
                    keyWidth / 2 + (i + 1) * keyWidth,
                    super.getY() + (keyHeight * 3)));
            this.letterKeys.add(key);

            if (i == 6) {
                Key backspaceKey = new Key(KeyboardEvent.KEY_BACKSPACE);
                backspaceKey.addActionListener(this);
                backspaceKey.setColor(Color.GREEN);
                backspaceKey.setArea(new RectF(
                        keyWidth / 2 + ((i + 1) * keyWidth),
                        super.getY() + (keyHeight * 2),
                        keyWidth / 2 + ((i + 3) * keyWidth),
                        super.getY() + (keyHeight * 3)));
                this.letterKeys.add(backspaceKey);
            }
        }

        Key spaceKey = new Key(KeyboardEvent.KEY_SPACE);
        spaceKey.addActionListener(this);
        spaceKey.setColor(Color.GREEN);
        spaceKey.setArea(new RectF(
                super.area.centerX() - (keyWidth * 2),
                super.getY() + (keyHeight * 3),
                super.area.centerX() + (keyWidth * 2),
                super.getY() + (keyHeight * 4)
        ));
        this.letterKeys.add(spaceKey);

        Key switchKey = new Key(KeyboardEvent.KEY_SWITCH);
        switchKey.addActionListener(this);
        switchKey.setColor(Color.GREEN);
        switchKey.setArea(new RectF(
                spaceKey.getArea().left - (keyWidth * 2),
                super.getY() + (keyHeight * 3),
                spaceKey.getArea().left,
                super.getY() + (keyHeight * 4)
        ));
        this.letterKeys.add(switchKey);

        Key confirmKey = new Key(KeyboardEvent.KEY_CONFIRM);
        confirmKey.addActionListener(this);
        confirmKey.setColor(Color.GREEN);
        confirmKey.setArea(new RectF(
                spaceKey.getArea().right,
                super.getY() + (keyHeight * 3),
                spaceKey.getArea().right + (keyWidth * 2),
                super.getY() + (keyHeight * 4)
        ));
        this.letterKeys.add(confirmKey);

        this.letters.add(this.letterKeys);
    }

    private void addNumberKeys() {

        this.numberKeys = new ArrayList<>();
        this.numbers = new Window(super.getX(), super.getY(), super.getWidth(), super.getHeight());

        float keyHeight = super.getHeight() / 5;
        float keyWidth = super.getWidth() / 3;

        int numberKeyCode[] = {29, 30, 31, 32, 33, 34, 35, 36, 37, 38};

        int tempCounter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Key key = new Key(numberKeyCode[tempCounter++]);
                key.addActionListener(this);
                key.setColor(Color.GREEN);
                key.setArea(new RectF(
                        j * keyWidth,
                        (i * keyHeight) + super.getY(),
                        (j + 1) * keyWidth,
                        (i * keyHeight) + super.getY() + keyHeight));
                this.numberKeys.add(key);
            }
        }
        Key zeroKey = new Key(38);
        zeroKey.addActionListener(this);
        zeroKey.setColor(Color.GREEN);
        zeroKey.setArea(new RectF(
                1 * keyWidth,
                (3 * keyHeight) + super.getY(),
                2 * keyWidth,
                (3 * keyHeight) + super.getY() + keyHeight));
        this.numberKeys.add(zeroKey);

        Key pointKey = new Key(KeyboardEvent.KEY_POINT);
        pointKey.addActionListener(this);
        pointKey.setColor(Color.GREEN);
        pointKey.setArea(new RectF(
                0,
                (3 * keyHeight) + super.getY(),
                keyWidth,
                (3 * keyHeight) + super.getY() + keyHeight));
        this.numberKeys.add(pointKey);

        Key switchKey = new Key(KeyboardEvent.KEY_SWITCH);
        switchKey.addActionListener(this);
        switchKey.setColor(Color.GREEN);
        switchKey.setArea(new RectF(
                0,
                (4 * keyHeight) + super.getY(),
                keyWidth,
                (4 * keyHeight) + super.getY() + keyHeight));
        this.numberKeys.add(switchKey);

        Key backspaceKey = new Key(KeyboardEvent.KEY_BACKSPACE);
        backspaceKey.addActionListener(this);
        backspaceKey.setColor(Color.GREEN);
        backspaceKey.setArea(new RectF(
                keyWidth,
                (4 * keyHeight) + super.getY(),
                2 * keyWidth,
                (4 * keyHeight) + super.getY() + keyHeight));
        this.numberKeys.add(backspaceKey);

        Key confirmKey = new Key(KeyboardEvent.KEY_CONFIRM);
        confirmKey.addActionListener(this);
        confirmKey.setColor(Color.GREEN);
        confirmKey.setArea(new RectF(
                2 * keyWidth,
                (4 * keyHeight) + super.getY(),
                3 * keyWidth,
                (4 * keyHeight) + super.getY() + keyHeight));
        this.numberKeys.add(confirmKey);

        this.numbers.add(this.numberKeys);
    }

    private void addKeyboardEffect() {
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
        fadeEffect1.setSpeed(2);
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
        fadeEffect2.setSpeed(2);
        super.effects.add(1, fadeEffect2);
    }

    private void enableKeyboard() {
        super.effects.get(1).start(new Event(0, 0));
        this.keyboardOn = true;
    }

    private void disableKeyboard() {
        super.effects.get(0).start(new Event(0,0));
    }

    /**
     * Registra uma entidade no teclado.
     * @param keyboardListener entidade com entrada de dados.
     */
    public void registerListener(KeyboardListener keyboardListener) {
        super.addListener(0, keyboardListener);
        this.entity = (Entity)keyboardListener;
        this.originalArea = new RectF(this.entity.getArea());
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
            this.enableKeyboard();
        }
        else
            this.disableKeyboard();
    }

    /**
     * Retorna o char atribuido a determinado keycode do teclado.
     * @param keyCode keycode do {@link KeyboardEvent}.
     * @return charactere atribuido ao keycode.
     */
    private static String getKeyName(int keyCode) {
        switch (keyCode) {
            case KeyboardEvent.KEY_A:
                return "A";
            case KeyboardEvent.KEY_B:
                return "B";
            case KeyboardEvent.KEY_C:
                return "C";
            case KeyboardEvent.KEY_D:
                return "D";
            case KeyboardEvent.KEY_E:
                return "E";
            case KeyboardEvent.KEY_F:
                return "F";
            case KeyboardEvent.KEY_G:
                return "G";
            case KeyboardEvent.KEY_H:
                return "H";
            case KeyboardEvent.KEY_I:
                return "I";
            case KeyboardEvent.KEY_J:
                return "J";
            case KeyboardEvent.KEY_K:
                return "K";
            case KeyboardEvent.KEY_L:
                return "L";
            case KeyboardEvent.KEY_M:
                return "M";
            case KeyboardEvent.KEY_N:
                return "N";
            case KeyboardEvent.KEY_O:
                return "O";
            case KeyboardEvent.KEY_P:
                return "P";
            case KeyboardEvent.KEY_Q:
                return "Q";
            case KeyboardEvent.KEY_R:
                return "R";
            case KeyboardEvent.KEY_S:
                return "S";
            case KeyboardEvent.KEY_T:
                return "T";
            case KeyboardEvent.KEY_U:
                return "U";
            case KeyboardEvent.KEY_V:
                return "V";
            case KeyboardEvent.KEY_X:
                return "X";
            case KeyboardEvent.KEY_W:
                return "W";
            case KeyboardEvent.KEY_Y:
                return "Y";
            case KeyboardEvent.KEY_Z:
                return "Z";
            case KeyboardEvent.KEY_SPACE:
                return "Espaço";
            case KeyboardEvent.KEY_BACKSPACE:
                return "Apagar";
            case KeyboardEvent.KEY_CONFIRM:
                return "Ok";
            case KeyboardEvent.KEY_SWITCH:
                return "Trocar";
            case KeyboardEvent.KEY_0:
                return "0";
            case KeyboardEvent.KEY_1:
                return "1";
            case KeyboardEvent.KEY_2:
                return "2";
            case KeyboardEvent.KEY_3:
                return "3";
            case KeyboardEvent.KEY_4:
                return "4";
            case KeyboardEvent.KEY_5:
                return "5";
            case KeyboardEvent.KEY_6:
                return "6";
            case KeyboardEvent.KEY_7:
                return "7";
            case KeyboardEvent.KEY_8:
                return "8";
            case KeyboardEvent.KEY_9:
                return "9";
            case KeyboardEvent.KEY_POINT:
                return ".";
            default:
                return "?";
        }
    }

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
            case KeyboardEvent.KEY_0:
                return '0';
            case KeyboardEvent.KEY_1:
                return '1';
            case KeyboardEvent.KEY_2:
                return '2';
            case KeyboardEvent.KEY_3:
                return '3';
            case KeyboardEvent.KEY_4:
                return '4';
            case KeyboardEvent.KEY_5:
                return '5';
            case KeyboardEvent.KEY_6:
                return '6';
            case KeyboardEvent.KEY_7:
                return '7';
            case KeyboardEvent.KEY_8:
                return '8';
            case KeyboardEvent.KEY_9:
                return '9';
            case KeyboardEvent.KEY_SPACE:
                return ' ';
            case KeyboardEvent.KEY_POINT:
                return '.';
            default:
                return '?';
        }
    }

    @Override
    public void actionPerformed(Event evt) {
        KeyboardEvent keyboardEvent = (KeyboardEvent)evt;
        if (keyboardEvent.keyCode == KeyboardEvent.KEY_CONFIRM)
            disableKeyboard();
        else if (keyboardEvent.keyCode == KeyboardEvent.KEY_SWITCH) {
            if (this.currentKeyboardType == this.letters)
                this.currentKeyboardType = this.numbers;
            else
                this.currentKeyboardType = this.letters;
        } else
            ((KeyboardListener)this.entity).keyPressed(keyboardEvent);
    }

    @Override
    public void update() {
        if (!super.visible)
            return;
        super.update();
        this.currentKeyboardType.update();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!super.visible)
            return;
        int savedState = canvas.save();

        canvas.drawRect(super.area, super.defaultPaint);
        this.currentKeyboardType.draw(canvas);
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
            for (WindowEntity key : this.currentKeyboardType.get()) {
                if (haveCollision(x, y, key))
                    key.onTouchEvent(motionEvent);
            }
            return true;
        } else
            if (!haveCollision(motionEvent.getX(), motionEvent.getY(), this.entity))
                this.setKeyboardOn(false);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    private class Key extends Button {

        private int keyCode;

        Key(int keyCode) {
            super(new RectF(), new Text(Keyboard.getKeyName(keyCode)));
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
