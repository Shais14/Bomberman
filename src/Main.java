
import data.Const;
import data.KinematicInfo;
import data.PlayerInfo;
import data.SteeringInfo;
import movement.Align;
import movement.Arrive;
import processing.core.*;
import java.util.ArrayList;

public class Main extends PApplet {
    boolean DEBUG_FLAG = false;
    PShape mainShape, circleShape, orientShape, crumbShape;
    long lastAIComputeTime, lastCrumbDrawTime;
    PlayerInfo player;
    Arrive sArrive;
    Align sAlign;
    SteeringInfo steering;
    ArrayList<PVector> breadcrumbs;

    public void setup() {
        PVector initialPos = new PVector(250, 250);

        player = new PlayerInfo();
        player.setPosition(initialPos);
        player.setOrientation(0);
        player.setMaxSpeed(Const.MAX_SPEED);

        KinematicInfo target = new KinematicInfo();
        target.setPosition(initialPos);
        target.setOrientation(0);

        sArrive = new Arrive();
        sArrive.setMaxAcceleration(Const.MAX_LINEAR_ACCELERATION);
        sArrive.setPlayer(player);
        sArrive.setTarget(target);
        sArrive.setMaxSpeed(Const.MAX_SPEED);
        sArrive.setRadiusOfDeceleration(Const.LINEAR_RADIUS_DECELERATION);
        sArrive.setRadiusOfSatisfaction(Const.LINEAR_RADIUS_SATISFACTION);
        sArrive.setTimeToTarget(Const.TIME_TARGET_VELOCITY);

        sAlign = new Align();
        sAlign.setMaxRotation(Const.MAX_ROTATION);
        sAlign.setMaxAngularAcceleration(Const.MAX_ANGULAR_ACCELERATION);
        sAlign.setRadiusOfSatisfaction(Const.ANGULAR_RADIUS_SATISFACTION);
        sAlign.setRadiusOfDeceleration(Const.ANGULAR_RADIUS_DECELERATION);
        sAlign.setTimeToTarget(Const.TIME_TARGET_ROTATION);
        sAlign.setPlayer(player);
        sAlign.setTarget(target);

        steering = new SteeringInfo();

        circleShape = createShape(ELLIPSE, 0, 0, 50, 50);
        circleShape.setFill(color(0));

        orientShape = createShape(TRIANGLE, 0, -25, 0, 25, 50, 0);
        orientShape.setFill(color(0));

        mainShape = createShape(GROUP);
        mainShape.addChild(circleShape);
        mainShape.addChild(orientShape);

        crumbShape = createShape(ELLIPSE, 0, 0, 2, 2);
        crumbShape.setFill(color(64));
        breadcrumbs = new ArrayList<PVector>();

        lastAIComputeTime = System.currentTimeMillis();
        lastCrumbDrawTime = System.currentTimeMillis();
    }

    public void settings() {
        size(500, 500);
    }

    public void draw() {
        background(255);

        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastAIComputeTime > Const.AI_COMPUTE_TIME)) {
            computeAI();
            lastAIComputeTime = currentTime;
        }

        if ((currentTime - lastCrumbDrawTime > Const.CRUMB_DRAW_TIME)) {
            updateCrumbs();
            lastCrumbDrawTime = currentTime;
        }

        if (sAlign.checkOrientationReached()) {
            player.setRotation(0);
            steering.setAngular(0);
        }

        player.update(steering, currentTime);

        drawCrumbs();

        pushMatrix();
        translate(player.getPosition().x, player.getPosition().y);
        rotate(player.getOrientation());
        shape(mainShape);
        popMatrix();
    }

    public void mousePressed() {
        PVector targetPos = new PVector(mouseX, mouseY);
        KinematicInfo target = new KinematicInfo();
        target.setPosition(targetPos);

        sArrive.setTarget(target);
        sAlign.setTarget(target);

        PVector direction = PVector.sub(target.getPosition(), player.getPosition());
        player.setRotation(0);
        sAlign.getTarget().setOrientation(direction.heading());
    }

    void updateCrumbs() {
        if (breadcrumbs.size() >= Const.BREADCRUMB_COUNT) {
            breadcrumbs.remove(0);
        }

        breadcrumbs.add(player.getPosition().copy());
    }

    void drawCrumbs() {
        for (PVector crumb: breadcrumbs) {
            pushMatrix();
            translate(crumb.x, crumb.y);
            shape(crumbShape);
            popMatrix();
        }
    }

    void computeAI() {
        steering = sArrive.getSteering();
        sAlign.getSteering(steering);
    }


    public static void main(String[] args) {
        {
            PApplet.main(new String[]{"--present", "Main"});
        }

    }
}
