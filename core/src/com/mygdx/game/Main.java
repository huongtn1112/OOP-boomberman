package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.PlayScreen;

import java.nio.channels.MulticastChannel;

public class Main extends Game {
    public static final int V_WIDTH = 420;  //420
    public static final int V_HEIGHT = 700;  //700


    public SpriteBatch batch;

    public static AssetManager manager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("sounds/gameMusic.mp3", Music.class);
        manager.load("sounds/gameFailed.mp3", Music.class);
        manager.load("sounds/bomb.mp3", Music.class);
        manager.finishLoading();

        setScreen(new PlayScreen(this, 1));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        this.getScreen().dispose();
    }
    public void restart(int levelNum){
        setScreen(new PlayScreen(this,levelNum));
        //this.dispose();
    }
}
