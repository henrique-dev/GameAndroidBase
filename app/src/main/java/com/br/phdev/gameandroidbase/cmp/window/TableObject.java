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

/**
 * Classe para criação de objetos(linhas) para a tabela.
 */
public final class TableObject {

    /**
     * Texto para o objeto.
     */
    private String name;

    /**
     * Objeto qualquer a ser repassado como evento para a escuta.
     */
    private Object object;

    /**
     * Cria um novo objeto(linha) para a tabela.
     * @param name texto para o objeto.
     */
    public TableObject(String name) {
        this.name = name;
    }

    /**
     * Cria um novo objeto(linha) para a tabela.
     * @param name texto para o objeto.
     * @param object objeto a ser repassado como evento para a escuta.
     */
    public TableObject(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    /**
     * Retorna o texto do objeto.
     * @return texto do objeto.
     */
    public String getName() {
        return name;
    }

    /**
     * Redefine o texto do objeto.
     * @param name texto para o objeto.
     */
    public void setName(String name) {
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
