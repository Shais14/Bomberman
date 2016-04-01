package data;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Anand on 3/31/2016.
 */
public class BombermanMap {
    public final int tileSize = 40;
    public int row, col;

    public Tile tiles[][];
    public ArrayList<String> bricks = new ArrayList<String>();

    public HashMap<String, ArrayList<String>> edges;
    public String Treasure;

    public int quantizeX(PVector coordinates) {
        return (int) (coordinates.x / tileSize);
    }

    public int quantizeY(PVector coordinates) {
        return (int) (coordinates.y / tileSize);
    }

    public void addTreasure(ArrayList<String> bricks) {
        Random r = new Random();
        int TreasurePos = r.nextInt(bricks.size());
        Treasure = bricks.get(TreasurePos);
    }

    public ArrayList<String> addBricks() {
        int x, y, count = 0;
        Random r = new Random();
        String temp;
        ArrayList<String> bricks = new ArrayList<String>();
        do {
            y = r.nextInt(row);
            x = r.nextInt(col);

//          To avoid boundary walls
            if (x == 0 || y == 0 || (x == col - 1) || y == (row - 1))
                continue;

//          To avoid the first 3 bricks
            if ((x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1))
                continue;

//          To avoid the solid bricks
            if (x % 2 == 0 && y % 2 == 0)
                continue;

            temp = String.valueOf(x) + " " + String.valueOf(y);

            if (bricks.contains(temp))
                continue;
            bricks.add(temp);
            count++;

        } while (count < 65);

        return bricks;
    }

    public static BombermanMap initializeBombermanMap(int width, int height) {
        BombermanMap bombermanMap = new BombermanMap();
        bombermanMap.initialize(width, height);
        return bombermanMap;
    }

    /*
        Note: X denotes the column number;
            Y denotes the row number in the grid.
    */
    public void initialize(int width, int height) {
        int i, j;
        String temp, adjTile;
        ArrayList<String> adjList = new ArrayList<String>();
        Tile.type type;

        row = height / tileSize;
        col = width / tileSize;
        tiles = new Tile[row][col];
        edges = new HashMap<String, ArrayList<String>>();

        bricks = addBricks();

        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                Tile tile;
                if (i == 0 || j == 0 || i == (row - 1) || j == (col - 1) || (i % 2 == 0 && j % 2 == 0)) {
                    type = Tile.type.OBSTACLE;
                    tile = new Tile(j, i, type, this);
                } else {
                    temp = String.valueOf(i) + " " + String.valueOf(j);

                    if (bricks.contains(temp)) {
                        type = Tile.type.BRICK;
                    } else {
                        type = Tile.type.EMPTY;
                    }

                    tile = new Tile(j, i, type, this);
                }

                tile.parent = this;
                tiles[i][j] = tile;
            }
        }

        //add edges
        for (i = 1; i < row - 1; i++) {
            for (j = 1; j < col - 1; j++) {
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
        }
        addTreasure(bricks);
//        System.out.println(Treasure);

//        for (i = 0; i < bricks.size(); i++) {
//            System.out.println(bricks.get(i));
//        }
    }

    public PVector getNewTileCords(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX > col || tileY > row) {
            return null;
        }

        Tile tile = tiles[tileY][tileX];
        if (tile.ty == Tile.type.EMPTY) {
            return tile.posCord;
        }

        return null;
    }

    public Tile toTile(String s) {
        int i, j;
        String[] str = s.split(" ");

        i = Integer.parseInt(str[0]);
        j = Integer.parseInt(str[1]);
        return tiles[i][j];
    }
}
