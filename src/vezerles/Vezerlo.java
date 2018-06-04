/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vezerles;

import alaposztalyok.Box;
import alaposztalyok.Cso;
import alaposztalyok.Ellenseg;
import alaposztalyok.Global;
import alaposztalyok.Lepcso;
import alaposztalyok.Mario;
import alaposztalyok.PalyaElem;
import feluletek.JatekPanel;
import feluletek.KijelzoPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

/**
 *
 * @author thean
 */
public class Vezerlo implements Runnable{

    private JatekPanel jatekPanel;
    private KijelzoPanel kijelzoPanel;
    private Mario mario;
    private boolean bal = false;
    
    
    private int VesztettElet = Global.KEZDO_ELET;
    private List<Box> boxok = new CopyOnWriteArrayList<>();
    private List<PalyaElem> palyaLista = new CopyOnWriteArrayList<>();
    private List<Cso> csovek = new CopyOnWriteArrayList<>();
    private List<Lepcso> lepcsoLista = new CopyOnWriteArrayList<>();
    private List<Ellenseg> ellensegek = new CopyOnWriteArrayList<>();
    
    private boolean ugrasPillanat = true;
    private boolean rajzolas = true;
    private boolean aktiv=true;
    private Clip clip;
    
    private PalyaElem ezAPalya;
    private Ellenseg ezAzEllenseg;
      

    public Vezerlo(JatekPanel jatekPanel, KijelzoPanel kijelzoPanel) {
        this.jatekPanel = jatekPanel;
        this.kijelzoPanel = kijelzoPanel;
    }

    public void frissit() {
        jatekPanel.repaint();
    }

    /**
    MainFrame Billentyű lenyomás figyelőjéből W gombnyomásra indított metódus.
    W gombnyomás hatására Márió hangeffekt kíséretében felugrik. 
    Ugrás hatására álló helyzeti képet leváltja az ugró kép. Sajnos Márió képkezelésénél van egy kis hiba, néha beragad egy-egy kép.
    */
    public void ugras(boolean b) {
        if (mario != null) {
            Image kep;
            if (bal) {
                kep = new ImageIcon(this.getClass().getResource("/media/ugroBalMarioPici.png")).getImage();
            } else {
                kep = new ImageIcon(this.getClass().getResource("/media/ugroJobbMarioPici.png")).getImage();
            }
            mario.setUgras(b);
            if (ugrasPillanat) {
                mario.setUgroMagassag(Global.UGRAS);
                effektJatszas("jump");
            }
            mario.setKep(kep);
        }
    }

    /**
    Kezdőképernyőn lenyomott egérgombnyomásra létrehozzuk Máriót, illetve az ellenségeket.
    Indulás pillanatában a szokásos zene kíséri játékmenetünket.
    */
    public void indul(List<Box> boxok, List<PalyaElem> palyaLista, List<Cso> csovek, List<Lepcso> lepcso, List<Ellenseg>ellensegek) {
        this.boxok = boxok;
        this.palyaLista = palyaLista;
        this.csovek = csovek;
        this.lepcsoLista = lepcso;
        this.ellensegek = ellensegek;

        Image kep = new ImageIcon(this.getClass().getResource("/media/alloJobbMarioPici.png")).getImage();
        int kepX = Global.MARIO_KEZDOX, kepY = Global.MARIO_KEZDOY;
        int kepSzelesseg = Global.MARIO_PICI_KEPSZELESSEG, kepMagassag = Global.MARIO_PICI_KEPMAGASSAG;
        int elet = Global.KEZDO_ELET - VesztettElet;
        int lepes = Global.LEPES;
        long ido = Global.IDO;
        
        for (Ellenseg ellenseg : ellensegek) {
            ellenseg.setVezerlo(this);
            ellenseg.beallitas(this, palyaLista, jatekPanel.getHeight());
            ellenseg.start();
        }

        mario = new Mario(kep, kepX, kepY, kepSzelesseg, kepMagassag, elet, lepes, ido, this);
        mario.beallitas(jatekPanel.getWidth(), jatekPanel.getHeight(), Global.MOZGAS_TURES, boxok, palyaLista, csovek, lepcso);
        mario.start();
        Thread szal = new Thread(this);
        zeneJatszas(true);
        szal.start();
    }

