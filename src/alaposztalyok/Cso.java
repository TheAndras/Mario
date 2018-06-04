/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author thean
 */
public class Cso{
    
    
    private Image kep;
    private int kepX, kepY;
    private int kepSzelesseg, kepMagassag;
    private int tipus;

    private int EREDETI_KEPX, EREDETI_KEPY;
    
    public Cso(Image kep, int kepX, int kepY, int kepSzelesseg, int kepMagassag, int tipus) {
        this.kep = kep;
        this.kepX = kepX;
        this.kepY = kepY;
        this.EREDETI_KEPX = kepX;
        this.EREDETI_KEPY = kepY;
        this.kepSzelesseg = kepSzelesseg;
        this.kepMagassag = kepMagassag;
        this.tipus = tipus;
    }
    
    public void eredetiVisszaallitas() {
        this.kepX = EREDETI_KEPX;
        this.kepY = EREDETI_KEPY;
    }
    
    public void setKepX(int kepX) {
        this.kepX -= kepX;
    }

    public void rajzol(Graphics g) {
        g.drawImage(kep, kepX, kepY, kepSzelesseg, kepMagassag, null); 
    }

    public Image getKep() {
        return kep;
    }

    public int getKepX() {
        return kepX;
    }

    public int getKepY() {
        return kepY;
    }

    public int getKepSzelesseg() {
        return kepSzelesseg;
    }

    public int getKepMagassag() {
        return kepMagassag;
    }

    public int getTipus() {
        return tipus;
    }
    
    
    
    

    
    
}
