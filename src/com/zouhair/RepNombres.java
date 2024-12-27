package com.zouhair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
import java.util.Random;
import java.util.prefs.Preferences;

public class RepNombres extends JFrame implements ActionListener
{
    PanDessin panDessin;
    JPanel panelNb, panel, panelAutoCapture;
    JLabel lblMin, lblMax, lblNombreCapture, lblNombreSaisiCapture, lblSepar;
    JTextField txtMin, txtMax, txtNombre, txtNombreCapture;
    JButton btnGenerer, btnRepresenter, btnCapture;
    String messageFormatNbIncorrect = "Format de nombre incorrect";
    String messageNomreInterval = "Les nombres ne peuvent pas être négatifs ou supérieurs à 999";
    int nombreCapture = 0;
    JCheckBox checkAutoCapture;
    int min, max;
    boolean booleanCapture;
    private static final int ICON_WIDTH = 40;  // Largeur souhaitée de l'icône
    private static final int ICON_HEIGHT = 40;// Hauteur souhaitée de l'icône
    private static final String LAST_USED_FOLDER = "LastUsedFolder";


    public RepNombres()
    {
        setTitle("RepreZ 1.0");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = Toolkit.getDefaultToolkit().getImage(RepNombres.class.getResource("/icons/numerique.png"));
        setIconImage(icon);
        booleanCapture = true;
        panelAutoCapture = new JPanel();
        checkAutoCapture = new JCheckBox("Capture auto");
        checkAutoCapture.addActionListener(this);
        lblNombreSaisiCapture = new JLabel("Nombre de captures ");
        lblNombreSaisiCapture.setForeground(Color.LIGHT_GRAY);
        lblSepar = new JLabel("  ");
        lblSepar.setPreferredSize(new Dimension(20, 20));
        txtNombreCapture = new JTextField(5);
        panelAutoCapture.add(checkAutoCapture);
        panelAutoCapture.add(lblSepar);
        panelAutoCapture.add(lblNombreSaisiCapture);
        panelAutoCapture.add(txtNombreCapture);
        add(panelAutoCapture, "North");
        txtNombreCapture.setText("10");
        txtNombreCapture.setEnabled(false);
        panel = new JPanel();
        panel.setBackground(Color.white);
        panDessin = new PanDessin();
        panel.add(panDessin);
        add(panel);
        lblMin = new JLabel("Min");
        lblMax = new JLabel("Max");
        txtMin = new JTextField(5);
        txtMin.setText("100");
        txtMax = new JTextField(5);
        txtMax.setText("999");
        txtNombre = new JTextField(5);
        txtNombre.addActionListener(this);
        btnGenerer = new JButton("Générer");
        btnGenerer.addActionListener(this);
        btnRepresenter = new JButton("Représenter");
        btnRepresenter.addActionListener(this);
        btnCapture = new JButton("Capturer");
        btnCapture.addActionListener(this);
        lblNombreCapture = new JLabel();
        panelNb = new JPanel();
        panelNb.add(lblMin);
        panelNb.add(txtMin);
        panelNb.add(lblMax);
        panelNb.add(txtMax);
        panelNb.add(btnGenerer);
        panelNb.add(txtNombre);
        panelNb.add(btnRepresenter);
        panelNb.add(btnCapture);
        panelNb.add(lblNombreCapture);
        add(panelNb, "South");
    }

    public static String getAppDataLocalPath() {
        String localAppData = System.getenv("LOCALAPPDATA");
        if (localAppData == null) {
            // Fallback pour d'autres systèmes ou si LOCALAPPDATA n'est pas défini
            localAppData = System.getProperty("user.home") +
                    File.separator + "AppData" +
                    File.separator + "Local";
        }
        return localAppData + File.separator + "RepreZ";
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JComponent source = (JComponent) e.getSource();
        if (source == btnGenerer)
            if (checkAutoCapture.isSelected())
                actionAutoCapture();
            else
                actionGenerer();
        if (source == btnRepresenter || source == txtNombre)
            actionRepresenter();
        if (source == btnCapture)
            actionCapturer();
        if (source == checkAutoCapture)
        {
            if (checkAutoCapture.isSelected())
            {
                txtNombreCapture.setEnabled(true);
                btnCapture.setEnabled(false);
                lblNombreSaisiCapture.setForeground(Color.black);
            } else
            {
                txtNombreCapture.setEnabled(false);
                btnCapture.setEnabled(true);
                lblNombreSaisiCapture.setForeground(Color.LIGHT_GRAY);
            }
        }
    }

    public void init(int n)
    {
        int cent = n / 100;
        panDessin.setCentaines(cent);
        int diz = (n - cent * 100) / 10;
        panDessin.setDizaines(diz);
        int un = n - (cent * 100 + diz * 10);
        panDessin.setUnites(un);
        panDessin.repaint();
    }

