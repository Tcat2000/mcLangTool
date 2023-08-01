package org.tcathebluecreper.library;

public class util {
    public static String[] removeExtensions(String[] strings) {
        String[] s = new String[strings.length];
        for(int i = strings.length - 1; i >= 0; i--) {
            s[i] = removeExtension(strings[i]);
        }
        return s;
    }
    public static String removeExtension(String string) {
        return string.substring(0, string.lastIndexOf('.'));
    }
}
