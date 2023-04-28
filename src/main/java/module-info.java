module com.example.video_player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.video_player to javafx.fxml;
    exports com.example.video_player;
}