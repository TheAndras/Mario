/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adatkezeles;

import alaposztalyok.Box;
import alaposztalyok.Cso;
import alaposztalyok.Ellenseg;
import alaposztalyok.Global;
import alaposztalyok.Lepcso;
import alaposztalyok.PalyaElem;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 *
 * @author thean
 */
public class FileInput implements AdatInput {

    private InputStream BoxFile;
    private InputStream PalyaFile;
    private InputStream CsoFile;
    private InputStream LepcsoFile;
    private InputStream EllensegFile;

    public FileInput(InputStream BoxFile, InputStream PalyaFile, InputStream CsoFile, InputStream LepcsoFile, InputStream EllensegFile) {
        this.BoxFile = BoxFile;
        this.PalyaFile = PalyaFile;
        this.CsoFile = CsoFile;
        this.LepcsoFile = LepcsoFile;
        this.EllensegFile = EllensegFile;
    }

    @Override
    public List<Box> BoxListaba() throws Exception {
        List<Box> boxok = new ArrayList<>();

        Scanner scanner = new Scanner(BoxFile);
        String sor;
        String[] adatok;
        Box box;
        Image kep = null;
        while (scanner.hasNextLine()) {
            sor = scanner.nextLine();
            adatok = sor.split(";");
            int tipus = Integer.parseInt(adatok[2]);
            switch (tipus) {
                case 0:
                    kep = new ImageIcon(this.getClass().getResource("/media/tegla.png")).getImage();
                    break;
                case 1:
                    kep = new ImageIcon(this.getClass().getResource("/media/kerdojel.gif")).getImage();
                    break;
                case 2:
                    kep = new ImageIcon(this.getClass().getResource("/media/atlatszo.png")).getImage();
                    break;
                default:
                    break;
            }
            Image ermeKep = new ImageIcon(this.getClass().getResource("/media/erme.png")).getImage();
            box = new Box(kep, ermeKep, Integer.parseInt(adatok[0]), Integer.parseInt(adatok[1]), Global.BOX_SZELESSEG, Global.BOX_MAGASSAG, Integer.parseInt(adatok[2]), Global.IDO);
            boxok.add(box);
        }

        return boxok;
    }

    @Override
    public List<PalyaElem> palyaListaba() throws Exception {
        List<PalyaElem> palyaLista = new ArrayList<>();

        Scanner scanner = new Scanner(PalyaFile);
        String sor;
        String[] adatok;
        PalyaElem palya;
        while (scanner.hasNextLine()) {
            sor = scanner.nextLine();
            adatok = sor.split(";");
            int kezdoX = Integer.parseInt(adatok[0]), vegX = Integer.parseInt(adatok[1]);
            int kepSzelesseg = vegX - kezdoX;
            palya = new PalyaElem(kezdoX, Global.PALYA_KEPY - Global.PALYA_MAGASSAG, kepSzelesseg, Global.PALYA_MAGASSAG);
            palyaLista.add(palya);
        }

        return palyaLista;
    }

    @Override
    public List<Cso> csoListaba() throws Exception {
        List<Cso> csovek = new ArrayList<>();

        Scanner scanner = new Scanner(CsoFile);
        String sor;
        String[] adatok;
        Cso cso = null;
        Image kep;
        while (scanner.hasNextLine()) {
            sor = scanner.nextLine();
            adatok = sor.split(";");
            int kepX = Integer.parseInt(adatok[0]), kepY = Integer.parseInt(adatok[1]);
            int tipus = Integer.parseInt(adatok[2]);
            switch (tipus) {
                case 0:
                    kep = new ImageIcon(this.getClass().getResource("/media/kisCso.png")).getImage();
                    cso = new Cso(kep, kepX, kepY, Global.CSO_SZELESSEG, Global.CSO_0_MAGASSAG, tipus);
                    break;
                case 1:
                    kep = new ImageIcon(this.getClass().getResource("/media/kozepesCso.png")).getImage();
                    cso = new Cso(kep, kepX, kepY, Global.CSO_SZELESSEG, Global.CSO_1_MAGASSAG, tipus);
                    break;
                case 2:
                    kep = new ImageIcon(this.getClass().getResource("/media/nagyCso.png")).getImage();
                    cso = new Cso(kep, kepX, kepY, Global.CSO_SZELESSEG, Global.CSO_2_MAGASSAG, tipus);
                    break;
                default:
                    break;
            }
            csovek.add(cso);
        }

        return csovek;
    }

