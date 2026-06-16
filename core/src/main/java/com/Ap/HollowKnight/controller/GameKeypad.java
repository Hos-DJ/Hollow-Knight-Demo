package com.Ap.HollowKnight.controller;

import com.badlogic.gdx.Input;

public enum GameKeypad {
    RIGHT(Input.Keys.D),
    LEFT(Input.Keys.A),
    UP(Input.Keys.W),
    DOWN(Input.Keys.S),
    JUMP(Input.Keys.SPACE),
    DASH(Input.Keys.SHIFT_LEFT),
    ATTACK(Input.Keys.J),
    FOCUS(Input.Keys.E),
    VENGEFUL_SPIRIT(Input.Keys.Z);



    private int keyNumber ;
     private GameKeypad(int keyNumber) {
        this.keyNumber = keyNumber;
     }
     public int getKeyNumber() {
         return keyNumber;
     }
     public void setKeyNumber(int keyNumber) {
         this.keyNumber = keyNumber;
     }

    public static GameKeypad fromKeycode(int keycode) {
        for (GameKeypad key : values()) {
            if (key.getKeyNumber() == keycode) {
                return key;
            }
        }
        return null;
    }

}
