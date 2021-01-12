package de.my5t3ry.ledcontroller;

import static java.util.stream.Collectors.toList;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class LedService {

  private static final int ledsCount = 80;
  private Ws281xLedStrip strip;

  @PostConstruct
  public void init() {
    strip = new Ws281xLedStrip(
        ledsCount,       // leds
        18,          // Using pin 10 to do SPI, which should allow non-sudo access
        800000,  // freq hz
        10,            // dma
        255,      // brightness
        0,      // pwm channel
        false,        // invert
        LedStripType.WS2811_STRIP_RGB,    // Strip type
        false    // clear on exit
    );
    setControlEvent(new LedControlEvent(Color.WHITE, 255));
  }

  public void setControlEvent(final LedControlEvent event) {
    for (int i = 0; i < ledsCount; i++) {
      strip.setPixel(i, event.getColor());
    }
    strip.setBrightness(event.getBrightness());
    strip.render();
  }

  public static Color getStaticColorsForString(final String color) throws IllegalAccessException {
    final List<Field> colors = Arrays.stream(Color.class.getDeclaredFields()).filter(f ->
        Modifier.isStatic(f.getModifiers())).collect(toList()).stream()
        .filter(curColor -> curColor.getName().equals(color)).collect(
            Collectors.toUnmodifiableList());
    if (colors.size() != 1) {
      throw new IllegalStateException(String
          .format("Unexpected matching colors found for ['%s']. Found ['%s'], expected 1", color,
              colors.size()));
    }
    return (Color) colors.get(0).get(null);
  }

}
