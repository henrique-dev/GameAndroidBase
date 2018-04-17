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
import android.view.MotionEvent;

import com.br.phdev.gameandroidbase.GameLog;
import com.br.phdev.gameandroidbase.cmp.Entity;
import com.br.phdev.gameandroidbase.cmp.effect.FlashEffect;
import com.br.phdev.gameandroidbase.cmp.listeners.ActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.ClickListener;
import com.br.phdev.gameandroidbase.cmp.listeners.Listener;
import com.br.phdev.gameandroidbase.cmp.listeners.TableActionListener;
import com.br.phdev.gameandroidbase.cmp.listeners.events.Event;
import com.br.phdev.gameandroidbase.cmp.listeners.events.TableEvent;
import com.br.phdev.gameandroidbase.cmp.utils.Text;

import java.util.ArrayList;
import java.util.List;

public class Table extends WindowEntity implements ActionListener {

    private List<Row> rows;

    private boolean canPerformAction;

    private int rowsPerView = 6;
    private int colunms;

    public Table() {
        super();
        super.defaultPaint.setColor(Color.GREEN);
        this.rows = new ArrayList<>();
    }

    public void addRow(String text) {
        Row row = new Row(text);

        FlashEffect flashEffect = new FlashEffect();
        flashEffect.setSpeed(1);
        row.setClickEffect(flashEffect);

        int rowHeight = super.getHeight() / rowsPerView;
        int x = super.getX();
        int y = super.getY() + this.rows.size() * rowHeight;
        row.setArea(new Rect(x, y, x + super.getWidth(), y + rowHeight));
        row.addListener(this);
        this.rows.add(row);
    }

    public void addRow(TableObject tableObject) {
        Row row = new Row(tableObject);

        FlashEffect flashEffect = new FlashEffect();
        flashEffect.setSpeed(1);
        row.setClickEffect(flashEffect);

        int rowHeight = super.getHeight() / rowsPerView;
        int x = super.getX();
        int y = super.getY() + this.rows.size() * rowHeight;
        row.setArea(new Rect(x, y, x + super.getWidth(), y + rowHeight));
        this.rows.add(row);
    }

    private void move(float y) {
        for (Entity ent : this.rows) {
            move(ent, 0, y);
        }
    }

    public void addTableActionListener(TableActionListener tableActionListener) {
        super.addListener(0, tableActionListener);
    }

    @Override
    public void actionPerformed(Event evt) {
        if (listeners.size() > 0)
            ((TableActionListener)super.listeners.get(0)).tableActionPerformed((TableEvent)evt);
    }


    @Override
    public void update() {
        super.update();
        for (Entity ent : this.rows)
            ent.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int savedState = canvas.save();
        canvas.clipRect(super.area);
        super.draw(canvas);
        for (Entity ent : this.rows)
            ent.draw(canvas);
        canvas.restoreToCount(savedState);
    }

    private float px = 0, py = 0;

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.clicked)
            return false;

        int action = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        if (haveCollision(x, y, this)) {

            Event event = new Event((int)x, (int)y);

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    this.fireListeners(event, ClickListener.PRESSED_PERFORMED);
                    this.onArea = true;
                    this.clickedOnArea = true;
                    this.canPerformAction = true;
                    break;
                case MotionEvent.ACTION_UP:
                    if (this.onArea && this.clickedOnArea) {
                        if (this.canPerformAction)
                            for (Row row : this.rows)
                                if (haveCollision(x, y, row))
                                    row.clickEffect.start(new TableEvent(row.tableObject));
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (this.clickedOnArea) {
                        this.onArea = true;
                        if (this.rows.size() > this.rowsPerView)
                            if (this.rows.get(0).getY() <= super.getY() &&
                                    this.rows.get(this.rows.size()-1).getY() >= super.getArea().bottom)
                                move(y - py);
                            else {
                                if (this.rows.get(0).getY() > super.getY()) {
                                    int exceeded = super.getY() - this.rows.get(0).getY();
                                    move(exceeded);
                                } else if (this.rows.get(this.rows.size()-1).getY() < super.getArea().bottom) {
                                    int exceeded = super.getArea().bottom - this.rows.get(this.rows.size()-1).getY();
                                    move(exceeded);
                                }
                            }
                            this.canPerformAction = false;
                    }
                    break;
            }
            px = x;
            py = y;
        } else {
            this.onArea = false;
            if (this.clickedOnArea)
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        this.clickedOnArea = false;
                        if (this.rows.get(0).getY() > super.getY()) {
                            int exceeded = super.getY() - this.rows.get(0).getY();
                            move(exceeded);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        this.clickedOnArea = false;
                        break;
                }
        }
        return true;
    }

    private class Row extends WindowEntity{

        private TableObject tableObject;

        Row(TableObject tableObject) {
            super(new Rect(), new Text(tableObject.getName()));
            super.defaultPaint.setColor(Color.YELLOW);
            this.tableObject = tableObject;
        }

        Row(String text) {
            super(new Rect(), new Text(text));
            this.tableObject = new TableObject(text);
            super.defaultPaint.setColor(Color.YELLOW);
        }

        public TableObject getTableObject() {
            return tableObject;
        }

        public void setTableObject(TableObject tableObject) {
            this.tableObject = tableObject;
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return true;
        }
    }



}
