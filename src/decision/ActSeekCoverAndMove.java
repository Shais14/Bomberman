package decision;

import data.*;
import data.Character;
import debug.DebugUtil;
import movement.PredictionHelper;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Anand on 4/17/2016.
 */
public class ActSeekCoverAndMove extends Action {
    Tile currSeekTile;
    Tile initialTile;
    ArrayList<Tile> pathTiles;
    data.Character currCharacter;
    BombermanMap map;
    Bomb activeBomb;
    Action subAction = null;
    HashMap<Integer, Object> newParamMap;

    int mode;

    static final int SEEKING_COVER = 1;
    static final int WAITING = 2;
    static final int RETURNING = 3;

    public void seekCover() {
        initialTile = map.getTileAt(currCharacter.kinematicInfo.getPosition());
        Random r = new Random();
        int newDir = r.nextInt(2) == 0 ? -1 : 1;

        // Pick a random turning direction (left or right)
        float newHeading = currCharacter.kinematicInfo.getOrientation() + newDir * PConstants.PI / 2;
        PVector candidateTileCoords = PVector.add(currCharacter.kinematicInfo.getPosition(), PVector.fromAngle(newHeading).mult(map.tileSize));
        Tile candidateTile = map.getTileAt(candidateTileCoords);
        if (candidateTile.ty == Tile.type.EMPTY && (activeBomb == null || map.getTileAt(activeBomb.bombPos) != candidateTile)) {
            // If tile is free, move to it
            currSeekTile = candidateTile;
            mode = WAITING;
        } else {
            // Check other direction
            newHeading = currCharacter.kinematicInfo.getOrientation() + ((-1) * newDir * PConstants.PI / 2);
            candidateTileCoords = PVector.add(currCharacter.kinematicInfo.getPosition(), PVector.fromAngle(newHeading).mult(map.tileSize));
            candidateTile = map.getTileAt(candidateTileCoords);
            if (candidateTile.ty == Tile.type.EMPTY && (activeBomb == null || map.getTileAt(activeBomb.bombPos) != candidateTile)) {
                // If tile is free, move to it
                currSeekTile = candidateTile;
                mode = WAITING;
            } else {
                // Retreat
                if (Helper.checkAngleEquality(currCharacter.kinematicInfo.getOrientation(), PredictionHelper.enemyToCollideWith.kinematicInfo.getOrientation())) {
                    newHeading = currCharacter.kinematicInfo.getOrientation();
                } else {
                    newHeading = currCharacter.kinematicInfo.getOrientation() + PConstants.PI;
                }
                candidateTileCoords = PVector.add(currCharacter.kinematicInfo.getPosition(), PVector.fromAngle(newHeading).mult(map.tileSize));
                candidateTile = map.getTileAt(candidateTileCoords);
//                if (candidateTile.ty == Tile.type.EMPTY  && (activeBomb == null || map.getTileAt(activeBomb.bombPos) != candidateTile)) {
                    currSeekTile = candidateTile;
//                } else {
                    //TODO : What to do here???
//                }
            }
        }

        pathTiles.add(currSeekTile);
        currCharacter.move(currSeekTile.posCord);
    }

    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {
        pathTiles = new ArrayList<Tile>();
        currCharacter = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);

        mode = SEEKING_COVER;
        pathTiles.add(map.getTileAt(currCharacter.kinematicInfo.getPosition()));
        activeBomb = (Bomb) paramMap.get(Const.DecisionTreeParams.BOMB_KEY);
        seekCover();
//        for (String adjTile: adjList) {
//            Tile candidateTile = map.toTile(adjTile);
//            if (candidateTile != initialTile) {
//                if (candidateTile.ty == Tile.type.EMPTY) {
//                    currCharacter.move(candidateTile.posCord);
//                }
//            }
//        }
    }

    boolean hasReachedTarget(Tile nextTile) {
        return (Math.abs(currCharacter.kinematicInfo.getPosition().x - nextTile.posCord.x) <= Const.LINEAR_EPSILON) &&
                (Math.abs(currCharacter.kinematicInfo.getPosition().y - nextTile.posCord.y) <= Const.LINEAR_EPSILON);
    }

    HashMap<Integer, Object> prepareParamMap(HashMap<Integer, Object> paramMap) {
        HashMap<Integer, Object> newParamMap = new HashMap<>();
        for (Integer currKey: paramMap.keySet()) {
            newParamMap.put(currKey, paramMap.get(currKey));
        }
        return newParamMap;
    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        activeBomb = (Bomb) paramMap.get(Const.DecisionTreeParams.BOMB_KEY);
        if (mode == SEEKING_COVER) {
            if (hasReachedTarget(currSeekTile)) {
                newParamMap = prepareParamMap(paramMap);
                newParamMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, initialTile);
                if (PredictionHelper.isCollisionPredicted(newParamMap)) {
                    // Keep seeking cover till a collision-free prediction is made
                    DebugUtil.printDebugString(" --- Seeking further cover");
                    seekCover();
                } else {
                    // When collision has been avoided, return to original position
                    mode = RETURNING;
                    Collections.reverse(pathTiles);
                }
            }
        } else if (mode == WAITING) {
            if (hasReachedTarget(currSeekTile)) {
                newParamMap = prepareParamMap(paramMap);
                newParamMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, initialTile);
//                currCharacter.sArrive.setTarget(currCharacter.kinematicInfo);
                if (!PredictionHelper.isCollisionPredicted(newParamMap)) {
                    // Do nothing till prediction is for no collision
                    mode = RETURNING;
                    Collections.reverse(pathTiles);
                }
            }
        } else {
            int currIndex = pathTiles.indexOf(currSeekTile);
            if (pathTiles.size() > currIndex + 1) {
                if ((subAction == null && hasReachedTarget(currSeekTile))) {
                    currSeekTile = pathTiles.get(currIndex + 1);
                    newParamMap = prepareParamMap(paramMap);
                    newParamMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, currSeekTile);
                    subAction = new ActMoveNextTile();
                    subAction.performAction(newParamMap);
                } else if (subAction != null) {
                    newParamMap = prepareParamMap(paramMap);
                    newParamMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, currSeekTile);
                    if (subAction.hasCompleted(newParamMap)) {
                        currSeekTile = pathTiles.get(currIndex + 1);
                        newParamMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, currSeekTile);
                        subAction = new ActMoveNextTile();
                        subAction.performAction(newParamMap);
                    }
                }
            } else {
                if (subAction == null || subAction.hasCompleted(newParamMap)) {
                    // Cover sought, and returned to original tile
                    currCharacter.sArrive.setTarget(currCharacter.kinematicInfo);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getNextTarget(HashMap<Integer, Object> paramMap) {
        if (currSeekTile == null) {
            return "";
        }

        if (subAction == null) {
            return currSeekTile.toString();
        }

        return currSeekTile.toString() + " [" + subAction.getNextTarget(paramMap) + "]";
    }
}
