package data;

import processing.core.PVector;

/**
 * This class represents the data structure used to store the accelerations output by a steering behaviour.
 * It contains both the linear as well as angular acceleration values.
 *
 * Created by Anand on 2/12/2016.
 */
public class SteeringInfo {
    PVector linear;
    float angular;

    public SteeringInfo() {
        linear = new PVector(0, 0);
        angular = 0;
    }

    public SteeringInfo(PVector linear) {
        this.linear = linear;
        this.angular = 0;
    }

    public SteeringInfo(float angular) {
        this.linear = new PVector(0, 0);
        this.angular = angular;
    }

    public SteeringInfo(PVector linear, float angular) {
        this.linear = linear;
        this.angular = angular;
    }

    public PVector getLinear() {
        return linear;
    }

    public void setLinear(PVector linear) {
        this.linear = linear;
    }

    public void setLinear(float linearX, float linearY) {
        this.linear.x = linearX;
        this.linear.y = linearY;
    }

    public float getAngular() {
        return angular;
    }

    public void setAngular(float angular) {
        this.angular = angular;
    }
}
