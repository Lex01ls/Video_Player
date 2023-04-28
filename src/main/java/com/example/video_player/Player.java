package com.example.video_player;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements Initializable {

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnStop;

    @FXML
    private Button btnForward;

    @FXML
    private Button btnPlaylist;

    @FXML
    private Button btnRepeat;

    @FXML
    private Button btnShuffle;

    @FXML
    private Button btnVolume;
    @FXML
    private BorderPane Media_Show;
    @FXML
    private ImageView Play_Image;
    @FXML
    private ImageView Volume_Image;
    @FXML
    private ImageView Repeat_Image;
    @FXML
    private Label lblDuration;

    @FXML
    private Label lblElapsed;

    @FXML
    private Label lblVolume;
    @FXML
    private MediaView mediaView;

    @FXML
    private Slider volume_slider;

    @FXML
    private ProgressBar progress_bar;
    @FXML
    private MenuItem btnChoose;

    @FXML
    private Menu btnHelp;

    private MediaPlayer mediaPlayer;
    private File file;
    private Media media;
    private Timer timer;
    private TimerTask task;
    private boolean isPlaying = true;


    @FXML
    void handleClicks(ActionEvent event) {

        //when the play button is pressed
        if(event.getSource() == btnPlay){

            if(mediaPlayer.getStatus()== MediaPlayer.Status.PAUSED){
                beginTimer();
                mediaPlayer.play();
                Play_Image.setImage(new Image("pause_button_24px.png"));
            }
            else{
                Play_Image.setImage(new Image("circled_play_24px.png"));
                mediaPlayer.pause();
            }


           /* if(isPlaying){
                Play_Image.setImage(new Image("circled_play_24px.png"));
                mediaPlayer.pause();
                isPlaying = false;
            }
          /*  else{
                beginTimer();
                mediaPlayer.play();
                isPlaying = true;
                Play_Image.setImage(new Image("pause_button_24px.png"));
            }
*/
        }

        //when the seek back button is pressed
        if(event.getSource() == btnPrevious){
            double current_time = mediaPlayer.getCurrentTime().toMillis();
            double new_time = current_time - 10000;
            Duration duration = new Duration(new_time);
            mediaPlayer.seek(duration);
        }

        //event when a stop button is clicked
        if(event.getSource() == btnStop){
            if(mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
                mediaPlayer.seek(Duration.seconds(0.0));
                progress_bar.setProgress(0);
                cancelTimer();
                mediaPlayer.stop();
                isPlaying = false;
                Play_Image.setImage(new Image("circled_play_24px.png"));
            }
        }
        //when the fast-forward button is clicked
        if(event.getSource() == btnForward){
            double current_time = mediaPlayer.getCurrentTime().toMillis();
            double new_time = current_time + 10000;
            Duration duration = new Duration(new_time);
            mediaPlayer.seek(duration);
        }

        if(event.getSource() == btnRepeat){

            Repeat_Image.setImage(new Image("repeat_one_24px.png"));
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        }

        //event when the volume is pressed
        if(event.getSource() == btnVolume){

             boolean isMuted = mediaPlayer.isMute();

            mediaPlayer.setMute(!isMuted);

            if(isMuted){

                mediaPlayer.setVolume(100);
                lblVolume.setText(String.valueOf(100));
                volume_slider.setValue(100);
            }
            else{

                mediaPlayer.setVolume(0);
                lblVolume.setText(String.valueOf(0));
                volume_slider.setValue(0);
            }
        }
    }
    @FXML
    void MenuClicks(ActionEvent event) {

        if(event.getSource() ==btnChoose){

            FileChooser chooser = new FileChooser();
            var filter = new FileChooser.ExtensionFilter("Videos", "*.mp4","*.mkv");
            chooser.getExtensionFilters().add(filter);

            file = chooser.showOpenDialog(null);
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            //elapsed time implementation
            mediaPlayer.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
                Duration elapsed = mediaPlayer.getCurrentTime();
                String elapsedFormatted = formatDuration(elapsed);
                lblElapsed.setText(elapsedFormatted);

            }));
            //when the media has ended implementation
            mediaPlayer.setOnEndOfMedia(()->{
                Play_Image.setImage(new Image("circled_play_24px.png"));
            });

            //total duration of media implementation
            mediaPlayer.setOnReady(()->{
                Duration duration = mediaPlayer.getTotalDuration();
                String durationFormatted = formatDuration(duration);
                lblDuration.setText(durationFormatted);
            });

            beginTimer();
            mediaPlayer.setAutoPlay(true);
            Play_Image.setImage(new Image("pause_button_24px.png"));

        }
        if(event.getSource()==btnHelp){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Press file and then 'Choose to select a video you would like to play");
            alert.show();
        }

    }
    //Timer method to begin a timer as soon as the video elapses
    private void beginTimer(){

        timer = new Timer();
        task = new TimerTask(){

            @Override
            public void run() {
                isPlaying = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                progress_bar.setProgress(current / end);

                if(current / end == 1){
                    isPlaying = false;
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);

    }
    //This method stops the counting of time
    private void cancelTimer(){

        isPlaying = false;
        timer.cancel();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //volume slider implementation
      volume_slider.valueProperty().addListener(new ChangeListener<Number>() {
          @Override
          public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
              mediaPlayer.setVolume(volume_slider.getValue() * 0.01);

              var Volume =  volume_slider.getValue() * 0.01;

              //changing icon for different volume levels
              if (Volume == 0){
                  Volume_Image.setImage(new Image("no_audio_24px.png"));
              }
              else if (Volume < 0.25 ){
                  Volume_Image.setImage(new Image("low_volume_24px.png"));
              }

              else if (0.25 < Volume && Volume < 0.75 ){
                  Volume_Image.setImage(new Image("speaker_24px.png"));
              }
              else if (Volume > 0.75){
                  Volume_Image.setImage(new Image("audio_24px.png"));
              }

              lblVolume.setText(Integer.toString((int) Math.round(Volume * 100))+ "%");

          }

      });

    }
    //format for time method
    private String formatDuration(Duration duration){
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
