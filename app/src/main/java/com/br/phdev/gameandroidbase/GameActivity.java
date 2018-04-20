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
package com.br.phdev.gameandroidbase;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.br.phdev.gameandroidbase.cmp.listeners.ActivityStateListener;

/**
 * Activity unica e principal d aaplicação.
 */
public class GameActivity extends Activity {

    private GameEngine gameEngine;
    private ActivityStateListener activityStateListener;

    @Override
    protected void onCreate(Bundle savedInstance) {
        GameLog.debug(this, "Activity criada.");
        super.onCreate(savedInstance);
        setupParameters();
        this.gameEngine = new GameEngine(this);
        this.activityStateListener = this.gameEngine.getSoundManager();
        super.setContentView(this.gameEngine);
    }

    @Override
    protected void onDestroy() {
        GameLog.debug(this, "Activity destruida.");
        super.onDestroy();
        this.gameEngine.finalizeApplication();
    }

    @Override
    public void onResume() {
        GameLog.debug(this, "Activity resumida.");
        super.onResume();
        this.activityStateListener.onResume();
    }

    @Override
    public void onPause() {
        GameLog.debug(this, "Activity pausada.");
        super.onPause();
        this.activityStateListener.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (this.gameEngine.keyBackPressed())
                onBackPressed();
        }
        return false;
    }

    /**
     * Define os parametros necessarios para o funcionamento da aplicação.
     */
    private void setupParameters() {

        GameParameters.getInstance().assetManager = getAssets();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        GameParameters.getInstance().screenSize = new Rect(0, 0, size.x, size.y);
    }

}
