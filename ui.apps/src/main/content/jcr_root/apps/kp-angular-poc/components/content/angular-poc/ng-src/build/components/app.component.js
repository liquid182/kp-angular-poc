import { Component, ElementRef } from '@angular/core';
var AppComponent = (function () {
    //aemInstanceId:string = 'err';
    function AppComponent(el) {
        //this.aemInstanceId = el.nativeElement.getAttribute('data-aeminstanceid');
    }
    return AppComponent;
}());
export { AppComponent };
AppComponent.decorators = [
    { type: Component, args: [{
                selector: '[data-ngapp-root]',
                templateUrl: './app-template.html',
                styleUrls: ["./hello-world.style.css"]
            },] },
];
/** @nocollapse */
AppComponent.ctorParameters = function () { return [
    { type: ElementRef, },
]; };
//# sourceMappingURL=app.component.js.map