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
import com.br.phdev.gameandroidbase.cmp.effect.FlashEffect;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.KeyboardListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.window.Button;
import com.br.phdev.gameandroidbase.cmp.window.Formable;
import com.br.phdev.gameandroidbase.cmp.window.GridLayout;
import com.br.phdev.gameandroidbase.cmp.window.Layout;
import com.br.phdev.gameandroidbase.cmp.window.Window;

import java.util.ArrayList;
import java.util.List;

public class Keyboard extends WindowEntity implements Formable{

    private Layout layout;
    private ArrayList<Entity> buttonKeys;

    public Keyboard() {
        super(new Rect(
                0,
                GameParameters.getInstance().screenSize.centerY(),
                GameParameters.getInstance().screenSize.width(),
                GameParameters.getInstance().screenSize.height()));
        this.buttonKeys = new ArrayList<>();

        GridLayout gridLayout = new GridLayout(3 ,10);
        gridLayout.set(this);
        gridLayout.setSpaceH(5);
        gridLayout.setSpaceV(5);
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
    }

    public void registerListener(KeyboardListener keyboardListener) {
        super.addListener(0, keyboardListener);
    }

    @Override
    public ArrayList<Entity> get() {
        return this.buttonKeys;
    }

    @Override
    public void update() {
        for (Entity ent : this.buttonKeys)
            if (ent.isActive())
                ent.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        canvas.drawRect(super.area, super.defaultPaint);
        for (Entity ent : this.buttonKeys)
            if (ent.isVisible())
                ent.draw(canvas);
        canvas.restoreToCount(savedState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        for (Entity ent : this.buttonKeys) {
            if (ent.isActive())
                ent.onTouchEvent(motionEvent);
        }
        return true;
    }
}
