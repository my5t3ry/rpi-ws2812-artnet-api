package de.my5t3ry.ledcontroller;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MatrixController {


  private static final int ledsCount = 96;
  private Ws281xLedStrip strip;


  private boolean power = true;
  BlockingQueue<byte[]> frameBuffer = new LinkedBlockingDeque<>();


  @PostConstruct
  @Async
  public void init() {
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
    setControlEvent(new LedControlEvent(Color.WHITE, 255));
    frameBufferDispatcher.start();
  }

  public void setControlEvent(final LedControlEvent event) {
    for (int i = 0; i < ledsCount; i++) {
      strip.setPixel(i, event.getColor());
    }
    strip.setBrightness(event.getBrightness());
    strip.render();
  }


  public void addFrame(final byte[] frame) throws InterruptedException {
    frameBuffer.put(frame);
  }


  public void togglePower() {
    if (!power) {
      setControlEvent(new LedControlEvent(Color.WHITE, 255));
      power = true;
    } else {
      setControlEvent(new LedControlEvent(Color.BLACK, 255));
      power = false;
    }
    strip.render();
  }
}
