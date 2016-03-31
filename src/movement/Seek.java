package movement;

import data.SteeringInfo;
import processing.core.PVector;

/**
 * Created by Anand on 2/13/2016.
 */
public class Seek extends AbstractSteering {
    @Override
    public void getSteering(SteeringInfo steering) {
        PVector posDiff = PVector.sub(target.getPosition(), player.getPosition());

        steering.setLinear(posDiff);

        steering.getLinear().normalize();
        steering.getLinear().mult(maxAcceleration);
    }

}
