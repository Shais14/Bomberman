package data;


import processing.core.PConstants;

/**
 * Created by Anand on 2/12/2016.
 */
public interface Const extends PConstants {
    float ANGLE_EPSILON = PI/30;
    float LINEAR_EPSILON = 1f;

    int BREADCRUMB_COUNT = 800;

    int AI_COMPUTE_TIME = 50;
    int CRUMB_DRAW_TIME = 250;

    float MAX_SPEED = 50f;
    float MAX_LINEAR_ACCELERATION = 12.5f;
    float MAX_ANGULAR_ACCELERATION = - PI / 1.5f;

    float LINEAR_RADIUS_SATISFACTION = 0.5f;
    float LINEAR_RADIUS_DECELERATION = 15f;
    float TIME_TARGET_VELOCITY = 0.15f;

    float MAX_ROTATION = -PI;
    float ANGULAR_RADIUS_SATISFACTION = PI / 64;
    float ANGULAR_RADIUS_DECELERATION = PI / 16;
    float TIME_TARGET_ROTATION = 0.05f;

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
}
