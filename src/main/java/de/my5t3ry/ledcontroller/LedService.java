package de.my5t3ry.ledcontroller;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
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

}
