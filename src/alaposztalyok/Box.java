/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.Graphics;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import vezerles.Vezerlo;

/**
 *
 * @author thean
 */
public class Box extends Thread{
    
    private Image kep;
    private Image ermeKep;
    private int kepX, kepY, ermeKepY;
    private int kepSzelesseg, kepMagassag;
    private int tipus;
    private long ido;
    
    private Image EREDETI_KEP;
    private int EREDETI_KEPX, EREDETI_KEPY;

    public Box(Image kep, Image ermeKep, int kepX, int kepY, int kepSzelesseg, int kepMagassag, int tipus, long ido) {
        this.kep = kep;
        this.ermeKep = ermeKep;
        this.EREDETI_KEP=kep;
        this.kepX = kepX;
        this.kepY = kepY;
        this.ermeKepY=kepY;
        this.EREDETI_KEPX = kepX;
        this.EREDETI_KEPY = kepY;
        this.kepSzelesseg = kepSzelesseg;
        this.kepMagassag = kepMagassag;
        this.tipus = tipus;
        this.ido = ido;
    }

    public void eredetiVisszaallitas() {
        this.kep=EREDETI_KEP;
        this.kepX = EREDETI_KEPX;
        this.kepY = EREDETI_KEPY;
        megFejeltek=false;
        ermeFel=false;
        ermeKiadas=false;
    }
   
    

    private int fejeltKepY;
    private Vezerlo vezerlo;
    
    private int eredetiKepY=kepY;
    private boolean megFejeltek;
    private boolean aktiv;
    
    private boolean felment=false;
    private boolean ermeFel=false, ermeKiadas=false;
    private int ermeMagassag;

    public void beallitas(int fejeltKepY, int ermeMagassag, Vezerlo vezerlo) {
        this.ermeMagassag = kepY-ermeMagassag;
        this.fejeltKepY = kepY-fejeltKepY;
        this.vezerlo = vezerlo;
    }
    
    @Override
    public void run() {
            megFejeltek=true;
            aktiv=true;
            eredetiKepY=kepY;
            while (aktiv) {
                mozdul(); 
                pihen();    
                frissit();  
        }
    }
    
    private synchronized void mozdul() {

        if (megFejeltek) {
            if (tipus == 1) {
                ermeKiadas = true;
                if (!ermeFel && ermeKepY > ermeMagassag) {
                    ermeKepY--;
                } else {
                    ermeFel = true;
                }
                
                if (ermeFel && ermeKepY < kepY) {
                    ermeKepY++;
                } else if(ermeKepY==kepY){
                    vezerlo.ermetSzerzett();
                    ermeKiadas=false;
                }
                
            }
            if (!felment && kepY >= fejeltKepY) {
                kepY--;
            } else if (kepY != eredetiKepY) {
                felment = true;
                kepY++;
            } else if (tipus == 1 || tipus==2) {
                kep = vezerlo.boxKepetKer();
                varakozik();
            } else {
                varakozik();
            }
        }

    }

    private synchronized void varakozik() {
        try {
            if (ermeKiadas == false) {
                wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void pihen() {
        try {
            Thread.sleep(ido);
        } catch (InterruptedException ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void frissit() {
        vezerlo.frissit();
    }

    public synchronized void ebresztes() {
        felment=false;         
        notifyAll();
    }

    public void megfejeltek() {
        megFejeltek = true;
    }
    
    public void rajzol(Graphics g) {
        if (ermeKiadas) {
            g.drawImage(ermeKep, kepX, ermeKepY, kepSzelesseg, kepMagassag, null);
        }
        g.drawImage(kep, kepX, kepY, kepSzelesseg, kepMagassag, null);
    }
    public void setKepX(int kepX) {
        this.kepX -= kepX;
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

    public boolean isMegFejeltek() {
        return megFejeltek;
    }

}
