"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var platform_browser_dynamic_1 = require("@angular/platform-browser-dynamic");
var main_module_1 = require("./modules/main.module");
platform_browser_dynamic_1.platformBrowserDynamic().bootstrapModule(main_module_1.MainModule);
