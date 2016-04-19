
import data.*;
import data.Character;
import debug.DebugUtil;
import debug.GenerateCSV;
import decision.Action;
import decision.DecisionTreeGenerator;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Random;


public class Main extends PApplet {
    //Data Structures
    BombermanMap bombermanMap;
    PlayerInfo player;
    Text text;
    ArrayList<Enemy> enemies;
    ArrayList<Record> records = new ArrayList<Record>();
    PImage img;
    int blastRadius = 1;


    Bomb activeBomb;

    //Timing control values
    long bombPlantTime;
    long startTime;
    long endTime;

    HashMap<Integer, Object> paramMap;
    ArrayList<HashMap<Integer, Object>> enemiesParamMap;

    String mapConfigFilePath;
    boolean useSavedMapConfig = false;
    boolean startNewIteration = false;
    int iterationCount = 0;
    long iterationTimer = 0;
    long pauseTimer = 0;

    public ArrayList<Tile> getEnemyTiles() {
        int currRow, currCol, playerRow, playerCol, enemyRow, enemyCol;
        boolean flag;
        Tile currTile, enemyTile;
        ArrayList<Tile> enemyTiles = new ArrayList<Tile>();
        Random r = new Random();

        playerRow = 1;
        playerCol = 1;
        do {
            currRow = r.nextInt(bombermanMap.row);
            currCol = r.nextInt(bombermanMap.col);
            currTile = bombermanMap.tiles[currRow][currCol];
            if (currTile.ty == Tile.type.EMPTY) {
                flag = true;
                for (int i = 0; i < enemyTiles.size(); i++) {
                    enemyTile = enemyTiles.get(i);
                    enemyRow = enemyTile.posNum.rowIndex;
                    enemyCol = enemyTile.posNum.colIndex;
                    if ((Math.abs(currRow - enemyRow) + Math.abs(currCol - enemyCol) < Const.minCharacterDist) ||
                            (Math.abs(currRow - playerRow) + Math.abs(currCol - playerCol) < Const.minCharacterDist)) {
                        flag = false;
                        break;
                    }

                }

                if (flag)
                    enemyTiles.add(currTile);
            }

        } while (enemyTiles.size() < Const.numOfEnemies);

        return enemyTiles;
    }

    public void initializeCharacters(boolean newMapGenerated) {
        ArrayList<Tile> enemyTiles = new ArrayList<Tile>();
        Enemy enemy;
        PVector startingPoint;
        startingPoint = bombermanMap.tiles[1][1].posCord;
        player = new PlayerInfo(this, bombermanMap);
        player.setFill(color(200, 200, 100));
        player.initialize(startingPoint);
//        if (newMapGenerated) {
        switch (iterationCount % 3) {
            case Const.IGNORE:
                startTime = System.nanoTime();
                player.decisionTreeHead = DecisionTreeGenerator.generateDecisionTree(Const.DecisionTreeParams.NO_SIGNAL_DECISION_TREE_FILE_NAME);
                DebugUtil.printDebugString("Iteration count: No signal");
                break;
            case Const.PRESENCE:
                startTime = System.nanoTime();
                player.decisionTreeHead = DecisionTreeGenerator.generateDecisionTree(Const.DecisionTreeParams.SIGNAL_PRESENCE_DECISION_TREE_FILE_NAME);
                DebugUtil.printDebugString("Iteration count: Presence");
                break;
            case Const.AMPLITUDE:
                startTime = System.nanoTime();
                player.decisionTreeHead = DecisionTreeGenerator.generateDecisionTree(Const.DecisionTreeParams.DECISION_TREE_FILE_NAME);
                DebugUtil.printDebugString("Iteration count: Signal seek");
                break;
        }
//        }

        enemyTiles = getEnemyTiles();
        enemies = new ArrayList<Enemy>();
        for (int i = 0; i < Const.numOfEnemies; i++) {
            enemy = new Enemy(this, bombermanMap);
            startingPoint = enemyTiles.get(i).posCord;
            enemy.initialize(startingPoint);
            enemy.kinematicInfo.setOrientation(PI);
            enemy.decisionTreeHead = DecisionTreeGenerator.generateDecisionTree(Const.DecisionTreeParams.ENEMY_DECISION_TREE_FILE_NAME);
            enemies.add(enemy);
        }
    }

