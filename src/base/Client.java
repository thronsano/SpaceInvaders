package base;

import entity.Player;
import graphics.Frame;
import graphics.Settings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import math.Vector2d;
import networking.Receiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PriorityQueue;

public class Client extends Application {
    private GridPane grid; //siatka gry składająca się z obrazków
    private PriorityQueue<Object> buffer = new PriorityQueue<>(); //kolejka przechowująca otrzymane klatki do wyświetlenia
    private int frameNumber = 0; //numer klatki przeznaczonej do wyświetlenia
    private Player player;
    private int clientID;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        window.setTitle("Let the battle begin");

        player = new Player(new Vector2d((int) (Settings.WIDTH / 2), 0));

        grid = new GridPane();
        Scene scene = new Scene(grid, Settings.MULTIPLIER * Settings.WIDTH, Settings.MULTIPLIER * Settings.HEIGHT);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> handleUserInput(key));
        window.setScene(scene);
        window.show();

        Receiver receiver = new Receiver("127.0.0.1", "ClientReceiver");
        receiver.setNewQueue(buffer);
        Thread t1 = new Thread(receiver);
        t1.start();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(250), ae -> waitForFrame())); //pętla uruchamiająca się co 250ms wywołująca funkcję waitForFrame
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Wyswietla, ktory klawisz wcisnal gracz
     *
     * @param key klawisz, ktory zostal wcisniety
     */
    private void handleUserInput(KeyEvent key) {
        System.out.println("You pressed " + key.getCode().toString());
    }

    /**
     * Funkcja weryfikująca numer klatki i przekazująca odpowiednią klatkę do wyświetlenia
     */
    private void waitForFrame() {
        //System.out.println(buffer.size());
        if (buffer.peek() != null && ((Frame) buffer.peek()).getFrameNumber() == frameNumber) {
            System.out.println(frameNumber);
            render((Frame) buffer.poll());
            frameNumber = (++frameNumber) % Settings.MAX_FRAME_AMOUNT;
        }


    }

    /**
     * Funkcja wypełniająca siatkę grid obrazkami zawartymi w parametrze frame
     *
     * @param frame klatka przeznaczona do wyświetlenia
     */
    private void render(Frame frame) {
        grid.getChildren().clear();
        for (int i = 0; i < Settings.WIDTH; i++) {
            for (int j = 0; j < Settings.HEIGHT; j++) {
                ImageView imageView = new ImageView();
                imageView.setImage(new Image(frame.getFrame()[i][j]));
                GridPane.setConstraints(imageView, i, j);
                grid.getChildren().add(imageView);
            }
        }
    }

    public static void main(String[] args) {
        boolean simulation = false;

        if (!simulation)
            launch(args);
        else {
            for (int i = 0; i < 100; i++) {
                new Thread(new ClientSimulate()).start();
            }

            while (true) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ClientSimulate implements Runnable {
    private PriorityQueue<Object> buffer = new PriorityQueue<>(); //kolejka przechowująca otrzymane klatki do wyświetlenia
    private int frameNumber = 0; //numer klatki przeznaczonej do wyświetlenia
    private Player player;

    public void simulate() {
        player = new Player(new Vector2d((int) (Settings.WIDTH / 2), 0));

        Receiver receiver = new Receiver("127.0.0.1", "Receiver:");
        receiver.setNewQueue(buffer);
        Thread t1 = new Thread(receiver);
        t1.start();

        while (true) {
            if (buffer.peek() != null && ((Frame) buffer.peek()).getFrameNumber() == frameNumber) {
                System.out.println("Frame No.:" + frameNumber);
                frameNumber = (++frameNumber) % Settings.MAX_FRAME_AMOUNT;
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        simulate();
    }
}