package org.jqassistant.contrib.plugin.go.scanner;

public class GoLanguageHelpers {

    public static boolean isExported(final String name) {
        return !name.substring(0, 1).equals(name.substring(0, 1).toLowerCase());
    }
}
