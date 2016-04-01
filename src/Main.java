
import data.*;
import movement.Align;
import movement.Arrive;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.util.ArrayList;

public class Main extends PApplet {
    //Data Structures
    BombermanMap bombermanMap;
    PlayerInfo player;

    int blastRadius = 1;

    //Shapes to be rendered
    PShape mainShape, circleShape, orientShape, crumbShape;
    PShape bombShape;
    PVector bombPos;
    ArrayList<PVector> breadcrumbs;

    //Steering behaviour info
    Arrive sArrive;
    Align sAlign;
    SteeringInfo steering;

    //Timing control values
    long lastAIComputeTime, lastCrumbDrawTime, bombPlantTime;

    //Colors used
    public final int empty = color(125, 125, 125);
    public final int brick = color(200, 100, 0);
    public final int obs = color(100, 100, 0);
    public final int treasure = color(0, 0, 0);

    public void initializePlayer(PVector startingPoint) {
        PVector initialPos;
        if (startingPoint == null) {
            initialPos = new PVector();
        } else {
            initialPos = startingPoint.copy();
        }

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

        bombShape = createShape(ELLIPSE, 0, 0, 10, 10);
        bombShape.setFill(color(127, 0, 0));

        lastAIComputeTime = System.currentTimeMillis();
        lastCrumbDrawTime = System.currentTimeMillis();
    }

    public void drawBomb() {
        pushMatrix();
        translate(bombPos.x, bombPos.y);
        shape(bombShape);
        popMatrix();
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

    public void drawMap() {
        int i, j;
        data.Tile tile;
        rectMode(CENTER);

        fill(treasure);
        int temp[] = new int[2];
        int space = bombermanMap.Treasure.indexOf(' ');
        temp[0] = Integer.parseInt(bombermanMap.Treasure.substring(0, space));
        temp[1] = Integer.parseInt(bombermanMap.Treasure.substring(space + 1,
                bombermanMap.Treasure.length()));


        tile = bombermanMap.tiles[temp[1]][temp[0]];
        rect(tile.posCord.x, tile.posCord.y, bombermanMap.tileSize, bombermanMap.tileSize);

        for (i = 0; i < bombermanMap.row; i++) {
            for (j = 0; j < bombermanMap.col; j++) {
                tile = bombermanMap.tiles[i][j];
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
                rect(tile.posCord.x, tile.posCord.y, bombermanMap.tileSize, bombermanMap.tileSize);
            }
        }
    }

    void updateCrumbs() {
        if (breadcrumbs.size() >= Const.BREADCRUMB_COUNT) {
            breadcrumbs.remove(0);
        }

        breadcrumbs.add(player.getPosition().copy());
    }

    void drawCrumbs() {
        for (PVector crumb : breadcrumbs) {
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

    public void setup() {
        background(155);

        bombermanMap = BombermanMap.initializeBombermanMap(width, height);

        initializePlayer(bombermanMap.tiles[1][1].posCord);

//        ArrayList<String> path = Astar.pathAstar(bricks.get(1), bricks.get(5), "E");
    }

    public void draw() {
        drawMap();
        drawPlayer();

        if (bombPos != null) {
            drawBomb();

            long currentTime = System.currentTimeMillis();
            if (currentTime - bombPlantTime > Const.BOMB_DETONATION_TIME) {
                detonateBomb();
                bombPos = null;
            }
        }
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
        PVector targetPos = bombermanMap.getNewTileCords(tileX, tileY);

        if (targetPos != null) {
            // Valid move
            move(targetPos);
        }
    }

    public void plantBomb(int tileX, int tileY) {
        Tile tile = bombermanMap.tiles[tileY][tileX];
        bombPos = tile.posCord.copy();
        bombPlantTime = System.currentTimeMillis();
    }

    public void detonateBomb() {
        int tileX = bombermanMap.quantizeX(bombPos);
        int tileY = bombermanMap.quantizeY(bombPos);

        Tile bombedTile = bombermanMap.tiles[tileY][tileX];
        //TODO: Check the way the edges are set up
        ArrayList<String> outgoingEdges = bombermanMap.edges.get(tileY + " " + tileX);
        destroyAllOnTile(tileY + " " + tileX);
        for (int i = 0; i < outgoingEdges.size(); i += 2) {
            String tileStr = outgoingEdges.get(i);
            destroyAllOnTile(tileStr);
        }
    }

    /**
     * Method to modify states of all objects (player, enemy, brick) on a particular tile
     *
     * @param s String indicating a particular tile
     */
    public void destroyAllOnTile(String s) {
        Tile tile = bombermanMap.toTile(s);
        switch (tile.ty) {
            case BRICK:
                //TODO: Maybe animate this?
                tile.ty = Tile.type.EMPTY;
                break;
            case OBSTACLE:
                break;
            case EMPTY:
                int tileX = bombermanMap.quantizeX(player.getPosition());
                int tileY = bombermanMap.quantizeY(player.getPosition());
                if (tileX == tile.posNum.x && tileY == tile.posNum.y) {
                    player.die();
                }

                //TODO: Destroy enemy also
                break;
        }
    }

    public void keyPressed() {
        int currPosX = bombermanMap.quantizeX(player.getPosition());
        int currPosY = bombermanMap.quantizeY(player.getPosition());
        if (keyPressed) {
            int activeKey = (key == CODED) ? keyCode : key;

            switch (activeKey) {
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
                case ' ':
                    plantBomb(currPosX, currPosY);
                    break;
                default:
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "Main"});
    }
}
