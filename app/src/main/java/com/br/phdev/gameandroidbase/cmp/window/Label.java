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

import com.br.phdev.gameandroidbase.cmp.models.WindowEntity;

/**
 * Classe responsavel pela criação de labels para janelas.
 * (Entidades que exibem algo, ou não, e não interagiveis por padrão).
 */
public class Label extends WindowEntity {

    /**
     * Cria um label em uma area.
     * @param x posição no eixo x do label.
     * @param y posição no eixo y do label.
     * @param width largura do label.
     * @param height altura do label.
     */
    public Label(int x, int y, int width, int height) {
        super(new Rect(x, y, x + width, y + height));
    }

    /**
     * Cria um label em uma area.
     * @param area area para o label.
     */
    public Label(Rect area) {
        super(area);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }
}
