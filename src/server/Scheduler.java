package server;

import entity.Enemy;
import entity.Entity;
import graphics.Settings;
import math.Vector2d;
import networking.Sender;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Szaman on 26.10.2017.
 */
public class Scheduler implements Runnable {
    private ArrayList<GameInstance> instances = new ArrayList<>();
    private int clientsAmount = 0;

    private ServerTCP serverTCP;
    private Server server1 = new Server();
    private Server server2 = new Server();

    /**
     * Tworzy nową instancję gry, oraz wypełnia ją przeciwnikami
     *
     * @param outputConnection połączenie do gracza, który wywołał instancję
     * @return utworzona instancja gry
     */
    public GameInstance createGame(Sender outputConnection) {
        ArrayList<Entity> entities = new ArrayList<>();
        Random rand = new Random();
        int enemiesAmount = rand.nextInt(5) + 3;

        for (int i = 0; i < enemiesAmount; i++) {
            Enemy enemy = new Enemy(Settings.FRAMES_PER_SECOND / 4, new Vector2d(rand.nextInt(Settings.WIDTH - 1), rand.nextInt(3)));
            enemy.changeMoveDirection(new Vector2d(rand.nextFloat() > 0.5f ? 1 : -1, rand.nextFloat()));
            entities.add(enemy);
        }

        return new GameInstance(entities, 0, serverTCP.maxClientID - 1, outputConnection);
    }

    /**
     * Wyszukuje instancję gry, która posiada takie samo id użytkownika jak połączenie załączone w parametrze
     *
     * @param sender połączenie do gracza
     * @return odnaleziona instancja gry
     */
    private GameInstance findGameInstance(Sender sender) {
        for (GameInstance instance : instances)
            if (sender.userID == instance.userID)
                return instance;

        return null;
    }

    /**
     * Usuwa instancje gry graczy, którzy się rozłączyli
     */
    private void removeInstance() {
        boolean found;

        for (GameInstance instance : instances) {
            found = false;

            for (Sender outputConnection : serverTCP.playerOutputConnections) {
                if (instance.userID == outputConnection.userID) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                instances.remove(instance);
                return;
            }
        }
    }

    /**
     * Wybiera mniej obciążony serwer
     *
     * @return mniej obciążony serwer
     */
    private Server unoccupiedServer() {
        return server1.getEstimatedTime() < server2.getEstimatedTime() ? server1 : server2;
    }

    @Override
    public void run() {
        serverTCP = new ServerTCP();
        new Thread(serverTCP).start();

        new Thread(server1).start();
        new Thread(server2).start();

        server1.moduleID = "Server 1";
        server2.moduleID = "Server 2";

        while (true) {
            //sprawdza czy ktos dolaczyl albo sie rozlaczyl
            if (serverTCP.playerOutputConnections.size() != clientsAmount) {
                if (serverTCP.playerOutputConnections.size() > clientsAmount)
                    instances.add(createGame(serverTCP.playerOutputConnections.get(serverTCP.playerOutputConnections.size() - 1)));
                else
                    removeInstance();

                clientsAmount = serverTCP.playerOutputConnections.size();
            }

            //wysylanie frame'ow do klienta
            for (Sender outputConnection : serverTCP.playerOutputConnections) {
                GameInstance instance = findGameInstance(outputConnection);

                if (instance != null) {
                    unoccupiedServer().updateGameInstance(instance); // aktualizuje stan instancji gry podanej jako parametr
                    unoccupiedServer().gameStatesToProcess.add(instance); // dodaje instancje gry do kolejki instacji oczekujących na generację klatki
                }
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
