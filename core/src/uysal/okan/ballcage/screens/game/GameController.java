package uysal.okan.ballcage.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
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

import uysal.okan.ballcage.BallCage;
import uysal.okan.ballcage.assets.AssetDescriptors;
import uysal.okan.ballcage.assets.RegionNames;
import uysal.okan.ballcage.config.GameConfig;
import uysal.okan.ballcage.objects.Ball;
import uysal.okan.ballcage.objects.Block;
import uysal.okan.ballcage.objects.Floor;
import uysal.okan.ballcage.objects.Star;
import uysal.okan.ballcage.util.GdxUtils;

public class GameController {

    private final BallCage game;
    private final AssetManager assetManager;

    private boolean gameOver = false;

    private TextureAtlas bgAtlas;
    private TextureAtlas blockAtlas;
    private TextureAtlas floorAtlas;
    private TextureAtlas ballAtlas;

    private World world;

    public GameController(BallCage game) {
        this.game = game;
        assetManager = game.getAssetManager();
        init();
    }

    private void init() {
        world = new World(new Vector2(0, 0f),true);

        bgAtlas = assetManager.get(AssetDescriptors.BG);
        blockAtlas = assetManager.get(AssetDescriptors.BLOCKS);
        floorAtlas = assetManager.get(AssetDescriptors.FLOORS);
        //ballAtlas = assetManager.get(AssetDescriptors.BALLS);
        ballAtlas = assetManager.get(AssetDescriptors.BALLSVAR[GameConfig.ballvar]);

    }

    public void update(float delta) {
        if(!gameOver)
            world.step(delta, 6, 2);
    }

    public boolean controlFinishStage(ArrayList<Ball> balls, ArrayList<Floor> floors) {
        boolean find = false;
        for(int i = 0; i < balls.size(); i++) {
            if(balls.get(i).color != 4) {
                find = false;
                for (int j = 0; j < floors.size(); j++) {

                    Circle circle = new Circle(balls.get(i).sprite.getX() + balls.get(i).sprite.getWidth() / 2,
                            balls.get(i).sprite.getY() + balls.get(i).sprite.getHeight() / 2, balls.get(i).sprite.getHeight() / 2);
                    Rectangle rectangle = new Rectangle(floors.get(j).sprite.getX() + 20, floors.get(j).sprite.getY() + 20,
                            floors.get(j).sprite.getWidth() - 40,
                            floors.get(j).sprite.getHeight() - 40);
                    if (Intersector.overlaps(circle, rectangle)) {
                        if (balls.get(i).color == floors.get(j).color) {
                            find = true;
                        } else {
                            return false;
                        }
                    }

                    // if(balls.get(i).sprite.)
                }
            } else {
                find = true;
            }
            if( find == false) {
                return false;
            }
        }
        if(TimeUtils.millis() - GdxUtils.adsTime > 120000) {
            game.getAdController().showInterstitial();
            GdxUtils.adsTime = TimeUtils.millis();
        }
        gameOver = true;
        return true;
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

    public ArrayList<Star> convertBalls(ArrayList<Ball> balls) {
        ArrayList<Star> stars = new ArrayList<Star>();

        for (int i = 0; i < balls.size(); i++) {
            Sprite sprite = new Sprite(ballAtlas.findRegion(RegionNames.STAR));
            sprite.setScale(3 /4f);
            sprite.setPosition(balls.get(i).sprite.getX(), balls.get(i).sprite.getY());
            stars.add(new Star(sprite));
            world.destroyBody(balls.get(i).body);
        }

        return stars;
    }

    public Sprite getBackgroundSprite() {
        Sprite sprite = new Sprite(bgAtlas.findRegion(RegionNames.BG));
        sprite.setPosition(0, 0);
        return sprite;
    }

    public ArrayList<Block> getGameBloks(int stage) {
        if(stage > GameConfig.maxLevel)
            stage = GameConfig.maxLevel;
        int stageConfig[] = GdxUtils.gameBlockArray(stage);
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
            if( stageConfig[i] == -2) {
                float x = 30 * ((i % 3) + 1) + (i % 3) * 200 + 100;
                float y = GameConfig.HUD_HEIGHT - (30 * ((i / 3) + 1) + (i / 3) * 230 + 115);
                blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                        x, y,0f, 0f, -5f, 0, true, BodyDef.BodyType.KinematicBody));
            }
            if( stageConfig[i] == -3) {
                float x = 30 * ((i % 3) + 1) + (i % 3) * 200 + 100;
                float y = GameConfig.HUD_HEIGHT - (30 * ((i / 3) + 1) + (i / 3) * 230 + 115);
                blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                        x, y,0f, 0f, 3f, 0, true, BodyDef.BodyType.KinematicBody));
            }
            if( stageConfig[i] == -4) {
                float x = 30 * ((i % 3) + 1) + (i % 3) * 200 + 100;
                float y = GameConfig.HUD_HEIGHT - (30 * ((i / 3) + 1) + (i / 3) * 230 + 115);
                blocks.add(addBlock(blockAtlas, RegionNames.BLOCK4, 100000,
                        x, y,0f, 0f, -3f, 0, true, BodyDef.BodyType.KinematicBody));
            }
        }

        return blocks;
    }

    public ArrayList<Ball> getGameBalls(int stage) {
        if(stage > GameConfig.maxLevel)
            stage = GameConfig.maxLevel;
        float stageConfig[][] = GdxUtils.gameBallArray(stage);
        ArrayList<Ball> balls = new ArrayList<Ball>();

        for(int i = 0; i < stageConfig.length; i++) {
            balls.add(addBall(ballAtlas, (int)stageConfig[i][0], stageConfig[i][1] * 60,
                    GameConfig.HUD_HEIGHT - stageConfig[i][2] * 60, stageConfig[i][3], stageConfig[i][4]));
        }

        return  balls;
    }

    public ArrayList<Floor> getGameFloors(int stage) {
        if(stage > GameConfig.maxLevel)
            stage = GameConfig.maxLevel;
        int stageConfig[] = GdxUtils.gameBlockArray(stage);
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
