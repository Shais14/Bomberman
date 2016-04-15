package data;

import debug.DebugUtil;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;

/**
 * This class represents the map used for a particular level in the game.
 *
 * Created by Anand on 3/31/2016.
 */
public class BombermanMap {
    public final int tileSize = 40;
    public int row, col;

    public Tile tiles[][];
    public ArrayList<String> bricks;
//    public ArrayList<String> originalBricks;

    public HashMap<String, ArrayList<String>> edges;
    public String Treasure;


    int variationDistance = 5;

    //Colors used
    public int empty;
    public int brick;
    public int obs;
    public int treasure;

    public PApplet parent;

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
        int TreasurePos;
        Tile tempTreasureTile;
        do {
            TreasurePos = r.nextInt(bricks.size());
            Treasure = bricks.get(TreasurePos);
            tempTreasureTile = Tile.toTile(Treasure, this);
        }while(tempTreasureTile.posNum.rowIndex <= 8 && tempTreasureTile.posNum.colIndex <= 8 );
    }

    public boolean isTreaureVisible()
    {
        return Tile.toTile(Treasure, this).ty==Tile.type.EMPTY;
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

//          To avoid the bottom right corner (enemy spawn tile)
            if (x == (col - 2) && y == (row - 2) || x == (col - 2) && y == (row - 3) || x == (col - 3) && y == (row - 2))
                continue;

//          To avoid the top right corner (enemy spawn tile)
            if (x == (col - 2) && y == 1 || x == (col - 3) && y == 1 || x == (col - 2) && y == 2)
                continue;

//          To avoid the bottom left corner (enemy spawn tile)
            if (x == 1 && y == (row - 2) || x == 1 && y == (row - 3) || x == 2 && y == (row - 2))
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
        bombermanMap.initialize(parent.width, parent.height, true);
        return bombermanMap;
    }

    public static BombermanMap initializeBombermanMap(PApplet parent, String mapConfigFilePath) {
        BombermanMap bombermanMap = new BombermanMap(parent);
        ArrayList<String> mapConfig = BombermanMapParser.parseMapConfig(mapConfigFilePath);

        bombermanMap.Treasure = mapConfig.get(0);
        bombermanMap.bricks = new ArrayList<>();
        for (int i = 1; i < mapConfig.size(); i++) {
            bombermanMap.bricks.add(mapConfig.get(i));
        }

        bombermanMap.initialize(parent.width, parent.height, false);
        return bombermanMap;
    }

    public void reinitialize() {
        if (parent != null) {
            initialize(parent.width, parent.height, false);
        }
    }

    /*
        Note: X denotes the column number;
            Y denotes the row number in the grid.
    */
    public void initialize(int width, int height, boolean generateNewMap) {
        int i, j;
        String temp, adjTile;
        ArrayList<String> adjList;
        Tile.type type;

        row = height / tileSize;
        col = width / tileSize;
        tiles = new Tile[row][col];
        edges = new HashMap<String, ArrayList<String>>();

        if (generateNewMap || bricks == null || bricks.size() == 0) {
            bricks = addBricks();
        }

//        bricks = new ArrayList<String>(originalBricks.size());
//        for (String currBrick: originalBricks) {
//            bricks.add(currBrick);
//        }
//        Collections.copy(bricks, originalBricks);

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
        if (generateNewMap) {
            addTreasure(bricks);
            System.out.println("****************** Map Config stored in - " + DebugUtil.saveMapConfig(this) + " ******************");
        }
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

        int d = 0;

        switch(d) {
            case 0: {
                signalStrengthInner = 50;

                for (int k = 1; k <= variationDistance; k++) {

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
            break;
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

                                if(k>variationDistance) {      signalStrengthOuter--;}
                        }
           }
            break;
            case 2:
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        Tile currTile = tiles[i][j];
                        currTile.signal = 0;
                    }
                }
        }
        DebugUtil.printSignalStrength(this);

    }

    public Tile getTileAt(PVector cords) {
        int tileX = quantizeX(cords);
        int tileY = quantizeY(cords);

        if (tileX >= col || tileY >= row || tileX < 0 || tileY < 0) {
            return null;
        }
        return tiles[tileY][tileX];
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

    public float getGERatio(Tile currTile, Tile nextTile) {
        float effort = 0;
        switch (nextTile.ty) {
            case BRICK:
                effort = 5;
                break;
            case EMPTY:
                effort = 1;
                break;
            case OBSTACLE:
                effort = Float.MAX_VALUE;
                break;
        }

        float signalGain = nextTile.signal - currTile.signal;

        return signalGain/effort;
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

    public void drawSignal(){

        Tile Trea = toTile(Treasure);


        int flag[][] = new int[15][15];

        int rk = Trea.posNum.rowIndex;
        int ck = Trea.posNum.colIndex;

        for (int k = 1; k <= variationDistance; k++) {

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
                    if (flag[r][c] == 0) {

                        int transparency = tiles[r][c].signal ;
                        parent.noStroke();
                        parent.fill(0, 0, 255, transparency);
                        parent.rect(tiles[r][c].posCord.x, tiles[r][c].posCord.y, tileSize, tileSize);
                        flag[r][c] = 1;
                    }
                }
            }
        }
    }

    public ArrayList<Tile> getPathToCover(PVector characterPos){
        Tile currTile = getTileAt(characterPos);

        //rc and cc is the character's current row and col index.
        int rc = currTile.posNum.rowIndex;
        int cc = currTile.posNum.colIndex;
        String current = rc+ " " + cc;
        ArrayList<String> processedList1 = new ArrayList<String>();
        ArrayList<String> processedList2 = new ArrayList<String>();
        ArrayList<Tile> Targets = new ArrayList<Tile>();

        while(processedList1.size() != 4 && Targets.size() != 2) {
            int x = (int) parent.random(1, 5);

            int nextTile[] = findTile(x, rc, cc);

            int r1 = nextTile[0];
            int c1 = nextTile[1];

            String adjacent = r1 + " " + c1;

            Tile adjacentTile = toTile(adjacent);

            if (processedList1.contains(adjacent)) {
                continue;
            }

            if (adjacentTile.ty == Tile.type.OBSTACLE || adjacentTile.ty == Tile.type.BRICK) {
                processedList1.add(adjacent);
            }
            else{
                processedList2.add(current);

                while(processedList2.size() != 4 && Targets.size() != 2 ) {
                    int y = (int) parent.random(1, 5);

                    int finalTile[] = findTile(y, r1, c1);

                    int r2 = finalTile[0];
                    int c2 = finalTile[1];

                    String target = r2 + " " + c2;
                    Tile targetTile = toTile(target);

                    if (processedList2.contains(target)) {
                        continue;
                    }

                    if (targetTile.ty == Tile.type.OBSTACLE || targetTile.ty == Tile.type.BRICK) {
                        processedList2.add(target);

                    } else {
                        Targets.add(adjacentTile);
                        Targets.add(targetTile);
                        return Targets;
                    }
                }
                if(processedList2.size() == 4)
                    processedList1.add(current);

            }
        }

        if(processedList1.size() == 4) {
            System.out.println("die");
        }
        return null;
//        System.out.println(" Target " +target);

    }

    public int[] findTile(int x, int r, int c) {

        int nextTile[] = new int[2];
        switch (x) {
            case 1:
                nextTile[0] = r - 1;
                nextTile[1] = c;
                break;
            case 2:
                nextTile[0] = r;
                nextTile[1] = c + 1;
                break;
            case 3:
                nextTile[0] = r + 1;
                nextTile[1] = c;
                break;
            case 4:
                nextTile[0] = r;
                nextTile[1] = c - 1;
                break;

        }
        return nextTile;
    }
}



