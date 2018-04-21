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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;

import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.utils.Text;

public class TableVisual extends Entity {

    private Text text;

    private Label head;

    private RectF leftBodyArea;
    private RectF rightBodyArea;
    private RectF topBodyArea;
    private RectF bottomBodyArea;
    private RectF centerBodyArea;

    private final float divideV = 16;
    private final float divideH = 16;

    private float rateHeadV = 2;
    private float rateLeftH = 2;
    private float rateRightH = 2;
    private float rateTopV = 1;
    private float rateBottomV = 1;

    public TableVisual() {
        super();
        super.defaultPaint.setColor(Color.GRAY);
    }

    public TableVisual(String text) {
        super();
        super.defaultPaint.setColor(Color.GRAY);
        this.text = new Text(text);
    }

    void set(RectF tableArea) {
        super.setArea(tableArea);
        float rateV = super.getHeight() / 16;
        float rateH = super.getWidth() / 16;
        this.head = new Label(new RectF(
                super.getX(),
                super.getY(),
                super.getX() + super.getWidth(),
                super.getY() + rateV * this.rateHeadV));
        this.head.getDefaultPaint().setColor(Color.GRAY);
        this.head.setEdgeVisible(false);
        this.topBodyArea = new RectF(
                super.getX(),
                this.head.getArea().bottom,
                super.getX() + super.getWidth(),
                this.head.getArea().bottom + rateV * rateTopV
        );
        this.bottomBodyArea = new RectF(
                super.getX(),
                super.getY() + super.getHeight() - (rateV * this.rateBottomV),
                super.getX() + super.getWidth(),
                super.getY() + super.getHeight()
        );
        this.leftBodyArea = new RectF(
                super.getX(),
                this.topBodyArea.bottom,
                this.getX() + rateH * this.rateLeftH,
                this.bottomBodyArea.top
        );
        this.rightBodyArea = new RectF(
                super.getX() + super.getWidth() - (rateH * this.rateRightH),
                this.topBodyArea.bottom,
                this.getX() + super.getWidth(),
                this.bottomBodyArea.top
        );
        this.centerBodyArea = new RectF(
                this.leftBodyArea.right,
                this.topBodyArea.bottom,
                this.rightBodyArea.left,
                this.bottomBodyArea.top
        );
        if (this.text != null) {
            this.text.setEntity(this.head);
        }
    }

    RectF getElementsArea() {
        return this.centerBodyArea;
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        this.head.draw(canvas);
        if (this.text != null)
            this.text.draw(canvas);
        canvas.drawRect(this.leftBodyArea, super.defaultPaint);
        canvas.drawRect(this.rightBodyArea, super.defaultPaint);
        canvas.drawRect(this.topBodyArea, super.defaultPaint);
        canvas.drawRect(this.bottomBodyArea, super.defaultPaint);
        //canvas.clipRect(this.centerBodyArea);
        canvas.restoreToCount(savedState);
    }

    @Override
    public void update() {

    }
}
