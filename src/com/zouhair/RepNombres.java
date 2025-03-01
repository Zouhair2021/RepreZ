
package com.zouhair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class RepNombres extends JFrame implements ActionListener
{
    private PanDessin panDessin;
    private JPanel panelNb, panel, panelAutoCapture;
    private JLabel lblMin, lblMax, lblNombreCapture, lblNombreSaisiCapture, lblSepar;
    private JTextField txtMin, txtMax, txtNombre, txtNombreCapture;
    private JButton btnGenerer, btnRepresenter, btnCapture, btnOuvrireDossierCaptures;
    private String messageFormatNbIncorrect = "Format de nombre incorrect";
    private String messageNomreInterval = "Les nombres ne peuvent pas être négatifs ou supérieurs à 9999";
    private int nombreCapture = 0;
    private JCheckBox checkAutoCapture;
    private int min, max;
    private boolean booleanCapture;
    private static final String LAST_USED_FOLDER = "LastUsedFolder";
    private SwingWorker<Void, Void> worker;
    private boolean isRunning = false;
    private boolean isPleinEcran = false;
    private JMenuBar menuBar;
    private JMenu menuOption;
    private JMenu menuCouleurs;
    private JMenu menuAffichage;
    private JRadioButtonMenuItem itemBleu;
    private JRadioButtonMenuItem itemRouge;
    private JRadioButtonMenuItem itemJaune;
    private JRadioButtonMenuItem itemPourpre;
    private JRadioButtonMenuItem itemVert;
    private JRadioButtonMenuItem itemBlanc;
    private JRadioButtonMenuItem itemNoir;
    private JRadioButtonMenuItem itemRose;
    private JRadioButtonMenuItem itemOrange;
    private JMenuItem itemPleinEcran;
    protected static String[] couleursBloc;
    private File dossier;
    private String[] noirs = {"Thousand_Block_black.png", "Hundred_Block_black.png", "Ten_Block_black.png", "One_Block_black.png"};
    private String[] bleus = {"Thousand_Block_blue.png", "Hundred_Block_blue.png", "Ten_Block_blue.png", "One_Block_blue.png"};
    private String[] verts = {"Thousand_Block_green.png", "Hundred_Block_green.png", "Ten_Block_green.png", "One_Block_green.png"};
    private String[] oranges = {"Thousand_Block_orange.png", "Hundred_Block_orange.png", "Ten_Block_orange.png", "One_Block_orange.png"};
    private String[] roses = {"Thousand_Block_pink.png", "Hundred_Block_pink.png", "Ten_Block_pink.png", "One_Block_pink.png"};
    private String[] pourpres = {"Thousand_Block_purple.png", "Hundred_Block_purple.png", "Ten_Block_purple.png", "One_Block_purple.png"};
    private String[] rouges = {"Thousand_Block_red.png", "Hundred_Block_red.png", "Ten_Block_red.png", "One_Block_red.png"};
    private String[] blancs = {"Thousand_Block_white.png", "Hundred_Block_white.png", "Ten_Block_white.png", "One_Block_white.png"};
    private String[] jaunes = {"Thousand_Block_yellow.png", "Hundred_Block_yellow.png", "Ten_Block_yellow.png", "One_Block_yellow.png"};
    private String messageOptCapture = "Voulez vous supprimer les fichiers images déjà existants dans ce dossier";
    private String messageOptAutoCapture = "Les fichiers images existants dans ce dossier vont être supprimés";
    private String messageNombreCaptures = "Le nombre de capture demandé est trop élevé";

    public RepNombres()
    {
        setTitle("RepreZ 2.0");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = Toolkit.getDefaultToolkit().getImage(RepNombres.class.getResource("/icons/numerique.png"));
        setIconImage(icon);
        creationBarreMenu();
        couleursBloc = bleus;
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
        panel.setBackground(Color.gray);
        panDessin = new PanDessin();
        String userHome = System.getProperty("user.home");
        Path imagesPath = Paths.get(userHome, "Pictures", "RepreZ captures");
        dossier = imagesPath.toFile();
        panDessin.dossier = dossier;
        panel.add(panDessin);
        add(panel);
        lblMin = new JLabel("Min");
        lblMax = new JLabel("Max");
        txtMin = new JTextField(5);
        txtMin.setText("0");
        txtMax = new JTextField(5);
        txtMax.setText("9999");
        txtNombre = new JTextField(5);
        txtNombre.addActionListener(this);
        btnGenerer = new JButton("Générer");
        btnGenerer.addActionListener(this);
        btnRepresenter = new JButton("Représenter");
        btnRepresenter.addActionListener(this);
        btnCapture = new JButton("Capturer");
        btnCapture.addActionListener(this);
        btnOuvrireDossierCaptures = new JButton("Ouvrir dossier captures");
        btnOuvrireDossierCaptures.addActionListener(this);
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
        panelNb.add(btnOuvrireDossierCaptures);
        panelNb.add(lblNombreCapture);
        add(panelNb, "South");

    }


    private void creationBarreMenu()
    {
        menuBar = new JMenuBar();
        menuOption = new JMenu("Options");
        menuCouleurs = new JMenu("Couleur");
        menuAffichage = new JMenu("Affichage");
        ButtonGroup groupCoulours = new ButtonGroup();
        itemBleu = new JRadioButtonMenuItem("Bleu");
        itemRouge = new JRadioButtonMenuItem("Rouge");
        itemJaune = new JRadioButtonMenuItem("Jaune");
        itemPourpre = new JRadioButtonMenuItem("Pourpre");
        itemVert = new JRadioButtonMenuItem("Vert");
        itemBlanc = new JRadioButtonMenuItem("Blanc");
        itemNoir = new JRadioButtonMenuItem("Noir");
        itemRose = new JRadioButtonMenuItem("Rose");
        itemOrange = new JRadioButtonMenuItem("Orange");
        itemPleinEcran = new JMenuItem("Plein Ecran");
        groupCoulours.add(itemBleu);
        groupCoulours.add(itemRouge);
        groupCoulours.add(itemJaune);
        groupCoulours.add(itemPourpre);
        groupCoulours.add(itemVert);
        groupCoulours.add(itemBlanc);
        groupCoulours.add(itemNoir);
        groupCoulours.add(itemRose);
        groupCoulours.add(itemOrange);
        menuCouleurs.add(itemBleu);
        itemBleu.setSelected(true);
        menuCouleurs.add(itemRouge);
        menuCouleurs.add(itemJaune);
        menuCouleurs.add(itemPourpre);
        menuCouleurs.add(itemVert);
        menuCouleurs.add(itemBlanc);
        menuCouleurs.add(itemNoir);
        menuCouleurs.add(itemRose);
        menuCouleurs.add(itemOrange);
        menuOption.add(menuCouleurs);
        menuAffichage.add(itemPleinEcran);
        menuBar.add(menuOption);
        menuBar.add(menuAffichage);
        setJMenuBar(menuBar);

        ajoutEcouteursElemntsMenu();

    }

    private void ajoutEcouteursElemntsMenu()
    {
        itemPleinEcran.addActionListener(ee ->
        {
            if (!isPleinEcran)
            {
                // On cache la fenêtre avant de modifier
                setVisible(false);
                dispose();
                setUndecorated(true);
                setVisible(true);
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
                itemPleinEcran.setText("Restaurer");
                isPleinEcran = true;
            } else
            {
                // Pour quitter le mode plein écran
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
                setVisible(false);
                dispose();
                setUndecorated(false);
                setVisible(true);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                itemPleinEcran.setText("Plein Ecran");
                isPleinEcran = false;
            }
        });
        itemBleu.addActionListener(ee ->
        {
            panDessin.couleursBloc = bleus;
            panDessin.chargerImages();
        });
        itemBlanc.addActionListener(ee ->
        {
            panDessin.couleursBloc = blancs;
            panDessin.chargerImages();
        });
        itemJaune.addActionListener(ee ->
        {
            panDessin.couleursBloc = jaunes;
            panDessin.chargerImages();
        });
        itemNoir.addActionListener(ee ->
        {
            panDessin.couleursBloc = noirs;
            panDessin.chargerImages();
        });
        itemVert.addActionListener(ee ->
        {
            panDessin.couleursBloc = verts;
            panDessin.chargerImages();
        });
        itemRouge.addActionListener(ee ->
        {
            panDessin.couleursBloc = rouges;
            panDessin.chargerImages();
        });
        itemOrange.addActionListener(ee ->
        {
            panDessin.couleursBloc = oranges;
            panDessin.chargerImages();
        });
        itemRose.addActionListener(ee ->
        {
            panDessin.couleursBloc = roses;
            panDessin.chargerImages();
        });
        itemPourpre.addActionListener(ee ->
        {
            panDessin.couleursBloc = pourpres;
            panDessin.chargerImages();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JComponent source = (JComponent) e.getSource();
        if (source == btnGenerer)
            if (checkAutoCapture.isSelected())
            {
                if (isRunning == false)
                    actionAutoCapture();
            } else
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
                btnRepresenter.setEnabled(false);
                lblNombreSaisiCapture.setForeground(Color.black);
            } else
            {
                txtNombreCapture.setEnabled(false);
                btnCapture.setEnabled(true);
                btnRepresenter.setEnabled(true);
                lblNombreSaisiCapture.setForeground(Color.LIGHT_GRAY);
            }
        }
        if (source == btnOuvrireDossierCaptures)
        {
            try
            {


                Runtime.getRuntime().exec("explorer.exe " + dossier);

                // Méthode 2 : Alternative avec ProcessBuilder
                // ProcessBuilder builder = new ProcessBuilder("explorer.exe", cheminDossier);
                // builder.start();

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void init(int nombre)
    {
        int milliers = nombre / 1000;
        panDessin.setMilliers(milliers);
        int centaines = (nombre % 1000) / 100;
        panDessin.setCentaines(centaines);
        int dizaines = (nombre % 100) / 10;
        panDessin.setDizaines(dizaines);
        int unites = nombre % 10;
        panDessin.setUnites(unites);
        panDessin.repaint();
    }

    private boolean verifier()
    {
        try
        {
            min = Integer.parseInt(txtMin.getText());
            max = Integer.parseInt(txtMax.getText());
            int nombreCapture = Integer.parseInt(txtNombreCapture.getText());
            if (verifInterval(min) && verifInterval(max) && nombreCapture <= max - min + 1 && nombreCapture <= 10000)
                return true;

            else
            {
                if (nombreCapture > max - min + 1 || nombreCapture > 10000)
                {
                    JOptionPane.showMessageDialog(this, messageNombreCaptures);

                } else
                {
                    JOptionPane.showMessageDialog(this, messageNomreInterval);
                    panDessin.getGraphics().clearRect(0, 0, panDessin.getWidth(), panDessin.getHeight());
                }
                return false;


            }


        } catch (NumberFormatException exception)
        {
            JOptionPane.showMessageDialog(this, messageFormatNbIncorrect);
            panDessin.getGraphics().clearRect(0, 0, panDessin.getWidth(), panDessin.getHeight());

            return false;
        }
    }

    private boolean verifInterval(int n)
    {
        return n >= 0 && n <= 9999;
    }

    private void actionGenerer()
    {
        if (verifier())
        {
            Random random = new Random();
            int n = random.nextInt(max - min + 1) + min;
            txtNombre.setText(n + "");
            init(n);
        }


    }

    private void actionRepresenter()
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

    private void actionCapturer()
    {

        File filePath;
        String fileName = (nombreCapture + 1) + ".png";
        if (!verifierChoixSuppressionImages(messageOptCapture))
        {

            int i = 1;
            do
            {
                if (i > 1)
                {
                    fileName = (nombreCapture + 1) + "_" + i + ".png";
                }
                filePath = new File(panDessin.dossier.getAbsolutePath(), fileName);
                i++;
            } while (filePath.exists());
        } else
            filePath = new File(panDessin.dossier.getAbsolutePath(), fileName);

        try
        {
            panDessin.capture(filePath.getName());
            nombreCapture++;
            lblNombreCapture.setText(nombreCapture + " captures");
            booleanCapture = false;
        } catch (Exception e)
        {
            System.err.println("Erreur de capture : " + e.getMessage());
        }
    }

    private void actionAutoCapture()
    {

        if (verifier())
            startWorker();
        booleanCapture = true;
    }

    private boolean verifierChoixSuppressionImages(String message)
    {
        if (booleanCapture == true)
        {

            UIManager.put("OptionPane.yesButtonText", "Oui");
            UIManager.put("OptionPane.noButtonText", "Non");

// Créer un son à partir d'un fichier audio système
            Toolkit.getDefaultToolkit().beep();

// Afficher la boîte de dialogue avec Non sélectionné par défaut
            Object[] options = {"Oui", "Non"};
            int choix = JOptionPane.showOptionDialog(this,
                    message,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]); // Sélectionne "Non" par défaut
            if (choix == JOptionPane.YES_OPTION)
            {
                panDessin.suppFichImages();
                nombreCapture = 0;
                return true;
            } else return false;
        }
        return false;
    }

    private void startWorker()
    {
        ArrayList<Integer> numbersGenerated = new ArrayList<>();
        if (verifierChoixSuppressionImages(messageOptAutoCapture))
        {
            Random random = new Random();
            worker = new SwingWorker<>()
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
                        return null;
                    }

                    for (int i = 0; i < nombre && !isCancelled(); i++)
                    {
                        if (isCancelled())
                        {
                            break;  // Sort immédiatement de la boucle si annulé
                        }

                        final int currentIndex = i + 1;
                        int n;
                        do
                        {
                            n = random.nextInt(max - min + 1) + min;
                        } while (numbersGenerated.contains(n));

                        final int m = n;
                        numbersGenerated.add(n);

                        try
                        {

                            SwingUtilities.invokeAndWait(() -> init(m));
                            Thread.sleep(100);
                            SwingUtilities.invokeAndWait(() -> panDessin.capture(currentIndex + ".png"));
                            txtNombre.setText(n + "");
                            lblNombreCapture.setText(currentIndex + " captures");

                        } catch (Exception ex)
                        {
                            if (!isCancelled())
                            {
                                ex.printStackTrace();
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void done()
                {
                    isRunning = false;
                    btnGenerer.setText("Générer");
                    try
                    {
                        get();
                        if (!isCancelled())
                        {
                            JOptionPane.showMessageDialog(RepNombres.this,
                                    nombre + " images générées avec succès");
                        }
                    } catch (InterruptedException | ExecutionException e)
                    {
                        e.printStackTrace();
                    } catch (CancellationException e)
                    {
                        JOptionPane.showMessageDialog(RepNombres.this,
                                "Le processus de génération a été arrêté",
                                "Génération arrêtée",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            };
            btnGenerer.setText("Annuler");
            isRunning = true;

            btnGenerer.addActionListener(e ->
            {
                if (isRunning)
                {
                    worker.cancel(true);
                    //   isRunning = false;
                    btnGenerer.setText("Générer");
                }
            });
            worker.execute();
        }
    }


}




