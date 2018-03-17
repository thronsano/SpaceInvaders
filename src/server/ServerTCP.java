package server;

import graphics.Settings;
import networking.Sender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Szaman on 20.01.2018.
 */
public class ServerTCP implements Runnable {
    ArrayList<Sender> playerOutputConnections = new ArrayList<>();
    int maxClientID = 0;

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(Settings.PORT_NUMBER, 100);

            while (true) {
                try {
                    //akceptuje nowe połączenia, po czym dodaje połączenie do listy połączeń
                    Socket socket = serverSocket.accept();
                    playerOutputConnections.add(new Sender(socket, serverSocket.getInetAddress().getHostName() + " was ", maxClientID++));

                    new Thread(playerOutputConnections.get(playerOutputConnections.size() - 1)).start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
