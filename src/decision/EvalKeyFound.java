package decision;

import data.BombermanMap;
import data.Const;

import java.util.HashMap;

/**
 * Evaluates whether or not the key / treasure has been found
 *
 * Created by Anand on 4/2/2016.
 */
public class EvalKeyFound extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        BombermanMap map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);
        return map.isTreaureVisible() ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);
    }

    public String toString() {
        return "+++ Checking if key found";
    }
}
