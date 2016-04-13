package decision;

import data.*;
import data.Character;
import processing.core.PVector;

import java.util.HashMap;

/**
 * Created by Anand on 4/12/2016.
 */
public class EvalPlayerVisible extends Evaluation {
    BombermanMap map;
    Tile currTile;
    Tile playerTile;

    public boolean canSeePlayerInDir(PVector dir) {
        Tile nextTile = currTile;
        while (nextTile != null) {
            if (nextTile.ty != Tile.type.EMPTY) {
                return false;
            }
            if (nextTile == playerTile) {
                return true;
            }

            PVector nextTileCord = PVector.add(nextTile.posCord, dir);
            nextTile = map.getTileAt(nextTileCord);
        }

        // Will never reach here
        return false;
    }

    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        data.Character currCharacter = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);
        map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);

        currTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);
        playerTile = map.getTileAt(player.kinematicInfo.getPosition());

        boolean isVisible = false;
        int dir = 1;
        Tile nextTile = currTile;

        if (currTile.posNum.rowIndex == playerTile.posNum.rowIndex ) {
            // In the same row
            dir = currTile.posNum.colIndex >= playerTile.posNum.colIndex ? -1 : 1;
            nextTile = map.tiles[nextTile.posNum.rowIndex][nextTile.posNum.colIndex + dir];
            isVisible = canSeePlayerInDir(PVector.sub(nextTile.posCord, currTile.posCord));
        } else if(currTile.posNum.colIndex == playerTile.posNum.colIndex) {
            // In the same column
            dir = currTile.posNum.rowIndex >= playerTile.posNum.rowIndex ? -1 : 1;
            nextTile = map.tiles[nextTile.posNum.rowIndex + dir][nextTile.posNum.colIndex];
            isVisible = canSeePlayerInDir(PVector.sub(nextTile.posCord, currTile.posCord));
        }

        if (isVisible) {
            PVector newDir = PVector.sub(player.kinematicInfo.getPosition(), currCharacter.kinematicInfo.getPosition());
            float changeInAngle = PVector.angleBetween(newDir, PVector.fromAngle(currCharacter.kinematicInfo.getOrientation()));

            return changeInAngle == -1 ? children.get(FALSE_INDEX) : children.get(TRUE_INDEX);
        }
        return children.get(FALSE_INDEX);
    }

    public String toString() {
        return "+++ Checking to see if player is visible";
    }
}
