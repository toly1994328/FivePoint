package top.toly.zutils.core.domain.song;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Locale;

import top.toly.zutils._pinyi.PinyinUtil;

public class Song implements Comparable<Song>, Parcelable {
    /**
     * 歌曲id
     */
    private long songId;
    /**
     * 歌曲专栏
     */
    private Album album;
    /**
     * 歌曲名
     */
    private String title;
    /**
     * 歌曲时长
     */
    private long duration;
    /**
     * 歌曲地址
     */
    private String url;


    public Song(Album album, String title, long songId, String url, long duration) {
        super();
        this.album = album;
        this.title = title;
        this.duration = duration;
        this.songId = songId;
        this.url = url;
    }

    public Song() {
    }

    @Override
    public int compareTo(@NonNull Song another) {
        String aLetter = PinyinUtil.getPinyin(title).toUpperCase(
                Locale.ENGLISH);
        String bLetter = PinyinUtil.getPinyin(another.getTitle())
                .toUpperCase(Locale.ENGLISH);
        return aLetter.compareTo(bLetter);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song) {
            Song item = (Song) obj;
            return item.getTitle().equals(title)
                    && item.getSongId() == songId
                    && item.getDuration() == duration
                    && item.getUrl().equals(url);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Album getAlbum() {
        return album;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public long getDuration() {
        return duration;
    }

    public long getSongId() {
        return songId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 序列化实体类
     */
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        public Song createFromParcel(Parcel source) {
            Song song = new Song();
            song.title = source.readString();
            song.duration = source.readLong();
            song.songId = source.readLong();
            song.url = source.readString();
            return song;
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(duration);
        dest.writeLong(songId);
        dest.writeString(url);
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", album=" + album +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", url='" + url + '\'' +
                '}';
    }
}
