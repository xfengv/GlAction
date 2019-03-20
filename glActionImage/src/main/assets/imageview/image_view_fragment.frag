#version 100 es
precision mediump float;

uniform sampler2D vTexture;
uniform int vChangeType;
uniform vec3 vChangeColor;
uniform float uXY;

varying vec4 gPosition;
varying vec2 aCoordinate;

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
        //高斯模糊
        nColor+=texture2D(vTexture, vec2(aCoordinate.x-vChangeColor.r, aCoordinate.y-vChangeColor.r));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x-vChangeColor.r, aCoordinate.y+vChangeColor.r));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x+vChangeColor.r, aCoordinate.y-vChangeColor.r));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x+vChangeColor.r, aCoordinate.y+vChangeColor.r));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x-vChangeColor.g, aCoordinate.y-vChangeColor.g));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x-vChangeColor.g, aCoordinate.y+vChangeColor.g));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x+vChangeColor.g, aCoordinate.y-vChangeColor.g));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x+vChangeColor.g, aCoordinate.y+vChangeColor.g));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x-vChangeColor.b, aCoordinate.y-vChangeColor.b));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x-vChangeColor.b, aCoordinate.y+vChangeColor.b));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x+vChangeColor.b, aCoordinate.y-vChangeColor.b));
        nColor+=texture2D(vTexture, vec2(aCoordinate.x+vChangeColor.b, aCoordinate.y+vChangeColor.b));
        nColor/=13.0;
        gl_FragColor=nColor;
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