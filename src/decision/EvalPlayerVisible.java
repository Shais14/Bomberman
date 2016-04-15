package decision;

import data.*;
import data.Character;
import processing.core.PVector;

import java.util.HashMap;

/**
 * Created by Anand on 4/12/2016.
 */
public class EvalPlayerVisible extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        data.Character currCharacter = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);
        BombermanMap map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);

        PVector heading = PVector.fromAngle(currCharacter.kinematicInfo.getOrientation());
        heading.mult(map.tileSize);

        boolean isVisible = currCharacter.canSeeTargetInDir(player, heading);
        return isVisible ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);

//        boolean isVisible = false;
//        int dir = 1;
//        Tile nextTile = currTile;
//
//        if (currTile.posNum.rowIndex == playerTile.posNum.rowIndex ) {
//            // In the same row
//            dir = currTile.posNum.colIndex >= playerTile.posNum.colIndex ? -1 : 1;
//            nextTile = map.tiles[nextTile.posNum.rowIndex][nextTile.posNum.colIndex + dir];
//            isVisible = currCharacter.canSeeTargetInDir(player, PVector.sub(nextTile.posCord, currTile.posCord));
//        } else if(currTile.posNum.colIndex == playerTile.posNum.colIndex) {
//            // In the same column
//            dir = currTile.posNum.rowIndex >= playerTile.posNum.rowIndex ? -1 : 1;
//            nextTile = map.tiles[nextTile.posNum.rowIndex + dir][nextTile.posNum.colIndex];
//            isVisible =  currCharacter.canSeeTargetInDir(player, PVector.sub(nextTile.posCord, currTile.posCord));
//        }
//
//        if (isVisible) {
//            PVector newDir = PVector.sub(player.kinematicInfo.getPosition(), currCharacter.kinematicInfo.getPosition());
//            float changeInAngle = PVector.angleBetween(newDir, PVector.fromAngle(currCharacter.kinematicInfo.getOrientation()));
//
//            return changeInAngle == -1 ? children.get(FALSE_INDEX) : children.get(TRUE_INDEX);
//        }
//        return children.get(FALSE_INDEX);
    }

    public String toString() {
        return "+++ Checking to see if player is visible";
    }
}
