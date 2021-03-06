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
 * Este tipo cria um layout parecido com o {@link GridLayout} porem,
 * redimensiona automaticamente o numero de linhas e colunas de acordo com a quantidade de componentes inseridos.
 */
public class SymmetricGrowLayout extends Layout {

    @Override
    public void format() {
        Formable componentsSource = ((Formable)super.entity);
        if (componentsSource == null || !(componentsSource.size() > 0))
            return;

        int size = componentsSource.size();
        int rows = 1;
        int columns = 1;

        while (rows * columns < size) {
            rows++;
            if (rows * columns >= size)
                break;
            columns++;
        }

        float x = super.entity.getArea().left;
        float y = super.entity.getArea().top;

        float cmpHeight = (super.entity.getArea().height() - super.spaceV * (rows+2)) / rows;
        float cmpWidth = (super.entity.getArea().width() - super.spaceH * (columns+2)) / columns;

        int counter = 0;

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                Entity ent = componentsSource.get(counter);
                ent.setArea( new RectF(
                        (super.spaceH + super.spaceH * j) + super.spaceH + x + (j * cmpWidth),
                        (super.spaceV + super.spaceV * i) + super.spaceV + y + (i * cmpHeight),
                        (super.spaceH + super.spaceH * j) + x + ((j+1) * cmpWidth),
                        (super.spaceV + super.spaceV * i) + y + ((i+1) * cmpHeight)));
                counter++;
                if ((componentsSource.size() == counter) || (counter == (rows * columns))) {
                    return;
                }
            }
        }
    }
}
