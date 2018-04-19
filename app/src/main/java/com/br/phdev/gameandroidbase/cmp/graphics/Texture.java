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
package com.br.phdev.gameandroidbase.cmp.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.GameParameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe responsavel pela criação de texturas, que podem ser consumidas por sprites, ou diretamente por componentes.
 * @version 1.0
 */
public class Texture {

    public final static int DEFAULT_WIDTH = -1;
    public final static int DEFAULT_HEIGHT = -1;

    /**
     * Qualidade de compressão do bitmap.
     */
    private final static int BITMAP_QUALITY = 20;

    /**
     * Bitmap utilizado pela textura.
     */
    private Bitmap bitmap;

    /**
     * Largura e altura da textura.
     */
    private int width, height;

    /**
     * Cria uma textura.
     * @param path caminho da textura.
     */
    public Texture(String path) throws IOException {
        this.bitmap = openImage(path, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    @Deprecated
    public Texture(String path, int reqWidth, int reqHeight) throws IOException {
        this.bitmap = openImage(path, reqWidth, reqHeight);
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    @Deprecated
    public Texture(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    /**
     * Retorna o bitmap utilizado pela textura.
     * @return bitmap utilizado.
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void release() {
        this.bitmap.recycle();
    }

    /**
     * Retorna a largura da textura.
     * @return largura.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Retorna a altura da textura.
     * @return altura.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Abre um bitmap a partir de um caminho valido fornecido.
     * @param path caminho contendo o bitmap para a textura.
     * @param reqWidth use DEFAULT_WIDTH para carregar a largura original da imagem, ou forneça uma largura para ser expansionada ou comprimida.
     * @param reqHeight use DEFAULT_WIDTH para carregar a altura original da imagem, ou forneça uma altura para ser expansionada ou comprimida.
     * @return retorna o bitmap carregado.
     */
    private static Bitmap openImage(String path, int reqWidth, int reqHeight) throws IOException {
        ByteArrayOutputStream finalBuffer = new ByteArrayOutputStream();
        InputStream buffer = GameParameters.getInstance().assetManager.open(path);
        int data = buffer.read();
        while (data != -1) {
            finalBuffer.write(data);
            data = buffer.read();
        }
        finalBuffer.flush();
        return byteArrayToBitmap(finalBuffer, reqWidth, reqHeight);
    }

    /**
     * Recorta a textura.
     * @param x posição x do recorte.
     * @param y posição y do recorte.
     * @param width largura do recorte.
     * @param height altura do recorte.
     */
    public void clipMe(int x, int y, int width, int height) {
        try {
            this.bitmap = Bitmap.createBitmap(this.bitmap, x, y, width, height);
        } catch (Exception e) {
            GameLog.error(this, e.getMessage());
        }
    }

    /**
     * Escala a textura.
     * @param width nova largura para a textura.
     * @param height nova altura para a textura.
     */
    public void scaleMe(int width, int height) {
        try {
            this.bitmap = Bitmap.createScaledBitmap(this.bitmap, width, height, false);
        } catch (Exception e) {
            GameLog.error(this, e.getMessage());
        }
    }

    /**
     * Carrega um bitmap a partir de um {@link ByteArrayOutputStream}.
     * @param buffer {@link ByteArrayOutputStream} que contem a textura.
     * @param reqWidth use DEFAULT_WIDTH para carregar a largura original da imagem, ou forneça uma largura para ser expansionada ou comprimida.
     * @param reqHeight use DEFAULT_WIDTH para carregar a altura original da imagem, ou forneça uma altura para ser expansionada ou comprimida.
     * @return retorna o bitmap carregado.
     */
    private static Bitmap byteArrayToBitmap(ByteArrayOutputStream buffer, int reqWidth, int reqHeight) {
        if (reqWidth != 1 && reqHeight != -1) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(buffer.toByteArray(), 0, buffer.size(), options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            ByteArrayOutputStream imagePosCompress = new ByteArrayOutputStream();

            Bitmap tempBitmap = BitmapFactory.decodeByteArray(buffer.toByteArray(), 0, buffer.size(), options);
            boolean result = tempBitmap.compress(Bitmap.CompressFormat.PNG, BITMAP_QUALITY, imagePosCompress);
            if (result)
                return BitmapFactory.decodeByteArray(imagePosCompress.toByteArray(), 0, imagePosCompress.size());
        }
        return BitmapFactory.decodeByteArray(buffer.toByteArray(), 0, buffer.size());
    }

    /**
     * Utilizado para calculos de compressão do bitmap.
     * @param options configurações do bitmap.
     * @param reqWidth nova largura do bitmap fornecida para o calculo.
     * @param reqHeight nova altura do bitmap fornecida para o calculo.
     * @return valor calculado para ser utilizado para compressão.
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
                inSampleSize *= 2;
        }
        return inSampleSize;
    }

}
