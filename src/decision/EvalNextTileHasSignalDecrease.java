package decision;

import data.BombermanMap;
import data.Const;
import data.Tile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anand on 4/15/2016.
 */
public class EvalNextTileHasSignalDecrease extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {

        Tile currTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        return nextTile.getSignal()<currTile.getSignal() ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);
    }

    public String toString() {
        return "+++ Checking to see if signal decrease on next tile ";
    }
}
