package decision;

import data.*;
import data.Character;
import movement.PredictionHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anand on 4/17/2016.
 */
public class EvalPredictEnemyMove extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        return PredictionHelper.isCollisionPredicted(paramMap) ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);
    }
}
