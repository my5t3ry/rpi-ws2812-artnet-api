package de.my5t3ry.ledcontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LedControllerApplication {


  public static void main(String[] args) {

    SpringApplication.run(LedControllerApplication.class, args);
  }

}
