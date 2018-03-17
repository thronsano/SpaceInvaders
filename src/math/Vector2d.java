package math;

/**
 * Created by Szaman on 26.10.2017.
 */
public class Vector2d {
    float x;
    float y;

    public Vector2d(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //RETURNS VECTOR 2D MULTIPLIED BY FLOAT MULTIPLIER
    public Vector2d multiply(float multiplier) {
        return new Vector2d(x * multiplier, y * multiplier);
    }

    public Vector2d add(Vector2d vector) {
        return new Vector2d(x + vector.x, y + vector.y);
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public void setX(float x) {this.x = x;}

    public void setY(float y) {this.y = y;}

    public static Vector2d downwards() {
        return new Vector2d(0, 1);
    }

    public static Vector2d upwards() {
        return new Vector2d(0, -1);
    }

    public static Vector2d right() {
        return new Vector2d(1, 0);
    }

    public static Vector2d left() { return new Vector2d(-1, 0); }

    public static Vector2d zero() { return new Vector2d(0, 0);}
}
