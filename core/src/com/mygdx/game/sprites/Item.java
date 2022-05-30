package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.screens.PlayScreen;

import static com.mygdx.game.Constants.ITEM_BIT;
import static com.mygdx.game.Constants.PPM;
import static com.mygdx.game.Constants.WALL_BIT;

public class Item extends InteractiveTileObject {
    protected Texture texture;
    protected Texture textureBox;
    protected PlayScreen screen;
    protected boolean isHit;
    protected float countTime;
    public Item(){};
    public Item(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        this.screen = screen;
        fixture.setUserData(this);
        setCategoryFilter(ITEM_BIT);
        isHit = false;
        textureBox = new Texture(("Item/box.png"));
       // texture = new Texture(("Item/powerup_speed.png"));
        countTime = 2f;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void render(SpriteBatch sb) {
        if(!isHit)
        {
            sb.begin();
            //sb.draw(texture, (int)(body.getPosition().x * PPM ), (int) (body.getPosition().y * PPM  ), 16/PPM, 16/PPM);
            sb.draw(textureBox, getTexX(), getTexY(), 8 / PPM, 8 / PPM);
            sb.end();
        }
        else {
            countTime -= Gdx.graphics.getDeltaTime();
            if(countTime > 0)
            {
                sb.begin();
                //sb.draw(texture, (int)(body.getPosition().x * PPM ), (int) (body.getPosition().y * PPM  ), 16/PPM, 16/PPM);
                sb.draw(texture, getTexX(), getTexY(), 8 / PPM, 8 / PPM);
                sb.end();
            }
        }
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Item", "Collision");
        //setCategoryFilter(DESTROYED_BIT);
        //getCell().setTile(null);
    }

    public float getTexX() {
        float x = (body.getPosition().x - 4 / PPM);
        return x;
    }

    public float getTexY() {
        float y = (body.getPosition().y - 4 / PPM);
        return y;
    }
}
