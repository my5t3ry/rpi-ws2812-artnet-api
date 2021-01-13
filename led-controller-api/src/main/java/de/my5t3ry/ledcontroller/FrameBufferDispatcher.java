package de.my5t3ry.ledcontroller;


import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import java.util.concurrent.BlockingQueue;

public class FrameBufferDispatcher extends Thread {

  private final Ws281xLedStrip strip;
  private BlockingQueue<byte[]> frameBuffer;


  public FrameBufferDispatcher(BlockingQueue<byte[]> frameBuffer,
      Ws281xLedStrip strip) {
    this.strip = strip;
    this.frameBuffer = frameBuffer;
  }

  public void run() {
    while (true) {
      try {
        if (!frameBuffer.isEmpty()) {
          final byte[] curBuffer = frameBuffer.take();
          for (int i = 0; i < strip.getLedsCount(); i++) {
            final Color color = new Color(curBuffer[(i * 3) + 18] & 0xFF,
                curBuffer[(i * 3) + 19] & 0xFF,
                curBuffer[(i * 3) + 20] & 0xFF);
            strip.setPixel(i, color);
          }
          strip.render();
        }
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
    }
  }
}