    public void settings() {
        size(600, 600);
    }

    public void initializeIteration(boolean generateNewMap) {
//        if (generateNewMap) {
//            iterationCount++;
//        }

        iterationTimer = System.currentTimeMillis();
        paramMap = new HashMap<Integer, Object>();
        enemiesParamMap = new ArrayList<HashMap<Integer, Object>>();

        if (generateNewMap) {
            bombermanMap = BombermanMap.initializeBombermanMap(this);
        } else {
            bombermanMap = BombermanMap.initializeBombermanMap(this, mapConfigFilePath);
        }

        initializeCharacters(generateNewMap);

        paramMap.put(Const.DecisionTreeParams.GRAPH_KEY, bombermanMap);
        paramMap.put(Const.DecisionTreeParams.PLAYER_KEY, player);
        paramMap.put(Const.DecisionTreeParams.ENEMY_KEY, enemies);

        HashMap<Integer, Object> enemyParamMap;
        for (int i = 0; i < Const.numOfEnemies; i++) {
            enemyParamMap = new HashMap<Integer, Object>();
            enemyParamMap.put(Const.DecisionTreeParams.GRAPH_KEY, bombermanMap);
            enemyParamMap.put(Const.DecisionTreeParams.PLAYER_KEY, player);
            enemyParamMap.put(Const.DecisionTreeParams.ENEMY_KEY, enemies);
            enemiesParamMap.add(enemyParamMap);
        }

        activeBomb = null;
    }

    public void setup() {
        text = new Text(this);
        background(155);
        initializeIteration(true);
        img = loadImage(Const.GREAT_SUCCESS_IMAGE_FILE_PATH);

    }

