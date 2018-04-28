package com.br.phdev.gameandroidbase.cmp.utils;

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
public class Timer {

    private long startTicks;
    private long pausedTicks;

    private boolean started, paused;

    public Timer() {
        this.startTicks = 0;
        this.pausedTicks = 0;
        this.paused = false;
        this.started = false;
    }

    public Timer start() {
        this.started = true;
        this.paused = false;
        this.startTicks = System.nanoTime();
        this.pausedTicks = 0;
        return this;
    }

    public void stop() {
        this.started = false;
        this.paused = false;
        this.startTicks = 0;
        this.pausedTicks = 0;
    }

    public void pause() {
        if (this.started && !this.paused) {
            this.paused = true;
            this.pausedTicks = System.nanoTime() - this.startTicks;
            this.startTicks = 0;
        }
    }

    public void unpause() {
        if (this.started && this.paused) {
            this.paused = false;
            this.startTicks = System.nanoTime() - this.pausedTicks;
            this.pausedTicks = 0;
        }
    }

    public long getTicksInNanoSeconds() {
        long time = 0;
        if (this.started) {
            if (this.paused) {
                time = this.pausedTicks;
            } else {
                time = System.nanoTime() - this.startTicks;
            }
        }
        return time;
    }

    public long getTicksInSeconds() {
        long time = 0;
        if (this.started) {
            if (this.paused) {
                time = this.pausedTicks;
            } else {
                time = System.nanoTime() - this.startTicks;
            }
        }
        return (int)(time / 1000000000);
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isPaused() {
        return paused;
    }
}
