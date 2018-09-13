package uysal.okan.ballcage.screens.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

import uysal.okan.ballcage.BallCage;
import uysal.okan.ballcage.assets.AssetDescriptors;
import uysal.okan.ballcage.assets.AssetPaths;
import uysal.okan.ballcage.assets.RegionNames;
import uysal.okan.ballcage.config.GameConfig;
import uysal.okan.ballcage.objects.Ball;
import uysal.okan.ballcage.objects.Block;
import uysal.okan.ballcage.screens.endlessgame.EndlessGameScreen;
import uysal.okan.ballcage.screens.game.GameScreen;
import uysal.okan.ballcage.util.GdxUtils;

public class LoadingScreen extends ScreenAdapter {

    private static final Logger log = new Logger(LoadingScreen.class.getName(), Logger.DEBUG);

    private static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH * 3 / 4; // world units
    private static final float PROGRESS_BAR_HEIGHT = 20; // world units

    private static final float DEGREES_TO_RADIANS = (float)(Math.PI/180);
    private static final float PIXELS_TO_METERS = 100f;

    private static boolean mainMenu = true;

    private static Random random = new Random();

    // == attributes ==
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private SpriteBatch batch;
    private World world;

    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    private float progress;
    private float waitTime = 0.75f;
    private boolean changeScreen;
    private boolean loadUiskin = false;
    private boolean hideAd = false;

    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Ball> balls = new ArrayList<Ball>();

    private final BallCage game;
    private final AssetManager assetManager;

    private Texture bg = null;
    private Sprite spriteBg = null;
    private Sprite star = null;

    private Stage stage;

    private BitmapFont effectFont = null;
    private BitmapFont effectFont2 = null;

    public static GlyphLayout glyphLayout = new GlyphLayout();
    public static GlyphLayout glyphLayout2 = new GlyphLayout();

    private static long time;






    public LoadingScreen(BallCage game) {
        Gdx.input.setCatchBackKey(true);
        this.game = game;
        batch = game.getBatch();
        assetManager = game.getAssetManager();
        time = TimeUtils.millis();
        if(TimeUtils.millis() - GdxUtils.adsTime > 120000) {
            game.getAdController().showInterstitial();
            GdxUtils.adsTime = TimeUtils.millis();
        }

    }

