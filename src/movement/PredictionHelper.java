package movement;

import data.*;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anand on 4/17/2016.
 */
public class PredictionHelper {
    public static Enemy enemyToCollideWith = null;
    public static boolean isCollisionPredicted(HashMap<Integer, Object> paramMap) {
        // is enemy at a position 1 from the target tile?
        Tile targetTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);
        data.Character currCharacter = (data.Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        ArrayList<Enemy> enemies = (ArrayList<Enemy>) paramMap.get(Const.DecisionTreeParams.ENEMY_KEY);
        BombermanMap map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);

        ArrayList<String> adjList = map.edges.get(targetTile.toString());
        for (Enemy currEnemy: enemies) {
            if (currEnemy != currCharacter) {
                Tile enemyTile = map.getTileAt(currEnemy.kinematicInfo.getPosition());
                if (enemyTile == targetTile) {
                    enemyToCollideWith = currEnemy;
                    return true;
                }

                PVector nextTileCord = PVector.add(enemyTile.posCord, PVector.fromAngle(currEnemy.kinematicInfo.getOrientation()).mult(map.tileSize));
                Tile predictedTile = map.getTileAt(nextTileCord);

                if (predictedTile == targetTile) {
                    enemyToCollideWith = currEnemy;
                    return true;
                }

                for (int i = 0; i < adjList.size(); i += 2) {
                    String adjTile = adjList.get(i);
                    Tile currTile = map.toTile(adjTile);
                    if (enemyTile == currTile || predictedTile == currTile) {
                        enemyToCollideWith = currEnemy;
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
