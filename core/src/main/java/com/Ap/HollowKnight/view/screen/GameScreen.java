package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.GameFlowController;
import com.Ap.HollowKnight.controller.GameProcessor;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.GameCamera;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.TiledMapHelper;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.player.PlayerCondition;
import com.Ap.HollowKnight.view.AnimationType;
import com.Ap.HollowKnight.view.AssetLoader;
import com.Ap.HollowKnight.view.PlayerHUD;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class GameScreen extends BaseScreen {
    private Knight knight;
    private GameProcessor inputController;
    private GameFlowController gameFlowController;
    private ScreenViewport viewport;
    private GameCamera camera;
    private SpriteBatch batch;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    private PlayerHUD hud;
    private LevelModel levelModel;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private ArrayList<Block> blocks;
    private TiledMapHelper mapHelper;
    private float stateTime = 0f;
    private float mapHeight;
    private float mapWidth;
    private final int[] backGrounds = {0};
    private final int[] foreGrounds = {1,2};
    private PlayerCondition previousCondition;
    public GameScreen(HollowKnight game, LevelModel levelModel, TiledMap map) {
        super(game);
        this.levelModel = levelModel;
        this.map = map;
    }

    @Override
    public void show() {
        this.blocks = levelModel.getBlocks();
        this.batch = new SpriteBatch();
        MapLayer spawnLayer = map.getLayers().get("collision objects");
        MapObject spawnObject = spawnLayer.getObjects().get("spawnPoint");
        float spawnX = spawnObject.getProperties().get("x", Float.class);
        float spawnY = spawnObject.getProperties().get("y", Float.class);


        this.knight = new Knight (new Vector2 (spawnX,spawnY),new Rectangle(0,0,32,64),new Vector2(Vector2.Zero));
        knight.setOnGround(true);
        this.camera = new GameCamera();
        this.inputController = new GameProcessor(knight,camera);
        this.viewport = new ScreenViewport(camera);
        Gdx.input.setInputProcessor(inputController);
        this.renderer = new OrthogonalTiledMapRenderer(map);
        this.shapeRenderer = new ShapeRenderer();
        this.hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hud = new PlayerHUD();
        this.gameFlowController= new GameFlowController(this.knight,map,blocks);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float spriteWidth = 90f;
        float spriteHeight = 128f;
        float offsetX = (knight.getHitBox().width - spriteWidth) / 2f;
        float offsetY = 0f;
        PlayerCondition current = knight.getPlayerCondition();
        if (current != previousCondition) {
            stateTime = 0f;
            previousCondition = current;
        }
        stateTime += delta;

        gameFlowController.update(delta);
        camera.updatePosition(knight,null,delta);

        AnimationType drawOne = getDrawOne();
        Animation <TextureRegion> animation = AssetLoader.getInstance().getAnimation(drawOne);
        animation.setPlayMode(drawOne.getPlayMode());
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        boolean shouldFaceRight = knight.getFacingDirection() == FacingDirection.RIGHT;
        if (currentFrame.isFlipX() != shouldFaceRight) {
            currentFrame.flip(true, false);
        }
        renderer.setView(camera);
        renderer.render(backGrounds);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(
            currentFrame,
            knight.getPosition().x + offsetX,
            knight.getPosition().y + offsetY,
            spriteWidth,
            spriteHeight
        );
        System.out.println(knight.getPlayerCondition().toString());
        batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(
            knight.getHitBox().x,
            knight.getHitBox().y,
            knight.getHitBox().width,
            knight.getHitBox().height
        );
        shapeRenderer.rect(
            knight.getNail().getHitBox().x,
            knight.getNail().getHitBox().y,
            knight.getNail().getHitBox().width,
            knight.getNail().getHitBox().height
        );
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(knight.getHitBox().x,knight.getHitBox().y,knight.getHitBox().width,knight.getHitBox().height);
        for (Block block : blocks) {
            shapeRenderer.rect(
                block.getBound().x,
                block.getBound().y,
                block.getBound().width,
                block.getBound().height
            );
        }
        shapeRenderer.end();
        renderer.render(foreGrounds);
        hud.render(knight, hudCamera);
    }

    private AnimationType getDrawOne() {
        AnimationType drawOne;
        switch(knight.getPlayerCondition()){
            case IDLE -> drawOne = AnimationType.KNIGHT_IDLE;
            case MOVING -> drawOne=AnimationType.KNIGHT_RUN;
            case DASHING ->  drawOne=AnimationType.KNIGHT_DASH;
            case FALLING -> drawOne = AnimationType.KNIGHT_LANDING;
            case FOCUSING ->  drawOne = AnimationType.KNIGHT_FOCUS;
            case MONARCHING -> drawOne = AnimationType.KNIGHT_DOUBLE_JUMP;
            case JUMPING -> drawOne = AnimationType.KNIGHT_AIRBORNE;
            default -> drawOne = AnimationType.KNIGHT_IDLE;

            // will become bigger
        }
        return drawOne;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        hudCamera.setToOrtho(false, width, height);

    }
}
