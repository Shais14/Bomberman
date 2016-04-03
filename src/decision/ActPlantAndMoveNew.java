package decision;

import java.util.HashMap;

/**
 * Plants a bomb at the current tile,
 * moves outside the bomb's blast radius,
 * waits till the bomb's detonation,
 * and then moves to the newly emptied tile
 *
 * Created by Anand on 4/2/2016.
 */
public class ActPlantAndMoveNew extends Action {
    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {

    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        return false;
    }
}
