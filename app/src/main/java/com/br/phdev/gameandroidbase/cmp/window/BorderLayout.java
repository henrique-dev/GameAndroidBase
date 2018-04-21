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

import android.graphics.Rect;
import android.graphics.RectF;

import com.br.phdev.gameandroidbase.cmp.Entity;

/**
 * Classe responsavel pela criação de layouts para menus.
 * Este tipo cria um layout que que pode adicionar componentes em lugares especificos do mesmo como no centro, bordas horizontais ou verticais.
 */
public class BorderLayout extends Layout {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;
    public static final int CENTER = 4;

    /**
     * Paineis que acomodam componentes nas bordas direita, esquerda, cima, baixo e tambem no centro.
     */
    private Window leftPanel, rightPanel, topPanel, bottomPanel, centerPanel;

    /**
     * Retorna uma painel para adicionar componentes.
     * @param side localização do painel: LEFT, RIGHT, CENTER, TOP ou BOTTOM.
     * @return Painel para adicionar os componentes.
     */
    public Window getPanel(int side) {
        switch (side) {
            case LEFT:
                if (this.leftPanel == null) {
                    this.leftPanel = new Window(new RectF(
                            0,
                            (super.entity.getArea().height()/8),
                            (super.entity.getArea().width()/8),
                            (super.entity.getArea().height()/8)*7),
                            new ListLayout(ListLayout.VERTICAL_ALIGNMENT, 0, 0));
                    this.leftPanel.getLayout().set(this.leftPanel);
                }
                return this.leftPanel;
            case RIGHT:
                if (this.rightPanel == null) {
                    this.rightPanel = new Window(new RectF(
                            (super.entity.getArea().width()/8)*7,
                            (super.entity.getArea().height()/8),
                            (super.entity.getArea().width()),
                            (super.entity.getArea().height()/8)*7),
                            new ListLayout(ListLayout.VERTICAL_ALIGNMENT, 0, 0));
                    this.rightPanel.getLayout().set(this.rightPanel);
                }
                return this.rightPanel;
            case TOP:
                if (this.topPanel == null) {
                    this.topPanel = new Window(new RectF(
                            0,
                            0,
                            (super.entity.getArea().width()),
                            (super.entity.getArea().height()/8)),
                            new ListLayout(ListLayout.HORIZONTAL_ALIGNMENT, 0, 0));
                    this.topPanel.getLayout().set(this.topPanel);
                }
                return this.topPanel;
            case BOTTOM:
                if (this.bottomPanel == null) {
                    this.bottomPanel = new Window(new RectF(
                            0,
                            (super.entity.getArea().height()/8) * 7,
                            (super.entity.getArea().width()),
                            (super.entity.getArea().height())),
                            new ListLayout(ListLayout.HORIZONTAL_ALIGNMENT, 0, 0));
                    this.bottomPanel.getLayout().set(this.bottomPanel);
                }
                return this.bottomPanel;
            case CENTER:
                if (this.centerPanel == null) {
                    this.centerPanel = new Window(new RectF(
                            (super.entity.getArea().width()/8),
                            (super.entity.getArea().height()/8),
                            (super.entity.getArea().width()/8)*7,
                            (super.entity.getArea().height()/8) * 7),
                            new SymmetricGrowLayout());
                    this.centerPanel.getLayout().set(this.centerPanel);
                }
                return this.centerPanel;
            default:
                return null;
        }
    }

    @Override
    public void set(Entity entity) {
        super.entity = entity;
    }

    @Override
    public void format() {
        if (this.leftPanel != null)
            this.leftPanel.getLayout().format();
        if (this.rightPanel != null)
            this.rightPanel.getLayout().format();

    }
}
