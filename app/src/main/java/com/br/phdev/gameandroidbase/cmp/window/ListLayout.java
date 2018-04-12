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
 * Este tipo cria um layout que exibe os componentes alinhados verticalmente ou horizontalmente.
 */
public class ListLayout implements Layout{

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
     * Espaçamento vertical entre o componente e a borda do layout.
     */
    private int spaceV = GameParameters.getInstance().screenSize.height() / 50;

    /**
     * Espaçamento horizontal entre o componente e a borda do layout.
     */
    private int spaceH = spaceV;

    /**
     * Entidade consumidora do layout. (Exemplo: Menu)
     */
    private Entity entity;

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

    public ListLayout(int alignment, int spaceH, int spaceV) {
        this.alignment = alignment;
        this.spaceV = spaceV;
        this.spaceH = spaceH;
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
        if (this.alignment == HORIZONTAL_ALIGNMENT) {
            int height = this.entity.getArea().height() - this.spaceV * 2;
            int cmpWidth = (this.entity.getArea().width() - this.spaceH * (tmpEntities.size()+1)) / tmpEntities.size();
            for (int i=0; i<tmpEntities.size(); i++) {
                Entity tmpEnt = tmpEntities.get(i);
                tmpEnt.setArea(new Rect(
                        (this.spaceH + this.spaceH *i) + x + (i * cmpWidth),
                        this.spaceV + y,
                        (this.spaceH + this.spaceH *i) + x + ((i+1) * cmpWidth),
                        this.spaceV + y + height));
            }
        } else if (this.alignment == VERTICAL_ALIGNMENT) {
            int width = this.entity.getArea().width() - this.spaceH * 2;
            int cmpHeight = (this.entity.getArea().height() - this.spaceV * (tmpEntities.size()+1)) / tmpEntities.size();
            for (int i=0; i<tmpEntities.size(); i++) {
                Entity ent = tmpEntities.get(i);
                ent.setArea(new Rect(
                        this.spaceH + x,
                        (this.spaceV + this.spaceV *i) + y + (i * cmpHeight),
                        this.spaceH + x + width,
                        (this.spaceV + this.spaceV *i) + y + ((i+1) * cmpHeight)));
            }
        }
    }


}
