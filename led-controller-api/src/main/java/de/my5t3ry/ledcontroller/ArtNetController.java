package de.my5t3ry.ledcontroller;

import ch.bildspur.artnet.ArtNetBuffer;
import ch.bildspur.artnet.ArtNetClient;
import ch.bildspur.artnet.events.ArtNetServerEventAdapter;
import ch.bildspur.artnet.packets.ArtNetPacket;
import ch.bildspur.artnet.packets.PacketType;
import java.util.Objects;
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

  public void startArtNetClient() {
    artnet = new ArtNetClient(new ArtNetBuffer(), 6000, 6000);
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
    if (Objects.nonNull(artnet) && artnet.isRunning()) {
      artnet.stop();
    } else {
      startArtNetClient();
    }
  }
}
