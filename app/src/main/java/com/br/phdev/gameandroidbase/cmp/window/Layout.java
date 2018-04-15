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

import com.br.phdev.gameandroidbase.GameParameters;
import com.br.phdev.gameandroidbase.cmp.Entity;

/**
 * Interface responsavel pela os layouts aplicados em menus.
 */
public abstract class Layout {

    protected int spaceH = GameParameters.getInstance().screenSize.width() / 50;
    protected int spaceV = spaceH;

    protected Entity entity;

    /**
     * Define a entidade que sera incluida e formatada pelo layout.
     * @param entity entidade a ser incluida.
     */
    public void set(Entity entity) {
        this.entity = entity;
        this.format();
    }

    /**
     * Formata a entidade de acordo com o layout.
     */
    public abstract void format();

}
