package decision;

import algorithms.Astar;
import data.*;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Plants a bomb at the current tile, and moves to an unexplored
 * tile outside of the bomb's blast radius
 *
 * Created by Anand on 4/2/2016.
 */
public class ActPlantAndMoveUnexplored extends Action {
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
        curr = currPosX + " " + currPosY;
        currTile = Tile.toTile(curr, map);
        Bomb.plantBomb(currTile, map.parent);

        brick = findUnexploredBrick(currTile.posNum);
        astar = new Astar(map);
        path = astar.pathAstar(curr, brick, "E");
        pathTiles = astar.getTiles(path);
        currentTargetIndex = 0;
        pathTiles.remove(pathTiles.size() - 1);

        subAction = new ActMoveNextTile();
        paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
        subAction.performAction(paramMap);
    }

    public String findUnexploredBrick(PosNum pos) {
        int i, j = 0, currPosX =  pos.colIndex, currPosY = pos.rowIndex,  level= 1;
        String str;
        boolean flag = true;
        int minDist = 2;

        Tile tile;


        do {

            for (i = currPosX - level; flag && i <= currPosX + level; i++) {
                if (i < 0 || i >= map.row)
                    continue;
                for (j = currPosY - level; j <= currPosY + level; j++) {
                    if (j > 0 && j < map.col && map.tiles[i][j].ty == Tile.type.BRICK
                            && Math.abs(i - currPosY) + Math.abs(j - currPosY)>=minDist) {
                        flag = false;
                        break;
                    }
                }

            }
            level++;

        } while (flag);

        str = i + " " + j;
        return str;

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
        return "--- Planting bomb and moving to an unexplored tile";
    }
}
