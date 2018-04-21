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

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.cmp.Entity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe responsavel pela criação de janelas.
 */
public class Window extends WindowEntity implements Formable {

    /**
     * Lista de entidades contidas na janela.
     */
    private ArrayList<WindowEntity> entities = new ArrayList<>();
    private Queue<WindowEntity> entitiesForAdd = new LinkedList<>();

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
        //this.layout = new ListLayout(ListLayout.VERTICAL_ALIGNMENT);
        //this.layout.set(this);
    }

    /**
     * Cria uma nova janela.
     * Layout padrão: {@link ListLayout} com alinhamento vertical.
     * @param x posição no eixo x da janela.
     * @param y posição no eixo y da janela.
     * @param width largura da janela.
     * @param height altura da janela.
     */
    public Window(float x, float y, float width, float height) {
        super(new RectF(x, y, x+ width, y + height));
        //this.layout = new ListLayout(ListLayout.VERTICAL_ALIGNMENT);
        //this.layout.set(this);
    }

    /**
     * Cria uma nova janela.
     * Layout padrão: sem.
     * @param area area para a janela.
     * @param layout novo {@link Layout} para a janela.
     */
    public Window(RectF area, Layout layout) {
        super(area);
        this.layout = layout;
        this.layout.set(this);
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
        if (this.layout != null)
            this.layout.format();
    }

    public void add(ArrayList<WindowEntity> windowEntities) {
        this.entities.addAll(windowEntities);
        if (this.layout != null)
            this.layout.format();
    }

    /**
     * Adiciona um componente na janela. (Usado mais especificamente quando o layout é do tipo {@link BorderLayout}.
     * Caso não seja, é adicionado normalmente fazendo a chamada para add(entity).
     * @param borderLayoutSide posição no layout para inserir o componente.
     * @param windowEntity componente a ser adicionado (Exemplo: botão, label, etc).
     */
    public void add(int borderLayoutSide, WindowEntity windowEntity) {
        if (this.layout instanceof BorderLayout) {
            ((BorderLayout)this.layout).getPanel(borderLayoutSide).add(windowEntity);
            this.entities.add(windowEntity);
            if (this.layout != null)
                this.layout.format();
        } else {
            this.add(windowEntity);
        }
    }

    /**
     * Adiciona um componente na janela em tempo de execução, evitando {@link ConcurrentModificationException}.
     * @param windowEntity componente a ser adicionado (Exemplo: botão, label, etc).
     */
    public void safeAdd(WindowEntity windowEntity) {
        registerBeforeAdd(windowEntity);
    }

    /**
     * Adiciona um componente na janela. em tempo de execução, evitando {@link ConcurrentModificationException}.
     * (Usado mais especificamente quando o layout é do tipo {@link BorderLayout}.
     * Caso não seja, é adicionado normalmente fazendo a chamada para add(entity).
     * @param borderLayoutSide posição no layout para inserir o componente.
     * @param windowEntity componente a ser adicionado (Exemplo: botão, label, etc).
     */
    public void safeAdd(int borderLayoutSide, WindowEntity windowEntity) {
        if (this.layout instanceof BorderLayout) {
            ((BorderLayout)this.layout).getPanel(borderLayoutSide).add(windowEntity);
            this.registerBeforeAdd(windowEntity);
        } else {
            this.registerBeforeAdd(windowEntity);
        }
    }

    /**
     * Para adicionar componentes na lista de entidades, os componentes são provisoriamente adicionados em uma
     * segunda lista que é inserida na primeira apos o termino da iteração da mesma.
     * @param windowEntity entidade a ser adicionada na lista segura.
     */
    private void registerBeforeAdd(WindowEntity windowEntity) {
        this.entitiesForAdd.add(windowEntity);
    }

    /**
     * Remove um componente da janela.
     * @param windowEntity componente a ser removido (Exemplo: botão, label, etc).
     */
    public void remove(WindowEntity windowEntity) {
        this.entities.remove(windowEntity);
    }

    public ArrayList<WindowEntity> get() {
        return this.entities;
    }

    /**
     * Retorna um componente especifico
     * @param index index do componente na lista.
     * @return componente requisitado.
     */
    @Override
    public Entity get(int index) {
        return entities.get(index);
    }

    @Override
    public int size() {
        return this.entities.size();
    }

    @Override
    public void update() {
        for (Entity ent : entities)
            if (ent.isActive())
                ent.update();
        if (this.entitiesForAdd.size() > 0) {
            this.entities.addAll(this.entitiesForAdd);
            this.entitiesForAdd.clear();
            if (this.layout != null)
                this.layout.format();
        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

}
