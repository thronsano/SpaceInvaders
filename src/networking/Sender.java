package networking;

import graphics.Frame;
import graphics.Settings;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Szaman on 14.12.2017.
 */
public class Sender implements Runnable {
    private ObjectOutputStream outputStream;
    private Socket socket;

    private String serverIP;
    private String senderID;
    public int userID;

    private ArrayList<Object> objectsToSend = new ArrayList<>();
    private boolean isServer = false;

    public Sender(Socket socket, String moduleID, int userID) {
        isServer = true;
        this.socket = socket;
        this.senderID = moduleID;
        this.userID = userID;
    }

    /**
     * Dodaje obiekt kolejki wysyłanych elementów
     *
     * @param object obiekt, który ma być wysłany
     */
    public void setObject(Object object) { objectsToSend.add(object); }

    @Override
    public void run() {
        while (true) {
            try {
                waitForConnection();
                setupStreams();
                sendData();
            } catch (EOFException eofException) {
                System.out.println(senderID + ": Server terminated the connection!");
            } catch (IOException ioE) {
                System.out.println(senderID + ": IOException!");
            } finally {
                closeStreams();
                break;
            }
        }
    }

    /**
     * Jeżeli w kolejce do wysyłania znajdują się jakieś obiekty, wysyła je do połączonego klienta.
     *
     * @throws IOException
     */
    private void sendData() throws IOException {
        while (true) {
            if (objectsToSend.size() != 0) {
                outputStream.flush();
                outputStream.writeObject(objectsToSend.get(0));
                System.out.println(senderID + "sent a package! Frame no. " + ((Frame) objectsToSend.get(0)).getFrameNumber());
                objectsToSend.remove(0);
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
        if (!isServer) {
            System.out.println(senderID + ": Establishing connection...");
            socket = new Socket(InetAddress.getByName(serverIP), Settings.PORT_NUMBER);
            System.out.println(senderID + ": Now connected to " + socket.getInetAddress().getHostName());
        }
    }

    /**
     * Otwiera strumień wychodzący do klienta
     *
     * @throws IOException
     */
    private void setupStreams() throws IOException {
        System.out.println(senderID + ": Setting up an output stream...");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        System.out.println(senderID + ": Stream is up!");
    }

    /**
     * Zamyka strumień połączony z klientem
     */
    private void closeStreams() {
        System.out.println(senderID + ": Closing connections...");

        try {
            outputStream.close();
            socket.close();
        } catch (IOException ioExc) {
            System.out.println(senderID + ": Failed to close the stream.");
        }
    }
}
