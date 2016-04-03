package decision;

import data.BombermanMap;
import data.Const;
import data.Tile;

import java.util.ArrayList;
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
        Tile currTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);
        BombermanMap map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);

        ArrayList<String> adjList = map.edges.get(currTile.toString());
        float maxGERatio = map.getGERatio(currTile, nextTile);

        boolean flag = true;

        for (int i = 0; i < adjList.size(); i += 2) {
            Tile adjTile = Tile.toTile(adjList.get(i), map);
            if (adjTile != nextTile) {
                float geRatio = map.getGERatio(currTile, adjTile);
                if (geRatio > maxGERatio) {
                    flag = false;
                    break;
                }
            }
        }
        return flag ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);
    }

    public String toString() {
        return "+++ Checking to see if next tile has the maximum GE ratio, amongst all adjacent tiles";
    }
}
