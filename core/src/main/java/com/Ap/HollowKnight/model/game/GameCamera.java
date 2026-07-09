package com.Ap.HollowKnight.model.game;

import com.Ap.HollowKnight.model.player.Knight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

public class GameCamera extends OrthographicCamera {
    private float shake = 0;
    private float shakeDuration = 0;
    private boolean lookingUp = false;
    private boolean lookingDown = false;

    public float getShake() {
        return shake;
    }

    public float getShakeDuration() {
        return shakeDuration;
    }

    public void initializeShake(float shake, float shakeDuration) {
        this.shake = shake;
        this.shakeDuration = shakeDuration;
    }

    public boolean isLookingUp() {
        return lookingUp;
    }

    public void setLookingUp(boolean lookingUp) {
        this.lookingUp = lookingUp;
    }

    public boolean isLookingDown() {
        return lookingDown;
    }

    public void setLookingDown(boolean lookingDown) {
        this.lookingDown = lookingDown;
    }

    public void shaker(float delta) {
        if (shakeDuration > 0) {

            this.position.x += (Math.random() - 0.5f) * shake;
            this.position.y += (Math.random() - 0.5f) * shake;

            shakeDuration -= delta;

            if (shakeDuration <= 0) {
                shakeDuration = 0;
                shake = 0;
            }
        }
    }
    public void updatePosition(Knight knight, Rectangle bounds, float delta) {
        float speed = 5.0f;
        float lerp = speed * Gdx.graphics.getDeltaTime();
        float targetX = knight.getHitBox().x + knight.getHitBox().width / 2f;
        float targetY = knight.getHitBox().y + knight.getHitBox().height / 2f;
        float CAMERA_OFFSET = 150.0f;
        if (this.lookingUp) {
            targetY += CAMERA_OFFSET;
        } else if (this.lookingDown) {
            targetY -= CAMERA_OFFSET;
        }

        this.position.x += (targetX - this.position.x) * lerp;
        this.position.y += (targetY - this.position.y) * lerp;

        if (bounds != null) {
            float halfWidth = bounds.width / 2f;
            float halfHeight = bounds.height / 2f;

            position.x = Math.clamp(position.x,
                bounds.x + halfWidth, bounds.x + bounds.width - halfWidth);

            position.y = Math.clamp(position.y,
                bounds.y + halfHeight, bounds.y + bounds.height - halfHeight);
        }
        this.shaker(delta);
        this.update();
    }

}
