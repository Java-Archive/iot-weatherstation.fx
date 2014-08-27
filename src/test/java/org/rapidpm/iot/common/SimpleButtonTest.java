package org.rapidpm.iot.common;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;

import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;
/**
 * Created by Sven Ruppert on 17.08.2014.
 */
@Category(TestFX.class)
public class SimpleButtonTest extends GuiTest {

  @Override
  protected Parent getRootNode() {
    final Button btn = new Button();
    btn.setId("btn");
    btn.setText("Hello World");
    btn.setOnAction((actionEvent)-> btn.setText( "was clicked" ));
    return btn;
  }

  @Test
  public void shouldClickButton()
  {
    final Button button = find( "#btn" );
    clickOn(button);
    verifyThat("#btn", hasText("was clicked"));
  }

}