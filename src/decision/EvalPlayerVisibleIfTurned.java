package decision;

import data.*;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.HashMap;

/**
 * Created by Anand on 4/15/2016.
 */
public class EvalPlayerVisibleIfTurned extends Evaluation {
    @Override
    public DTreeNode evaluate(HashMap<Integer, Object> paramMap) {
        data.Character currCharacter = (data.Character) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);
        BombermanMap map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);

        PVector newDir = PVector.fromAngle(currCharacter.kinematicInfo.getOrientation() + PConstants.PI/2);
        newDir.mult(map.tileSize);
        boolean isVisible = currCharacter.canSeeTargetInDir(player, newDir);
        if (!isVisible) {
            newDir = PVector.fromAngle(currCharacter.kinematicInfo.getOrientation() - PConstants.PI/2);
            newDir.mult(map.tileSize);
            isVisible = currCharacter.canSeeTargetInDir(player, newDir);
        }

        return isVisible ? children.get(TRUE_INDEX) : children.get(FALSE_INDEX);
    }

    public String toString() {
        return "+++ Checking to see if player is visible if turned";
    }
}
