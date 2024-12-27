package com.zouhair;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.IntStream;

public class PanDessin extends JPanel
{
    BufferedImage imageCent, imageDiz, imageUn;
    Image imageCentScaled, imageDizScaled, imageUnScaled;
    int largPan = 0, hauPan = 0;
    static File dossier;
    private int centaines, dizaines, unites;


    public PanDessin()
    {
        centaines = 0;
        dizaines = 0;
        unites = 0;
        setPreferredSize(new Dimension(540, 370));
        setBackground(Color.white);
        try
        {
            imageCent = ImageIO.read(getClass().getResource("/imgCent.tif"));
            imageCentScaled = imageCent.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageDiz = ImageIO.read(getClass().getResource("/imgDiz.tif"));
            imageDizScaled = imageDiz.getScaledInstance(10, 100, Image.SCALE_SMOOTH);
            imageUn = ImageIO.read(getClass().getResource("/imgUn.tif"));
            imageUnScaled = imageUn.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setCentaines(int centaines)
    {
        this.centaines = centaines;
    }

    public void setDizaines(int dizaines)
    {
        this.dizaines = dizaines;
    }

    public void setUnites(int unites)
    {
        this.unites = unites;
    }

    public static void creationDossier()
    {

        if (!dossier.exists())
        {
            dossier.mkdir();
        }
    }

    public void suppFichImages()
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

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        final int e = 15;
        g.translate(e, e);

        int origX = 0, origY = 0;
        int centLarg = imageCentScaled.getWidth(this);
        int centHaut = imageCentScaled.getHeight(this);
        int dizLarg = imageDizScaled.getWidth(this);
        int dizHaut = imageDizScaled.getHeight(this);
        int unLarg = imageUnScaled.getWidth(this);
        int unHaut = imageUnScaled.getHeight(this);
        int hauCentLigne = 0, hauDizLigne = 0, hauUnLigne = 0;
        int largCentCol = 0, largDizCol = 0, largUnCol = 0;
        for (int i = 0; i < centaines; i++)
        {
            g.drawImage(imageCentScaled, origX, origY, this);
            if (i < 2)
                origX += centLarg + e;
            else if (i < 5)
            {
                if (i == 2) origX = 0;
                origY = centHaut + e;
                if (i != 2) origX += centLarg + e;
            } else
            {
                if (i == 5) origX = 0;
                origY = 2 * (centHaut + e);
                if (i != 5) origX += centLarg + e;
            }
        }
        int trans;
        int largCentaine = centLarg + e;
        if (centaines == 0)
        {
            trans = 0;
            largCentCol = trans;
        } else if (centaines == 1)
        {
            trans = largCentaine;
            largCentCol = trans;
        } else if (centaines == 2)
        {
            trans = 2 * largCentaine;
            largCentCol = trans;

        } else
        {
            trans = 3 * largCentaine;
            largCentCol = trans;
        }
        g.translate(trans + e, 0);
        origX = 0;
        origY = 0;
        for (int i = 0; i < dizaines; i++)
        {
            g.drawImage(imageDizScaled, origX, origY, this);
            if (i < 2)
                origX += dizLarg + e;
            else if (i < 5)
            {
                if (i == 2) origX = 0;
                origY = dizHaut + e;
                if (i != 2) origX += dizLarg + e;
            } else
            {
                if (i == 5) origX = 0;
                origY = 2 * (dizHaut + e);
                if (i != 5) origX += dizLarg + e;
            }
        }
        int largDizaine = imageDizScaled.getWidth(this) + e;
        if (dizaines == 0)
        {
            trans = 0;
            largDizCol = trans;
        } else if (dizaines == 1)
        {
            trans = largDizaine;
            largDizCol = trans;
        } else if (dizaines == 2)
        {
            trans = 2 * largDizaine;
            largDizCol = trans;
        } else
        {
            trans = 3 * largDizaine;
            largDizCol = trans;
        }
        g.translate(trans + e, 0);
        origX = 0;
        origY = 0;
        for (int i = 0; i < unites; i++)
        {
            g.drawImage(imageUnScaled, origX, origY, this);
            if (i < 2)
                origX += unLarg + e;
            else if (i < 5)
            {
                if (i == 2) origX = 0;
                origY = unHaut + e;
                if (i != 2) origX += unLarg + e;
            } else
            {
                if (i == 5) origX = 0;
                origY = 2 * (unHaut + e);
                if (i != 5) origX += unLarg + e;
            }
        }
        if (unites == 0)
            largUnCol = 0;
        else if (unites == 1)
            largUnCol = unLarg + e;
        else if (unites == 2)
            largUnCol = 2 * unLarg + e;
        else largUnCol = 3 * unLarg + e;

        //Definir la hauteur du panneau
        if (centaines != 0)
        {
            if (centaines < 4)
                hauCentLigne = centHaut + e;
            else if (centaines < 7)
                hauCentLigne = 2 * (centHaut + e);
            else hauCentLigne = 3 * (centHaut + e);
        }
        if (dizaines != 0)
        {
            if (dizaines < 4)
                hauDizLigne = dizHaut + e;
            else if (dizaines < 7)
                hauDizLigne = 2 * (dizHaut + e);
            else hauDizLigne = 3 * (dizHaut + e);
        }
        if (unites != 0)
        {
            if (unites < 4)
                hauUnLigne = unHaut + e;
            else if (unites < 7)
                hauUnLigne = 2 * (unHaut + e);
            else hauUnLigne = 3 * (unHaut + e);
        }
        int[] tabHaut = {hauCentLigne, hauDizLigne, hauUnLigne};
        hauPan = IntStream.of(tabHaut).max().getAsInt();
        largPan = largCentCol + largDizCol + largUnCol;


        this.setPreferredSize(new Dimension(largPan + 5 * e, hauPan + e));
        revalidate();


    }

    public void capture(String filePath)
    {
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