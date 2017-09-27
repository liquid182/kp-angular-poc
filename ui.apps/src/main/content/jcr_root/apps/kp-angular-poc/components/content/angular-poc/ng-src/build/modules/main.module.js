import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from '../components/app.component';
import { HelloWorldComponent } from '../components/hello-world.component';
export { AppComponent };
var MainModule = (function () {
    function MainModule() {
    }
    return MainModule;
}());
export { MainModule };
MainModule.decorators = [
    { type: NgModule, args: [{
                bootstrap: [AppComponent],
                declarations: [AppComponent, HelloWorldComponent],
                imports: [BrowserModule],
                providers: []
            },] },
];
/** @nocollapse */
MainModule.ctorParameters = function () { return []; };
//# sourceMappingURL=main.module.js.map