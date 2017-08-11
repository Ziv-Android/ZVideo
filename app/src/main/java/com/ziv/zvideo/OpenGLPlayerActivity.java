package com.ziv.zvideo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 使用OpenGL的AVI Player
 * (黑屏未解决)
 * <p>
 * Created by ziv on 2017/7/25.
 */

public class OpenGLPlayerActivity extends AbstractPlayerActivity {
    // 正在播放
    private final AtomicBoolean isPlaying = new AtomicBoolean();

    // 原生渲染器
    private long instance;

    // GLSurfaceView实例
    private GLSurfaceView glSurfaceView;
    /**
     * 根据帧速请求渲染
     */
    private final Runnable player = new Runnable() {
        @Override
        public void run() {
            // 帧速计算延迟
            long frameDelay = (long) (1000 / getFrameRate(avi));
            // 开始播放
            while (isPlaying.get()) {
                // 请求渲染
                glSurfaceView.requestRender();

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
     * OpenGL renderer
     */
    private final GLSurfaceView.Renderer renderer = new GLSurfaceView.Renderer() {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // 初始化OpenGl Surface
            initSurface(instance, avi);
            // Surface准备好后开始播放
            isPlaying.set(true);
            // 启动播放器
            new Thread(player).start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 渲染下一帧
            if (!render(instance, avi)) {
                isPlaying.set(false);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl_player);

        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
        // 设置渲染器
        glSurfaceView.setRenderer(renderer);
        // 请求时渲染
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 初始化原生渲染器
        instance = init(avi);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 通知GLSurfaceView执行onResume方法
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 释放原生渲染器
        free(avi);
        instance = 0;
    }

    /**
     * 初始化原生渲染器
     *
     * @param avi file descriptor
     * @return native instance
     */
    private native static long init(long avi);

    /**
     * 初始化OpenGL Surface
     *
     * @param instance native instance
     * @param avi      file descriptor
     */
    private native static void initSurface(long instance, long avi);

    /**
     * 用给定的文件进行渲染
     *
     * @param instance native instance
     * @param avi      file descriptor
     * @return true if there are more frames, false otherwise.
     */
    private native static boolean render(long instance, long avi);

    /**
     * 释放原生渲染器
     *
     * @param instance native instance
     */
    private native static void free(long instance);
}
