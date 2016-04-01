/**
 * Created by jeevan on 29-Mar-16.
 */

import data.Const;
import data.KinematicInfo;
import data.PlayerInfo;
import data.SteeringInfo;
import movement.Align;
import movement.Arrive;
import processing.core.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Graph extends PApplet {
    public static final int tileSize = 40;
    public static int row, col;

    public final int empty = color(125, 125, 125);
    public final int brick = color(200, 100, 0);
    public final int obs = color(100, 100, 0);
    public final int treasure = color(0, 0, 0);

    public static Tile tiles[][];
    public static ArrayList<String> bricks = new ArrayList<String>();

    public static HashMap<String, ArrayList<String>> edges;
    public String Treasure;

    PShape mainShape, circleShape, orientShape, crumbShape;
    long lastAIComputeTime, lastCrumbDrawTime;
    PlayerInfo player;
    Arrive sArrive;
    Align sAlign;
    SteeringInfo steering;
    ArrayList<PVector> breadcrumbs;

    public void initializePlayer() {
        PVector initialPos = tiles[1][1].posCord.copy();

        player = new PlayerInfo();
        player.setPosition(initialPos);
        player.setOrientation(0);
        player.setMaxSpeed(Const.MAX_SPEED);

        KinematicInfo target = new KinematicInfo();
        target.setPosition(initialPos);
        target.setOrientation(0);

        sArrive = new Arrive();
        sArrive.setMaxAcceleration(Const.MAX_LINEAR_ACCELERATION);
        sArrive.setPlayer(player);
        sArrive.setTarget(target);
        sArrive.setMaxSpeed(Const.MAX_SPEED);
        sArrive.setRadiusOfDeceleration(Const.LINEAR_RADIUS_DECELERATION);
        sArrive.setRadiusOfSatisfaction(Const.LINEAR_RADIUS_SATISFACTION);
        sArrive.setTimeToTarget(Const.TIME_TARGET_VELOCITY);

        sAlign = new Align();
        sAlign.setMaxRotation(Const.MAX_ROTATION);
        sAlign.setMaxAngularAcceleration(Const.MAX_ANGULAR_ACCELERATION);
        sAlign.setRadiusOfSatisfaction(Const.ANGULAR_RADIUS_SATISFACTION);
        sAlign.setRadiusOfDeceleration(Const.ANGULAR_RADIUS_DECELERATION);
        sAlign.setTimeToTarget(Const.TIME_TARGET_ROTATION);
        sAlign.setPlayer(player);
        sAlign.setTarget(target);

        steering = new SteeringInfo();

        circleShape = createShape(ELLIPSE, 0, 0, 20, 20);
        circleShape.setFill(color(0));

        orientShape = createShape(TRIANGLE, 0, -10, 0, 10, 15, 0);
        orientShape.setFill(color(0));

        mainShape = createShape(GROUP);
        mainShape.addChild(circleShape);
        mainShape.addChild(orientShape);

        crumbShape = createShape(ELLIPSE, 0, 0, 2, 2);
        crumbShape.setFill(color(64));
        breadcrumbs = new ArrayList<PVector>();

        lastAIComputeTime = System.currentTimeMillis();
        lastCrumbDrawTime = System.currentTimeMillis();
    }

    public void drawPlayer() {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastAIComputeTime > Const.AI_COMPUTE_TIME)) {
            computeAI();
            lastAIComputeTime = currentTime;
        }

        if ((currentTime - lastCrumbDrawTime > Const.CRUMB_DRAW_TIME)) {
            updateCrumbs();
            lastCrumbDrawTime = currentTime;
        }

        if (sAlign.checkOrientationReached()) {
            player.setRotation(0);
            steering.setAngular(0);
        }

        player.update(steering, currentTime);

        drawCrumbs();

        pushMatrix();
        translate(player.getPosition().x, player.getPosition().y);
        rotate(player.getOrientation());
        shape(mainShape);
        popMatrix();
    }

    void updateCrumbs() {
        if (breadcrumbs.size() >= Const.BREADCRUMB_COUNT) {
            breadcrumbs.remove(0);
        }

        breadcrumbs.add(player.getPosition().copy());
    }

    void drawCrumbs() {
        for (PVector crumb: breadcrumbs) {
            pushMatrix();
            translate(crumb.x, crumb.y);
            shape(crumbShape);
            popMatrix();
        }
    }

    void computeAI() {
        steering = sArrive.getSteering();
        sAlign.getSteering(steering);
    }

    public void settings() {
        size(600, 600);
    }

    /*
    Note: X denotes the coloumn number;
          Y denotes the row number in the grid.
    */

    public static int quantizeX(PVector coordinates) {
        return (int) (coordinates.x / tileSize);
    }

    public static int quantizeY(PVector coordinates) {
        return (int) (coordinates.y / tileSize);
    }

    public void setup() {
        background(155);

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
                if (i == 0 || j == 0 || i == (row - 1) || j == (col - 1) || (i % 2 == 0 && j % 2 == 0)) {
                    type = Tile.type.OBSTACLE;
                    tiles[i][j] = new Tile(j, i, type);
                    continue;
                }

                temp = String.valueOf(i) + " " + String.valueOf(j);
                if (bricks.contains(temp)) {
                    type = Tile.type.BRICK;
                } else
                    type = Tile.type.EMPTY;

                tiles[i][j] = new Tile(j, i, type);
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
        System.out.println(Treasure);

        for(i = 0; i< bricks.size(); i++){
            System.out.println(bricks.get(i));
        }

        initializePlayer();
        ArrayList<String> path = Astar.pathAstar(bricks.get(1), bricks.get(5), "E");
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
        do
        {
            y = r.nextInt(row);
            x = r.nextInt(col);

//          To avoid boundary walls
            if( x == 0 || y == 0|| (x == col-1) || y == (row -1))
                continue;

//          To avoid the first 3 bricks
            if( ( x == 1 && y == 1 )|| (x == 1 && y == 2) || ( x == 2 && y == 1 )  )
                continue;

//          To avoid the solid bricks
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

        fill(treasure);
        int temp[] = new int[2];
        int space = Treasure.indexOf(' ');
        temp[0] = Integer.parseInt(Treasure.substring(0,space));
        temp[1] = Integer.parseInt(Treasure.substring(space + 1, Treasure.length()));


        tile = tiles[temp[1]][temp[0]];
        rect(tile.posCord.x, tile.posCord.y, tileSize, tileSize);

        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                tile = tiles[i][j];
                if (tile.ty == Tile.type.OBSTACLE) {
                    fill(obs);
                    stroke(0);
                    strokeWeight(2);
                } else if (tile.ty == Tile.type.BRICK) {
                    stroke(0);
                    fill(brick);
                } else {
                    fill(empty);
                    noStroke();
                }
                rect(tile.posCord.x, tile.posCord.y, tileSize, tileSize);
            }
        }

        drawPlayer();
    }

    public void move(PVector targetPos) {
        KinematicInfo target = new KinematicInfo();
        target.setPosition(targetPos.copy());

        sArrive.setTarget(target);
        sAlign.setTarget(target);

        PVector direction = PVector.sub(target.getPosition(), player.getPosition());
        player.setRotation(0);
        sAlign.getTarget().setOrientation(direction.heading());
    }

    public void moveToTile(int tileX, int tileY) {
        if (tileX < 0 || tileY < 0 || tileX > col || tileY > row) {
            return;
        }

        Tile tile = tiles[tileY][tileX];
        if (tile.ty == Tile.type.EMPTY) {
            move(tile.posCord);
        }
    }

    public void keyPressed() {
        int currPosX = Graph.quantizeX(player.getPosition());
        int currPosY = Graph.quantizeY(player.getPosition());
        if (keyPressed && key == CODED) {
            switch (keyCode) {
                case UP:
                    moveToTile(currPosX, currPosY - 1);
                    break;
                case DOWN:
                    moveToTile(currPosX, currPosY + 1);
                    break;
                case LEFT:
                    moveToTile(currPosX - 1, currPosY);
                    break;
                case RIGHT:
                    moveToTile(currPosX + 1, currPosY);
                    break;
                default:
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] {"--present","Graph"});
    }
}
