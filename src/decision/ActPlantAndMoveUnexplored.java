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
        Tile currTile;
        ArrayList<String> path;


        map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);
        player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        currTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);
        paramMap.put(Const.DecisionTreeParams.BOMB_KEY, Bomb.plantBomb(currTile, map.parent));


        pathTiles = findUnexploredBrickPath(currTile.posNum);

        currentTargetIndex = 0;
        if (pathTiles.size() > 2) {
            subAction = new ActMoveNextTile();
            paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
            subAction.performAction(paramMap);
        } else {
            //TODO: Still try to seek cover, maybe?
            DebugUtil.printDebugString("$$$ Going to die");
            subAction = null;
        }
    }

    public ArrayList<Tile> findUnexploredBrickPath(PosNum pos) {
        int i, j, k, currRow, currCol,  level;
        String nextTileString, currTileString;
        ArrayList<Tile> emptyTiles;
        boolean flag = true;
        Astar astar  = new Astar(map);
        ArrayList<String> path = new ArrayList<String>();

        currRow =  pos.rowIndex;
        currCol = pos.colIndex;
        currTileString = map.tiles[currRow][currCol].toString();
        level= 1;

        do {

            for (i = currRow - level; flag && i <= currRow + level; i = i + 2*level)
            {
                if (i <= 0 || i >= map.row-1)
                    continue;
                for (j = currCol - level; flag && j <= currCol + level; j++)
                {
                    if (j <= 0 || i >= map.col-1)
                        continue;

                    if (map.tiles[i][j].ty != Tile.type.BRICK || Math.abs(i-currRow) + Math.abs(j - currCol)==1)
                        continue;

                    emptyTiles = getEmptyTiles(currRow, currCol, i, j);

                    for (k = 0; flag && k < emptyTiles.size(); k++)
                    {
                        nextTileString = emptyTiles.get(k).toString();
                        path = astar.pathAstar(currTileString, nextTileString, "E");
                        if (path.size() > 2)
                            flag = false;
                    }
                }
            }

            for (i = currRow - level + 1; flag && i < currRow + level; i++)
            {

                if (i <= 0 || i >= map.row-1)
                    continue;

                for (j = currCol - level; flag && j <= currCol + level; j = j + 2 * level) {
                    if (j <= 0 || j >= map.col - 1)
                        continue;

                    if (map.tiles[i][j].ty != Tile.type.BRICK || Math.abs(i-currRow) + Math.abs(j - currCol)==1)
                        continue;

                    emptyTiles = getEmptyTiles(currRow, currCol, i, j);
                    for (k = 0; k < emptyTiles.size(); k++)
                    {
                        nextTileString = emptyTiles.get(k).toString();
                        path = astar.pathAstar(currTileString, nextTileString, "E");
                        if (path.size() > 2)
                            flag = false;

                    }
                }
            }
            level++;

        } while (flag);

        return astar.getTiles(path);
    }


    public ArrayList<Tile> getEmptyTiles(int currRow, int currCol, int i, int j)
    {
        String str;
        int brickRow = 0, brickCol = 0,minDist = 2;
        ArrayList<String> edgeList;
        ArrayList<Tile> emptyTiles = new ArrayList<Tile>();
        Tile brick, currTile, emptyTile;
//        float x1, y1, x2, y2, min  = Float.MAX_VALUE;
//        x1 = map.tiles[currRow][currCol].posCord.x;
//        y1 = map.tiles[currRow][currCol].posCord.y;

        currTile = map.tiles[currRow][currCol];
        brick = map.tiles[i][j];

        edgeList = map.edges.get(brick.toString());
        for (int k = 0; k<edgeList.size(); k = k+2)
        {
            str = edgeList.get(k);
            emptyTile = Tile.toTile(str, map);
            if(emptyTile.ty == Tile.type.EMPTY && currTile!=emptyTile && Math.abs(emptyTile.posNum.rowIndex - currRow) +
                    Math.abs(emptyTile.posNum.colIndex - currCol)>=minDist)
            {
                emptyTiles.add(emptyTile);
//                x2 = tile1.posCord.x;
//                y2 = tile1.posCord.y;
//                if ( min < Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2) );
//                {
//                    brickRow = tile1.posNum.rowIndex;
//                    brickCol = tile1.posNum.colIndex;
//                }

            }
        }

        return emptyTiles;
//        if ( brickRow == 0 && brickCol == 0)
//            return null;
//        return map.tiles[brickRow][brickCol].toString();


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
