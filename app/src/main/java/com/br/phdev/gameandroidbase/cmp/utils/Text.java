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

package com.br.phdev.gameandroidbase.cmp.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.br.phdev.gameandroidbase.cmp.Entity;

/**
 * Classe responsavel pela abstração e controle dos textos desenhados no canvas.
 * @version 1.0
 */
public class Text extends Entity {

    public static final int AUTOMATIC_TEXT_SIZE = -1;

    /**
     * Constantes para alinhamento do texto.
     */
    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    /**
     * Alinhamento horizontal e vertical do texto.
     */
    private int horizontalAlignment = CENTER;
    private int verticalAlignment = CENTER;

    /**
     * {@link String} contendo os caracters do texto.
     */
    private String textToDraw[];

    /**
     * Usado para aplicar efeito de borda nas letras do texto.
     */
    private Paint strokePaint;

    /**
     * Estado ativo da borda.
     */
    private boolean strokeOn;

    /**
     * Tamanho da fonte do texto.
     */
    private float textSize = AUTOMATIC_TEXT_SIZE;

    /**
     * Espaçamento horizontal e vertical entre o texto e a area do texto.
     */
    private int spaceW = 10; // 10
    private int spaceH = 50; // 50

    /**
     * Entidade consumidora do texto.
     */
    private Entity entity;

    /**
     * Hash da {@link String} utilizada.
     * Usado para comparações.
     */
    private int originalHashString;

    /**
     * Cria um texto para ser exibido em uma entidade.
     * @param entity entidade para consumir o texto.
     * @param text texto para ser exibido.
     */
    public Text(@NonNull Entity entity,@NonNull String text) {
        super();
        this.entity = entity;
        super.defaultPaint.setColor(Color.BLACK);
        super.defaultPaint.setAntiAlias(true);
        this.textToDraw = checkAndFormatText(text);
        this.spaceH = entity.getArea().height() / 20;
        this.spaceW = entity.getArea().width() / 10;
        this.originalHashString = text.hashCode();
        defineTextSize(this, this.textSize);
        align(this);
    }

    /**
     * Cria um texto para ser exeibido em uma entidade. (É preciso definir apos a entidade com o setEntity().
     * @param text textopara ser exibido.
     */
    public Text(String text) {
        super();
        super.defaultPaint.setColor(Color.BLACK);
        super.defaultPaint.setAntiAlias(true);
        this.textToDraw = checkAndFormatText(text);
        this.originalHashString = text.hashCode();
    }

    /**
     * Retorna a entidade que consome o texto.
     * @return entidade consumidora do texto.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Define a entidade consumidora do texto.
     * @param entity entidade para consumir o texto.
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
        this.spaceH = entity.getArea().height() / 20;
        this.spaceW = entity.getArea().width() / 10;
        defineTextSize(this, this.textSize);
        align(this);
    }

    /**
     * Redefine a {@link String} do texto.
     * @param text texto.
     */
    public void setText(String text) {

        int tempHash = text.hashCode();
        if (tempHash == this.originalHashString) {
            return;
        }
        this.originalHashString = tempHash;

        this.textToDraw = checkAndFormatText(text);
        defineTextSize(this, this.textSize);
        align(this);
    }

    /**
     * Redefine o tamanho da fonte do texto.
     * @param size tamanho da fonte.
     */
    public void setTextSize(float size) {
        this.textSize = size;
        this.spaceH = entity.getArea().height() / 20;
        this.spaceW = entity.getArea().width() / 10;
        defineTextSize(this, size);
        align(this);
    }

    @Override
    public void setArea(@NonNull Rect area) {
        this.spaceH = entity.getArea().height() / 20;
        this.spaceW = entity.getArea().width() / 10;
        defineTextSize(this, this.textSize);
        align(this);
    }

    /**
     * Redefine a borda do texto.
     * @param color cor da borda.
     * @param strokeWidth largura da borda.
     */
    public void setStroke(int color, float strokeWidth) {
        this.strokePaint = new Paint(super.defaultPaint);

        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setColor(color);
        this.strokePaint.setStrokeWidth(strokeWidth);
        this.strokePaint.setAntiAlias(true);

        this.strokeOn = true;
    }

    /**
     * Checa e formata o texto para caso haja quebra de linhas. Separa as quebras em um array.
     * @param text texto a ser checado
     * @return array contendo as quebras de linhas encotradas.
     */
    private static String[] checkAndFormatText(String text) {
        String textToDraw[];
        if (checkEspecialChars(text) > 0) {
            textToDraw = new String[checkEspecialChars(text) + 1];
            int counter = 0;
            for (int i=0; counter<text.length(); counter++) {
                if (textToDraw[i] == null)
                    textToDraw[i] = "";
                if (text.charAt(counter) == '\n')
                    i++;
                else
                    textToDraw[i] = textToDraw[i] + text.charAt(counter);
            }
        } else
            return new String[]{text};

        return textToDraw;
    }

    /**
     * Checa por caracters especiais na {@link String} do texto. Atualmente somente quebra de linhas.
     * @param text texto a ser checado.
     * @return quantidade de quebras de linhas na {@link String}.
     */
    private static int checkEspecialChars(String text) {
        int counter = 0;
        for (int i=0; i<text.length(); i++) {
            if (text.charAt(i) == '\n')
                counter++;
        }
        return counter;
    }

