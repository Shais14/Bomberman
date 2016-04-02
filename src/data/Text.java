
package data;

        import processing.core.PApplet;

/**
 * Created by Shais Shaikh on 4/2/2016.
 */
public class Text {


    PApplet parent;

    public Text(PApplet parent){ this.parent = parent;}

    public void draw(PlayerInfo player) {
        drawScore();
        drawLife(player);
    }

    public void drawLife(PlayerInfo player) {
        parent.textSize(20);
        parent.fill(255, 160);
        String lives = "Lives : " + player.lives ;
        parent.text(lives, 450, 38);

    }

    public void drawScore() {
        parent.textSize(20);
        parent.fill(255, 160);
        parent.text("Score : ", 0, 38);
    }



}