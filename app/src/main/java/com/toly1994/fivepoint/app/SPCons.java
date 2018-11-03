package com.toly1994.fivepoint.app;


import com.toly1994.fivepoint.R;

/**
 * Created by Linux on 2016/4/8.
 */
public class SPCons {
    //线的颜色
    public final static String SP_LINE_COLOR = "line_color";
    public final static int LINE_COLOR = 0xff000000;


    // 五子连珠
    public final static String SP_MAX_COUNT_IN_LINE = "max_count_in_line";
    public final static int MAX_COUNT_IN_LINE = 5;
    // 棋盘的行数
    public final static String SP_MAX_LINE = "max_line";
    public final static int MAX_LINE = 15;
    //白棋图片
    public final static String SP_WHITE_IMG = "white_img";
    public final static int WHITE_IMG = R.drawable.stone_w2;
    //黑棋图片
    public final static String SP_BLACK_IMG = "black_img";
    public final static int BLACK_IMG = R.drawable.stone_b1;

    //落子音效
    public final static String SP_FALL_SOUND = "fall_sound";
    public final static int FALL_SOUND = R.raw.fall;
    //落子音效
    public final static String SP_ALLOW_SOUND = "is_sound";
    public final static boolean ALLOW_SOUND = false;

    //背景音效
    public final static String SP_BGM_SOUND = "bgm_sound";
    public final static int BGM_SOUND = R.raw.bgm;

    //是否有背景音
    public final static String SP_IS_BGM_SOUND = "is_bgm_sound";
    public final static boolean IS_BGM_SOUND = false;
    //背景图片
    public final static String SP_BG_IMG = "bg_img";



    // 检查的方向
    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;
    public final static int LEFT_DIAGONAL = 2;
    public final static int RIGHT_DIAGONAL = 3;

}
