package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.screens.PlayScreen;

import static com.mygdx.game.Constants.BRICK_BIT;
import static com.mygdx.game.Constants.DESTROYED_BIT;
import static com.mygdx.game.Constants.WALL_BIT;

public class Wall extends InteractiveTileObject{
    public Wall(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(WALL_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Wall", "Collision");
        //setCategoryFilter(DESTROYED_BIT);
        //getCell().setTile(null);
    }
}

