/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import adatkezeles.AdatInput;
import adatkezeles.FileInput;
import alaposztalyok.Cso;
import alaposztalyok.Global;
import alaposztalyok.Mario;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thean
 */
public class Teszt {
    
    public Teszt() {

    }
    
    @Test
    public void adatInputTeszt() throws Exception{
        InputStream BoxFile = this.getClass().getResourceAsStream("/media/boxok.txt");
        InputStream PalyaFile = this.getClass().getResourceAsStream("/media/palya.txt");
        InputStream CsoFile = this.getClass().getResourceAsStream("/media/csovek.txt");
        InputStream LepcsoFile = this.getClass().getResourceAsStream("/media/lepcso.txt");
        InputStream EllensegFile = this.getClass().getResourceAsStream("/media/ellensegek.txt");
        
        AdatInput adatInput = new FileInput(BoxFile, PalyaFile, CsoFile, LepcsoFile, EllensegFile);
        
        assertEquals(54, adatInput.BoxListaba().size());
        assertEquals(4, adatInput.palyaListaba().size());
        assertEquals(6, adatInput.csoListaba().size());
        assertEquals(89, adatInput.lepcsoListaba().size());
        assertEquals(26, adatInput.ellensegListaba().size());
        
    }
    
    @Test
    public void marioTeszt() throws Exception {
        int erme = 0;
        Mario mario = new Mario(null, Global.MARIO_KEZDOX, Global.MARIO_KEZDOY, Global.MARIO_PICI_KEPSZELESSEG, Global.MARIO_PICI_KEPMAGASSAG, 3, Global.LEPES, Global.IDO, null);

        assertEquals(mario.getKepX(), Global.MARIO_KEZDOX);
        
        assertTrue(mario.getErme() == 0);
        mario.ermetSzerzett(); erme++;
        assertTrue(mario.getErme() == 1);
        mario.setErmeNULL();
        assertFalse(mario.getErme()==erme);
        
    }
    
    @Test
    public void csoTeszt() throws Exception {
        int eredetiKepX=50;
        Cso cso = new Cso(null, eredetiKepX, 75, 100, 150, 0);
        int kepX=250;
        cso.setKepX(kepX);

        assertEquals(cso.getKepX(), eredetiKepX-kepX);
        
        cso.eredetiVisszaallitas();
        assertTrue(cso.getKepX()==eredetiKepX);
        assertFalse(cso.getKepX()==kepX);
        
    }
}
