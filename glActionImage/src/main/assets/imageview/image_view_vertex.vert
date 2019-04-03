attribute vec4 vPosition;
attribute vec2 vCoordinate;
uniform mat4 vMatrix;
uniform int vertexType;



// 高斯算子大小(3 x 3)
const int GAUSSIAN_SAMPLES = 9;

uniform float texelWidthOffset;
uniform float texelHeightOffset;
varying vec2 blurCoordinates0[GAUSSIAN_SAMPLES];
varying vec2 blurCoordinates1[GAUSSIAN_SAMPLES];


varying vec2 aCoordinate;
varying vec4 aPos;
varying vec4 gPosition;

void main(){
    gl_Position=vMatrix*vPosition;
    aPos=vPosition;
    aCoordinate=vCoordinate;
    gPosition=vMatrix*vPosition;




    if(vertexType==3){
        int multiplier0 = 0;
        vec2 blurStep0;
        vec2 singleStepOffset0 = vec2(texelHeightOffset, texelWidthOffset);
        for (int i = 0; i < GAUSSIAN_SAMPLES; i++) {
            multiplier0 = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
            blurStep0 = float(multiplier0) * singleStepOffset0;
            blurCoordinates0[i] = vCoordinate.xy + blurStep0;
        }
        int multiplier1 = 0;
        vec2 blurStep1;
        vec2 singleStepOffset1 = vec2(texelWidthOffset, texelHeightOffset);
        for (int i = 0; i < GAUSSIAN_SAMPLES; i++) {
            multiplier1 = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
            blurStep1 = float(multiplier1) * singleStepOffset1;
            blurCoordinates1[i] = vCoordinate.xy + blurStep1;
        }
    }
}