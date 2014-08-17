package org.rapidpm.iot.tinkerforge.weatherstation.components;

import eu.hansolo.enzo.sevensegment.SevenSegment;
import eu.hansolo.enzo.sevensegment.SevenSegmentBuilder;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by Sven Ruppert on 17.08.2014.
 */
public class SevenSegmentLine extends VBox {

  private static final Insets padding = new Insets(5, 5, 5, 5);
  private final Label label = new Label();
  private final HBox line = new HBox();
  private final SevenSegment[] sevenSegments = new SevenSegment[7];


  public SevenSegmentLine() {
    init();
  }

  private void init() {
    label.setId("label");
    getChildren().add(label);
    line.setId("innerLine");
    getChildren().addAll(line);
    line.setPadding(padding);
    initSevenSegments();
    for (final SevenSegment sevenSegment : sevenSegments) {
      line.getChildren().add(sevenSegment);
    }
  }

  public SevenSegmentLine(double spacing) {
    super(spacing);
    init();

  }

  public SevenSegmentLine(Node... children) {
    super(children);
    init();
  }

  public SevenSegmentLine(double spacing, Node... children) {
    super(spacing, children);
    init();
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


  public void setTheSegments(int rawValue) {
    int[] charArray = getCharArrayFromRawValue(rawValue);

    int counter = 0;
    boolean pre = true;
    boolean red = true;
    for (final int aChar : charArray) {
      final SevenSegment sevenSegment = sevenSegments[counter];
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

  private void initSevenSegments() {
    sevenSegments[0] = createSevenSegmentElement(0);
    sevenSegments[1] = createSevenSegmentElement(0);
    sevenSegments[2] = createSevenSegmentElement(0);
    sevenSegments[3] = createSevenSegmentElement(0);
    sevenSegments[4] = createSevenSegmentElement(0);
    sevenSegments[5] = createSevenSegmentElement(0);
    sevenSegments[6] = createSevenSegmentElement(0);

    for (int i = 0; i < sevenSegments.length; i++) {
      SevenSegment sevenSegment = sevenSegments[i];
      sevenSegment.setId("segment_"+i);
    }

  }

  private SevenSegment createSevenSegmentElement(final int value) {
    return SevenSegmentBuilder.create().prefSize(26, 50)
        .character(value)
        .segmentStyle(SevenSegment.SegmentStyle.RED)
        .dotOn(false)
        .build();
  }


  /**
   * setting the description Label
   * for example:  Illuminance [lx]
   * @param text
   */
  public void setDescription(final String text) {
      label.setText(text);
      label.setTextFill(Color.STEELBLUE);
      label.setStyle("-fx-font-size: 15px");
  }

  /**
   * setting the colors for the pre and post comma segments
   * @param nachkommastellen
   */
  public void setNachkommastelle(int nachkommastellen){
    for (int i = sevenSegments.length - 1; i >= 0; i--) {
      SevenSegment sevenSegment = sevenSegments[i];
      if (nachkommastellen == 0) {
        sevenSegment.setDotOn(true);
      }

      if (nachkommastellen > 0) {
        sevenSegment.setSegmentStyle(SevenSegment.SegmentStyle.BLUE);
      } else {
        sevenSegment.setSegmentStyle(SevenSegment.SegmentStyle.RED);
      }
      nachkommastellen--;
    }
  }

//  public SevenSegment[] getSevenSegments() {
//    return sevenSegments;
//  }
}
