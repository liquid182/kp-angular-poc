"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var platform_browser_1 = require("@angular/platform-browser");
var main_module_ngfactory_1 = require("./modules/main.module.ngfactory");
platform_browser_1.platformBrowser().bootstrapModuleFactory(main_module_ngfactory_1.MainModuleNgFactory);
