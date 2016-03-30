/**
 * Created by jeevan on 29-Mar-16.
 */
import processing.core.*;

import java.util.ArrayList;
import java.util.HashSet;

class Tile
{
    PVector posNum;
    PVector posCord;
    Tile.type ty;
    public enum type {
        EMPTY(255), BRICK(100), OBSTACLE(200);
        private int value;
        private type(int v)
        {
            value = v;
        }
        public int getValue()
        {
            return value;
        }
    }

    public Tile(int x, int y, type e)
    {
        posNum = new PVector(x, y);
        posCord = localize(posNum);
        ty = e;

    }

    public PVector localize(PVector v)
    {
        float x = (float) (v.x + 0.5) * Graph.tileSize;
        float y = (float) (v.y + 0.5) * Graph.tileSize;
        return new PVector(x, y);
    }

    public PVector quantize(PVector pos)
    {
        int x = (int) Math.floor(pos.x/Graph.tileSize);
        int y = (int) Math.floor(pos.y/Graph.tileSize);
        return new PVector(x, y);
    }

}
public class Graph extends PApplet {
    public static final int tileSize = 40;
    public static int row, col;

    public final int empty = color(125, 125, 125);
    public final int brick = color(255, 100, 0);
    public final int obs = color(100, 100, 0);

    Tile tiles[][];
    public void settings() {
        size(600, 600);
    }

    public void setup() {
        int i, j;
//        ArrayList<Integer,Integer> bricks = new ArrayList<Integer,Integer>();
        Tile.type type;
        background(155);
        row = height/tileSize;
        col = width/tileSize;
        tiles = new Tile[row][col];
//        bricks = getBricks();

        for (i = 0; i<row; i++)
            for (j = 0; j<col; j++)
            {
                if ( i==0 || j==0 || i == (row-1) || j == (col-1) || (i%2 == 0 && j%2 == 0))
                    type = Tile.type.OBSTACLE;
                else
                    type = Tile.type.EMPTY;
                tiles[i][j] = new Tile(i, j, type);
            }

    }

//    public ArrayList getBricks()
//    {
//        int i, j;
//        HashSet<> brick = new ArrayList<>();
//        for (i = 0; i<65; i++)
//        {
//
//        }
//    }

    public void draw() {
        int i, j;
        Tile tile;
        rectMode(CENTER);
        for (i = 0; i<row; i++) {
            for (j = 0; j < col; j++) {
                tile = tiles[i][j];
                if (tile.ty == Tile.type.OBSTACLE) {
                    fill(obs);
                    stroke(0);
                    strokeWeight(2);
                } else if(tile.ty == Tile.type.OBSTACLE){
                    fill(brick);
                }
                else {
                    fill(empty);
                    noStroke();
                }
                rect(tile.posCord.x, tile.posCord.y, tileSize, tileSize);
            }
        }


    }

    public static void main(String[] args) {
        PApplet.main(new String[] {"--present","Graph"});}
}
