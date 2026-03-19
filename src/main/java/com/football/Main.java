package com.football;

import javafx.application.Application;
import com.football.ui.AppUI;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new AppUI(primaryStage).showLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
