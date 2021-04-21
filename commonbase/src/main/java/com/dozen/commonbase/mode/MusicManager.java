package com.dozen.commonbase.mode;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/4/20
 */
public class MusicManager {
    public static final int MUSIC_END_GAME = 2;
    public static final int MUSIC_GAME = 1;
    public static final int MUSIC_MENU = 0;
    private static final String TAG = "MusicManager";
    public static boolean isActivityChanging = false;
    static MediaPlayer mp;
    private static Map<Integer, MediaPlayer> musicPlayers = new HashMap();
    private static Map<Integer, MediaPlayer> soundPlayers = new HashMap();

    public MusicManager() {
    }

    private static boolean isMusicOn(Context var0) {
        return PreferenceManager.getDefaultSharedPreferences(var0).getBoolean("MUSIC", true);
    }

    public static void pauseMusic() {
        if (!isActivityChanging) {
            Iterator var0 = musicPlayers.values().iterator();

            while(var0.hasNext()) {
                MediaPlayer var1 = (MediaPlayer)var0.next();
                if (var1.isPlaying()) {
                    var1.pause();
                }
            }
        }

    }

    public static void playSound(Context param0, int param1) {
        startMusic(param0,param1);
    }

    public static void putPrefBoolean(String var0, boolean var1, Context var2) {
        SharedPreferences.Editor var3 = PreferenceManager.getDefaultSharedPreferences(var2).edit();
        var3.putBoolean(var0, var1);
        var3.commit();
    }

    public static void setMusicOff(Context var0) {
        putPrefBoolean("MUSIC", false, var0);
    }

    public static void setMusicOn(Context var0) {
        putPrefBoolean("MUSIC", true, var0);
    }

    public static void setSoundOff(Context var0) {
        putPrefBoolean("SOUND", false, var0);
    }

    public static void setSoundOn(Context var0) {
        putPrefBoolean("SOUND", true, var0);
    }

    public static void startMusic(Context var0, int var1) {
        if (isMusicOn(var0)) {
            stopMusic();
            MediaPlayer var2 = (MediaPlayer)musicPlayers.get(var1);
            if (var2 != null) {
                if (!var2.isPlaying()) {
                    var2.start();
                }
            } else {
                MediaPlayer var4 = MediaPlayer.create(var0, var1);
                musicPlayers.put(var1, var4);
                if (var4 != null) {
                    try {
                        var4.start();
                        return;
                    } catch (Exception var3) {
                        Log.e("MusicManager", var3.getMessage(), var3);
                        return;
                    }
                }
            }
        }

    }

    public static void stopMusic() {
        MediaPlayer var1;
        for(Iterator var0 = musicPlayers.values().iterator(); var0.hasNext(); var1.seekTo(0)) {
            var1 = (MediaPlayer)var0.next();
            if (var1.isPlaying()) {
                var1.pause();
            }
        }

    }
}

