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
import com.br.phdev.gameandroidbase.cmp.WindowEntity;

import java.util.ArrayList;

/**
 * Classe responsavel pela criação de layouts para menus.
 * Este tipo cira layouts em forma de grade (linha x coluna).
 */
public class GridLayout extends Layout {

    private int rows, columns;

    public GridLayout(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
    public void format() {
        ArrayList<WindowEntity> tmpEntities = ((Formable)super.entity).get();
        if (!(tmpEntities.size() > 0))
            return;

        int x = super.entity.getArea().left;
        int y = super.entity.getArea().top;

        int cmpHeight = (super.entity.getArea().height() - super.spaceV * (this.rows+2)) / this.rows;
        int cmpWidth = (super.entity.getArea().width() - super.spaceH * (this.columns+2)) / this.columns;

        int counter = 0;

        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.columns; j++) {
                Entity ent = tmpEntities.get(counter);
                ent.setArea( new Rect(
                        (super.spaceH + super.spaceH * j) + super.spaceH + x + (j * cmpWidth),
                        (super.spaceV + super.spaceV * i) + super.spaceV + y + (i * cmpHeight),
                        (super.spaceH + super.spaceH * j) + x + ((j+1) * cmpWidth),
                        (super.spaceV + super.spaceV * i) + y + ((i+1) * cmpHeight)));
                counter++;
                if ((tmpEntities.size() == counter) || (counter == (this.rows * this.columns))) {
                    return;
                }
            }
        }
    }
}
