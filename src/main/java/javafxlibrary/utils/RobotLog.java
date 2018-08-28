package javafxlibrary.utils;

import java.util.LinkedList;

public class RobotLog {

    private static boolean ignoreDuplicates = false;
    private static LinkedList<String> loggedMessages = new LinkedList<>();


    public static void ignoreDuplicates() {
        ignoreDuplicates = true;
    }

    public static void reset() {
        ignoreDuplicates = false;
        loggedMessages.clear();
    }

    public static void info(String message) {
        if (shouldLogMessage(message))
            System.out.println("*INFO* " + message);
    }

    public static void debug(String message) {
        if (shouldLogMessage(message))
            System.out.println("*DEBUG* " + message);
    }

    public static void trace(String message) {
        if (shouldLogMessage(message))
            System.out.println("*TRACE* " + message);
    }

    public static void warn(String message) {
        if (shouldLogMessage(message))
            System.out.println("*WARN* " + message);
    }

    public static void error(String message) {
        if (shouldLogMessage(message))
            System.out.println("*ERROR* " + message);
    }

    private static boolean shouldLogMessage(String message) {
        if (ignoreDuplicates) {
            if (loggedMessages.contains(message)) {
                return false;
            } else {
                loggedMessages.add(message);
                return true;
            }
        } else {
            return true;
        }
    }
}
