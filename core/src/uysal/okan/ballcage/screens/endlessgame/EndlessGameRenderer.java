package uysal.okan.ballcage.screens.endlessgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import uysal.okan.ballcage.BallCage;
import uysal.okan.ballcage.assets.AssetDescriptors;
import uysal.okan.ballcage.config.GameConfig;
import uysal.okan.ballcage.objects.Ball;
import uysal.okan.ballcage.objects.Block;
import uysal.okan.ballcage.objects.Floor;
import uysal.okan.ballcage.objects.Star;
import uysal.okan.ballcage.screens.game.GameController;
import uysal.okan.ballcage.screens.game.GameScreen;
import uysal.okan.ballcage.screens.loading.LoadingScreen;
import uysal.okan.ballcage.util.GdxUtils;

public class EndlessGameRenderer implements Disposable {

    private final EndlessGameController controller;
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private final BallCage game;

    private Stage stageMenu;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Sprite background;

    private ArrayList<Block> frame;
    private ArrayList<Block> blocks;
    private ArrayList<Floor> floors;
    private ArrayList<Ball> balls;
    private ArrayList<Star> stars;

    private boolean finishStage = false;
    private boolean drawActor = false;
    private boolean gameOver = false;

    private int selectedBlock = -1;

    private BitmapFont effectFont = null;
    public static GlyphLayout glyphLayout = new GlyphLayout();


    public EndlessGameRenderer(BallCage game, SpriteBatch batch, AssetManager assetManager, EndlessGameController controller) {
        this.game = game;
        this.batch = batch;
        this.assetManager = assetManager;
        this.controller = controller;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        stageMenu = new Stage(viewport, batch);

        background = controller.getBackgroundSprite();
        frame = controller.getFrameBloks();
        blocks = controller.getGameBloks();
        floors = controller.getGameFloors();
        balls = controller.getGameBalls();
        stars = new ArrayList<Star>();

        effectFont = assetManager.get(AssetDescriptors.EFFECT_FONT2);
        effectFont.getData().markupEnabled = true;

    }

