package com.zouhair;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.prefs.Preferences;

public class Main
{
    // Constantes pour les préférences
    private static final String PREF_NODE = "com/zouhair/reprez";
    private static final String PREF_LANGUAGE = "language";
    private static final String PREF_FIRST_RUN = "firstRun";

    public static void main(String[] args)
    {
        try
        {
            // Définir le look and feel natif
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Récupérer les préférences utilisateur
        Preferences prefs = Preferences.userRoot().node(PREF_NODE);
        boolean isFirstRun = prefs.getBoolean(PREF_FIRST_RUN, true);
        String savedLanguage = "fr"; // Valeur par défaut

        // Si c'est la première exécution, afficher le dialogue de langue
        if (isFirstRun)
        {
            savedLanguage = showLanguageDialog(prefs);
            // Marquer que l'application a déjà été exécutée
            prefs.putBoolean(PREF_FIRST_RUN, false);
        }
        else
        {
            // Sinon, récupérer la langue précédemment sélectionnée
            savedLanguage = prefs.get(PREF_LANGUAGE, "fr");
        }

        // Important: définir la locale avant de lancer l'application
        final String languageCode = savedLanguage;

        // Lancer l'application avec la langue définie
        SwingUtilities.invokeLater(() ->
        {
            // Définir la langue juste avant de créer l'interface
            switch (languageCode)
            {
                case "en":
                    MessagesBundle.setLocale(MessagesBundle.LOCALE_EN);
                    break;
                case "ar":
                    MessagesBundle.setLocale(MessagesBundle.LOCALE_AR);
                    break;
                default:
                    MessagesBundle.setLocale(MessagesBundle.LOCALE_FR);
                    break;
            }

            // Maintenant créer l'interface
            RepNombres repNombres = new RepNombres();
            repNombres.setVisible(true);
        });
    }

    private static String showLanguageDialog(Preferences prefs)
    {
        // Variable pour stocker le choix de langue
        final String[] selectedLanguage = {"fr"}; // Valeur par défaut

        // Créer une boîte de dialogue personnalisée
        JDialog dialog = new JDialog();
        dialog.setTitle("اللغة / Language / Langue");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel label = new JLabel("اختر لغتك / Choose your language / Choisissez votre langue", JLabel.CENTER);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));
        panel.add(label);

        // Boutons pour chaque langue - ordre changé: Arabe, Anglais, Français
        JButton arButton = new JButton("العربية");
        JButton enButton = new JButton("English");
        JButton frButton = new JButton("Français");

        arButton.addActionListener(e ->
        {
            selectedLanguage[0] = "ar";
            prefs.put(PREF_LANGUAGE, "ar");
            dialog.dispose();
        });

        enButton.addActionListener(e ->
        {
            selectedLanguage[0] = "en";
            prefs.put(PREF_LANGUAGE, "en");
            dialog.dispose();
        });

        frButton.addActionListener(e ->
        {
            selectedLanguage[0] = "fr";
            prefs.put(PREF_LANGUAGE, "fr");
            dialog.dispose();
        });

        // Ajouter les boutons dans le nouvel ordre
        panel.add(arButton);
        panel.add(enButton);
        panel.add(frButton);

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setVisible(true);

        // Retourner la langue sélectionnée
        return selectedLanguage[0];
    }
}