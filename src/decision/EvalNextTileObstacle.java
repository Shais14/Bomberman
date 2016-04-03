package decision;

import data.Const;
import data.Tile;

import java.util.HashMap;

/**
 * Evaluates whether or not the next tile is an obstacle
 *
 * Created by Anand on 4/2/2016.
 */
public class EvalNextTileObstacle extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        return (nextTile.ty == Tile.type.OBSTACLE) ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);
    }
}
