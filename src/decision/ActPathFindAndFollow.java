package decision;

import algorithms.Astar;
import data.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Finds the shortest path (using AStar) to aa target node,
 * and then proceeds to follow that path
 *
 * Created by Anand on 4/2/2016.
 */
public class ActPathFindAndFollow extends Action {
    BombermanMap map;
    PlayerInfo player;
    ArrayList<Tile> pathTiles;
    int currentTargetIndex;
    Action subAction;

    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {
        int currPosX, currPosY;
        String curr, brick;
        Tile currTile;
        ArrayList<String> path;
        Astar astar;

        map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);
        player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        currPosX = map.quantizeX(player.kinematicInfo.getPosition());
        currPosY = map.quantizeY(player.kinematicInfo.getPosition());
        curr = currPosY + " " + currPosX;
        currTile = Tile.toTile(curr, map);
        Bomb.plantBomb(currTile, map.parent);

        brick = map.Treasure;
        astar = new Astar(map);
        path = astar.pathAstar(curr, brick, "E");
        pathTiles = astar.getTiles(path);

        subAction = new ActMoveNextTile();
        paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
        subAction.performAction(paramMap);
    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        if (subAction.hasCompleted(paramMap)) {
            if (currentTargetIndex + 1 < pathTiles.size()) {
                currentTargetIndex++;
                subAction = new ActMoveNextTile();
                paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
                subAction.performAction(paramMap);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "--- Finding path to target, and proceeding to follow it";
    }
}
