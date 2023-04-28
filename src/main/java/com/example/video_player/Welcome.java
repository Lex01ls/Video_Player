package com.example.video_player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Welcome implements Initializable {
    @FXML
    private Button btnGetStarted;


    @FXML
    void handleClicks(ActionEvent event) throws IOException {
        if(event.getSource()==btnGetStarted){
            toPlayer(event);
        }
    }

    private void toPlayer(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Welcome.class.getResource("Player.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        //adding stylesheet to the application
        String css = Objects.requireNonNull(this.getClass().getResource("/application.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.setTitle("Player");
        stage.show();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnGetStarted.setId("start");
    }
}
