package com.cornerstone.ioKit;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/4/1.
 */

public class FileKit {
    /**
     * 将assets目录下的资源文件拷贝到SD卡中
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source 比如assets目录下有目录 sync,在该目录下有test.txt,
     *               传入的 source 就应该是 "sync/test.txt"
     * @param dest /sd卡上的路径
     */
    public static void copyFromAssetsToSdcard(Context context, boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
//                is = context.getResources().getAssets().open("bdtts/"+source);
                is = context.getResources().getAssets().open(source);

                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
