package de.my5t3ry.ledcontroller;

import static java.util.stream.Collectors.toList;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MatrixController {


  private static final int ledsCount = 96;
  private Ws281xLedStrip strip;


  private boolean patching = false;
  BlockingQueue<byte[]> frameBuffer = new LinkedBlockingDeque<>();


  @PostConstruct
  @Async
  public void init() throws InterruptedException {

    strip = new Ws281xLedStrip(
        ledsCount,       // leds
        18,          // Using pin 10 to do SPI, which should allow non-sudo access
        800000,  // freq hz
        10,            // dma
        255,      // brightness
        0,      // pwm channel
        false,        // invert
        LedStripType.WS2811_STRIP_GRB,    // Strip type
        false    // clear on exit
    );

    final FrameBufferDispatcher frameBufferDispatcher = new FrameBufferDispatcher(frameBuffer,
        strip);
    setControlEvent(new LedControlEvent("WHITE", 255));
    frameBufferDispatcher.start();
  }

  public void setControlEvent(final LedControlEvent event) {
    for (int i = 0; i < ledsCount; i++) {
      strip.setPixel(i, getStaticColorsForString(event.getColor()));
    }
    strip.setBrightness(event.getBrightness());
    strip.render();
  }


  public static Color getStaticColorsForString(final String color) {
    final List<Field> colors = Arrays.stream(Color.class.getDeclaredFields()).filter(f ->
        Modifier.isStatic(f.getModifiers())).collect(toList()).stream()
        .filter(curColor -> curColor.getName().equals(color)).collect(
            Collectors.toUnmodifiableList());
    if (colors.size() != 1) {
      throw new IllegalStateException(String
          .format("Unexpected matching colors found for ['%s']. Found ['%s'], expected 1", color,
              colors.size()));
    }
    try {
      return (Color) colors.get(0).get(null);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(String
          .format("Could not access field ['%s']. With msg ['%s']", colors.get(0).getName(),
              e.getMessage()));
    }
  }

  public void addFrame(final byte[] frame) throws InterruptedException {
    frameBuffer.put(frame);
  }


  public void togglePower() {
    log.info(String.format("Current brightness [%s]", strip.getBrightness()));
    if (strip.getBrightness() == 0) {
      strip.setBrightness(255);
    } else {
      strip.setBrightness(0);
    }
    strip.render();
  }
}
