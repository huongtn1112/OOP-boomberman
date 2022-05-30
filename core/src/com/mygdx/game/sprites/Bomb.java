package com.mygdx.game.sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Main;
import com.mygdx.game.screens.PlayScreen;

import java.awt.Rectangle;

import static com.mygdx.game.Constants.BOOM_BIT;
import static com.mygdx.game.Constants.BOT_BIT;
import static com.mygdx.game.Constants.BRICK_BIT;
import static com.mygdx.game.Constants.DEFAULT_BIT;
import static com.mygdx.game.Constants.ITEM_BIT;
import static com.mygdx.game.Constants.PLAYER_BIT;
import static com.mygdx.game.Constants.PPM;
import static com.mygdx.game.Constants.WALL_BIT;

public class Bomb extends Sprite {
    public float countdown;
    public float power;
    public boolean isCreat;
    public boolean isBang;
    private World world;
    private Body b2body;
    private TextureRegion bombTextureRegion;


    public float posX;
    public float posY;

    private int leftHeight;
    public int rightHeight;
    private int upHeight;
    private int downHeight;
    private Animation bombStand;
    private float stateTimer;

    private PlayScreen screen;

    private Texture textureMid;
    private Texture textureLeft1;
    private Texture textureRight1;
    private Texture textureDown1;
    private Texture textureUp1;
    private Texture textureLeft2;
    private Texture textureRight2;
    private Texture textureDown2;
    private Texture textureUp2;

