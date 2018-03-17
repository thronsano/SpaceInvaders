package server;

import entity.Entity;
import javafx.scene.input.KeyEvent;
import networking.Sender;

import java.util.ArrayList;

/**
 * Created by Szaman on 26.10.2017.
 */
public class GameInstance {
    Sender outputConnection;
    ArrayList<Entity> objects;
    KeyEvent event = null;
    int frameNo = 0;
    int userID;


    public GameInstance(ArrayList<Entity> objects, int frameNo, int userID, Sender outputConnection) {
        this.outputConnection = outputConnection;
        this.objects = objects;
        this.frameNo = frameNo;
        this.userID = userID;
    }
}
