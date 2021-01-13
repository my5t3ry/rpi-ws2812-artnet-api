package de.my5t3ry.ledcontroller;

import ch.bildspur.artnet.ArtNetClient;
import ch.bildspur.artnet.events.ArtNetServerEventAdapter;
import ch.bildspur.artnet.packets.ArtNetPacket;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArtNetController {

  private ArtNetClient artnet;

  @Autowired
  private LedService ledService;

  @PostConstruct
  public void init() {
    artnet = new ArtNetClient();
    artnet.getArtNetServer().addListener(
        new ArtNetServerEventAdapter() {
          @Override
          public void artNetPacketReceived(ArtNetPacket packet) {
            log.info(String.format("package type {%s} " , packet.getType().name()));
            ledService.patchArtNetData(packet.getData());
          }
        });

    artnet.start();
  }
}
