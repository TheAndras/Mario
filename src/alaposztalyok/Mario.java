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
public class Mario extends Thread{

    private Image kep;
    private int kepX, kepY;
    private int kepSzelesseg, kepMagassag;
    private int elet;
    private int lepes;
    private long ido;
    private Vezerlo vezerlo;


    public Mario(Image kep, int kepX, int kepY, int kepSzelesseg, int kepMagassag, int elet, int lepes, long ido, Vezerlo vezerlo) {
        this.kep = kep;
        this.kepX = kepX;
        this.kepY = kepY;
        this.kepSzelesseg = kepSzelesseg;
        this.kepMagassag = kepMagassag;
        this.elet = elet;
        this.lepes = lepes;
        this.ido = ido;
        this.vezerlo = vezerlo;
    }
    
    
    private int panelSzelesseg;    
    private int panelMagassag; 
    private int mozgasTures; 
    private List<Box> boxok = new CopyOnWriteArrayList<>();
    private List<PalyaElem> palyaLista = new CopyOnWriteArrayList<>();
    private List<Cso> csovek = new CopyOnWriteArrayList<>();
    private List<Lepcso> lepcsoLista = new CopyOnWriteArrayList<>();

    public void beallitas(int panelSzelesseg, int panelMagassag, int mozgasTures, List<Box> boxok, List<PalyaElem> palyaLista, List<Cso> csovek, List<Lepcso> lepcso) {
        this.panelSzelesseg = panelSzelesseg;
        this.panelMagassag = panelMagassag;
        this.mozgasTures = mozgasTures;
        this.boxok = boxok;
        this.palyaLista = palyaLista;
        this.csovek = csovek;
        this.lepcsoLista = lepcso;
    }

    public void ujraelesztes(int kepX, int kepY, int elet, List<Box> boxok, List<PalyaElem> palyaLista, List<Cso> csovek, List<Lepcso> lepcso) {
        this.kepX = kepX;
        this.kepY = kepY;
        this.elet = elet;
        this.boxok = boxok;
        this.palyaLista = palyaLista;
        this.csovek = csovek;
        this.lepcsoLista = lepcso;
    }
    
    private boolean fut = true;

    private boolean jobbraMegy = false;
    private boolean balraMegy = false;
    private boolean celbaMegy = false;
    private boolean ugras = false;
    
    private int cel;
    private int ugroMagassag;
    private int palyaMagassag, boxMagassag, csoMagassag, lepcsoMagassag;
    private boolean felugrott=false;
    
    private boolean fejeles= false;
    private boolean esik=false;
    private boolean palyaEses = false;
    
    private Box ezABox;
    private Cso ezACso;
    private Lepcso ezALepcso;
    private int erme;


    @Override
    public void run(){
        while(fut){
        mozdul();
        pihen();
        frissit();
        }
    }
    
    private void mozdul() {
        jobbraMegy();
        balraMegy();
        ugras();
        leEsik();
    }

