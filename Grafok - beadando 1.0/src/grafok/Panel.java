/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafok;

import javax.swing.*;
import java.awt.*;
import java.awt.BasicStroke;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.io.*;

/**
 *
 * @author Száraz Richárd
 */
public class Panel extends JPanel{
    private Graf<Csucs> graf = new Graf<Csucs>();   
    private boolean huzhato = false;
    private Integer csucsSzam = 1;
    private Csucs kijeloltCsucs;  
    private GUI gui;

    public Panel(){
        this.addMouseMotionListener(new PanelMouseMotionAdapter());
        this.addMouseListener(new PanelMouseAdapter());
    }
    
    public GUI getGUI(){    //visszaadja a gui-t
        if(gui == null){
            gui = (GUI)(SwingUtilities.getRoot(this));
        }
        return gui;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Color csucsszin;
        Color cimkeszin = Color.BLACK;  //a csúcson lévő felirat fekete színű legyen
        Graphics2D gr = (Graphics2D)g;
        gr.setBackground(new Color(180, 224, 120)); //a szerkesztőfelület háttérszíne
        gr.clearRect(0,0,this.getWidth(),this.getHeight());     //törli az egész panelt
        gr.scale(1,1);
        FontMetrics metrics = gr.getFontMetrics(new Font(Font.SANS_SERIF,Font.BOLD,14));
        gr.setFont(metrics.getFont());
        int hgt = metrics.getHeight();
        ArrayList<Csucs> csucsok = graf.getCsucsok();
        ArrayList<Graf<Csucs>.El> csucsbolElek;
        Csucs cs;
        Graf<Csucs>.El el;

        for(int i=0; i<csucsok.size(); i++){    //élek berajzolását, színezését végzi
            cs = csucsok.get(i);					
            csucsbolElek = graf.getElekCsucsbol(cs);
            for(int j=0; j<csucsbolElek.size(); j++){
                el = csucsbolElek.get(j);
                if(el.jelolt){
                    gr.setColor(Color.BLUE);    //kékre színezi az éleket, ha az algoritmus futtatásakor ez szükséges
                    gr.setStroke(new BasicStroke(3.0f));    //a vonal vastagságát állítja be
                }
                if(el.kijelolt && getGUI().kijeloltEl != null){
                    gr.setColor(Color.RED); //pirosra színezi az éleket, ha ki vannak jelölve
                    gr.setStroke(new BasicStroke(3.0f));    //a vonal vastagságát állítja be
                }
                gr.drawLine(cs.x, cs.y, el.hova.x, el.hova.y);  //az él kirajzolása
                gr.setColor(Color.BLACK);
                gr.setStroke(new BasicStroke(1.0f));    //a vonal vastagságát állítja be
                String elk = Integer.toString(el.koltseg);
                if(!getGUI().szelBejSelected() && !getGUI().melyBejSelected()){ //ha nem a szélességi vagy mélységi bejárás van kiválasztva
                    gr.drawString(elk,((cs.x + el.hova.x)/2), ((cs.y + el.hova.y)/2));  //akkor a költséget kiírja az élre
                }
            }
        }

        for(int i=0; i<csucsok.size(); i++){    //csúcsok kirajzolását, színezését végzi
            cs = csucsok.get(i);	
            
            if(kijeloltCsucs == cs && getGUI().csucsSelected() && getGUI().kijeloltCsucs != null){
                csucsszin = Color.RED;  //pirosra színezi a csúcsot, ha az ki van jelölve
                cimkeszin = Color.WHITE;
            }
            else{
                csucsszin = Color.WHITE;    //fehérre színezi a csúcsot, ha az nincs kijelölve
                cimkeszin = Color.BLACK;
            }
            if(cs.szin == OsCsucs.SZURKE){  //ha szükséges, szürkére állítja a háttér és fehérre a betű színt
                csucsszin = Color.GRAY;
                cimkeszin = Color.WHITE;
            }
            if(cs.szin == OsCsucs.FEKETE){  //ha szükséges, feketére állítja a háttér és fehérre a betű színt
                csucsszin = Color.BLACK;
                cimkeszin = Color.WHITE;
            }
            
            String felsoSor = null;
            String alsoSor;
            if(getGUI().szelBejSelected() || getGUI().krusAlgSelected()){ //ha a szélességi bejárás vagy a Kruskal-algoritmus van kiválasztva
                felsoSor = "   " + cs.cimke + "   ";    //a csúcs címkéje
                gr.setColor(csucsszin);
                gr.fill(new Ellipse2D.Double(cs.x - cs.getSugar(gr, felsoSor)-10, cs.y - cs.getSugar(gr, felsoSor)-10, (double)(cs.getSugar(gr, felsoSor)*2+20), (double)(cs.getSugar(gr, felsoSor)*2+20)));    //csúcs kirajzolása
                gr.setColor(cimkeszin);
                gr.drawString(felsoSor, cs.x - (metrics.stringWidth(felsoSor)/2), cs.y + (hgt/3)-5);    //felső sor kiírása
            }
            else if(getGUI().melyBejSelected()){ //ha a mélységi bejárás van kiválasztva
                felsoSor = "   " + cs.cimke + "   ";    //a csúcs címkéje
                alsoSor = cs.beSzam + "," + cs.kiSzam;   //a csúcs belépési(mélységi) száma és a kilépési(befejezési) száma
                gr.setColor(csucsszin);
                gr.fill(new Ellipse2D.Double(cs.x - cs.getSugar(gr, felsoSor)-10, cs.y - cs.getSugar(gr, felsoSor)-10, (double)(cs.getSugar(gr, felsoSor)*2+20), (double)(cs.getSugar(gr, felsoSor)*2+20)));    //csúcs kirajzolása
                gr.setColor(cimkeszin);
                gr.drawString(felsoSor, cs.x - (metrics.stringWidth(felsoSor)/2), cs.y + (hgt/3)-5);    //felső sor kiírása
                gr.drawString(alsoSor, cs.x - (metrics.stringWidth(alsoSor)/2), cs.y + hgt);    //alsó sor kiírása
            }
        }
    }

