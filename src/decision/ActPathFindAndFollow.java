package decision;

import java.util.HashMap;

/**
 * Finds the shortest path (using AStar) to aa target node,
 * and then proceeds to follow that path
 *
 * Created by Anand on 4/2/2016.
 */
public class ActPathFindAndFollow extends Action {
    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {

    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        return false;
    }

    public String toString() {
        return "--- Finding path to target, and proceeding to follow it";
    }
}
