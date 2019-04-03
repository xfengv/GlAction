package com.xfeng.glactionimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.xfeng.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by xfengimacgomo
 * data 2019/3/20 10:50
 * email xfengv@yeah.net
 */
public abstract class ActionImageFilter implements GLSurfaceView.Renderer {
    private final float[] vertices = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    };

    private final float[] coordinates = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };
    private Context mContext;
    private String mVertex;
    private String mFragment;
    private FloatBuffer mVerticesfloatBuffer;
    private FloatBuffer mFragfloatBuffer;
    private Bitmap mBitmap;
    //都是4x4矩阵
    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int mProgram;
    private int mGlHPosition;
    private int mGlHCoordinate;
    private int mGlHTexture;
    private int mGlHMatrix;
    private int mGlHUxy;
    private float uXY;
    public static final int SCALE_TYPE_FIT_XY = 1001;
    public static final int SCALE_TYPE_CENTER_CROP = 1002;
    public static final int SCALE_TYPE_CENTER_INSIDE = 1003;
    private static int mCurrentScaleType = SCALE_TYPE_CENTER_CROP;

    public ActionImageFilter(Context context, String vertex, String fragment) {
        this.mContext = context;
        this.mVertex = vertex;
        this.mFragment = fragment;
        //开辟内存空间,一个float(c语言)占用四个字节
        ByteBuffer verticesByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        //设置字节顺序
        verticesByteBuffer.order(ByteOrder.nativeOrder());
        //转换为float缓冲类型
        mVerticesfloatBuffer = verticesByteBuffer.asFloatBuffer();
        //向缓冲中放入数据
        mVerticesfloatBuffer.put(vertices);
        //设置缓冲区的起始位置
        mVerticesfloatBuffer.position(0);


        ByteBuffer fragByteBuffer = ByteBuffer.allocateDirect(coordinates.length * 4);
        fragByteBuffer.order(ByteOrder.nativeOrder());
        mFragfloatBuffer = fragByteBuffer.asFloatBuffer();
        mFragfloatBuffer.put(coordinates);
        mFragfloatBuffer.position(0);
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public void setScaleType(int scaleType) {
        this.mCurrentScaleType = scaleType;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        // 初始化着色器
        // 基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtils.createProgram(mContext.getResources(), mVertex, mFragment);
        // 获取着色器中的属性引用id(传入的字符串就是我们着色器脚本中的属性名)
        mGlHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mGlHCoordinate = GLES20.glGetAttribLocation(mProgram, "vCoordinate");
        mGlHTexture = GLES20.glGetUniformLocation(mProgram, "vTexture");
        mGlHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");

        //其他的一些Uniform数据
        onDrawCreatedSet(mProgram);
        // 设置clear color颜色RGBA(这里仅仅是设置清屏时GLES20.glClear()用的颜色值而不是执行清屏)
        gl.glClearColor(0, 0, 0, 0);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        float sWH = w / (float) h;
        float sWidthHeight = width / (float) height;
        uXY = sWidthHeight;
        switch (mCurrentScaleType) {
            case SCALE_TYPE_FIT_XY:
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1, 1, 3, 5);
                break;
            case SCALE_TYPE_CENTER_CROP:
                if (width > height) {
                    if (sWH > sWidthHeight) {
                        Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight / sWH, sWidthHeight / sWH, -1, 1, 3, 5);
                    } else {
                        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH / sWidthHeight, sWH / sWidthHeight, 3, 5);
                    }
                } else {
                    if (sWH > sWidthHeight) {
                        Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight / sWH, sWidthHeight / sWH, -1, 1, 3, 5);
                    } else {
                        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH / sWidthHeight, sWH / sWidthHeight, 3, 5);
                    }
                }
                break;
            case SCALE_TYPE_CENTER_INSIDE:
                if (width > height) {
                    if (sWH > sWidthHeight) {
                        Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight * sWH, sWidthHeight * sWH, -1, 1, 3, 5);
                    } else {
                        Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight / sWH, sWidthHeight / sWH, -1, 1, 3, 5);
                    }
                } else {
                    if (sWH > sWidthHeight) {
                        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH, 3, 5);
                    } else {
                        Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH / sWidthHeight, sWH / sWidthHeight, 3, 5);
                    }
                }
                break;

        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清屏
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        // 使用某套shader程序
        GLES20.glUseProgram(mProgram);
        onDrawSet();


        GLES20.glUniformMatrix4fv(mGlHMatrix, 1, false, mMVPMatrix, 0);
        GLES20.glUniform1i(mGlHTexture, 0);
        //创建texture
        createTexture();
        // 为画笔指定顶点位置数据(mGlHPosition)
        GLES20.glVertexAttribPointer(mGlHPosition, 2, GLES20.GL_FLOAT, false, 0, mVerticesfloatBuffer);
        GLES20.glEnableVertexAttribArray(mGlHPosition);
        // 为画笔指定顶点位置数据(mGlHCoordinate)
        GLES20.glVertexAttribPointer(mGlHCoordinate, 2, GLES20.GL_FLOAT, false, 0, mFragfloatBuffer);
        GLES20.glEnableVertexAttribArray(mGlHCoordinate);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    private int createTexture() {
        int[] texture = new int[1];
        if (mBitmap != null && !mBitmap.isRecycled()) {
            //生成纹理
            GLES20.glGenTextures(1, texture, 0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }

    //更多的uniform信息
    public abstract void onDrawSet();

    //更多的uniform位置
    public abstract void onDrawCreatedSet(int program);

}
