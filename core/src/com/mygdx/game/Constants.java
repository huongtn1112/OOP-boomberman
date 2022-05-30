package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Constants {
    private Constants() {
    }

    public static final float PPM = 100;
    //public static final float SPEED = 1;
    public static final String MAP_NAME1 = "map2.tmx";
    public static final String MAP_NAME2 = "map3.tmx";
    public static final float DEFAULT_ZOOM = 0.3f;
    public static final Vector2 GRAVITY = new Vector2(0f, 0f);

    public static final short DEFAULT_BIT = 1;
    public static final short BOOM_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short WALL_BIT = 8;
    public static final short ITEM_BIT = 16;
    public static final short PLAYER_BIT = 32;
    public static final short BOT_BIT = 64;
    public static final short DESTROYED_BIT = 128;

    public static final int SIZE_MAP_LEVEL_1 = 30;
}
