package com.example.demo;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ServerGui extends Application {

    private Parent rootNode;
    private FXMLLoader fxmlLoader;

    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Label runningServerText;

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ServerGui.fxml"));
        rootNode = fxmlLoader.load();

        primaryStage.setTitle("Snubbull's Server");
        Scene scene = new Scene(rootNode, 640, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    public void onClickShutDownServer (Event event) {
        System.exit(0);
    }
}
