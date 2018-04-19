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
package com.br.phdev.gameandroidbase;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.br.phdev.gameandroidbase.cmp.sound.Music;
import com.br.phdev.gameandroidbase.cmp.sound.ShortSound;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de audio para o jogo.
 */
public final class SoundManager {

    /**
     * Lista de musicas disponiveis no contexto atual do gerenciador.
     */
    private List<Music> musicList;

    /**
     * Lista de efeitos sonoros disponiveis no contexto atual do gerenciador.
     */
    private List<ShortSound> shortSoundList;

    /**
     * Contexto da activity
     */
    private Context context;

    /**
     * Players utilizados para executar as musicas e efeitos sonoros.
     */
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;

    /**
     * Cria o gerenciador.
     * @param context contexto da activity.
     */
    SoundManager(Context context) {
        this.context = context;
        this.musicList = new ArrayList<>();
        this.shortSoundList = new ArrayList<>();
        if (this.soundPool == null)
            this.soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * Adiciona uma musica na lista de musicas do gerenciador.
     * @param music musica a ser adicionada.
     * @return id correspondente ao seu index na lista.
     */
    public int addMusicToList(Music music) {
        this.musicList.add(music);
        return this.musicList.size()-1;
    }

    /**
     * Limpa a lista de musicas do gerenciador.
     */
    public void clearMusicList() {
        this.musicList.clear();
    }

    /**
     * Adiciona um efeito sonoro na lista de efeitos sonoros do gerenciador.
     * @param shortSound efeito sonoro a ser adicionado.
     * @return id correspondente ao seu index na lista.
     */
    public int addShortSoundToList(ShortSound shortSound) {
        shortSound.setPoolId(this.soundPool.load(this.context, shortSound.getResourceId(), 1));
        this.shortSoundList.add(shortSound);
        return this.shortSoundList.size()-1;
    }

    /**
     * Limpa a lista de efeitos sonoros do gerenciador.
     */
    public void clearSoundList() {
        for (ShortSound shortSound : this.shortSoundList) {
            this.soundPool.unload(shortSound.getPoolId());
        }
        this.shortSoundList.clear();
    }

    /**
     * Reproduz uma musica.
     * @param index index da musica na lista.
     */
    public void playMusic(int index) {
        loadAndPlayMusic(musicList.get(index));
    }

    /**
     * Carrega e executa a musica repassada.
     * @param music musica a ser reproduzida.
     */
    private void loadAndPlayMusic(final Music music) {
        new Thread(){
            @Override
            public void run() {
                if (SoundManager.this.mediaPlayer != null) {
                    try {
                        if (SoundManager.this.mediaPlayer.isPlaying())
                            SoundManager.this.mediaPlayer.stop();
                        SoundManager.this.mediaPlayer.release();
                    } catch (Exception e) {
                        GameLog.error(this, e);
                    }
                }
                try {
                    SoundManager.this.mediaPlayer = MediaPlayer.create(context, music.getResourceId());
                    SoundManager.this.mediaPlayer.setOnCompletionListener(music.getOnCompletionListener());
                    SoundManager.this.mediaPlayer.start();
                } catch (Exception e) {
                    GameLog.error(this, e);
                }
            }
        }.start();
    }

    /**
     * Reproduz um efeito sonoro.
     * @param index index do efeito sonoro na lista.
     */
    public void playSound(int index) {
        this.soundPool.play(shortSoundList.get(index).getPoolId(), shortSoundList.get(index).getLeftVolume(),
                shortSoundList.get(index).getRightVolume(), 1, shortSoundList.get(index).getLoop(), shortSoundList.get(index).getRate());
    }

    /**
     * Libera o {@link MediaPlayer} e o {@link SoundPool} para encerramento da aplicação.
     */
    void release() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        if (this.soundPool != null)
            this.soundPool.release();
    }

}
