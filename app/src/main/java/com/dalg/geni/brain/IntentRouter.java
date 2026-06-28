
package com.dalg.geni.brain;

public class IntentRouter {

    public static String route(String text) {

        text = text.toLowerCase();

        if (text.contains("زنگ")) {
            return "CALL";
        }

        if (text.contains("پیام")) {
            return "SMS";
        }

        if (text.contains("بلندگو")) {
            return "AUDIO";
        }

        return "AI";
    }
}
