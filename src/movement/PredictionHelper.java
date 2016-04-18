package movement;

import data.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anand on 4/17/2016.
 */
public class PredictionHelper {
    public static boolean isCollisionPredicted(HashMap<Integer, Object> paramMap) {
        // is enemy at a position 1 from the target tile?
        Tile targetTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);
        data.Character currCharacter = (data.Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        ArrayList<Enemy> enemies = (ArrayList<Enemy>) paramMap.get(Const.DecisionTreeParams.ENEMY_KEY);
        BombermanMap map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);

        ArrayList<String> adjList = map.edges.get(targetTile.toString());
        for (int i = 0; i < adjList.size(); i += 2) {
            String adjTile = adjList.get(i);
            Tile currTile = map.toTile(adjTile);
            for (Enemy currEnemy: enemies) {
                Tile enemyTile = map.getTileAt(currEnemy.kinematicInfo.getPosition());
                if (enemyTile == currTile && currEnemy != currCharacter) {
                    //TODO: Use enemy's heading as well
                    return true;
                }
            }
        }
        return false;
    }
}
