package uysal.okan.ballcage.screens.endlessgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

import uysal.okan.ballcage.BallCage;
import uysal.okan.ballcage.assets.AssetDescriptors;
import uysal.okan.ballcage.assets.RegionNames;
import uysal.okan.ballcage.config.GameConfig;
import uysal.okan.ballcage.objects.Ball;
import uysal.okan.ballcage.objects.Block;
import uysal.okan.ballcage.objects.Floor;
import uysal.okan.ballcage.objects.Star;
import uysal.okan.ballcage.util.GdxUtils;

public class EndlessGameController {

    private final BallCage game;
    private final AssetManager assetManager;

    private boolean gameOver = false;

    private TextureAtlas bgAtlas;
    private TextureAtlas blockAtlas;
    private TextureAtlas floorAtlas;
    private TextureAtlas ballAtlas;

    private boolean bluefloor = false;
    private boolean addBlock = false;
    private boolean addBlock2 = false;
    private boolean addBlock3 = false;
    private boolean greenfloor = false;
    private boolean yellowfloor = false;

    public int score = 0;

    private Random random = new Random();

    private World world;

    public EndlessGameController(BallCage game) {
        this.game = game;
        assetManager = game.getAssetManager();
        init();
    }

    private void init() {
        world = new World(new Vector2(0, 0f),true);

        bgAtlas = assetManager.get(AssetDescriptors.BG);
        blockAtlas = assetManager.get(AssetDescriptors.BLOCKS);
        floorAtlas = assetManager.get(AssetDescriptors.FLOORS);
        ballAtlas = assetManager.get(AssetDescriptors.BALLS);

    }

    public void update(float delta) {
        if(!gameOver)
            world.step(delta, 6, 2);
    }

    public void controlBalls(ArrayList<Ball> balls, ArrayList<Floor> floors, ArrayList<Star> stars) {
        for (int j = 0; j < floors.size(); j++) {
            ArrayList<Integer> findBalls = new ArrayList<Integer>();
            boolean findNegaive = false;
            for ( int i = 0; i < balls.size(); i++) {
                Circle circle = new Circle(balls.get(i).sprite.getX() + balls.get(i).sprite.getWidth() / 2,
                        balls.get(i).sprite.getY() + balls.get(i).sprite.getHeight() / 2, balls.get(i).sprite.getHeight() / 2);
                Rectangle rectangle = new Rectangle(floors.get(j).sprite.getX() + 20, floors.get(j).sprite.getY() + 20,
                        floors.get(j).sprite.getWidth() - 40,
                        floors.get(j).sprite.getHeight() - 40);
                if (Intersector.overlaps(circle, rectangle)) {
                    findBalls.add(i);
                    if (balls.get(i).color != floors.get(j).color) {
                        findNegaive = true;
                        break;
                    }
                }
            }
            if(!findNegaive) {
                for(int i = findBalls.size() - 1; i >= 0; i--) {
                    Sprite sprite = new Sprite(ballAtlas.findRegion(RegionNames.STAR));
                    sprite.setScale(3 /4f);
                    sprite.setPosition(balls.get(findBalls.get(i)).sprite.getX(), balls.get(findBalls.get(i)).sprite.getY());
                    stars.add(new Star(sprite));
                    world.destroyBody(balls.get(findBalls.get(i)).body);
                    int a = findBalls.get(i);
                    balls.remove(a);
                    controlBallCount(balls);
                }
            }
        }
    }

    public boolean controlGameOver(ArrayList<Ball> balls, ArrayList<Floor> floors) {

        for(int i = 0; i < balls.size(); i++) {
            if(balls.get(i).color == 4) {
                for (int j = 0; j < floors.size(); j++) {

                    Circle circle = new Circle(balls.get(i).sprite.getX() + balls.get(i).sprite.getWidth() / 2,
                            balls.get(i).sprite.getY() + balls.get(i).sprite.getHeight() / 2, balls.get(i).sprite.getHeight() / 2);
                    Rectangle rectangle = new Rectangle(floors.get(j).sprite.getX() + 20, floors.get(j).sprite.getY() + 20,
                            floors.get(j).sprite.getWidth() - 40,
                            floors.get(j).sprite.getHeight() - 40);
                    if (Intersector.overlaps(circle, rectangle)) {
                        if(TimeUtils.millis() - GdxUtils.adsTime > 120000) {
                            game.getAdController().showInterstitial();
                            GdxUtils.adsTime = TimeUtils.millis();
                        }
                        gameOver = true;
                        return true;
                    }

                    // if(balls.get(i).sprite.)
                }
            }
        }
        return false;
    }


    public Sprite getBackgroundSprite() {
        Sprite sprite = new Sprite(bgAtlas.findRegion(RegionNames.BG));
        sprite.setPosition(0, 0);
        return sprite;
    }

