/**
 * Created by jeevan on 29-Mar-16.
 */
import processing.core.*;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Graph extends PApplet {
    public static final int tileSize = 40;
    public static int row, col;

    public final int empty = color(125, 125, 125);
    public final int brick = color(200, 100, 0);
    public final int obs = color(100, 100, 0);

    public static Tile tiles[][];
    public static ArrayList<String> bricks = new ArrayList<String>();

    public static HashMap<String, ArrayList<String>> edges;
    public void settings() {
        size(600, 600);
    }

    public void setup() {
        background(155);

        int i, j;
        String temp, adjTile;
        ArrayList<String> adjList  = new ArrayList<String>();
        Tile.type type;

        row = height/tileSize;
        col = width/tileSize;
        tiles = new Tile[row][col];
        edges  = new HashMap<String, ArrayList<String>>();

        bricks = addBricks();

        for (i = 0; i<row; i++)
            for (j = 0; j<col; j++)
            {
                if ( i==0 || j==0 || i == (row-1) || j == (col-1) || (i%2 == 0 && j%2 == 0)) {
                    type = Tile.type.OBSTACLE;
                    tiles[i][j] = new Tile(i, j, type);
                    continue;
                }

                temp = String.valueOf(i) + " " + String.valueOf(j);
                if(bricks.contains(temp))
                   type = Tile.type.BRICK;
                else
                    type = Tile.type.EMPTY;

                tiles[i][j] = new Tile(i, j, type);
            }

//add edges
        for (i = 1; i<row-1; i++)
            for (j = 1; j<col-1; j++)
            {
                temp = i + " " + j;

                adjList.clear();
                if (i != 0 && tiles[i - 1][j].ty == Tile.type.EMPTY) {
                    adjTile = String.valueOf(i - 1) + " " + String.valueOf(j);
                    adjList.add(adjTile);
                    adjList.add(String.valueOf(1));
                }

                if (j != 0 && tiles[i][j - 1].ty == Tile.type.EMPTY) {
                    adjTile = String.valueOf(i) + " " + String.valueOf(j - 1);
                    adjList.add(adjTile);
                    adjList.add(String.valueOf(1));
                }

                if (i != (row - 1) && tiles[i + 1][j].ty == Tile.type.EMPTY) {
                    adjTile = String.valueOf(i + 1) + " " + String.valueOf(j);
                    adjList.add(adjTile);
                    adjList.add(String.valueOf(1));
                }

                if (j != (col - 1) && tiles[i][j + 1].ty == Tile.type.EMPTY) {
                    adjTile = String.valueOf(i) + " " + String.valueOf(j + 1);
                    adjList.add(adjTile);
                    adjList.add(String.valueOf(1));
                }

                edges.put(temp, adjList);
            }


    }

    public ArrayList<String> addBricks()
    {
        int x, y, count = 0;
        Random r = new Random();
        String temp;
        ArrayList<String> bricks = new ArrayList<String>();
        do
        {
            x = r.nextInt(row);
            y = r.nextInt(col);

            if( ( x == 1 && y == 1 )|| (x == 1 && y == 2) || ( x == 2 && y == 1 )  )
                continue;

            if ( x%2 == 0 && y%2 == 0)
                continue;

            temp = String.valueOf(x) + " " +  String.valueOf(y);

            if (bricks.contains(temp))
                continue;
            bricks.add(temp);
            count++;

        }while(count<65);

        return bricks;
    }


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
                } else if(tile.ty == Tile.type.BRICK){
                    stroke(0);
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
