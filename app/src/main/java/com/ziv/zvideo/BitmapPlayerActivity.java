package com.ziv.zvideo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 使用Bitmap的AVI Player
 *
 * Created by ziv on 2017/7/23.
 */

public class BitmapPlayerActivity extends AbstractPlayerActivity {
    private final AtomicBoolean isPlaying = new AtomicBoolean();
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_player);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceHolderCallback);
    }

    /**
     * SurfaceHolder监听surface事件回调
     */
    private final SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            isPlaying.set(true);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            new Thread(renderer).start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (canvas != null){
                canvas = null;
            }
            isPlaying.set(false);
        }
    };

    /**
     * 渲染线程通过一个Bitmap将AVI文件中的视频帧渲染到Surface上
     */
    private final Runnable renderer = new Runnable() {
        @Override
        public void run() {
            // 创建一个新的Bitmap保存所有帧
            Bitmap bitmap = Bitmap.createBitmap(getWidth(avi), getHeight(avi), Bitmap.Config.RGB_565);
            // 使用帧速计算延迟
            long frameDelay = (long)(1000 / getFrameRate(avi));
            // 播放时开始渲染
            while(isPlaying.get()){
                // 讲视频帧渲染至bitmap
                render(avi, bitmap);

                // 锁定canvas画布
                canvas = surfaceHolder.lockCanvas();
                if (canvas != null && bitmap != null) {
                    // 将bitmap绘制在canvas上
                    canvas.drawBitmap(bitmap, 0, 0, null);
                    // canvas准备显示
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

                // 等待下一帧
                try {
                    Thread.sleep(frameDelay);
                }catch (InterruptedException e){
                    break;
                }
            }
        }
    };

    /**
     * 从AVI文件描述符输出到指定Bitmap来渲染帧
     *
     * @param avi file descriptor.
     * @param bitmap bitmap instance.
     * @return true is there are more frames, false otherwise
     */
    private native static boolean render(long avi, Bitmap bitmap);
}
