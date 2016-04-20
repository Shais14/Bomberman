package data;


import processing.core.PConstants;

/**
 * File containing values of various parameters used by the game.
 * Ideally, all parameter values should be defined here, and used from this interface.
 *
 * Created by Anand on 2/12/2016.
 */
public interface Const extends PConstants {
    // The epsilon value for all floating point comparisons to be used on angles (orientations, rotations, etc.)
    float ANGLE_EPSILON = PI/30;

    // Epsilon value for floating point comparisons to be used on linear values (positions, velocities, etc.)
    // (Currently not used anywhere)
    float LINEAR_EPSILON = 1f;

    // Number of breadcrumbs to be drawn on the screen
    int BREADCRUMB_COUNT = 800;

    // Time after which AI computations should take place (in milliseconds)
    int AI_COMPUTE_TIME = 50;
    // Time after which the next bread crumb should be drawn (in milliseconds)
    int CRUMB_DRAW_TIME = 250;

    int ITERATION_TIME_LIMIT = 180*1000;
    int NUMBER_OF_ITERATIONS = 14;
//  Number of enemies
    int numOfEnemies = 2;

//    Minimum Character Distance
    int minCharacterDist = 8;

    // Maximum values that should be allowed for a player
    float MAX_SPEED = 100f;
    float MAX_ROTATION = -250*PI;
    float MAX_LINEAR_ACCELERATION = 12.5f;
    float MAX_ANGULAR_ACCELERATION = - 10*PI;

    // Satisfaction and deceleration values for Arrive
    float LINEAR_RADIUS_SATISFACTION = 0.5f;
    float LINEAR_RADIUS_DECELERATION = 15f;

    float TIME_TARGET_VELOCITY = 0.05f;

    //Satisfaction and deceleration values for Align (in radians)
    float ANGULAR_RADIUS_SATISFACTION = PI / 128;
    float ANGULAR_RADIUS_DECELERATION = PI / 16;

    float TIME_TARGET_ROTATION = 0.01f;


    float BOMB_DETONATION_TIME = 2000;

    int TREASURE_DISTANCE = 8;



//    float WANDER_OFFSET = 100;
//    float WANDER_RADIUS = 50;
//    float WANDER_RATE = 4 * PI;
//
//    float COLLISION_RADIUS = 50;
//    float TIME_TARGET_FLOCK_VELOCITY = 5f;
//
//    float WEIGHT_SEPARATION = 0.4f;
//    float WEIGHT_VELOCITY_MATCH = 0.15f;
//    float WEIGHT_SEEK_LEADER = 0.3f;
//    float WEIGHT_ARRIVE_CENTRE_MASS = 0.15f;
//    float WEIGHT_ALIGN = 1;
//
//    int BEHAVIOR_SEPARATION = 0;
//    int BEHAVIOR_VELOCITY_MATCH = 1;
//    int BEHAVIOR_SEEK_LEADER = 2;
//    int BEHAVIOR_ARRIVE_CENTRE_MASS = 3;
//    int BEHAVIOR_ALIGN = 4;
//
//    int NUM_FOLLOWERS = 4;
//    int NUM_LEADERS = 1;
//
//    float MAX_LEADER_ACCELERATION = 0.1f;
//    float MAX_FOLLOWER_ACCELERATION = 0.01f;


    String GRAPH_FILE_NAME = "C:\\Users\\Anand\\IdeaProjects\\CSC584\\Assignment 2\\huntModel.txt";
    int GRAPH_START_NODE_INDEX = 0;
//    float[] GRAPH_LEVEL_OFFSET = {65, 250, 450, 600, 800};
    float GRAPH_LEVEL_OFFSET = 140;
    float GRAPH_NODE_HEIGHT = 40;
    float GRAPH_X_OFFSET = -200;
    float GRAPH_Y_OFFSET = 70;
    float GRAPH_NODE_WIDTH = 50;
    float GRAPH_LOBBY_WIDTH = 150;

    int IGNORE = 0;
    int PRESENCE = 1;
    int AMPLITUDE = 2;

    interface DecisionTreeParams {
        int GRAPH_KEY = 1;
        int PLAYER_KEY = 2;
        int ENEMY_KEY = 3;
        int ALL_CHARS_KEY = 4;
        int CURR_CHAR_KEY = 5;
        int CURR_TILE_KEY = 6;
        int NEXT_TILE_KEY = 7;
        int BOMB_KEY = 8;
        int NEXT_TARGET_STR_KEY = 9;

        String DECISION_TREE_FILE_NAME = "decisionTree.txt";
        String NO_SIGNAL_DECISION_TREE_FILE_NAME = "NoSignal.txt";
        String SIGNAL_PRESENCE_DECISION_TREE_FILE_NAME = "SignalWW.txt";
        String ENEMY_DECISION_TREE_FILE_NAME = "enemyDTree.txt";

    }
    String RECORDS_FILE_PATH = "debug/Records" + NUMBER_OF_ITERATIONS+ ".txt";
//    String RECORDS_FILE_PATH = "debug/Records98.txt";
    String GREAT_SUCCESS_IMAGE_FILE_PATH = "Great Success.jpg";

    int DEATH_BY_BOMB = 1;
    int DEATH_BY_ENEMY = 2;
    int DEATH_BY_TIME = 3;
    int DEATH_BY_EXCEPTION = 4;

    int USE_LOGS = 1;
}
