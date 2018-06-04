/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author thean
 */
public class PalyaElem {
    
    private int kepX, kepY;
    private int kepSzelesseg, kepMagassag;

    private int EREDETI_KEPX, EREDETI_KEPY;
    
    public PalyaElem(int kepX, int kepY, int kepSzelesseg, int kepMagassag) {
        this.kepX = kepX;
        this.kepY = kepY;
        this.EREDETI_KEPX = kepX;
        this.EREDETI_KEPY = kepY;
        this.kepSzelesseg = kepSzelesseg;
        this.kepMagassag = kepMagassag;
    }
    
    public void eredetiVisszaallitas() {
        this.kepX = EREDETI_KEPX;
        this.kepY = EREDETI_KEPY;
    }

    public void rajzol(Graphics g) {
        Color color = new Color(00,00,00,.0f);
        g.setColor(color);
        g.drawRect(kepX, kepY, kepSzelesseg, kepMagassag);
    }

    public void setKepX(int kepX) {
        this.kepX -= kepX;
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
    
    
    
    
    
    
}
