package entity;

import math.Vector2d;
import utility.GameState;

/**
 * Created by Szaman on 26.10.2017.
 */

public class Enemy extends Character {
    public Enemy(float speed, Vector2d position) {
        super(speed, position);
        spriteURL = new String[]{"file:drawable/kosmita1.png", "file:drawable/kosmita2.png"};
        moveDirection = Vector2d.downwards();
    }

    @Override
    public Missile shootMissile(float speed, Vector2d direction) {
        return super.shootMissile(speed, Vector2d.downwards());
    }

    @Override
    void belowBorder() {
        GameState.LifeAmount--;

        if(GameState.LifeAmount <= 0)
            GameState.GameRunning = false;

        super.belowBorder();
    }
}
