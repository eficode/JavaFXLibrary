package javafxlibrary.utils;

public class RobotLog {

    public static void info(String message) {
        System.out.println("*INFO* " + message);
    }

    public static void debug(String message) {
        System.out.println("*DEBUG* " + message);
    }

    public static void trace(String message) {
        System.out.println("*TRACE* " + message);
    }

    public static void warn(String message) {
        System.out.println("*WARN* " + message);
    }

    public static void error(String message) {
        System.out.println("*ERROR* " + message);
    }
}
