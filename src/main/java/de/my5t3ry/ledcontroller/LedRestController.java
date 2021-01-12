package de.my5t3ry.ledcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("led")
public class LedRestController {

  @Autowired
  private LedService ledService;

  @PostMapping()
  public ResponseEntity save(@RequestBody LedControlEvent event) {
    ledService.setControlEvent(event);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
