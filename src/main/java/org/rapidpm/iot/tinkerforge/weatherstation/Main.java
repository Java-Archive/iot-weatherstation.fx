package org.rapidpm.iot.tinkerforge.weatherstation;

import com.tinkerforge.*;
import eu.hansolo.enzo.clock.Clock;
import eu.hansolo.enzo.clock.ClockBuilder;
import eu.hansolo.enzo.common.Section;
import eu.hansolo.enzo.gauge.SimpleGauge;
import eu.hansolo.enzo.gauge.SimpleGaugeBuilder;
import eu.hansolo.enzo.sevensegment.SevenSegment;
import eu.hansolo.enzo.sevensegment.SevenSegmentBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.rapidpm.iot.tinkerforge.weatherstation.components.SevenSegmentLine;

import java.io.IOException;

/**
 * Created by Sven Ruppert on 16.08.2014.
 */
public class Main extends Application {

  private String temperatureUID = "";  // 216
  private String barometerUID = "";    // 221
  private String ambientLightUID = ""; // 21
  private String humidityUID = "";     // 27
  private static String masterIP = "";
  private int masterPort = 4223;

  public static void main(String[] args) {
    masterIP = args[0];
    launch(args);
  }


  private BrickletTemperature temp;
  private BrickletBarometer barometer;
  private BrickletAmbientLight ambientLight;
  private BrickletHumidity humidity;

  private final IPConnection ipcon = new IPConnection();

  final AnchorPane root = new AnchorPane();

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("TF RaspiWeather");
    primaryStage.setFullScreen(true);

    final AnchorPane weatherOverviewPane = new AnchorPane();
    weatherOverviewPane.setStyle("-fx-background-color: black");

    final HBox hBox = new HBox();
    weatherOverviewPane.getChildren().add(hBox);

    final VBox left = new VBox();
    final VBox right = new VBox();

    hBox.getChildren().add(left);
    hBox.getChildren().add(right);

    final Clock clock = createClock();
    right.getChildren().add(clock);

    final SimpleGauge thermoMeter = createThermoMeter();
    right.getChildren().add(thermoMeter);

    final SevenSegmentLine row01 = new SevenSegmentLine();
    row01.setDescription("Air Pressure [mbar]");
    row01.setNachkommastelle(3);
    left.getChildren().add(row01);

    final SevenSegmentLine row02 = new SevenSegmentLine();
    row02.setDescription("Illuminance [lx]");
    row02.setNachkommastelle(1);
    left.getChildren().add(row02);

    final SevenSegmentLine row03 = new SevenSegmentLine();
    row03.setDescription("relative Humidity [%RH]");
    row03.setNachkommastelle(1);
    left.getChildren().add(row03);

    initTinkerForgeElements(thermoMeter, row01, row02, row03);

    primaryStage.setScene(new Scene(weatherOverviewPane, 320, 240));
    primaryStage.show();
  }

  private void initTinkerForgeElements(SimpleGauge thermoMeter,
                                       SevenSegmentLine row01,
                                       SevenSegmentLine row02,
                                       SevenSegmentLine row03)
      throws IOException, AlreadyConnectedException, NotConnectedException, TimeoutException {
    //Connect TinkerForge Elements
    ipcon.setAutoReconnect(true);
    ipcon.connect(masterIP, masterPort);
    ipcon.addEnumerateListener((uid, s1, c, shorts, shorts1, deviceIdentifier, i1) -> {
      switch (deviceIdentifier) {
        case 216: temperatureUID = uid ; break;
        case 221: barometerUID = uid ; break;
        case 21: ambientLightUID = uid ; break;
        case 27: humidityUID = uid ; break;
      }
    });
    ipcon.enumerate();


    temp = new BrickletTemperature(temperatureUID, ipcon);
    temp.addTemperatureListener(i -> Platform.runLater(() -> thermoMeter.setValue((i / 100.0) + 20)));
    temp.setTemperatureCallbackPeriod(1000);  //TODO einstellbar

    //fuehrende 0 bei weniger 7 Stellen
    barometer = new BrickletBarometer(barometerUID, ipcon);
//    barometer.addAirPressureListener(i -> Platform.runLater(() -> setTheSegments(sevenSegmentsLine01, i)));
    barometer.addAirPressureListener(i -> Platform.runLater(() -> row01.setTheSegments(i)));
    barometer.setAirPressureCallbackPeriod(1000); //TODO einstellbar

    ambientLight = new BrickletAmbientLight(ambientLightUID, ipcon);
    ambientLight.addIlluminanceListener(i -> Platform.runLater(() -> row02.setTheSegments(i)));
    ambientLight.setIlluminanceCallbackPeriod(1000); //TODO einstellbar

    humidity = new BrickletHumidity(humidityUID, ipcon);
    humidity.addHumidityListener(i -> Platform.runLater(() -> row03.setTheSegments(i)));
    humidity.setHumidityCallbackPeriod(1000);  //TODO einstellbar
  }

  private Clock createClock() {
    final Clock clock = ClockBuilder.create()
        .prefSize(106, 120)
        .design(Clock.Design.DB)
        .text("TinkerTime")
        .autoNightMode(true)
        .build();
    clock.setRunning(true);
    return clock;
  }

  private SimpleGauge createThermoMeter() {
    return SimpleGaugeBuilder.create()
          .prefSize(106, 120)
          .sections(
              new Section(-20, -10, "0"),
              new Section(-10, 0, "1"),
              new Section(0, 10, "2"),
              new Section(10, 20, "3"),
              new Section(20, 30, "4"),
              new Section(30, 40, "5")
          )
          .minValue(-20)
          .maxValue(40)
          .decimals(2)
          .title("Temperature")
          .unit("C")
        .styleClass(SimpleGauge.STYLE_CLASS_BLUE_TO_RED_6)
          .build();
  }

}
