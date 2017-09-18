package org.kp.foundation.angular.poc.core.constants;

public class AngularConstants {
    public static String BUILD_AOT_CMD = "npm run build-aot";
    public static String BUILD_JIT_CMD = "npm run build-jit";

    public static String AOT_DIST = "/dist/";
    public static String JIT_DIST = "/dist-jit/";

    public static String COMPILED_FILENAME = "main.bundle.js";
    //Should be in the format kp-angular.<componentName>.< jit | aot >
    public static String CLIENTLIB_CATEGORY_REGEX = "kp-angular.{}.{}";

    public static String AOT = "aot";
    public static String JIT = "jit";
}