    /**
     * Configura o tamanho da fonte do texto.
     * @param text texto a ser configurado.
     * @param textSize tamanho da fonte para o texto.
     */
    private static void defineTextSize(Text text, float textSize) {
        if (text.entity.getArea().width() == 0 && text.entity.getArea().height() == 0)
            return;
        if (text.toString().trim().length() == 0) {
            return;
        }

        if (textSize < 0) {
            Paint tmpPaint = new Paint(text.defaultPaint);
            float tempTextSize = 1;
            tmpPaint.setTextSize(tempTextSize);

            String biggerLine = getBiggerLine(text.textToDraw);
            String stringWithSpacesChanged = biggerLine.replace(' ', '-');

            Rect rectTextBounds;
            while (true) {
                rectTextBounds = new Rect();
                tmpPaint.getTextBounds(stringWithSpacesChanged, 0, stringWithSpacesChanged.length(), rectTextBounds);
                if (text.entity.getArea().height() > rectTextBounds.height() * text.textToDraw.length + text.spaceH * 2)
                    tempTextSize += 5;
                else
                    break;
                tmpPaint.setTextSize(tempTextSize);
            }

            //biggerLine = getBiggerLine(text.textToDraw);
            while (true) {
                rectTextBounds = new Rect();
                tmpPaint.getTextBounds(stringWithSpacesChanged, 0, stringWithSpacesChanged.length(), rectTextBounds);

                if (text.entity.getArea().width() < rectTextBounds.width() + text.spaceW * 2)
                    tempTextSize -= 1;
                else
                    break;
                tmpPaint.setTextSize(tempTextSize);
            }
            text.area = new Rect(rectTextBounds);
            text.defaultPaint.setTextSize(tempTextSize);

        } else {
            Rect rectTextBounds = new Rect();
            text.defaultPaint.setTextSize(textSize);
            String biggerLine = getBiggerLine(text.textToDraw);
            text.defaultPaint.getTextBounds(biggerLine, 0, biggerLine.length(), rectTextBounds);
            text.area = new Rect(rectTextBounds);
        }
    }

    /**
     * Retorna a linha da {@link String} com maior largura.
     * @param text array de {@link String} contendo as linhas a serem examinadas.
     * @return {@link String} com maior largura.
     */
    private static String getBiggerLine(String text[]) {
        String biggerLine = "";
        int biggerWidth = 0;
        Paint tempPaint = new Paint();
        tempPaint.setTextSize(20);
        for (String aText : text) {
            Rect rectTextBounds = new Rect();
            tempPaint.getTextBounds(aText, 0, aText.length(), rectTextBounds);
            if (rectTextBounds.width() > biggerWidth) {
                biggerLine = aText;
                biggerWidth = rectTextBounds.width();
            }
        }
        return biggerLine;
    }

    /**
     * Alinha horizontalmente e verticalmente o texto.
     * @param text texto a ser alinhado.
     */
    private static void align(Text text) {
        verticalAlign(text);
        horizontalAlign(text);
    }

    /**
     * Alinha verticalmente o texto.
     * @param text texto a ser alinhado.
     */
    private static void verticalAlign(Text text) {
        int alignment = text.verticalAlignment;
        Rect rectTextBounds = new Rect();
        String biggerLine = getBiggerLine(text.textToDraw);
        text.defaultPaint.getTextBounds(biggerLine, 0, biggerLine.length(), rectTextBounds);
        switch (alignment) {
            case TOP:
                text.setY(rectTextBounds.height());
                break;
            case CENTER:
                //text.setY((text.entity.getArea().height() - text.area.height())/2);
                text.setY((text.entity.getArea().height() - (rectTextBounds.height() * text.textToDraw.length))/2 + rectTextBounds.height());
                break;
            case BOTTOM:
                text.setY(text.entity.getArea().height() - rectTextBounds.height() * (text.textToDraw.length-1));
                break;
        }
    }

    /**
     * Alinha horizontalmente o texto.
     * @param text texto a ser alinhado.
     */
    private static void horizontalAlign(Text text) {
        int alignment = text.horizontalAlignment;
        switch (alignment) {
            case LEFT:
                text.defaultPaint.setTextAlign(Paint.Align.LEFT);
                text.setX(0);
                break;
            case CENTER:
                text.defaultPaint.setTextAlign(Paint.Align.CENTER);
                text.setX(text.entity.getArea().width()/2);
                break;
            case RIGHT:
                text.defaultPaint.setTextAlign(Paint.Align.RIGHT);
                text.setX(text.entity.getArea().width());
                break;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        for (int i=0; i<textToDraw.length; i++) {
            canvas.drawText(this.textToDraw[i],
                    this.entity.getArea().left + (super.area.left),
                    //this.entity.getArea().top + (super.area.top + (i * (super.defaultPaint.getTextSize()))),
                    this.entity.getArea().top + (super.area.top + (i * (area.height()))),
                    super.defaultPaint);
            if (strokeOn) {
                canvas.drawText(this.textToDraw[i], super.area.left, super.area.top + (i * (super.defaultPaint.getTextSize())), this.strokePaint);
            }
        }
        canvas.restoreToCount(savedState);
    }

    @Override
    public String toString() {
        StringBuilder tmpString = new StringBuilder();
        if (textToDraw != null) {
            for (int i = 0; i < textToDraw.length; i++) {
                tmpString.append(textToDraw[i]);
                if (!(textToDraw.length-1 == i))
                    tmpString.append('\n' + "");
            }
            return tmpString.toString();
        } else
            return "";
    }
}
