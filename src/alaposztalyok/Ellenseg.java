/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import vezerles.Vezerlo;

/**
 *
 * @author thean
 */
public class Ellenseg extends Thread{
    
    
    private Image kep;
    private int kepX, kepY;
    private int kepSzelesseg, kepMagassag;
    private int tipus, lepes;
    private long ido;

    private int EREDETI_KEPX, EREDETI_KEPY;
    
    public Ellenseg(Image kep, int kepX, int kepY, int kepSzelesseg, int kepMagassag, int tipus, int lepes, long ido) {
        this.kep = kep;
        this.kepX = kepX;
        this.kepY = kepY;
        this.EREDETI_KEPX = kepX;
        this.EREDETI_KEPY = kepY;
        this.kepSzelesseg = kepSzelesseg;
        this.kepMagassag = kepMagassag;
        this.tipus = tipus;
        this.lepes = lepes;
        this.ido = ido;
    }
    
    private Vezerlo vezerlo;
    private List<PalyaElem> palyaLista = new CopyOnWriteArrayList<>();
    private int panelMagassag;

    public void beallitas(Vezerlo vezerlo, List<PalyaElem> palyaLista, int panelMagassag) {
        this.vezerlo = vezerlo;
        this.palyaLista = palyaLista;
        this.panelMagassag = panelMagassag;
    }
    
   public void ujraelesztes(List<PalyaElem> palyaLista) {
        this.kepX = EREDETI_KEPX;
        this.kepY = EREDETI_KEPY;
        this.palyaLista = palyaLista;
    }
    
    private boolean aktiv=false;
    private boolean balraMegy = true, jobbraMegy=false;
    private PalyaElem ezAPalya;
    
    @Override
    public void run(){
        while(true){
        mozdul();
        pihen();
        frissit();
        }
    }
    
    
    private void mozdul() {
        
        if (tipus == 2 || tipus == 3) {
            balraMegy();
            jobbraMegy();
        }
        
        if(tipus==4 &&aktiv){
            jobbraRepul();
        }

        if (tipus == 6 && aktiv) {
            leEsik();
        }
        
        if (tipus == 0 && aktiv || tipus == 1 && aktiv) {
            felMegy();
        }
    }

    private void balraMegy() {
        if (balraMegy) {

            for (PalyaElem palya : palyaLista) {
                if (palya.getKepX() < getKepX() && palya.getKepX() + palya.getKepSzelesseg() > getKepX()) {
                    ezAPalya = palya;
                }
            }

            if (ezAPalya != null && getKepX() - getLepes() < ezAPalya.getKepX()) {
                setBalraMegy(false);
                setJobbraMegy(true);
            }

            kepX--;
        }
    }

    private void jobbraMegy() {
        if (jobbraMegy) {

            for (PalyaElem palya : palyaLista) {
                if (palya.getKepX() < getKepX() && palya.getKepX() + palya.getKepSzelesseg() > getKepX()) {
                    ezAPalya = palya;

                }

            }
            if (ezAPalya != null && getKepX() + getKepSzelesseg() + getLepes() > ezAPalya.getKepX() + ezAPalya.getKepSzelesseg()) {
                setJobbraMegy(false);
                setBalraMegy(true);
            }

            kepX++;
        }
    }
    
    private void leEsik() {
        if(kepY < panelMagassag){kepY++;}
    }
    
    private void felMegy() {
        if(tipus==0 && kepY > -kepMagassag){kepY--;}
        if(tipus==1 && kepY > EREDETI_KEPY-kepMagassag){kepY--;}
    }

    public void eltalaltak() {
        kepY=750;
    }
    
    
    private void pihen() {
        try {
            Thread.sleep(ido);
        } catch (InterruptedException ex) {
            Logger.getLogger(Ellenseg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void frissit() {
        vezerlo.frissit();
    }
    
    public void eredetiVisszaallitas() {
        this.kepX = EREDETI_KEPX;
        this.kepY = EREDETI_KEPY;
        balraMegy=true;
        jobbraMegy=false;
        aktiv=false;
    }

    public void setVezerlo(Vezerlo vezerlo) {
        this.vezerlo = vezerlo;
    }
    
    public void setKepX(int kepX) {
        this.kepX -= kepX;
    }

    public void setBalraMegy(boolean balraMegy) {
        this.balraMegy = balraMegy;
    }

    public void setJobbraMegy(boolean jobbraMegy) {
        this.jobbraMegy = jobbraMegy;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }
    
    public void rajzol(Graphics g) {
        if (tipus == 6 || tipus == 7 || tipus==4) {
            if (aktiv) {
                g.drawImage(kep, kepX, kepY, kepSzelesseg, kepMagassag, null);
            }
        } else {
            g.drawImage(kep, kepX, kepY, kepSzelesseg, kepMagassag, null);
        }
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

    public int getLepes() {
        return lepes;
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

    private void jobbraRepul() {
        kepX--;
    }

}
