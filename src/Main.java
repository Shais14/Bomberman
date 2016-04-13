
import data.*;
import data.Character;
import debug.DebugUtil;
import decision.Action;
import decision.DTreeNode;
import decision.DecisionTreeGenerator;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static data.Tile.type.BRICK;

public class Main extends PApplet {
    //Data Structures
    BombermanMap bombermanMap;
    PlayerInfo player;
    Text text;
    //TODO: Convert this into an arraylist, to allow multiple enemies
    Enemy enemy;


    int blastRadius = 1;

    Bomb activeBomb;

    //Timing control values
    long bombPlantTime;

    HashMap<Integer, Object> paramMap;
    HashMap<Integer, Object> enemyParamMap;
    ArrayList<Character> allCharacters;

    public void initializeCharacters(PVector startingPoint, PVector enemyStartingPoint) {
        player = new PlayerInfo(this, bombermanMap);
        player.initialize(startingPoint);
        player.decisionTreeHead = DecisionTreeGenerator.generateDecisionTree(Const.DecisionTreeParams.DECISION_TREE_FILE_NAME);

        enemy = new Enemy(this, bombermanMap);
        //TODO: Randomize enemy starting point
        enemy.initialize(enemyStartingPoint);
        enemy.kinematicInfo.setOrientation(-PI/2);
        enemy.decisionTreeHead = DecisionTreeGenerator.generateDecisionTree(Const.DecisionTreeParams.ENEMY_DECISION_TREE_FILE_NAME);
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

        enemyParamMap = new HashMap<Integer, Object>();
        enemyParamMap.put(Const.DecisionTreeParams.GRAPH_KEY, bombermanMap);
        enemyParamMap.put(Const.DecisionTreeParams.PLAYER_KEY, player);
        enemyParamMap.put(Const.DecisionTreeParams.ENEMY_KEY, enemy);
        enemyParamMap.put(Const.DecisionTreeParams.ALL_CHARS_KEY, allCharacters);
    }

    public void draw() {
        bombermanMap.draw();
        bombermanMap.drawSignal();
        player.draw();
        text.draw(player);
        enemy.draw();

        if (paramMap.get(Const.DecisionTreeParams.BOMB_KEY) != null) {
            if (activeBomb == null) {
                activeBomb = (Bomb) paramMap.get(Const.DecisionTreeParams.BOMB_KEY);
                bombPlantTime = System.currentTimeMillis();
            }
        }

        if (activeBomb != null) {
            activeBomb.draw();

            long currentTime = System.currentTimeMillis();
            if (currentTime - bombPlantTime > Const.BOMB_DETONATION_TIME) {
                detonate();
                activeBomb = null;
            }
        }

        Tile currTile = bombermanMap.getTileAt(player.kinematicInfo.getPosition());
        if (currTile != null) {
            paramMap.put(Const.DecisionTreeParams.CURR_TILE_KEY, currTile);
            paramMap.put(Const.DecisionTreeParams.BOMB_KEY, activeBomb);

            if (isNextDTreeEvalReqd(player, paramMap)) {
                PVector predictedPosition = PVector.add(player.kinematicInfo.getPosition(), PVector.fromAngle(player.kinematicInfo.getOrientation()).mult(bombermanMap.tileSize));
                paramMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, bombermanMap.getTileAt(predictedPosition));
                paramMap.put(Const.DecisionTreeParams.CURR_CHAR_KEY, player);
                Action nextAction = player.evaluateDTree(paramMap);
                if (nextAction != null) {
                    player.isPerformingAction = true;
                    player.currAction = nextAction;
                    DebugUtil.printDecisionTreeNode(nextAction);
                    nextAction.performAction(paramMap);
                }
            }
        }

        Tile currEnemyTile = bombermanMap.getTileAt(enemy.kinematicInfo.getPosition());
        if (currEnemyTile != null) {
            enemyParamMap.put(Const.DecisionTreeParams.CURR_TILE_KEY, currEnemyTile);

            if (isNextDTreeEvalReqd(enemy, enemyParamMap)) {
                PVector predictedPosition = PVector.add(enemy.kinematicInfo.getPosition(), PVector.fromAngle(enemy.kinematicInfo.getOrientation()).mult(bombermanMap.tileSize));
                enemyParamMap.put(Const.DecisionTreeParams.NEXT_TILE_KEY, bombermanMap.getTileAt(predictedPosition));
                enemyParamMap.put(Const.DecisionTreeParams.CURR_CHAR_KEY, enemy);
                Action nextAction = enemy.evaluateDTree(enemyParamMap);
                if (nextAction != null) {
                    enemy.isPerformingAction = true;
                    enemy.currAction = nextAction;
//                    DebugUtil.printDecisionTreeNode(nextAction);
                    nextAction.performAction(enemyParamMap);
                }
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

    public void killAll(Tile tile) {
        //Kill player
        int playerTileX = bombermanMap.quantizeX(player.kinematicInfo.getPosition());
        int playerTileY = bombermanMap.quantizeY(player.kinematicInfo.getPosition());
        if (playerTileX == tile.posNum.colIndex && playerTileY == tile.posNum.rowIndex) {
            DebugUtil.printDebugString("***** Last action executed by PLAYER - " + player.currAction);
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

    public boolean findEdge(String current, String target) {
        ArrayList<String> edgeList;
        edgeList = bombermanMap.edges.get(current);

        return edgeList.contains(target);
    }

    public boolean isNextDTreeEvalReqd(Character character, HashMap<Integer, Object> paramMap) {
        return character.currAction == null || character.currAction.hasCompleted(paramMap);

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
