package com.example.video_player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class VideoPlayerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayerApplication.class.getResource("Welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //adding stylesheet to the application
        String css = Objects.requireNonNull(this.getClass().getResource("/application.css")).toExternalForm();
        scene.getStylesheets().add(css);
        Image icon = new Image("film_reel_100px.png");
        stage.getIcons().add(icon);
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}