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
 * Classe responsavel pela criação de sons curtos (normalmente efeitos sonoros).
 * @version 1.0
 */
public class ShortSound {

    /**
     * Id de recurso do som.
     */
    private int resourceId;

    /**
     * Id do som no {@link android.media.SoundPool}
     */
    private int poolId;

    /**
     * Volume esquerdo e direito da musica. (vai de 0 a 1).
     */
    private float leftVolume;
    private float rightVolume;

    /**
     * Quantidades de loops do som. (0 sem loop, -1 loop infinito).
     */
    private int loop;

    /**
     * Taxa de reprodução do som (1.0 normal, varia de 0.5 e vai ate 2.0)
     */
    private float rate;

    /**
     * Cria um som.
     * @param resourceId id de recurso associado ao som.
     * @param leftVolume volume esquerdo do som. (vai de 0 a 1).
     * @param rightVolume volume direito do som. (vai de 0 a 1).
     * @param loop quantidades de loops do som. (0 sem loop, -1 loop infinito).
     * @param rate taxa de reprodução do som (1.0 normal, varia de 0.5 e vai ate 2.0).
     */
    public ShortSound(int resourceId, float leftVolume, float rightVolume, int loop, float rate) {
        this.resourceId = resourceId;
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        this.loop = loop;
        this.rate = rate;
    }

    /**
     * Retorna o id de recurso do som.
     * @return id de recurso do som.
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * Redefine o id de recurso do som.
     * @param resourceId id de recurso para o som.
     */
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Retorna o id do som no {@link android.media.SoundPool}
     * @return id do som.
     */
    public int getPoolId() {
        return poolId;
    }

    /**
     * Redefine o id do som no {@link android.media.SoundPool}
     * @param poolId id para o som
     */
    @Deprecated
    public void setPoolId(int poolId) {
        this.poolId = poolId;
    }

    /**
     * Retorna o volume esquerdo do som.
     * @return volume esquerdo do som.
     */
    public float getLeftVolume() {
        return leftVolume;
    }

    /**
     * Redefine o volume esquerdo do som.
     * @param leftVolume volume esquerdo para o som.
     */
    public void setLeftVolume(float leftVolume) {
        this.leftVolume = leftVolume;
    }

    /**
     * Retorna  o volume direito do som.
     * @return volume direito do som.
     */
    public float getRightVolume() {
        return rightVolume;
    }

    /**
     * Redefine o volume direito do som.
     * @param rightVolume volume direito para o som.
     */
    public void setRightVolume(float rightVolume) {
        this.rightVolume = rightVolume;
    }

    /**
     * Retorna a quantidade de loops definidos para o som.
     * @return quantidade de loops (0 sem loop, -1 loop infinito).
     */
    public int getLoop() {
        return loop;
    }

    /**
     * Redefine a quantidade de loops definidos para o som.
     * @param loop quantidade de loops (0 sem loop, -1 loop infinito).
     */
    public void setLoop(int loop) {
        this.loop = loop;
    }

    /**
     * Retorna a taxa de reprodução do som.
     * @return taxa de reprodução (1.0 normal, varia de 0.5 e vai ate 2.0).
     */
    public float getRate() {
        return rate;
    }

    /**
     * Redefine a taxa de reprodução do som.
     * @param rate taxa de reprodução (1.0 normal, varia de 0.5 e vai ate 2.0).
     */
    public void setRate(float rate) {
        this.rate = rate;
    }
}
