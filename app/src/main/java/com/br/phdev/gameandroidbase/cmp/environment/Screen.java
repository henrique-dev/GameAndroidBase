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
package com.br.phdev.gameandroidbase.cmp.environment;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameEngine;
import com.br.phdev.gameandroidbase.SoundManager;
import com.br.phdev.gameandroidbase.cmp.models.Component;
import com.br.phdev.gameandroidbase.cmp.models.Entity;

import java.util.ArrayList;

/**
 * Classe responsavel pela criação de telas, que fazem o intermedio entre a @{@link GameEngine} e as @{@link Scene}.
 * Possui uma lista com as cenas disponiveis no objeto de tela do contexto.
 * @version 1.0
 */
public abstract class Screen extends Entity implements Component {

    /**
     * Gerenciador de audio do jogo.
     */
    protected SoundManager soundManager;

    /**
     * Lista de cenas.
     */
    private ArrayList<Scene> scenes;

    /**
     * Cria uma tela, que ira conter as cenas relativo ao seu contexto.
     * @param x posição x da tela.
     * @param y posição y da tela.
     * @param width largura da tela.
     * @param height altura da tela.
     */
    protected Screen(int x, int y, int width, int height) {
        super(new Rect(x, y, x + width, y + height));
        this.scenes = new ArrayList<>();
    }

    /**
     * Metodo onde todas as criações e inicialização de componentes do jogo devem ser feitas.
     */
    public abstract void init();

    /**
     * Adiciona uma cena na tela.
     *
     * @param scene cena a ser adicionada.
     */
    protected void addScene(Scene scene) {
        scene.setSoundManager(this.soundManager);
        this.scenes.add(scene);
    }

    /**
     * Remove determinada cena da tela.
     *
     * @param scene cena a ser removida.
     */
    protected void removeScene(Scene scene) {
        this.scenes.remove(scene);
    }

    /**
     * Pega determinada cena da tela.
     * @param index posição da cena na lista.
     * @return determinada cena da lista.
     */
    protected Scene getScene(int index) {
        return this.scenes.get(index);
    }

    /**
     * Pega a lista de cenas.
     * @return a lista de cenas da tela.
     */
    protected ArrayList<Scene> getScenes() {
        return this.scenes;
    }

    /**
     * Define o gerenciador de audio da tela.
     * @param soundManager gerenciador de audio para a tela.
     */
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    @Override
    public void update() {
        for (Scene sc : this.scenes)
            if (sc.isActive())
                sc.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        canvas.drawRect(super.area, super.defaultPaint);
        for (Scene sc : this.scenes) {
            if (sc.isVisible())
                sc.draw(canvas);
        }
        canvas.restoreToCount(savedState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        for (Scene sc : this.scenes)
            if (sc.isActive())
                sc.onTouchEvent(motionEvent);
        return true;
    }
}
