/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafok;

import java.util.concurrent.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author Száraz Richárd
 */
public class Graf<C extends OsCsucs> implements Serializable{

    private ConcurrentHashMap<C, ArrayList<El>> graf = new ConcurrentHashMap<C, ArrayList<El>>();   //a gráfot tárolja éllistásan
    private LinkedBlockingQueue<C> sor = new LinkedBlockingQueue<C>();
    private HashMap<C,OsCsucs> cache = new HashMap<C,OsCsucs>();
    private HashMap<C, El> szulo = new HashMap<C, El>();
    private ListIterator<C> csucsIterator;
    private PriorityQueue<El> elekPQ;
    private ArrayList<C> szomszedok;
    private PriorityQueue<C> minQ;
    private Graf<OsCsucs> fa;   
    private C aktCsucs;    
    public int inf = (Integer.MAX_VALUE / 2);
    private int sorszám = 1;    //azt tárolja, hogy hányadikként lett elhelyezve a csúcs a panelen    
    private int lepesSzam;    
    int delay = 10;

    public Graf(){  //Graf konstruktor
        minQ = new PriorityQueue<C>(2, new KisebbTavComparator());  //2 = kezdeti kapacitás a sorban, new KisebbTavComparator() = a komparátor, amit a prioritási sor használ
    }

    public void addCsucs(C csucs){  //hozzáad egy csúcsot a gráfhoz
        if(graf.get(csucs) == null){    //ha még nem létezik a csúcs
            graf.put(csucs, new ArrayList<El>());
            csucs.tav = inf;    //a csúcs távolságát végtelenre állítja
            csucs.hanyadik = sorszám++; //beállítja, hogy hányadikként lett elhelyezve a csúcs a panelen
        }
    }

    public void addEl(C honnan, C hova, int koltseg){   //az él hozzáadását végzi
        if (!graf.get(honnan).contains(new El(hova, honnan, koltseg))) {  //ha még nem létezik az él
            graf.get(honnan).add(new El(hova, honnan, koltseg));
        }
    }

    public ArrayList<C> getCsucsok(){   //visszaadja az összes csúcsot
        ArrayList<C> csucsok = new ArrayList<C>();
        csucsok.addAll(graf.keySet());
        return csucsok;
    }

    public ArrayList<El> getElekCsucsbol(C csucs){  //visszaadja a paraméterül kapott csúcshoz tartozó éleket
        ArrayList<El> elek = graf.get(csucs);
        Collections.sort(elek, new HovaHanyadikComparator());
        return elek;
    }

    public PriorityQueue<El> getOsszesEl(){ //visszaadja az összes élt
        ArrayList<C> csucsok = getCsucsok();
        PriorityQueue<El> osszesel = new PriorityQueue<El>(2, new KisebbSulyComparator());
        for(int i=0; i<csucsok.size(); i++){    //végigmegy minden csúcson
            osszesel.addAll(getElekCsucsbol(csucsok.get(i)));
        }
        return osszesel;
    }

    public ArrayList<C> szelessegiBejaras(C s){ //a szélességi bejárás algoritmusa
        ArrayList<C> sorrend = new ArrayList<C>();  //ebben tároljuk az eredményt, a csúcsokat, a startcsúcstól való távolságuk növekvő sorrendjében
        return sorrend;
    }

    public boolean szelessegiBejarasLepesenkent(){  //a szélességi bejárás lépésenkénti algoritmusa
        return true;
    }
    
    public void melysegiBejaras(C s){   //a mélységi bejárás algoritmusa
    }

    public void MB(C aktCsucs){    //az MB eljárás algoritmusa
    }
    
    public boolean melysegiBejarasLepesenkent(){    //a mélységi bejárás lépésenkénti algoritmusa
        return true;
    }

    public void MBLepesenkent(){    //az MB eljárás lépésenkénti algoritmusa
    }

    public HashMap<C, El> dijkstraAlgoritmus(C s){  //a Dijkstra-algoritmus
        HashMap<C, El> szulo = new HashMap<C, El>();
        return szulo;
    }

    public boolean dijkstraAlgoritmusLepesenkent(){ //a Dijkstra-algoritmus lépésenként
        return true;
    }
    
    public HashMap<C, El> bellmanfordAlgoritmus(C s){   //a Bellman-Ford-algoritmus
        HashMap<C, El> szulo = new HashMap<C, El>();
        return szulo;
    }

    public boolean bellmanfordAlgoritmusLepesenkent(){  //a Bellman-Ford-algoritmus lépésenként
        return false;
    }

