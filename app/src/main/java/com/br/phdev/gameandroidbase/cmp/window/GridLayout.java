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
import com.br.phdev.gameandroidbase.cmp.models.Entity;

import java.util.ArrayList;

/**
 * Classe responsavel pela criação de layouts para menus.
 * Este tipo cira layouts em forma de grade (linha x coluna).
 */
public class GridLayout implements Layout {

    private int spaceH = GameParameters.getInstance().screenSize.width() / 50;
    private int spaceV = spaceH;

    private Entity entity;

    private int rows, columns;

    public GridLayout(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public void set(Entity entity) {
        this.entity = entity;
        this.format();
    }

    @Override
    public void format() {
        ArrayList<Entity> tmpEntities = ((Window)this.entity).get();
        if (!(tmpEntities.size() > 0))
            return;

        int x = this.entity.getArea().left;
        int y = this.entity.getArea().top;

        int cmpHeight = (this.entity.getArea().height() - this.spaceV * (this.rows+2)) / this.rows;
        int cmpWidth = (this.entity.getArea().width() - this.spaceH * (this.columns+2)) / this.columns;

        int counter = 0;

        for (int i=0; i<this.columns; i++) {
            for (int j=0; j<this.rows; j++) {
                Entity ent = tmpEntities.get(counter);
                ent.setArea( new Rect(
                        (this.spaceH + this.spaceH * j) + this.spaceH + x + (j * cmpWidth),
                        (this.spaceV + this.spaceV * i) + this.spaceV + y + (i * cmpHeight),
                        (this.spaceH + this.spaceH * j) + x + ((j+1) * cmpWidth),
                        (this.spaceV + this.spaceV * i) + y + ((i+1) * cmpHeight)));
                counter++;
                if ((tmpEntities.size() == counter) || (counter == (this.rows * this.columns))) {
                    return;
                }
            }
        }
    }
}
