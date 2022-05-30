package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.screens.PlayScreen;

import static com.mygdx.game.Constants.PPM;

public class Portal extends Item {
    public Portal(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        texture = new Texture(("Item/portal.png"));
        setTexture(texture);
    }

    @Override
    public void onHeadHit() {
        //setCategoryFilter(DESTROYED_BIT);
        //getCell().setTile(null);
        if(!isHit)
        {
            isHit = true;
        }
        if (screen.isDestroyAll()) {

            screen.gameWin(true);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!isHit) {
            sb.begin();
            //sb.draw(texture, (int)(body.getPosition().x * PPM ), (int) (body.getPosition().y * PPM  ), 16/PPM, 16/PPM);
            sb.draw(textureBox, getTexX(), getTexY(), 8 / PPM, 8 / PPM);
            sb.end();
        } else {

            sb.begin();
            //sb.draw(texture, (int)(body.getPosition().x * PPM ), (int) (body.getPosition().y * PPM  ), 16/PPM, 16/PPM);
            sb.draw(texture, getTexX(), getTexY(), 8 / PPM, 8 / PPM);
            sb.end();

        }
    }
}
