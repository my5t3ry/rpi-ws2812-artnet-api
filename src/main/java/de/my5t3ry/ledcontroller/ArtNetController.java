package de.my5t3ry.ledcontroller;

import ch.bildspur.artnet.ArtNetClient;
import ch.bildspur.artnet.events.ArtNetServerEventAdapter;
import ch.bildspur.artnet.packets.ArtNetPacket;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArtNetController {

  private ArtNetClient artnet;

  @PostConstruct
  public void init() {
    artnet = new ArtNetClient();
    artnet.getArtNetServer().addListener(
        new ArtNetServerEventAdapter() {
          @Override
          public void artNetPacketReceived(ArtNetPacket packet) {
            final byte[] data = packet.getData();
            final int r = data[0] & 0xFF;
            final int g = data[1] & 0xFF;
            final int b = data[2] & 0xFF;

            log.info(String.format("R: " +r + " Green: " +g+ " Blue: " +b));
          }
        });

    artnet.start();
  }
}
