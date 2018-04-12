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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.br.phdev.gameandroidbase.cmp.environment.Screen;
import com.br.phdev.gameandroidbase.test.GameScreen;

/**
 * View aplicada na activity, contendo os metodos base para o loop do jogo.
 */
public class GameEngine extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Thread principal do jogo, que roda o main loop.
     */
    private MainThread mainThread;

    /**
     * Tela atual do jogo.
     */
    private Screen screen;

    /**
     * Gerenciador de audio do jogo.
     */
    private SoundManager soundManager;

    /**
     * Aplica o contexto da activity na view e repassa callback para comunicação entre ambas.
     * Tambem instancia a {@link MainThread}.
     * @param context contexto da activity.
     */
    public GameEngine(Context context) {
        super(context);
        getHolder().addCallback(this);

        if (this.mainThread == null) {
            GameLog.debug(this, "MainThread criada");
            this.mainThread = new MainThread(this);
        }

        setFocusable(true);
    }

    /**
     * Quando a aplicação é aberta e colocada em foco. Inicializa os componentes da primeira tela do jogo e apóes,
     * inicia a {@link MainThread} e define o status de rodando do main-loop como verdadeiro.
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        GameLog.debug(this, "Surface criada.");

        try {
            initComponents();
        } catch (Exception e) {
            GameLog.error(this, e.getMessage());
        }

        GameLog.debug(this, "MainThread iniciada");
        this.mainThread.start();

        GameLog.debug(this, "Loop da MainThread ativado");
        this.mainThread.setRunning(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        GameLog.debug(this, "Surface modificada");
    }

    /**
     * Quando a aplicação é encerrada. Finaliza os componentes atuais criados e depois tenta encerrar a {@link MainThread}.
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        GameLog.debug(this, "Surface destruida");

        boolean retry = true;
        while (retry) {
            if (this.mainThread != null) {
                GameLog.debug(this, "Finalizando componentes");
                finalizeComponents();

                GameLog.debug(this, "Loop da MainThread desativado");
                this.mainThread.setRunning(false);

                try {
                    this.mainThread.join();
                    GameLog.debug(this, "MainThread destruida");
                } catch (InterruptedException ie) {
                    GameLog.error(this, ie.getMessage());
                } finally {
                    retry = false;
                }
            }
        }
    }

    /**
     * Desenha a tela no canvas.
     * @param canvas
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        if (this.screen != null)
            this.screen.draw(canvas);
    }

    /**
     * Atualiza a tela.
     */
    public void update() {
        if (this.screen != null)
            this.screen.update();
    }

    /**
     * Recebe e envia eventos de entrada de dados do usuario para a tela.
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.screen != null)
            this.screen.onTouchEvent(motionEvent);
        return true;
    }

    /**
     * Inicializa os componentes principais do jogo, como tela e gerenciador de audio.
     */
    private void initComponents() {
        this.soundManager = new SoundManager(getContext());
        this.screen = new GameScreen(0,0, GameParameters.getInstance().screenSize.right, GameParameters.getInstance().screenSize.bottom);
        this.screen.setSoundManager(this.soundManager);
        this.screen.init();
    }

    /**
     * Finaliza os componentes atuais do jogo.
     */
    private void finalizeComponents() {
        this.screen = null;
        this.soundManager.release();
    }

    /**
     * Classe responsavel pela thread principal do jogo, que contem o main-loop.
     */
    private class MainThread extends Thread {

        /**
         * Taxa de atualização dos frames desejada.
         */
        private int FPS = 60;

        /**
         * FPS atual alcançado.
         */
        private int averageFPS;

        /**
         * Estado de funcionamento de main-loop.
         */
        private boolean running;

        /**
         * Canvas que recebe os desenhos dos componetes para então desenha-los.
         */
        private Canvas canvas;

        private final SurfaceHolder surfaceHolder;
        private GameEngine gameEngine;

        MainThread(GameEngine gameEngine) {
            this.surfaceHolder = gameEngine.getHolder();
            this.gameEngine = gameEngine;
            this.running = false;
        }

        @Override
        public void run() {

            long startTime;
            long timeMillis;
            long waitTime;
            long totalTime = 0;
            long frameCount = 0;
            long targetTime = 1000/FPS;

            while (this.running) {
                startTime = System.nanoTime();
                this.canvas = null;
                try {
                    this.canvas =  this.surfaceHolder.lockCanvas();
                    synchronized (this.surfaceHolder) {
                        this.gameEngine.update();
                        this.gameEngine.draw(this.canvas);
                    }
                } catch (Exception e) {
                    GameLog.error(this, e.getMessage());
                } finally {
                    if (canvas != null) {
                        try {
                            this.surfaceHolder.unlockCanvasAndPost(this.canvas);
                        } catch (Exception e) {
                            GameLog.error(this, e.getMessage());
                        }
                    }
                }
                timeMillis = (System.nanoTime() - startTime) / 1000000;
                waitTime = targetTime - timeMillis;
                try {
                    sleep(waitTime);
                } catch (Exception ie) {
                    //GameLog.e(this.getClass().getAliasId(), ie.getMessage());
                    //ie.printStackTrace();
                }
                totalTime += System.nanoTime() - startTime;
                frameCount++;
                if (frameCount == FPS) {
                    averageFPS = (int)(1000/((totalTime/frameCount)/1000000));
                    frameCount = 0;
                    totalTime = 0;
                }
            }

        }

        void setRunning(boolean running) {
            this.running = running;
        }

    }

}
