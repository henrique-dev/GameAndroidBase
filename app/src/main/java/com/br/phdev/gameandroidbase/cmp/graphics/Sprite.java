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
package com.br.phdev.gameandroidbase.cmp.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.GameParameters;
import com.br.phdev.gameandroidbase.cmp.models.Drawable;
import com.br.phdev.gameandroidbase.cmp.models.Entity;

/**
 * Classe responsavel por criar sprites a partir de determinada textura.
 * @version 1.0
 */
public class Sprite implements Drawable {

    private static boolean debugSprite = true;

    /**
     * Textura consumida pelo sprite.
     */
    private Texture texture;

    /**
     * Area da textura consumida pelo sprite.
     */
    private Rect imageClip;

    /**
     * {@link Paint} para o sprite.
     */
    private Paint paint;

    /**
     * {@link Paint} utilizado para debug.
     */
    private Paint debugPaint1, debugPaint2;

    /**
     * {@link Matrix} para aplicação de efeitos ao canvnas, como inversão, rotação, etc.
     */
    private Matrix matrix;

    /**
     * Estado atual de inversão do canvas.
     */
    private boolean invertH, invertV;

    /**
     * Grau rotacionado.
     */
    private float degrees;

    private Entity entity;

    /**
     * Cria um sprite a partir de uma textura e a área da textura.
     * @param texture textura consumida pelo sprite.
     * @param imageClip area da textura consumida pelo sprite.
     * @throws Error caso a textura ou a area inserida sejam nulas.
     */
    public Sprite(Texture texture, Rect imageClip, Entity entity) throws Exception {
        if (texture == null || imageClip == null || entity == null)
            throw new Exception("A textura, area ou entidade não podem ser nulos.");
        if (imageClip.width() == 0 || imageClip.height() == 0)
            throw new Exception("A area não pode ter largura igual a 0 ou altura igual a 0.");
        this.texture = texture;
        this.imageClip = imageClip;
        this.matrix = new Matrix();
        this.paint = new Paint();
        this.debugPaint1 = new Paint();
        this.debugPaint1.setColor(Color.GRAY);
        this.debugPaint2 = new Paint();
        this.debugPaint2.setColor(Color.RED);
        this.entity = entity;
    }

    /**
     * Redefine a textura e a área da textura do sprite.
     * @param texture textura consumida pelo sprite.
     * @param imageClip area da textura consumida pelo sprite.
     */
    public void set(Texture texture, Rect imageClip) throws Exception{
        if (texture == null || imageClip == null)
            throw new Exception("A textura ou a area não podem ser nulos.");
        if (imageClip.width() == 0 || imageClip.height() == 0)
            throw new Exception("A area não pode ter largura igual a 0 ou altura igual a 0.");
        this.texture = texture;
        this.imageClip = imageClip;
    }

    /**
     * Retorna a textura do sprite.
     * @return textura que o sprite consome.
     */
    public Texture getTexture() {
        return this.texture;
    }

    /**
     * Retorna area da textura consumida pelo sprite.
     * @return area da textura que o sprite consome.
     */
    public Rect getImageClip() {
        return this.imageClip;
    }

    /**
     * Redefine o @{@link Paint} do sprite.
     * @param paint novo @{@link Paint} para o sprite.
     */
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    /**
     * Retorna o @{@link Paint} utilizado pelo sprite.
     * @return @{@link Paint} do sprite.
     */
    public Paint getPaint() {
        return this.paint;
    }

    /**
     * Inverte a orientação horizontal do sprite.
     */
    public void invertH() {
        this.invertH = !invertH;
        this.matrix.postScale(-1, 1);
    }

    /**
     * Inverte a orientação vertical do sprite.
     */
    public void invertV() {
        this.invertV = !invertV;
        this.matrix.postScale(1, -1);
    }

    /**
     * Rotaciona o sprite.
     * @param degrees grau de inclinação do sprite.
     */
    @Deprecated
    public void rotate(float degrees) {
        this.degrees = degrees;
        this.matrix.postRotate(degrees, this.imageClip.centerX(), this.imageClip.centerY());
    }

