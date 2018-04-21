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

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.cmp.Entity;

/**
 * Classe responsavel pela criação de layouts para menus.
 * Este tipo cria um layout que exibe os componentes alinhados verticalmente ou horizontalmente.
 */
public class ListLayout extends Layout{

    /**
     * Constantes para definir o tipo de alinhamento.
     */
    public static final int HORIZONTAL_ALIGNMENT = 0;
    public static final int VERTICAL_ALIGNMENT = 1;

    /**
     * Alinhamento atual.
     */
    private int alignment;

    /**
     * Cria um layout padrão (Alinhamento vertical dos componentes).
     */
    public ListLayout() {
        this.alignment = VERTICAL_ALIGNMENT;
    }

    /**
     * Cria um layout fornecendo o alinhamento desejado.
     * @param alignment tipo de alinhamento. ListLayout.HORIZONTAL_ALIGNMENT ou ListLayout.VERTICAL_ALIGNMENT
     */
    public ListLayout(int alignment) {
        this.alignment = alignment;
    }

    /**
     * Cria um layout fornecendo o alinhamento desejado e o tamanho dos espaçamentos horizonateis e verticais.
     * @param alignment tipo de alinhamento. ListLayout.HORIZONTAL_ALIGNMENT ou ListLayout.VERTICAL_ALIGNMENT
     * @param spaceH espaçamento horizontal.
     * @param spaceV espaçamento vertical.
     */
    public ListLayout(int alignment, int spaceH, int spaceV) {
        this.alignment = alignment;
        super.spaceV = spaceV;
        super.spaceH = spaceH;
    }

    @Override
    public void format() {
        Formable componentsSource = ((Formable)super.entity);
        if (componentsSource == null || !(componentsSource.size() > 0))
            return;

        float x = super.entity.getArea().left;
        float y = super.entity.getArea().top;
        if (this.alignment == HORIZONTAL_ALIGNMENT) {
            float height = super.entity.getArea().height() - super.spaceV * 2;
            float cmpWidth = (super.entity.getArea().width() - super.spaceH * (componentsSource.size()+1)) / componentsSource.size();
            for (int i=0; i<componentsSource.size(); i++) {
                Entity tmpEnt = componentsSource.get(i);
                tmpEnt.setArea(new RectF(
                        (super.spaceH + super.spaceH *i) + x + (i * cmpWidth),
                        super.spaceV + y,
                        (super.spaceH + super.spaceH *i) + x + ((i+1) * cmpWidth),
                        super.spaceV + y + height));
            }
        } else if (this.alignment == VERTICAL_ALIGNMENT) {

            float width = super.entity.getArea().width() - super.spaceH * 2;

            float cmpHeight = (super.entity.getArea().height() - super.spaceV * (componentsSource.size()+1)) / componentsSource.size();
            for (int i=0; i<componentsSource.size(); i++) {
                Entity ent = componentsSource.get(i);
                ent.setArea(new RectF(
                        super.spaceH + x,
                        (super.spaceV + super.spaceV * (float)i) + y + ((float)i * cmpHeight),
                        super.spaceH + x + width,
                        (super.spaceV + super.spaceV * (float)i) + y + ((float)(i+1) * cmpHeight)));
            }
        }
    }


}