    public void kruskalAlgoritmus(){    //a Kruskal-algoritmus
        PriorityQueue<El> elek = getOsszesEl();
        Graf<OsCsucs> fa = new Graf<OsCsucs>();
        El el;
        while(!elek.isEmpty()){ //addig folytatja, ameddig az elek üres nem lesz
            el = elek.poll();   //kiveszi és egyenlővé teszi az elek első elemével az el-t
            elek.remove(getParja(el));    //kiveszi az elek-ből az el-hez tartozó másik élt is, az el párját          
            OsCsucs cs1;
            OsCsucs cs2;
            if(cache.containsKey(el.honnan)){   //ez biztosítja, hogy a gráf szükséges csúcsai egyértelműen leképezhetőek legyenek a fa-ba
                cs1 = cache.get(el.honnan); //ha már benne van a cache-ben, akkor azt használja
            }
            else{
                cs1 = new OsCsucs();    //ha még nem létezett, létrehoz egy újat
                cache.put(el.honnan, cs1);  //majd beteszi a cache-be
            }
            if(cache.containsKey(el.hova)){ //ez biztosítja, hogy a gráf szükséges csúcsai egyértelműen leképezhetőek legyenek a fa-ba
                cs2 = cache.get(el.hova);   //ha már benne van a cache-ben, akkor azt használja
            }
            else{
                cs2 = new OsCsucs();    //ha még nem létezett, létrehoz egy újat
                cache.put(el.hova, cs2);    //majd beteszi a cache-be
            }       
            Graf<OsCsucs>.El masolatEl = new Graf<OsCsucs>.El(cs1, cs2, 1); //létrehoz egy élt a cs1 és cs2 csúcsokkal 1 költséggel 
            if(!fa.szelessegiBejaras(masolatEl.honnan).contains(masolatEl.hova)){ //ha a fa nem tartalmaz kört
                fa.addCsucs(masolatEl.honnan); //a fa-hoz hozzáadja az élhez tartozó egyik csúcsot
                fa.addCsucs(masolatEl.hova);   //a fa-hoz hozzáadja az élhez tartozó másik csúcsot
                fa.addEl(masolatEl.hova, masolatEl.honnan, masolatEl.koltseg);   //a fa-hoz hozzáadja a 2 csúcs közötti élt
                fa.addEl(masolatEl.honnan, masolatEl.hova, masolatEl.koltseg);   //a fa-hoz adja az élhez tartozó másik élt, az él párját is
                el.jelolt = true;   //jelöltté teszi el-et
                getParja(el).jelolt = true; //jelöltté teszi az el-hez tartozó másik élt, az el párját is
                el.honnan.szinez(OsCsucs.FEKETE);   //feketére színezi az el-hez tarozó egyik csúcsot
                el.hova.szinez(OsCsucs.FEKETE); //feketére színezi az el-hez tarozó másik csúcsot
            }
        }
    }

    public boolean kruskalAlgoritmusLepesenkent(){  //a Kruskal-algoritmus lépésenként
        El el;
        if(elekPQ != null && !elekPQ.isEmpty()){  //ha az elekPQ nem üres
            el = elekPQ.poll(); //kiveszi és egyenlővé teszi az elekPQ első elemével az el-t
            elekPQ.remove(getParja(el));    //kiveszi az elekPQ-ból az el-hez tartozó másik élt is, az el párját          
            OsCsucs cs1;
            OsCsucs cs2;
            if(cache.containsKey(el.honnan)){   //ez biztosítja, hogy a gráf szükséges csúcsai egyértelműen leképezhetőek legyenek a fa-ba
                cs1 = cache.get(el.honnan); //ha már benne van a cache-ben, akkor azt használja
            }
            else{
                cs1 = new OsCsucs();    //ha még nem létezett, létrehoz egy újat
                cache.put(el.honnan, cs1);  //majd beteszi a cache-be
            }
            if(cache.containsKey(el.hova)){ //ez biztosítja, hogy a gráf szükséges csúcsai egyértelműen leképezhetőek legyenek a fa-ba
                cs2 = cache.get(el.hova);   //ha már benne van a cache-ben, akkor azt használja
            }
            else{
                cs2 = new OsCsucs();    //ha még nem létezett, létrehoz egy újat
                cache.put(el.hova, cs2);    //majd beteszi a cache-be
            }       
            Graf<OsCsucs>.El masolatEl = new Graf<OsCsucs>.El(cs1, cs2, 1); //létrehoz egy élt a cs1 és cs2 csúcsokkal 1 költséggel     
            if(!fa.szelessegiBejaras(masolatEl.honnan).contains(masolatEl.hova)){ //ha a fa nem tartalmaz kört
                fa.addCsucs(masolatEl.honnan); //a fa-hoz hozzáadja a másolt élhez tartozó egyik csúcsot
                fa.addCsucs(masolatEl.hova);   //a fa-hoz hozzáadja a másolt élhez tartozó másik csúcsot
                fa.addEl(masolatEl.hova, masolatEl.honnan, masolatEl.koltseg);   //a fa-hoz hozzáadja a 2 csúcs közötti másolt élt
                fa.addEl(masolatEl.honnan, masolatEl.hova, masolatEl.koltseg);   //a fa-hoz adja a másolt élhez tartozó másik élt, a másolt él párját is
                el.jelolt = true;   //jelöltté teszi el-et
                getParja(el).jelolt = true; //jelöltté teszi az el-hez tartozó másik élt is, az el párját
                el.honnan.szinez(OsCsucs.FEKETE);   //feketére színezi az el-hez tarozó egyik csúcsot
                el.hova.szinez(OsCsucs.FEKETE); //feketére színezi az el-hez tarozó másik csúcsot
            }
        } 
        else{   //ha az elekPQ üres
            if(elekPQ == null){
               fa = new Graf<OsCsucs>();
               elekPQ = getOsszesEl();
               kruskalAlgoritmusLepesenkent();
           }else{
               elekPQ = null;
           }
       }
       return elekPQ == null;
    }  

