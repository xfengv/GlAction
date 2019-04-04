attribute vec4 vPosition;
attribute vec2 vCoordinate;
uniform mat4 vMatrix;
uniform int vertexType;



// 高斯算子左右偏移值，当偏移值为2时，高斯算子为5 x 5
const int SHIFT_SIZE = 2;

uniform float texelWidthOffset;
uniform float texelHeightOffset;
varying vec4 blurShiftCoordinates0[SHIFT_SIZE];
varying vec4 blurShiftCoordinates1[SHIFT_SIZE];


varying vec2 aCoordinate;
varying vec4 aPos;
varying vec4 gPosition;

void main(){
    gl_Position=vMatrix*vPosition;
    aPos=vPosition;
    aCoordinate=vCoordinate.xy;
    gPosition=vMatrix*vPosition;




    if(vertexType==3){
        // 偏移步距
        vec2 singleStepOffset0 = vec2(texelWidthOffset, texelHeightOffset);
        // 记录偏移坐标
        for (int i = 0; i < SHIFT_SIZE; i++) {
            blurShiftCoordinates0[i] = vec4(aCoordinate.xy - float(i + 1) * singleStepOffset0,
            aCoordinate.xy + float(i + 1) * singleStepOffset0);
        }


        // 偏移步距
        vec2 singleStepOffset1 = vec2(texelHeightOffset, texelWidthOffset);
        // 记录偏移坐标
        for (int i = 0; i < SHIFT_SIZE; i++) {
            blurShiftCoordinates1[i] = vec4(aCoordinate.xy - float(i + 1) * singleStepOffset1,
            aCoordinate.xy + float(i + 1) * singleStepOffset1);
        }
    }
}