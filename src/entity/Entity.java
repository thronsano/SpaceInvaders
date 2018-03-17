package entity;

import graphics.Settings;
import math.Vector2d;

/**
 * Created by Szaman on 26.10.2017.
 */
public class Entity {
    String[] spriteURL; // Adres obrazka przedstawiającego obiekt
    Vector2d position;
    Vector2d moveDirection = Vector2d.downwards();

    private float speed = 1f; // Ile kratek obiekt porusza się na sekundę

    public Entity(float speed, Vector2d position) {
        try {
            setSpeed(speed);
        } catch (Exception ex) {
            setSpeed(Settings.FRAMES_PER_SECOND);
        }

        setPosition(position);
    }

    /**
     * Wykonuje czynności przed wygenerowaniem klatki
     */
    public void update() {
        move();
    }

    /**
     * Zmienia kierunek poruszania się
     * @param moveDirection nowy kierunek ruchu
     */
    public void changeMoveDirection(Vector2d moveDirection) {
        this.moveDirection = moveDirection;
    }

    /**
     * Ustawia prędkość maksymalną ograniczoną z góry poprzez parametr FRAMES_PER_SECOND w klasie Settings
     * @param speed predkosc danego obiektu
     */
    void setSpeed(float speed) {
        if (speed > Settings.FRAMES_PER_SECOND)
            throw new RuntimeException("ERROR: Speed exceeds amount of frames per second!");

        this.speed = speed / Settings.FRAMES_PER_SECOND;
    } // Otrzymujemy wartość cząstkową, która określa o jaką część kratki przesuwamy się do przodu

    /**
     * Ustawia pozycje obiektu na wektor załączony w parametrze
     * @param position miejsce, w którym powinien znajdować się obiekt
     */
    void setPosition(Vector2d position) { //Zabezpieczenia, aby obiekty nie przekroczyly pola gry
        if (position.getX() < 0) {
            position.setX(0);
            changeMoveDirection(new Vector2d(-moveDirection.getX(), moveDirection.getY()));
        } else if (position.getX() > Settings.WIDTH - 1) {
            position.setX(Settings.WIDTH - 1);
            changeMoveDirection(new Vector2d(-moveDirection.getX(), moveDirection.getY()));
        }

        if (position.getY() < 0 || position.getY() > Settings.HEIGHT - 1)
            position.setY(0);

        this.position = position;
    }

    /**
     * Zwraca pozycję, w której znajduje się obiekt
     * @return pozycja, w której znajduje się obiekt
     */
    public Vector2d getPosition() {
        return position;
    }

    /**
     * Używane jeżeli obiekt wypadnie poza ekran
     */
    void belowBorder() { // Rozne dzialanie funkcji w zaleznosci od dziedziczacego obiektu
        destroy();
    }

    /**
     * Służy do uaktualniania pozycji wyliczonej na podstawie obecnej pozycji, prędkości i kierunku ruchu
     */
    private void move() {
        setPosition(position.add(moveDirection.multiply(speed)));
    }

    void onCollision(Entity objectHit) {
        objectHit.destroy();
        destroy();
        //Instantiate Explosion class
    }

    void destroy() {
        //Delete object
        //System.gc();
        position.setY(0);
    }

    public String[] getSpriteURL() {
        return spriteURL;
    }
}
