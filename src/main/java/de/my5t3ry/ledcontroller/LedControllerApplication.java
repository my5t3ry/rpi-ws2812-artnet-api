package de.my5t3ry.ledcontroller;

import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LedControllerApplication {



  public static void main(String[] args) {

    SpringApplication.run(LedControllerApplication.class, args);
  }

}