    public Graf<Csucs> getGraf(){
        return graf;
    }

    public void setGraf(Graf<Csucs> graf){
        this.graf = graf;
    }
    
    private boolean elenVane(Graf<Csucs>.El el, int x, int y){  //megadja, hogy egy kattintás az élen van-e
        double vx, vy, A, B, C, tav, minX, minY, maxX, maxY;
        minX = Math.min(el.hova.x, el.honnan.x);
        minY = Math.min(el.hova.y, el.honnan.y);
        maxX = Math.max(el.hova.x, el.honnan.x);
        maxY = Math.max(el.hova.y, el.honnan.y);
        vx = el.hova.x - el.honnan.x;
        vy = el.hova.y - el.honnan.y;
        A = vy;
        B = -vx;
        C = -(vy*el.honnan.x - vx*el.honnan.y);
        tav = (Math.abs(A*x + B*y + C))/(Math.sqrt(A*A + B*B)); //a kattintás és az él közötti távolság
        return tav < 4 && (minX <= x && x <= maxX && minY <= y && y <= maxY);   //szélességben plusz 4 pixelnyivel, hosszúságban csak a csúcsok középpontjaiban érvényes a kattintás
    }

    public void elTorles(int x, int y){ //él törlését végzi el
        ArrayList<Csucs> csucsok = graf.getCsucsok();
        boolean l = false;  //igaz lesz, ha elég közel kattintunk egy élhez
        for(int i=0; i<csucsok.size() && !l; i++){  //végig megy a csúcsokon
            Csucs cs = csucsok.get(i);
            ArrayList<Graf<Csucs>.El> elek = graf.getElekCsucsbol(cs);			
            for(int j=0; j<elek.size() && !l; j++){ //a csúcsokhoz tartozó éleken is végigmegy
                Graf<Csucs>.El el = elek.get(j);
                if(elenVane(el, x, y)){
                    graf.elTorles(el, cs);  //az él 2 egymáson lévő egyenesből áll, ez törli az elsőt
                    ArrayList<Graf<Csucs>.El> elek2 = graf.getElekCsucsbol(el.hova);
                    ArrayList<Integer> sz = new ArrayList();
                    for(int k=0; k<elek2.size(); k++){
                        if(elek2.get(k).hova == cs){
                            sz.add(k);
                        }
                    }
                    for(int m=0; m<sz.size(); m++){
                        graf.elTorles(elek2.get(sz.get(m)), el.hova);   //törli a 2. egyenest, amiből az él állt, így az él teljesen eltűnik
                    }
                }
            }
        }		
    }

    public void elAtnevezeshezKijeloles(int x, int y){  //az él költségének átnevezéséhez szükséges
        ArrayList<Csucs> csucsok = graf.getCsucsok();
        boolean l = false;
        for(int i=0; i<csucsok.size() && !l; i++){  //végig megy a csúcsokon
            Csucs cs = csucsok.get(i);
            ArrayList<Graf<Csucs>.El> elek = graf.getElekCsucsbol(cs);			
            for(int j=0; j<elek.size() && !l; j++){ //a csúcsokhoz tartozó éleken is végigmegy
                Graf<Csucs>.El el = elek.get(j);
                if(elenVane(el, x, y)){
                    getGUI().setKijeloltEl(elek.get(j));   //az él, amin a kattintás történt, lesz a kijelölt él, így átnezevhető lesz
                }
            }
        }
    }
    
    public int hanyadikCsucsraKattintas(int x, int y){ //megadja, hogy hányadik csúcsra esett a kattintás
        ArrayList<Csucs> csucsok = graf.getCsucsok();       			
        for(int i = 0; i<csucsok.size(); i++){
            if(csucsok.get(i).rajtaVane(x, y)){
                return i+1;
            }
        }
        return -1;
    }

    public static class Csucs extends OsCsucs implements Serializable{ //a Csucs osztály, amely az OsCSucs osztályból származik és implementálja a Serializable interfészt(a tároláshoz)
        public String cimke;    //csúcs neve
        public int x;   //csúcs középpontjának X koordinátája
        public int y;   //csúcs középpontjának Y koordinátája
        public int sugar;   //a csúcs sugara

