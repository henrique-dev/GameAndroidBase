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
package com.br.phdev.gameandroidbase.cmp.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.br.phdev.gameandroidbase.cmp.Entity;

/**
 * Classe para abstração e controle dos textos desenhados no canvas.
 * @version 1.0
 */
@Deprecated
public class OldText extends Entity {

    /**
     * Constantes para alinhamento do texto.
     */
    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    /**
     * Area original do componente. Usado para calculos de alinhamento.
     */
    private RectF originalArea;

    /**
     * Alinhamento horizontal e vertical do texto.
     */
    private int horizontalAlignment = CENTER;
    private int verticalAlignment = CENTER;

    /**
     * {@link String} do texto.
     */
    private String text;

    /**
     * {@link String} usada caso o haja quebra de linhas no texto.
     */
    private String textToDraw[];

    /**
     * Tamanho da fonte do texto.
     */
    private float textSize;

    /**
     * Cor principal do texto.
     */
    private int colorText = Color.BLACK;

    /**
     * Usado para aplicar efeito de borda nas letras do texto.
     */
    private Paint strokePaint;

    /**
     * Estado ativo da borda.
     */
    private boolean strokeOn;

    /**
     * Espaçamento horizontal e vertical entre o texto e a area do texto.
     */
    private int spaceW = 10; // 10
    private int spaceH = 50; // 50

    /**
     * Estado de ajuste automatico do tamanho da fonte do texto.
     */
    private boolean textSizeAdjusted = true;

    /**
     * Entidade consumidora do texto.
     */
    private Entity entity;

    /**
     * Area ocupada pela fonte.
     */
    private Rect areaFont;

    /**
     * Cria um texto para ser exibido em uma entidade.
     *
     * @param entity entidade para consumir o texto.
     * @param text texto para ser exibido.
     */
    public OldText(@NonNull  Entity entity, @NonNull String text) {
        super(new RectF(entity.getArea()));
        this.entity = entity;
        this.originalArea = new RectF(super.area);
        super.defaultPaint.setColor(colorText);
        super.defaultPaint.setAntiAlias(true);
        this.text = text;
        checkAndFormatText(this);
        automaticTextSize(this);
        prepareTextToDraw(this);
    }

    /**
     * Redefine o tamanho da fonte do texto.
     *
     * @param textSize tamanho da fonte.
     */
    public void setTextSize(float textSize) {
        if (textSize <= 0)
            throw new Error("Tamanho da fonte inferior ou igual a 0.");
        this.textSize = textSize;
        prepareTextToDraw(this);
    }

    @Override
    public void setArea(RectF area) {
        super.setArea(area);
        this.originalArea = new RectF(super.area);
        checkAndFormatText(this);
        if (this.textSizeAdjusted)
            automaticTextSize(this);
        prepareTextToDraw(this);
    }

    /**
     * Retorna a {@link String} usada.
     *
     * @return @{@link String} usada.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Redefine a {@link String} para usar.
     *
     * @param text {@link String} para usar.
     */
    public void setText(String text) {
        this.text = text;
        checkAndFormatText(this);
        prepareTextToDraw(this);
    }

    /**
     * Redefine a borda do texto.
     *
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
     * Redefine o estado ativo da borda.
     *
     * @param strokeOn true para ativar o desenho da borda e false para desativar o desenho da borda.
     */
    public void setStrokeOn(boolean strokeOn) {
        this.strokeOn = strokeOn;
    }

    /**
     * Retorna o estado ativo da borda.
     *
     * @return estado ativo.
     */
    public boolean isStrokeOn() {
        return this.strokeOn;
    }

    /**
     * Retorna o estado de ajuste automatico do tamanho da fonte do texto.
     *
     * @return estado do ajuste.
     */
    public boolean isTextSizeAdjusted() {
        return textSizeAdjusted;
    }

    /**
     * Redefine o estado de ajuste automatico do tamanho da fonte do texto.
     *
     * @param textSizeAdjusted true para ativar o ajuste automatico e false para desativar o ajuste automatico.
     */
    public void setTextSizeAdjusted(boolean textSizeAdjusted) {
        this.textSizeAdjusted = textSizeAdjusted;
    }

    /**
     * Retorna a cor principal do texto.
     *
     * @return
     */
    public int getColor() {
        return this.colorText;
    }

    /**
     * Redefine a cor principal do texto.
     *
     * @param color
     */
    public void setColor(int color) {
        this.defaultPaint.setColor(color);
    }

