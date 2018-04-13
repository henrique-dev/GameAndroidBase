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

import com.br.phdev.gameandroidbase.SoundManager;
import com.br.phdev.gameandroidbase.cmp.Component;
import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.GameEntity;
import com.br.phdev.gameandroidbase.cmp.WindowEntity;

import java.util.ArrayList;

/**
 * Classe responsavel pela criação de cenas, que faz o intermedio entre a classe @{@link Screen} e as classes @{@link Entity}.
 * Possui duas listas, uma com objetos da janela e outra com objetos de in-game.
 * @version 1.0
 */
public abstract class Scene extends Entity implements Component {

    /**
     * Gerenciador de audio do jogo.
     */
    private SoundManager soundManager;

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
    protected Scene(int x, int y, int width, int height) {
        super(new Rect(x, y, x + width, y + height));
        this.windowEntities = new ArrayList<>();
        this.gameEntities = new ArrayList<>();
    }

    /**
     * Metodo onde todas as criações e inicialização de componentes do jogo devem ser feitas.
     */
    public abstract void init();

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
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    /**
     * Retorna o gerenciador de audio.
     * @return gerenciador de audio.
     */
    public SoundManager getSoundManager() {
        return this.soundManager;
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
        for (Entity ent : this.gameEntities)
            if (ent.isVisible())
                ent.draw(canvas);
        for (Entity ent : this.windowEntities)
            if (ent.isVisible())
                ent.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        for (Entity ent : this.windowEntities)
            if (ent.isActive())
                ent.onTouchEvent(motionEvent);
        return true;
    }


}
