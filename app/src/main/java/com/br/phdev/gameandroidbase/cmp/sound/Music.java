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
 *
 */

package com.br.phdev.gameandroidbase.cmp.sound;

/**
 * Classe responsavel pela criação de musicas, com algumas informações.
 * @version 1.0
 */
public class Music {

    /**
     * Id do recurso.
     */
    private int resourceId;

    /**
     * Informação sobre a musica.
     */
    private String info;

    /**
     * Volume esquerdo e direito da musica. (vai de 0 a 1).
     */
    private float leftVolume;
    private float rightVolume;

    /**
     * Cria uma musica.
     * @param resourceId id de recurso associado a esta musica.
     * @param info informação sobre esta musica.
     * @param leftVolume volume esquerdo da musica. (vai de 0 a 1).
     * @param rightVolume volume direito da musica. (vai de 0 a 1).
     */
    public Music(int resourceId, String info, float leftVolume, float rightVolume) {
        this.resourceId = resourceId;
        this.info = info;
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
    }

    /**
     * Retorna o id de recurso da musica.
     * @return id de recurso.
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * Redefine o id de recurso desta musica.
     * @param resourceId
     */
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Retorna a informação da musica.
     * @return informação da musica.
     */
    public String getInfo() {
        return info;
    }

    /**
     * Redefine a informação da musica.
     * @param info informação para a musica.
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Retorna o volune esquerdo da musica.
     * @return volume esquerdo.
     */
    public float getLeftVolume() {
        return leftVolume;
    }

    /**
     * Redefine o volume esquerdo da musica.
     * @param leftVolume volume esquerdo para a musica.
     */
    public void setLeftVolume(float leftVolume) {
        this.leftVolume = leftVolume;
    }

    /**
     * Retorna o volume direito da musica.
     * @return volume direito.
     */
    public float getRightVolume() {
        return rightVolume;
    }

    /**
     * Redefine o volume direito da musica.
     * @param rightVolume volume direito para a musica.
     */
    public void setRightVolume(float rightVolume) {
        this.rightVolume = rightVolume;
    }
}
