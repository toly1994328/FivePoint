package top.toly.zutils.core.domain;

import java.util.HashMap;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/10/27 0027:12:54<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：图片格式和图片资源的映射
 */
public class MimeMap {

    private String[] mimeType = new String[]{
            "image/jpeg", "image/png", "application/zip"
    };

    private HashMap<String, Integer> mHashMap;

    public MimeMap(int[] res) {
        mHashMap = new HashMap<>();
        for (int i = 0; i < mimeType.length; i++) {
            mHashMap.put(mimeType[i], res[i]);
        }
    }

    public int getIcon(String type, int def) {
        Integer res = mHashMap.get(type);
        if (res == null) {
            res = def;
        }
        return res;
    }
}
