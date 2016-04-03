package decision;

import java.util.HashMap;

/**
 * Evaluates whether or not the next tile has the
 * maximum gain-to-effort ratio, amongst all adjacent tiles
 *
 * Created by Anand on 4/2/2016.
 */
public class EvalNextTileHasMaxGE extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        //TODO: Replace this code with actual stuff
        return children.get(TRUE_INDEX);
    }

    public String toString() {
        return "+++ Checking to see if next tile has the maximum GE ratio, amongst all adjacent tiles";
    }
}
