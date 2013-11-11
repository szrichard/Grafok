/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafok;

import java.io.*;

/**
 *
 * @author Száraz Richárd
 */
public class OsCsucs implements Serializable{
    public int szin;
    public static final int FEHER = 1;
    public static final int SZURKE = 2;
    public static final int FEKETE = 3;
    public int tav = 2; //az eddig talált legrövidebb út hosszát tárolja(a végtelen nagy tévolságot '#'-kal jelöljük)
    public int beSzam = 0;    //a csúcs belépési száma, azaz mélységi száma, azt tárolja, hogy a csúcsot hányadikként értük el(hányadikként lett szürke)
    public int kiSzam = 0;    //a csúcs kilépési száma, azaz befejezési száma, azt tárolja, hogy hányadikként fejeztük be a csúcsot(hányadikként lett fekete)
    public int hanyadik = 1;    //azt tárolja, hogy hányadikként került be a csúcs(hányadikként lett elhelyezve a panelen)

    public void szinez(int sz){
        szin = sz;
    }
}