    public void rajzol(Graphics g) {
        if (rajzolas) {
            if(ellensegek!=null){
                for (Ellenseg ellenseg : ellensegek) {
                    ellenseg.rajzol(g);
                }
            }
            
            if (boxok != null) {
                for (Box box : boxok) {
                    box.rajzol(g);
                }
            }
            if (palyaLista != null) {
                for (PalyaElem palya : palyaLista) {
                    palya.rajzol(g);
                }
            }

            if (csovek != null) {
                for (Cso cso : csovek) {
                    cso.rajzol(g);
                }
            }
            if (lepcsoLista != null) {
                for (Lepcso lepcso : lepcsoLista) {
                    lepcso.rajzol(g);
                }
            }
            if (mario != null) {
                mario.rajzol(g);
            }
        }
    }

    /**
    Márió a képernyő közepéig tud futni. Utána a háttérkép mozog a pályaelemekkel és az ellenségekkel együtt
    Ha Márió elérte a célzászlót, akkor lelassul, majd besétál a várba.
    */
    public void kepernyoIgazitas() {
        if (jatekPanel.getHatterX() - jatekPanel.getWidth() > -Global.HATTER_SZELESSEG) {
            jatekPanel.setHatterX(Global.LEPES);
            for (Box box : boxok) {
                box.setKepX(Global.LEPES);
            }
            for (PalyaElem palya : palyaLista) {
                palya.setKepX(Global.LEPES);
            }
            for (Cso cso : csovek) {
                cso.setKepX(Global.LEPES);
            }
            for (Lepcso lepcso : lepcsoLista) {
                lepcso.setKepX(Global.LEPES);
            }
            for (Ellenseg ellenseg : ellensegek) {
                ellenseg.setKepX(Global.LEPES);
            }
        }

        if(-jatekPanel.getHatterX()+mario.getKepX()>Global.CEL_ZASZLO){
            aktiv=false;
            zeneJatszas(false);
            effektJatszas("complete");
            int cel=Global.CEL_KAPU+jatekPanel.getHatterX();
            Image kep = new ImageIcon(this.getClass().getResource("/media/jobbMarioPici.gif")).getImage();
            mario.setKep(kep);
            mario.celbaMegy(cel, Global.CEL_IDO);
        }
    }

    /**
    Márió földet érését követően kér egy képet, a metódus pedig visszaadja a megfelelő oldali képet.
    */
    public Image kepetKer() {
        Image kep;
        if (bal) {
            kep = new ImageIcon(this.getClass().getResource("/media/alloBalMarioPici.png")).getImage();
        } else {
            kep = new ImageIcon(this.getClass().getResource("/media/alloJobbMarioPici.png")).getImage();
        }
        return kep;
    }

    /*
    MainFrame Billentyű lenyomás figyelőjéből D gombnyomásra indított metódus.
    D gombnyomás hatására Márió elindul jobbra, a gomb felengedéséig jobbra megy.
    Álló helyzeti képet leváltja a futó kép. 
    Gomb felengedésekor visszavált álló helyzeti képre.
    */
    void jobbraMegy(boolean b) {
        if (mario != null && aktiv) {
            
            Image kep;
            if (b == true) {
                kep = new ImageIcon(this.getClass().getResource("/media/jobbMarioPici.gif")).getImage();
                mario.setKep(kep);
            } else {
                kep = new ImageIcon(this.getClass().getResource("/media/alloJobbMarioPici.png")).getImage();
                mario.setKep(kep);
            }
            bal = false;
            mario.setJobbraMegy(b);
        }
    }

