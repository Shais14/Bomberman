package decision;

import data.*;

import java.util.HashMap;

import static sun.audio.AudioPlayer.player;

/**
 * Plants a bomb at the current tile, and moves to an unexplored
 * tile outside of the bomb's blast radius
 *
 * Created by Anand on 4/2/2016.
 */
public class ActPlantAndMoveUnexplored extends Action {
    BombermanMap map;
    PlayerInfo player;

    @Override
    public void performAction(HashMap<Integer, Object> paramMap) {

        map = (BombermanMap) paramMap.get(Const.DecisionTreeParams.GRAPH_KEY);
        player = (PlayerInfo) paramMap.get(Const.DecisionTreeParams.CURR_CHAR_KEY);
        Tile brick = findBrick();

    }

    public Tile findBrick() {
        int i, j = 0, level = 1;
        String str;
        boolean flag = true;

        Tile tile;
        int currPosX = map.quantizeX(player.kinematicInfo.getPosition());
        int currPosY = map.quantizeY(player.kinematicInfo.getPosition());


        do {

            for (i = currPosX - level; flag && i <= currPosX + level; i++) {
                if (i < 0 || i >= map.row)
                    continue;
                for (j = currPosY - level; j <= currPosY + level; j++) {
                    if (j > 0 && j < map.col && map.tiles[i][j].ty == Tile.type.BRICK) {
                        flag = false;
                        break;
                    }
                }

            }
            level++;

        } while (flag);

        str = i + " " + j;
        tile = Tile.toTile(str, map);
        return tile;

    }


    @Override
    public boolean hasCompleted(HashMap<Integer, Object> paramMap) {
        return false;
    }
}
