package org.rapidpm.iot.tinkerforge.weatherstation.components;/**
 * Created by Sven Ruppert on 02.09.2014.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SevenSegmentdemo extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    final AnchorPane weatherOverviewPane = new AnchorPane();
    weatherOverviewPane.setStyle("-fx-background-color: black");

    final SevenSegmentLine row01 = new SevenSegmentLine();
    row01.setDescription("Air Pressure [mbar]");
    row01.setNachkommastelle(3);
    row01.setTheSegments(12345);
    weatherOverviewPane.getChildren().add(row01);
    primaryStage.setScene(new Scene(weatherOverviewPane, 320, 240));
    primaryStage.show();
  }
}
