package data;

import processing.core.PApplet;

/**
 * Created by Anand on 4/1/2016.
 */
public class Enemy extends Character {
    public Enemy() {
    }

    public Enemy(PApplet parent, BombermanMap map) {
        super(parent, map);
    }

    @Override
    public void die() {
        //TODO: Implement this function (kill the enemy)

    }
}
