package org.rapidpm.iot.common;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;

import java.time.LocalDateTime;
import java.util.concurrent.*;

import static org.loadui.testfx.controls.Commons.hasText;

/**
 * Created by Sven Ruppert on 22.08.2014.
 */
@Category(TestFX.class)
public class SimpleWaitTest extends GuiTest {

  public static final int ThreeSeconds = 3;
  private final ScheduledThreadPoolExecutor ste
      = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);


  private final Runnable oneShotTask = () -> {
    System.out.println("\t oneShotTask Execution Time: "+ LocalDateTime.now());
    final Button button = find("#btn");
    Platform.runLater(() -> button.setText("was clicked"));
  };

  @Override
  protected Parent getRootNode() {
    final Button btn = new Button();
    btn.setId("btn");
    btn.setText("Hello World");
    btn.setOnAction((actionEvent) -> {
      System.out.println("Submission Time: " + LocalDateTime.now());
      ScheduledFuture<?> oneShotFuture = ste.schedule(oneShotTask, ThreeSeconds, TimeUnit.SECONDS);
    });
    return btn;
  }


  @Test(expected = RuntimeException.class)
  public void shouldClickButton01() {
    final Button button = find("#btn");
    clickOn(button);
    waitUntil(button, hasText("was clicked"), 1);
  }

  @Test
  public void shouldClickButton02() {
    final Button button = find("#btn");
    clickOn(button);
    waitUntil(button, hasText("was clicked"), 10);
  }
}
