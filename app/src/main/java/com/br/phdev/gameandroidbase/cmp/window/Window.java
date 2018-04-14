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
package com.br.phdev.gameandroidbase.cmp.window;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.WindowEntity;

import java.util.ArrayList;

/**
 * Classe responsavel pela criação de janelas.
 */
public class Window extends WindowEntity implements Formable {

    /**
     * Lista de entidades contidas na janela.
     */
    private ArrayList<WindowEntity> entities = new ArrayList<>();

    /**
     * Botão padrão para fechar a janela. (AINDA FALTA IMPLEMENTAR)
     */
    private Button closeButton;

    /**
     * Tipo de layout da janela.
     */
    private Layout layout;

    /**
     * Cria uma nova janela.
     * Layout padrão: {@link ListLayout} com alinhamento vertical.
     */
    public Window() {
        super();
        this.layout = new ListLayout(ListLayout.VERTICAL_ALIGNMENT);
    }

    /**
     * Cria uma nova janela.
     * Layout padrão: {@link ListLayout} com alinhamento vertical.
     * @param x posição no eixo x da janela.
     * @param y posição no eixo y da janela.
     * @param width largura da janela.
     * @param height altura da janela.
     */
    public Window(int x, int y, int width, int height) {
        super(new Rect(x, y, x+ width, y + height));
        this.layout = new ListLayout(ListLayout.VERTICAL_ALIGNMENT);
    }

    /**
     * Cria uma nova janela.
     * Layout padrão: sem.
     * @param area area para a janela.
     * @param layout novo {@link Layout} para a janela.
     */
    public Window(Rect area, Layout layout) {
        super(area);
        this.layout = layout;
    }

    /**
     * Redefine o layout da janela.
     * @param layout novo layout.
     */
    public void setLayout(Layout layout) {
        this.layout = layout;
        this.layout.set(this);
    }

    /**
     * Retorna o layout atual da janela.
     * @return layout da janela.
     */
    public Layout getLayout() {
        return this.layout;
    }

    /**
     * Adiciona um componente na janela.
     * @param windowEntity componente a ser adicionado (Exemplo: botão, label, etc).
     */
    public void add(WindowEntity windowEntity) {
        this.entities.add(windowEntity);
        this.layout.format();
    }

    /**
     * Remove um componente da janela.
     * @param entity componente a ser removido (Exemplo: botão, label, etc).
     */
    public void remove(Entity entity) {
        this.entities.remove(entity);
    }

    /**
     * Retorna um componente especifico
     * @param index index do componente na lista.
     * @return componente requisitado.
     */
    public Entity get(int index) {
        return entities.get(index);
    }

    /**
     * Retorna toda a lista de componentes contidos na janela.
     * @return lista de componentes da janela.
     */
    public ArrayList<WindowEntity> get() {
        return this.entities;
    }

    @Override
    public void update() {
        for (Entity ent : entities)
            if (ent.isActive())
                ent.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        canvas.drawRect(super.area, super.defaultPaint);
        for (Entity ent : entities)
            if (ent.isVisible())
                ent.draw(canvas);
        canvas.restoreToCount(savedState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        for (WindowEntity ent : entities) {
            if (ent.isActive())
                ent.onTouchEvent(motionEvent);
        }
        return true;
    }

}
