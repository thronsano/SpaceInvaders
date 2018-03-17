package entity;

import graphics.Settings;
import math.Vector2d;
import utility.GameState;

/**
 * Created by Szaman on 26.10.2017.
 */
public class Player extends Character {
    public Player(Vector2d position) {
        super(Settings.FRAMES_PER_SECOND, position);
        spriteURL = new String[]{"file:drawable/kosmita1.png"};
    }

    //TODO: Set Sprite


    @Override
    void onCollision(Entity objectHit) {
        GameState.LifeAmount--;

        if (GameState.LifeAmount <= 0)
            GameState.GameRunning = false;
    }

    @Override
    public Missile shootMissile(float speed, Vector2d direction) {
        return super.shootMissile(speed, Vector2d.upwards());
    }
}
