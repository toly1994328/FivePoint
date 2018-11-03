package top.toly.zutils.phone.media;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import top.toly.zutils.core.domain.song.Album;
import top.toly.zutils.core.domain.song.Artist;
import top.toly.zutils.core.domain.song.ArtistMusicInfo;
import top.toly.zutils.core.domain.song.Song;


/**
 * 音乐数据库相关操作
 */
public class MusicScanner {

    /**
     * 歌曲集合
     */
    private static List<Song> songs = new ArrayList<>();

    public static final int SONG_LOADER_ID = 1;

    /**
     * 音频的uri
     */
    private static final Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private LoaderManager manager;

    /**
     * 读取音频
     */
    public void loadMusic(final Activity context) {
        manager = context.getLoaderManager();

        // 初始化loader：(任意唯一ID,选项,加载的回调监听)
        manager.initLoader(SONG_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                String[] projection = new String[]{
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ARTIST_ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.IS_MUSIC};
                return new CursorLoader(context, contentUri, projection,
                        null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                songs.clear();
            }

            // cursor会自动关闭
            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null) {
                    // 清除旧数据
                    songs.clear();
                    // 获取所需列的索引
                    int albumIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                    int artistIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                    int titleIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    int durationIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                    int songIdIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    int albumIdIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
                    int artistIdIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID);
                    int dataUrlIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    int isMusicIdx = data.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC);

                    while (data.moveToNext()) {
                        int isMusic = data.getInt(isMusicIdx);
                        if (isMusic != 0) {
                            String album = data.getString(albumIdx);
                            String artist = data.getString(artistIdx);
                            String title = data.getString(titleIdx);
                            String dataUrl = data.getString(dataUrlIdx);
                            long duration = data.getLong(durationIdx);
                            long songId = data.getLong(songIdIdx);
                            long albumId = data.getLong(albumIdIdx);
                            long artistId = data.getLong(artistIdIdx);
                            Song item = new Song(new Album(albumId, album,
                                    new Artist(artistId, artist,
                                            new ArtistMusicInfo()), null),
                                    title, songId, dataUrl, duration);
                            songs.add(item);
                        }
                    }
                    // 歌曲检索完毕
                    manager.destroyLoader(SONG_LOADER_ID); // 销毁loader
                    if (mOnFinish != null) {
                        mOnFinish.scanOver(songs);
                    }
                }
            }
        });
    }

    //////////////////////////////////扫描歌曲结束监听------------------
    public interface OnFinish {
        void scanOver(List<Song> songs);
    }

    private OnFinish mOnFinish;

    public MusicScanner setOnFinish(OnFinish onFinish) {
        mOnFinish = onFinish;
        return this;
    }
}
