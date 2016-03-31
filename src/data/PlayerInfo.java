package data;

import processing.core.PVector;

/**
 * Created by Anand on 2/12/2016.
 */
public class PlayerInfo extends KinematicInfo {
    public PlayerInfo() {
        super();
    }

    public float getNewOrientation() {
        return getNewOrientation(this);
    }

    public float getNewOrientation(KinematicInfo dynamicInfo) {
        if (dynamicInfo.getVelocity().magSq() > 0) {
            return dynamicInfo.getVelocity().heading();
        }

        return orientation;
    }

    public float getNewOrientation(SteeringInfo steeringInfo) {
        if (steeringInfo.getLinear().magSq() > 0) {
            return steeringInfo.getLinear().heading();
        }

        return orientation;
    }

    public void updateOrientation() {
        orientation = getNewOrientation();
    }

    public void updateOrientation(KinematicInfo dynamicInfo) {
        orientation = getNewOrientation(dynamicInfo);
    }

    public void updateOrientation(SteeringInfo steeringInfo) {
        orientation = getNewOrientation(steeringInfo);
    }
}
