package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.GameFlowController;
import com.Ap.HollowKnight.controller.GameProcessor;
import com.Ap.HollowKnight.model.enemy.*;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.GameCamera;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.player.PlayerCondition;
import com.Ap.HollowKnight.model.spells.HowlingWrath;
import com.Ap.HollowKnight.model.spells.VengefulSprit;
import com.Ap.HollowKnight.model.zote.Zote;
import com.Ap.HollowKnight.model.zote.ZoteState;
import com.Ap.HollowKnight.view.*;
import com.Ap.HollowKnight.view.animations.*;
import com.Ap.HollowKnight.view.sounds.MusicType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class GameScreen extends BaseScreen {

    private final LevelModel levelModel;
    private final TiledMap map;
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
    private ParticleEffect glowingDots;
    private BitmapFont font;

    private ArrayList<Block> blocks;
    private float stateTime = 0f;
    private PlayerCondition previousCondition;
    private ArrayList<EnemyModel> enemies;
    private Knight knight;
    private Zote zote;

    private float attackStateTime = 0f;
    private boolean wasAttacking = false;
    private float dashStateTime = 0f;
    private boolean wasDashing = false;
    private float vengefulSpellTime = 0f;
    private boolean wasVengefulActive = false;
    private float howlingSpellTime = 0f;
    private boolean wasHowlingActive = false;

    private final int[] backGrounds = {0};
    private final int[] foreGrounds = {1, 2};
    private final float KNIGHT_SPRITE_WIDTH = 90f;
    private final float KNIGHT_SPRITE_HEIGHT = 128f;
    private final float EFFECT_SPRITE_SIZE = 150.0f;
    private static final float PARALLAX_FACTOR = 0.5f;

    public GameScreen(HollowKnight game, TiledMap map) {
        super(game);
        this.levelModel = LevelModel.getInstance();
        this.map = map;
    }

    @Override
    public void show() {
        super.show();
        this.blocks = levelModel.getBlocks();
        this.batch = new SpriteBatch();
        this.hudCamera = new OrthographicCamera();
        audioManager.playMusic(MusicType.CITY_OF_TEARS);
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hud = levelModel.getHud();

        Vector2 spawnPoint = new Vector2(levelModel.getSpawnPoint().x, levelModel.getSpawnPoint().y);
        this.knight = levelModel.getKnight();
        this.zote = levelModel.getZote();
        knight.setOnGround(true);

        this.camera = new GameCamera();
        this.enemies = levelModel.getEnemies();
        this.inputController = new GameProcessor(knight, camera, enemies,zote);
        this.viewport = new ScreenViewport(camera);
        addInputProcessor(inputController);
        this.background = new Texture(Gdx.files.internal("background.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        this.font = loader.getFont("font_24");

        this.renderer = new OrthogonalTiledMapRenderer(map);
        this.shapeRenderer = new ShapeRenderer();


        this.gameFlowController = new GameFlowController(this.knight, map, blocks, inputController, enemies,zote);
        this.glowingDots = new ParticleEffect();
        glowingDots.load(Gdx.files.internal("particle/glowParticle.p"), Gdx.files.internal("particle"));
        glowingDots.start();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 0.05f);
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
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        float emitterX = camera.position.x;
        float emitterY = camera.position.y;
        glowingDots.setPosition(emitterX, emitterY);
        glowingDots.update(delta);
        glowingDots.draw(batch);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawEnemies();

        drawEnemyEffects();
        drawZote();
        drawKnightAndEffects();
        batch.end();
        if(zote.isPlayerNearby()&&(zote.getStatus()== ZoteState.IDLE||zote.getStatus()== ZoteState.TALKING)){
            drawZotePrompt();
        }

        renderer.render(foreGrounds);
        if(zote.isPlayerNearby()&&(zote.getStatus()== ZoteState.IDLE||zote.getStatus()== ZoteState.TALKING)){
            drawZotePrompt();
        }
        hud.render(knight, hudCamera,delta);

        drawDebugHitboxes();
        stage.act(delta);
        stage.draw();
    }

    private void updateTimers(float delta) {
        PlayerCondition current = knight.getPlayerCondition();
        if (current != previousCondition) {
            stateTime = 0f;
            previousCondition = current;
        }
        stateTime += delta;

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
        boolean vengefulActive = knight.getSpellManager().getVengefulSprit().isActive();
        if (vengefulActive && !wasVengefulActive) vengefulSpellTime = 0f;
        else if (vengefulActive) vengefulSpellTime += delta;
        wasVengefulActive = vengefulActive;

        boolean howlingActive = knight.getSpellManager().getHowlingWraiths().isActive();
        if (howlingActive && !wasHowlingActive) howlingSpellTime = 0f;
        else if (howlingActive) howlingSpellTime += delta;
        wasHowlingActive = howlingActive;
    }

    private void drawKnightAndEffects() {
        boolean isFacingRight = knight.getFacingDirection() == FacingDirection.RIGHT;

        KnightAnimationType drawKnight = getDrawKnight();
        Animation<TextureRegion> knightAnim = loader.getAnimation(drawKnight);
        knightAnim.setPlayMode(drawKnight.getPlayMode());
        TextureRegion currentKnightFrame = knightAnim.getKeyFrame(stateTime);

        float offsetX = (knight.getHitBox().width - KNIGHT_SPRITE_WIDTH) / 2f;
        float offsetY = 0f;

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

            isFacingRight,
            false
        );

        EffectAnimationType drawEffect = getDrawEffect();
        if (drawEffect != null) {
            float effectTimer;
            if (drawEffect == EffectAnimationType.DASH_EFFECT) {
                effectTimer = dashStateTime;
            } else if (drawEffect == EffectAnimationType.SOUL_BALL) {
                effectTimer = vengefulSpellTime;
            } else if (drawEffect == EffectAnimationType.SOUL_SCREAM) {
                effectTimer = howlingSpellTime;
            } else {
                effectTimer = attackStateTime;
            }
            Animation<TextureRegion> effectAnim = loader.getAnimation(drawEffect);
            TextureRegion currentEffectFrame = effectAnim.getKeyFrame(effectTimer);

            handleDrawingEffect(currentEffectFrame, drawEffect, isFacingRight);
        }
    }

    private void drawEnemies() {
        for (EnemyModel enemy : enemies) {
            AnimationType animType = getEnemyAnimationType(enemy);
            if (animType == null) continue;

            Animation<TextureRegion> animation = loader.getAnimation(animType);
            animation.setPlayMode(animType.getPlayMode());

            TextureRegion currentFrame = animation.getKeyFrame(stateTime);
            boolean isFacingRight = enemy.getFacingDirection() == FacingDirection.RIGHT;

            float drawWidth = enemy.getHitBox().width * 1.5f;
            float drawHeight = enemy.getHitBox().height * 1.5f;

            float offsetX = (enemy.getHitBox().width - drawWidth) / 2f;

            float offsetY = 0f;

            boolean flipX = !isFacingRight;
            if (enemy instanceof HuskHornHead || enemy instanceof CrystalGuardian) {
                flipX = isFacingRight;
            }

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
                flipX,
                false
            );
        }
    }

    private void drawEnemyEffects() {
        for (EnemyModel enemy : enemies) {
            if (enemy instanceof CrystalGuardian) {
                CrystalGuardian cg = (CrystalGuardian) enemy;
                boolean isFacingRight = cg.getFacingDirection() == FacingDirection.RIGHT;

                if (cg.getCurrentState() == EnemyState.CHARGING) {
                    Animation<TextureRegion> anim = loader.getAnimation(EffectAnimationType.LASER_CIRCLE);
                    TextureRegion frame = anim.getKeyFrame(stateTime, true);

                    float size = 150f;
                    float ex = cg.getHitBox().x + cg.getHitBox().width / 2f - size / 2f + (isFacingRight ? 30f : -30f);
                    float ey = cg.getHitBox().y + cg.getHitBox().height / 2f - size / 2f;

                    batch.draw(
                        frame.getTexture(), ex, ey, size, size,
                        frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight(),
                        !isFacingRight, false
                    );
                } else if (cg.getCurrentState() == EnemyState.SHOOTING) {
                    Animation<TextureRegion> anim = loader.getAnimation(EffectAnimationType.CRYSTAL_LASER);
                    TextureRegion frame = anim.getKeyFrame(stateTime, true);

                    Rectangle laserRect = cg.getLaser();

                    batch.draw(
                        frame.getTexture(),
                        laserRect.x, laserRect.y, laserRect.width, laserRect.height,
                        frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight(),
                        !isFacingRight, false
                    );
                }
            }
        }
    }
    private void drawZote(){
        boolean isFacingRight = zote.getFacingDirection() == FacingDirection.RIGHT;
        ZoteAnimationType drawZote = getZoteAnimationType();
        Animation<TextureRegion> zoteAnim = loader.getAnimation(drawZote);
        zoteAnim.setPlayMode(drawZote.getPlayMode());
        TextureRegion currentZoteFrame = zoteAnim.getKeyFrame(stateTime);

        batch.draw(
            currentZoteFrame.getTexture(),
            zote.getPosition().x ,
            zote.getPosition().y ,
            KNIGHT_SPRITE_WIDTH,
            KNIGHT_SPRITE_HEIGHT,
            currentZoteFrame.getRegionX(),
            currentZoteFrame.getRegionY(),
            currentZoteFrame.getRegionWidth(),
            currentZoteFrame.getRegionHeight(),

            isFacingRight,
            false
        );

    }

    private void handleDrawingEffect(TextureRegion effectFrame, EffectAnimationType type, boolean isFacingRight) {
        if (effectFrame == null) return;

        float effectX, effectY;

        if (type == EffectAnimationType.SOUL_BALL) {
            VengefulSprit vs = knight.getSpellManager().getVengefulSprit();
            effectX = vs.getPosition().x-vs.getHitBox().width / 2f;
            effectY = vs.getPosition().y -  vs.getHitBox().height ;
        }
        else if (type == EffectAnimationType.SOUL_SCREAM) {
            HowlingWrath hw = knight.getSpellManager().getHowlingWraiths();
            effectX = hw.getPosition().x-hw.getHitBox().width / 2f;
            effectY = hw.getPosition().y;
        }
        else if (type == EffectAnimationType.DASH_EFFECT) {
            effectX = knight.getHitBox().x + knight.getHitBox().width / 2f - EFFECT_SPRITE_SIZE / 2f;
            effectX += isFacingRight ? -30f : 30f;
            effectY = knight.getHitBox().y + knight.getHitBox().height / 2f - EFFECT_SPRITE_SIZE / 2f;
        } else {
            Rectangle nailHitBox = knight.getNail().getHitBox();
            effectX = nailHitBox.x + nailHitBox.width / 2f - EFFECT_SPRITE_SIZE / 2f;
            effectY = nailHitBox.y + nailHitBox.height / 2f - EFFECT_SPRITE_SIZE / 2f;
        }

        boolean flipEffectX = (type == EffectAnimationType.DASH_EFFECT || type == EffectAnimationType.SOUL_BALL) != isFacingRight;

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

    public void drawBackGroundImage() {
        float cameraX = camera.position.x;
        float cameraY = camera.position.y;
        float viewW = camera.viewportWidth;
        float viewH = camera.viewportHeight;

        float bgX = cameraX * PARALLAX_FACTOR;
        float bgY = cameraY * PARALLAX_FACTOR;

        float drawX = cameraX - viewW / 2f;
        float drawY = cameraY - viewH / 2f;

        int srcX = (int) (bgX) % background.getWidth();
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

    private void drawZotePrompt() {
        Zote zote = levelModel.getZote();
        if (!zote.isPlayerNearby()) return;

        float bobbingOffset = (float) Math.sin(Gdx.graphics.getFrameId() * 0.06f) * 4f;

        float promptX = zote.getHitBox().x + zote.getHitBox().width / 2f;
        float promptY = zote.getHitBox().y + zote.getHitBox().height + 20f + bobbingOffset;

        String textToRender = (zote.getStatus() ==ZoteState.TALKING) ? zote.showDialogue() : "[ E ]  Talk";

        GlyphLayout layout = new GlyphLayout(font, textToRender);
        float paddingX = 14f;
        float paddingY = 8f;
        float boxWidth = layout.width + (paddingX * 2);
        float boxHeight = layout.height + (paddingY * 2);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.6f);
        shapeRenderer.rect(promptX - boxWidth / 2f, promptY, boxWidth, boxHeight);
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.draw(batch, layout, promptX - layout.width / 2f, promptY + paddingY + layout.height);
        batch.end();

    }

    private KnightAnimationType getDrawKnight() {
        switch (knight.getPlayerCondition()) {
            case MOVING:
                return KnightAnimationType.KNIGHT_RUN;
            case DASHING:
                return KnightAnimationType.KNIGHT_DASH;
            case FALLING:
                return KnightAnimationType.KNIGHT_LANDING;
            case FOCUSING:
                return KnightAnimationType.KNIGHT_FOCUS;
            case MONARCHING:
                return KnightAnimationType.KNIGHT_DOUBLE_JUMP;
            case JUMPING:
                return KnightAnimationType.KNIGHT_AIRBORNE;
            case ATTACKING:
                switch (knight.getCurrentAttackDirection()) {
                    case UP:
                        return KnightAnimationType.KNIGHT_UP_SLASH;
                    case DOWN:
                        return KnightAnimationType.KNIGHT_DOWN_SLASH;
                    default:
                        return KnightAnimationType.KNIGHT_SLASH;
                }
            case VENGEFUL_SPIRIT:
                return KnightAnimationType.KNIGHT_FIREBALL_CAST;
            case HOWLING_WRATH:
                return KnightAnimationType.KNIGHT_UP_SLASH;
            default:
                return KnightAnimationType.KNIGHT_IDLE;
        }
    }


    private EffectAnimationType getDrawEffect() {
        if (knight.isDashing()) return EffectAnimationType.DASH_EFFECT;
        if (knight.getSpellManager().getVengefulSprit().isActive()) {
            return EffectAnimationType.SOUL_BALL;
        }
        if (knight.getSpellManager().getHowlingWraiths().isActive()) {
            return EffectAnimationType.SOUL_SCREAM;
        }
        if (knight.isAttacking()) {
            switch (knight.getCurrentAttackDirection()) {
                case UP:
                    return EffectAnimationType.NAIL_UP_SLASH;
                case DOWN:
                    return EffectAnimationType.NAIL_DOWN_SLASH;
                default:
                    return EffectAnimationType.NAIL_SLASH;
            }
        }
        return null;
    }

    private AnimationType getEnemyAnimationType(EnemyModel enemy) {
        if (enemy instanceof Crawlid) {
            if (enemy.isDead()) return CrawlidAnimationType.DEATH_LAND;

            switch (enemy.getCurrentState()) {
                case TURNING:
                    return CrawlidAnimationType.TURN;
                case PATROLLING:
                case RUNNING:
                default:
                    return CrawlidAnimationType.WALK;
            }
        } else if (enemy instanceof HuskHornHead) {
            if (enemy.isDead()) return HuskHornHeadAnimationType.DEATH_LAND;

            switch (enemy.getCurrentState()) {
                case IDLE:
                    return HuskHornHeadAnimationType.IDLE;
                case RUNNING:
                    return HuskHornHeadAnimationType.ATTACK;
                case TURNING:
                    return HuskHornHeadAnimationType.TURN;
                case PATROLLING:
                default:
                    return HuskHornHeadAnimationType.WALK;
            }
        } else if (enemy instanceof Mossfly) {
            if (enemy.isDead()) return MossflyAnimationType.DEATH_LAND;
            switch (enemy.getCurrentState()) {
                case IDLE:
                    return MossflyAnimationType.SHAKE;
                case RUNNING:
                    return MossflyAnimationType.FLY;
            }
        }
        else if (enemy instanceof CrystalGuardian) {
            if (enemy.isDead()) return CrystalGuardianAnimationType.DEATH_LAND;

            return switch (enemy.getCurrentState()) {
                case IDLE -> CrystalGuardianAnimationType.IDLE;
                case CHARGING -> CrystalGuardianAnimationType.SHOOT;
                case RUNNING -> CrystalGuardianAnimationType.RUN;
                case SHOOTING -> CrystalGuardianAnimationType.SHOOT;
                case TURNING -> CrystalGuardianAnimationType.TURN;
                default -> CrystalGuardianAnimationType.IDLE;
            };
        }

        return null;
    }

    private ZoteAnimationType getZoteAnimationType() {
        return switch (zote.getStatus()) {
            case IDLE -> ZoteAnimationType.IDLE;
            case KNOCKING -> ZoteAnimationType.KNOCK;
            case ENRAGED -> ZoteAnimationType.ATTACK;
            case WAKING -> ZoteAnimationType.WAKE;
            case RESTING -> ZoteAnimationType.REST;
            case TALKING -> ZoteAnimationType.TALK;
        };
    }

    private void drawDebugHitboxes() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(knight.getHitBox().x, knight.getHitBox().y, knight.getHitBox().width, knight.getHitBox().height);

        if (knight.isAttacking()) {
            shapeRenderer.rect(knight.getNail().getHitBox().x, knight.getNail().getHitBox().y, knight.getNail().getHitBox().width, knight.getNail().getHitBox().height);
        }

        shapeRenderer.setColor(Color.GREEN);
        for (Block block : blocks) {
            shapeRenderer.rect(block.getBound().x, block.getBound().y, block.getBound().width, block.getBound().height);
        }
        for (EnemyModel enemy : enemies) {
            shapeRenderer.rect(enemy.getHitBox().x, enemy.getHitBox().y, enemy.getHitBox().width, enemy.getHitBox().height);
        }

        shapeRenderer.setColor(Color.YELLOW);
        for (EnemyModel enemy : enemies) {
            if (enemy instanceof HuskHornHead) {
                Rectangle fov = ((HuskHornHead) enemy).getFov();
                shapeRenderer.rect(fov.x, fov.y, fov.width, fov.height);
            }
            else if (enemy instanceof CrystalGuardian) {
                CrystalGuardian cg = (CrystalGuardian) enemy;
                shapeRenderer.rect(cg.getFov().x, cg.getFov().y, cg.getFov().width, cg.getFov().height);

                shapeRenderer.setColor(Color.MAGENTA);
                shapeRenderer.rect(cg.getLaser().x, cg.getLaser().y, cg.getLaser().width, cg.getLaser().height);

            }
        }

        shapeRenderer.setColor(Color.CYAN);
        for (EnemyModel enemy : enemies) {
            if (enemy instanceof Mossfly) {
                Circle patrolCircle = ((Mossfly) enemy).getPatrolCircle();
                shapeRenderer.circle(patrolCircle.x, patrolCircle.y, patrolCircle.radius);
            }
        }

        shapeRenderer.setColor(Color.ORANGE);

        VengefulSprit vs = knight.getSpellManager().getVengefulSprit();
        if (vs.isActive()) {
            shapeRenderer.rect(vs.getHitBox().x, vs.getHitBox().y, vs.getHitBox().width, vs.getHitBox().height);
        }

        HowlingWrath hw = knight.getSpellManager().getHowlingWraiths();
        if (hw.isActive()) {
            shapeRenderer.rect(hw.getHitBox().x, hw.getHitBox().y, hw.getHitBox().width, hw.getHitBox().height);
        }
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        hudCamera.setToOrtho(false, width, height);
    }
}
