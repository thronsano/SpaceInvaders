package networking;

import graphics.Settings;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Szaman on 14.12.2017.
 */
public class Receiver implements Runnable {
    private ObjectInputStream inputStream;
    private ServerSocket serverSocket;
    private Socket socket;
    private String serverIP;
    private String receiverID;
    private Queue<Object> receivedObjects;
    private boolean isServer = false;

    public Receiver(String serverIP, String moduleID) {
        this.serverIP = serverIP;
        this.receiverID = moduleID;
    }

    /**
     * Przypisuje kolejkę dla obiektów przychodzących
      * @param queue kolejka dla obiektów przychodzących
     */
    public void setNewQueue(PriorityQueue<Object> queue) {
        receivedObjects = queue;
    }


    @Override
    public void run() {
        if (receivedObjects == null)
            receivedObjects = new PriorityQueue<>();
        while (true) {
            try {
                waitForConnection();
                setupStreams();
                receiveData();
            } catch (EOFException eofException) {
                System.out.println(receiverID + ": Server terminated the connection!");
            } catch (IOException ioE) {
                //System.out.println(receiverID + ": IOException!");
                ioE.printStackTrace();
            } finally {
                closeStream();
            }

        }
    }

    /**
     * Odbiera dane od połączonego użytkownika
     * @throws IOException
     */
    private void receiveData() throws IOException {
        while (true) {
            try {
                receivedObjects.add(inputStream.readObject());
                System.out.println(System.currentTimeMillis() + " " + receiverID + " received a package!");
            } catch (ClassNotFoundException classNotFoundExc) {
                System.out.println(receiverID + ": Received packet is of incorrect type!");
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Oczekuje na połączenie od klienta
     *
     * @throws IOException
     */
    private void waitForConnection() throws IOException {
        System.out.println(receiverID + ": Establishing connection...");
        if (isServer)
            socket = serverSocket.accept();
        else
            socket = new Socket(InetAddress.getByName(serverIP), Settings.PORT_NUMBER);
        System.out.println(receiverID + ": Now connected to " + socket.getInetAddress().getHostName());
    }

    /**
     * Otwiera strumień przychodzący do klienta
     *
     * @throws IOException
     */
    private void setupStreams() throws IOException {
        System.out.println(receiverID + ": Setting up an input stream...");
        inputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println(receiverID + ": Stream is up!");
    }

    /**
     * Zamyka strumień
     */
    private void closeStream() {
        System.out.println(receiverID + ": Closing connections...");

        try {
            inputStream.close();
            socket.close();
        } catch (IOException ioExc) {
            System.out.println(receiverID + ": Failed to close the stream.");
        }
    }
}
