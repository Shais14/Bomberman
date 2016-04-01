///**
// * Created by jeevan on 31-Mar-16.
// */
//import processing.core.*;
//public class Tile
//{
//    PVector posNum;
//    PVector posCord;
//    Tile.type ty;
//    public enum type {
//        EMPTY(255), BRICK(100), OBSTACLE(200);
//        private int value;
//        private type(int v)
//        {
//            value = v;
//        }
//        public int getValue()
//        {
//            return value;
//        }
//    }
//
//    public Tile(int x, int y, type e)
//    {
//        posNum = new PVector(x, y);
//        posCord = localize(posNum);
//        ty = e;
//
//    }
//
//    public PVector localize(PVector v)
//    {
//        float x = (float) (v.x + 0.5) * Graph.tileSize;
//        float y = (float) (v.y + 0.5) * Graph.tileSize;
//        return new PVector(x, y);
//    }
//
//    public PVector quantize(PVector pos)
//    {
//        int x = (int) Math.floor(pos.x/Graph.tileSize);
//        int y = (int) Math.floor(pos.y/Graph.tileSize);
//        return new PVector(x, y);
//    }
//
//    public String toString() {
//        return posNum.x + " " + posNum.y;
//    }
//
//    public static Tile toTile(String s)
//    {
//        int i, j;
//        String[] str = s.split(" ");
//
//        i = Integer.parseInt(str[0]);
//        j = Integer.parseInt(str[1]);
//        return Graph.tiles[i][j];
//    }
//}
//
