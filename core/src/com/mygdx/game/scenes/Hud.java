package com.mygdx.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.mygdx.game.Main.V_HEIGHT;
import static com.mygdx.game.Main.V_WIDTH;

public class Hud implements Disposable {

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private final Viewport viewport;

    //Mario score/time Tracking Variables
    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    public static float currentTime;
    private static int boom;
    private static Integer score;
    private static Integer health;
    //Scene2D widgets
    private final Label countdownLabel;
    private static Label scoreLabel;
    private final Label timeLabel;
    private final Label levelLabel;
    private final Label worldLabel;
    private final Label marioLabel;
    private final Label boomCount;
    private static  Label healthLable;

    public Hud(SpriteBatch sb) {
        //define our tracking variables
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        boom = 1;
        currentTime = 0;
        health = 3;
        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label("Mark: " + String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.RED));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("BOOMER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        boomCount = new Label("BOOM: " + String.format("%02d", boom) , new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        healthLable = new Label(String.format("%01d", health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(healthLable);

        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        //
        table.row();
        table.add(boomCount).expandX().padTop(10);
        table.add(scoreLabel).expandX();


        //add our table to the stage
        stage.addActor(table);

    }
    public static void subHealth()
    {
        health --;
        healthLable.setText(String.format("%01d", health));
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            currentTime ++;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static Integer getHealth() {
        return health;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public Integer getWorldTimer() {
        return worldTimer;
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText("Mark: " + String.format("%03d", score));
    }
    public  void addBoom(){
        boom++;
        boomCount.setText("BOOM: " + String.format("%01d", boom));
    }
    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }
}
