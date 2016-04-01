package data; /**
 * Created by jeevan on 31-Mar-16.
 */

import processing.core.PVector;

public class Tile {
    public PosNum posNum;
    public PVector posCord;
    public Tile.type ty;
    public BombermanMap parent;
    public int signal;

    public enum type {
        EMPTY(255), BRICK(100), OBSTACLE(200);
        private int value;

        private type(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }
    }

    public Tile(int x, int y, type e, BombermanMap parent) {
        posNum = new PosNum(x, y);
        this.parent = parent;
        posCord = localize(posNum);
        ty = e;
        signal = 5;

    }

    public PVector localize(PosNum v) {
        float x = (float) (v.colIndex + 0.5) * parent.tileSize;
        float y = (float) (v.rowIndex + 0.5) * parent.tileSize;
        return new PVector(x, y);
    }

    @Deprecated
    public PVector quantize(PVector pos) {
        int x = (int) Math.floor(pos.x / parent.tileSize);
        int y = (int) Math.floor(pos.y / parent.tileSize);
        return new PVector(x, y);
    }

    public String toString() {
        return posNum.toString();
    }

    public static Tile toTile(String s, BombermanMap bombermanMap) {
        return bombermanMap.toTile(s);
    }
}

