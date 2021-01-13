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
  private Color currentColor = Color.WHITE;
  BlockingQueue<byte[]> frameBuffer = new LinkedBlockingDeque<>();

  @PostConstruct
  @Async
  public void init() {
    strip = new Ws281xLedStrip(
        ledsCount,
        18,
        800000,
        10,
        255,
        0,
        false,
        LedStripType.WS2811_STRIP_RGB,
        false
    );
    final FrameBufferDispatcher frameBufferDispatcher = new FrameBufferDispatcher(frameBuffer,
        strip);
    setControlEvent(new LedControlEvent(Color.WHITE, 255));
    frameBufferDispatcher.start();
  }

  public void setControlEvent(final LedControlEvent event) {
    log.info(String.format("Setting color to [%s]", event.getColor().toString()));
    currentColor = event.getColor();
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
      setControlEvent(new LedControlEvent(currentColor, 255));
      power = true;
    } else {
      setControlEvent(new LedControlEvent(Color.BLACK, 255));
      power = false;
    }
    strip.render();
  }
}