    /*
    Hasonló a jobbraMegy metódussal, csak ebben a metódusban balra megy.
    */
    void balraMegy(boolean b) {
        if (mario != null && aktiv) {
            Image kep;
            if (b == true) {
                kep = new ImageIcon(this.getClass().getResource("/media/balMarioPici.gif")).getImage();
                mario.setKep(kep);
            } else {
                kep = new ImageIcon(this.getClass().getResource("/media/alloBalMarioPici.png")).getImage();
                mario.setKep(kep);
            }
            bal = true;
            mario.setBalraMegy(b);

        }
    }

    /**
    Amikor Márió megfejel egy Box-ot, akkor, ha még előtte nem lett megfejelve beállítjuk, majd elindítjuk a box szálat.
    Hatására a box felugrik, ha kérdőjeles box, akkor kiesik belőle egy érme. Majd a szál várakozik egy esetleges következő megfejelésre.
    */
    public void fejeles(Box box) {
        if (!box.isAlive()) {
            box.beallitas(Global.BOX_FEJELT_MAGASSAG, Global.BOX_ERME_MAGASSAG,  this);
            box.start();
        } else if (box.getTipus() == 0) {
            box.ebresztes();
            box.megfejeltek();
        } else {
            box.megfejeltek();
        }
    }

    /**
    Ha kérdőjeles volt a megfejelt box, akkor az érme kiesése után le kell váltani a box-nak a képét.
    */
    public Image boxKepetKer() {
        Image kep = new ImageIcon(this.getClass().getResource("/media/boxKi.png")).getImage();
        return kep;
    }

    /*
    Ha Márió életet veszt, akkor leáll a játék zenéje, a hagyományos halál effektet hallhatjuk helyette.
    Elveszünk Máriótól egy életet, a JatekPanelt pedig a KijelzoPanel váltja, ahol Márió jelenlegi életét lehet látni.
    */
    public void eletVesztes() {
        if(aktiv){
        zeneJatszas(false);
        effektJatszas("die");
        VesztettElet--;
        rajzolas = false;
        jatekPanel.setVisible(false);
        kijelzoPanel.setElet(VesztettElet);
        kijelzoPanel.setVisible(true);
        }
    }

    /**
    Életvesztés után, van lehetőségünk újrakezdeni a játékot.
    Ebben az esetben minden játék elemet az eredeti helyére teszünk vissza.
    Máriót visszahozzuk a halálból, természetesen egyel kevesebb élettel. Az elvesztett élet mellet sajnos az eddig összegyűjtött érméi is elvesznek.
    */
    public void panelValtas() {
        for (Box box : boxok) {
            box.ebresztes();
            box.eredetiVisszaallitas();
        }
        for (PalyaElem palya : palyaLista) {
            palya.eredetiVisszaallitas();
        }
        for (Cso cso : csovek) {
            cso.eredetiVisszaallitas();
        }
        for (Lepcso lepcso : lepcsoLista) {
            lepcso.eredetiVisszaallitas();
        }
        for (Ellenseg ellenseg : ellensegek) {
                ellenseg.eredetiVisszaallitas();
            }
        jatekPanel.setHatterXNULL();
        mario.ujraelesztes(Global.MARIO_KEZDOX, Global.MARIO_KEZDOY, VesztettElet, boxok, palyaLista, csovek, lepcsoLista);
        mario.setErmeNULL();
        zeneJatszas(true);
        jatekPanel.ermetSzerzett(mario.getErme());
        mario.ebresztes();
        kijelzoPanel.setVisible(false);
        jatekPanel.setVisible(true);
        rajzolas = true;
        
    }

    @Override
    public void run() {
        while(aktiv){
        jobbraMegyEllenorzes();
        balraMegyEllenorzes();
        fejelesEllenorzes();
        esesEllenorzes();
        raUgrasEllenorzes();
        ellensegJobbraMegyEllenorzes();
        ellensegBalraMegyEllenorzes();
        ellensegAktivalas();
        }
        
        
    }

