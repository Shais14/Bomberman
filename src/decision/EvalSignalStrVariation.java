package decision;

import data.BombermanMap;
import data.Const;
import data.PlayerInfo;
import data.Tile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Evaluates whether or not there is a variation in signal strength among adjacent tiles
 * <p/>
 * Created by Anand on 4/2/2016.
 */
public class EvalSignalStrVariation extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        BombermanMap map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);
        Tile currTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);

        boolean flag = false;
        ArrayList<String> adjList = map.edges.get(currTile.toString());
        for (int i = 0; i < adjList.size(); i += 2) {
            String adjTileStr = adjList.get(i);
            Tile adjTile = Tile.toTile(adjTileStr, map);
            if (adjTile.signal != currTile.signal) {
                flag = true;
                break;
            }
        }

        return flag ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);
    }
}
