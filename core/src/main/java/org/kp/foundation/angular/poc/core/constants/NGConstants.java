package org.kp.foundation.angular.poc.core.constants;

public class NGConstants {
    public final static String NPM_INSTALL = "npm install";
    public final static String BUILD_AOT_CMD = "npm run build-aot";
    public final static String BUILD_JIT_CMD = "npm run build-jit";

    public final static String BASE_PROJECT_PATH = "/etc/angular/base-project";

    public final static String AOT_DIST = "/dist/";
    public final static String JIT_DIST = "/dist-jit/";

    public final static String COMPILED_FILENAME = "main.bundle.js";
    //Should be in the format kp-angular.<componentId>.< jit | aot >
    public final static String CLIENTLIB_CATEGORY_REGEX = "clientlibs.kp-angular.%s.%s";


    public final static String AOT = "aot";
    public final static String JIT = "jit";

    public final static String CLIENTLIB_BASE_DIR = "/etc/designs/kporg/generated/angular/";
    public final static String NG_SRC = "ng-src";
    public final static String NG_BASE_APP_DIR = "/src/app/components/";
    public final static String NG_UUID = "uuid";

    public final static String HTML_EXTENSION = ".html";

    public enum COMPILE_TYPE {
        AOT,
        JIT
    }

    public static String getCompileTypeString(COMPILE_TYPE compileType){
        String compileTypeString = null;
        switch(compileType){
            case AOT:
                compileTypeString = AOT;
                break;
            case JIT:
                compileTypeString = JIT;
                break;
        }
        return compileTypeString;
    }

    public static COMPILE_TYPE getCompileType(String compileTypeStr){
        COMPILE_TYPE compileType = null;
        switch(compileTypeStr){
            case AOT:
                compileType = COMPILE_TYPE.AOT;
                break;
            case JIT:
                compileType = COMPILE_TYPE.JIT;
                break;
        }
        return compileType;
    }

    public static String getDistForCompileType(COMPILE_TYPE compileType){
        String distFolderName = null;
        switch(compileType){
            case AOT:
                distFolderName = AOT_DIST;
                break;
            case JIT:
                distFolderName = JIT_DIST;
                break;
        }
        return distFolderName;
    }

    public static String getNpmBuildCmdForCompileType(COMPILE_TYPE compileType){
        String npmBuildCmd = null;
        switch(compileType){
            case AOT:
                npmBuildCmd = BUILD_AOT_CMD;
                break;
            case JIT:
                npmBuildCmd = BUILD_JIT_CMD;
                break;
        }
        return npmBuildCmd;
    }
}
