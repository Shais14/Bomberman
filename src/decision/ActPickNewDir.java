package decision;

import data.*;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Selects a new direction to move in, and orients the character in that direction
 *
 * Created by Anand on 4/2/2016.
 */
public class ActPickNewDir extends Action {
    Tile newNextTile;
    float dirOrientation;

    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {
        BombermanMap map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        Tile currTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);
        Tile nextTile = (Tile) paramMap.get(Const.DecisionTreeParams.NEXT_TILE_KEY);

        ArrayList<String> adjList = map.edges.get(currTile.toString());
        Random r = new Random();
        do {
            int nextDir = r.nextInt(adjList.size() / 2);

            String newNextTileStr = adjList.get(nextDir * 2);
            newNextTile = Tile.toTile(newNextTileStr, map);
        } while (newNextTile.ty == Tile.type.OBSTACLE || newNextTile == nextTile);

        dirOrientation = PVector.angleBetween(newNextTile.posCord, player.kinematicInfo.getPosition());
        player.turn(newNextTile.posCord);
    }

    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        PlayerInfo player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);

        return player.sAlign.checkOrientationReached();
//        return Math.abs(Helper.mapToRange(player.kinematicInfo.getOrientation() - dirOrientation)) <= Const.ANGULAR_RADIUS_SATISFACTION;
    }

    public String toString() {
        return "--- New direction to be picked";
    }
}
