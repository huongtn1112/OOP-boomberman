package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.screens.PlayScreen;

import static com.mygdx.game.Constants.DESTROYED_BIT;

public class SpeedUp extends Item {

    public SpeedUp(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        this.screen = screen;
        texture = new Texture(("Item/powerup_speed.png"));
        setTexture(texture);
    }
    @Override
    public void onHeadHit() {
        setCategoryFilter(DESTROYED_BIT);
        //getCell().setTile(null);
        screen.addSpeed();
        isHit = true;
    }
}
