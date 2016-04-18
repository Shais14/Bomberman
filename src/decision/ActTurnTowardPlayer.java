package decision;

import data.*;
import data.Character;

import java.util.HashMap;

/**
 * Created by Anand on 4/12/2016.
 */
public class ActTurnTowardPlayer extends Action {
    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {
        Character currCharacter = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);

        currCharacter.turn(player.kinematicInfo.getPosition());
    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        data.Character character = (Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);

        return character.sAlign.checkOrientationReached();
    }

    @Override
    public String getNextTarget(HashMap<Integer, Object> paramMap) {
        return "";
    }

    public String toString() {
        return "--- Character turning toward player";
    }
}
