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

import com.br.phdev.gameandroidbase.GameParameters;
import com.br.phdev.gameandroidbase.cmp.Entity;

public class BorderLayout implements Layout {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;
    public static final int CENTER = 4;

    private Window leftPanel;
    private Window rightPanel;
    private Window topPanel;
    private Window bottomPanel;
    private Window centerPanel;

    private Layout LRLayout;
    private Layout TBLayout;

    private Entity entity;

    public BorderLayout() {
        LRLayout = new ListLayout(ListLayout.VERTICAL_ALIGNMENT);
        TBLayout = new ListLayout(ListLayout.HORIZONTAL_ALIGNMENT);
    }

    public Window getPanel(int side) {
        switch (side) {
            case LEFT:
                if (this.leftPanel == null) {
                    this.leftPanel = new Window(new Rect(
                            0,
                            (this.entity.getArea().height()/8),
                            (this.entity.getArea().width()/8),
                            (this.entity.getArea().height()/8)*7),
                            new ListLayout(ListLayout.VERTICAL_ALIGNMENT, 0, 0));
                    this.leftPanel.getLayout().set(this.leftPanel);
                }
                return this.leftPanel;
            case RIGHT:
                if (this.rightPanel == null) {
                    this.rightPanel = new Window(new Rect(
                            (this.entity.getArea().width()/8)*7,
                            (this.entity.getArea().height()/8),
                            (this.entity.getArea().width()),
                            (this.entity.getArea().height()/8)*7),
                            new ListLayout(ListLayout.VERTICAL_ALIGNMENT, 0, 0));
                    this.rightPanel.getLayout().set(this.rightPanel);
                }
                return this.rightPanel;
            case TOP:
                if (this.topPanel == null) {
                    this.topPanel = new Window(new Rect(
                            0,
                            0,
                            (this.entity.getArea().width()),
                            (this.entity.getArea().height()/8)),
                            new ListLayout(ListLayout.HORIZONTAL_ALIGNMENT, 0, 0));
                    this.topPanel.getLayout().set(this.topPanel);
                }
                return this.topPanel;
            case BOTTOM:
                if (this.bottomPanel == null) {
                    this.bottomPanel = new Window(new Rect(
                            0,
                            (this.entity.getArea().height()/8) * 7,
                            (this.entity.getArea().width()),
                            (this.entity.getArea().height())),
                            new ListLayout(ListLayout.HORIZONTAL_ALIGNMENT, 0, 0));
                    this.bottomPanel.getLayout().set(this.bottomPanel);
                }
                return this.bottomPanel;
            case CENTER:
                return this.centerPanel;
            default:
                return null;
        }
    }

    @Override
    public void set(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void format() {
        if (this.leftPanel != null)
            this.leftPanel.getLayout().format();
        if (this.rightPanel != null)
            this.rightPanel.getLayout().format();

    }
}