    @Override
    public List<Lepcso> lepcsoListaba() throws Exception {
        List<Lepcso> lepcsoLista = new ArrayList<>();

        Scanner scanner = new Scanner(LepcsoFile);
        String sor;
        String[] adatok;
        Lepcso lepcso;
        while (scanner.hasNextLine()) {
            sor = scanner.nextLine();
            adatok = sor.split(";");
            int kepX = Integer.parseInt(adatok[0]), n = Integer.parseInt(adatok[1]);
            for (int i = 0; i < n; i++) {
                int kepY = Global.PALYA_KEPY - (Global.BOX_MAGASSAG + Global.PALYA_MAGASSAG + i * Global.BOX_MAGASSAG);

                lepcso = new Lepcso(kepX, kepY, Global.BOX_SZELESSEG, Global.BOX_MAGASSAG);
                lepcsoLista.add(lepcso);
            }

        }

        return lepcsoLista;
    }

    @Override
    public List<Ellenseg> ellensegListaba() throws Exception {
        List<Ellenseg> ellensegek = new ArrayList<>();

        Scanner scanner = new Scanner(EllensegFile);
        String sor;
        String[] adatok;
        Ellenseg ellenseg = null;
        Image kep;
        while (scanner.hasNextLine()) {
            sor = scanner.nextLine();
            adatok = sor.split(";");
            int kepX = Integer.parseInt(adatok[0]), kepY = Integer.parseInt(adatok[1]);
            int tipus = Integer.parseInt(adatok[2]);
            switch (tipus) {
                case 0:
                    kep = new ImageIcon(this.getClass().getResource("/media/tuz.gif")).getImage();
                    ellenseg = new Ellenseg(kep, kepX, kepY, Global.ELLENSEG_0_SZELESSEG, Global.ELLENSEG_0_MAGASSAG, tipus, Global.LEPES, Global.ELLENSEG_0_IDO);
                    break;
                case 1:
                    kep = new ImageIcon(this.getClass().getResource("/media/noveny.gif")).getImage();
                    ellenseg = new Ellenseg(kep, kepX, kepY, Global.ELLENSEG_1_SZELESSEG, Global.ELLENSEG_1_MAGASSAG, tipus, Global.LEPES, Global.ELLENSEG_1_IDO);
                    break;
                case 2:
                    kep = new ImageIcon(this.getClass().getResource("/media/gomba.gif")).getImage();
                    ellenseg = new Ellenseg(kep, kepX, kepY, Global.ELLENSEG_2_SZELESSEG, Global.ELLENSEG_2_MAGASSAG, tipus, Global.LEPES, Global.ELLENSEG_IDO);
                    break;
                case 3:
                    kep = new ImageIcon(this.getClass().getResource("/media/suni.gif")).getImage();
                    ellenseg = new Ellenseg(kep, kepX, kepY, Global.ELLENSEG_3_SZELESSEG, Global.ELLENSEG_3_MAGASSAG, tipus, Global.LEPES, Global.ELLENSEG_IDO);
                    break;
                case 4:
                    kep = new ImageIcon(this.getClass().getResource("/media/raketa.gif")).getImage();
                    ellenseg = new Ellenseg(kep, kepX, kepY, Global.ELLENSEG_4_SZELESSEG, Global.ELLENSEG_4_MAGASSAG, tipus, Global.LEPES, Global.ELLENSEG_0_IDO);
                    break;
                case 5:
                    kep = new ImageIcon(this.getClass().getResource("/media/tuske.png")).getImage();
                    ellenseg = new Ellenseg(kep, kepX, kepY, Global.ELLENSEG_5_SZELESSEG, Global.ELLENSEG_5_MAGASSAG, tipus, Global.LEPES, Global.ELLENSEG_IDO);
                    break;
                case 6:
                    kep = new ImageIcon(this.getClass().getResource("/media/tuz.gif")).getImage();
                    ellenseg = new Ellenseg(kep, kepX, kepY, Global.ELLENSEG_0_SZELESSEG, Global.ELLENSEG_0_MAGASSAG, tipus, Global.LEPES, Global.ELLENSEG_0_IDO);
                    break;
                default:
                    break;
            
            }
            ellensegek.add(ellenseg);
        }

        return ellensegek;
    }

}