    /**
    Amikor Márió jobbra megy, akkor ez a metódus figyeli, hogy beleütközik-e valamibe a következő lépésénél.
    Ha esetleg ellenségbe ütközne, akkor az sajnos egy életvesztéssel jár.
    Ha csak az egyik pálya elembe, akkor nem mehet tovább.
    */
    private void jobbraMegyEllenorzes() {
        for (Ellenseg ellenseg : ellensegek) {
            if(mario.getKepX()+mario.getKepSzelesseg()+mario.getLepes()>=ellenseg.getKepX()  && mario.getKepX()<=ellenseg.getKepX()+ellenseg.getKepSzelesseg()
                    && mario.getKepY()+mario.getKepMagassag()>=ellenseg.getKepY()+Global.MOZGAS_TURES && mario.getKepY()<=ellenseg.getKepY()+ellenseg.getKepMagassag()){
            mario.eletVesztes();
            }
        }
        
        
        for (Box box : boxok) {
            if(box.getTipus()!=2 && mario.getKepX()+mario.getKepSzelesseg()+mario.getLepes()>=box.getKepX()  && mario.getKepX()<=box.getKepX()+box.getKepSzelesseg()
                    && mario.getKepY()+mario.getKepMagassag()>=box.getKepY()+ Global.MOZGAS_TURES && mario.getKepY()<=box.getKepY()+box.getKepMagassag()){
            mario.setJobbraMegy(false);
            }
        }
        
        for (Cso cso : csovek) {
            if(mario.getKepX()+mario.getKepSzelesseg()+mario.getLepes()>=cso.getKepX()  && mario.getKepX()<=cso.getKepX()+cso.getKepSzelesseg()
                    && mario.getKepY()+mario.getKepMagassag()>=cso.getKepY()+Global.MOZGAS_TURES && mario.getKepY()<=cso.getKepY()+cso.getKepMagassag()){
            mario.setJobbraMegy(false);
            }
        }
        
        for (Lepcso lepcso : lepcsoLista) {
            if(mario.getKepX()+mario.getKepSzelesseg()+mario.getLepes()>=lepcso.getKepX()  && mario.getKepX()<=lepcso.getKepX()+lepcso.getKepSzelesseg()
                    && mario.getKepY()+mario.getKepMagassag()>=lepcso.getKepY()+Global.MOZGAS_TURES && mario.getKepY()<=lepcso.getKepY()+lepcso.getKepMagassag()){
            mario.setJobbraMegy(false);
            }
        }
        
        for (PalyaElem palya : palyaLista){
            if(mario.getKepX()+mario.getKepSzelesseg()+mario.getLepes()>=palya.getKepX()
                    && mario.getKepY()+mario.getKepMagassag()>=palya.getKepY()+Global.MOZGAS_TURES && mario.getKepY()<=palya.getKepY()+palya.getKepMagassag()){
            mario.setJobbraMegy(false);
            }
        }
    }

