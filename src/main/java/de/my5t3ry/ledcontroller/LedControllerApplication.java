package de.my5t3ry.ledcontroller;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LedControllerApplication {

  private static final int ledsCount = 80;

  public static void main(String[] args) {
    final Ws281xLedStrip strip = new Ws281xLedStrip(
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
    for (int i = 0; i < ledsCount; i++) {
      strip.setPixel(i, Color.RED);
    }
    strip.render();
    SpringApplication.run(LedControllerApplication.class, args);
  }

}
