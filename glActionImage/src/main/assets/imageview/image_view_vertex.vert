#version 100 es
attribute vec4 vPosition;
attribute vec2 vCoordinate;
uniform mat4 vMatrix;
varying vec2 aCoordinate;
void main() {
    gl_FragColor = vMatrix*vPosition;
    aCoordinate=vCoordinate;
}
