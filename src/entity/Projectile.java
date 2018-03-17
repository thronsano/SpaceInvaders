package entity;

/**
 * Created by Szaman on 26.10.2017.
 */
import math.Vector2d;

public class Projectile extends Entity {
    public Projectile(float speed, Vector2d position, Vector2d direction) { //direction - ustawienie czy pocisk bedzie sie poruszal w gore/w dol
        super(speed, position);
        moveDirection = direction;
    }
}