    public void startBeallitas(C csucs){    //a start beállításokat végzi
        if(sor.size() == 0){    //ha üres a sor
            lepesSzam = 0;  //a lépésszámot nullára állítja
            aktCsucs = csucs;   //beállítja a paraméterül kapott csúcsot az aktuális csúcsnak
            csucsIterator = null;
            szomszedok = null;
            feherre();  //minden csúcsot fehérre színez
        }
    }

    public void feherre(){  //a csúcsok fehérre színezését végzi
        ArrayList<C> csucsok = getCsucsok();
        for(int i=0; i<csucsok.size(); i++){    //végigmegy az összes csúcson
            csucsok.get(i).szinez(OsCsucs.FEHER);   //beállítja a színüket fehérre
        }
    }

    public void csucsTorles(C csucs){   //csúcs törlését végzi
        ArrayList<El> elek = new ArrayList<El>();
        graf.remove(csucs); //eltávolítja a graf-ból a csúcsot
        for(C cs : getCsucsok()){   //végigmegy a csúcsokon
            for(El e : getElekCsucsbol(cs)){    //végigmegy egy csúcshoz tartozó éleken
                if(e.hova == csucs){    //ha az élhez tatozó(cél) csúcs egyenlő a paraméterül kapott csúccsal
                    e.hova = cs;    //kicseréli a csúcsot a cs csúcsra
                    elek.add(e);    //eltárolja a törölni kívánt csúcshoz tartozó éleket
                }
            }
        }
        for(El e : elek){   //végigmegy az elek-en
            //graf.get(e.hova).remove(e); 
            elTorles(e, e.hova);    //eltávolítja a szükséges éleket
        }
    }

    public void elTorles(El el, C kezdocs){ //él törlését végzi
        graf.get(kezdocs).remove(el);
    }

    public void setKoltseg(El e, Integer k){    //él költségének a beállítását végzi
        e.koltseg = k;
        getParja(e).koltseg = k;
    }

    El getParja(El e){  //visszaadja az élhez tartozó másik élt is, az él párját
        ArrayList<El> elek = getElekCsucsbol(e.hova);
        for(int i=0; i<elek.size(); i++){   //végigmegy a paraméterül kapott élhez tartozó célcsúcsból kiinduló éleken
            if(e.honnan == elek.get(i).hova){   //ha a paraméterül kapott él kiinduló csúcsa megegyezik az él cél csúcsával
                return elek.get(i); //visszaadja az él párját
            }
        }
        return null;
    }

    public void jelolesTorles(Collection<El> h){
        for(El e : h){  //végigmegy az éleken
            e.jelolt = false;   //törli az él jelöltségét
            getParja(e).jelolt = false; //törli az élhez tartozó másik élnek, az él párjának is a jelöltségét
        }
    }

    public class El implements Serializable{    //az El osztály, amely implementálja a Serializable interfészt(a tároláshoz)
        public C hova;  //a csúcs, ahova tart az él
        public C honnan;    //a csúcs, ahonnan indul az él
        public int koltseg; //az él költsége
        public boolean jelolt;  //jelölt-e az él(kék színű) az algortimusok futtatása közben
        public boolean kijelolt;    //jelölt-e az él(piros) átnevezéshez vagy törléshez

        public El(C h, C ho, int k){    //El konstruktor
            hova = h;
            honnan = ho;
            koltseg = k;
        }

        @Override
        public boolean equals(Object o){    //visszaadja, hogy egyenlő-e a két objektum
            if(o != null){  //ha a paraméterül kapott objektum nem null
                El e = (El)(o); //a paraméterül kapott objektum El-lé alakítása
                return hova.equals(e.hova) && honnan.equals(e.honnan) && koltseg == e.koltseg;  //ha a két él honnan és hová csúcsa, valamint a költségük megegyezik, akkor egyenlő  a két él
            }
            return false;   //különben nem egyenlő
        }
    }

    public class KisebbTavComparator implements Comparator<OsCsucs>, Serializable{  //összehasonlítja két csúcs távolságát
        public int compare(OsCsucs a, OsCsucs b) {
            return a.tav - b.tav;
        }
    }

    public class KisebbSulyComparator implements Comparator<El>, Serializable{  //összehasonlítja két él költségét
        public int compare(El a, El b){
            return a.koltseg - b.koltseg;
        }
    }

    public class HovaHanyadikComparator implements Comparator<El>, Serializable{    //összehasonlítja két él célcsúcsának a bekerülési(panelre történő elhelyezésének) sorszámát
        public int compare(El a, El b){
            return a.hova.hanyadik - b.hova.hanyadik;
        }
    }  
}
