package com.xfeng.glactionimage;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by xfengimacgomo
 * data 2019/3/20 17:40
 * email xfengv@yeah.net
 */
public class ActionImageColorFiter extends ActionImageFilter {

    private  Filter mFilter;
    private int hChangeType;
    private int hChangeColor;
    private int mTexelWidthOffset;
    private int mTexelHeightOffset;
    private int mVertexType;

    public ActionImageColorFiter(Context context, Filter filter) {
        super(context, "imageview/image_view_vertex.vert", "imageview/image_view_fragment.frag");
        this.mFilter = filter;
    }

    @Override
    public void onDrawSet() {
        GLES20.glUniform1i(hChangeType,mFilter.getType());
        GLES20.glUniform1i(mVertexType,mFilter.getType());
        GLES20.glUniform3fv(hChangeColor,1,mFilter.data(),0);
        isBlur=true;
        GLES20.glUniform1f(mTexelWidthOffset, 1f/100f);
        GLES20.glUniform1f(mTexelHeightOffset, 1f/100f);
    }

    @Override
    public void onDrawCreatedSet(int program) {
        hChangeType=GLES20.glGetUniformLocation(program,"vChangeType");
        hChangeColor=GLES20.glGetUniformLocation(program,"vChangeColor");
        mTexelWidthOffset = GLES20.glGetUniformLocation(program, "texelWidthOffset");
        mTexelHeightOffset = GLES20.glGetUniformLocation(program, "texelHeightOffset");
        mVertexType = GLES20.glGetUniformLocation(program, "vertexType");
    }

    public enum Filter{

        NONE(0,new float[]{0.0f,0.0f,0.0f}),
        GRAY(1,new float[]{0.299f,0.587f,0.114f}),
        COOL(2,new float[]{0.0f,0.0f,0.1f}),
        WARM(2,new float[]{0.1f,0.1f,0.0f}),
        BLUR(3,new float[]{0.006f,0.004f,0.002f}),
        MAGN(4,new float[]{0.0f,0.0f,0.4f});


        private int vChangeType;
        private float[] data;

        Filter(int vChangeType,float[] data){
            this.vChangeType=vChangeType;
            this.data=data;
        }

        public int getType(){
            return vChangeType;
        }

        public float[] data(){
            return data;
        }

    }
}