    public boolean verifier()
    {
        try
        {
            min = Integer.parseInt(txtMin.getText());
            max = Integer.parseInt(txtMax.getText());
            if (verifInterval(min) && verifInterval(max))
                return true;
            else
            {
                JOptionPane.showMessageDialog(this, messageNomreInterval);
                panDessin.getGraphics().clearRect(0, 0, panDessin.getWidth(), panDessin.getHeight());
                return false;


            }

        } catch (NumberFormatException exception)
        {
            JOptionPane.showMessageDialog(this, messageFormatNbIncorrect);
            panDessin.getGraphics().clearRect(0, 0, panDessin.getWidth(), panDessin.getHeight());

            return false;
        }
    }

    public boolean verifInterval(int n)
    {
        return n >= 0 && n <= 999;
    }

    public void actionGenerer()
    {
        if (verifier())
        {
            Random random = new Random();
            int n = random.nextInt(max - min + 1) + 1;
            txtNombre.setText(n + "");
            init(n);
        }


    }

    public void actionRepresenter()
    {
        try
        {
            int n = Integer.parseInt(txtNombre.getText());
            if (verifInterval(n))
                init(n);
            else JOptionPane.showMessageDialog(this, messageNomreInterval);
        } catch (NumberFormatException exception)
        {
            JOptionPane.showMessageDialog(this, messageFormatNbIncorrect);

        }
    }

    public void actionCapturer()
    {
        if (booleanCapture == true)
        {
            PanDessin.dossier = chooseDirectory();
            PanDessin.creationDossier();
            UIManager.put("OptionPane.yesButtonText", "Oui");
            UIManager.put("OptionPane.noButtonText", "Non");

// Créer un son à partir d'un fichier audio système
            Toolkit.getDefaultToolkit().beep();

// Afficher la boîte de dialogue avec Non sélectionné par défaut
            Object[] options = {"Oui", "Non"};
            int choix = JOptionPane.showOptionDialog(this,
                    "Voulez vous supprimer les fichiers images déjà existants dans ce dossier",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]); // Sélectionne "Non" par défaut
            Toolkit.getDefaultToolkit().beep();
            if (choix == JOptionPane.YES_OPTION)
            {
                panDessin.suppFichImages();
                nombreCapture = 0;
            }
        }

        panDessin.capture((nombreCapture + 1) + ".png");
        nombreCapture++;
        lblNombreCapture.setText(nombreCapture + " captures");
        booleanCapture = false;
    }

    public void actionAutoCapture()
    {
        PanDessin.dossier = chooseDirectory();
        PanDessin.creationDossier();
        int choix = JOptionPane.showConfirmDialog(this, "Les fichiers image " +
                        "dans ce dossier vont être supprimés", "Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choix == JOptionPane.YES_OPTION)
            panDessin.suppFichImages();
        else return;
        panDessin.suppFichImages();
        if (verifier())
        {
            Random random = new Random();

            SwingWorker<Void, Void> worker = new SwingWorker<>()
            {
                int nombre = 0;

                @Override
                protected Void doInBackground()
                {
                    try
                    {
                        nombre = Integer.parseInt(txtNombreCapture.getText());
                    } catch (NumberFormatException e)
                    {
                        JOptionPane.showMessageDialog(RepNombres.this, messageFormatNbIncorrect);
                    }
                    for (int i = 0; i < nombre; i++)
                    {
                        btnGenerer.setEnabled(false);
                        final int currentIndex = i + 1;
                        int n = random.nextInt(max - min + 1) + min;
                        try
                        {
                            SwingUtilities.invokeAndWait(() -> init(n));
                            Thread.sleep(100);
                            SwingUtilities.invokeAndWait(() -> panDessin.capture(currentIndex +
                                    ".png"));
                            txtNombre.setText(n + "");
                            lblNombreCapture.setText(currentIndex + " captures");
                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                    return null;
                }

                protected void done()
                {

                    JOptionPane.showMessageDialog(RepNombres.this, nombre + " images générées " +
                            " avec succès");
                    btnGenerer.setEnabled(true);
                }
            };
            worker.execute();
        }
        booleanCapture = true;
    }
    public  File chooseDirectory() {
        // Création d'une nouvelle instance de JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Configuration pour ne sélectionner que des répertoires
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        Preferences prefs = Preferences.userNodeForPackage(RepNombres.class);
        String lastUsedFolder = prefs.get(LAST_USED_FOLDER, System.getProperty("user.home"));
        fileChooser.setCurrentDirectory(new File(lastUsedFolder));
        fileChooser.setDialogTitle("Sélectionner un répertoire");

        // Désactivation du filtre "Tous les fichiers"
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Affichage de la boîte de dialogue et récupération de la réponse
        int result = fileChooser.showOpenDialog(this);

        // Si l'utilisateur a sélectionné un répertoire et cliqué sur OK
        if (result == JFileChooser.APPROVE_OPTION) {
            prefs.put(LAST_USED_FOLDER, fileChooser.getSelectedFile().getAbsolutePath());
            return fileChooser.getSelectedFile();
        }

        // Si l'utilisateur a annulé, retourner null
        return null;
    }


}




