package kr.endsoft.softend.util;

public class TerminalColorUtil {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static String formatColor(String plainText) {
        return plainText
                .replaceAll("§r", ANSI_RESET)
                .replaceAll("§0", ANSI_BLACK)
                .replaceAll("§c", ANSI_RED)
                .replaceAll("§a", ANSI_GREEN)
                .replaceAll("§e", ANSI_YELLOW)
                .replaceAll("§9", ANSI_BLUE)
                .replaceAll("§d", ANSI_PURPLE)
                .replaceAll("§5", ANSI_PURPLE)
                .replaceAll("§b", ANSI_CYAN)
                .replaceAll("§f", ANSI_WHITE) + ANSI_RESET;
    }

}
