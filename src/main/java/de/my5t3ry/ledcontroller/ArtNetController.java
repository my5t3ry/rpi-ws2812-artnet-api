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
            log.info(String.format("new packet received!['%s']", packet.toString()));
          }
        });

    artnet.start();
  }
}
