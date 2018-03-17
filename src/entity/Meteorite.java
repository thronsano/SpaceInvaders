package entity;

/**
 * Created by Szaman on 26.10.2017.
 */

import math.Vector2d;

public class Meteorite extends Projectile {
    //TODO: Set Sprite
    public Meteorite(float speed, Vector2d position) {
        super(speed, position, Vector2d.downwards());
    }
}
