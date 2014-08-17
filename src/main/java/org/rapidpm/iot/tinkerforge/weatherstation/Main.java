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

    //entscheiden ob die Config angezeigt werden soll oder nicht



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

    final VBox row01 = new VBox();
    final VBox row02 = new VBox();
    final VBox row03 = new VBox();

    left.getChildren().add(row01);
    left.getChildren().add(row02);
    left.getChildren().add(row03);


    final HBox line01 = createLine();
    final HBox line02 = createLine();
    final HBox line03 = createLine();


    row01.getChildren().add(createLabel("Air Pressure [mbar]"));
    row01.getChildren().add(line01);

    row02.getChildren().add(createLabel("Illuminance [lx]"));
    row02.getChildren().add(line02);

    row03.getChildren().add(createLabel("relative Humidity [%RH]"));
    row03.getChildren().add(line03);

    final SevenSegment[] sevenSegmentsLine01 = getSevenSegments(3);
    for (final SevenSegment sevenSegment : sevenSegmentsLine01) {
      line01.getChildren().add(sevenSegment);
    }

    final SevenSegment[] sevenSegmentsLine02 = getSevenSegments(1);
    for (final SevenSegment sevenSegment : sevenSegmentsLine02) {
      line02.getChildren().add(sevenSegment);
    }

    final SevenSegment[] sevenSegmentsLine03 = getSevenSegments(1);
    for (final SevenSegment sevenSegment : sevenSegmentsLine03) {
      line03.getChildren().add(sevenSegment);
    }



    initTinkerForgeElements(thermoMeter, sevenSegmentsLine01, sevenSegmentsLine02, sevenSegmentsLine03);


    primaryStage.setScene(new Scene(weatherOverviewPane, 320, 240));
    primaryStage.show();
  }

  private void initTinkerForgeElements(SimpleGauge thermoMeter,
                                       SevenSegment[] sevenSegmentsLine01,
                                       SevenSegment[] sevenSegmentsLine02,
                                       SevenSegment[] sevenSegmentsLine03) throws IOException, AlreadyConnectedException, NotConnectedException, TimeoutException {
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
    barometer.addAirPressureListener(i -> Platform.runLater(() -> setTheSegments(sevenSegmentsLine01, i)));
    barometer.setAirPressureCallbackPeriod(1000); //TODO einstellbar

    ambientLight = new BrickletAmbientLight(ambientLightUID, ipcon);
    ambientLight.addIlluminanceListener(i -> Platform.runLater(() -> setTheSegments(sevenSegmentsLine02, i)));
    ambientLight.setIlluminanceCallbackPeriod(1000); //TODO einstellbar

    humidity = new BrickletHumidity(humidityUID, ipcon);
    humidity.addHumidityListener(i -> Platform.runLater(() -> setTheSegments(sevenSegmentsLine03, i)));
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
  //        .value(20)
          .styleClass(SimpleGauge.STYLE_CLASS_BLUE_TO_RED_6)
          .build();
  }

  private void setTheSegments(SevenSegment[] sevenSegmentsLine, int i) {
    int[] charArray = getCharArrayFromRawValue(i);

    int counter = 0;
    boolean pre = true;
    boolean red = true;
    for (final int aChar : charArray) {
      final SevenSegment sevenSegment = sevenSegmentsLine[counter];
      if (aChar == 0 && pre) {
        sevenSegment.setSegmentStyle(SevenSegment.SegmentStyle.BLACK);
      } else if (aChar != 0 && pre) {
        pre = false;
        sevenSegment.setSegmentStyle(SevenSegment.SegmentStyle.RED);
      } else if (sevenSegment.isDotOn()) {
        sevenSegment.setSegmentStyle(SevenSegment.SegmentStyle.RED);
        red = false;
      } else if (!red) {
        sevenSegment.setSegmentStyle(SevenSegment.SegmentStyle.BLUE);
      }
      sevenSegment.setCharacter(aChar);
      counter++;
    }
  }

  private SevenSegment[] getSevenSegments(int nachkommastellen) {
    final SevenSegment l01E01 = createSevenSegmentElement(0);
    final SevenSegment l01E02 = createSevenSegmentElement(0);
    final SevenSegment l01E03 = createSevenSegmentElement(0);
    final SevenSegment l01E04 = createSevenSegmentElement(0);
    final SevenSegment l01E05 = createSevenSegmentElement(0);
    final SevenSegment l01E06 = createSevenSegmentElement(0);
    final SevenSegment l01E07 = createSevenSegmentElement(0);

    final SevenSegment[] sevenSegments = new SevenSegment[7];
    sevenSegments[0] = l01E01;
    sevenSegments[1] = l01E02;
    sevenSegments[2] = l01E03;
    sevenSegments[3] = l01E04;
    sevenSegments[4] = l01E05;
    sevenSegments[5] = l01E06;
    sevenSegments[6] = l01E07;

    for (int i = sevenSegments.length - 1; i >= 0; i--) {
      SevenSegment sevenSegment = sevenSegments[i];
      if (nachkommastellen == 0) {
        sevenSegment.setDotOn(true);
      }

      if (nachkommastellen > 0) {
        sevenSegment.setSegmentStyle(SevenSegment.SegmentStyle.BLUE);
      }
      nachkommastellen--;
    }
    return sevenSegments;
  }


  private synchronized int[] getCharArrayFromRawValue(int rawValueInt) {
    final String rawValue = String.format("%07d", rawValueInt);
    int[] ia = new int[7];
    int j = 0;
    for (char c : rawValue.toCharArray()) {
      ia[j++] = Character.getNumericValue(c);
    }
    return ia;
  }

  private SevenSegment createSevenSegmentElement(final int value) {
    return SevenSegmentBuilder.create().prefSize(26, 50)
        .character(value)
        .segmentStyle(SevenSegment.SegmentStyle.RED)
        .dotOn(false)
        .build();
  }

  private final Insets padding = new Insets(5, 5, 5, 5);

  private HBox createLine() {
    final HBox hBox = new HBox();
    hBox.setPadding(padding);
    return hBox;
  }

  private Label createLabel(final String text) {
    final Label label = new Label(text);
    label.setTextFill(Color.STEELBLUE);
    label.setStyle("-fx-font-size: 15px");
    return label;
  }
}