    /**
    A jobbraMegyEllenorzes metódushoz hasonló, csak a másik irányba.
    */
    private void balraMegyEllenorzes() {
        for (Ellenseg ellenseg : ellensegek) {
            if (mario.getKepX() - mario.getLepes() <= ellenseg.getKepX() + ellenseg.getKepSzelesseg() && mario.getKepX() >= ellenseg.getKepX() + ellenseg.getKepSzelesseg()
                    && mario.getKepY() + mario.getKepMagassag() >= ellenseg.getKepY() + Global.MOZGAS_TURES && mario.getKepY() <= ellenseg.getKepY() + ellenseg.getKepMagassag()) {
                mario.eletVesztes();
            }
        }
        
        for (Box box : boxok) {
            if (box.getTipus() != 2 && mario.getKepX() - mario.getLepes() <= box.getKepX() + box.getKepSzelesseg() && mario.getKepX() >= box.getKepX() + box.getKepSzelesseg()
                    && mario.getKepY() + mario.getKepMagassag() >= box.getKepY() + Global.MOZGAS_TURES && mario.getKepY() <= box.getKepY() + box.getKepMagassag()) {
                mario.setBalraMegy(false);
            }
        }

        for (Cso cso : csovek) {
            if (mario.getKepX() - mario.getLepes() <= cso.getKepX() + cso.getKepSzelesseg() && mario.getKepX() >= cso.getKepX() + cso.getKepSzelesseg()
                    && mario.getKepY() + mario.getKepMagassag() >= cso.getKepY() + Global.MOZGAS_TURES && mario.getKepY() <= cso.getKepY() + cso.getKepMagassag()) {
                mario.setBalraMegy(false);
            }
        }

        for (Lepcso lepcso : lepcsoLista) {
            if (mario.getKepX() - mario.getLepes() <= lepcso.getKepX() + lepcso.getKepSzelesseg() && mario.getKepX() >= lepcso.getKepX() + lepcso.getKepSzelesseg()
                    && mario.getKepY() + mario.getKepMagassag() >= lepcso.getKepY() + Global.MOZGAS_TURES && mario.getKepY() <= lepcso.getKepY() + lepcso.getKepMagassag()) {
                mario.setBalraMegy(false);
            }
        }
    }

    /**
    Sajnos a jobbra/balraMegyEllenorzes metódusban használt ellenőrzés nem működik esésnél.
    Ezért Márió osztályon belül ellenőrzöm  az esést.
    */
    private void esesEllenorzes() {
//        for (PalyaElem palya : palyaLista) {
//            int palyaMagassag = palya.getKepY() - mario.getKepMagassag();
//            mario.setPalyaMagassag(palyaMagassag);
//            if (palya.getKepX() < mario.getKepX() + Global.MOZGAS_TURES && palya.getKepX() + palya.getKepSzelesseg() > mario.getKepX()+Global.MOZGAS_TURES) {
//                mario.setPalyaEses(false);
//            } else if (palya.getKepX() < mario.getKepX() +Global.MOZGAS_TURES && palya.getKepX() + palya.getKepSzelesseg() < mario.getKepX()+Global.MOZGAS_TURES){
//                mario.setPalyaEses(true);
//            }
//        }
//       
//        for (Box box : boxok) {
//            if (box.getTipus()!=2 && mario.getKepY() + mario.getKepMagassag() == box.getKepY() && box.getKepX() - box.getKepSzelesseg() / 2 < mario.getKepX() && box.getKepX() + box.getKepSzelesseg() > mario.getKepX()) {
//                int boxMagassag = box.getKepY()-mario.getKepMagassag(); 
//                mario.setBoxMagassag(boxMagassag);
//                mario.setEsik(false);
//                ezABox = box;
//            }
//            if (ezABox != null && !mario.isUgras()) {
//                if (ezABox.getKepX() > mario.getKepX()+mario.getKepSzelesseg() || ezABox.getKepX() + ezABox.getKepSzelesseg() < mario.getKepX()) {
//                    mario.setEsik(true);
//                    ezABox=null;
//                    mario.setBoxMagassag(0);
//                }
//            }
//        }
//        
//        for (Cso cso : csovek) {
//            if (mario.getKepY() + mario.getKepMagassag() == cso.getKepY() && cso.getKepX() - cso.getKepSzelesseg() / 2 < mario.getKepX() && cso.getKepX() + cso.getKepSzelesseg() > mario.getKepX()) {
//                int csoMagassag = cso.getKepY()-mario.getKepMagassag(); 
//                mario.setCsoMagassag(csoMagassag);
//                mario.setEsik(false);
//                ezACso = cso;
//            }
//            
//            if (ezACso != null && !mario.isUgras()) {
//                if (ezACso.getKepX() > mario.getKepX()+mario.getKepSzelesseg() || ezACso.getKepX() + ezACso.getKepSzelesseg() < mario.getKepX()) {
//                    mario.setEsik(true);;System.out.println("cso");
//                    ezACso=null;
//                    mario.setCsoMagassag(0);
//                }
//            }
//        }
//        
//        for (Lepcso lepcso : lepcsoLista) {
//            if (mario.getKepY() + mario.getKepMagassag() == lepcso.getKepY() && lepcso.getKepX() - lepcso.getKepSzelesseg() / 2 < mario.getKepX() && lepcso.getKepX() + lepcso.getKepSzelesseg() > mario.getKepX()) {
//                int lepcsoMagassag = lepcso.getKepY()-mario.getKepMagassag(); 
//                mario.setLepcsoMagassag(lepcsoMagassag);
//                mario.setEsik(false);
//                ezALepcso = lepcso;
//            }
//            if (ezALepcso != null  && !mario.isUgras()) {
//                if (ezALepcso.getKepX() > mario.getKepX()+mario.getKepSzelesseg() || ezALepcso.getKepX() + ezALepcso.getKepSzelesseg() < mario.getKepX()) {
//                    mario.setEsik(true);
//                    ezALepcso=null;
//                    mario.setLepcsoMagassag(0);
//                }
//            }
//        }
    }

