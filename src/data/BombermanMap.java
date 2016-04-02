package data;

import debug.DebugUtil;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class represents the map used for a particular level in the game.
 *
 * Created by Anand on 3/31/2016.
 */
public class BombermanMap {
    public final int tileSize = 40;
    public int row, col;

    public Tile tiles[][];
    public ArrayList<String> bricks = new ArrayList<String>();

    public HashMap<String, ArrayList<String>> edges;
    public String Treasure;

    //Colors used
    public int empty;
    public int brick;
    public int obs;
    public int treasure;

    PApplet parent;

    public BombermanMap() {

    }

    public BombermanMap(PApplet parent) {
        this.parent = parent;
        empty = parent.color(125, 125, 125);
        brick = parent.color(200, 100, 0);
        obs = parent.color(100, 100, 0);
        treasure = parent.color(0, 0, 0);
    }

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

            temp = x + " " + y;

            if (bricks.contains(temp))
                continue;
            bricks.add(temp);
            count++;

        } while (count < 65);

        return bricks;
    }

    public static BombermanMap initializeBombermanMap(PApplet parent) {
        BombermanMap bombermanMap = new BombermanMap(parent);
        bombermanMap.initialize(parent.width, parent.height);
        return bombermanMap;
    }

    /*
        Note: X denotes the column number;
            Y denotes the row number in the grid.
    */
    public void initialize(int width, int height) {
        int i, j;
        String temp, adjTile;
        ArrayList<String> adjList;
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
                    tile = new Tile(i, j, type, this);
                } else {
                    temp = i + " " +  j;

                    if (bricks.contains(temp)) {
                        type = Tile.type.BRICK;
                    } else {
                        type = Tile.type.EMPTY;
                    }

                    tile = new Tile(i, j, type, this);
                }

                tile.parent = this;
                tiles[i][j] = tile;
            }
        }

        //add edges
        for (i = 1; i < row - 1; i++) {
            for (j = 1; j < col - 1; j++) {
                {
                    if (tiles[i][j].ty==Tile.type.OBSTACLE)
                        continue;

                    temp = tiles[i][j].toString();

                    adjList  = new ArrayList<String>();
                    if (i != 0 && tiles[i - 1][j].ty != Tile.type.OBSTACLE) {
                        adjTile = (i - 1) + " " + j;
                        adjList.add(adjTile);
                        adjList.add(String.valueOf(1));
                    }

                    if (j != 0 && tiles[i][j - 1].ty != Tile.type.OBSTACLE) {
                        adjTile = i + " " + (j - 1);
                        adjList.add(adjTile);
                        adjList.add(String.valueOf(1));
                    }

                    if (i != (row - 1) && tiles[i + 1][j].ty != Tile.type.OBSTACLE) {
                        adjTile =  (i + 1) + " " + j;
                        adjList.add(adjTile);
                        adjList.add(String.valueOf(1));
                    }

                    if (j != (col - 1) && tiles[i][j + 1].ty != Tile.type.OBSTACLE) {
                        adjTile = i + " " +  (j + 1);
                        adjList.add(adjTile);
                        adjList.add(String.valueOf(1));
                    }

                    edges.put(temp, adjList);
                }
            }
        }
//        DebugUtil.printEdges(this);
        addTreasure(bricks);
//        System.out.println(Treasure);

//        for (i = 0; i < bricks.size(); i++) {
//            System.out.println(bricks.get(i));
//        }


        setSignalStrength();


    }

    private void setSignalStrength() {

        System.out.println(Treasure + " Treasure");
        Tile Trea = toTile(Treasure);

        int flag[][] = new int[15][15];

        int signalStrengthInner;
        int signalStrengthOuter;

        int rk = Trea.posNum.rowIndex;
        int ck = Trea.posNum.colIndex;

        int d = 1;

        switch(d) {
            case 0: {
                signalStrengthInner = 50;

                for (int k = 1; k <= 5; k++) {

                    int level = k;
                    int lextreme = rk - level;
                    if (lextreme < 1) lextreme = 1;
                    int rextreme = rk + level;
                    if (rextreme > 13) rextreme = 13;
                    int textreme = ck - level;
                    if (textreme < 1) textreme = 1;
                    int bextreme = ck + level;
                    if (bextreme > 13) bextreme = 13;

                    for (int r = lextreme; r <= rextreme; r++) {
                        for (int c = textreme; c <= bextreme; c++) {
                            if (r == rk && c == ck) {
                                tiles[r][c].signal = 100;
                            }
                            if (flag[r][c] == 0) {
                                tiles[r][c].signal = signalStrengthInner;
                                flag[r][c] = 1;
                            }
                        }
                    }
                    signalStrengthInner -= 10;
                }
            }

            case 1:
            {
                signalStrengthInner = 50;
                signalStrengthOuter = 15;
                        for (int k = 1; k <= 14; k++) {

                            int level = k;
                            int lextreme = rk - level;
                            if (lextreme < 1) lextreme = 1;
                            int rextreme = rk + level;
                            if (rextreme > 13) rextreme = 13;
                            int textreme = ck - level;
                            if (textreme < 1) textreme = 1;
                            int bextreme = ck + level;
                            if (bextreme > 13) bextreme = 13;

                            for (int r = lextreme; r <= rextreme; r++) {
                                for (int c = textreme; c <= bextreme; c++) {

                                    if(k<=5) {
                                        if (r == rk && c == ck) {
                                            tiles[r][c].signal = 100;
                                        }
                                        if (flag[r][c] == 0) {
                                            tiles[r][c].signal = signalStrengthInner;
                                            flag[r][c] = 1;
                                        }
                                    }
                                    else{
                                        if(flag[r][c] == 0) {
                                            tiles[r][c].signal = signalStrengthOuter;
                                            flag[r][c] = 1;
                                        }
                                    }
                                }
                          }

                                if(k>5) {      signalStrengthOuter--;}

                        }








           }
        }
        DebugUtil.printSignalStrength(this);

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
        return PosNum.toTile(s, this);
    }

    public void draw() {
        int i, j;
        data.Tile tile;
        parent.rectMode(PApplet.CENTER);

        parent.fill(treasure);
        int temp[] = new int[2];
        int space = Treasure.indexOf(' ');
        temp[0] = Integer.parseInt(Treasure.substring(0, space));
        temp[1] = Integer.parseInt(Treasure.substring(space + 1,
                Treasure.length()));


        tile = tiles[temp[0]][temp[1]];
        parent.rect(tile.posCord.x, tile.posCord.y, tileSize, tileSize);

        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                tile = tiles[i][j];
                if (tile.ty == Tile.type.OBSTACLE) {
                    parent.fill(obs);
                    parent.stroke(0);
                    parent.strokeWeight(2);
                } else if (tile.ty == Tile.type.BRICK) {
                    parent.stroke(0);
                    parent.fill(brick);
                } else {
                    parent.fill(empty);
                    parent.noStroke();
                }
                parent.rect(tile.posCord.x, tile.posCord.y, tileSize, tileSize);
            }
        }
    }
}
