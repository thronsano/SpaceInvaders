package server;

import entity.Entity;
import entity.Player;
import graphics.Frame;
import graphics.Settings;
import javafx.scene.input.KeyCode;
import math.Vector2d;

import java.util.ArrayList;

public class Server implements Runnable {
    ArrayList<GameInstance> gameStatesToProcess = new ArrayList<>();
    String moduleID;

    /**
     * Zwraca ilość elementów oczekujących na przetworzenie
     * @return ilość elementów oczekujących na przetworzenie
     */
    public float getEstimatedTime() {
        return gameStatesToProcess.size();
    }

    /**
     * Tworzy instancję ramki oraz wypełnia ją tłem i obiektami znajdującymi się w grze
     * @param instance instancja gry
     * @return utworzona ramka
     */
    Frame generateFrame(GameInstance instance) {
        Frame frame = new Frame(instance.frameNo);

        for (int i = 0; i < Settings.WIDTH; i++)
            for (int j = 0; j < Settings.HEIGHT; j++)
                frame.getFrame()[i][j] = "file:drawable/tlo.png";

        for (Entity entity : instance.objects)
            frame.getFrame()[(int) entity.getPosition().getX()][(int) entity.getPosition().getY()] = entity.getSpriteURL()[frame.getFrameNumber() % entity.getSpriteURL().length];

        return frame;
    }

    /**
     * Uaktualnia stan gry, poprzez analizę wciśniętych klawiszy i wywoływanie metod update
     * @param instance instancja gry, którą aktualizujemy
     */
    void updateGameInstance(GameInstance instance) {
        for (Entity entity : instance.objects) {
            if (entity instanceof Player) {
                if (instance.event.getCode() == KeyCode.RIGHT)
                    entity.changeMoveDirection(Vector2d.right());
                else if (instance.event.getCode() == KeyCode.LEFT)
                    entity.changeMoveDirection(Vector2d.left());
                else if (instance.event.getCode() == KeyCode.SPACE) {
                    entity.changeMoveDirection(Vector2d.zero());
                    ((Player) entity).shootMissile(Settings.FRAMES_PER_SECOND / 2, Vector2d.upwards());
                } else if (instance.event == null)
                    entity.changeMoveDirection(Vector2d.zero());

                instance.event = null;
            }

            entity.update();
        }
    }

    /**
     * Funkcja tworząca klatkę wypełnioną obrazkami o numerze określonym w parametrze frameNo
     *
     * @param frameNo numer tworzonej klatki
     * @return nowa wypełniona klatka
     */
    Frame createTestFrame(int frameNo) {
        Frame frame = new Frame(frameNo);

        for (int i = 0; i < Settings.WIDTH; i++)
            for (int j = 0; j < Settings.HEIGHT; j++)
                frame.getFrame()[i][j] = "file:drawable/tlo.png";

        if (frameNo % 2 == 0)
            frame.getFrame()[frameNo % Settings.WIDTH][frameNo % Settings.HEIGHT] = "file:drawable/kosmita1.png";
        else
            frame.getFrame()[frameNo % Settings.WIDTH][frameNo % Settings.HEIGHT] = "file:drawable/kosmita2.png";

        return frame;
    }

    @Override
    public void run() {
        while (true) {
            //Jeżeli istnieją instancje do przetworzenia, to generuje na ich podstawie nowe ramki i wysyła je do klienta
            if (gameStatesToProcess.size() != 0) {
                GameInstance gameInstance = gameStatesToProcess.get(0);

                System.out.println(moduleID + " processed frame no. " + gameInstance.frameNo);
                gameInstance.outputConnection.setObject(generateFrame(gameInstance));
                gameInstance.frameNo++;

                gameStatesToProcess.remove(0);
            }

            try {
                Thread.sleep((long) (1000.0 / (float) Settings.FRAMES_PER_SECOND));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}