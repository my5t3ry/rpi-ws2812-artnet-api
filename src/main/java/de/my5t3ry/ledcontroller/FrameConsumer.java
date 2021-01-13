package de.my5t3ry.ledcontroller;


import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import java.util.concurrent.TransferQueue;
import lombok.SneakyThrows;

public class FrameConsumer implements Runnable {

  private TransferQueue<byte[]> frameBuffer;

  public FrameConsumer(TransferQueue<byte[]> frameBuffer) {
    this.frameBuffer = frameBuffer;
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
  }

  private static final int ledsCount = 96;
  private Ws281xLedStrip strip;

  @SneakyThrows
  @Override
  public void run() {
    while (true) {
      if (!frameBuffer.isEmpty()) {
        final byte[] curBuffer = frameBuffer.take();
        frameBuffer.remove(0);
        for (int i = 0; i < ledsCount; i++) {
          final Color color = new Color(curBuffer[(i * 3) + 138] & 0xFF,
              curBuffer[(i * 3) + 139] & 0xFF,
              curBuffer[(i * 3) + 140] & 0xFF);
          strip.setPixel(i, color);
        }
        strip.render();
      }
    }
  }

  // standard constructors
}
