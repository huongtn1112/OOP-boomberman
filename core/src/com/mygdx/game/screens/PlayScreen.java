package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Main;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.sprites.Balloom;
import com.mygdx.game.sprites.Bomb;
import com.mygdx.game.sprites.Coin;
import com.mygdx.game.sprites.Enemy2;
import com.mygdx.game.sprites.Enemy3;
import com.mygdx.game.sprites.Player;
import com.mygdx.game.sprites.Portal;
import com.mygdx.game.sprites.PowerUp;
import com.mygdx.game.sprites.PowerUpBombs;
import com.mygdx.game.sprites.SpeedUp;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.mygdx.game.Constants.DEFAULT_ZOOM;
import static com.mygdx.game.Constants.GRAVITY;
import static com.mygdx.game.Constants.MAP_NAME1;
import static com.mygdx.game.Constants.MAP_NAME2;
import static com.mygdx.game.Constants.PPM;


public class PlayScreen implements Screen {
    // Constants
    private static final int MOVE_NONE = 0;
    private static final int MOVE_UP = 1;
    private static final int MOVE_DOWN = 2;
    private static final int MOVE_LEFT = 3;
    private static final int MOVE_RIGHT = 4;
    private static final float CAMERA_ZOOM = 0.01f;
    private static final float SCALE = 2.0f;
    private final Main game;
    private final OrthographicCamera gamecam;
    private final Viewport gamePort;
    private final Hud hud;
    //load map
    private final TmxMapLoader mapLoader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    //
    private final float viewportWidth;
    private final float viewportHeight;
    //Box2d var
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final Player player;
    public SpriteBatch batch;
    //atlas
    private final TextureAtlas atlas;
    private final TextureAtlas bombAtlas;
    //
    private WorldContactListener contactListener;
    public B2WorldCreator b2WorldCreator;

    //
    private int power;
    private float time;
    private int countBomb;
    private float speed;
    private int health;
    //

    private int mDirection = MOVE_NONE;
    //Boom
    Bomb bomb;
    Bomb bomb2;
    Bomb bomb3;
    // items
    private Array<PowerUpBombs> poweruUpBooms;
    private Array<SpeedUp> speedUps;
    private Array<PowerUp> powerUps;
    private Array<Portal> portals;
    private Array<Coin> coins;
    private Array<Balloom> ballooms;
    private Array<Enemy2> enemy2s;
    private Array<Enemy3> enemy3s;

    private int sumEnemy;
    //
    private boolean destroyAll;

    //
    private boolean isGameOver;
    private boolean isGameWin;

    // Music
    private Music music;

    private int level;

    public PlayScreen(Main game, int level) {
        this.level = level;
        isGameWin = false;
        isGameOver = false;
        destroyAll = false;

        health = 3;
        power = 1;
        countBomb = 1;
        speed = 1f;

        atlas = new TextureAtlas("Player1.pack");
        bombAtlas = new TextureAtlas("Bomb.pack");
        this.game = game;
        gamecam = new OrthographicCamera();
        gamecam.zoom = DEFAULT_ZOOM;
        gamePort = new FitViewport(Main.V_WIDTH / PPM, Main.V_HEIGHT / PPM, gamecam);
        hud = new Hud(game.batch);
        //
        mapLoader = new TmxMapLoader();
        if(level == 1)
        {
            map = mapLoader.load(MAP_NAME1);
        } else  {
            map = mapLoader.load(MAP_NAME2);
        }

        //
        viewportWidth = gamePort.getWorldWidth();
        viewportHeight = gamePort.getWorldHeight();

        //
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(GRAVITY, true);
        b2dr = new Box2DDebugRenderer();

        batch = game.batch;

        //create ground bodies/fixtures

        b2WorldCreator = new B2WorldCreator(this);
        //create coin bodies/fixtures
        player = new Player(this);
        //


        createItem();

        bomb = new Bomb(this);
        bomb2 = new Bomb(this);
        bomb3 = new Bomb(this);

        //check collision player vs map
        contactListener = new WorldContactListener();
        world.setContactListener(contactListener);

        music = Main.manager.get("sounds/gameMusic.mp3", Music.class);
        music.setLooping(true);
        music.play();
    }


    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TextureAtlas getBombAtlas() {
        return bombAtlas;
    }

