package com.Ap.HollowKnight.model.game;

import com.Ap.HollowKnight.model.Knight.Knight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

public class GameCamera extends OrthographicCamera {
    private float shake = 0;
    private float shakeDuration = 0;
    private boolean lookingUp = false;
    private boolean lookingDown = false;
    private Rectangle bounds = null;
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
    public void updatePosition(Knight knight, float delta) {
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
            float halfWidth = this.viewportWidth / 2f;
            float halfHeight = this.viewportHeight / 2f;

            float minX = bounds.x + halfWidth;
            float maxX = bounds.x + bounds.width - halfWidth;

            if (minX <= maxX) {
                position.x = Math.clamp(position.x, minX, maxX);
            } else {
                position.x = bounds.x + bounds.width / 2f;
            }

            float minY = bounds.y + halfHeight;
            float maxY = bounds.y + bounds.height - halfHeight;

            if (minY <= maxY) {
                position.y = Math.clamp(position.y, minY, maxY);
            } else {
                position.y = bounds.y + bounds.height / 2f;
            }
        }
        this.shaker(delta);
        this.update();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