    public void draw(Canvas canvas) {
        int savedState = canvas.save();

        int x = this.entity.getArea().left;
        int y = this.entity.getArea().top;

        if (invertV || invertH || degrees != 0) {
            canvas.setMatrix(this.matrix);
            canvas.translate(
                    this.invertH ? -GameParameters.getInstance().screenSize.width() : 0,
                    this.invertV ? -GameParameters.getInstance().screenSize.height() : 0
            );
            x = this.invertH ? GameParameters.getInstance().screenSize.width() - x - this.imageClip.width() : x;
            y = this.invertV ? GameParameters.getInstance().screenSize.height() - y - this.imageClip.height() : y;

            //canvas.rotate(this.degrees, this.imageClip.centerX(), this.imageClip.centerY());
        }

        if (debugSprite) {
            canvas.drawRect(new Rect(x, y, x + this.imageClip.width(), y + this.imageClip.height()), this.debugPaint1);
            canvas.drawCircle(x, y, 15, this.debugPaint2);
        }

        canvas.drawBitmap(this.texture.getBitmap(), this.imageClip, new Rect(x, y, x + this.imageClip.width(), y + this.imageClip.height()), this.paint);
        canvas.restoreToCount(savedState);
    }

    /**
     * Retorna os sprites de um spritesheet.
     * @param spriteSheet spritesheet que contem os sprites a serem retirados.
     * @param numberSpritesLines numero de linhas do spritesheet.
     * @param numberSpritesColumns numero de colunas do spritesheet.
     * @param maxSprites numero maximo de sprites contidos no spritesheet.
     * @return array de sprites retirados do spritesheet.
     */
    public static Sprite[] getSpriteFromTexture(Entity entity, Texture spriteSheet, int numberSpritesLines, int numberSpritesColumns, int maxSprites){
        int counter = 0;
        int spriteWidth = spriteSheet.getBitmap().getWidth() / numberSpritesColumns;
        int spriteHeight = spriteSheet.getBitmap().getHeight() / numberSpritesLines;
        Sprite sprites[] = new Sprite[maxSprites];
        int cont = 0;
        for(int i=0; i<numberSpritesLines; i++){
            for(int j=0; j<numberSpritesColumns; j++){
                try {
                    sprites[cont++] = new Sprite(spriteSheet, new Rect( j*spriteWidth, i*spriteHeight, (j*spriteWidth) + spriteWidth, (i*spriteHeight) + spriteHeight), entity);
                } catch (Exception e) {
                    GameLog.error(Sprite.class, e.getMessage());
                }
                counter++;
                if (counter == maxSprites)
                    break;
            }
        }
        return sprites;
    }

    /*
    @Deprecated
    public static Sprite[] getSpriteFromTexture(Texture texture, int numberSpritesLines, int numberSpritesColumns, int maxSprites){
        int counter = 0;
        int spriteWidth = texture.getBitmap().getWidth() / numberSpritesColumns;
        int spriteHeight = texture.getBitmap().getHeight() / numberSpritesLines;
        Sprite sprites[] = new Sprite[maxSprites];
        int cont = 0;
        for(int i=0; i<numberSpritesLines; i++){
            for(int j=0; j<numberSpritesColumns; j++){
                sprites[cont++] = new Sprite( new Texture( Bitmap.createBitmap(texture.getBitmap(), j*spriteWidth, i*spriteHeight, spriteWidth, spriteHeight)));
                counter++;
                if (counter == maxSprites)
                    break;
            }
        }

        return sprites;
    }
    */

    /**
     * Retira um array de sprites especificos.
     * @param sprites array de sprites base.
     * @param indexBegin index de oomeço de retirada dos sprites.
     * @param indexEnd index com fim de retirada dos psrites.
     * @param reverse case true, inverte a ordem dos sprites como retorno.
     * @return array de sprites especificos
     */
    public static Sprite[] getSpritesFromSprites(Sprite[] sprites, int indexBegin, int indexEnd, boolean reverse){
        Sprite cSprite[] = null;
        cSprite = new Sprite[(indexEnd+1)-indexBegin];
        int tCounter = 0;
        for (int i=indexBegin; i<=indexEnd; i++){

            if (!reverse){
                cSprite[tCounter] = sprites[i];
            }
            else {
                cSprite[cSprite.length-1 - tCounter] = sprites[i];
            }
            tCounter++;
        }
        return cSprite;
    }

}
