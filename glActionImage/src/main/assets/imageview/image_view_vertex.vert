attribute vec4 vPosition;
attribute vec2 vCoordinate;
uniform mat4 vMatrix;
uniform int vertexType;



// 高斯算子大小(3 x 3)
const int GAUSSIAN_SAMPLES = 9;

uniform float texelWidthOffset;
uniform float texelHeightOffset;
varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];


varying vec2 aCoordinate;
varying vec4 aPos;
varying vec4 gPosition;

void main(){
    gl_Position=vMatrix*vPosition;
    aPos=vPosition;
    aCoordinate=vCoordinate;
    gPosition=vMatrix*vPosition;



    int multiplier = 0;
    vec2 blurStep;
    vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset);
    for (int i = 0; i < GAUSSIAN_SAMPLES; i++) {
        multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
        blurStep = float(multiplier) * singleStepOffset;
        blurCoordinates[i] = vCoordinate.xy + blurStep;
    }
}