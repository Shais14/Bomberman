package decision;

import data.*;
import data.Character;

import java.util.HashMap;

/**
 * Moves to the next tile
 *
 * Created by Anand on 4/2/2016.
 */
public class ActMoveNextTile extends Action {
    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {
        Character character = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        character.move(nextTile.posCord);
    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        Character character = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        if ((Math.abs(character.kinematicInfo.getPosition().x - nextTile.posCord.x) <= Const.LINEAR_EPSILON) &&
                (Math.abs(character.kinematicInfo.getPosition().y - nextTile.posCord.y) <= Const.LINEAR_EPSILON)) {
            character.sArrive.setTarget(character.kinematicInfo);
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return "--- Moving to next tile";
    }
}