        public Csucs(String c, int x, int y){   //Csucs konstruktor
            cimke = c;
            this.x = x;
            this.y = y;
        }

        public int getSugar(Graphics2D gr, String s){   //megadja a csúcs sugarát(pixelben)
            FontMetrics metrics = gr.getFontMetrics(new Font(Font.SANS_SERIF,Font.BOLD,14));
            sugar = metrics.stringWidth(s)/2;
            return metrics.stringWidth(s)/2;
        }

        public boolean rajtaVane(int px, int py){   //eldönti 1 pontról, hogy az a csúcson belül van-e
            return Math.sqrt((px-x)*(px-x) + (py-y)*(py-y)) <= sugar + 10;  //a +10 a korábbi ráhagyás miatt kell, hogy a szöveg és a csúcs széle között maradjon hely
        }
    }	

    public class PanelMouseAdapter extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){ //az egér lenyomásakor történő események:
            Panel p = ((Panel)(e.getSource()));
            GUI gui = p.getGUI();
            ArrayList<Csucs> csucsok = graf.getCsucsok();
            int i = hanyadikCsucsraKattintas(e.getX(), e.getY());
            if(i<0 && gui.csucsSelected()){ //ha csúcsmódban nem csúcsra esik a kattintás
                graf.addCsucs(new Csucs(csucsSzam.toString(), e.getX(), e.getY())); //létrehoz egy új csúcsot
                csucsSzam++;
                p.repaint();
            }
            else{
                if(0 < i){  //ha a csúcsra esik a kattintás
                    kijeloltCsucs = csucsok.get(i-1);   //az a csúcs legyen a kijelölt, amelyikre a kattintás esett
                    gui.setKijeloltCsucs(kijeloltCsucs);
                    p.repaint();
                }
            }
            if(gui.elSelected() && i < 0){  //élmódban, ha nem a csúcsra esik a kattintás
                if(getGUI().kijeloltEl != null){ //csak egy él lehet kijelölve
                    getGUI().kijeloltEl.kijelolt = false;
                    graf.getParja(getGUI().kijeloltEl).kijelolt = false;
                }
                p.elAtnevezeshezKijeloles(e.getX(), e.getY());
                p.repaint();
            }
            if(gui.torlesSelected()){   //törlésmódban
                if(i > 0){  //ha a csúcsra esik a kattintás
                    graf.csucsTorles(csucsok.get(i-1)); //törli a csúcsot(és a csúcshoz tatozó éleket), amin a kattintás történt
                    p.repaint();
                }
                else{   //ha nem a csúcsra esik a kattintás
                    p.elTorles(e.getX(), e.getY()); //megpróbál élt törölni
                    p.repaint();
                }
            }           
            huzhato = (kijeloltCsucs != null && kijeloltCsucs.rajtaVane(e.getX(), e.getY()));   //beállítja, hogy a kattintás után, a kijeölt csúcs, húzható legyen-e
        }

        @Override
        public void mouseReleased(MouseEvent e){    //az egér felengedésekor történő események:
            Panel p = ((Panel)(e.getSource()));
            GUI gui = p.getGUI();
            if(gui.elSelected() && kijeloltCsucs != null){  //élmódban, ha van kijelölt csúcs
                ArrayList<Csucs> csucsok = graf.getCsucsok();
                int i = hanyadikCsucsraKattintas(e.getX(), e.getY());			
                if(i > 0 && kijeloltCsucs != csucsok.get(i-1)){ //ha a felngedés egy másik csúcsra esik(nem arra, ahol az egér lenyomása történt)
                    graf.addEl(kijeloltCsucs, csucsok.get(i-1), 1);
                    graf.addEl(csucsok.get(i-1), kijeloltCsucs, 1);
                    kijeloltCsucs = null;
                    p.repaint();
                }
                else{   //ha a felengedés ugyanazon a csúcson törénik
                    kijeloltCsucs = null;   //tölri a csúcs kijelöltségét
                    p.repaint();
                }
            }
            huzhato = false;
        }
    }

    public class PanelMouseMotionAdapter extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e){ //az egér folyamatos lenyomásakor és húzásakor történő események:
            Panel p = ((Panel)(e.getSource()));
            GUI gui = p.getGUI();
            if(kijeloltCsucs != null && gui.csucsSelected() && huzhato){    //ha csúcsmódban van kijelölt csúcs, ami húzható
                if(e.getX() < 10 || e.getY() < 10 || e.getX() > p.getWidth()-10 || e.getY() > p.getWidth()-10){ //nem engedi, hogy egy csúcs teljes terjedelmével kikerülhessen a felületről
                    return;
                }
                kijeloltCsucs.x = e.getX(); //változtatja a csúcs középpontjának x koordinátáját
                kijeloltCsucs.y = e.getY(); //változtatja a csúcs középpontjának y koordinátáját
                ((Panel)(e.getSource())).repaint();
            }
        }				
    }
}
