import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from '../components/app.component';
${properties.ngApp.imports}

export { AppComponent };

@NgModule({
  bootstrap: [AppComponent],
  ${properties.ngApp.declarations},
  imports: [BrowserModule],
  providers: []
})
export class MainModule {}
//
