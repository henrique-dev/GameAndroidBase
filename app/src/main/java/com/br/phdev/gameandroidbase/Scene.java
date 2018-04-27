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

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.cmp.Controllable;
import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.GameEntity;
import com.br.phdev.gameandroidbase.cmp.listeners.OnConfigurationChangedListener;
import com.br.phdev.gameandroidbase.cmp.window.WindowEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe responsavel pela criação de cenas, que faz o intermedio entre a classe @{@link Board} e as classes @{@link Entity}.
 * Possui duas listas, uma com objetos da janela e outra com objetos de in-game.
 * @version 1.0
 */
public abstract class Scene extends Entity implements Controllable, OnConfigurationChangedListener {

    private Board parentBoard;

    /**
     * Gerenciador de audio do jogo.
     */
    private SoundManager soundManager;

    /**
     * Gerenciador de dispositivos do jogo. (Teclado, etc.)
     */
    private DeviceManager deviceManager;

    private ConnectionManager connectionManager;

    /**
     * Lista de objetos out-game.
     */
    private ArrayList<WindowEntity> windowEntities;

    /**
     * Lista de objetos in-game.
     */
    private ArrayList<GameEntity> gameEntities;

    /**
     * Cria uma cena, que ira conter todos os objetos relativos a determinado contexto.
     * @param x posição x da cena.
     * @param y posição y da cena
     * @param width largura da cena.
     * @param height altura da cena.
     */
    public Scene(float x, float y, float width, float height) {
        super(new RectF(x, y, x + width, y + height));
        this.windowEntities = new ArrayList<>();
        this.gameEntities = new ArrayList<>();
    }

    protected Scene(RectF area) {
        super(area);
        this.windowEntities = new ArrayList<>();
        this.gameEntities = new ArrayList<>();
    }

    /**
     * Metodo onde todas as criações e inicialização de componentes do jogo devem ser feitas.
     */
    public abstract void init();

    public abstract void finalizeScene();

    /**
     * Adiciona um objeto na cena.
     * @param windowEntity objeto a ser adicionado.
     */
    public void add(WindowEntity windowEntity) {
        this.windowEntities.add(windowEntity);
    }

    /**
     * Adiciona um objeto na cena.
     * @param gameEntity objeto a ser adicionado.
     */
    public void add(GameEntity gameEntity) {
        this.gameEntities.add(gameEntity);
    }

    /**
     * Define o gerenciador de audio.
     * @param soundManager gerenciador de audio.
     */
    void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    /**
     * Retorna o gerenciador de audio.
     * @return gerenciador de audio.
     */
    public SoundManager getSoundManager() {
        return this.soundManager;
    }

    /**
     * Define p gerenciador de dispositivos.
     * @param deviceManager gerenciador de dispositivos.
     */
    void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    /**
     * Retorna o gerenciador de dispositivos.
     * @return gerenciador de dispositivos.
     */
    public DeviceManager getDeviceManager() {
        return this.deviceManager;
    }

    void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    void setParentBoard(Board parentBoard) {
        this.parentBoard = parentBoard;
    }

    public Board getParentBoard() {
        return this.parentBoard;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    @Override
    public void update() {
        for (Entity ent : this.gameEntities)
            if (ent.isActive())
                ent.update();
        for (Entity ent : this.windowEntities)
            if (ent.isActive())
                ent.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int savedCount = canvas.save();
        //canvas.drawRect(super.area, super.defaultPaint);
        for (Entity ent : this.gameEntities)
            if (ent.isVisible())
                ent.draw(canvas);
        for (Entity ent : this.windowEntities)
            if (ent.isVisible())
                ent.draw(canvas);
        canvas.restoreToCount(savedCount);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        for (WindowEntity ent : this.windowEntities)
            if (ent.isActive())
                ent.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    public boolean keyBackPressed() {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

}