    public void render(float delta) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new LoadingScreen(game));
        }

        if(finishStage && stars.size() == 0 && !drawActor) {
            drawActor = true;
            stageMenu.addActor(createStageUi());
            //game.setScreen(new LoadingScreen(game));
        }

        controller.controlBalls(balls, floors, stars);
        //controller.controlBallCount(balls);
        //stageMenu.addActor(createStageUi());
        controller.setBlockDefaultLine(blocks, selectedBlock);
        if(!finishStage && controller.controlGameOver(balls, floors)) {
            gameOver = true;
            finishStage = true;
        }


        if (Gdx.input.justTouched()) {
            Vector3 touchPoint = new Vector3();
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPoint);

            for (int i = 0; i < blocks.size(); i++) {
                if(blocks.get(i).body.getAngularVelocity() == 0f) {
                    if (blocks.get(i).vertical) {
                        Sprite sprite = new Sprite(blocks.get(i).sprite);
                        sprite.setScale(3f, 1f);
                        if (sprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                            selectedBlock = i;
                            break;
                        }
                    } else {
                        Sprite sprite = new Sprite(blocks.get(i).sprite);
                        sprite.setScale(1f, 3f);
                        if (sprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                            selectedBlock = i;
                            break;
                        }
                    }
                }
                /*if(blocks.get(i).sprite.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                    selectedBlock = i;
                    break;
                }*/
            }
        } else if (Gdx.input.isTouched() && selectedBlock != -1) {
            Vector3 touchPoint = new Vector3();
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPoint);
            controller.setBlockLinearVelocity(blocks.get(selectedBlock), touchPoint);
        } else {
            if (selectedBlock != -1) {
                //controller.clearBlockLinearVelocity(blocks.get(selectedBlock));
                selectedBlock = -1;
            }

        }



        GdxUtils.clearScreen();

        renderGamePlay();

        // render ui/hud
        renderUi();


    }

    private void renderUi() {
        stageMenu.act();
        stageMenu.draw();
    }

    private Actor createStageUi() {
        Gdx.input.setInputProcessor(stageMenu);
        Table table = new Table();


        Skin uiskin = assetManager.get(AssetDescriptors.UISKIN);

        Table buttonTable = new Table(uiskin);
        buttonTable.defaults().pad(20);
        //buttonTable.setBackground(RegionNames.PANEL);


        /*ImageButton home = new ImageButton(new TextureRegionDrawable(buttonAtlas.findRegion(RegionNames.HOME_BUTON)),
                new TextureRegionDrawable(buttonAtlas.findRegion(RegionNames.HOME_BUTON_DOWN)));*/
        ImageButton home = new ImageButton(uiskin, "default");


        home.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LoadingScreen(game));
            }
        });



        ImageButton replay = new ImageButton(uiskin, "replay");

        replay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EndlessGameScreen(game));
            }
        });

        buttonTable.add(home).row();

        buttonTable.add(replay);


        buttonTable.center();
        table.add(buttonTable);

        System.out.println("buttonTable.getHeight():" + buttonTable.getHeight());
        System.out.println("buttonTable.getWidth():" + buttonTable.getWidth());
        System.out.println("home.getHeight():" + home.getHeight());
        System.out.println("home.getWidth():" + home.getWidth());

        table.setPosition(GameConfig.HUD_WIDTH / 2 - home.getWidth() / 2 - 20,
                (GameConfig.HUD_HEIGHT + GameConfig.GAME_MENU_HEIGHT) / 2 - (home.getHeight() * 2 + 60) / 2);

        table.pack();

        return table;


    }

    private void renderGamePlay() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(background, background.getX(), background.getY(), background.getOriginX(),
                background.getOriginY(),
                background.getWidth(), background.getHeight(), background.getScaleX(), background.
                        getScaleY(), background.getRotation());

        for (int i = 0; i < frame.size(); i++) {

            batch.draw(frame.get(i).sprite, frame.get(i).sprite.getX(), frame.get(i).sprite.getY(), frame.get(i).sprite.getOriginX(),
                    frame.get(i).sprite.getOriginY(),
                    frame.get(i).sprite.getWidth(), frame.get(i).sprite.getHeight(), frame.get(i).sprite.getScaleX(), frame.get(i).sprite.
                            getScaleY(), frame.get(i).sprite.getRotation());
        }

        for (int i = 0; i < floors.size(); i++) {

            batch.draw(floors.get(i).sprite, floors.get(i).sprite.getX(), floors.get(i).sprite.getY(), floors.get(i).sprite.getOriginX(),
                    floors.get(i).sprite.getOriginY(),
                    floors.get(i).sprite.getWidth(), floors.get(i).sprite.getHeight(), floors.get(i).sprite.getScaleX(), floors.get(i).sprite.
                            getScaleY(), floors.get(i).sprite.getRotation());
        }

        for (int i = 0; i < blocks.size(); i++) {

            blocks.get(i).sprite.setPosition(blocks.get(i).body.getPosition().x * GameConfig.PIXELS_TO_METERS - blocks.get(i).sprite.
                            getWidth() / 2,
                    blocks.get(i).body.getPosition().y * GameConfig.PIXELS_TO_METERS - blocks.get(i).sprite.getHeight() / 2)
            ;
            blocks.get(i).sprite.setRotation((float) Math.toDegrees(blocks.get(i).body.getAngle()));


            batch.draw(blocks.get(i).sprite, blocks.get(i).sprite.getX(), blocks.get(i).sprite.getY(), blocks.get(i).sprite.getOriginX(),
                    blocks.get(i).sprite.getOriginY(),
                    blocks.get(i).sprite.getWidth(), blocks.get(i).sprite.getHeight(), blocks.get(i).sprite.getScaleX(), blocks.get(i).sprite.
                            getScaleY(), blocks.get(i).sprite.getRotation());
        }
        if(stars != null) {
            for (int i = 0; i < stars.size(); i++) {
                stars.get(i).moveStar();
                batch.draw(stars.get(i).sprite, stars.get(i).sprite.getX(), stars.get(i).sprite.getY(), stars.get(i).sprite.getOriginX(),
                        stars.get(i).sprite.getOriginY(),
                        stars.get(i).sprite.getWidth(), stars.get(i).sprite.getHeight(), stars.get(i).sprite.getScaleX(), stars.get(i).sprite.
                                getScaleY(), stars.get(i).sprite.getRotation());

                if(stars.get(i).sprite.getX() > GameConfig.HUD_WIDTH && stars.get(i).sprite.getY() > GameConfig.HUD_HEIGHT) {
                    GdxUtils.star++;
                    controller.score++;
                    if(controller.score > GdxUtils.highScore) {
                        GdxUtils.highScore = controller.score;
                        GameConfig.PREFS.putInteger(GameConfig.HIGHSCORE_KEY, GdxUtils.highScore);
                    }
                    GameConfig.PREFS.putInteger(GameConfig.STAR_KEY, GdxUtils.star);
                    GameConfig.PREFS.flush();
                    stars.remove(i--);
                    controller.changeFloors(floors, blocks);

                }
            }
        }


        for (int i = 0; i < balls.size(); i++) {
            //blocks.get(i).body.setTransform(blocks.get(i).body.getPosition().x, blocks.get(i).body.getPosition().y + 10f, blocks.get(i).body.getAngle());
            balls.get(i).sprite.setPosition((balls.get(i).body.getPosition().x) * GameConfig.PIXELS_TO_METERS - balls.get(i).sprite.
                            getWidth()/2 ,
                    (balls.get(i).body.getPosition().y) * GameConfig.PIXELS_TO_METERS - balls.get(i).sprite.getHeight()/2  )
            ;
            balls.get(i).sprite.setRotation((float)Math.toDegrees(balls.get(i).body.getAngle()));

            Vector2 vec = balls.get(i).body.getLinearVelocity();
            if( Math.sqrt(Math.pow(vec.x,2) + Math.pow(vec.y,2)) < 2 ) {
                float x = vec.x + (vec.x / (Math.abs(vec.x) + Math.abs(vec.y)));
                float y = vec.y + (vec.y / (Math.abs(vec.x) + Math.abs(vec.y)));
                balls.get(i).body.setLinearVelocity(x, y);
            }

            if( Math.sqrt(Math.pow(vec.x,2) + Math.pow(vec.y,2)) > 5 ) {
                float x = 5 * (vec.x / (Math.abs(vec.x) + Math.abs(vec.y)));
                float y = 5 * (vec.y / (Math.abs(vec.x) + Math.abs(vec.y)));
                balls.get(i).body.setLinearVelocity(x, y);
            }



            vec = balls.get(i).body.getLinearVelocity();

            if(Math.abs(vec.x) < 1.5f) {
                if(vec.x < 0)
                    vec.x -= 0.1f;
                else
                    vec.x +=0.1f;

                balls.get(i).body.setLinearVelocity(vec);
            }

            if(Math.abs(vec.y) < 1.5f) {
                if(vec.y < 0)
                    vec.y -= 0.1f;
                else
                    vec.y +=0.1f;

                balls.get(i).body.setLinearVelocity(vec);
            }




            batch.draw(balls.get(i).sprite, balls.get(i).sprite.getX(), balls.get(i).sprite.getY(), balls.get(i).sprite.getOriginX(),
                    balls.get(i).sprite.getOriginY(),
                    balls.get(i).sprite.getWidth(), balls.get(i).sprite.getHeight(), balls.get(i).sprite.getScaleX(), balls.get(i).sprite.
                            getScaleY(), balls.get(i).sprite.getRotation());

        }

        if(effectFont != null) {
            glyphLayout.setText(effectFont, "SCORE: " + controller.score + "/" + GdxUtils.highScore);
            effectFont.draw(batch, "[#960019]" + "SCORE: " + controller.score + "/" + GdxUtils.highScore,  GameConfig.HUD_WIDTH / 2 - glyphLayout.width / 2,
                    GameConfig.GAME_MENU_HEIGHT - 10f);
        }

        batch.end();


    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stageMenu.dispose();
    }
}