    public void draw() {
        Enemy enemy;
        HashMap<Integer, Object> enemyParamMap;

        long currentTime = System.currentTimeMillis();
        long gameTime = currentTime - iterationTimer;
        try {
            if ( gameTime > Const.ITERATION_TIME_LIMIT) {
                player.reasonOfDeath = Const.DEATH_BY_TIME;
                DebugUtil.printDebugString("Death by time");
                reset();
            }

            if (startNewIteration) {
                initializeIteration(!useSavedMapConfig);
                startNewIteration = false;
            }

            bombermanMap.draw();
            bombermanMap.drawSignal();
            player.draw();
            for (Enemy currEnemy : enemies) {
                currEnemy.draw();
            }

            if (paramMap.get(Const.DecisionTreeParams.BOMB_KEY) != null) {
                if (activeBomb == null) {
                    activeBomb = (Bomb) paramMap.get(Const.DecisionTreeParams.BOMB_KEY);
                    bombPlantTime = System.currentTimeMillis();
                }
            }

            if (activeBomb != null) {
                activeBomb.draw();

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

            text.draw(paramMap, iterationCount, !useSavedMapConfig,  gameTime);

            for (int i = 0; i < enemies.size(); i++) {
                enemy = enemies.get(i);
                enemyParamMap = enemiesParamMap.get(i);
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


//        Collision Detection
            Tile currPlayerTile = (Tile) paramMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);

            if (currPlayerTile == Tile.toTile(bombermanMap.Treasure, bombermanMap)) {
                success();
            }

            for (int i = 0; i < enemies.size(); i++) {
                enemyParamMap = enemiesParamMap.get(i);
                Tile currEnemyTile = (Tile) enemyParamMap.get(Const.DecisionTreeParams.CURR_TILE_KEY);
                if (currEnemyTile == currPlayerTile) {
                    player.die();
                    player.reasonOfDeath = Const.DEATH_BY_ENEMY;
                    DebugUtil.printDebugString("Death by enemy");
                    reset();
                    break;
                }
            }
        } catch (Exception e) {
            DebugUtil.printDebugString(e.toString());
            e.printStackTrace();
            player.reasonOfDeath = Const.DEATH_BY_EXCEPTION;
            DebugUtil.printDebugString("**** About to reset now ****");
            DebugUtil.printDebugString("Death by exception");
            reset();
        }
    }

    public void success() {
        System.out.println("BORAT SAYS GR8 SUCCESS");
        image(img, 150, 50);
        player.success = true;

        reset();

//        noLoop();
    }

    public void moveToTile(int tileX, int tileY) {
        PVector targetPos = bombermanMap.getNewTileCords(tileX, tileY);

        if (targetPos != null) {
            // Valid move
            player.move(targetPos);
        }
    }

    public void killAll(Tile tile) {
        Enemy enemy;
        //Kill player
        int playerTileX = bombermanMap.quantizeX(player.kinematicInfo.getPosition());
        int playerTileY = bombermanMap.quantizeY(player.kinematicInfo.getPosition());
        if (playerTileX == tile.posNum.colIndex && playerTileY == tile.posNum.rowIndex) {
            DebugUtil.printDebugString("***** Last action executed by PLAYER - " + player.currAction);
            player.reasonOfDeath = Const.DEATH_BY_BOMB;
            DebugUtil.printDebugString("Death by bomb");
            player.die();
            reset();
        }

        //Kill enemy(ies)
        int k = 0;
        ListIterator<Enemy> list = enemies.listIterator();
        while (list.hasNext()) {
            enemy = list.next();
            int enemyTileX = bombermanMap.quantizeX(enemy.kinematicInfo.getPosition());
            int enemyTileY = bombermanMap.quantizeY(enemy.kinematicInfo.getPosition());
            if (enemyTileX == tile.posNum.colIndex && enemyTileY == tile.posNum.rowIndex) {
                list.remove();
                enemiesParamMap.remove(k);
                player.incrementScore(100);
            }
            k++;
        }
    }

    public void reset() {

        try {
            if (iterationCount <= Const.NUMBER_OF_ITERATIONS) {
                startNewIteration = true;
                iterationTimer = System.currentTimeMillis();
                Record rc;
                if (iterationCount % 3 == 0) {
                    rc = new Record(iterationCount / 3);
                    records.add(rc);
                } else {
                    rc = records.get(iterationCount / 3);
                }
                endTime = System.nanoTime();
                updateRecords(rc);
                iterationCount++;
                if (iterationCount % 3 != 0) {
                    mapConfigFilePath = DebugUtil.saveMapConfig(bombermanMap);
                    useSavedMapConfig = true;
                } else {
                    useSavedMapConfig = false;
                }
            } else {
                DebugUtil.printRecords(records.get((iterationCount) / 3 - 1));
                DebugUtil.saveRecords(records);
                GenerateCSV.generateCsvFile("records" + Integer.toString(Const.NUMBER_OF_ITERATIONS)+".csv", records);
                Analysis anal = new Analysis();
                anal.readFile(Const.RECORDS_FILE_PATH);
                System.exit(0);
            }
//        if(player.lives > 0) {
//            PVector startingPoint;
//            startingPoint = bombermanMap.tiles[1][1].posCord;
//            player.initialize(startingPoint);
//        }
//        else if(player.lives == 0){
//            System.exit(0);
//        }
        } catch (Exception e) {
            DebugUtil.printDebugString(e.toString());
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void updateRecords(Record rc) {
        rc.incrementScore(iterationCount % 3, player.score);
        rc.isSuccess(iterationCount % 3, player.success);
        rc.numOfBombsPlanted(iterationCount % 3, Bomb.numBombs);
        rc.deathReason(iterationCount % 3, player.reasonOfDeath);
        long totalTime = (endTime - startTime) / 1000000000;
        rc.timeTaken(iterationCount % 3, totalTime);
        Bomb.numBombs = 0;
        startTime = 0;
        endTime = 0;
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
                case 'p':
                    if (looping) {
                        pauseTimer = System.currentTimeMillis() - iterationTimer;
                        noLoop();
                    } else {
                        long nowTime = System.currentTimeMillis();
                        player.kinematicInfo.lastUpdateTime = nowTime;
                        iterationTimer = nowTime - pauseTimer;
                        loop();
                    }
                    break;
                case 's':
                    mapConfigFilePath = DebugUtil.saveMapConfig(bombermanMap);
                    startNewIteration = true;
                    useSavedMapConfig = true;
                    break;
                case 'r':
                    startNewIteration = true;
                    useSavedMapConfig = false;
                    break;
                case 'n':
//                    iterationCount++;
                    startNewIteration = true;
                    if (!useSavedMapConfig) {
                        mapConfigFilePath = DebugUtil.saveMapConfig(bombermanMap);
                        useSavedMapConfig = true;
                    }
                    reset();
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
