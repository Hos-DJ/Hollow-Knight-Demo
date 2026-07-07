package com.Ap.HollowKnight.model.map;

import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.badlogic.gdx.math.Rectangle;

public class DestructibleWall extends Block{
    private final int DAMAGE = 3 ;
    private int damageTaken = 0;
    private boolean isDestroyed = false;
    public DestructibleWall(Rectangle bound, BlockType type) {
        super(bound, type);
    }

    public void takeDamage(){
        damageTaken++;
        if(damageTaken == DAMAGE){
            damageTaken = 0;
            isDestroyed = true;
            GameEventMessenger.getInstance().dispatch(GameEvent.SECRET_DISCOVERED,this);
            setType(BlockType.DESTROYED_WALL);

        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
