package org.rapidpm.iot.tinkerforge.weatherstation.components;

import eu.hansolo.enzo.sevensegment.SevenSegment;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import org.loadui.testfx.utils.FXTestUtils;

import static eu.hansolo.enzo.sevensegment.SevenSegment.SegmentStyle.BLACK;
import static eu.hansolo.enzo.sevensegment.SevenSegment.SegmentStyle.BLUE;
import static eu.hansolo.enzo.sevensegment.SevenSegment.SegmentStyle.RED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;

/**
 * SevenSegmentLine Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Aug 17, 2014</pre>
 */
@Category(TestFX.class)
public class SevenSegmentLineTest extends GuiTest {

  /**
   * Method: setTheSegments(int rawValue)
   */
  @Test
  public void testSetTheSegments01() throws Exception {
    final SevenSegmentLine line = find("#line");
    assertNotNull(line);
    FXTestUtils.invokeAndWait(() -> {
      line.setNachkommastelle(2);
      line.setTheSegments(100); //Ergebnis 1.00
    }, 20);


    final SevenSegment seg_0 = find("#segment_0"); // -
    assertNotNull(seg_0);
    assertFalse(seg_0.isDotOn());
    assertTrue(seg_0.getSegmentStyle().equals(BLACK));

    final SevenSegment seg_1 = find("#segment_1"); // -
    assertNotNull(seg_1);
    assertFalse(seg_1.isDotOn());
    assertTrue(seg_1.getSegmentStyle().equals(BLACK));

    final SevenSegment seg_2 = find("#segment_2"); // -
    assertNotNull(seg_2);
    assertFalse(seg_2.isDotOn());
    assertTrue(seg_2.getSegmentStyle().equals(BLACK));

    final SevenSegment seg_3 = find("#segment_3"); // -
    assertNotNull(seg_3);
    assertFalse(seg_3.isDotOn());
    assertTrue(seg_3.getSegmentStyle().equals(BLACK));


    final SevenSegment seg_6 = find("#segment_6"); // 0
    assertNotNull(seg_6);
    assertFalse(seg_6.isDotOn());
    assertTrue(seg_6.getSegmentStyle().equals(BLUE));

    final SevenSegment seg_5 = find("#segment_5"); // 0
    assertNotNull(seg_5);
    assertFalse(seg_5.isDotOn());
    assertTrue(seg_5.getSegmentStyle().equals(BLUE));

    final SevenSegment seg_4 = find("#segment_4"); //1.
    assertNotNull(seg_4);
    assertTrue(seg_4.isDotOn());
    assertTrue(seg_4.getSegmentStyle().equals(RED));

    final Adapter adapter = new Adapter();
    adapter.find("#segment_0").isDotOn(false).isStyle(BLACK);
    adapter.find("#segment_1").isDotOn(false).isStyle(BLACK);
    adapter.find("#segment_2").isDotOn(false).isStyle(BLACK);
    adapter.find("#segment_3").isDotOn(false).isStyle(BLACK);
    adapter.find("#segment_4").isDotOn(true).isStyle(RED);
    adapter.find("#segment_5").isDotOn(false).isStyle(BLUE);
    adapter.find("#segment_6").isDotOn(false).isStyle(BLUE);

    captureScreenshot();
  }

  public static class Adapter {

    private SevenSegment segment;

    public Adapter find(final String query){
      final SevenSegment seg = GuiTest.find(query);
      assertNotNull(seg);
      segment = seg;
      return this;
    }

    public Adapter isDotOn(final boolean b){
      if(b){
        assertTrue(segment.isDotOn());
      } else{
        assertFalse(segment.isDotOn());
      }
       return this;
    }

    public Adapter isStyle(SevenSegment.SegmentStyle style){
      assertTrue(segment.getSegmentStyle().equals(style));
      return this;
    }

  }

  /**
   * Method: setDescription(final String text)
   */
  @Test
  public void testSetDescription() throws Exception {
    final SevenSegmentLine line = find("#line");
    FXTestUtils.invokeAndWait(() -> line.setDescription("DemoDescription"), 20);
    final Label label = find("#label");
    assertNotNull(label);
    Assert.assertEquals(label.getText(), "DemoDescription");

    verifyThat("#label", hasText("DemoDescription"));
//    captureScreenshot();
  }

  /**
   * Method: setNachkommastelle(int nachkommastellen)
   */
  @Test
  public void testSetNachkommastelle01() throws Exception {
    final SevenSegmentLine line = find("#line");
    FXTestUtils.invokeAndWait(() -> line.setNachkommastelle(3), 20);
    final SevenSegment node = find("#segment_3");
    assertNotNull(node);
    assertTrue(node.isDotOn());
//    captureScreenshot();
  }

  @Test
  public void testSetNachkommastelle02() throws Exception {
    final SevenSegmentLine line = find("#line");
    FXTestUtils.invokeAndWait(() -> line.setNachkommastelle(3), 20);
    for(int i=0; i<7;i++){
      final SevenSegment node = find("#segment_"+i);
      assertNotNull(node);
      if(i == 3){
        assertTrue(node.isDotOn());
      } else{
        assertFalse(node.isDotOn());
      }
    }
//    captureScreenshot();
  }


  @Override
  protected Parent getRootNode() {
    final SevenSegmentLine sevenSegmentLine = new SevenSegmentLine();
    sevenSegmentLine.setId("line");
    return sevenSegmentLine;
  }
}
