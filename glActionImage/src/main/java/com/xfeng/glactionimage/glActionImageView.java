package com.xfeng.glactionimage;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by xfengimacgomo
 * data 2019/3/20 10:47
 * email xfengv@yeah.net
 */
public class glActionImageView extends GLSurfaceView {
    public glActionImageView(Context context) {
        this(context,null);
    }

    public glActionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);


    }
}
