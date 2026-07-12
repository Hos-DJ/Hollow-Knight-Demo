package com.Ap.HollowKnight.controller;

import com.badlogic.gdx.Input;

public enum GameKeypad {
    RIGHT(Input.Keys.D, 'D'),
    LEFT(Input.Keys.A, 'A'),
    UP(Input.Keys.W, 'W'),
    DOWN(Input.Keys.S, 'S'),
    JUMP(Input.Keys.SPACE, ' '),
    DASH(Input.Keys.SHIFT_LEFT, 'L'),
    ATTACK(Input.Keys.J, 'J'),
    FOCUS(Input.Keys.E, 'E'),
    INVENTORY(Input.Keys.I, 'I'),
    VENGEFUL_SPIRIT(Input.Keys.Z, 'Z'),
    HOWLING_WRATH(Input.Keys.X, 'X'),
    CHEAT_GOD(Input.Keys.NUM_1,'1'),
    CHEAT_SOUL(Input.Keys.NUM_2,'2'),
    CHEAT_BOSS(Input.Keys.NUM_3,'3'),
    CHEAT_EMERGENCY_HEALTH(Input.Keys.NUM_4,'4'),
    CHEAT_SPECTATOR(Input.Keys.NUM_5,'5'),
    CHEAT_DEADLY_NAIL(Input.Keys.NUM_6,'6'),
    CHEAT_ACTIVATOR(Input.Keys.Y, 'Y');

    private int keyNumber;
    private char keyChar;

    private final int defaultKeyNumber;
    private final char defaultKeyChar;

    GameKeypad(int keyNumber, char keyChar) {
        this.keyNumber = keyNumber;
        this.keyChar = keyChar;

        this.defaultKeyNumber = keyNumber;
        this.defaultKeyChar = keyChar;
    }

    public int getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(int keyNumber) {
        this.keyNumber = keyNumber;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public void setKeyChar(char keyChar) {
        this.keyChar = Character.toUpperCase(keyChar);
    }


    public void reset() {
        this.keyNumber = defaultKeyNumber;
        this.keyChar = defaultKeyChar;
    }


    public static void resetAll() {
        for (GameKeypad key : values()) {
            key.reset();
        }
    }

    public static GameKeypad fromKeycode(int keycode) {
        for (GameKeypad key : values()) {
            if (key.keyNumber == keycode) {
                return key;
            }
        }
        return null;
    }


    public static void changeKey(char keyChar, GameKeypad keypad) {
        keyChar = Character.toUpperCase(keyChar);

        keypad.keyChar = keyChar;
        keypad.keyNumber = Input.Keys.valueOf(String.valueOf(keyChar));
    }
}
