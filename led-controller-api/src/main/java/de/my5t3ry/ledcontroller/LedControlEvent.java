package de.my5t3ry.ledcontroller;

import com.github.mbelling.ws281x.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedControlEvent {

  private Color color;
  private Integer brightness;
}
