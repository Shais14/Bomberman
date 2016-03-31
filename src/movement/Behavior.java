package movement;

import data.SteeringInfo;

/**
 * Created by Anand on 2/15/2016.
 */
public abstract class Behavior {
    public float weight;

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * This method is used when there is no steering object that has been output from another steering behaviour.
     */
    public abstract SteeringInfo getSteering();

    /**
     * This method is to be used when this behaviour is supposed to add on to another steering
     * behaviour that was computed elsewhere. For example - if this steering behaviour is
     * computed after computing the acceleration output by an align behaviour.
     *
     * @param steering Steering behaviour that was computed by another steering behaviour
     */
    public abstract void getSteering(SteeringInfo steering);
}
