package data;

import processing.core.PVector;

/**
 * Created by Anand on 2/12/2016.
 */
public class KinematicInfo {
    PVector position;
    float orientation;
    PVector velocity;
    float rotation;

    float maxSpeed = 1000000000;
    long lastUpdateTime = 0;

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public void setPosition(float positionX, float positionY) {
        this.position.x = positionX;
        this.position.y = positionY;
    }

    public float getOrientation() {
        return orientation;
    }

    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.velocity.x = velocityX;
        this.velocity.y = velocityY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public KinematicInfo() {
        lastUpdateTime = System.currentTimeMillis();
        position = new PVector();
        velocity = new PVector();
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }


    /**
     * Kinematic update of the character's information
     *
     * @param steering Info regarding the accelerations experienced by the character
     * @param time     The current time
     */
    public void update(SteeringInfo steering, long time) {
        float elapsedTime = ((float)(time - lastUpdateTime)) / 1000;

        //Update position and orientation
        position.add(PVector.mult(velocity, elapsedTime));

        float newOrientation = orientation + (rotation * elapsedTime);
        newOrientation = Helper.mapToRange(newOrientation);
        orientation = newOrientation;

        //Update velocity and rotation
        velocity.add(PVector.mult(steering.getLinear(), elapsedTime));
        rotation += steering.getAngular() * elapsedTime;

        if (velocity.mag() > maxSpeed) {
            velocity.normalize();
            velocity.mult(maxSpeed);
        }

        lastUpdateTime = time;
    }
}
