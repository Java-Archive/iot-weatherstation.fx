package org.rapidpm.iot.common;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import static javafx.scene.input.KeyCode.TAB;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.loadui.testfx.controls.TextInputControls.clearTextIn;

/**
 * Created by Sven Ruppert on 25.08.2014.
 */
public class SimpleTExtFieldTest extends GuiTest {

  public static final String TEXT_FIELD = ".text-field";
  public static final String HELLO_IO_T_WORLD = "Hello IoT World";

  @Override
  protected Parent getRootNode() {
    return  new VBox(new TextField());
  }

  @Test
  public void shouldClearText() {
    clickOn(TEXT_FIELD).type(HELLO_IO_T_WORLD);
    verifyThat(TEXT_FIELD, hasText(HELLO_IO_T_WORLD));

    push(TAB); // To change focus from the TextField.

    clearTextIn(TEXT_FIELD);
    verifyThat(TEXT_FIELD, hasText(""));
  }
}
