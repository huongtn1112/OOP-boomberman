package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.screens.PlayScreen;

import static com.mygdx.game.Constants.BOT_BIT;
import static com.mygdx.game.Constants.BRICK_BIT;
import static com.mygdx.game.Constants.DEFAULT_BIT;
import static com.mygdx.game.Constants.ITEM_BIT;
import static com.mygdx.game.Constants.PLAYER_BIT;
import static com.mygdx.game.Constants.PPM;
import static com.mygdx.game.Constants.WALL_BIT;


/**
 * Created by brentaureli on 8/27/15.
 */
public class Player extends Sprite {
    public enum State {FALLING, STANDING, LEFT, RIGHT, UP, DOWN}

    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private final TextureRegion marioStand;
    private Animation marioRun;
    private Animation marioJump;
    private final Animation moveLeft;
    private final Animation moveRight;
    private final Animation moveUp;
    private final Animation moveDown;

    private float stateTimer;
    public boolean isDie = false;

    public Player(PlayScreen screen) {
        super(screen.getAtlas().findRegion("down"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(getTexture(), i * 130, 0, 130, 160));
        moveUp = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(getTexture(), i * 130, 163, 130, 160));
        moveRight = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(getTexture(), i * 130, 325, 130, 160));
        moveLeft = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 4; i++)
            frames.add(new TextureRegion(getTexture(), i * 130, 487, 130, 160));
        moveDown = new Animation(0.1f, frames);

        marioStand = new TextureRegion(getTexture(), 1, 487, 130, 160);

        definePlayer();
        setBounds(0, 0, 16 / PPM, 16 / PPM);
        setRegion(marioStand);

    }

    //
    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case UP:
                region = (TextureRegion) moveUp.getKeyFrame(stateTimer, true);
                break;
            case DOWN:
                region = (TextureRegion) moveDown.getKeyFrame(stateTimer, true);
                break;
            case LEFT:
                region = (TextureRegion) moveLeft.getKeyFrame(stateTimer, true);
                break;
            case RIGHT:
                region = (TextureRegion) moveRight.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }
/*
//Dao nguoc right khi co 3 anh
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }
 */

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;

    }

    public State getState() {
        if (b2body.getLinearVelocity().y == 0) {
            if (b2body.getLinearVelocity().x > 0) {

                return State.RIGHT;
            } else if (b2body.getLinearVelocity().x < 0) {
                return State.LEFT;
            }
        } else if (b2body.getLinearVelocity().x == 0) {
            if (b2body.getLinearVelocity().y > 0) {
                return State.UP;
            } else if (b2body.getLinearVelocity().y < 0) {
                return State.DOWN;
            }
        }

        return State.STANDING;

    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PPM, 32 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PPM);
//
        fdef.filter.categoryBits = PLAYER_BIT;
        fdef.filter.maskBits = DEFAULT_BIT | WALL_BIT | BRICK_BIT | ITEM_BIT | BOT_BIT;
//
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);



    }

    public void setDie(boolean die) {
        isDie = die;
        System.out.println(die);
    }

    public boolean isDie() {
        return isDie;
    }
}
