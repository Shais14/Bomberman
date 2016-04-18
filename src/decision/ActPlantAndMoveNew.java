package decision;

import data.*;
import debug.DebugUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Plants a bomb at the current tile,
 * moves outside the bomb's blast radius,
 * waits till the bomb's detonation,
 * and then moves to the newly emptied tile
 *
 * Created by Anand on 4/2/2016.
 */
public class ActPlantAndMoveNew extends Action {
    BombermanMap map;
    PlayerInfo player;
    Tile currTile;
    Tile targetTile;

    ArrayList<Tile> pathTiles;
    int currentTargetIndex;

    Action subAction;

    int currentTask;

    static final int SEEK_COVER = 0;
    static final int RETURN_TO_TILE = 1;
    static final int MOVING_TO_TARGET = 2;

    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {
        map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);
        player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        currTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);
        targetTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);
        paramMap.put(Const.DecisionTreeParams.BOMB_KEY, Bomb.plantBomb(currTile, map.parent));

        pathTiles = map.getPathToCover(player.kinematicInfo.getPosition());
        DebugUtil.printDebugString("Attempting to move from " + currTile + " (current tile) -> " +
                targetTile + " (target tile) through : " + pathTiles);
        currentTargetIndex = 0;
        if (pathTiles.size() > 0) {
//            pathTiles.remove(pathTiles.size() - 1);
            subAction = new ActMoveNextTile();
            paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
            currentTask = SEEK_COVER;
            subAction.performAction(paramMap);
        } else {
            subAction = null;
        }
    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        if (subAction != null && subAction.hasCompleted(paramMap)) {
            if (currentTask == ActPlantAndMoveNew.SEEK_COVER) {
                if (currentTargetIndex + 1 < pathTiles.size()) {
                    // Select next tile to move to (while seeking)
                    currentTargetIndex++;
                    subAction = new ActMoveNextTile();
                    paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
                    subAction.performAction(paramMap);
                    return false;
                } else {
                    // Check if bomb has detonated
                    if (paramMap.get(Const.DecisionTreeParams.BOMB_KEY) == null) {
                        // Bomb detonated, now return to original target tile
                        // Now return to original tile
                        Collections.reverse(pathTiles);
                        pathTiles.add(currTile);
                        currentTargetIndex = 0;
                        subAction = new ActMoveNextTile();
                        paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));

                        currentTask = ActPlantAndMoveNew.RETURN_TO_TILE;
                        subAction.performAction(paramMap);
                        return false;
                    } else {
                        // Wait for bomb to detonate
                        return false;
                    }
                }
            } else if (currentTask == ActPlantAndMoveNew.RETURN_TO_TILE) {
                if (currentTargetIndex + 1 < pathTiles.size()) {
                    // Select next tile to move to (while seeking)
                    currentTargetIndex++;
                    subAction = new ActMoveNextTile();
                    paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, pathTiles.get(currentTargetIndex));
                    subAction.performAction(paramMap);
                    return false;
                } else {
                    // Bomb detonated and reached original tile, now move to targetTile
                    subAction = new ActMoveNextTile();
                    paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, targetTile);

                    currentTask = ActPlantAndMoveNew.MOVING_TO_TARGET;
                    subAction.performAction(paramMap);
                    return false;
                }
            } else if (currentTask == ActPlantAndMoveNew.MOVING_TO_TARGET) {
                // Cover sought -> bomb detonated -> original position reached -> moved to target position
                return true;
            }
        }
        return false;
    }

    @Override
    public String getNextTarget(HashMap<Integer, Object> paramMap) {
        if (targetTile == null) {
            return "";
        }

        if (subAction == null) {
            return targetTile.toString();
        }

        return  targetTile.toString() + " [" + subAction.getNextTarget(paramMap) + "]";
    }

    public String toString() {
        return "--- Planting bomb and moving to newly emptied tile";
    }
}
