package de.my5t3ry.ledcontroller;

import static java.util.stream.Collectors.toList;

import com.github.mbelling.ws281x.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class FrameBufferController {


  private boolean patching = false;
  BlockingQueue<byte[]> frameBuffer = new SynchronousQueue<>();

  public AtomicInteger frameBufferSize
      = new AtomicInteger();

  @PostConstruct
  @Async
  public void init() throws InterruptedException {
    final FrameConsumer frameConsumer = new FrameConsumer(frameBuffer);
    setControlEvent(new LedControlEvent("WHITE", 255));
    frameConsumer.start();
  }

  public void setControlEvent(final LedControlEvent event) {
//    frameBuffer = new ArrayList<>();
//    for (int i = 0; i < ledsCount; i++) {
//      strip.setPixel(i, getStaticColorsForString(event.getColor()));
//    }
//    strip.setBrightness(event.getBrightness());
//    strip.render();
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


}
