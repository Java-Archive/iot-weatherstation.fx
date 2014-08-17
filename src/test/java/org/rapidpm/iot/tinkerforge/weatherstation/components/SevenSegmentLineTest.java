package org.rapidpm.iot.tinkerforge.weatherstation.components;

import eu.hansolo.enzo.sevensegment.SevenSegment;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import org.loadui.testfx.utils.FXTestUtils;

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
  @Test @Ignore
  public void testSetTheSegments01() throws Exception {
    final SevenSegmentLine line = find("#line");
    Assert.assertNotNull(line);
    FXTestUtils.invokeAndWait(() -> {
      line.setNachkommastelle(2);
      line.setTheSegments(100); //Ergebnis 1.00
    }, 20);

    final SevenSegment seg_0 = find("#segment_0"); //0
    Assert.assertNotNull(seg_0);
    Assert.assertFalse(seg_0.isDotOn());

    final SevenSegment seg_1 = find("#segment_1"); //0
    Assert.assertNotNull(seg_1);
    Assert.assertFalse(seg_1.isDotOn());
    final SevenSegment.SegmentStyle segmentStyle = seg_1.getSegmentStyle();
    Assert.assertTrue(segmentStyle.equals(SevenSegment.SegmentStyle.BLUE));

    final SevenSegment seg_2 = find("#segment_2"); //1.
    Assert.assertNotNull(seg_2);
    Assert.assertFalse(seg_2.isDotOn());

  }

  /**
   * Method: setDescription(final String text)
   */
  @Test
  public void testSetDescription() throws Exception {
    final SevenSegmentLine line = find("#line");
    FXTestUtils.invokeAndWait(() -> line.setDescription("DemoDescription"), 20);
    final Label label = find("#label");
    Assert.assertNotNull(label);
    Assert.assertEquals(label.getText(), "DemoDescription");

    verifyThat("#label", hasText("DemoDescription"));
  }

  /**
   * Method: setNachkommastelle(int nachkommastellen)
   */
  @Test
  public void testSetNachkommastelle01() throws Exception {
    final SevenSegmentLine line = find("#line");
    FXTestUtils.invokeAndWait(() -> line.setNachkommastelle(3), 20);
    final SevenSegment node = find("#segment_3");
    Assert.assertNotNull(node);
    Assert.assertTrue(node.isDotOn());
  }

  @Test
  public void testSetNachkommastelle02() throws Exception {
    final SevenSegmentLine line = find("#line");
    FXTestUtils.invokeAndWait(() -> line.setNachkommastelle(3), 20);
    for(int i=0; i<7;i++){
      final SevenSegment node = find("#segment_"+i);
      Assert.assertNotNull(node);
      if(i == 3){
        Assert.assertTrue(node.isDotOn());
      } else{
        Assert.assertFalse(node.isDotOn());
      }
    }
  }


  @Override
  protected Parent getRootNode() {
    final SevenSegmentLine sevenSegmentLine = new SevenSegmentLine();
    sevenSegmentLine.setId("line");
    return sevenSegmentLine;
  }
}
