package com.xfeng.glactionimage;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by xfengimacgomo
 * data 2019/4/1 14:39
 * email xfengv@yeah.net
 */
public class glActionRender implements GLSurfaceView.Renderer{

    private final ActionImageFilter mActionImageFilter;
    private Bitmap bitmap;
    private EGLConfig config;
    private int width,height;
    private boolean refreshFlag=false;


    public glActionRender(View mView) {
        mActionImageFilter = new ActionImageColorFiter(mView.getContext(), ActionImageColorFiter.Filter.NONE);
    }

    public void setImage(Bitmap bitmap){
        this.bitmap=bitmap;
        mActionImageFilter.setBitmap(bitmap);
    }

    public void setScaleType(int scaleType) {
        mActionImageFilter.setScaleType(scaleType);
    }


    /**
     * 当GLSurfaceView中的Surface被创建的时候(界面显示)回调此方法，一般在这里做一些初始化
     * @param gl 1.0版本的OpenGL对象，这里用于兼容老版本，用处不大
     * @param config egl的配置信息(GLSurfaceView会自动创建egl，这里可以先忽略)
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.config=config;
        mActionImageFilter.onSurfaceCreated(gl, config);
    }


    /**
     * 当GLSurfaceView中的Surface被改变的时候回调此方法(一般是大小变化)
     * @param gl 同onSurfaceCreated()
     * @param width Surface的宽度
     * @param height Surface的高度
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width=width;
        this.height=height;
        mActionImageFilter.onSurfaceChanged(gl, width, height);
    }

    /**
     * 当Surface需要绘制的时候回调此方法
     * 根据GLSurfaceView.setRenderMode()设置的渲染模式不同回调的策略也不同：
     * GLSurfaceView.RENDERMODE_CONTINUOUSLY : 固定一秒回调60次(60fps)
     * GLSurfaceView.RENDERMODE_WHEN_DIRTY   : 当调用GLSurfaceView.requestRender()之后回调一次
     * @param gl 同onSurfaceCreated()
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        if(refreshFlag&&width!=0&&height!=0){
            mActionImageFilter.onSurfaceCreated(gl, config);
            mActionImageFilter.onSurfaceChanged(gl,width,height);
            refreshFlag=false;
        }
        mActionImageFilter.onDrawFrame(gl);
    }
}
