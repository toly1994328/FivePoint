package com.toly1994.fivepoint.app;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/29 0029:20:45<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：
 */
public class Point implements Parcelable {
    public int x;
    public int y;
    public boolean isWhite;

    public Point() {}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected Point(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        isWhite = in.readByte() != 0;
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeByte((byte) (isWhite ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", isWhite=" + isWhite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }
}
