package com.ziv.zvideo;

import android.support.v7.app.AppCompatActivity;
import java.io.IOException;

/**
 * Created by ziv on 2017/7/20.
 */

public abstract class AbstractPlayerActivity extends AppCompatActivity{
    // AVI文件名的extra
    public static final String EXTRA_FILE_NAME = "com.ziv.zvideo.EXTRA_FILE_NAME";
    // AVI文件描述符
    protected long avi = 0;

    @Override
    protected void onStart() {
        super.onStart();

        try {
            avi = open(getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (0 != avi){
            close(avi);
            avi = 0;
        }
    }

    public String getFileName() {
        return getIntent().getExtras().getString(EXTRA_FILE_NAME);
    }

    /**
     * 打开指定avi文件并返回文件描述符
     *
     * @param fileName file name
     * @return file descriptor
     * @throws IOException
     */
    protected native static long open(String fileName) throws IOException;

    /**
     * 获取视频宽度
     *
     * @param avi file descriptor
     * @return video width
     */
    protected native static int getWidth(long avi);

    /**
     * 获取视频高度
     *
     * @param avi file descriptor
     * @return video height
     */
    protected native static int getHeight(long avi);

    /**
     * 获取帧速
     *
     * @param avi file descriptor
     * @return frame rate
     */
    protected native static double getFrameRate(long avi);

    /**
     * 基于文件描述符，关闭指定avi文件
     *
     * @param avi file descriptor
     */
    protected native static void close(long avi);

    static {
        System.loadLibrary("AVIPlayer");
    }
}
