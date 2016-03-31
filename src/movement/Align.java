package movement;

import data.Helper;
import data.SteeringInfo;

/**
 * Created by Anand on 2/14/2016.
 */
public class Align extends AbstractAlignSteering {
    float radiusOfSatisfaction, radiusOfDeceleration;
    float timeToTarget;

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
        float rotation = target.getOrientation() - player.getOrientation();
        rotation = Helper.mapToRange(rotation);

        float rotationSize = Math.abs(rotation);

        if (rotationSize < radiusOfSatisfaction) {
            steering.setAngular(0);
            player.setRotation(0);
        } else {
            float targetRotation;
            if (rotationSize > radiusOfDeceleration) {
                targetRotation = maxRotation;
            } else {
                targetRotation = maxRotation * rotationSize / radiusOfDeceleration;
            }

            targetRotation *= rotation / rotationSize;

            float angular = targetRotation - player.getRotation();
            angular = angular /  timeToTarget;

            float angularAcceleration = Math.abs(angular);
            if (angularAcceleration >= Math.abs(maxAngularAcceleration)) {
                angular /= angularAcceleration;
                angular *= maxAngularAcceleration;
            }

            steering.setAngular(angular);
        }
    }

    public boolean checkOrientationReached() {
        return Helper.checkAngleEquality(target.getOrientation(), player.getOrientation());
    }
}
