package com.Ap.HollowKnight.model.charms;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollectibleCharm {
    private Vector2 position;
    private Rectangle hitBox;
    private Rectangle interactionZone;
    private Charm charmType;
    private boolean pickedUp = false;
    private boolean playerNearby = false;

    public CollectibleCharm(Vector2 position, Charm charmType) {
        this.position = position;
        this.charmType = charmType;
        this.hitBox = new Rectangle(position.x, position.y, 60f, 60f);
        this.interactionZone = new Rectangle(position.x - 50f, position.y - 20f, 160f, 100f);
    }

    public Vector2 getPosition() { return position; }
    public Rectangle getHitBox() { return hitBox; }
    public Rectangle getInteractionZone() { return interactionZone; }
    public Charm getCharmType() { return charmType; }

    public boolean isPickedUp() { return pickedUp; }
    public void setPickedUp(boolean pickedUp) { this.pickedUp = pickedUp; }

    public boolean isPlayerNearby() { return playerNearby; }
    public void setPlayerNearby(boolean playerNearby) { this.playerNearby = playerNearby; }
}
