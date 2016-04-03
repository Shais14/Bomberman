package decision;

import java.util.HashMap;

/**
 * Plants a bomb at the current tile, and moves to an unexplored
 * tile outside of the bomb's blast radius
 *
 * Created by Anand on 4/2/2016.
 */
public class ActPlantAndMoveUnexplored extends Action {
    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {

    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        return false;
    }
}
