package data;

import processing.core.PApplet;

/**
 * Created by Anand on 4/1/2016.
 */
public class Enemy extends Character {
    public Enemy() {
    }

    public Enemy(BombermanMap map, PApplet parent) {
        super(map, parent);
    }

    @Override
    public void die() {
        //TODO: Implement this function (kill the enemy)
    }

    void computeAI() {

    }
}