    private void pihen() {
        try {
            Thread.sleep(ido);
        } catch (InterruptedException ex) {
            Logger.getLogger(Mario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void frissit() {
        vezerlo.frissit();
    }

    public void rajzol(Graphics g) {
        g.drawImage(kep, kepX, kepY, kepSzelesseg, kepMagassag, null); 
    }

    private void jobbraMegy() {
        if (jobbraMegy) {
            if (kepX + kepSzelesseg + lepes >= panelSzelesseg / 2) {
                vezerlo.kepernyoIgazitas();
            } else {
                kepX += lepes;
            }
        }
        if (celbaMegy) {
            jobbraMegy = false;
            balraMegy = false;
            if (kepX <= cel) {
                kepX++;
            } else {
                vezerlo.vege();
            }
        }
    }

    private void balraMegy() {
        if (balraMegy && kepX - lepes > 0) {
            kepX -= lepes;
        }
    }
    
    private void ugras() {
        if (ugras && !fejeles && kepY >= ugroMagassag && !felugrott) {
            vezerlo.setUgrasPillanat(false);
            kepY--;
        } else {
            felugrott = true;
            esik = true;
        }
    }

    private void leEsik() {
        
        for (PalyaElem palya : palyaLista) {
            palyaMagassag = palya.getKepY() - kepMagassag;
            if (palya.getKepX() < kepX + mozgasTures && palya.getKepX() + palya.getKepSzelesseg() > kepX+mozgasTures) {
                palyaEses = false;
            } else if (palya.getKepX() < kepX +mozgasTures && palya.getKepX() + palya.getKepSzelesseg() < kepX+mozgasTures){
                palyaEses = true;
            }
        }
       
        for (Box box : boxok) {
            if (box.getTipus()!=2 &&kepY + kepMagassag == box.getKepY() && box.getKepX() - box.getKepSzelesseg() / 2 < kepX && box.getKepX() + box.getKepSzelesseg() > kepX) {
                boxMagassag = box.getKepY()-kepMagassag; 
                esik = false;
                ezABox = box;
            }
            if (ezABox != null && !ugras) {
                if (ezABox.getKepX() > kepX+kepSzelesseg || ezABox.getKepX() + ezABox.getKepSzelesseg() < kepX) {
                    esik = true;
                    ezABox=null;
                    boxMagassag=0;
                }
            }
        }
       
        
        for (Cso cso : csovek) {
            if (kepY + kepMagassag == cso.getKepY() && cso.getKepX() - cso.getKepSzelesseg() / 2 < kepX && cso.getKepX() + cso.getKepSzelesseg() > kepX) {
                csoMagassag = cso.getKepY()-kepMagassag; 
                esik = false;
                ezACso = cso;
            }
            if (ezACso != null && !ugras) {
                if (ezACso.getKepX()  > kepX+kepSzelesseg || ezACso.getKepX() + ezACso.getKepSzelesseg() < kepX) {
                    esik = true;
                    ezACso=null;
                    csoMagassag=0;
                }
            }
        }
        
        for (Lepcso lepcso : lepcsoLista) {
            if (kepY + kepMagassag == lepcso.getKepY() && lepcso.getKepX() - lepcso.getKepSzelesseg() / 2 < kepX && lepcso.getKepX() + lepcso.getKepSzelesseg() > kepX) {
                lepcsoMagassag = lepcso.getKepY()-kepMagassag; 
                esik = false;
                ezALepcso = lepcso;
            }
            if (ezALepcso != null  && !ugras) {
                if (ezALepcso.getKepX() > kepX+kepSzelesseg || ezALepcso.getKepX() + ezALepcso.getKepSzelesseg() < kepX) {
                    esik = true;
                    ezALepcso=null;
                    lepcsoMagassag=0;
                }
            }
        }
        
        if (esik && palyaEses) {
            if (kepY <= panelMagassag) {
                kepY++;
            } else {
                palyaEses=false;
                eletVesztes();
            }
        }
        
        if(kepY+kepMagassag<= boxMagassag && !celbaMegy){
        kep=vezerlo.kepetKer();
        }
        
        if(esik && !palyaEses){
            if(kepY!= palyaMagassag){
               kepY++;

            } else{
                esik = false;
                fejeles=false;
                felugrott=false;
                vezerlo.setUgrasPillanat(true);
            }
        }else if (kepY == palyaMagassag || kepY == boxMagassag || kepY == csoMagassag || kepY == lepcsoMagassag){
        //kep = vezerlo.kepetKer();
        felugrott=false;
        fejeles=false;
        vezerlo.setUgrasPillanat(true);
        }
    }

    public void eletVesztes() {
            vezerlo.eletVesztes();
            kepY=0;
            varakozik();
    }
    
    public void setKep(Image kep) {
        this.kep = kep;
    }

    public void setUgroMagassag(int ugroMagassag) {
        this.ugroMagassag = kepY-ugroMagassag;
    }
   
    public void setJobbraMegy(boolean jobbraMegy) {
        this.jobbraMegy = jobbraMegy;
    }

    public void setBalraMegy(boolean balraMegy) {
        this.balraMegy = balraMegy;
    }

    public void setUgras(boolean ugras) {
        this.ugras = ugras;
    }

    public void setPalyaEses(boolean palyaEses) {
        this.palyaEses = palyaEses;
    }
    
    public void setEsik(boolean esik) {
        this.esik = esik;
    }
    
    public void setPalyaMagassag(int palyaMagassag) {
        this.palyaMagassag = palyaMagassag;
    }

    public void setBoxMagassag(int boxMagassag) {
        this.boxMagassag = boxMagassag;
    }

    public void setCsoMagassag(int csoMagassag) {
        this.csoMagassag = csoMagassag;
    }

    public void setLepcsoMagassag(int lepcsoMagassag) {
        this.lepcsoMagassag = lepcsoMagassag;
    }

    public void setFejeles(boolean fejeles) {
        this.fejeles = fejeles;
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

    public boolean isUgras() {
        return ugras;
    }

    private synchronized void varakozik() {
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Mario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void ebresztes() {       
        notifyAll();
    }

    public void ermetSzerzett() {
        erme++;
    }

    public void setErmeNULL() {
        erme=0;
    }

    public int getErme() {
        return erme;
    }

    public void celbaMegy(int cel, long ido) {
        this.cel=cel-kepSzelesseg;
        this.ido=ido;
        celbaMegy=true;
    }
    
}