    /**
     * Prepara o texto para desenhar.
     *
     * @param text texto a ser desenhado.
     */
    private static void prepareTextToDraw(OldText text) {
        text.defaultPaint.setTextSize(text.textSize);
        Rect rectTextBounds = new Rect();
        text.defaultPaint.getTextBounds(text.getText(), 0, text.getText().length(), rectTextBounds);
        text.areaFont = new Rect(rectTextBounds);
        align(text);
    }

    /**
     * Checa e formata o texto para caso haja quebra de linhas.
     *
     * @param text texto a ser checado.
     */
    private static void checkAndFormatText(OldText text) {
        if (checkEspecialChars(text.text) > 0) {
            text.textToDraw = new String[checkEspecialChars(text.text) + 1];
            int counter = 0;
            for (int i=0; counter<text.text.length(); counter++) {
                if (text.textToDraw[i] == null)
                    text.textToDraw[i] = "";
                if (text.text.charAt(counter) == '\n')
                    i++;
                else
                    text.textToDraw[i] = text.textToDraw[i] + text.text.charAt(counter);
            }
        } else
            text.textToDraw = new String[]{text.text};
    }

    /**
     * Checa por caracters especiais na {@link String} do texto. Atualmente somente quebra de linhas.
     *
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
     * Configura automaticamente o tamanho da fonte do texto.
     *
     * @param text texto para ser configurado.
     */
    private static void automaticTextSize(OldText text) {
        if (text.area.width() == 0 && text.area.height() == 0)
            return;

        Paint tmpPaint = new Paint(text.defaultPaint);
        float textSize = 1;
        tmpPaint.setTextSize(textSize);

        String biggerLine = text.text;

        Rect rectTextBounds;

        while (true) {
            rectTextBounds = new Rect();
            tmpPaint.getTextBounds(biggerLine, 0, biggerLine.length(), rectTextBounds);
            if (text.area.height() > rectTextBounds.height() * text.textToDraw.length + text.spaceH * 2)
                textSize += 1;
            else
                break;
            tmpPaint.setTextSize(textSize);
        }

        biggerLine = getBiggerLine(text.textToDraw);

        while (true) {
            rectTextBounds = new Rect();
            tmpPaint.getTextBounds(biggerLine, 0, biggerLine.length(), rectTextBounds);

            if (text.area.width() < rectTextBounds.width() + text.spaceW * 2)
                textSize -= 1;
            else
                break;
            tmpPaint.setTextSize(textSize);
        }

        text.areaFont = new Rect(rectTextBounds);
        text.setTextSize(textSize);
    }

    /**
     * Retorna a linha da {@link String} com maior largura.
     *
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
     *
     * @param text
     */
    private static void align(OldText text) {
        verticalAlign(text);
        horizontalAlign(text);
    }

    /**
     * Alinha verticalmente o texto.
     *
     * @param text texto a ser alinhado.
     */
    private static void verticalAlign(OldText text) {
        int alignment = text.verticalAlignment;
        Rect rectTextBounds = new Rect();
        text.defaultPaint.getTextBounds(text.text, 0, text.text.length(), rectTextBounds);
        switch (alignment) {
            case TOP:
                text.setY(rectTextBounds.height());
                break;
            case CENTER:
                text.setY((text.entity.getArea().height() - (rectTextBounds.height() * text.textToDraw.length))/2 + rectTextBounds.height());
                break;
            case BOTTOM:
                text.setY(text.entity.getArea().height() - (int)text.textSize * (text.textToDraw.length-1));
                break;
        }
    }

    /**
     * Alinha horizontalmente o texto.
     *
     * @param text texto a ser alinhado.
     */
    private static void horizontalAlign(OldText text) {
        int alignment = text.horizontalAlignment;
        switch (alignment) {
            case LEFT:
                text.defaultPaint.setTextAlign(Paint.Align.LEFT);
                text.setX(0);
                break;
            case CENTER:
                text.defaultPaint.setTextAlign(Paint.Align.CENTER);
                text.setX(text.entity.getArea().right - text.originalArea.centerX());
                break;
            case RIGHT:
                text.defaultPaint.setTextAlign(Paint.Align.RIGHT);
                text.setX(text.originalArea.right - text.entity.getArea().left);
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
                    this.entity.getArea().top + (super.area.top + (i * (areaFont.height()))),
                    super.defaultPaint);
            if (strokeOn) {
                canvas.drawText(this.textToDraw[i], super.area.left, super.area.top + (i * (super.defaultPaint.getTextSize())), this.strokePaint);
            }
        }
        canvas.restoreToCount(savedState);
    }

}