    /**
    Ez a metódus ellenőrzi, hogy Márió belefejel-e egy box-ba.
    Ha belefejel, akkor meghívjuk a fejeles() metódust, Márió visszaesik a földre, illetve ha téglába fejel, akkor lejátsszuk  annak az effektjét.
    */
    private void fejelesEllenorzes() {
        for (Box box : boxok) {
                if (box.getKepY() + box.getKepMagassag() == mario.getKepY() && box.getKepX() - mario.getKepSzelesseg() < mario.getKepX() && box.getKepX() + box.getKepSzelesseg() + mario.getKepSzelesseg() > mario.getKepX() + mario.getKepSzelesseg()) {
                    fejeles(box);
                    mario.setFejeles(true);
                    if(box.getTipus()!=1){
                    effektJatszas("bump");
                    }
                }
            }
    }

    /**
    Ha Márió ráugrik egy ellenségre, akkor meg tudja ölni. De csak abban az esetben, ha az adott ellenség egy Gomba.
    Ha Gomba, akkor eltűnik a pályáról egy hangeffekttel kísérve.
    */
    private void raUgrasEllenorzes() {
        for (Ellenseg ellenseg : ellensegek) {
            if (ellenseg.getTipus() == 2 && mario.getKepY() + mario.getKepMagassag() == ellenseg.getKepY()
                    && ellenseg.getKepX() - ellenseg.getKepSzelesseg() / 2 < mario.getKepX()
                    && ellenseg.getKepX() + ellenseg.getKepSzelesseg() > mario.getKepX()) {

                ellenseg.eltalaltak();
                effektJatszas("stomp");

            }
        }
    }

    /**
    Hasonló a jobbraMegyEllenorzéshez. Ebben a metódusban az ellenséget vizsgáljuk, hogy nem ütközik e bele a következő lépéssel egy pálya elembe.
    */
    private void ellensegJobbraMegyEllenorzes() {
        for (Ellenseg ellenseg : ellensegek) {

            for (Cso cso : csovek) {
                if (ellenseg.getKepX() + ellenseg.getKepSzelesseg() + ellenseg.getLepes() >= cso.getKepX() && ellenseg.getKepX() + ellenseg.getKepSzelesseg() == cso.getKepX()) {
                    ellenseg.setJobbraMegy(false);
                    ellenseg.setBalraMegy(true);
                }
            }

            for (Lepcso lepcso : lepcsoLista) {
                if (ellenseg.getKepX() + ellenseg.getKepSzelesseg() + ellenseg.getLepes() >= lepcso.getKepX() && ellenseg.getKepX() <= lepcso.getKepX() + lepcso.getKepSzelesseg()) {
                    ellenseg.setJobbraMegy(false);
                    ellenseg.setBalraMegy(true);
                }
            }
        }

    }

