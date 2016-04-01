package data;

import processing.core.PApplet;
import processing.core.PVector;

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

    public PlayerInfo() {
        super();
        lives = 3;
        score = 0;
    }

    public PlayerInfo(PApplet parent) {
        super(parent);
        lives = 3;
        score = 0;
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
