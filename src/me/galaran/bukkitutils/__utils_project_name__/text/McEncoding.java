package me.galaran.bukkitutils.__utils_project_name__.text;

import java.util.HashMap;
import java.util.Map;

public class McEncoding {

    private static final String WRONG =  "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ¸¨";
    private static final String RIGHT =  "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюяёЁ";

    private static final Map<Character, Character> encMap = new HashMap<Character, Character>();
    static {
        for (int i = 0; i < WRONG.length(); i++) {
            encMap.put(WRONG.charAt(i), RIGHT.charAt(i));
        }
    }

    public static String fix(String str) {
        if (str == null) return null;
        if (str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();

        for (char curChar : str.toCharArray()) {
            Character replace = encMap.get(curChar);
            sb.append(replace == null ? curChar : replace);
        }

        return sb.toString();
    }
}
