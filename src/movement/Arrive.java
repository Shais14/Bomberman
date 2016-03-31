package movement;

import data.SteeringInfo;
import processing.core.PVector;

/**
 * Class representing the steering behaviour, Arrive.
 * Parameters involeved here are - satisfaction distance (radiusOfSatisfaction),
 * deceleration distance (radiusOfDeceleration) and time to target velocity
 *
 * Created by Anand on 2/14/2016.
 */
public class Arrive extends AbstractSteering {
    float maxSpeed;
    float radiusOfSatisfaction, radiusOfDeceleration;
    float timeToTarget;

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getRadiusOfSatisfaction() {
        return radiusOfSatisfaction;
    }

    public void setRadiusOfSatisfaction(float radiusOfSatisfaction) {
        this.radiusOfSatisfaction = radiusOfSatisfaction;
    }

    public float getRadiusOfDeceleration() {
        return radiusOfDeceleration;
    }

    public void setRadiusOfDeceleration(float radiusOfDeceleration) {
        this.radiusOfDeceleration = radiusOfDeceleration;
    }

    public float getTimeToTarget() {
        return timeToTarget;
    }

    public void setTimeToTarget(float timeToTarget) {
        this.timeToTarget = timeToTarget;
    }

    @Override
    public void getSteering(SteeringInfo steering) {
        float speed;

        PVector posDiff = PVector.sub(target.getPosition(), player.getPosition());
        float distance = posDiff.mag();

        if (distance < radiusOfSatisfaction) {
            PVector newAcceleration = new PVector();
            newAcceleration.x = player.getVelocity().x;
            newAcceleration.y = player.getVelocity().y;

            newAcceleration.mult(-1);
            newAcceleration.div(timeToTarget);
            steering.setLinear(newAcceleration);
        } else {
            if (distance > radiusOfDeceleration) {
                speed = maxSpeed;
            } else {
                speed = maxSpeed * distance / radiusOfDeceleration;
            }

            posDiff.normalize();
            posDiff.mult(speed);

            posDiff.sub(player.getVelocity());
            posDiff.div(timeToTarget);
            steering.setLinear(posDiff);
        }
    }
}
