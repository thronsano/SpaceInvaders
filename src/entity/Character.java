package entity;

import math.Vector2d;

/**
 * Created by Szaman on 26.10.2017.
 */

public class Character extends Entity {

    public Character(float speed, Vector2d position) {
        super(speed, position);
    }

    public Missile shootMissile(float speed, Vector2d direction) {
        return new Missile(speed, position.add(direction), direction); //postac tworzy pocisk, ktory bedzie poruszal sie w zaleznosci od typu postaci
    }
}
