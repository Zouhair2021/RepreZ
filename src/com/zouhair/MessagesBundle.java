package com.zouhair;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessagesBundle {
    private static ResourceBundle messages;
    private static Locale currentLocale;

    // Langues supportées
    public static final Locale LOCALE_FR = new Locale("fr", "FR");
    public static final Locale LOCALE_EN = new Locale("en", "US");
    public static final Locale LOCALE_AR = new Locale("ar", "MA");

    static {
        // Initialisation avec la locale par défaut (français)
        setLocale(LOCALE_FR);
    }

    /**
     * Change la langue de l'application
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        messages = ResourceBundle.getBundle("com.zouhair.messages", locale);
    }

    /**
     * Retourne la traduction d'une clé
     */
    public static String getString(String key) {
        try {
            return messages.getString(key);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }

    /**
     * Retourne la locale actuelle
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Vérifie si la langue actuelle est une langue RTL (de droite à gauche)
     */
    public static boolean isRightToLeft() {
        return currentLocale.getLanguage().equals("ar");
    }
}