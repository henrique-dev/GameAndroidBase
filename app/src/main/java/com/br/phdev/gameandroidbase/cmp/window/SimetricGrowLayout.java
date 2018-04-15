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

import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.WindowEntity;

import java.util.ArrayList;

public class SimetricGrowLayout extends Layout {


    @Override
    public void format() {
        ArrayList<WindowEntity> tmpEntities = ((Formable)super.entity).get();
        if (!(tmpEntities.size() > 0))
            return;

        int size = tmpEntities.size();
        int rows = 1;
        int columns = 1;

        while (rows * columns < size) {
            rows++;
            if (rows * columns >= size)
                break;
            columns++;
        }

        int x = super.entity.getArea().left;
        int y = super.entity.getArea().top;

        int cmpHeight = (super.entity.getArea().height() - super.spaceV * (rows+2)) / rows;
        int cmpWidth = (super.entity.getArea().width() - super.spaceH * (columns+2)) / columns;

        int counter = 0;

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                Entity ent = tmpEntities.get(counter);
                ent.setArea( new Rect(
                        (super.spaceH + super.spaceH * j) + super.spaceH + x + (j * cmpWidth),
                        (super.spaceV + super.spaceV * i) + super.spaceV + y + (i * cmpHeight),
                        (super.spaceH + super.spaceH * j) + x + ((j+1) * cmpWidth),
                        (super.spaceV + super.spaceV * i) + y + ((i+1) * cmpHeight)));
                counter++;
                if ((tmpEntities.size() == counter) || (counter == (rows * columns))) {
                    return;
                }
            }
        }
    }
}