    public void handleInput(float dt) {
        float x;
        float y;
        x = player.b2body.getPosition().x * PPM;
        y = player.b2body.getPosition().y * PPM;
        x = (int) (x / 16) * 16 + 8;
        y = (int) (y / 16) * 16 + 8;
        //camera zoom
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            gamecam.zoom -= CAMERA_ZOOM;
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            gamecam.zoom += CAMERA_ZOOM;
        }
        // Exit
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            game.restart(2);

        }
        //control our player using immediate impulses
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            mDirection = MOVE_UP;
            //System.out.println(boomCheck.right);

        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            mDirection = MOVE_DOWN;

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mDirection = MOVE_LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mDirection = MOVE_RIGHT;
        } else {
            mDirection = MOVE_NONE;
        }
        //
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {

            for (int i = 1; i <= 3; i++) {
                if (i == 1 && !bomb.isCreat) {
                    bomb.isCreat = true;
                    bomb.creatBoom(x / PPM, y / PPM);
                    break;
                }

                if (i == 2 && !bomb2.isCreat && countBomb >= 2) {
                    bomb2.isCreat = true;
                    bomb2.creatBoom(x / PPM, y / PPM);
                    break;
                }
                if (i == 3 && !bomb3.isCreat && countBomb >= 3) {
                    bomb.isCreat = true;
                    bomb3.creatBoom(x / PPM, y / PPM);
                    break;
                }
            }


        }

    }

    public boolean isDestroyAll() {
        return destroyAll;
    }

    public void processInput(float dt) {
        //body->SetLinearVelocity(b2Vec2(3, 4));
        if (mDirection == MOVE_LEFT) {
            player.b2body.setLinearVelocity(-speed, 0);
        } else if (mDirection == MOVE_RIGHT) {
            player.b2body.setLinearVelocity(speed, 0);
        } else if (mDirection == MOVE_DOWN) {
            player.b2body.setLinearVelocity(0, -speed);
        } else if (mDirection == MOVE_UP) {
            player.b2body.setLinearVelocity(0, speed);
        }
        player.b2body.setLinearDamping(10 * speed);

    }

    public void update(float dt) {
        handleInput(dt);
        processInput(dt);
        world.step(1 / 60f, 6, 2);
        //update pos player
        player.update(dt);

        bomb.update(dt);
        bomb2.update(dt);
        bomb3.update(dt);
        sumEnemy = ballooms.size;
        if(sumEnemy == 0)
        {
            destroyAll = true;

        }
        for(int i=0; i<ballooms.size; i++)
        {
            ballooms.get(i).setPlayerPos(getPlayerX(), getPlayerY());
           if(bomb.isDie2((int)ballooms.get(i).getX(),(int)ballooms.get(i).getY())){
               ballooms.get(i).isDead();
               ballooms.removeIndex(i);
               i--;
           }

        }
        for(int i=0; i<enemy2s.size; i++)
        {
            enemy2s.get(i).setPlayerPos(getPlayerX(), getPlayerY());
            if(bomb.isDie2((int)enemy2s.get(i).getX(),(int)enemy2s.get(i).getY())){
                enemy2s.get(i).isDead();
                enemy2s.removeIndex(i);
                i--;
            }

        }
        for(int i=0; i<enemy3s.size; i++)
        {
            enemy3s.get(i).setPlayerPos(getPlayerX(), getPlayerY());
            if(bomb.isDie2((int)enemy3s.get(i).getX(),(int)enemy3s.get(i).getY())){
                enemy3s.get(i).isDead();
                enemy3s.removeIndex(i);
                i--;
            }

        }
     //   System.out.println(getPlayerX() + " player " + getPlayerY());
        renderer.setView(gamecam);
        cameraUpdate(dt);
        // Time update
        hud.update(dt);


    }

    // get pos of player
    public float getPlayerX() {
        float x = player.b2body.getPosition().x * PPM;
        // float y = player.b2body.getPosition().y * PPM;
        x = (int) (x / 16) * 16;
        //y = (int) (y / 16) * 16 ;
        return x;
    }

    public float getPlayerY() {
        //float x = player.b2body.getPosition().x * PPM;
        float y = player.b2body.getPosition().y * PPM;
        // x = (int) (x / 16) * 16 ;
        y = (int) (y / 16) * 16;
        return y;
    }

    //update camera
    public void cameraUpdate(float dt) {
        Vector3 pos = gamecam.position;
        pos.x = player.b2body.getPosition().x;
        pos.y = player.b2body.getPosition().y;
        gamecam.position.set(pos);
        gamecam.update();
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void show() {

    }

    public WorldContactListener getContactListener() {
        return contactListener;
    }

    public float getTime() {
        return time;
    }

    @Override
    public void render(float delta) {
        //System.out.println(hud.getWorldTimer());
        time += Gdx.graphics.getDeltaTime();
        //
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(Gdx.graphics.getDeltaTime());
        renderer.render();
        //
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        //

        player.draw(game.batch);


        bomb.draw(game.batch);
        bomb.renderBang(game.batch);

        bomb2.draw(game.batch);
        bomb2.renderBang(game.batch);

        bomb3.draw(game.batch);
        bomb3.renderBang(game.batch);
        //
        //boom.draw(game.batch);


        //
        game.batch.end();
        //draw items
        for (int i = 0; i < powerUps.size; i++) {
            powerUps.get(i).render(game.batch);
        }
        for (int i = 0; i < poweruUpBooms.size; i++) {
            poweruUpBooms.get(i).render(game.batch);
        }
        for (int i = 0; i < speedUps.size; i++) {
            speedUps.get(i).render(game.batch);
        }
        for (int i = 0; i < portals.size; i++) {
            portals.get(i).render(game.batch);
        }
        for (int i = 0; i < coins.size; i++) {
            coins.get(i).render(game.batch);
        }

        for (int i = 0; i < ballooms.size; i++) {
            ballooms.get(i).render(game.batch);
        }
        for (int i = 0; i < enemy2s.size; i++) {
            enemy2s.get(i).render(game.batch);
        }
        for (int i = 0; i < enemy3s.size; i++) {
            enemy3s.get(i).render(game.batch);
        }
        //
        hud.stage.draw();
        b2dr.render(world, gamecam.combined);

        // game over
        isGameOver = Hud.getHealth() == 0 || player.isDie;
        if (isGameOver) {
            if(Hud.getHealth() > 0){
                game.restart(level);
                hud.subHealth();
            } else {
                game.setScreen(new GameOverScreen(game));
                dispose();
                Main.manager.get("sounds/gameMusic.mp3", Music.class).stop();
                Main.manager.get("sounds/gameFailed.mp3", Music.class).play();
            }

        }

        // game win
        if (isGameWin) {
            if(level == 2)
            {
                game.setScreen(new GameWinScreen(game));
                Main.manager.get("sounds/gameMusic.mp3", Music.class).stop();
            }


            game.restart(level+1);
            bomb.isCreat = false;
            bomb2.isCreat = false;
            bomb3.isCreat = false;
            bomb.isBang = false;
            bomb2.isBang = false;
            bomb3.isBang = false;
           // Main.manager.get("sounds/uiGo.mp3", Music.class).play();
        }
    }

    public void addCountBomb() {
        this.countBomb++;
        if(countBomb <= 3)
        {
            hud.addBoom();
        }

    }
    public void addSpeed(){
        speed+=0.1f;
    }

    public void bombBang(int px, int py, Bomb bomb) {
        px /= 16;
        py /= 16;
        b2WorldCreator.explosive((int) px, (int) py, power, bomb);
    }
    public void subHealth()
    {
        health --;
        hud.subHealth();
    }
    public void addMark()
    {
        Hud.addScore(1);
    }

    public void addPower() {
        this.power ++;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }

    private void createItem() {

        // create list of crystals
        powerUps = new Array<PowerUp>();
        poweruUpBooms = new Array<PowerUpBombs>();
        speedUps = new Array<SpeedUp>();
        portals = new Array<Portal>();
        coins = new Array<>();
        ballooms = new Array<>();
        enemy2s = new Array<>();
        enemy3s = new Array<>();

        // get all crystals in "crystals" layer,
        // create bodies for each, and add them
        // to the crystals list
        for (MapObject object : map.getLayers().get("Power").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            PowerUp powerUp = new PowerUp(this, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            powerUps.add(powerUp);
            //System.out.println(new Vector2(x, y));
        }
        for (MapObject object : map.getLayers().get("Booms").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            PowerUpBombs powerUpBombs = new PowerUpBombs(this, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            poweruUpBooms.add(powerUpBombs);
            //System.out.println(new Vector2(x, y));
        }
        for (MapObject object : map.getLayers().get("Speeds").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            SpeedUp speedUp = new SpeedUp(this, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            speedUps.add(speedUp);
            //System.out.println(new Vector2(x, y));
        }
        for (MapObject object : map.getLayers().get("Portal").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            Portal portal = new Portal(this, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            portals.add(portal);
            //System.out.println(new Vector2(x, y));
        }
        for (MapObject object : map.getLayers().get("Coin").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            Coin coin = new Coin(this, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            coins.add(coin);
            //System.out.println(new Vector2(x, y));
        }

        for (MapObject object : map.getLayers().get("Balloom").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            Balloom balloom = new Balloom(this, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            ballooms.add(balloom);
            //System.out.println(new Vector2(x, y));
        }
        for (MapObject object : map.getLayers().get("Enemy2").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            Enemy2 enemy2 = new Enemy2(this, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            enemy2s.add(enemy2);
            //System.out.println(new Vector2(x, y));
        }
        for (MapObject object : map.getLayers().get("Enemy3").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            Enemy3 enemy3 = new Enemy3(this, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            enemy3s.add(enemy3);
            //System.out.println(new Vector2(x, y));
        }

    }
    public boolean gameWin(boolean isGameWin)
    {
        System.out.println("Win");
        return this.isGameWin = isGameWin;
    }


    public boolean gameOver()
    {
        return health == 0;
    }

}
