package movement;

/**
 * Created by Anand on 2/14/2016.
 */
public abstract class AbstractAlignSteering extends AbstractSteering {
    float maxAngularAcceleration;
    float maxRotation;

    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    public float getMaxRotation() {
        return maxRotation;
    }

    public void setMaxRotation(float maxRotation) {
        this.maxRotation = maxRotation;
    }
}