    /**
    Hasonló, mint az ellensegJobbraMegyEllenorzes, csak a másik irányba ellenőriz.
    */
    private void ellensegBalraMegyEllenorzes() {
        for (Ellenseg ellenseg : ellensegek) {

            for (Cso cso : csovek) {
                if (ellenseg.getKepX() - ellenseg.getLepes() == cso.getKepX() + cso.getKepSzelesseg() && ellenseg.getKepX() >= cso.getKepX() + cso.getKepSzelesseg()) {
                    ellenseg.setBalraMegy(false);
                    ellenseg.setJobbraMegy(true);
                }
            }
        }
    }

    private void ellensegAktivalas() {
        boolean aktivalas = false;
        for (Ellenseg ellenseg : ellensegek) {
            if (mario.getKepX() + mario.getLepes() > ellenseg.getKepX()) {
                ellenseg.setAktiv(true);
                if (ellenseg.getTipus() == 6) {
                    aktivalas = true;
                }
            }

            if (ellenseg.getTipus() == 4) {
                if (-jatekPanel.getHatterX() + mario.getKepX() + mario.getLepes() > Global.ELLENSEG_AKTIVALO_X && mario.getKepY() <= ellenseg.getKepY()) {
                    ellenseg.setAktiv(true);
                }
            }
        }

        for (Ellenseg ellenseg : ellensegek) {
            if (aktivalas && ellenseg.getTipus() == 6) {
                ellenseg.setAktiv(true);
            }
        }

    }
    
    

    /**
    Ha Máriónak sikerült egy érmet kifejelnie az egyik Box-ból, akkor az az érme hozzáadódik a gyűjtött érméihez.
    Ezt az érme a fenti fejlécen is látható.
    */
    public void ermetSzerzett() {
        mario.ermetSzerzett();
        effektJatszas("coin");
        jatekPanel.ermetSzerzett(mario.getErme());
    }
    
    /**
    Különböző események által kiváltott effektet játszik le ez a metódus.
    */
    public synchronized void effektJatszas(String effektNev){
       
        try {
            AudioInputStream effekt=null;
            switch(effektNev){
                case "die": effekt = AudioSystem.getAudioInputStream(this.getClass().getResource("/media/die.wav")); break;
                case "coin": effekt = AudioSystem.getAudioInputStream(this.getClass().getResource("/media/coin.wav"));break;
                case "complete": effekt = AudioSystem.getAudioInputStream(this.getClass().getResource("/media/complete.wav"));break;
                case "bump": effekt = AudioSystem.getAudioInputStream(this.getClass().getResource("/media/bump.wav"));break;
                case "jump": effekt = AudioSystem.getAudioInputStream(this.getClass().getResource("/media/jump.wav"));break;
                case "stomp": effekt = AudioSystem.getAudioInputStream(this.getClass().getResource("/media/stomp.wav"));break;
            }
            
                Clip clip = AudioSystem.getClip();
                clip.open(effekt);
                clip.start();
            } catch (Exception ex) {
                Logger.getLogger(Vezerlo.class.getName()).log(Level.SEVERE, null, ex);
            }
           
    }

    /**
    Játék kezdésekor elindított, hagyományos Márió zene. Ez a zene Márió haláláig, vagy célba érkezéséig kísér minket.
    */
    public void zeneJatszas(boolean b) {

        try {
            
            if (b) {
                if (clip != null) {
                    clip.close();
                }
                clip = AudioSystem.getClip();
                AudioInputStream main = AudioSystem.getAudioInputStream(this.getClass().getResource("/media/main.wav"));
                clip.open(main);
                clip.start();
            } else {
                clip.stop();
            }
        } catch (Exception ex) {
            Logger.getLogger(Vezerlo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
    Ha Márió beért a célba, akkor eltűnik.
    */
    public void vege() {
        mario=null;
    }
    
    public void setUgrasPillanat(boolean ugrasPillanat) {
        this.ugrasPillanat = ugrasPillanat;
    }
    
    
}
