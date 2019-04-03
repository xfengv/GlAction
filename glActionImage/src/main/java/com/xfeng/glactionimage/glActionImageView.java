package com.xfeng.glactionimage;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.io.IOException;

/**
 * Created by xfengimacgomo
 * data 2019/3/20 10:47
 * email xfengv@yeah.net
 */
public class glActionImageView extends GLSurfaceView {

    private glActionRender mGlActionRender;

    public glActionImageView(Context context) {
        this(context,null);
    }

    public glActionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 设置OpenGL版本(一定要设置)
        setEGLContextClientVersion(2);
        //背景透明
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        mGlActionRender = new glActionRender(this);
        // 设置渲染器
        setRenderer(mGlActionRender);
        // 设置渲染模式为连续模式,手动调用刷新
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mGlActionRender.setScaleType(ActionImageFilter.SCALE_TYPE_FIT_XY);
        try {
            mGlActionRender.setImage(BitmapFactory.decodeStream(getResources().getAssets().open("texture/timg.jpeg")));
            requestLayout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
