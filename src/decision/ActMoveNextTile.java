package decision;

import data.Const;
import data.PlayerInfo;
import data.Tile;

import java.util.HashMap;

/**
 * Moves to the next tile
 *
 * Created by Anand on 4/2/2016.
 */
public class ActMoveNextTile extends Action {
    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        player.move(nextTile.posCord);
    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.PLAYER_KEY);
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        return (player.kinematicInfo.getPosition().x - nextTile.posCord.x <= Const.LINEAR_EPSILON) &&
                (player.kinematicInfo.getPosition().y - nextTile.posCord.y <= Const.LINEAR_EPSILON);
    }

    public String toString() {
        return "--- Moving to next tile";
    }
}
