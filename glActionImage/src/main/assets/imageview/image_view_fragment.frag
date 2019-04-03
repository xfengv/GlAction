precision mediump float;
uniform sampler2D vTexture;
uniform int vChangeType;
uniform vec3 vChangeColor;
uniform float uXY;

varying vec4 gPosition;
varying vec2 aCoordinate;

const lowp int GAUSSIAN_SAMPLES = 9;
varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];

void modifyColor(vec4 color){
    color.r=max(min(color.r, 1.0), 0.0);
    color.g=max(min(color.g, 1.0), 0.0);
    color.b=max(min(color.b, 1.0), 0.0);
    color.a=max(min(color.a, 1.0), 0.0);
}

void main(){
    vec4 nColor=texture2D(vTexture, aCoordinate);
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
        lowp vec3 sum = vec3(0.0);
        lowp vec4 fragColor=texture2D(vTexture,aCoordinate);

        sum += texture2D(vTexture, blurCoordinates[0]).rgb * 0.05;
        sum += texture2D(vTexture, blurCoordinates[1]).rgb * 0.09;
        sum += texture2D(vTexture, blurCoordinates[2]).rgb * 0.12;
        sum += texture2D(vTexture, blurCoordinates[3]).rgb * 0.15;
        sum += texture2D(vTexture, blurCoordinates[4]).rgb * 0.18;
        sum += texture2D(vTexture, blurCoordinates[5]).rgb * 0.15;
        sum += texture2D(vTexture, blurCoordinates[6]).rgb * 0.12;
        sum += texture2D(vTexture, blurCoordinates[7]).rgb * 0.09;
        sum += texture2D(vTexture, blurCoordinates[8]).rgb * 0.05;

        gl_FragColor = vec4(sum, fragColor.a);

    } else if (vChangeType==4){
        float dis=distance(vec2(gPosition.x, gPosition.y/uXY), vec2(vChangeColor.r, vChangeColor.g));
        if (dis<vChangeColor.b){
            nColor=texture2D(vTexture, vec2(aCoordinate.x/2.0+0.25, aCoordinate.y/2.0+0.25));
        }
        gl_FragColor=nColor;
    } else {
        //原图
        gl_FragColor=nColor;
    }
}