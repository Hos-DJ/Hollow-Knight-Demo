package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.GameFlowController;
import com.Ap.HollowKnight.controller.GameProcessor;
import com.Ap.HollowKnight.model.enemy.Crawlid;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.enemy.HuskHornHead;
import com.Ap.HollowKnight.model.enemy.Mossfly;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.GameCamera;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.player.PlayerCondition;
import com.Ap.HollowKnight.view.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class GameScreen extends BaseScreen {

    // Cleaning: Grouped all MVC and View components together for better readability.
    // No logic was changed here, just organizing the variables you already had.
    private final LevelModel levelModel;
    private final TiledMap map;
    private Knight knight;
    private GameProcessor inputController;
    private GameFlowController gameFlowController;

    private ScreenViewport viewport;
    private GameCamera camera;
    private SpriteBatch batch;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    private PlayerHUD hud;
    private OrthogonalTiledMapRenderer renderer;
    private Texture background;
    private ParticleEffect bgEffect;

    private ArrayList<Block> blocks;
    private float stateTime = 0f;
    private PlayerCondition previousCondition;
    private ArrayList <EnemyModel> enemies;

    // New: Added independent timers to track attack and dash durations separately.
    // This prevents the bug where the effect animation disappears because the main stateTime resets too early when inputs are spammed.
    private float attackStateTime = 0f;
    private boolean wasAttacking = false;
    private float dashStateTime = 0f;
    private boolean wasDashing = false;

    // Cleaning: Moved your magic numbers (like 90f and 128f) into final constants at the top of the class so they are easy to adjust later.
    private final int[] backGrounds = {0};
    private final int[] foreGrounds = {1, 2};
    private final float KNIGHT_SPRITE_WIDTH = 90f;
    private final float KNIGHT_SPRITE_HEIGHT = 128f;
    private final float EFFECT_SPRITE_SIZE = 120f;
    private static final float PARALLAX_FACTOR = 0.5f;

    public GameScreen(HollowKnight game, TiledMap map) {
        super(game);
        this.levelModel = LevelModel.getInstance();
        this.map = map;
    }

    @Override
    public void show() {
        // Cleaning: Retained your exact initialization logic, just removed the TiledMapHelper
        // since the map and blocks are now safely injected via the constructor.
        this.blocks = levelModel.getBlocks();
        this.batch = new SpriteBatch();

        Vector2 spawnPoint = new Vector2(levelModel.getSpawnPoint().x, levelModel.getSpawnPoint().y);
        this.knight = levelModel.getKnight();
        knight.setOnGround(true);

        this.camera = new GameCamera();
        this.enemies = levelModel.getEnemies();
        this.inputController = new GameProcessor(knight, camera,enemies);
        this.viewport = new ScreenViewport(camera);
        Gdx.input.setInputProcessor(inputController);
        this.background = new Texture(Gdx.files.internal("background.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        this.renderer = new OrthogonalTiledMapRenderer(map);
        this.shapeRenderer = new ShapeRenderer();

        this.hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hud = new PlayerHUD();

        this.gameFlowController = new GameFlowController(this.knight, map, blocks,inputController,enemies);
        this.bgEffect = new ParticleEffect();
        bgEffect.load(Gdx.files.internal("particleDemo.p"), Gdx.files.internal("animation/Particles & Effects"));
        bgEffect.start();
    }

    @Override
    public void render(float delta) {
        // Cleaning: The render loop is exactly the same logically, but split into helper methods
        // to cure the "God-Class" syndrome and make it readable like a book.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateTimers(delta);
        gameFlowController.update(delta);
        camera.updatePosition(knight, null, delta);

        renderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        drawBackGroundImage();
        batch.end();

        renderer.render(backGrounds);
        batch.begin();
        bgEffect.update(Gdx.graphics.getDeltaTime());
        bgEffect.draw(batch);
        drawEnemies();
        drawKnightAndEffects();
        batch.end();

        renderer.render(foreGrounds);
        hud.render(knight, hudCamera);

        drawDebugHitboxes();
    }

    private void updateTimers(float delta) {
        // Cleaning: Extracted your previous stateTime logic exactly as you wrote it.
        PlayerCondition current = knight.getPlayerCondition();
        if (current != previousCondition) {
            stateTime = 0f;
            previousCondition = current;
        }
        stateTime += delta;

        // New: Added logic to track attack and dash time independently from the main stateTime.
        // This ensures effect animations play fully, fixing the bug where they wouldn't render if the player rapidly changed states.
        if (knight.isAttacking() && !wasAttacking) {
            attackStateTime = 0f;
        } else if (knight.isAttacking()) {
            attackStateTime += delta;
        }
        wasAttacking = knight.isAttacking();

        if (knight.isDashing() && !wasDashing) {
            dashStateTime = 0f;
        } else if (knight.isDashing()) {
            dashStateTime += delta;
        }
        wasDashing = knight.isDashing();
    }

    private void drawKnightAndEffects() {
        boolean isFacingRight = knight.getFacingDirection() == FacingDirection.RIGHT;

        // Cleaning: Extracted the Knight fetching logic. Retained your precise offset math.
        KnightAnimationType drawKnight = getDrawKnight();
        Animation<TextureRegion> knightAnim = AssetLoader.getInstance().getAnimation(drawKnight);
        knightAnim.setPlayMode(drawKnight.getPlayMode());
        TextureRegion currentKnightFrame = knightAnim.getKeyFrame(stateTime);

        float offsetX = (knight.getHitBox().width - KNIGHT_SPRITE_WIDTH) / 2f;
        float offsetY = 0f;

        // New: Replaced currentKnightFrame.flip() with the advanced batch.draw() parameters.
        // Flipping the TextureRegion directly corrupts the cached memory (causing vibration bugs). Using the flipX parameter in batch.draw is the safe libGDX standard.
        batch.draw(
            currentKnightFrame.getTexture(),
            knight.getPosition().x + offsetX,
            knight.getPosition().y + offsetY,
            KNIGHT_SPRITE_WIDTH,
            KNIGHT_SPRITE_HEIGHT,
            currentKnightFrame.getRegionX(),
            currentKnightFrame.getRegionY(),
            currentKnightFrame.getRegionWidth(),
            currentKnightFrame.getRegionHeight(),

            isFacingRight, // Flip X
            false           // Flip Y
        );

        // Cleaning: Extracted effect drawing logic, only calling it if an effect actually exists.
        EffectAnimationType drawEffect = getDrawEffect();
        if (drawEffect != null) {
            // New: Assigning the correct independent timer based on the effect type to prevent the visual stutter.
            float effectTimer = (drawEffect == EffectAnimationType.DASH_EFFECT) ? dashStateTime : attackStateTime;
            Animation<TextureRegion> effectAnim = AssetLoader.getInstance().getAnimation(drawEffect);
            TextureRegion currentEffectFrame = effectAnim.getKeyFrame(effectTimer);

            handleDrawingEffect(currentEffectFrame, drawEffect, isFacingRight);
        }
    }

    private void drawEnemies() {
        for (EnemyModel enemy : enemies) {
            AnimationType animType = getEnemyAnimationType(enemy);
            if (animType == null) continue;

            Animation<TextureRegion> animation = AssetLoader.getInstance().getAnimation(animType);
            animation.setPlayMode(animType.getPlayMode());

            TextureRegion currentFrame = animation.getKeyFrame(stateTime);
            boolean isFacingRight = enemy.getFacingDirection() == FacingDirection.RIGHT;

            // Calculating sprite dimensions. We use a 1.5x scaling factor as a baseline
            // so the graphics generously cover the tight physical hitbox bounds.
            float drawWidth = enemy.getHitBox().width *1.5f;
            float drawHeight = enemy.getHitBox().height * 1.5f;

            // Center the graphic over the physical hitbox horizontally
            float offsetX = (enemy.getHitBox().width - drawWidth) / 2f;

            // Keep the feet planted on the bottom of the hitbox
            float offsetY = 0f;

            // Safe drawing to avoid TextureRegion memory corruption
            batch.draw(
                currentFrame.getTexture(),
                enemy.getPosition().x + offsetX,
                enemy.getPosition().y + offsetY,
                drawWidth,
                drawHeight,
                currentFrame.getRegionX(),
                currentFrame.getRegionY(),
                currentFrame.getRegionWidth(),
                currentFrame.getRegionHeight(),
                (enemy instanceof HuskHornHead) == isFacingRight, // Flip X based on facing direction
                false           // Flip Y
            );
        }
    }

    private void handleDrawingEffect(TextureRegion effectFrame, EffectAnimationType type, boolean isFacingRight) {
        if (effectFrame == null) return;

        float effectX, effectY;

        // New: Instead of random offset numbers, the effect coordinates are mathematically locked to your physical hitboxes.
        if (type == EffectAnimationType.DASH_EFFECT) {
            // Dash effect centers on the Knight's hitbox, slightly trailing behind based on facing direction.
            effectX = knight.getHitBox().x + knight.getHitBox().width / 2f - EFFECT_SPRITE_SIZE / 2f;
            effectX += isFacingRight ? -30f : 30f;
            effectY = knight.getHitBox().y + knight.getHitBox().height / 2f - EFFECT_SPRITE_SIZE / 2f;
        } else {
            // Nail effect flawlessly locks onto the exact center of the invisible physical Nail HitBox from your model.
            Rectangle nailHitBox = knight.getNail().getHitBox();
            effectX = nailHitBox.x + nailHitBox.width / 2f - EFFECT_SPRITE_SIZE / 2f;
            effectY = nailHitBox.y + nailHitBox.height / 2f - EFFECT_SPRITE_SIZE / 2f;
        }

        boolean flipEffectX = (type == EffectAnimationType.DASH_EFFECT) != isFacingRight;

        // New: Again, used the advanced batch.draw() to avoid corrupting TextureRegion memory via flip().
        batch.draw(
            effectFrame.getTexture(),
            effectX,
            effectY,
            EFFECT_SPRITE_SIZE,
            EFFECT_SPRITE_SIZE,
            effectFrame.getRegionX(),
            effectFrame.getRegionY(),
            effectFrame.getRegionWidth(),
            effectFrame.getRegionHeight(),
            flipEffectX,
            false
        );
    }

    public void drawBackGroundImage(){
        float cameraX = camera.position.x;
        float cameraY = camera.position.y;
        float viewW = camera.viewportWidth;
        float viewH = camera.viewportHeight;

        float bgX = cameraX * PARALLAX_FACTOR;
        float bgY = cameraY * PARALLAX_FACTOR;

        float drawX = cameraX - viewW / 2f;
        float drawY = cameraY - viewH / 2f;

        int srcX = (int)(bgX) % background.getWidth();
        int srcY = 0;

        batch.draw(
            background,
            drawX, drawY,
            viewW, viewH,
            srcX, srcY,
            (int) viewW, (int) viewH,
            false, false
        );
    }

    private KnightAnimationType getDrawKnight() {
        // Cleaning: Simplified your switch statement. Logic is identical, just less typing.
        switch (knight.getPlayerCondition()) {
            case MOVING:     return KnightAnimationType.KNIGHT_RUN;
            case DASHING:    return KnightAnimationType.KNIGHT_DASH;
            case FALLING:    return KnightAnimationType.KNIGHT_LANDING;
            case FOCUSING:   return KnightAnimationType.KNIGHT_FOCUS;
            case MONARCHING: return KnightAnimationType.KNIGHT_DOUBLE_JUMP;
            case JUMPING:    return KnightAnimationType.KNIGHT_AIRBORNE;
            case ATTACKING:
                switch (knight.getCurrentAttackDirection()) {
                    case UP:   return KnightAnimationType.KNIGHT_UP_SLASH;
                    case DOWN: return KnightAnimationType.KNIGHT_DOWN_SLASH;
                    default:   return KnightAnimationType.KNIGHT_SLASH;
                }
            default:         return KnightAnimationType.KNIGHT_IDLE;
        }
    }

    private EffectAnimationType getDrawEffect() {
        // Cleaning: Condensing your large switch/if-else logic into a very direct lookup structure.
        if (knight.isDashing()) return EffectAnimationType.DASH_EFFECT;

        if (knight.isAttacking()) {
            switch (knight.getCurrentAttackDirection()) {
                case UP:   return EffectAnimationType.NAIL_UP_SLASH;
                case DOWN: return EffectAnimationType.NAIL_DOWN_SLASH;
                default:   return EffectAnimationType.NAIL_SLASH;
            }
        }
        return null;
    }

    private AnimationType getEnemyAnimationType(EnemyModel enemy) {
        if (enemy instanceof Crawlid) {
            if (enemy.isDead()) return CrawlidAnimationType.DEATH_LAND;

            switch (enemy.getCurrentState()) {
                case TURNING: return CrawlidAnimationType.TURN;
                case PATROLLING:
                case RUNNING:
                default: return CrawlidAnimationType.WALK;
            }
        }
        else if (enemy instanceof HuskHornHead) {
            if (enemy.isDead()) return HuskHornHeadAnimationType.DEATH_LAND;

            switch (enemy.getCurrentState()) {
                case IDLE: return HuskHornHeadAnimationType.IDLE;
                case RUNNING: return HuskHornHeadAnimationType.ATTACK; // Husk charges when running
                case TURNING: return HuskHornHeadAnimationType.TURN;
                case PATROLLING:
                default: return HuskHornHeadAnimationType.WALK;
            }
        }

        else if (enemy instanceof Mossfly){
            if (enemy.isDead()) return MossflyAnimationType.DEATH_LAND;
            switch (enemy.getCurrentState()) {
                case IDLE: return MossflyAnimationType.SHAKE;
                case RUNNING: return MossflyAnimationType.FLY;
            }
        }
        return null;
    }

    private void drawDebugHitboxes() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Cleaning: Removed the duplicate draw call you had for the Knight's main green hitbox.
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(knight.getHitBox().x, knight.getHitBox().y, knight.getHitBox().width, knight.getHitBox().height);

        if (knight.isAttacking()) {
            shapeRenderer.rect(knight.getNail().getHitBox().x, knight.getNail().getHitBox().y, knight.getNail().getHitBox().width, knight.getNail().getHitBox().height);
        }

        shapeRenderer.setColor(Color.GREEN);
        for (Block block : blocks) {
            shapeRenderer.rect(block.getBound().x, block.getBound().y, block.getBound().width, block.getBound().height);
        }
        for(EnemyModel enemy : enemies) {
            shapeRenderer.rect(enemy.getHitBox().x,enemy.getHitBox().y , enemy.getHitBox().width , enemy.getHitBox().height);
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        hudCamera.setToOrtho(false, width, height);
    }
}
