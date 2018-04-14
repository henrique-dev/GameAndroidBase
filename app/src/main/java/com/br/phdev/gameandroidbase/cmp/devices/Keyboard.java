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
import android.graphics.Rect;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.GameParameters;
import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.WindowEntity;
import com.br.phdev.gameandroidbase.cmp.effect.FadeEffect;
import com.br.phdev.gameandroidbase.cmp.effect.FlashEffect;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.KeyboardListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.window.Button;
import com.br.phdev.gameandroidbase.cmp.window.Formable;
import com.br.phdev.gameandroidbase.cmp.window.GridLayout;
import com.br.phdev.gameandroidbase.cmp.window.Layout;

import java.util.ArrayList;

public class Keyboard extends WindowEntity implements Formable{

    private Layout layout;
    private ArrayList<WindowEntity> buttonKeys;

    private boolean keyboardOn;

    public Keyboard() {
        super(new Rect(
                0,
                GameParameters.getInstance().screenSize.centerY() + GameParameters.getInstance().screenSize.centerY()/4,
                GameParameters.getInstance().screenSize.width(),
                GameParameters.getInstance().screenSize.height()));
        //super.active = false;
        //super.visible = false;

    }

    public void loadComponents() {
        this.buttonKeys = new ArrayList<>();

        super.visible = true;
        super.active = true;

        GridLayout gridLayout = new GridLayout(3 ,10);
        gridLayout.set(this);
        gridLayout.setSpaceH(1);
        gridLayout.setSpaceV(1);
        this.layout = gridLayout;
        super.setFireActionType(ACTION_TYPE_ON_CLICK);
        super.addListener(0, new KeyboardListener() {
            @Override
            public void keyPressed(char key) {
                GameLog.debug(this, "Nenhum entidade registrada no teclado! Key: " + key);
            }
        });
        char keys[] = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F',
                'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '_'};
        for (int i=0; i<28; i++) {
            final Button button = new Button(keys[i] + "");
            FlashEffect flashEffect = new FlashEffect();
            flashEffect.setSpeed(1);
            button.setClickEffect(flashEffect);
            button.setTextSize(GameParameters.getInstance().screenSize.width() / 20);
            button.setFireActionType(ACTION_TYPE_ON_CLICK);
            button.setEdgeSize(1);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    ((KeyboardListener)Keyboard.super.getListener(0)).keyPressed(button.getText().charAt(0));
                }
            });
            button.setColor(Color.GREEN);
            this.buttonKeys.add(button);
            this.layout.format();
        }
        super.effects.add(0, new FadeEffect(this, FadeEffect.FADE_OUT, new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                Keyboard.this.keyboardOn = false;
            }
        }));
        super.effects.add(1, new FadeEffect(this, FadeEffect.FADE_IN, new ActionListener() {
            @Override
            public void actionPerformed(Event evt) {
                Keyboard.this.keyboardOn = true;
            }
        }));
    }

    public void registerListener(KeyboardListener keyboardListener) {
        super.addListener(0, keyboardListener);
    }

    public boolean isKeyboardOn() {
        return keyboardOn;
    }

    public void setKeyboardOn(boolean keyboardOn) {
        if (keyboardOn) {
            super.effects.get(1).start(new Event(0, 0));
            this.keyboardOn = true;
        }
        else
            super.effects.get(0).start(new Event(0,0));
    }

    @Override
    public ArrayList<WindowEntity> get() {
        return this.buttonKeys;
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
        canvas.restoreToCount(savedState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!super.visible)
            return true;
        for (WindowEntity ent : this.buttonKeys) {
            if (ent.isActive())
                ent.onTouchEvent(motionEvent);
        }
        return true;
    }
}
