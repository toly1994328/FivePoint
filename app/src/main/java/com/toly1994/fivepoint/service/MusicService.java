package com.toly1994.fivepoint.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.toly1994.fivepoint.app.SPCons;


/**
 * 作者：张风捷特烈
 * 时间：2018/4/22:16:48
 * 邮箱：1981462002@qq.com
 * 说明：
 */
public class MusicService extends Service {
    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mMediaPlayer = MediaPlayer.create(this, SPCons.BGM_SOUND);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        super.onDestroy();
    }
}
