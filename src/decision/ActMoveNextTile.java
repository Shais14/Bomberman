package decision;

import data.*;
import data.Character;
import debug.DebugUtil;
import movement.PredictionHelper;

import java.util.HashMap;

/**
 * Moves to the next tile
 *
 * Created by Anand on 4/2/2016.
 */
public class ActMoveNextTile extends Action {
    Action subAction = null;
    Tile targetTile;

    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {
        Character character = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        targetTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        if (PredictionHelper.isCollisionPredicted(paramMap)) {
            subAction = new ActSeekCoverAndMove();
            subAction.performAction(paramMap);
            DebugUtil.printDebugString("--- Seeking cover from enemy");
        } else {
            character.move(targetTile.posCord);
        }
    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        Character character = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        if (subAction == null) {
            if ((Math.abs(character.kinematicInfo.getPosition().x - nextTile.posCord.x) <= Const.LINEAR_EPSILON) &&
                    (Math.abs(character.kinematicInfo.getPosition().y - nextTile.posCord.y) <= Const.LINEAR_EPSILON)) {
                character.sArrive.setTarget(character.kinematicInfo);
                return true;
            } else {
                return false;
            }
        } else {
            if (subAction.hasCompleted(paramMap)) {
                subAction = null;
                character.move(targetTile.posCord);
                DebugUtil.printDebugString("--- Enemy avoided, returning to original mission");
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

        return targetTile.toString() + " [" + subAction.getNextTarget(paramMap) + "]";
    }

    public String toString() {
        return "--- Moving to next tile";
    }
}
