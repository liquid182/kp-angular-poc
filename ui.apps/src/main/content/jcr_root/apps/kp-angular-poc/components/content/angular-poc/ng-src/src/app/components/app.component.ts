import { Component, ElementRef } from '@angular/core';

@Component({
  selector: '[data-ngapp-root]',
  templateUrl: './app-template.html',
  styleUrls: ["./hello-world.style.css"]
})
export class AppComponent {
  //aemInstanceId:string = 'err';

  constructor (el:ElementRef) {
    //this.aemInstanceId = el.nativeElement.getAttribute('data-aeminstanceid');
  }
}
