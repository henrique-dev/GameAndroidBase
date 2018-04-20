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

import com.br.phdev.gameandroidbase.cmp.environment.Board;

public class BoardManager {

    public static BoardManager make = new BoardManager();

    private DeviceManager deviceManager;
    private SoundManager soundManager;

    private Board currentBoard;
    private Board nextBoard;

    private boolean currentBoardState;

    private BoardManager() {

    }

    public static BoardManager getInstance() {
        return make;
    }

    void set(DeviceManager deviceManager, SoundManager soundManager) {
        this.deviceManager = deviceManager;
        this.soundManager = soundManager;
    }

    public void register(Board board) {
        this.nextBoard = board;
        this.nextBoard.setDeviceManager(this.deviceManager);
        this.nextBoard.setSoundManager(this.soundManager);
        post();
    }

    public void post() {
        if (this.currentBoard != null) {
            this.currentBoard.finalizeBoard();
            this.currentBoard = null;
        }
        this.currentBoard = this.nextBoard;
        this.currentBoard.initBoard();
        this.currentBoardState = true;
    }

    public void post(Board board) {
        if (this.currentBoard != null) {
            this.currentBoard.finalizeBoard();
        }
        this.currentBoard = board;
        this.currentBoard.setDeviceManager(this.deviceManager);
        this.currentBoard.setSoundManager(this.soundManager);
        new Thread() {
            @Override
            public void run() {
                BoardManager.this.currentBoard.initBoard();
                BoardManager.this.currentBoardState = true;
            }
        }.start();
    }

    void releaseBoard() {
        this.currentBoard.finalizeBoard();
        this.currentBoard = null;
        this.currentBoardState = false;
    }

    boolean isOk() {
        return currentBoardState;
    }

    Board getBoard() {
        return this.currentBoard;
    }
}