    public ArrayList<Block> getGameBloks() {
        int stageConfig[] = new int[]{1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1};
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(int i = 0; i < stageConfig.length; i++) {
            if(stageConfig[i] > 0) {
                if(i - 3 < 0) {
                    float x = 30 * ((i % 3) + 1) + (i % 3) * 200 + 100;
                    float y = GameConfig.HUD_HEIGHT - 275;
                    blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                            x, y,0f, 0f, 0f, 0, false, BodyDef.BodyType.DynamicBody));
                } else if( i - 3 < 6) {
                    if(stageConfig[i - 3] <= 0) {
                        float x = 30 * ((i % 3) + 1) + (i % 3) * 200 + 100;
                        float y = GameConfig.HUD_HEIGHT - (260 * ((i / 3) )) - 15;
                        blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                                x, y,0f, 0f, 0f, 0, false, BodyDef.BodyType.DynamicBody));
                    }
                    float x = 30 * ((i % 3) + 1) + (i % 3) * 200 + 100;
                    float y = GameConfig.HUD_HEIGHT - (260 * ((i / 3) + 1)) - 15;
                    blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                            x, y,0f, 0f, 0f, 0, false, BodyDef.BodyType.DynamicBody));
                }

                else {
                    if(stageConfig[i - 3] <= 0) {
                        float x = 30 * ((i % 3) + 1) + (i % 3) * 200 + 100;
                        float y = GameConfig.HUD_HEIGHT - (260 * ((i / 3) )) - 15;
                        blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                                x, y,0f, 0f, 0f, 0, false, BodyDef.BodyType.DynamicBody));
                    }
                }

                if(i % 3 == 0) {
                    float y = GameConfig.HUD_HEIGHT - (30 * ((i / 3) + 1) + (i / 3) * 230 + 115);
                    float x = (230 * ((i % 3) + 1)) + 15;
                    blocks.add(addBlock(blockAtlas, RegionNames.BLOCK, 100000,
                            x, y,0f, 0f, 0f, 0, true, BodyDef.BodyType.DynamicBody));
                } else if (i % 3 == 1) {
                    if( stageConfig[i - 1] <= 0) {
                        float y = GameConfig.HUD_HEIGHT - (30 * ((i / 3) + 1) + (i / 3) * 230 + 115);
                        float x = (230 * ((i % 3))) + 15;
                        blocks.add(addBlock(blockAtlas, RegionNames.BLOCK, 100000,
                                x, y,0f, 0f, 0f, 0, true, BodyDef.BodyType.DynamicBody));
                    }
                    float y = GameConfig.HUD_HEIGHT - (30 * ((i / 3) + 1) + (i / 3) * 230 + 115);
                    float x = (230 * ((i % 3) + 1)) + 15;
                    blocks.add(addBlock(blockAtlas, RegionNames.BLOCK, 100000,
                            x, y,0f, 0f, 0f, 0, true, BodyDef.BodyType.DynamicBody));
                } else {
                    if( stageConfig[i - 1] <= 0) {
                        float y = GameConfig.HUD_HEIGHT - (30 * ((i / 3) + 1) + (i / 3) * 230 + 115);
                        float x = (230 * ((i % 3))) + 15;
                        blocks.add(addBlock(blockAtlas, RegionNames.BLOCK, 100000,
                                x, y,0f, 0f, 0f, 0, true, BodyDef.BodyType.DynamicBody));
                    }
                }
            }
            if( stageConfig[i] == -1) {
                float x = 30 * ((i % 3) + 1) + (i % 3) * 200 + 100;
                float y = GameConfig.HUD_HEIGHT - (30 * ((i / 3) + 1) + (i / 3) * 230 + 115);
                blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                        x, y,0f, 0f, 5f, 0, true, BodyDef.BodyType.KinematicBody));
            }
        }

        return blocks;
    }

    public ArrayList<Ball> getGameBalls() {
        float stageConfig[][] = new float[][] { {0, 5, 3, 1, -1.5f}, {0, 5, 5, -3, -2.5f}, {0, 5, 11, -1, -1.25f}};
        ArrayList<Ball> balls = new ArrayList<Ball>();

        for(int i = 0; i < stageConfig.length; i++) {
            balls.add(addBall(ballAtlas, (int)stageConfig[i][0], stageConfig[i][1] * 60,
                    GameConfig.HUD_HEIGHT - stageConfig[i][2] * 60, stageConfig[i][3], stageConfig[i][4]));
        }

        return  balls;
    }

    public void controlBallCount(ArrayList<Ball> balls) {
        int blackBall = balls.size() / 5;
        int countBlackBall = 0;
        for( int i = 0; i < balls.size(); i++) {
            if(balls.get(i).color == 4)
                countBlackBall++;
        }
        if(countBlackBall < blackBall) {
            int x = random.nextInt(4) + 4;
            int y = random.nextInt(6) + 6;
            float movX = (random.nextInt(600) - 300) / 100.0f;
            float movY = (random.nextInt(600) - 300) / 100.0f;
            balls.add(addBall(ballAtlas, 4, x * 60,
                    GameConfig.HUD_HEIGHT - y * 60, movX, movY));
        }

        if(score < 5 ) {
            while(balls.size() < 3) {
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, 0, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if(score < 10) {
            while(balls.size() < 4) {
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, 0, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if(score < 20) {
            while(balls.size() < 5) {
                int color = random.nextInt(2);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if(score < 30) {
            while(balls.size() < score / 7 + 3) {
                int color = random.nextInt(2);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if( score < 50) {
            while(balls.size() < score / 7 + 3) {
                int color = random.nextInt(3);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if(score < 75) {
            while(balls.size() < score / 7 + 3) {
                int color = random.nextInt(4);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if(score < 100) {
            while(balls.size() < score / 7 + 3) {
                int color = random.nextInt(4);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if(score < 150) {
            while(balls.size() < score / 7 + 3) {
                int color = random.nextInt(4);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if(score < 200) {
            while(balls.size() < score / 7 + 3) {
                int color = random.nextInt(4);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else if(score < 250) {
            while(balls.size() < score / 7 + 3) {
                int color = random.nextInt(4);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        } else {
            while(balls.size() < score / 7 + 3) {
                int color = random.nextInt(4);
                int x = random.nextInt(4) + 4;
                int y = random.nextInt(6) + 6;
                float movX = (random.nextInt(600) - 300) / 100.0f;
                float movY = (random.nextInt(600) - 300) / 100.0f;
                balls.add(addBall(ballAtlas, color, x * 60,
                        GameConfig.HUD_HEIGHT - y * 60, movX, movY));
            }
        }

    }

    public void changeFloors(ArrayList<Floor> floors, ArrayList<Block> blocks) {
        if(!bluefloor && score >= 10) {
            floors.get(1).sprite.setRegion(floorAtlas.findRegion(RegionNames.BLUEFLOOR));
            floors.get(1).color = 1;
            floors.get(2).sprite.setRegion(floorAtlas.findRegion(RegionNames.BLUEFLOOR));
            floors.get(2).color = 1;
            bluefloor = true;
        } else if(!greenfloor && score >= 20) {
            floors.get(0).sprite.setRegion(floorAtlas.findRegion(RegionNames.GREENFLOOR));
            floors.get(0).color = 2;;
            greenfloor = true;
        } else if(!addBlock && score >= 40) {
            float x = 30 * ((4 % 3) + 1) + (4 % 3) * 200 + 100;
            float y = GameConfig.HUD_HEIGHT - (30 * ((4 / 3) + 1) + (4 / 3) * 230 + 115);
            blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                    x, y,0f, 0f, 3f, 0, true, BodyDef.BodyType.KinematicBody));
            addBlock = true;
        } else if(!addBlock2 && score >= 80) {
            float x = 30 * ((7 % 3) + 1) + (7 % 3) * 200 + 100;
            float y = GameConfig.HUD_HEIGHT - (30 * ((7 / 3) + 1) + (7 / 3) * 230 + 115);
            blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                    x, y,0f, 0f, 3f, 0, true, BodyDef.BodyType.KinematicBody));
            addBlock2 = true;
        } else if(!yellowfloor && score >= 50) {
            floors.get(1).sprite.setRegion(floorAtlas.findRegion(RegionNames.YELLOWFLOOR));
            floors.get(1).color = 3;;
            yellowfloor = true;
        }
    }

    public ArrayList<Floor> getGameFloors() {
        int stageConfig[] = new int[]{1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1};
        ArrayList<Floor> sprites = new ArrayList<Floor>();

        for(int i = 0; i < stageConfig.length; i++) {
            if(stageConfig[i] > 0) {
                Sprite sprite = new Sprite(floorAtlas.findRegion(RegionNames.FLOORARRAY[stageConfig[i] - 1]));
                sprite.setPosition(30 * ((i % 3) + 1) + (i % 3) * 200 + 10, GameConfig.HUD_HEIGHT - (260 * ((i / 3) + 1) - 10));
                sprites.add(new Floor(sprite, stageConfig[i] - 1));
            }
        }
        return sprites;
    }

    public ArrayList<Block> getFrameBloks() {
        ArrayList<Block> frame = new ArrayList<Block>();

        frame.add(addBlock(blockAtlas, RegionNames.BLOCK2, 1,15, GameConfig.HUD_HEIGHT / 2 + GameConfig.GAME_MENU_HEIGHT / 2,
                0f,0f, 0f, 0, true, BodyDef.BodyType.StaticBody));

        frame.add(addBlock(blockAtlas, RegionNames.BLOCK2, 1,GameConfig.HUD_WIDTH - 15, GameConfig.HUD_HEIGHT / 2 + GameConfig.GAME_MENU_HEIGHT / 2,
                0f,0f, 0f, 0, true, BodyDef.BodyType.StaticBody));

        frame.add(addBlock(blockAtlas, RegionNames.BLOCK3, 1,GameConfig.HUD_WIDTH / 2, GameConfig.HUD_HEIGHT - 15,
                0f,0f, 0f, 0, true, BodyDef.BodyType.StaticBody));

        frame.add(addBlock(blockAtlas, RegionNames.BLOCK3, 1,GameConfig.HUD_WIDTH / 2, GameConfig.GAME_MENU_HEIGHT + 15,
                0f,0f, 0f,0, true, BodyDef.BodyType.StaticBody));
        return frame;
    }

    private Ball addBall(TextureAtlas mapAtlas, int color, float posX, float posY, float vecX, float vecY) {
        Sprite sprite = new Sprite(mapAtlas.findRegion(RegionNames.BALLARRAY[color]));
        sprite.setPosition(posX - sprite.getWidth()/2, posY - sprite.getHeight() /2);
        sprite.setScale(3 /4f);

        CircleShape shape = new CircleShape();
        shape.setRadius(sprite.getHeight() * 3 / 8 / GameConfig.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) / GameConfig.PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2) / GameConfig.PIXELS_TO_METERS);

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);


        body.setFixedRotation(true);

        //body.applyForceToCenter((float)(random.nextInt(20) - 10),(float)(random.nextInt(20) - 10),true);
        //body.applyForceToCenter(vecX, vecY, true);
        body.setLinearVelocity(vecX, vecY);



        shape.dispose();
        return new Ball(sprite, body, color);
    }

    private Block addBlock(TextureAtlas mapAtlas, String regionName, float density, float posX, float posY, float vecX, float vecY, float vecAngular, float angle, boolean verticle, BodyDef.BodyType bodyType) {
        Sprite sprite = new Sprite(mapAtlas.findRegion(regionName));
        sprite.setPosition(posX - sprite.getWidth()/2, posY - sprite.getHeight() /2);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2 / GameConfig.PIXELS_TO_METERS , sprite.getHeight() /2 / GameConfig.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(((sprite.getX() + sprite.getWidth()/2) / GameConfig.PIXELS_TO_METERS),
                ((sprite.getY() + sprite.getHeight()/2) / GameConfig.PIXELS_TO_METERS));

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        if(vecAngular == 0)
            body.setFixedRotation(true);
        else
            body.setAngularVelocity(vecAngular);

        body.setTransform(body.getPosition(), body.getAngle() + angle * GameConfig.DEGREES_TO_RADIANS);

        body.setLinearVelocity(vecX, vecY);
        //body.applyForceToCenter(vecX, vecY, true);
        shape.dispose();

        return new Block(sprite,body, verticle);
    }

    public void setBlockLinearVelocity(Block block, Vector3 vector) {
        if(block.vertical) {
            if(block.sprite.getOriginY() + block.sprite.getY() - vector.y > 20f)
                block.body.setLinearVelocity(0f, -10f);
            else if(block.sprite.getOriginY() + block.sprite.getY()- vector.y < -20f)
                block.body.setLinearVelocity(0f, 10f);
            else
                block.body.setLinearVelocity(0f, 0f);
        } else {
            if(block.sprite.getOriginX() + block.sprite.getX() - vector.x > 20f)
                block.body.setLinearVelocity(-10f, 0f);
            else if(block.sprite.getOriginX() + block.sprite.getX() - vector.x < -20f)
                block.body.setLinearVelocity(10f, 0f);
            else
                block.body.setLinearVelocity(0f, 0f);
        }
    }

    public void clearBlockLinearVelocity(Block block) {
        block.body.setLinearVelocity(0f, 0f);
    }

    public void setBlockDefaultLine(ArrayList<Block> blocks, int selectedBlock) {
        for (int i = 0; i < blocks.size(); i++) {
            if(selectedBlock != i) {
                blocks.get(i).body.setLinearVelocity(0f, 0f);
            }
            if(blocks.get(i).vertical) {
                blocks.get(i).body.setTransform(blocks.get(i).x, blocks.get(i).body.getPosition().y, blocks.get(i).body.getAngle());
            } else {
                blocks.get(i).body.setTransform(blocks.get(i).body.getPosition().x,blocks.get(i).y, blocks.get(i).body.getAngle());
            }
        }
    }
}
