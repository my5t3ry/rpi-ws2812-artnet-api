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

  constructor(private http: HttpClient) {
  }

  togglePower() {
    this.http.post(this.api + "/power", null).toPromise().then(value => {
      console.log(value);
    })
  }
}