    public Bomb(PlayScreen screen) {
        super(screen.getBombAtlas().findRegion("boom4"));
        this.screen = screen;
        this.world = screen.getWorld();
        definePlayer();
        posX = -64;
        posY = -64;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 52, 1, 50, 50));
        }
        bombStand = new Animation(0.5f, frames);
        //bombTextureRegion = new TextureRegion(getTexture(), 50, 1, 50, 50);
        setBounds(0, 0, 14 / PPM, 14 / PPM);
        //setRegion(bombTextureRegion);

        leftHeight = 1;
        rightHeight = 1;
        upHeight = 1;
        downHeight = 1;
        isBang = true;
        countdown = 0;
        isCreat = false;

        textureMid = new Texture(("BoomBang/mid.png"));
        textureLeft1 = new Texture(("BoomBang/left1.png"));
        textureRight1 = new Texture(("BoomBang/right1.png"));
        textureUp1 = new Texture(("BoomBang/up1.png"));
        textureDown1 = new Texture(("BoomBang/down1.png"));

        textureLeft2 = new Texture(("BoomBang/left2.png"));
        textureRight2 = new Texture(("BoomBang/right2.png"));
        textureUp2 = new Texture(("BoomBang/up2.png"));
        textureDown2 = new Texture(("BoomBang/down2.png"));

    }

    public Bomb(PlayScreen screen, float x, float y) {
        super(screen.getAtlas().findRegion("down"));
        leftHeight = 1;
        rightHeight = 1;
        upHeight = 1;
        downHeight = 1;
        isBang = true;
        power = 1;

        this.posX = x;
        this.posY = y;
        this.world = screen.getWorld();
        definePlayer();
    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(posX / PPM, posY / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PPM);
//
        fdef.filter.categoryBits = BOOM_BIT;
        fdef.filter.maskBits = DEFAULT_BIT | WALL_BIT | BRICK_BIT | PLAYER_BIT | ITEM_BIT | BOT_BIT;
//
        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = (TextureRegion) bombStand.getKeyFrame(stateTimer, true);
        stateTimer = stateTimer + dt;
        return region;
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        countdown -= dt;
        if (countdown < 0 && !isBang) {
            b2body.setTransform(-16 / PPM, -16 / PPM, b2body.getAngle());
            float px = (posX * PPM - 8); // PosX
            float py = (posY * PPM - 8);
            screen.bombBang((int) px, (int) py, this);

            isDie((int) screen.getPlayerX(), (int) screen.getPlayerY());
            isBang = true;
            isCreat = false;
        }

    }

    public void setBang(boolean bang) {
        isBang = bang;
    }

    public void creatBoom(float x, float y) {

        if (countdown < 0 || countdown == 2) {
            isCreat = false;
            countdown = 2;
            isBang = false;
            posX = x;
            posY = y;
            b2body.setTransform(x, y, b2body.getAngle());
        }

    }

    public void renderBang(SpriteBatch batch) {
        float x = posX * PPM - 8;
        float y = posY * PPM - 8;
        float width = 16;
        float height = 16;
        if (countdown > -0.5 && countdown < 0) {
            Main.manager.get("sounds/bomb.mp3", Music.class).play();
            batch.draw(textureMid, x / PPM, y / PPM, width / PPM, height / PPM);
            for (int i = 1; i <= upHeight; i++) {
                if (i != upHeight) {
                    batch.draw(textureUp1, x / PPM, (y + 16 * i) / PPM, width / PPM, height / PPM);
                }
                if (i == upHeight) {
                    batch.draw(textureUp2, x / PPM, (y + 16 * i) / PPM, width / PPM, height / PPM);
                }

            }
            for (int i = 1; i <= downHeight; i++) {
                if (i != downHeight) {
                    batch.draw(textureDown1, x / PPM, (y - 16 * i) / PPM, width / PPM, height / PPM);
                }
                if (i == downHeight) {
                    batch.draw(textureDown2, x / PPM, (y - 16 * i) / PPM, width / PPM, height / PPM);
                }

            }
            for (int i = 1; i <= leftHeight; i++) {
                if (i != leftHeight) {
                    batch.draw(textureLeft1, (x - 16 * i) / PPM, y / PPM, width / PPM, height / PPM);
                }
                if (i == leftHeight) {
                    batch.draw(textureLeft2, (x - 16 * i) / PPM, y / PPM, width / PPM, height / PPM);
                }

            }
            for (int i = 1; i <= rightHeight; i++) {
                if (i != rightHeight) {
                    batch.draw(textureRight1, (x + 16 * i) / PPM, y / PPM, width / PPM, height / PPM);
                }
                if (i == rightHeight) {
                    batch.draw(textureRight2, (x + 16 * i) / PPM, y / PPM, width / PPM, height / PPM);
                }
            }
        }

    }

    public void isDie(int x, int y) {
        int Bx = (int) (posX * PPM - 8);
        int By = (int) (posY * PPM - 8);
        Rectangle r1 = new Rectangle(x, y, 16, 16);
        Rectangle r2 = new Rectangle(Bx, By - 16 * downHeight, 16, 16 + 16 * (downHeight + upHeight));
        Rectangle r3 = new Rectangle(Bx - 16 * leftHeight, By, 16 + 16 * (leftHeight + rightHeight), 16);
        if (r1.intersects(r2) || r1.intersects(r3)) {
            //what to happen when collision occurs goes here
            //System.out.println("die");
            // health - 1;
            screen.subHealth();
        } else {
            //System.out.println("alive");
        }
    }
    public boolean isDie2(int x, int y) {
        int Bx = (int) (posX * PPM - 8);
        int By = (int) (posY * PPM - 8);
        Rectangle r1 = new Rectangle(x, y, 16, 16);
        Rectangle r2 = new Rectangle(Bx, By - 16 * downHeight, 16, 16 + 16 * (downHeight + upHeight));
        Rectangle r3 = new Rectangle(Bx - 16 * leftHeight, By, 16 + 16 * (leftHeight + rightHeight), 16);
        return  (r1.intersects(r2) || r1.intersects(r3)) && countdown < -0.2f ;

    }


    public void setLeftHeight(int leftHeight) {
        this.leftHeight = leftHeight;
    }

    public void setRightHeight(int rightHeight) {
        this.rightHeight = rightHeight;
    }

    public void setUpHeight(int upHeight) {
        this.upHeight = upHeight;
    }

    public void setDownHeight(int downHeight) {
        this.downHeight = downHeight;
    }
}
