package server;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        new Thread(new Scheduler()).start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
