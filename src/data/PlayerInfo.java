package data;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * This subclass of {@link KinematicInfo} is used to represent a player or an enemy.
 * The main difference between this class and its parent, is purely semantic, though.
 * However, in the future, this class may see some extension and hence should
 * be used to represent drawable characters, while the parent class is used for specifying targets only.
 *
 * Created by Anand on 2/12/2016.
 */
public class PlayerInfo extends Character {
    public int lives;
    public int score;
    public BombermanMap map;
    public boolean explored[][];
//    public Tile current;
//    public Tile parent;
    public ArrayList<String> moveList;

    public PlayerInfo() {
        super();
        lives = 3;
        score = 0;
        moveList = new ArrayList<String>();

    }

    public PlayerInfo(PApplet parent, BombermanMap map) {
        super(parent);
        lives = 3;
        score = 0;
        this.map = map;
        explored = new boolean[map.row][map.col];

    }

    public void die() {
        lives--;
        if (lives == 0) {
            //Game Over
            System.exit(0);
        }
    }

    public void incrementScore(int amount) {
        score += amount;
    }
}
