
import data.*;
import data.Character;
import decision.Action;
import decision.DTreeNode;
import decision.DecisionTreeGenerator;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends PApplet {
    //Data Structures
    BombermanMap bombermanMap;
    PlayerInfo player;
    Text text;
    Enemy enemy;


    int blastRadius = 1;

    Bomb activeBomb;

    //Timing control values
    long bombPlantTime;

    HashMap<Integer, Object> paramMap;
    ArrayList<Character> allCharacters;

    public void initializeCharacters(PVector startingPoint, PVector enemyStartingPoint) {
        player = new PlayerInfo(this, bombermanMap);
        player.initialize(startingPoint);
        player.decisionTreeHead = DecisionTreeGenerator.generateDecisionTree(Const.DecisionTreeParams.DECISION_TREE_FILE_NAME);

        enemy = new Enemy(this, bombermanMap);
        enemy.initialize(enemyStartingPoint);
    }

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        background(155);

        bombermanMap = BombermanMap.initializeBombermanMap(this);
        initializeCharacters(bombermanMap.tiles[1][1].posCord, bombermanMap.tiles[13][13].posCord);

        text = new Text(this);

        allCharacters = new ArrayList<Character>();
        allCharacters.add(player);
        allCharacters.add(enemy);

        paramMap = new HashMap<Integer, Object>();
        paramMap.put(Const.DecisionTreeParams.GRAPH_KEY, bombermanMap);
        paramMap.put(Const.DecisionTreeParams.PLAYER_KEY, player);
        paramMap.put(Const.DecisionTreeParams.ENEMY_KEY, enemy);
        paramMap.put(Const.DecisionTreeParams.ALL_CHARS_KEY, allCharacters);
    }

//    public void mousePressed()
//    {
//        ArrayList<String> path = Astar.pathAstar("1 1", "3 3", "E", bombermanMap);
//    }

    public void draw() {
        bombermanMap.draw();
        bombermanMap.drawSignal();
        player.draw();
        NodePlus2();
        text.draw(player);
        enemy.draw();

        if (activeBomb != null) {
            activeBomb.draw();

            long currentTime = System.currentTimeMillis();
            if (currentTime - bombPlantTime > Const.BOMB_DETONATION_TIME) {
                detonate();
                activeBomb = null;
            }
        }

        paramMap.put(Const.DecisionTreeParams.CURR_TILE_KEY, bombermanMap.getTileAt(player.kinematicInfo.getPosition()));
        paramMap.put(Const.DecisionTreeParams.BOMB_KEY, activeBomb);

        // TODO: Check if this prediction is correct
        PVector predictedPosition = PVector.add(player.kinematicInfo.getPosition(), PVector.fromAngle(player.kinematicInfo.getOrientation()).mult(bombermanMap.tileSize));
        paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, bombermanMap.getTileAt(predictedPosition));

//        if (isNextDTreeEvalReqd()) {
//            paramMap.put(Const.DecisionTreeParams.CURR_CHAR_KEY, player);
//            Action nextAction = player.evaluateDTree(paramMap);
//            if (nextAction != null) {
//                player.isPerformingAction = true;
//                player.currAction = nextAction;
//                nextAction.performAction(paramMap);
//            }
//        }

    }

    public void moveToTile(int tileX, int tileY) {
        PVector targetPos = bombermanMap.getNewTileCords(tileX, tileY);

        if (targetPos != null) {
            // Valid move
            player.move(targetPos);
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

    public void detonate() {
        int tileX = bombermanMap.quantizeX(activeBomb.bombPos);
        int tileY = bombermanMap.quantizeY(activeBomb.bombPos);

        Tile bombedTile = bombermanMap.tiles[tileY][tileX];
        ArrayList<String> outgoingEdges = bombermanMap.edges.get(bombedTile.toString());
        destroyAllOnTile(bombedTile.toString());
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
                    if (activeBomb == null) {
                        activeBomb = Bomb.plantBomb(bombermanMap.tiles[currPosY][currPosX], this);
                        bombPlantTime = System.currentTimeMillis();
                    }
                    break;
                default:
            }
        }
    }

    public void NodePlus2() {

        Tile currTile = bombermanMap.getTileAt(player.kinematicInfo.getPosition());
        //rc and cc is the character's current row and col index.

        int rc = currTile.posNum.rowIndex;
        int cc = currTile.posNum.colIndex;
        String current, target;

        int r1, c1; // to store the index of the tile at distance 1.

        List<String> processedList = new ArrayList<String>();

        boolean hasEdge = false;
        do {
            int x = (int) random(1, 5);

            int[] nextTile;
            nextTile = findTile(x, rc, cc);

            r1 = nextTile[0];
            c1 = nextTile[1];

            current = rc + " " + cc;
            target = r1 + " " + c1;

            Tile targertTile = Tile.toTile(target, bombermanMap);
            processedList.add(current);

            if(!processedList.contains(target)){
                hasEdge = findEdge(current, target);
                processedList.add(target);
            }
            target = null;

        } while (!hasEdge);

        processedList.clear();

        processedList.add(current);

//        System.out.print("Current " + current);


        hasEdge = false;
        do {
            int y = (int) random(1, 4);

            int[] nextTile;
            nextTile = findTile(y, r1, c1);

            int r2 = nextTile[0];
            int c2 = nextTile[1];

            current = r1 + " " + c1;
            target = r2 + " " + c2;

            processedList.add(current);


            if(!processedList.contains(target)){
                hasEdge = findEdge(current, target);
                processedList.add(target);

            }
        } while (!hasEdge);

        processedList.clear();

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

    public boolean findEdge(String current, String target) {
        ArrayList<String> edgeList;
        edgeList = bombermanMap.edges.get(current);

        return edgeList.contains(target);
    }

    public boolean isNextDTreeEvalReqd() {
        return player.currAction == null || player.currAction.hasCompleted(paramMap);

    }

    public void findEdge(Tile current, Tile target) {

        String cur = current.toString();
        String tar = target.toString();

        findEdge(cur, tar);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"--present", "Main"});
    }
}
