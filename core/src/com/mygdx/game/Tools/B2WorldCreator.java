package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.sprites.Bomb;
import com.mygdx.game.sprites.Brick;
import com.mygdx.game.sprites.Wall;

import java.util.HashMap;

import static com.mygdx.game.Constants.SIZE_MAP_LEVEL_1;

public class B2WorldCreator {
    private HashMap<Vector2, Brick> brickHashMap = new HashMap<>();
    private int[][] arr = new int[SIZE_MAP_LEVEL_1][SIZE_MAP_LEVEL_1];
    private int valueWall = -1;
    private int valueBick = 1;
    private int sizeMap = SIZE_MAP_LEVEL_1;
    private Texture texture;

    public B2WorldCreator(PlayScreen screen) {
        //
        TiledMap map = screen.getMap();
        World world = screen.getWorld();
        //BodyDef bdef = new BodyDef();
        //PolygonShape shape = new PolygonShape();
        // FixtureDef fdef = new FixtureDef();
        Body body;
        initializeArray();

        //create pipe bodies/fixtures
        for (MapObject object : map.getLayers().get("Wall").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            Wall wall = new Wall(screen, rect);
            int x = ((int) (rect.x)) / 16;
            int y = (int) (rect.y) / 16;
            //  System.out.println("Toa do ne " + rect.x * PPM + " " +  rect.y* PPM);
            pushObject(x, y, valueWall);
        }

        //create brick bodies/fixtures
        for (MapObject object : map.getLayers().get("Brick").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            Brick brick = new Brick(screen, rect);
            int x = ((int) (rect.x + 6)) / 16;
            int y = (int) (rect.y + 6) / 16;
            pushObject(x, y, valueBick);
            brickHashMap.put(new Vector2(x, y), brick);
            //System.out.println(new Vector2(x, y));

        }
        //printMap();


    }

    public static class InteractiveTileObject {
    }

    private void initializeArray() {
        int size = sizeMap;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                arr[i][j] = 0;
            }
        }
    }

    public void pushObject(int x, int y, int value) {
        int size = sizeMap;
        arr[y][x] = value;
    }

    public void printMap() {
        int size = sizeMap;
        for (int y = 0; y < size; y++) {
            int overturned = size - 1 - y; // Lat nguoc cho giong map

            for (int x = 0; x < size; x++) {
                if (arr[overturned][x] == 0) {
                    System.out.print("  ");
                } else if (arr[overturned][x] == valueWall) {
                    System.out.print("w ");
                } else if (arr[overturned][x] == valueBick) {
                    System.out.print("b ");
                }
                //System.out.print(arr[overturned][x] + " ");
            }
            System.out.println("");
        }
    }

    public int checkExplosiveRight(int x, int y, int power) {
        int length = -1; // -1 la khong cham
        for (int i = 1; i <= power; i++) {
            int x1 = x + i;
            int y1 = y;
            if (x1 > sizeMap) {
                length = i;
                break;
            }
            //System.out.print(arr[y1][x1] + " ");
            if (arr[y1][x1] != 0) {
                if (arr[y1][x1] == valueBick) {
                    arr[y1][x1] = 0;
                    // System.out.println("bang bick");
                    {
                        // remove brick
                        removeBrick(x1, y1);
                    }
                }
                length = i;
                break;
            }
        }
       // System.out.println(length);
        return length;
    }

    public int checkExplosiveLeft(int x, int y, int power) {
        int length = -1;
        for (int i = 1; i <= power; i++) {
            int x1 = x - i;
            int y1 = y;
            if (x1 < 0) {
                length = i;
                break;
            }
            // System.out.print(arr[y1][x1] + " ");
            if (arr[y1][x1] != 0) {
                if (arr[y1][x1] == valueBick) {
                    arr[y1][x1] = 0;
                    // System.out.println("bang bick");
                    {
                        // remove brick
                        removeBrick(x1, y1);
                    }
                }
                length = i;
                break;
            }
        }
        //System.out.println(length);
        return length;
    }

    public int checkExplosiveUp(int x, int y, int power) {
        int length = -1; // -1 la khong cham
        for (int i = 1; i <= power; i++) {
            int x1 = x;
            int y1 = y + i;
            if (y1 > sizeMap) {
                length = i;
                break;
            }
            //System.out.print(arr[y1][x1] + " ");
            if (arr[y1][x1] != 0) {
                if (arr[y1][x1] == valueBick) {
                    arr[y1][x1] = 0;
                    // System.out.println("bang bick");
                    {
                        // remove brick
                        removeBrick(x1, y1);
                    }
                }
                length = i;
                break;
            }
        }
        //System.out.println(length);
        return length;
    }

    public int checkExplosiveDown(int x, int y, int power) {
        int length = -1; // -1 la khong cham
        for (int i = 1; i <= power; i++) {
            int x1 = x;
            int y1 = y - i;
            if (y1 < 0) {
                length = i;
                break;
            }
            //  System.out.print(arr[y1][x1] + " ");
            if (arr[y1][x1] != 0) {
                if (arr[y1][x1] == valueBick) {
                    arr[y1][x1] = 0;
                    // System.out.println("bang bick");
                    {
                        // remove brick
                        removeBrick(x1, y1);
                    }
                }
                length = i;
                break;
            }
        }
        System.out.println(length);
        return length;
    }

    public void removeBrick(int x, int y) {
        if (!brickHashMap.containsKey(new Vector2(x, y))) {
            return;
        }
        brickHashMap.get(new Vector2(x, y)).onHeadHit();
        brickHashMap.remove(new Vector2(x, y));
    }

    public void explosive(int x, int y, int power) {
        int left, right, up, down;
        left = checkExplosiveLeft(x, y, power);
        right = checkExplosiveRight(x, y, power);
        up = checkExplosiveUp(x, y, power);
        down = checkExplosiveDown(x, y, power);

    }

    public void explosive(int x, int y, int power, Bomb bomb) {
        int left, right, up, down;

        left = checkExplosiveLeft(x, y, power);
        right = checkExplosiveRight(x, y, power);
        up = checkExplosiveUp(x, y, power);
        down = checkExplosiveDown(x, y, power);
        System.out.println(left + " " + right);
        if (down == -1) {
            bomb.setDownHeight(power);
        } else {
            bomb.setDownHeight(down - 1);
        }
        if (up == -1) {
            bomb.setUpHeight(power);
        } else {
            bomb.setUpHeight(up - 1);
        }
        if (right == -1) {
            bomb.setRightHeight(power);
        } else {
            bomb.setRightHeight(right - 1);
        }
        if (left == -1) {
            bomb.setLeftHeight(power);
        } else {
            bomb.setLeftHeight(left - 1);
        }


    }
}
