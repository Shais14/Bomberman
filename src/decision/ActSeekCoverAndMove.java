package decision;

import data.BombermanMap;
import data.Character;
import data.Const;
import data.Tile;
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
    ArrayList<Tile> pathTiles;
    data.Character currCharacter;
    BombermanMap map;

    int mode;

    static final int SEEKING_COVER = 1;
    static final int WAITING = 2;
    static final int RETURNING = 3;

    public void seekCover() {
        Random r = new Random();
        int newDir = r.nextInt(2) == 0 ? -1 : 1;

        // Pick a random turning direction (left or right)
        float newHeading = currCharacter.kinematicInfo.getOrientation() + newDir * PConstants.PI / 2;
        PVector candidateTileCoords = PVector.add(currCharacter.kinematicInfo.getPosition(), PVector.fromAngle(newHeading).mult(map.tileSize));
        Tile candidateTile = map.getTileAt(candidateTileCoords);
        if (candidateTile.ty == Tile.type.EMPTY) {
            // If tile is free, move to it
            currSeekTile = candidateTile;
            mode = WAITING;
        } else {
            // Check other direction
            newHeading = currCharacter.kinematicInfo.getOrientation() + ((-1) * newDir * PConstants.PI / 2);
            candidateTileCoords = PVector.add(currCharacter.kinematicInfo.getPosition(), PVector.fromAngle(newHeading).mult(map.tileSize));
            candidateTile = map.getTileAt(candidateTileCoords);
            if (candidateTile.ty == Tile.type.EMPTY) {
                // If tile is free, move to it
                currSeekTile = candidateTile;
                mode = WAITING;
            } else {
                // Retreat
                newHeading = currCharacter.kinematicInfo.getOrientation() + PConstants.PI;
                candidateTileCoords = PVector.add(currCharacter.kinematicInfo.getPosition(), PVector.fromAngle(newHeading).mult(map.tileSize));
                candidateTile = map.getTileAt(candidateTileCoords);
                currSeekTile = candidateTile;
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
        seekCover();
//        for (String adjTile: adjList) {
//            //TODO: Candidates should follow a specific priority (L/R then backward)
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

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        if (mode == SEEKING_COVER) {
            if (hasReachedTarget(currSeekTile)) {
                if (PredictionHelper.isCollisionPredicted(paramMap)) {
                    // Keep seeking cover till a collision-free prediction is made
                    seekCover();
                } else {
                    // When collision has been avoided, return to original position
                    mode = RETURNING;
                    Collections.reverse(pathTiles);
                }
            }
        } else if (mode == WAITING) {
            if (hasReachedTarget(currSeekTile)) {
                currCharacter.sArrive.setTarget(currCharacter.kinematicInfo);
                if (!PredictionHelper.isCollisionPredicted(paramMap)) {
                    // Do nothing till prediction is for no collision
                    mode = RETURNING;
                    Collections.reverse(pathTiles);
                }
            }
        } else {
            if (hasReachedTarget(currSeekTile)) {
                int currIndex = pathTiles.indexOf(currSeekTile);
                if (pathTiles.size() > currIndex + 1) {
                    // Some tiles are remaining
                    currSeekTile = pathTiles.get(currIndex + 1);
                    // Future scope : recursively call ActMoveToNextTile
                    currCharacter.move(currSeekTile.posCord);
                } else {
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

        return currSeekTile.toString();
    }
}
