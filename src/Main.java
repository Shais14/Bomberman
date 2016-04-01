
import algorithms.Astar;
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
    Enemy enemy;

    int blastRadius = 1;

    //Shapes to be rendered
    PShape bombShape;
    PVector bombPos;

    //Timing control values
    long bombPlantTime;

    public void initializeCharacters(PVector startingPoint, PVector enemyStartingPoint) {
        player = new PlayerInfo(this);
        player.initialize(startingPoint);

        enemy = new Enemy(this);
        enemy.initialize(enemyStartingPoint);

        bombShape = createShape(ELLIPSE, 0, 0, 10, 10);
        bombShape.setFill(color(127, 0, 0));
    }

    public void drawBomb() {
        pushMatrix();
        translate(bombPos.x, bombPos.y);
        shape(bombShape);
        popMatrix();
    }

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        background(155);

        bombermanMap = BombermanMap.initializeBombermanMap(this);
        initializeCharacters(bombermanMap.tiles[1][1].posCord, bombermanMap.tiles[13][13].posCord);
    }

//    public void mousePressed()
//    {
//        ArrayList<String> path = Astar.pathAstar("1 1", "3 3", "E", bombermanMap);
//    }

    public void draw() {
        bombermanMap.draw();
        player.draw();
        enemy.draw();

        if (bombPos != null) {
            drawBomb();

            long currentTime = System.currentTimeMillis();
            if (currentTime - bombPlantTime > Const.BOMB_DETONATION_TIME) {
                detonateBomb();
                bombPos = null;
            }
        }

    }

    public void moveToTile(int tileX, int tileY) {
        PVector targetPos = bombermanMap.getNewTileCords(tileX, tileY);

        if (targetPos != null) {
            // Valid move
            player.move(targetPos);
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
        ArrayList<String> outgoingEdges = bombermanMap.edges.get(bombedTile.toString());
        destroyAllOnTile(bombedTile.toString());
        for (int i = 0; i < outgoingEdges.size(); i += 2) {
            String tileStr = outgoingEdges.get(i);
            destroyAllOnTile(tileStr);
        }
    }

    public void killAll(Tile tile) {
        //Kill player
        int playerTileX = bombermanMap.quantizeX(player.kinematicInfo.getPosition());
        int playerTileY = bombermanMap.quantizeY(player.kinematicInfo.getPosition());
        if (playerTileX == tile.posNum.colIndex && playerTileY == tile.posNum.rowIndex) {
            player.die();
        }

        //Kill enemy(ies)
        int enemyTileX = bombermanMap.quantizeX(enemy.kinematicInfo.getPosition());
        int enemyTileY = bombermanMap.quantizeY(enemy.kinematicInfo.getPosition());
        if (enemyTileX == tile.posNum.colIndex && enemyTileY == tile.posNum.rowIndex) {
            enemy.die();
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
                killAll(tile);
                break;
        }
    }

    public void keyPressed() {
        int currPosX = bombermanMap.quantizeX(player.kinematicInfo.getPosition());
        int currPosY = bombermanMap.quantizeY(player.kinematicInfo.getPosition());
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
