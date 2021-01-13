import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'led-controller-web';
  api: string = "http://192.168.0.10:8080/led-api/led"
  state: any;
  color: any;

  constructor(private http: HttpClient) {
  }

  togglePower() {
    this.http.post(this.api + "/toggle-power", null).toPromise().then(value => {
      console.log(value);
    })
  }

  hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
      red: parseInt(result[1], 16),
      green: parseInt(result[2], 16),
      blue: parseInt(result[3], 16)
    } : null;
  }

  setColor() {
    let event = {
      color: this.hexToRgb(this.color),
      brightness: 255
    }
    this.http.post(this.api, event).toPromise().then(value => {
      console.log(value);
    })
  }

  toggleArtNetNode() {
    this.http.post(this.api + "/toggle-artnet", null).toPromise().then(value => {
      console.log(value);
    })

  }
}
