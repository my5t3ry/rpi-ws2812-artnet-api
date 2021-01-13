package de.my5t3ry.ledcontroller;

import static java.util.stream.Collectors.toList;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FrameBufferController {


  private static final int ledsCount = 96;
  private Ws281xLedStrip strip;
  private List<byte[]> frameBuffer;

  private boolean patching = false;

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
    setControlEvent(new LedControlEvent("WHITE", 255));
    patchArtNetData();
  }


  public void setControlEvent(final LedControlEvent event) {
    frameBuffer = new ArrayList<>();
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

  public void addFrame(final byte[] frame) {
    frameBuffer.add(frame);
    if (patching == false) {
      patchArtNetData();
    }
  }


  public void patchArtNetData() {
    while (!frameBuffer.isEmpty()) {
      this.patching = true;
      final byte[] curBuffer = frameBuffer.get(0);
      frameBuffer.remove(0);
      for (int i = 0; i < ledsCount; i++) {
        final Color color = new Color(curBuffer[(i * 3) + 138] & 0xFF,
            curBuffer[(i * 3) + 139] & 0xFF,
            curBuffer[(i * 3) + 140] & 0xFF);
        strip.setPixel(i, color);
      }
      strip.render();
      if (!frameBuffer.isEmpty()) {
        this.patchArtNetData();
      }
    }
    patching = false;
  }
}
