//package decision;
//
//import data.Bomb;
//import data.BombermanMap;
//import data.PlayerInfo;
//import data.Tile;
//import processing.core.PApplet;
//
///**
// * Created by jeevan on 02-Apr-16.
// */
//public class Explore {
//
//    public BombermanMap map;
//    public PApplet parent;
//    public PlayerInfo player;
//
//    public Explore(PlayerInfo player, BombermanMap map, PApplet parent) {
//        this.player = player;
//        this.map = map;
//        this.parent = parent;
//    }
//
//    public void nextAction(BombermanMap bomb) {
//        Tile target;
//
//        if (map.isTreaureVisible())
//            target = Tile.toTile(map.Treasure, map);
//        else if (bomb != null)
//            ;
////            move 2 steps;
//        else
//            target = findBrick();
//
//
//    }
//
//    public Tile findBrick() {
//        int i, j, level = 1;
//        String str;
//        boolean flag = true;
//
//        Tile tile;
//        int currPosX = map.quantizeX(player.kinematicInfo.getPosition());
//        int currPosY = map.quantizeY(player.kinematicInfo.getPosition());
//
//
//        do {
//
//            for (i = currPosX - level; flag && i <= currPosX + level; i++) {
//                if (i < 0 || i >= map.row)
//                    continue;
//                for (j = currPosY - level; j <= currPosY + level; j++) {
//                    if (j > 0 && j < map.col && map.tiles[i][j].ty == Tile.type.BRICK) {
//                        flag = false;
//                        break;
//                    }
//                }
//
//            }
//            level++;
//
//        } while (flag);
//
//        str = i + " " + j;
//        tile = Tile.toTile(str, map);
//        return tile;
//
//    }
//
//}
