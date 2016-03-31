package movement;

import data.KinematicInfo;
import data.PlayerInfo;
import data.SteeringInfo;

/**
 * An abstract class to represent the various methods to be associated with a steering behaviour.
 *
 * Created by Anand on 2/13/2016.
 */
public abstract class AbstractSteering extends Behavior {
    float maxAcceleration;

    KinematicInfo target;
    PlayerInfo player;

    public AbstractSteering() {
    }

    public AbstractSteering(float maxAcceleration, KinematicInfo target, PlayerInfo player) {
        this.maxAcceleration = maxAcceleration;
        this.target = target;
        this.player = player;
    }

    public float getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(float maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public KinematicInfo getTarget() {
        return target;
    }

    public void setTarget(KinematicInfo target) {
        this.target = target;
    }

    public PlayerInfo getPlayer() {
        return player;
    }

    public void setPlayer(PlayerInfo player) {
        this.player = player;
    }

    @Override
    public SteeringInfo getSteering() {
        SteeringInfo steering = new SteeringInfo();
        getSteering(steering);
        return steering;
    }

    public abstract void getSteering(SteeringInfo steering);
}
