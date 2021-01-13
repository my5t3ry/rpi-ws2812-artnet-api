package de.my5t3ry.ledcontroller;

import ch.bildspur.artnet.ArtNetClient;
import ch.bildspur.artnet.events.ArtNetServerEventAdapter;
import ch.bildspur.artnet.packets.ArtNetPacket;
import ch.bildspur.artnet.packets.PacketType;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArtNetController {

  private ArtNetClient artnet;

  @Autowired
  private MatrixController matrixController;

  @PostConstruct
  public void init() {
    artnet = new ArtNetClient();
  }

  public void startArtNetClient() {
    artnet.getArtNetServer().addListener(
        new ArtNetServerEventAdapter() {
          @SneakyThrows
          @Override
          public void artNetPacketReceived(ArtNetPacket packet) {
            if (packet.getType().equals(PacketType.ART_OUTPUT)) {
              matrixController.addFrame(packet.getData());
            }
          }
        });
    artnet.start();
  }

  public void toggleClient() {
    if (artnet.isRunning()) {
      artnet.stop();
    } else {
      startArtNetClient();
    }
  }
}
