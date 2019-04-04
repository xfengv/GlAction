precision mediump float;
uniform sampler2D vTexture0;
uniform sampler2D vTexture1;
uniform int vChangeType;
uniform vec3 vChangeColor;
uniform float uXY;

varying vec4 gPosition;
varying vec2 aCoordinate;

// 高斯算子左右偏移值，当偏移值为2时，高斯算子为5 x 5
const int SHIFT_SIZE = 2;
varying vec4 blurShiftCoordinates0[SHIFT_SIZE];
varying vec4 blurShiftCoordinates1[SHIFT_SIZE];

void modifyColor(vec4 color){
    color.r=max(min(color.r, 1.0), 0.0);
    color.g=max(min(color.g, 1.0), 0.0);
    color.b=max(min(color.b, 1.0), 0.0);
    color.a=max(min(color.a, 1.0), 0.0);
}

void main(){
    vec4 nColor=texture2D(vTexture0, aCoordinate);
    if (vChangeType==1){
        //黑白
        float c=nColor.r*vChangeColor.r+nColor.g*vChangeColor.g+nColor.b*vChangeColor.b;
        gl_FragColor=vec4(c, c, c, nColor.a);//只有灰度 abgr
    } else if (vChangeType==2){
        //冷暖色调
        vec4 deltaColor=nColor+vec4(vChangeColor, 0.0);
        modifyColor(deltaColor);
        gl_FragColor=deltaColor;
    } else if (vChangeType==3){

        // 计算当前坐标的颜色值
        vec4 currentColor = texture2D(vTexture0, aCoordinate);




        mediump vec3 sum0 = currentColor.rgb;
        // 计算偏移坐标的颜色值总和
        for (int i = 0; i < SHIFT_SIZE; i++) {
            sum0 += texture2D(vTexture0, blurShiftCoordinates0[i].xy).rgb;
            sum0 += texture2D(vTexture0, blurShiftCoordinates0[i].zw).rgb;
        }

        mediump vec3 sum1 = currentColor.rgb;
        // 计算偏移坐标的颜色值总和
        for (int i = 0; i < SHIFT_SIZE; i++) {
            sum1 += texture2D(vTexture1, blurShiftCoordinates1[i].xy).rgb;
            sum1 += texture2D(vTexture1, blurShiftCoordinates1[i].zw).rgb;
        }


        // 求出平均值
        gl_FragColor = vec4((sum0 +sum1)*0.5 / float(2 * SHIFT_SIZE + 1), currentColor.a);

    } else if (vChangeType==4){
        float dis=distance(vec2(gPosition.x, gPosition.y/uXY), vec2(vChangeColor.r, vChangeColor.g));
        if (dis<vChangeColor.b){
            nColor=texture2D(vTexture0, vec2(aCoordinate.x/2.0+0.25, aCoordinate.y/2.0+0.25));
        }
        gl_FragColor=nColor;
    } else {
        //原图
        gl_FragColor=nColor;
    }
}