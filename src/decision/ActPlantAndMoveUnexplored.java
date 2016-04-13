package decision;

import algorithms.Astar;
import data.*;
import debug.DebugUtil;

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
        String currTileString, brickString;
        Tile currTile;
        ArrayList<String> path;
        Astar astar;


        map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);
        player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        currTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);
        paramMap.put(Const.DecisionTreeParams.BOMB_KEY, Bomb.plantBomb(currTile, map.parent));

        currTileString = currTile.toString();
        brickString = findUnexploredBrick(currTile.posNum);
        astar = new Astar(map);
        DebugUtil.printDebugString("Attempting to find path from " + currTileString + " (current tile) -> " + brickString + " (unexplored tile)");
        path = astar.pathAstar(currTileString, brickString, "E");
        pathTiles = astar.getTiles(path);
        currentTargetIndex = 0;
        if (pathTiles.size() > 2) {
//            pathTiles.remove(pathTiles.size() - 1);
            subAction = new ActMoveNextTile();
            paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
            subAction.performAction(paramMap);
        } else {
            //TODO: Still try to seek cover, maybe?
            DebugUtil.printDebugString("$$$ Going to die");
            subAction = null;
        }
    }

    public String findUnexploredBrick(PosNum pos) {
        int i, j = 0, currRow =  pos.rowIndex, currCol = pos.colIndex,  level= 1;
        String str;
        boolean flag = true;
        int brickRow = 0, brickCol = 0,minDist = 2;
        ArrayList<String> edgeList;
        Tile tile, tile1;
        float x1, y1, x2, y2, min  = Float.MAX_VALUE;

        x1 = map.tiles[currRow][currCol].posCord.x;
        y1 = map.tiles[currRow][currCol].posCord.y;

        do {

            for (i = currRow - level; flag && i <= currRow + level; i++) {
                if (i <= 0 || i >= map.row - 1)
                    continue;
                for (j = currCol - level; flag && j <= currCol + level; j++) {
                    if (j > 0 && j < map.col-1 && map.tiles[i][j].ty == Tile.type.BRICK
                            && Math.abs(i - currRow) + Math.abs(j - currCol)>=minDist) {
                        tile = map.tiles[i][j];
                        edgeList = map.edges.get(tile.toString());
                        for (int k = 0; k<edgeList.size(); k = k+2)
                        {
                            str = edgeList.get(k);
                            tile1 = Tile.toTile(str, map);
                            if(tile1.ty == Tile.type.EMPTY)
                            {
                                x2 = tile1.posCord.x;
                                y2 = tile1.posCord.y;
                                if (min<Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
                                {
                                    brickRow = tile1.posNum.rowIndex;
                                    brickCol = tile1.posNum.colIndex;
                                    flag = false;
                                }

                            }
                        }
                    }
                }

            }
            level++;

        } while (flag);

        str = brickRow + " " + brickCol;
        return str;

    }


    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        if (subAction != null && subAction.hasCompleted(paramMap)) {
            if (currentTargetIndex + 1 < pathTiles.size()) {
                // Unexplored tile not yet reached -> bomb may or may not have detonated (but we don't care about it yet)
                currentTargetIndex++;
                subAction = new ActMoveNextTile();
                paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
                subAction.performAction(paramMap);
                return false;
            } else {
                // Unexplored tile reached -> check if bomb detonated
                // If bomb detonated (and unexplored tile reached) -> action complete
                return paramMap.get(Const.DecisionTreeParams.BOMB_KEY) == null;
            }
        }
        return false;
    }

    public String toString() {
        String actionInfo = "--- Planting bomb and moving to an unexplored tile";

        if (pathTiles!= null && pathTiles.size() > 0) {
            actionInfo += (" while following the path - " + pathTiles);
        }

        return actionInfo;
    }
}
