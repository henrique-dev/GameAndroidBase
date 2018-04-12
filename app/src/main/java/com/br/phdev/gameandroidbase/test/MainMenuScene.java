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
 *
 */

package com.br.phdev.gameandroidbase.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.GameParameters;
import com.br.phdev.gameandroidbase.R;
import com.br.phdev.gameandroidbase.cmp.effect.FadeEffect;
import com.br.phdev.gameandroidbase.cmp.environment.Scene;
import com.br.phdev.gameandroidbase.cmp.graphics.Sprite;
import com.br.phdev.gameandroidbase.cmp.graphics.Texture;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.sound.Music;
import com.br.phdev.gameandroidbase.cmp.sound.ShortSound;
import com.br.phdev.gameandroidbase.cmp.window.Button;
import com.br.phdev.gameandroidbase.cmp.window.ListLayout;
import com.br.phdev.gameandroidbase.cmp.window.Window;

public class MainMenuScene extends Scene {

    private MainWindow mainWindow;

    private Texture texture;

    private Texture bg;

    private int BORDERLANDS_MUSIC;
    private int PUNCH_SOUND;

    //private Rect rects[];

    private int cont = 0;
    private int spriteAtual = cont;
    private boolean test = true;

    Sprite sprites[];

    TesteEntity heroi;

    public MainMenuScene(int x, int y, int width, int height) {
        super(x, y, width, height);
        mainWindow = new MainWindow();
        super.add(mainWindow);
    }

    @Override
    public void init() {
        heroi = new TesteEntity();
        heroi.setArea(new Rect(0,0,0,0));
        try {
            this.texture = new Texture("sprites01.png");
            this.bg = new Texture("image.png");
            //this.bg.scaleMe(width, height);
        } catch (Exception ioe) {
            GameLog.error(this, ioe.getMessage());
        }

        BORDERLANDS_MUSIC = super.getSoundManager().addMusicToList(new Music(R.raw.music, "", 1, 1));
        PUNCH_SOUND = super.getSoundManager().addShortSoundToList(new ShortSound(R.raw.p1, 1, 1, 0, 1f));


        //this.sprites = Sprite.getSpriteFromTexture(heroi, this.texture, 9, 7, 62);
    }

    private class MainWindow extends Window {

        private Button startButton;
        private Button optionButton;
        private Button exitButton;

        public MainWindow() {
            super();
            int divWidth = (GameParameters.getInstance().screenSize.width()/8);
            int divHeight = (GameParameters.getInstance().screenSize.height()/8);
            int spaceW = 20;
            int spaceH = 20;
            float defaultTextSize = divHeight * 0.9f;

            super.getArea().set(0, MainMenuScene.this.area.bottom - 400, MainMenuScene.this.area.right, MainMenuScene.this.area.bottom);
            //super.setLayout(new ListLayout(ListLayout.HORIZONTAL_ALIGNMENT, spaceW, spaceH));
            super.setLayout(new ListLayout(ListLayout.VERTICAL_ALIGNMENT));

            this.startButton = new Button("Cancelar");
            this.startButton.setColor(Color.RED);
            //this.startButton.setTextSize(85);

            this.startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    GameLog.error(this, startButton.getText());
                }
            });

            super.add(this.startButton);

            //this.optionButton = new Button("Paulo\nHenrique\nGoncalves\nBacelar");
            this.optionButton = new Button("Calcular");
            this.optionButton.setColor(Color.GRAY);
            //this.optionButton.setTextSize(defaultTextSize);
            //this.optionButton.setTextSize(150);

            this.optionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    //sprites[0].invertV();
                    optionButton.setText("Nani");
                    getSoundManager().playSound(PUNCH_SOUND);
                }
            });

            super.add(this.optionButton);

            //this.exitButton = new Button("Paulo Henrique");
            this.exitButton = new Button("OK");
            this.exitButton.setColor(Color.GREEN);
            //this.exitButton.setTextSize(defaultTextSize);

            this.exitButton.setClickEffect(new FadeEffect());
            this.exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(Event evt) {
                    //sprites[0].rotate(2);
                    getSoundManager().playMusic(BORDERLANDS_MUSIC);
                }
            });

            super.add(this.exitButton);

        }
    }

    int x = 0;
    int y = 0;
    int speed = 10;

    @Override
    public void update() {
        super.update();
        x += speed;
        if (x > GameParameters.getInstance().screenSize.width()) {
            x = 0;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        canvas.drawBitmap(this.bg.getBitmap(), x - GameParameters.getInstance().screenSize.width(), 0, defaultPaint);
        canvas.drawBitmap(this.bg.getBitmap(), x, 0, defaultPaint);

        super.draw(canvas);

        //this.sprites[spriteAtual].draw(canvas);

        canvas.restoreToCount(savedState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return false;
    }

}
