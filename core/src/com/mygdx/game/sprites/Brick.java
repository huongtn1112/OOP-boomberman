package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.PlayScreen;

import java.util.HashMap;

import static com.mygdx.game.Constants.BRICK_BIT;
import static com.mygdx.game.Constants.DESTROYED_BIT;
import static com.mygdx.game.Constants.PPM;


public class Brick extends InteractiveTileObject {
    private PlayScreen screen;
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(BRICK_BIT);

    }

    @Override
    public void onHeadHit() {
        System.out.println(body.getPosition());
        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(DESTROYED_BIT);
        getCell().setTile(null);
        body.destroyFixture(fixture);
        getCell().setTile(map.getTileSets().getTile(99));
    }
    public int getX()
    {
        int x = (int) ( body.getPosition().x * PPM);
        x = (x+4)/16;
        return x;
    }
    public int getY()
    {
        int y = (int) ( body.getPosition().y * PPM);
        y = (y+4)/16;
        return y;
    }


}
