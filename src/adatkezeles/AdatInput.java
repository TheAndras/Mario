/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adatkezeles;

import alaposztalyok.Box;
import alaposztalyok.Cso;
import alaposztalyok.Ellenseg;
import alaposztalyok.Lepcso;
import alaposztalyok.PalyaElem;
import java.util.List;

/**
 *
 * @author thean
 */
public interface AdatInput {
    List<Box> BoxListaba() throws Exception;
    List<PalyaElem>palyaListaba()throws Exception;
    List<Cso>csoListaba()throws Exception;
    List<Lepcso>lepcsoListaba()throws Exception;
    List<Ellenseg>ellensegListaba()throws Exception;
}
