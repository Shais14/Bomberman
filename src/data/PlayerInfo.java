package data;

import processing.core.PVector;

/**
 * This subclass of {@link KinematicInfo} is used to represent a player or an enemy.
 * The main difference between this class and its parent, is purely semantic, though.
 * However, in the future, this class may see some extension and hence should
 * be used to represent drawable characters, while the parent class is used for specifying targets only.
 *
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
