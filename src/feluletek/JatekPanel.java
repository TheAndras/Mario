/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feluletek;

import adatkezeles.AdatInput;
import adatkezeles.FileInput;
import alaposztalyok.Box;
import alaposztalyok.Cso;
import alaposztalyok.Ellenseg;
import alaposztalyok.Global;
import alaposztalyok.Lepcso;
import alaposztalyok.PalyaElem;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import vezerles.Vezerlo;

/**
 *
 * @author thean
 */
public class JatekPanel extends javax.swing.JPanel {

    private Image hatterKep = new ImageIcon(this.getClass().getResource("/media/kezdoHatter.jpg")).getImage();
    private Image fejlecKep = new ImageIcon(this.getClass().getResource("/media/fejlec.png")).getImage();
    private int hatterX = 0, hatterY = 0;
    private int fejlecX=164;
    private int kepSzelesseg = 800;
    private int fejlecSzelesseg = 471, fejlecMagassag = 64;
    private int ermeX=403, ermeY=60;
    private int erme;
    private Vezerlo vezerlo = null;
    private boolean elindult = false;
    private List<Box> boxok = new ArrayList<>();
    private List<PalyaElem> palyaLista = new ArrayList<>();
    private List<Cso> csovek;
    private List<Lepcso> lepcso;
    private List<Ellenseg> ellensegek;

    public JatekPanel() {
        initComponents();
    }

    /**
    Szokásos rajzolásokon kívül itt rajzolom ki a fejlécet, illetve a gyűjtött érmék számát.
    */
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(hatterKep, hatterX, hatterY, kepSzelesseg, this.getHeight(), null);
        
        if (vezerlo != null) {
            vezerlo.rajzol(g);
        }
        
        
        if (elindult) {
            g.drawImage(fejlecKep, fejlecX, hatterY, fejlecSzelesseg, fejlecMagassag, null);
            Font font = new Font("Tahoma", 1, 24);
            g.setFont(font);
            g.setColor(Color.WHITE);
            if (erme < 10) {
                g.drawString("0 " + erme, ermeX, ermeY);
            } else {
                g.drawString("" + erme, ermeX, ermeY);
            }
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    /**
    Kezdőképernyő betöltése után egérgombnyomásra elindul a játék.
    */
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (!elindult) {
            adatBeolvasas();
            elindult = true;
            kepSzelesseg = Global.HATTER_SZELESSEG;
            hatterKep = new ImageIcon(this.getClass().getResource("/media/hatter.png")).getImage();
            vezerlo.indul(boxok, palyaLista, csovek, lepcso, ellensegek);
            repaint();
        }
    }//GEN-LAST:event_formMouseClicked

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
    Adat file beolvasás.
    Szokásos File helyett inkább InputStream-et adok át az AdatInput-nak. 
    Sajnos File használata esetén a JAR file elszáll az alábbi hibakóddal: java.lang.IllegalArgumentException: URI is not hierarchical.
    InputStream használatával már elindul a JAR, de a következő getResource-nál (Kép file olvasás) ismét elszáll.
    Ezt majd vizsgaidőszak után próbálom javítani, hogy EXE file-t készítsek a projectből :)
    */
    private void adatBeolvasas() {
        try {

//            File BoxFile = new File(this.getClass().getResource("/media/boxok.txt").toURI());
//            File PalyaFile = new File(this.getClass().getResource("/media/palya.txt").toURI());
//            File CsoFile = new File(this.getClass().getResource("/media/csovek.txt").toURI());
//            File LepcsoFile = new File(this.getClass().getResource("/media/lepcso.txt").toURI());
//            File EllensegFile = new File(this.getClass().getResource("/media/ellensegek.txt").toURI());
            InputStream BoxFile = this.getClass().getResourceAsStream("/media/boxok.txt");
            InputStream PalyaFile = this.getClass().getResourceAsStream("/media/palya.txt");
            InputStream CsoFile = this.getClass().getResourceAsStream("/media/csovek.txt");
            InputStream LepcsoFile = this.getClass().getResourceAsStream("/media/lepcso.txt");
            InputStream EllensegFile = this.getClass().getResourceAsStream("/media/ellensegek.txt");

            AdatInput adatInput = new FileInput(BoxFile, PalyaFile, CsoFile, LepcsoFile, EllensegFile);
            boxok = adatInput.BoxListaba();
            palyaLista = adatInput.palyaListaba();
            csovek = adatInput.csoListaba();
            lepcso = adatInput.lepcsoListaba();
            ellensegek = adatInput.ellensegListaba();
        } catch (Exception ex) {
            Logger.getLogger(JatekPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    

    
    public int getHatterX() {
        return hatterX;
    }
    
    public void setHatterX(int lepes) {
        this.hatterX -= lepes;
    }

    public void setHatterXNULL() {
        this.hatterX = 0;
    }

    public void setVezerlo(Vezerlo vezerlo) {
        this.vezerlo = vezerlo;
    }

    public void ermetSzerzett(int erme) {
        this.erme=erme;
    }
}
