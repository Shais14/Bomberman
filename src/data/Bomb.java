package data;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

/**
 * Created by Anand on 4/1/2016.
 */
public class Bomb {
    PShape bombShape;
    PApplet parent;

    int blastRadius = 1;

    public PVector bombPos;
    BombermanMap bombermanMap;

    public Bomb(PApplet parent) {
        this.parent = parent;
    }

    public static Bomb plantBomb(Tile tile, PApplet parent) {
        Bomb bomb = new Bomb(parent);
        bomb.bombPos = tile.posCord.copy();
        bomb.bombermanMap = tile.parent;
        bomb.bombShape = parent.createShape(PApplet.ELLIPSE, 0, 0, 10, 10);

        bomb.bombShape.setFill(parent.color(127, 0, 0));

        return bomb;
    }

    public void draw() {
        parent.pushMatrix();
        parent.translate(bombPos.x, bombPos.y);
        parent.shape(bombShape);
        parent.popMatrix();
    }
}
