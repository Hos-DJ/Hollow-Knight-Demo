package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.GameFlowController;
import com.Ap.HollowKnight.controller.GameProcessor;
import com.Ap.HollowKnight.controller.SaveManager;
import com.Ap.HollowKnight.controller.SettingsController;
import com.Ap.HollowKnight.controller.events.*;
import com.Ap.HollowKnight.model.boss.FalseKnight;
import com.Ap.HollowKnight.model.boss.FalseKnightPhase;
import com.Ap.HollowKnight.model.boss.ShockWave;
import com.Ap.HollowKnight.model.enemy.*;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.GameCamera;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.DestructibleWall;
import com.Ap.HollowKnight.model.charms.CollectibleCharm;
import com.Ap.HollowKnight.model.Knight.Knight;
import com.Ap.HollowKnight.model.Knight.PlayerCondition;
import com.Ap.HollowKnight.model.spells.HowlingWrath;
import com.Ap.HollowKnight.model.spells.VengefulSprit;
import com.Ap.HollowKnight.model.zote.Zote;
import com.Ap.HollowKnight.model.zote.ZoteState;
import com.Ap.HollowKnight.view.PlayerHUD;
import com.Ap.HollowKnight.view.animations.*;
import com.Ap.HollowKnight.view.screen.modals.InventoryModal;
import com.Ap.HollowKnight.view.screen.modals.PauseModal;
import com.Ap.HollowKnight.view.screen.modals.VictoryModal;
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
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class GameScreen extends BaseScreen {

    private final LevelModel levelModel;
    private final TiledMap map;
    private GameProcessor inputController;
    private GameFlowController gameFlowController;

    private ScreenViewport viewport;
    private GameCamera camera;
    private OrthographicCamera hudCamera;
    private ShapeRenderer shapeRenderer;
    private PlayerHUD hud;
    private OrthogonalTiledMapRenderer renderer;
    private Texture background;
    private ParticleEffect glowingDots;
    private BitmapFont font;
    private PauseModal pauseModal;
    private VictoryModal victoryModal;
    private InventoryModal inventoryModal;
    private GameEventMessenger messenger;
    private AchievementListener achievementListener;
    private AudioListener audioListener;
    private CameraListener cameraListener;
    private boolean isInventoryOpen = false;
    private boolean isinBossArena = false;
    private boolean secretRevealed = false;


    private ArrayList<Block> blocks;
    private float stateTime = 0f;
    private PlayerCondition previousCondition;
    private ArrayList<EnemyModel> enemies;
    private Knight knight;
    private Zote zote;
    private GameEventListener saveListener;
    private StatisticsListener statsListener;
    private GameEventListener bossRoomListener;
    private GameEventListener wallHitListener;

    private float wallHitTimer = -1f;
    private float attackStateTime = 0f;
    private boolean wasAttacking = false;
    private float dashStateTime = 0f;
    private boolean wasDashing = false;
    private float vengefulSpellTime = 0f;
    private boolean wasVengefulActive = false;
    private float howlingSpellTime = 0f;
    private boolean wasHowlingActive = false;
    private boolean gameFinished = false;

    private FalseKnightPhase previousBossPhase = FalseKnightPhase.IDLE;
    private float bossStateTime = 0f;

    private boolean isVictoryTimerRunning = false;
    private float victoryTimer = 10.0f;
    private GameEventListener victoryListener;
    private boolean wasPaused = false;
    private final int[] backGrounds = {0, 1};
    private final int[] foreGrounds = {2, 3};
    private final int[] secretForeGrounds = {5};
    private final int[] bossDecor = {6, 7};
    private final float KNIGHT_SPRITE_WIDTH = 120;
    private final float KNIGHT_SPRITE_HEIGHT = 150;
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
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.hud = levelModel.getHud();

        Vector2 spawnPoint = new Vector2(levelModel.getSpawnPoint().x, levelModel.getSpawnPoint().y);
        this.knight = levelModel.getKnight();
        this.zote = levelModel.getZote();
        int pendingSlot = SaveManager.getInstance().getSlotInPending();
        if (pendingSlot != -1) {
            SaveManager.getInstance().loadGame(pendingSlot);
        }
        knight.setOnGround(true);

        this.camera = new GameCamera();
        this.enemies = levelModel.getEnemies();
        this.inputController = new GameProcessor(knight, camera, enemies, zote);
        this.viewport = new ScreenViewport(camera);
        addInputProcessor(inputController);
        this.background = new Texture(Gdx.files.internal("background.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        this.font = loader.getFont("font_24");

        this.renderer = new OrthogonalTiledMapRenderer(map);
        this.shapeRenderer = new ShapeRenderer();


        this.gameFlowController = new GameFlowController(this.knight, map, blocks, inputController, enemies, zote, levelModel.getGateBlock());
        this.glowingDots = new ParticleEffect();
        glowingDots.load(Gdx.files.internal("particle/glowParticle.p"), Gdx.files.internal("particle"));
        glowingDots.start();

        pauseModal = new PauseModal(this) {
            @Override
            public void onResume() {
                gameFlowController.setPaused(false);
                inputController.setPaused(false);
            }
        };

        if (SettingsController.getInstance().isReturnToPauseMenu()) {
            gameFlowController.setPaused(true);
            inputController.setPaused(true);
            wasPaused = true;
            pauseModal.show();
            SettingsController.getInstance().setReturnToPauseMenu(false);
        }
        inventoryModal = new InventoryModal(this) {
            @Override
            public void onHide() {
                isInventoryOpen = false;
                inputController.setInventory(false);
            }
        };
        saveListener = (event, data) -> {
            SaveManager.getInstance().saveGame(pendingSlot, isinBossArena, null);
            SaveManager.getInstance().clearSlotInPending();
        };
        messenger = GameEventMessenger.getInstance();
        achievementListener = new AchievementListener(this);
        messenger.addListener(GameEvent.BOSS_DEFEATED, achievementListener);
        messenger.addListener(GameEvent.ENEMY_KILLED, achievementListener);
        messenger.addListener(GameEvent.GAME_COMPLETED, achievementListener);
        messenger.addListener(GameEvent.SECRET_DISCOVERED, achievementListener);
        messenger.addListener(GameEvent.SPEED_RUN, achievementListener);
        messenger.addListener(GameEvent.SAVE_GAME, saveListener);
        audioListener = new AudioListener();
        addAudioEvents(audioListener);
        messenger.dispatch(GameEvent.ENTER_CROSSROADS, null);
        cameraListener = new CameraListener(camera);
        addCameraEvents(cameraListener);
        statsListener = StatisticsListener.getInstance();
        messenger.addListener(GameEvent.ENEMY_KILLED, statsListener);
        messenger.addListener(GameEvent.PLAYER_DEATH, statsListener);
        victoryListener = (event, data) -> {
            isVictoryTimerRunning = true;
            System.out.println("Boss defeated! Victory timer started...");
        };
        messenger.addListener(GameEvent.BOSS_DEFEATED, victoryListener);
        bossRoomListener = (event, data) -> {
            isinBossArena = false;
            gameFlowController.setReachedTheBoss(false);
            gameFlowController.resetDoors();
        };
        messenger.addListener(GameEvent.PLAYER_DEATH, bossRoomListener);

        wallHitListener = (event, data) -> {
            wallHitTimer = 0f;
        };
        messenger.addListener(GameEvent.ATTACKING_WALL, wallHitListener);

    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 0.05f);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (inputController.isInventoryTriggered()) {
            inputController.setInventoryTriggered(false);

            isInventoryOpen = !isInventoryOpen;

            inputController.setInventory(isInventoryOpen);

            if (isInventoryOpen) {
                inventoryModal.show();
            } else {
                inventoryModal.hide();
            }
        }

        pauseModal.setVisible(gameFlowController.isPaused());
        boolean currentPaused = gameFlowController.isPaused();
        if (currentPaused && !wasPaused && !gameFinished) {
            pauseModal.show();
        } else if (!currentPaused && wasPaused) {
            pauseModal.hide();
        }
        wasPaused = currentPaused;
        if (!currentPaused) {
            updateTimers(delta);
            gameFlowController.update(delta);
            camera.updatePosition(knight, delta);
            statsListener.update(delta);
        }

        if (isVictoryTimerRunning && !currentPaused) {
            victoryTimer -= delta;
            if (victoryTimer <= 5) {
                messenger.dispatch(GameEvent.GAME_COMPLETED, null);
            }
            if (victoryTimer <= 0) {
                isVictoryTimerRunning = false;
                gameFinished = true;
                gameFlowController.setPaused(true);
                inputController.setPaused(true);
                victoryModal = new VictoryModal(this);
                victoryModal.show();
            }
        }

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
        drawCollectibleCharm();
        drawKnightAndEffects();
        drawWallHitEffect(delta);
        drawBossShockWaves();
        batch.end();
        if (zote.isPlayerNearby() && (zote.getStatus() == ZoteState.IDLE || zote.getStatus() == ZoteState.TALKING)) {
            drawZotePrompt();
        }

        renderer.render(foreGrounds);
        secretRevealed = inputController.isWallDestroyed();
        if (!secretRevealed) {
            renderer.render(secretForeGrounds);
        }
        isinBossArena = gameFlowController.isReachedTheBoss();
        if (isinBossArena) {
            renderer.render(bossDecor);
        }


        if (zote.isPlayerNearby() && (zote.getStatus() == ZoteState.IDLE || zote.getStatus() == ZoteState.TALKING)) {
            drawZotePrompt();
        }
        if (inputController.getNearbyCharm() != null && !inputController.getNearbyCharm().isPickedUp()) {
            drawCharmPrompt(inputController.getNearbyCharm());
        }
        hud.render(knight, hudCamera, delta);

        drawDebugHitboxes();
        super.render(delta);
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

        for (EnemyModel enemy : enemies) {
            if (enemy instanceof FalseKnight boss) {
                FalseKnightPhase currentPhase = boss.getCurrentPhase();
                if (currentPhase != previousBossPhase) {
                    bossStateTime = 0f;
                    previousBossPhase = currentPhase;
                }
                bossStateTime += delta;
            }
        }
    }

    private void drawCollectibleCharm() {
        for (CollectibleCharm charm : levelModel.getCollectibleCharms()) {
            if (!charm.isPickedUp()) {
                Texture tex = loader.getTexture("Ui/Charms/" + charm.getCharmType().name() + ".png");
                batch.draw(tex, charm.getPosition().x, charm.getPosition().y, 60f, 60f);
            }
        }
    }

    private void drawKnightAndEffects() {
        boolean isFacingRight = knight.getFacingDirection() == FacingDirection.RIGHT;

        KnightAnimationType drawKnight = getDrawKnight();
        Animation<TextureRegion> knightAnim = loader.getAnimation(drawKnight);
        knightAnim.setPlayMode(drawKnight.getPlayMode());
        TextureRegion currentKnightFrame;

        if (drawKnight == KnightAnimationType.KNIGHT_RUN) {
            float frameDuration = knightAnim.getFrameDuration();
            int totalFrames = 13;
            int loopStartFrame = 5;
            int loopLength = 8;

            int frameNumber = (int) (stateTime / frameDuration);
            int frameIndex;

            if (frameNumber < totalFrames) {
                frameIndex = frameNumber;
            } else {
                int loopFrames = frameNumber - totalFrames;
                frameIndex = loopStartFrame + (loopFrames % loopLength);
            }

            currentKnightFrame = knightAnim.getKeyFrame(frameIndex * frameDuration);

        } else {
            currentKnightFrame = knightAnim.getKeyFrame(stateTime);
        }
        float offsetX = (knight.getHitBox().width - KNIGHT_SPRITE_WIDTH) / 2f;
        float offsetY = 0f;
        if (knight.isInvincible()) {
            float alpha = ((int) (stateTime * 15) % 2 == 0) ? 0.2f : 1.0f;
            batch.setColor(1f, 1f, 1f, alpha);
        }
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
            } else if (drawEffect == EffectAnimationType.SOUL_BALL || drawEffect == EffectAnimationType.SHADOW_SOUL_BALL) {
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
            if (enemy.isDead()) {
                animation.setPlayMode(Animation.PlayMode.NORMAL);
            }

            float timerToUse = (enemy instanceof FalseKnight) ? bossStateTime : stateTime;
            TextureRegion currentFrame = animation.getKeyFrame(timerToUse);
            if (enemy instanceof CrystalGuardian) {
                CrystalGuardian cg = (CrystalGuardian) enemy;
                if (cg.getCurrentState() == EnemyState.CHARGING) {
                    int frameIndex = Math.min((int) (timerToUse / animation.getFrameDuration()), 2);
                    currentFrame = animation.getKeyFrames()[frameIndex];
                } else if (cg.getCurrentState() == EnemyState.SHOOTING) {
                    int frameIndex = 3 ;
                    frameIndex = Math.min(frameIndex, animation.getKeyFrames().length - 1);
                    currentFrame = animation.getKeyFrames()[frameIndex];
                }
            }
            boolean isFacingRight = enemy.getFacingDirection() == FacingDirection.RIGHT;

            float drawWidth = enemy.getHitBox().width * 1.5f;
            float drawHeight = enemy.getHitBox().height * 1.5f;

            float offsetX = (enemy.getHitBox().width - drawWidth) / 2f;

            float offsetY = 0f;

            boolean flipX = isFacingRight;


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

                if (cg.getCurrentState() == EnemyState.SHOOTING) {
                    Animation<TextureRegion> anim = loader.getAnimation(EffectAnimationType.CRYSTAL_LASER);
                    TextureRegion frame;
                    int frameIndex = 8 ;
                    frameIndex = Math.min(frameIndex, anim.getKeyFrames().length - 1);
                    frame = anim.getKeyFrames()[frameIndex];
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


private void drawZote() {
    boolean isFacingRight = zote.getFacingDirection() == FacingDirection.RIGHT;
    ZoteAnimationType drawZote = getZoteAnimationType();
    Animation<TextureRegion> zoteAnim = loader.getAnimation(drawZote);
    zoteAnim.setPlayMode(drawZote.getPlayMode());
    TextureRegion currentZoteFrame = zoteAnim.getKeyFrame(stateTime);

    batch.draw(
        currentZoteFrame.getTexture(),
        zote.getPosition().x,
        zote.getPosition().y,
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
    float multiplier = 1;

    if (type == EffectAnimationType.SOUL_BALL || type == EffectAnimationType.SHADOW_SOUL_BALL) {
        VengefulSprit vs = knight.getSpellManager().getVengefulSprit();
        effectX = vs.getPosition().x - vs.getHitBox().width / 2f;
        effectY = vs.getPosition().y - vs.getHitBox().height;
    } else if (type == EffectAnimationType.SOUL_SCREAM) {
        HowlingWrath hw = knight.getSpellManager().getHowlingWraiths();
        effectX = hw.getPosition().x - hw.getHitBox().width / 2f;
        effectY = hw.getPosition().y;
        multiplier = 2.0f;
    } else if (type == EffectAnimationType.DASH_EFFECT) {
        effectX = knight.getHitBox().x + knight.getHitBox().width / 2f - EFFECT_SPRITE_SIZE / 2f;
        effectX += isFacingRight ? -30f : 30f;
        effectY = knight.getHitBox().y + knight.getHitBox().height / 2f - EFFECT_SPRITE_SIZE / 2f;
    } else {

        Rectangle nailHitBox = knight.getNail().getHitBox();
        effectX = nailHitBox.x + nailHitBox.width / 2f - EFFECT_SPRITE_SIZE / 2f;
        effectY = nailHitBox.y + nailHitBox.height * 0.75f - EFFECT_SPRITE_SIZE / 2f;
    }

    boolean flipEffectX = (type == EffectAnimationType.DASH_EFFECT ||
        type == EffectAnimationType.SOUL_BALL ||
        type == EffectAnimationType.SHADOW_SOUL_BALL) != isFacingRight;

    batch.draw(
        effectFrame.getTexture(),
        effectX,
        effectY,
        EFFECT_SPRITE_SIZE * multiplier,
        EFFECT_SPRITE_SIZE * multiplier,
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

    I18NBundle bundle = game.getBundle();

    float bobbingOffset = (float) Math.sin(Gdx.graphics.getFrameId() * 0.06f) * 4f;

    float promptX = zote.getHitBox().x + zote.getHitBox().width / 2f;
    float promptY = zote.getHitBox().y + zote.getHitBox().height + 20f + bobbingOffset;

    String textToRender = (zote.getStatus() == ZoteState.TALKING) ? zote.showDialogue(bundle) : bundle.get("zote_talk_prompt");

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

private void drawCharmPrompt(CollectibleCharm charm) {
    float bobbingOffset = (float) Math.sin(Gdx.graphics.getFrameId() * 0.06f) * 4f;

    float promptX = charm.getHitBox().x + charm.getHitBox().width / 2f;
    float promptY = charm.getHitBox().y + charm.getHitBox().height + 30f + bobbingOffset;

    String textToRender = "Press [E] to Pick Up";

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
            return (knight.hasSharpShadow()) ? KnightAnimationType.KNIGHT_SHADOW_DASH : KnightAnimationType.KNIGHT_DASH;
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
        case WALL_SLIDING:
            return KnightAnimationType.KNIGHT_WALL_SLIDE;
        case WALL_JUMPING:
            return KnightAnimationType.KNIGHT_WALL_JUMP;
        default:
            return KnightAnimationType.KNIGHT_IDLE;
    }
}


private EffectAnimationType getDrawEffect() {
    if (knight.isDashing()) return EffectAnimationType.DASH_EFFECT;
    if (knight.getSpellManager().getVengefulSprit().isActive()) {
        if (knight.hasVoidHeart()) {
            return EffectAnimationType.SHADOW_SOUL_BALL;
        } else {
            return EffectAnimationType.SOUL_BALL;
        }
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
    } else if (enemy instanceof CrystalGuardian) {
        if (enemy.isDead()) return CrystalGuardianAnimationType.DEATH_LAND;

        return switch (enemy.getCurrentState()) {
            case IDLE -> CrystalGuardianAnimationType.IDLE;
            case CHARGING -> CrystalGuardianAnimationType.SHOOT;
            case RUNNING -> CrystalGuardianAnimationType.RUN;
            case SHOOTING -> CrystalGuardianAnimationType.SHOOT;
            case TURNING -> CrystalGuardianAnimationType.TURN;
            default -> CrystalGuardianAnimationType.IDLE;
        };
    } else if (enemy instanceof FalseKnight boss) {
        return switch (boss.getCurrentPhase()) {
            case IDLE -> FalseKnightAnimationType.IDLE;
            case WINDUP -> FalseKnightAnimationType.CHARGING_THE_MACE;
            case SLAMMING -> FalseKnightAnimationType.MACE_SLAM;
            case RECOVERING -> FalseKnightAnimationType.RETURN_FROM_ATTACK_TO_IDLE;
            case RUNNING -> FalseKnightAnimationType.RUN;
            case JUMPING_ATTACK -> FalseKnightAnimationType.JUMP_ATTACK;
            case JUMPING_DEFENSE -> FalseKnightAnimationType.JUMP_DEFENSE;
            case LANDING -> FalseKnightAnimationType.LAND;
            case GETTING_STUNNED -> FalseKnightAnimationType.GETTING_STUNNED;
            case STUNNED -> FalseKnightAnimationType.STUNNED_BODY;
            case WAKING_UP -> FalseKnightAnimationType.STUN_TO_IDLE;
            case DEAD -> FalseKnightAnimationType.STUNNED_BODY;
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
        } else if (enemy instanceof CrystalGuardian) {
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
    shapeRenderer.setColor(Color.MAGENTA);
    for (EnemyModel enemy : enemies) {
        if (enemy instanceof FalseKnight) {
            FalseKnight boss = (FalseKnight) enemy;
            shapeRenderer.rect(
                boss.getMaceHitBox().x,
                boss.getMaceHitBox().y,
                boss.getMaceHitBox().width,
                boss.getMaceHitBox().height
            );
        }
    }
    shapeRenderer.setColor(Color.ORANGE);
    for (EnemyModel enemy : enemies) {
        if (enemy instanceof FalseKnight) {
            FalseKnight boss = (FalseKnight) enemy;
            for (ShockWave wave : boss.getShockWaves()) {
                shapeRenderer.rect(wave.getHitBox().x, wave.getHitBox().y, wave.getHitBox().width, wave.getHitBox().height);
            }
        }
    }
    shapeRenderer.end();
}

private void drawWallHitEffect(float delta) {
    if (wallHitTimer >= 0) {
        wallHitTimer += delta;

        EffectAnimationType hitAnimType = EffectAnimationType.DASH_EFFECT;
        Animation<TextureRegion> anim = loader.getAnimation(hitAnimType);

        if (anim.isAnimationFinished(wallHitTimer)) {
            wallHitTimer = -1f;
        } else {
            TextureRegion frame = anim.getKeyFrame(wallHitTimer);
            DestructibleWall wall = levelModel.getDestructibleWall();

            if (wall != null && !wall.isDestroyed()) {
                float effectX = wall.getBound().x - (EFFECT_SPRITE_SIZE / 2f);
                float effectY = wall.getBound().y + (wall.getBound().height / 2f) - (EFFECT_SPRITE_SIZE / 2f);
                effectY += (float) (Math.random() * 40 - 20);

                batch.draw(
                    frame.getTexture(),
                    effectX,
                    effectY,
                    EFFECT_SPRITE_SIZE,
                    EFFECT_SPRITE_SIZE,
                    frame.getRegionX(),
                    frame.getRegionY(),
                    frame.getRegionWidth(),
                    frame.getRegionHeight(),
                    false,
                    false
                );
            }
        }
    }
}

private void drawBossShockWaves() {
    for (EnemyModel enemy : enemies) {
        if (enemy instanceof FalseKnight) {
            FalseKnight boss = (FalseKnight) enemy;

            EffectAnimationType shockWaveAnimType = EffectAnimationType.SHOCK_WAVE;
            Animation<TextureRegion> anim = loader.getAnimation(shockWaveAnimType);
            anim.setPlayMode(Animation.PlayMode.LOOP);

            TextureRegion frame = anim.getKeyFrame(stateTime);

            for (ShockWave wave : boss.getShockWaves()) {
                if (wave.isActive()) {
                    boolean isFacingRight = wave.getFacingDirection() == FacingDirection.RIGHT;

                    float drawWidth = wave.getHitBox().width * 2.0f;
                    float drawHeight = wave.getHitBox().height * 1.2f;

                    float drawX = wave.getHitBox().x - (drawWidth - wave.getHitBox().width) / 2f;
                    float drawY = wave.getHitBox().y;

                    batch.draw(
                        frame.getTexture(),
                        drawX,
                        drawY,
                        drawWidth,
                        drawHeight,
                        frame.getRegionX(),
                        frame.getRegionY(),
                        frame.getRegionWidth(),
                        frame.getRegionHeight(),
                        !isFacingRight,
                        false
                    );
                }
            }
        }
    }
}

private void addAudioEvents(AudioListener audioListener) {
    messenger.addListener(GameEvent.ENTER_CROSSROADS, audioListener);
    messenger.addListener(GameEvent.ENTER_GREENPATH, audioListener);
    messenger.addListener(GameEvent.ENTERED_BOSS_ROOM, audioListener);
    messenger.addListener(GameEvent.BOSS_DEFEATED, audioListener);

    messenger.addListener(GameEvent.PLAYER_STARTED_WALKING, audioListener);
    messenger.addListener(GameEvent.PLAYER_ENDED_WALKING, audioListener);
    messenger.addListener(GameEvent.PLAYER_FOCUS_START, audioListener);
    messenger.addListener(GameEvent.PLAYER_FOCUS_END, audioListener);
    messenger.addListener(GameEvent.PLAYER_STARTED_WALL_SLIDING, audioListener);
    messenger.addListener(GameEvent.PLAYER_ENDED_WALL_SLIDING, audioListener);
    messenger.addListener(GameEvent.ZOTE_STARTED_ATTACKING, audioListener);
    messenger.addListener(GameEvent.ZOTE_ENDED_ATTACKING, audioListener);
    messenger.dispatch(GameEvent.ENTER_CROSSROADS, null);
    messenger.addListener(GameEvent.PLAYER_DASH, audioListener);
    messenger.addListener(GameEvent.PLAYER_MONARCH_WINGS, audioListener);
    messenger.addListener(GameEvent.PLAYER_ATTACKING, audioListener);
    messenger.addListener(GameEvent.POGO_SPIKE, audioListener);
    messenger.addListener(GameEvent.PLAYER_VENGEFUL, audioListener);
    messenger.addListener(GameEvent.PLAYER_HOWLING, audioListener);

    messenger.addListener(GameEvent.ATTACKING_WALL, audioListener);
    messenger.addListener(GameEvent.WALL_DESTROYED, audioListener);

    messenger.addListener(GameEvent.ENEMY_HURT, audioListener);
    messenger.addListener(GameEvent.ENEMY_KILLED, audioListener);
    messenger.addListener(GameEvent.PLAYER_HURT, audioListener);
    messenger.addListener(GameEvent.PLAYER_DOUBLE_HURT, audioListener);
    messenger.addListener(GameEvent.PLAYER_DEATH, audioListener);
    messenger.addListener(GameEvent.PLAYER_SOUL_GAIN, audioListener);
    messenger.addListener(GameEvent.SLAM_MACE, audioListener);

    messenger.addListener(GameEvent.ZOTE_IS_TALKING, audioListener);
}

public void addCameraEvents(CameraListener cameraListener) {
    messenger.addListener(GameEvent.PLAYER_HURT, cameraListener);
    messenger.addListener(GameEvent.PLAYER_DOUBLE_HURT, cameraListener);
    messenger.addListener(GameEvent.SLAM_MACE, cameraListener);

    messenger.addListener(GameEvent.PLAYER_VENGEFUL, cameraListener);
    messenger.addListener(GameEvent.PLAYER_HOWLING, cameraListener);
}

@Override
public void resize(int width, int height) {
    super.resize(width, height);
    camera.setToOrtho(false, width, height);
    hudCamera.setToOrtho(false, width, height);
}

public boolean isSecretRevealed() {
    return secretRevealed;
}

public void setSecretRevealed(boolean secretRevealed) {
    this.secretRevealed = secretRevealed;
}

public boolean isIsinBossArena() {
    return isinBossArena;
}

public void setIsinBossArena(boolean isinBossArena) {
    this.isinBossArena = isinBossArena;
}

@Override
public void hide() {
    super.hide();
    isVictoryTimerRunning = false;
    victoryTimer = 10.0f;
    if (messenger != null) {
        messenger.removeListener(GameEvent.BOSS_DEFEATED, achievementListener);
        messenger.removeListener(GameEvent.ENEMY_KILLED, achievementListener);
        messenger.removeListener(GameEvent.GAME_COMPLETED, achievementListener);
        messenger.removeListener(GameEvent.SECRET_DISCOVERED, achievementListener);

        messenger.removeListener(GameEvent.SAVE_GAME, saveListener);

        messenger.removeListener(GameEvent.ENTER_CROSSROADS, audioListener);
        messenger.removeListener(GameEvent.ENTER_GREENPATH, audioListener);
        messenger.removeListener(GameEvent.ENTERED_BOSS_ROOM, audioListener);
        messenger.removeListener(GameEvent.BOSS_DEFEATED, audioListener);
        messenger.removeListener(GameEvent.PLAYER_STARTED_WALKING, audioListener);
        messenger.removeListener(GameEvent.PLAYER_ENDED_WALKING, audioListener);
        messenger.removeListener(GameEvent.PLAYER_FOCUS_START, audioListener);
        messenger.removeListener(GameEvent.PLAYER_FOCUS_END, audioListener);
        messenger.removeListener(GameEvent.PLAYER_STARTED_WALL_SLIDING, audioListener);
        messenger.removeListener(GameEvent.PLAYER_ENDED_WALL_SLIDING, audioListener);
        messenger.removeListener(GameEvent.ZOTE_STARTED_ATTACKING, audioListener);
        messenger.removeListener(GameEvent.ZOTE_ENDED_ATTACKING, audioListener);
        messenger.removeListener(GameEvent.PLAYER_DASH, audioListener);
        messenger.removeListener(GameEvent.PLAYER_MONARCH_WINGS, audioListener);
        messenger.removeListener(GameEvent.PLAYER_ATTACKING, audioListener);
        messenger.removeListener(GameEvent.POGO_SPIKE, audioListener);
        messenger.removeListener(GameEvent.PLAYER_VENGEFUL, audioListener);
        messenger.removeListener(GameEvent.PLAYER_HOWLING, audioListener);
        messenger.removeListener(GameEvent.ATTACKING_WALL, audioListener);
        messenger.removeListener(GameEvent.WALL_DESTROYED, audioListener);
        messenger.removeListener(GameEvent.ENEMY_HURT, audioListener);
        messenger.removeListener(GameEvent.ENEMY_KILLED, audioListener);
        messenger.removeListener(GameEvent.PLAYER_HURT, audioListener);
        messenger.removeListener(GameEvent.PLAYER_DOUBLE_HURT, audioListener);
        messenger.removeListener(GameEvent.PLAYER_DEATH, audioListener);
        messenger.removeListener(GameEvent.PLAYER_SOUL_GAIN, audioListener);
        messenger.removeListener(GameEvent.SLAM_MACE, audioListener);
        messenger.removeListener(GameEvent.ZOTE_IS_TALKING, audioListener);

        messenger.removeListener(GameEvent.PLAYER_HURT, cameraListener);
        messenger.removeListener(GameEvent.PLAYER_DOUBLE_HURT, cameraListener);
        messenger.removeListener(GameEvent.SLAM_MACE, cameraListener);
        messenger.removeListener(GameEvent.PLAYER_VENGEFUL, cameraListener);
        messenger.removeListener(GameEvent.PLAYER_HOWLING, cameraListener);


        messenger.removeListener(GameEvent.ENEMY_KILLED, statsListener);
        messenger.removeListener(GameEvent.PLAYER_DEATH, statsListener);

        messenger.removeListener(GameEvent.BOSS_DEFEATED, victoryListener);

        messenger.removeListener(GameEvent.PLAYER_DEATH, bossRoomListener);
        messenger.removeListener(GameEvent.ATTACKING_WALL, wallHitListener);
    }
}
}
