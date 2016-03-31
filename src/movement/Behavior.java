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

    public abstract SteeringInfo getSteering();
    public abstract void getSteering(SteeringInfo steering);
}
