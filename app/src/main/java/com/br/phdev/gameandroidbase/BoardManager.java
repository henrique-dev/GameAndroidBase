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

import com.br.phdev.gameandroidbase.cmp.environment.Board;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.PathClassLoader;

public class BoardManager {

    BoardManager(DeviceManager deviceManager, SoundManager soundManager) {
        try {
            Class<?> clazz = Class.forName("com.br.phdev.gameandroidbase.test.GameBoard");

            ClassLoader classLoader = getClass().getClassLoader();

            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();
            Board board = (Board)instance;


        } catch (ClassNotFoundException e) {
            GameLog.error(this, e.getMessage());
        } catch (NoSuchMethodException e) {
            GameLog.error(this, e.getMessage());
        } catch (IllegalAccessException e) {
            GameLog.error(this, e.getMessage());
        } catch (InstantiationException e) {
            GameLog.error(this, e.getMessage());
        } catch (InvocationTargetException e) {
            GameLog.error(this, e.getMessage());
        }

    }
}