    // == public methods ==
    @Override
    public void show() {
        debugRenderer = new Box2DDebugRenderer();
        log.debug("show");
        log.debug(String.valueOf( (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f) );
        log.debug(String.valueOf( (GameConfig.HUD_HEIGHT - GameConfig.HUD_WIDTH - PROGRESS_BAR_HEIGHT) / 2f) );
        log.debug(String.valueOf( GameConfig.HUD_HEIGHT ) );
        log.debug(String.valueOf( GameConfig.HUD_WIDTH ) );
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        stage = new Stage(viewport, game.getBatch());
        renderer = new ShapeRenderer();
        world = new World(new Vector2(0, 0f),true);
        //game.getAdController().hideBanner();

        if(!assetManager.isLoaded(AssetPaths.UISKIN)) {
            assetManager.load(AssetDescriptors.EFFECT_FONT);
            assetManager.load(AssetDescriptors.EFFECT_FONT2);
            assetManager.load(AssetDescriptors.BG);
            assetManager.load(AssetDescriptors.BLOCKS);
            assetManager.load(AssetDescriptors.BALLS);
            assetManager.load(AssetDescriptors.GRAVELBALLS);
            assetManager.load(AssetDescriptors.FLOORS);
            assetManager.load(AssetDescriptors.BUTTONS);
            assetManager.load(AssetDescriptors.UISKIN);
        }


        GdxUtils.star = GameConfig.PREFS.getInteger(GameConfig.STAR_KEY, 0);
        GameConfig.level = GameConfig.PREFS.getInteger(GameConfig.STAGE_KEY, 1);
        GdxUtils.highScore = GameConfig.PREFS.getInteger(GameConfig.HIGHSCORE_KEY, 0);




    }

    @Override
    public void render(float delta) {
        if(!hideAd && GdxUtils.loadAd) {
            game.getAdController().hideBanner();
            hideAd = true;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if(!mainMenu) {
                stage.dispose();
                stage = new Stage(viewport, game.getBatch());
                stage.addActor(createUi());
                time = TimeUtils.millis();
            }
            else if(TimeUtils.millis() - time > 1500) {
                Gdx.app.exit();
            }
        }

        world.step(delta, 6, 2);

        update(delta);

        GdxUtils.clearScreen();
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);


        batch.setProjectionMatrix(camera.combined);

        debugMatrix = batch.getProjectionMatrix().cpy().scale(1,
                1, 0);

        batch.begin();

        drawBatch();

        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        draw();

        renderer.end();

        debugRenderer.render(world, debugMatrix);

        stage.act();
        stage.draw();

        if(changeScreen) {

        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        log.debug("hide");
        // NOTE: screens dont dispose automatically
        dispose();
    }

    @Override
    public void dispose() {
        log.debug("dispose");
        stage.dispose();
        renderer.dispose();
        renderer = null;
    }

    // == private methods ==
    private void update(float delta) {
        // progress is between 0 and 1
        progress = assetManager.getProgress();
        if(effectFont == null && assetManager.isLoaded(AssetPaths.EFFECT_FONT)) {
            effectFont = assetManager.get(AssetDescriptors.EFFECT_FONT);
            effectFont.getData().markupEnabled = true;
            glyphLayout.setText(effectFont, GdxUtils.GAME_NAME);
        }
        if(effectFont2 == null && assetManager.isLoaded(AssetPaths.EFFECT_FONT2)) {
            effectFont2 = assetManager.get(AssetDescriptors.EFFECT_FONT2);
            effectFont2.getData().markupEnabled = true;
        }
        if(spriteBg == null && assetManager.isLoaded(AssetPaths.BG)) {
            TextureAtlas mapAtlas = assetManager.get(AssetDescriptors.BG);
            bg = mapAtlas.findRegion(RegionNames.BG).getTexture();
            spriteBg = new Sprite(mapAtlas.findRegion(RegionNames.BG));
            spriteBg.setPosition(0, 0);
        }
        if(blocks.size() == 0 && assetManager.isLoaded(AssetPaths.BLOCKS)) {
            addBloks();
        }
        if(balls.size() == 0 && assetManager.isLoaded(AssetPaths.BALLS)) {
            addBalls();
        }
        if(!loadUiskin && assetManager.isLoaded(AssetPaths.UISKIN)) {
            loadUiskin = true;
            stage.addActor(createUi());
        }

        if(star == null && assetManager.isLoaded(AssetPaths.BALLS)) {
            TextureAtlas mapAtlas = assetManager.get(AssetDescriptors.BALLS);
            star = new Sprite(mapAtlas.findRegion(RegionNames.STAR));
            star.setPosition(GameConfig.HUD_WIDTH - star.getWidth() - 50, GameConfig.HUD_HEIGHT - star.getHeight() - 50);
        }
        /*if(sudoku == null && assetManager.isLoaded(AssetPaths.MAP)) {
            TextureAtlas mapAtlas = assetManager.get(AssetDescriptors.MAP);
            sudoku = new CustomActor(mapAtlas.findRegion(RegionNames.SUDOKU));
            sudoku.setSize(GameConfig.HUD_WIDTH * 3 / 4, GameConfig.HUD_WIDTH * 3 / 4);
            sudoku.setPosition(GameConfig.HUD_WIDTH / 2 - sudoku.getWidth() / 2, GameConfig.HUD_HEIGHT / 2 - sudoku.getHeight() / 2);
            stage.addActor(sudoku);
        }*/




        // update returns true when all assets are loaded
        if(assetManager.update()) {
            waitTime -= delta;

            if(waitTime <= 0) {
                changeScreen = true;
            }
        }

        if(Gdx.input.justTouched()) {
            Vector3 touchPoint = new Vector3();
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),0);
            camera.unproject(touchPoint);
            log.debug("x:" + touchPoint.x + "-y:" + touchPoint.y);
        }
    }

    private void addBalls() {
        TextureAtlas mapAtlas = assetManager.get(AssetDescriptors.BALLS);

        for(int i = 0; i < 10; i++) {
            addBall(mapAtlas,RegionNames.BALLARRAY[random.nextInt(RegionNames.BALLARRAY.length)],
                    (float)(random.nextInt((int)GameConfig.HUD_WIDTH - 60) + 30), (float)(random.nextInt((int)(GameConfig.HUD_HEIGHT - GameConfig.GAME_MENU_HEIGHT - 30)) + 30 + GameConfig.GAME_MENU_HEIGHT),
                    (float) (random.nextInt(100) - 50) / 10f, (float) (random.nextInt(100) - 50) / 10f
            );
        }
    }



    private void addBloks() {
        TextureAtlas mapAtlas = assetManager.get(AssetDescriptors.BLOCKS);

        addBlock(mapAtlas, RegionNames.BLOCK, 100000,
                GameConfig.HUD_WIDTH * 1 / 8, GameConfig.HUD_HEIGHT * 6 / 8,
                4f, 0f, 90, true, BodyDef.BodyType.DynamicBody);

        addBlock(mapAtlas, RegionNames.BLOCK, 100000,
                GameConfig.HUD_WIDTH * 3 / 8, GameConfig.HUD_HEIGHT * 5 / 8,
                1.5f, 0f, 90, true, BodyDef.BodyType.DynamicBody);

        addBlock(mapAtlas, RegionNames.BLOCK, 100000,
                GameConfig.HUD_WIDTH * 5 / 8, GameConfig.HUD_HEIGHT * 4 / 8,
                2f, 0f, 90, true, BodyDef.BodyType.DynamicBody);

        addBlock(mapAtlas, RegionNames.BLOCK, 100000,
                GameConfig.HUD_WIDTH * 7 / 8, GameConfig.HUD_HEIGHT * 3 / 8,
                3f, 0f, 90, true, BodyDef.BodyType.DynamicBody);

        addBlock(mapAtlas, RegionNames.BLOCK2, 1,15, GameConfig.HUD_HEIGHT / 2 + GameConfig.GAME_MENU_HEIGHT / 2,
                0f,0f, 0, true, BodyDef.BodyType.StaticBody);

        addBlock(mapAtlas, RegionNames.BLOCK2, 1,GameConfig.HUD_WIDTH - 15, GameConfig.HUD_HEIGHT / 2 + GameConfig.GAME_MENU_HEIGHT / 2,
                0f,0f, 0, true, BodyDef.BodyType.StaticBody);

        addBlock(mapAtlas, RegionNames.BLOCK3, 1,GameConfig.HUD_WIDTH / 2, GameConfig.HUD_HEIGHT - 15,
                0f,0f, 0, true, BodyDef.BodyType.StaticBody);

        addBlock(mapAtlas, RegionNames.BLOCK3, 1,GameConfig.HUD_WIDTH / 2, GameConfig.GAME_MENU_HEIGHT + 15,
                0f,0f, 0, true, BodyDef.BodyType.StaticBody);

    }

    private void addBall(TextureAtlas mapAtlas, String regionName, float posX, float posY, float vecX, float vecY) {
        Sprite sprite = new Sprite(mapAtlas.findRegion(regionName));
        sprite.setPosition(posX - sprite.getWidth()/2, posY - sprite.getHeight() /2);
        sprite.setScale(3 /4f);

        CircleShape shape = new CircleShape();
        shape.setRadius(sprite.getHeight() * 3 / 8 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) / PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);


        body.setFixedRotation(true);

        //body.applyForceToCenter((float)(random.nextInt(20) - 10),(float)(random.nextInt(20) - 10),true);
        //body.applyForceToCenter(vecX, vecY, true);
        body.setLinearVelocity(vecX, vecY);


        balls.add(new Ball(sprite,body, 0));

        shape.dispose();

    }

    private void addBlock(TextureAtlas mapAtlas, String regionName, float density, float posX, float posY, float vecX, float vecY, float angle, boolean verticle, BodyDef.BodyType bodyType) {
        Sprite sprite = new Sprite(mapAtlas.findRegion(regionName));
        sprite.setPosition(posX - sprite.getWidth()/2, posY - sprite.getHeight() /2);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2 / PIXELS_TO_METERS , sprite.getHeight() /2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = 1f;
        fixtureDef.friction = 0f;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(((sprite.getX() + sprite.getWidth()/2) / PIXELS_TO_METERS),
                ((sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS));

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);


        body.setFixedRotation(true);

        body.setTransform(body.getPosition(), body.getAngle() + angle * DEGREES_TO_RADIANS);

        body.setLinearVelocity(vecX, vecY);
        //body.applyForceToCenter(vecX, vecY, true);


        blocks.add(new Block(sprite,body, verticle));

        shape.dispose();
    }

    private Actor createStageUi() {
        if(TimeUtils.millis() - GdxUtils.adsTime > 120000) {
            game.getAdController().showInterstitial();
            GdxUtils.adsTime = TimeUtils.millis();
        }
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();

        Skin uiskin = assetManager.get(AssetDescriptors.UISKIN);

        Table buttonTable = new Table(uiskin);
        buttonTable.defaults().pad(20);
        buttonTable.setBackground(RegionNames.PANEL);

        int count = 0;
        for(int i = Math.min(GameConfig.level, GameConfig.maxLevel); i > 0; i--) {
            count++;
            TextButton stageButton = new TextButton(i + "", uiskin);
            final int finalI = i;
            stageButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    play(finalI);
                }
            });

            buttonTable.add(stageButton).width(75f).center();

            count = count % 4;
            if(count == 0)
                buttonTable.add().row();
        }

        buttonTable.center();

        ScrollPane scrollPane = new ScrollPane(buttonTable);

        table.add(scrollPane).height(GameConfig.HUD_HEIGHT * 5 / 8).width(GameConfig.HUD_WIDTH * 3 / 4);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;


    }

    private Actor createMarketUi() {
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();


        final Skin uiskin = assetManager.get(AssetDescriptors.UISKIN);

        Table buttonTable = new Table(uiskin);
        buttonTable.defaults().pad(20);
        buttonTable.setBackground(RegionNames.PANEL);


        /*ImageButton home = new ImageButton(new TextureRegionDrawable(buttonAtlas.findRegion(RegionNames.HOME_BUTON)),
                new TextureRegionDrawable(buttonAtlas.findRegion(RegionNames.HOME_BUTON_DOWN)));*/
        ImageButton defaultBall = null;
        if(GameConfig.ballvar == 0) {
            defaultBall =new ImageButton(uiskin, "defaultballselect");
        } else {
            defaultBall =new ImageButton(uiskin, "defaultball");
        }
        final ImageButton finalDefaultBall = defaultBall;



        ImageButton gravelBall = null;
        if(GameConfig.ballvar == 0) {
            gravelBall =new ImageButton(uiskin, "gravelballlock");
        } else {
            gravelBall =new ImageButton(uiskin, "gravelball");
        }
        final ImageButton finalGravelBall = gravelBall;

        ImageButton marbleBall = null;
        if(GameConfig.ballvar == 0) {
            marbleBall =new ImageButton(uiskin, "marbleballlock");
        } else {
            marbleBall =new ImageButton(uiskin, "marbleball");
        }
        final ImageButton finalmarbleBall = marbleBall;

        ImageButton woodBall = null;
        if(GameConfig.ballvar == 0) {
            woodBall =new ImageButton(uiskin, "woodballlock");
        } else {
            woodBall =new ImageButton(uiskin, "woodball");
        }
        final ImageButton finalwoodBall = woodBall;


        defaultBall.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameConfig.ballvar = 0;
                ImageButton temp =new ImageButton(uiskin, "defaultballselect");
                finalDefaultBall.setStyle(temp.getStyle());
                temp =new ImageButton(uiskin, "gravelball");
                finalGravelBall.setStyle(temp.getStyle());
                temp =new ImageButton(uiskin, "marbleball");
                finalmarbleBall.setStyle(temp.getStyle());
                temp =new ImageButton(uiskin, "woodball");
                finalwoodBall.setStyle(temp.getStyle());
            }
        });


        gravelBall.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(GdxUtils.star >= 200) {
                    GameConfig.ballvar = 1;
                    ImageButton temp =new ImageButton(uiskin, "gravelballselect");
                    finalGravelBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "defaultball");
                    finalDefaultBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "marbleball");
                    finalmarbleBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "woodball");
                    finalwoodBall.setStyle(temp.getStyle());
                }
            }
        });

        marbleBall.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(GdxUtils.star >= 200) {
                    GameConfig.ballvar = 2;
                    ImageButton temp =new ImageButton(uiskin, "marbleballselect");
                    finalmarbleBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "defaultball");
                    finalDefaultBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "gravelball");
                    finalGravelBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "woodball");
                    finalwoodBall.setStyle(temp.getStyle());
                }
            }
        });

       woodBall.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(GdxUtils.star >= 200) {
                    GameConfig.ballvar = 3;
                    ImageButton temp =new ImageButton(uiskin, "woodballselect");
                    finalwoodBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "defaultball");
                    finalDefaultBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "gravelball");
                    finalGravelBall.setStyle(temp.getStyle());
                    temp =new ImageButton(uiskin, "marbleball");
                    finalmarbleBall.setStyle(temp.getStyle());
                }
            }
        });


        buttonTable.add(defaultBall);
        buttonTable.add(gravelBall).row();
        buttonTable.add(marbleBall);
        buttonTable.add(woodBall);

        buttonTable.center();

        ScrollPane scrollPane = new ScrollPane(buttonTable);

        table.add(scrollPane).height(GameConfig.HUD_HEIGHT * 5 / 8).width(GameConfig.HUD_WIDTH * 3 / 4);
        table.center();

        table.setFillParent(true);
        table.pack();

        return table;

    }

    private Actor createUi() {
        if(TimeUtils.millis() - GdxUtils.adsTime > 120000) {
            game.getAdController().showInterstitial();
            GdxUtils.adsTime = TimeUtils.millis();
        }

        Gdx.input.setInputProcessor(stage);
        Table table = new Table();

        Skin uiskin = assetManager.get(AssetDescriptors.UISKIN);


        // play button
        TextButton playButton = new TextButton("PLAY", uiskin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenu = false;
                if(GameConfig.level == 1) {
                    game.setScreen(new GameScreen(game, 1));
                } else {
                    stage.dispose();
                    stage = new Stage(viewport, game.getBatch());
                    stage.addActor(createStageUi());
                    //play(1);
                }
            }
        });

        TextButton endlessButton = new TextButton("ENDLESS MODE", uiskin);
        endlessButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EndlessGameScreen(game));
                //showHighScore();
            }
        });

        // high score button
        TextButton marketButton = new TextButton("HIGH SCORES", uiskin);
        marketButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getAdController().showLeaderboard();
                mainMenu = false;
                stage.dispose();
                stage = new Stage(viewport, game.getBatch());
                stage.addActor(createMarketUi());
            }
        });

        // quit button
        TextButton quitButton = new TextButton("QUIT", uiskin);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // setup table
        Table buttonTable = new Table(uiskin);
        buttonTable.defaults().pad(30);
        buttonTable.setBackground(RegionNames.PANEL);

        buttonTable.add(playButton).row();
        buttonTable.add(endlessButton).row();
        buttonTable.add(marketButton).row();
        buttonTable.add(quitButton);


        buttonTable.center();

        ScrollPane scrollPane = new ScrollPane(buttonTable);

        table.add(scrollPane).height(GameConfig.HUD_HEIGHT * 5 / 8).width(GameConfig.HUD_WIDTH * 3 / 4);
        table.center();

        table.setFillParent(true);
        table.pack();

        return table;

    }

    private void play(int stage) {
        game.setScreen(new GameScreen(game, stage));
    }

    private void draw() {
        Color oldColor = renderer.getColor();

        renderer.setColor(Color.BLACK);

        float progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f;
        float progressBarY = (GameConfig.HUD_HEIGHT - GameConfig.HUD_WIDTH - PROGRESS_BAR_HEIGHT) / 2f;

        /*renderer.rect(progressBarX, progressBarY,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT
        );*/

        renderer.setColor(oldColor);
    }

    private void drawBatch() {

        if(bg != null) {
            //batch.draw(bg, 0, 0, GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
            batch.draw(spriteBg, spriteBg.getX(), spriteBg.getY(), spriteBg.getOriginX(),
                    spriteBg.getOriginY(),
                    spriteBg.getWidth(), spriteBg.getHeight(), spriteBg.getScaleX(), spriteBg.
                            getScaleY(), spriteBg.getRotation());
        }

        if(effectFont != null) {
            effectFont.draw(batch, GdxUtils.GAME_NAME.substring((int) (GdxUtils.GAME_NAME.length() * (progress / 100))), GameConfig.HUD_WIDTH / 2 - glyphLayout.width / 2 , GameConfig.GAME_MENU_HEIGHT / 2 + glyphLayout.height / 2);
        }

        for (int i = 0; i < blocks.size(); i++) {
            //blocks.get(i).body.setTransform(blocks.get(i).body.getPosition().x, blocks.get(i).body.getPosition().y + 10f, blocks.get(i).body.getAngle());
            blocks.get(i).sprite.setPosition(blocks.get(i).body.getPosition().x * PIXELS_TO_METERS - blocks.get(i).sprite.
                            getWidth()/2 ,
                    blocks.get(i).body.getPosition().y * PIXELS_TO_METERS - blocks.get(i).sprite.getHeight()/2 )
            ;
            blocks.get(i).sprite.setRotation((float)Math.toDegrees(blocks.get(i).body.getAngle()));

            batch.draw(blocks.get(i).sprite, blocks.get(i).sprite.getX(), blocks.get(i).sprite.getY(), blocks.get(i).sprite.getOriginX(),
                    blocks.get(i).sprite.getOriginY(),
                    blocks.get(i).sprite.getWidth(), blocks.get(i).sprite.getHeight(), blocks.get(i).sprite.getScaleX(), blocks.get(i).sprite.
                            getScaleY(), blocks.get(i).sprite.getRotation());
        }

        for (int i = 0; i < balls.size(); i++) {
            //blocks.get(i).body.setTransform(blocks.get(i).body.getPosition().x, blocks.get(i).body.getPosition().y + 10f, blocks.get(i).body.getAngle());
            balls.get(i).sprite.setPosition((balls.get(i).body.getPosition().x) * PIXELS_TO_METERS - balls.get(i).sprite.
                            getWidth()/2 ,
                    (balls.get(i).body.getPosition().y) * PIXELS_TO_METERS - balls.get(i).sprite.getHeight()/2  )
            ;
            balls.get(i).sprite.setRotation((float)Math.toDegrees(balls.get(i).body.getAngle()));

            Vector2 vec = balls.get(i).body.getLinearVelocity();
            if( Math.sqrt(Math.pow(vec.x,2) + Math.pow(vec.y,2)) < 3 ) {
                float x = vec.x + (vec.x / (Math.abs(vec.x) + Math.abs(vec.y)));
                float y = vec.y + (vec.y / (Math.abs(vec.x) + Math.abs(vec.y)));
                balls.get(i).body.setLinearVelocity(x, y);
            }

            if( Math.sqrt(Math.pow(vec.x,2) + Math.pow(vec.y,2)) > 6 ) {
                float x = vec.x - (vec.x / (Math.abs(vec.x) + Math.abs(vec.y)));
                float y = vec.y - (vec.y / (Math.abs(vec.x) + Math.abs(vec.y)));
                balls.get(i).body.setLinearVelocity(x, y);
            }



            vec = balls.get(i).body.getLinearVelocity();

            if(Math.abs(vec.x) < 1.5f) {
                if(vec.x < 0)
                    vec.x -= 0.2f;
                else
                    vec.x +=0.2f;

                balls.get(i).body.setLinearVelocity(vec);
            }

            if(Math.abs(vec.y) < 1.5f) {
                if(vec.y < 0)
                    vec.y -= 0.2f;
                else
                    vec.y +=0.2f;

                balls.get(i).body.setLinearVelocity(vec);
            }




            batch.draw(balls.get(i).sprite, balls.get(i).sprite.getX(), balls.get(i).sprite.getY(), balls.get(i).sprite.getOriginX(),
                    balls.get(i).sprite.getOriginY(),
                    balls.get(i).sprite.getWidth(), balls.get(i).sprite.getHeight(), balls.get(i).sprite.getScaleX(), balls.get(i).sprite.
                            getScaleY(), balls.get(i).sprite.getRotation());
            //if(balls.get(i).body.getLinearVelocity().x < 30f && balls.get(i).body.getLinearVelocity().x > -30f)
            //log.debug("yavas: " + balls.get(i).sprite.getX());

        }

        if(star != null) {
            batch.draw(star, star.getX(), star.getY(), star.getOriginX(),
                    star.getOriginY(),
                    star.getWidth(), star.getHeight(), star.getScaleX(), star.
                            getScaleY(), star.getRotation());
            if(effectFont2 != null) {
                glyphLayout2.setText(effectFont2, String.valueOf(GdxUtils.star));
                effectFont2.draw(batch, "[#960019]" + GdxUtils.star,  star.getX() - glyphLayout2.width - 30,
                        star.getY() + star.getHeight() / 2 + glyphLayout2.height / 2);
            }
        }





    }

    private static void waitMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
