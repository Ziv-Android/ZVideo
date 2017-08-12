package com.ziv.zvideo;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 原生window渲染器更新AVI Player
 * <p>
 * Created by ziv on 2017/8/12.
 */

public class NativeWindowPlayerActivity extends AbstractPlayerActivity {
    private final AtomicBoolean isPlaying = new AtomicBoolean();

    // Surface存储器
    private SurfaceHolder surfaceHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_window_player);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceViewCallback);
    }

    private final SurfaceHolder.Callback surfaceViewCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // surface准备好后开始播放
            isPlaying.set(true);

            // 独立线程中启动渲染器
            new Thread(renderer).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // surface销毁时停止播放
            isPlaying.set(false);
        }
    };

    private final Runnable renderer = new Runnable() {
        @Override
        public void run() {
            // 获取surface实例
            Surface surface = surfaceHolder.getSurface();

            // 初始化原生window
            init(avi, surface);

            // 使用帧减速计算延时
            long frameDelay = (long) (1000 / getFrameRate(avi));

            // 播放时开始渲染
            while (isPlaying.get()){
                // 将帧渲染至surface
                render(avi, surface);
                // 等待下一帧
                try {
                    Thread.sleep(frameDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    };

    /**
     * 初始化原生Window
     *
     * @param avi file descriptor
     * @param surface surface instance
     */
    private native static void init(long avi, Surface surface);

    /**
     * 将给定AVI文件的帧渲染到给定的surface上
     *
     * @param avi file descriptor
     * @param surface surface instance
     * @return true if there are more frames, false otherwise.
     */
    private native static boolean render(long avi, Surface surface);
}
