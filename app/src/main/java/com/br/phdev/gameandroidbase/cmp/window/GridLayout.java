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
 * Este tipo cira layouts em forma de grade (linha x coluna).
 */
public class GridLayout extends Layout {

    /**
     * Quantidade linhas e colunas para o grid.
     */
    private int rows, columns;

    /**
     * Cria um novo layout.
     * @param rows numero de linhas do grid.
     * @param columns numero de colunas do grid.
     */
    public GridLayout(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Retorna o numero de linhas do grid.
     * @return numero de linhas.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Redefine o numero de linhas do grid.
     * @param rows
     */
    public void setRows(int rows) {
        this.rows = rows;
        this.format();
    }

    /**
     * Retorna o numero de colunas do grid.
     * @return numero de colunas.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Redefine o numero de colunas do grid.
     * @param columns numero de colunas.
     */
    public void setColumns(int columns) {
        this.columns = columns;
        this.format();
    }

    @Override
    public void format() {
        Formable componentsSource = ((Formable)super.entity);
        if (componentsSource == null || !(componentsSource.size() > 0))
            return;

        float x = super.entity.getArea().left;
        float y = super.entity.getArea().top;

        float cmpHeight = (super.entity.getArea().height() - super.spaceV * (this.rows+2)) / this.rows;
        float cmpWidth = (super.entity.getArea().width() - super.spaceH * (this.columns+2)) / this.columns;

        int counter = 0;

        for (int i=0; i<this.rows; i++) {
            for (int j=0; j<this.columns; j++) {
                Entity ent = componentsSource.get(counter);
                ent.setArea( new RectF(
                        (super.spaceH + super.spaceH * j) + super.spaceH + x + (j * cmpWidth),
                        (super.spaceV + super.spaceV * i) + super.spaceV + y + (i * cmpHeight),
                        (super.spaceH + super.spaceH * j) + x + ((j+1) * cmpWidth),
                        (super.spaceV + super.spaceV * i) + y + ((i+1) * cmpHeight)));
                counter++;
                if ((componentsSource.size() == counter) || (counter == (this.rows * this.columns))) {
                    return;
                }
            }
        }
    }
}
