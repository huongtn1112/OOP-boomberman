package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.game.screens.PlayScreen;

import static com.mygdx.game.Constants.BOT_BIT;
import static com.mygdx.game.Constants.PPM;

public class Enemy2 extends Balloom{
    public Enemy2(PlayScreen screen, Rectangle bounds) {
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
        texture = new Texture(("Item/oneal.png"));
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
        if (px != 0f && py != 0) {
            catchPlayer(px, py);
        }

    }

}
