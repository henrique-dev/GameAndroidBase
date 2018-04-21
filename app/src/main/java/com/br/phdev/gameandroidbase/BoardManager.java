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
package com.br.phdev.gameandroidbase;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.cmp.utils.Text;

public class BoardManager {

    static BoardManager make = new BoardManager();

    Thread loadingThread;

    private DeviceManager deviceManager;
    private SoundManager soundManager;

    private Board loadingBoard;

    private Board currentBoard;
    private Board nextBoard;

    enum State { LOADING, RUNNING, OFF}

    private State currentBoardState;

    private BoardManager() {
    }

    void start(DeviceManager deviceManager, SoundManager soundManager) {
        this.deviceManager = deviceManager;
        this.soundManager = soundManager;
        this.loadingBoard = new LoadingBoard(0,0,GameParameters.getInstance().screenSize.width(), GameParameters.getInstance().screenSize.height());
        this.currentBoardState = State.LOADING;

        this.loadingThread = new Thread() {
            @Override
            public void run() {
                try {
                    if (BoardManager.this.currentBoardState == BoardManager.State.OFF) {
                        GameLog.debugr(this, "CANCELOU CARREGAMENTO");
                        return;
                    }
                    BoardManager.this.currentBoard = BoardManager.this.nextBoard;
                    BoardManager.this.currentBoard.initBoard();
                    BoardManager.this.currentBoardState = BoardManager.State.RUNNING;
                    GameLog.debugr(this, "COMPLETOU O CARREGAMENTO");
                } catch (Exception e) {
                    GameLog.error(this, e);
                }
            }
        };
    }

    void register(Board board) {
        this.nextBoard = board;
        this.nextBoard.setDeviceManager(this.deviceManager);
        this.nextBoard.setSoundManager(this.soundManager);
        post();
    }

    private void post() {
        if (this.currentBoard != null) {
            this.currentBoard.finalizeBoard();
            this.currentBoard = null;
        }
        this.currentBoardState = State.LOADING;
        this.loadingThread.start();
    }

    void releaseBoard() {
        this.currentBoardState = State.OFF;
        if (this.currentBoard != null) {
            this.currentBoard.finalizeBoard();
            this.currentBoard = null;
        }
        if (this.loadingBoard != null) {
            this.loadingBoard.finalizeBoard();
            this.loadingBoard = null;
        }
    }

    State isOk() {
        return currentBoardState;
    }

    Board getBoard() {
        return this.currentBoardState == State.RUNNING ? this.currentBoard : this.loadingBoard;
    }

    private class LoadingBoard extends Board {

        private Text text;

        public LoadingBoard(float x, float y, float width, float height) {
            super();
            super.setArea(new RectF(x, y, x + width, y + height));
            super.getDefaultPaint().setColor(Color.WHITE);
            this.text = new Text(this, "Loading");
            this.text.setSize(50);
            this.text.setColor(Color.BLACK);
        }

        /*
        public LoadingBoard(int x, int y, int width, int height) {
            super(x, y, width, height);
            super.getDefaultPaint().setColor(Color.WHITE);
            this.text = new Text("Loading");
            this.text.setTextSize(50);
            this.text.setColor(Color.BLACK);
        }*/

        @Override
        public void update() {

        }

        @Override
        public void draw(Canvas canvas) {
            int savedCount = canvas.getSaveCount();
            canvas.drawRect(super.getArea(), super.defaultPaint);
            this.text.draw(canvas);
            canvas.restoreToCount(savedCount);
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public void initBoard() {

        }

        @Override
        public void finalizeBoard() {

        }
    }
}
