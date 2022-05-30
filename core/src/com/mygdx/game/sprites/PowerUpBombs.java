package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.screens.PlayScreen;

import static com.mygdx.game.Constants.DESTROYED_BIT;

public class PowerUpBombs extends Item{
    public PowerUpBombs(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        texture = new Texture(("Item/powerup_bombs.png"));
        setTexture(texture);
        //bdef.type = BodyDef.BodyType.KinematicBody;

    }
    @Override
    public void onHeadHit() {
        setCategoryFilter(DESTROYED_BIT);
        //getCell().setTile(null);
        screen.addCountBomb();
        isHit = true;
    }
}
