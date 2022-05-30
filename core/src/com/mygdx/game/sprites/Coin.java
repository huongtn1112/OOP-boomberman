package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.screens.PlayScreen;

import static com.mygdx.game.Constants.DESTROYED_BIT;

public class Coin extends Item {

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        this.screen = screen;
        texture = new Texture(("Item/coin.png"));
        setTexture(texture);
    }
    @Override
    public void onHeadHit() {
        setCategoryFilter(DESTROYED_BIT);
        //body.destroyFixture(fixture);
        //getCell().setTile(null);
        screen.addMark();
        isHit = true;
    }
}
