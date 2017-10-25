package org.kp.foundation.angular.poc.core.constants;

public class NGConstants {
    public static String NPM_INSTALL = "npm install";
    public static String BUILD_AOT_CMD = "npm run build-aot";
    public static String BUILD_JIT_CMD = "npm run build-jit";

    public static String BASE_PROJECT_PATH = "/etc/angular/base-project";

    public static String AOT_DIST = "/dist/";
    public static String JIT_DIST = "/dist-jit/";

    public static String COMPILED_FILENAME = "main.bundle.js";
    //Should be in the format kp-angular.<componentId>.< jit | aot >
    public static String CLIENTLIB_CATEGORY_REGEX = "clientlibs.kp-angular.%s.%s";

    public static String AOT = "aot";
    public static String JIT = "jit";

    public static String CLIENTLIB_BASE_DIR = "/etc/designs/kporg/generated/angular/";

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
