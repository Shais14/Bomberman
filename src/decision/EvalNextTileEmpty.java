package decision;

import data.Const;
import data.PlayerInfo;
import data.Tile;

import java.util.HashMap;

/**
 * Evaluates whether or not the next tile is empty
 *
 * Created by Anand on 4/2/2016.
 */
public class EvalNextTileEmpty extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        return (nextTile.ty == Tile.type.EMPTY) ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);
    }

    public String toString() {
        return "+++ Checking to see if next tile is empty";
    }
}
