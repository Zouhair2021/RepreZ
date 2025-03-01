package com.zouhair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.IntStream;

public class PanDessin extends JPanel
{
    private BufferedImage imageMill, imageCent, imageDiz, imageUn;
    private Image imageMillScaled, imageCentScaled, imageDizScaled, imageUnScaled;
    protected File dossier;
    private int milliers, centaines, dizaines, unites;
    private final int marge = 15;
    private final int largMill;
    private final int largCent;
    private final int largDiz;
    private final int largUn;
    private final int hautMill;
    private final int hautCent;
    private final int hautDiz;
    private final int hautUn;
    private int origX, origY, trans;
    protected String[] couleursBloc;


    public PanDessin()
    {   milliers = 0;
        centaines = 0;
        dizaines = 0;
        unites = 0;
        couleursBloc = RepNombres.couleursBloc;
        setPreferredSize(new Dimension(1000, 800));
        setBackground(Color.white);
        chargerImages();
        largMill = imageMillScaled.getWidth(this) + marge;
        largCent = imageCentScaled.getWidth(this) + marge;
        largDiz = imageDizScaled.getWidth(this) + marge;
        largUn = imageUnScaled.getWidth(this) + marge;
        hautMill = imageMillScaled.getHeight(this) + marge;
        hautCent = imageCentScaled.getHeight(this) + marge;
        hautDiz = imageDizScaled.getHeight(this) + marge;
        hautUn = imageUnScaled.getHeight(this) + marge;
        creerDossierDedestination();
    }


    private void creerDossierDedestination()
    {
        // Obtenir le chemin du dossier Images
        String userHome = System.getProperty("user.home");
        Path imagesPath = Paths.get(userHome, "Pictures", "RepreZ captures");

        // Créer un objet File pour le nouveau dossier
        dossier = imagesPath.toFile();

        try {
            // Vérifier si le dossier existe déjà
            if (!dossier.exists()) {
                // Créer le dossier
                boolean created = dossier.mkdir();

                if (created) {
                    System.out.println("Le dossier a été créé avec succès à : " + dossier.getAbsolutePath());
                } else {
                    System.out.println("Impossible de créer le dossier");
                }
            } else {
                System.out.println("Le dossier existe déjà à : " + dossier.getAbsolutePath());
            }

        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
            e.printStackTrace();
        }

    }



    protected void chargerImages()
    {
        int d = 50;
        try
        {   imageMill = ImageIO.read(getClass().getResource("/"+couleursBloc[0]));
            imageMillScaled = imageMill.getScaledInstance(132+d, 132+d, Image.SCALE_SMOOTH);
            imageCent = ImageIO.read(getClass().getResource("/"+couleursBloc[1]));
            imageCentScaled = imageCent.getScaledInstance(95+d, 95+d, Image.SCALE_SMOOTH);
            imageDiz = ImageIO.read(getClass().getResource("/"+couleursBloc[2]));
            imageDizScaled = imageDiz.getScaledInstance(20, 95+d, Image.SCALE_SMOOTH);
            imageUn = ImageIO.read(getClass().getResource("/"+couleursBloc[3]));
            imageUnScaled = imageUn.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            repaint();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected void setMilliers(int milliers) {this.milliers = milliers;}
    protected void setCentaines(int centaines)
    {
        this.centaines = centaines;
    }

    protected void setDizaines(int dizaines)
    {
        this.dizaines = dizaines;
    }

    protected void setUnites(int unites)
    {
        this.unites = unites;
    }


    protected void suppFichImages()
    {
        Path path = dossier.toPath();
        try
        {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes att) throws IOException
                {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException ioException) throws IOException
                {
                    if (!dir.equals(path))
                        Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, "Erreurs lors du suppreesion des fichiers");
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.translate(marge, marge);
        origX = 0; origY = 0;

        // Dessiner chaque bloc en ajustant l'espace
        dessinerBloc(g, imageMillScaled, milliers);
        translation(g, milliers, largMill);
        dessinerBloc(g, imageCentScaled, centaines);
        translation(g, centaines, largCent);
        dessinerBloc(g, imageDizScaled, dizaines);
        translation(g, dizaines, largDiz);
        dessinerBloc(g, imageUnScaled, unites);

        // Redimensionner le panneau de dessin
        resizePanDessin();
    }

    private void dessinerBloc(Graphics g, Image image, int chiffre) {
        int largeurImage = image.getWidth(this) + marge;
        int hauteurImage = image.getHeight(this) + marge;

        for (int i = 0; i < chiffre; i++) {
            g.drawImage(image, origX, origY, this);

            if (i < 2) {
                origX += largeurImage;
            } else if (i < 5) {
                if (i == 2) origX = 0;
                origY = hauteurImage;
                if (i != 2) origX += largeurImage;
            } else {
                if (i == 5) origX = 0;
                origY = 2 * hauteurImage;
                if (i != 5) origX += largeurImage;
            }
        }
    }

    private void translation(Graphics g, int chiffre, int largBloc) {
        if (chiffre == 0) {
            trans = 0;
        } else if (chiffre <= 3) {
            trans = chiffre * largBloc;
        } else {
            trans = 3 * largBloc;
        }
        g.translate(trans, 0);
        origX = 0;
        origY = 0;
    }

    private void resizePanDessin() {
        int largColMill = largMill * largCol(milliers);
        int largColCent = largCent * largCol(centaines);
        int largColDiz = largDiz * largCol(dizaines);
        int largColUn = largUn * largCol(unites);

        int hautLigneMill = (milliers == 0) ? 0 : ((milliers - 1) / 3 + 1) * hautMill;
        int hautLigneCent = (centaines == 0) ? 0 : ((centaines - 1) / 3 + 1) * hautCent;
        int hautLigneDiz = (dizaines == 0) ? 0 : ((dizaines - 1) / 3 + 1) * hautDiz;
        int hautLigneUn = (unites == 0) ? 0 : ((unites - 1) / 3 + 1) * hautUn;

        int largPan = largColMill + largColCent + largColDiz + largColUn + marge;
        int hautPan = Math.max(Math.max(hautLigneMill, hautLigneCent), Math.max(hautLigneDiz, hautLigneUn)) + marge;

        this.setPreferredSize(new Dimension(largPan, hautPan));
        this.revalidate();
    }

    private int largCol(int n) {
        if (n == 0) {
            return 0;
        } else if (n <= 3) {
            return n;
        } else {
            return 3;
        }
    }

    protected void capture(String filePath)
    {
        System.out.println("dossier = "+dossier);
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        this.paint(g2d);
        g2d.dispose();
        try
        {
            ImageIO.write(image, "png", new File(dossier, filePath));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}