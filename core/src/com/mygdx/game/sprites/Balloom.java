package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.screens.PlayScreen;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.Constants.BOT_BIT;
import static com.mygdx.game.Constants.DESTROYED_BIT;
import static com.mygdx.game.Constants.PPM;

public class Balloom extends Item {
    protected float speed = 0.3f;
    protected float timeB;
    protected float timeGoBack;
    protected float px, py;
    protected float timeUpdate;
    protected float timeFreeMove;

    public Balloom() {
    }

    //public boolean isCollision;
    public Balloom(PlayScreen screen, Rectangle bounds) {
        super();
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        //bdef.type = BodyDef.BodyType.StaticBody;
        bdef.type = BodyDef.BodyType.DynamicBody;

        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PPM, (bounds.getY() + bounds.getHeight() / 2) / PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / PPM, bounds.getHeight() / 2 / PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

        this.screen = screen;
        fixture.setUserData(this);
        setCategoryFilter(BOT_BIT);
        isHit = false;
        texture = new Texture(("Item/balloom.png"));
        setTexture(texture);
        //bdef.type = BodyDef.BodyType.KinematicBody;
        //move();
        timeB = 0;
        timeGoBack = 3;
        isCollision = false;
        px = 0;
        py = 0;
        timeFreeMove = 1;
        timeUpdate = 3;

    }

    public void setPlayerPos(float px, float py) {
        this.px = px;
        this.py = py;
    }


    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(texture, getTexX(), getTexY(), 8 / PPM, 8 / PPM);
        sb.end();
        timeB += Gdx.graphics.getDeltaTime();
        // System.out.println("time" + timeB);
        //setSpeed();
        //move();
        //move(50, 20);
        move();
    }

    public void catchPlayer(float px, float py) {
        if (timeUpdate == 3) {
            if (Math.abs(getX() - px) < 50 && Math.abs(getY() - py) < 50) {
                move(px, py);
                timeUpdate -= Gdx.graphics.getDeltaTime();
            }
        } else {
            timeUpdate = 3;
        }

    }

    public void move() {
        //body.setLinearVelocity(new Vector2(speed/PPM, 0));
        float x = body.getPosition().x + speed / PPM;
        float y = body.getPosition().y;
        // body.setTransform(x, y, body.getAngle());

        if (timeB < timeGoBack) {
            moveLeft();
        }
        if (timeB > timeGoBack || isCollision) {
            moveRight();
            //speed *=-1f;
            //System.out.println("doi");
            if (timeB > 2 * timeGoBack) {
                timeB -= 2 * timeGoBack;
            }

        }
    }

    public void freeMove() {
        if (timeFreeMove >= 1) {
            int val = random.nextInt(4);
            if (val == 0) {
                //nonMove();
                moveUp();
            }
            if (val == 1) {
                moveLeft();
            }
            if (val == 2) {
                moveRight();
            }
            if (val == 3) {
                moveDown();
            }
            if (val == 4) {
                moveUp();
            }
        }
        timeFreeMove -= Gdx.graphics.getDeltaTime();
        if (timeFreeMove < 0) {
            timeFreeMove = 1;
        }

    }

    public void move(float x, float y) {
        if (Math.abs(x - getX()) > 5) {
            if (x - getX() > 0) {
                moveRight();
            }
            if (x - getX() < 0) {
                moveLeft();
            }
        }
        if (Math.abs(y - getY()) > 2 && Math.abs(x - getX()) < 5) {
            if (y - getY() > 0) {
                moveUp();
            }
            if (y - getY() < 0) {
                moveDown();
            }
        }
        if (Math.abs(y - getY()) < 5 && Math.abs(x - getX()) < 5) {
            nonMove();
        }
    }

    public void moveLeft() {
        body.setLinearVelocity(new Vector2(-speed, 0));
    }

    public void moveRight() {
        body.setLinearVelocity(new Vector2(speed, 0));
    }

    public void moveUp() {
        body.setLinearVelocity(new Vector2(0, speed));
    }

    public void moveDown() {
        body.setLinearVelocity(new Vector2(0, -speed));
    }

    public void nonMove() {
        body.setLinearVelocity(new Vector2(0, 0));
    }

    public void setSpeed() {


    }

    public float getX() {
        return (int) ((body.getPosition().x * PPM) / 16) * 16;
    }

    public float getY() {
        return (int) ((body.getPosition().y * PPM) / 16) * 16;
    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(DESTROYED_BIT);

        isHit = true;
    }

    public void isDead() {
        setCategoryFilter(DESTROYED_BIT);
        //getCell().setTile(null);
        body.destroyFixture(fixture);
    }
}
