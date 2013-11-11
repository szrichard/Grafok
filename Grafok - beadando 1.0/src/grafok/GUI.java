/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafok;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*; 

/**
 *
 * @author Száraz Richárd
 */
public class GUI extends JFrame implements ActionListener{
    java.util.Timer timer = new java.util.Timer(); 
    Panel.Csucs kijeloltCsucs;
    Graf.El kijeloltEl;
    String sebesseg = "1";
    double aktSeb = 1;
    Panel p;
    Graf graf;
    JFrame gui;   
    
    Panel panel = new Panel();
    JPanel oldalPanel = new JPanel();
    JPanel adatokPanel = new JPanel();
    JPanel atnevezPanel = new JPanel();
    JPanel sebessegPanel = new JPanel();
    JPanel futtatasPanel = new JPanel();
    
    JMenuBar menuBar = new JMenuBar();
    JMenu fajlMenu = new JMenu("Fájl");
    JMenu szerkMenu = new JMenu("Szerkesztés");
    JMenu grafalgMenu = new JMenu("Gráfalgoritmus");
    JMenu leirMenu = new JMenu("Leírás");   
    JMenuItem mentes = new JMenuItem("Mentés");
    JMenuItem megnyitas = new JMenuItem("Megnyitás");
    JMenuItem kilepes = new JMenuItem("Kilépés");
    JRadioButtonMenuItem csucs = new JRadioButtonMenuItem("Csúcs");
    JRadioButtonMenuItem el = new JRadioButtonMenuItem("Él");
    JRadioButtonMenuItem torles = new JRadioButtonMenuItem("Törlés");
    JRadioButtonMenuItem szelBej = new JRadioButtonMenuItem("Szélességi bejárás");
    JRadioButtonMenuItem krusAlg = new JRadioButtonMenuItem("Kruskal-algoritmus");
    JRadioButtonMenuItem dijAlg = new JRadioButtonMenuItem("Dijkstra-algoritmus");
    JRadioButtonMenuItem melyBej = new JRadioButtonMenuItem("Mélységi bejárás");
    JRadioButtonMenuItem bellFordAlg = new JRadioButtonMenuItem("Bellman-Ford-algoritmus");
    JMenuItem haszn = new JMenuItem("Használati útmutató");

    ButtonGroup szerkGroup = new ButtonGroup();
    ButtonGroup grafalgGroup = new ButtonGroup();
    JTextField atnevezmezo = new JTextField();
    JTextField sebessegmezo = new JTextField();
    JTextField adatokAlg = new JTextField("Algoritmus: Szélességi bejárás");
    JTextField adatokAlgJell = new JTextField("Jellemzés: Szélességi bejárás egy forrásból");
    JTextField adatokSeb = new JTextField("Sebesség: " + sebesseg);
    JTextField adatokSzerkmod = new JTextField("Szerkesztésmód: Csúcs");
    JButton atnevez = new JButton("Átnevez");
    JButton beallit = new JButton("Beállít");
    JButton futtatas = new JButton("Futtatás");
    JButton lepesfuttatas = new JButton("Futtatás lépésenként");

    public GUI(String title){
        super(title);
        gui = this;
        panel.setSize(200,200);
        add(panel, BorderLayout.CENTER);
        menuBar.add(fajlMenu);
        fajlMenu.add(mentes);
        fajlMenu.add(megnyitas);
        fajlMenu.add(kilepes);
        menuBar.add(szerkMenu);
        szerkMenu.add(csucs);
        szerkMenu.add(el);
        szerkMenu.add(torles);
        menuBar.add(grafalgMenu);
        grafalgMenu.add(szelBej);
        grafalgMenu.add(melyBej);       
        grafalgMenu.add(dijAlg);
        grafalgMenu.add(bellFordAlg);
        grafalgMenu.add(krusAlg);
        menuBar.add(leirMenu);
        leirMenu.add(haszn);
        panel.add(menuBar);
        setJMenuBar(menuBar);
        oldalPanel.setLayout(new GridLayout(4,1,15,15));    //elrendezés beállítása, paraméterek: sorok(nullával), oszlopok(nullával), vízszintes rés, függőleges rés
        adatokPanel.setLayout(new GridLayout(4,1,5,5));
        oldalPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));  //térköz beállítása felül, balra, alul, jobbra
        szerkGroup.add(csucs);
        szerkGroup.add(el);
        szerkGroup.add(torles);
        grafalgGroup.add(szelBej);
        grafalgGroup.add(melyBej);              
        grafalgGroup.add(dijAlg);     
        grafalgGroup.add(bellFordAlg);
        grafalgGroup.add(krusAlg);
        futtatas.setToolTipText("Akkor aktív, ha van kijelölt csúcs.");
        lepesfuttatas.setToolTipText("Akkor aktív, ha van kijelölt csúcs.");

        adatokPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Adatok"),
                BorderFactory.createEmptyBorder(5,10,5,10)));
        
        atnevezPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Átnevezés"),
                BorderFactory.createEmptyBorder(40,20,20,20)));

        sebessegPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Sebesség(másodperc)"),
                BorderFactory.createEmptyBorder(40,20,20,20)));

        futtatasPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Gráfalgoritmus futtatása"),
                BorderFactory.createEmptyBorder(40,20,20,20)));

        atnevezmezo.setEnabled(false);
        atnevezmezo.setMinimumSize(new Dimension(200,25));
        atnevezmezo.setPreferredSize(new Dimension(200,25));
        sebessegmezo.setMinimumSize(new Dimension(200,25));
        sebessegmezo.setPreferredSize(new Dimension(200,25));
        atnevez.setEnabled(false);
        futtatas.setEnabled(false);
        lepesfuttatas.setEnabled(false);
        atnevezPanel.add(atnevezmezo);
        atnevezPanel.add(atnevez);
        sebessegPanel.add(sebessegmezo);
        sebessegPanel.add(beallit);
        csucs.setSelected(true);
        szelBej.setSelected(true);
        adatokAlg.setEditable(false);
        adatokAlgJell.setEditable(false);
        adatokSeb.setEditable(false);
        adatokSzerkmod.setEditable(false);
        adatokPanel.add(adatokAlg);
        adatokPanel.add(adatokAlgJell);
        adatokPanel.add(adatokSeb);
        adatokPanel.add(adatokSzerkmod);
        futtatasPanel.add(futtatas);
        futtatasPanel.add(lepesfuttatas);

        el.addActionListener(this);
        csucs.addActionListener(this);
        torles.addActionListener(this);
        szelBej.addActionListener(this);
        melyBej.addActionListener(this);
        dijAlg.addActionListener(this);
        krusAlg.addActionListener(this);
        bellFordAlg.addActionListener(this);
        atnevez.addActionListener(this);
        futtatas.addActionListener(this);
        lepesfuttatas.addActionListener(this);
        kilepes.addActionListener(this);
        mentes.addActionListener(this);
        megnyitas.addActionListener(this);
        haszn.addActionListener(this);
        beallit.addActionListener(this);
        oldalPanel.add(adatokPanel);
        oldalPanel.add(atnevezPanel);
        oldalPanel.add(sebessegPanel);
        oldalPanel.add(futtatasPanel);
        add(oldalPanel, BorderLayout.EAST);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(140,12);
        setSize(1050,700);
        
        sebessegmezo.addKeyListener(new java.awt.event.KeyAdapter(){  //akkor lesz aktív a Beállít gomb, ha a sebességmező nem üres
            public void keyReleased(java.awt.event.KeyEvent evt){   
                beallit.setEnabled(sebessegmezo.getDocument().getLength() > 0);
            }  
        });
        
        atnevezmezo.addKeyListener(new java.awt.event.KeyAdapter(){  //akkor lesz aktív az Átnevez gomb, ha az átnevezmező nem üres
            public void keyReleased(java.awt.event.KeyEvent evt){   
                atnevez.setEnabled(atnevezmezo.getDocument().getLength() > 0);
            }  
        });    

    }

    public boolean csucsSelected(){
        return csucs.isSelected();
    }

    public boolean elSelected(){
        return el.isSelected();
    }

    public boolean torlesSelected(){
        return torles.isSelected();
    }
    
    public boolean szelBejSelected(){
        return szelBej.isSelected();
    }
    
    public boolean melyBejSelected(){
        return melyBej.isSelected();
    }
    
    public boolean dijAlgSelected(){
        return dijAlg.isSelected();
    }
    
    public boolean bellFordAlgSelected(){
        return bellFordAlg.isSelected();
    }
    
    public boolean krusAlgSelected(){
        return krusAlg.isSelected();
    } 
    
    public void kijeloltElKijeloltsegTorles(){ //törli a kijelölt él kijelöltségét
        if(kijeloltEl != null){
            kijeloltEl = null;
        }
    }
    
    public void kijeloltCsucsKijeloltsegTorles(){  //törli a kijelölt csúcs kijelöltségét
        if(kijeloltCsucs != null){
            kijeloltCsucs = null;
        }
    }

    public void setKijeloltCsucs(Panel.Csucs cs){  //csúcs kijelölésekor hívódik meg
        kijeloltCsucs = cs;
        if(csucsSelected() && cs != null){  //csúcsmódban, ha a kijelölt csúcs nem üres
            atnevezmezo.setEnabled(true);
            atnevezmezo.setText(cs.cimke);
            atnevez.setEnabled(true);
            futtatas.setEnabled(true);
            lepesfuttatas.setEnabled(true);
            panel.getGraf().startBeallitas(kijeloltCsucs);           
        }
    }

    public void setKijeloltEl(Graf.El el){ //él kijelölésekor hívódik meg
        kijeloltEl = el;
        if(elSelected() && el != null && !szelBejSelected() && !melyBejSelected()){ //élmódban, ha a kijelölt él nem üres és nem a szélességi vagy a mélységi bejárás van kiválasztva
            atnevezmezo.setEnabled(true);
            String koltseg = Integer.toString(el.koltseg);
            atnevezmezo.setText(koltseg);
            atnevez.setEnabled(true);
            el.kijelolt = true;
        }
    }
    
     public void futasUtaniTiltas(){ //inaktívvá tesz minden gombot és szövegmezőt, kivéve a sebessegmezo-t és a beallit gombot
        futtatas.setEnabled(false);
        lepesfuttatas.setEnabled(false);
        atnevez.setEnabled(false);
        atnevezmezo.setEnabled(false);
    }
    
    public void mindenLetiltva(){   //inaktívvá tesz minden gombot és szövegmezőt
        futtatas.setEnabled(false);
        lepesfuttatas.setEnabled(false);
        atnevez.setEnabled(false);
        beallit.setEnabled(false);
        sebessegmezo.setEnabled(false);
        atnevezmezo.setEnabled(false);
    }
    
    public void mindenEngedelyezve(){   //aktívvá tesz minden gombot és szövegmezőt
        futtatas.setEnabled(true);
        lepesfuttatas.setEnabled(true);
        atnevez.setEnabled(true);
        beallit.setEnabled(true);
        sebessegmezo.setEnabled(true);
        atnevezmezo.setEnabled(true);
    }

    public void actionPerformed(ActionEvent e){
        Object button = e.getSource();
        
        if(button == kilepes){  //Kilépés gombra kattintva
            System.exit(0);
        }
        
        if(button == csucs){    //ha a csúcsmód van kiválasztva
            adatokSzerkmod.setText("Szerkesztésmód: Csúcs"); 
            atnevezmezo.setEnabled(false);
            atnevez.setEnabled(false);
            sebessegmezo.setEnabled(true);
            beallit.setEnabled(true);
            kijeloltElKijeloltsegTorles(); 
            panel.repaint();
        }
        
        if(button == el){   //ha az élmód van kiválasztva
            adatokSzerkmod.setText("Szerkesztésmód: Él");
            atnevezmezo.setEnabled(false);
            atnevez.setEnabled(false);
            lepesfuttatas.setEnabled(false);
            futtatas.setEnabled(false);           
            sebessegmezo.setEnabled(true);
            beallit.setEnabled(true);
            kijeloltCsucsKijeloltsegTorles();  
            panel.repaint();
        }
        
        if(button == torles){   //ha a törlésmód van kiválasztva  
            adatokSzerkmod.setText("Szerkesztésmód: Törlés");
            kijeloltCsucsKijeloltsegTorles();
            kijeloltElKijeloltsegTorles();          
            mindenLetiltva(); 
            panel.repaint();
        }
        
        if(button == haszn){
            String msg = "Csúcsmódban: \n"
                + "     -a panelre kattintva új csúcs keletkezik\n"
                + "     -egy meglévő csúcsra kattintva a csúcs lesz a kijelölt csúcs (színe piros lesz) és ez lesz a startcsúcs is,\n"
                + "       azaz ebből a csúcsból indulnak el az algoritmusok\n"
                + "Élmódban: \n"
                + "     -egy csúcsra kattintva és az egér folyamatos lenyomása, majd áthúzása egy másik csúcsra\n"
                + "       folyamat után, egy él jön létre a két csúcs között\n"
                + "     -egy meglévő élre történő kattintáskor az él lesz a kijelölt él (színe piros lesz)\n"
                + "Törlésmódban: \n"
                + "     -egy csúcsra kattintva törlődik a csúcs és a hozzá tartozó összes él is\n"
                + "     -egy élre kattintva törlődik az él\n"
                + "Átnevez gomb: \n"
                + "     -lenyomásakor az előtte lévő szövegdoboz tartalma lesz a kijelölt él költsége vagy a kijelölt csúcs címkéje\n"
                + "Beállít gomb: \n"
                + "     -lenyomásakor az előtte lévő szövegdoboz tartalma lesz(ez lehet tört is, 2 tizedesjegyig) a Gráfalgoritmus menüben\n"
                + "       kiválasztott algoritmus lépésenkénti futtatásának a sebessége(másodpercben)\n"
                + "Futtatás gomb: \n"
                + "     -lenyomásakor lefut a Gráfalgoritmus menüben kiválasztott algoritmus\n"
                + "Futtatás lépésenként: \n"
                + "     -lenyomásakor lépésekre bontva fut le a Gráfalgoritmus menüben kiválasztott algoritmus\n"
                + "A Futtatás és a Futtatás lépésenként gombok csak akkor lesznek aktívak, ha van kijelölt, azaz startcsúcs.\n";
            JOptionPane optionPane = new JOptionPane();
            optionPane.setMessage(msg);
            optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog = optionPane.createDialog(null, "Használati útmutató");
            dialog.setVisible(true);
        }
        
        if(button == szelBej){
            adatokAlg.setText("Algoritmus: Szélességi bejárás");
            adatokAlgJell.setText("Jellemzés: Szélességi bejárás egy forrásból");
            panel.repaint();
        }
        
        if(button == melyBej){
            adatokAlg.setText("Algoritmus: Mélységi bejárás");
            adatokAlgJell.setText("Jellemzés: Mélységi bejárás egy forrásból");
            panel.repaint();
        }
        
        if(button == dijAlg){
            adatokAlg.setText("Algoritmus: Dijkstra-algoritmus");
            adatokAlgJell.setText("Jellemzés: Legrövidebb út egy forrásból");
            panel.repaint();
        }   
        
        if(button == bellFordAlg){
            adatokAlg.setText("Algoritmus: Bellman-Ford-algoritmus");
            adatokAlgJell.setText("Jellemzés: Legrövidebb út egy forrásból");
            panel.repaint();
        }
        
        if(button == krusAlg){
            adatokAlg.setText("Algoritmus: Kruskal-algoritmus");
            adatokAlgJell.setText("Jellemzés: Minimális költségű feszítőfa");
            panel.repaint();
        }       
        
        if(button == atnevez && kijeloltCsucs != null && csucsSelected()){ //Átnevez gombra kattintva, ha van kijelölve csúcs
            kijeloltCsucs.cimke = atnevezmezo.getText();    //a csúcs cimkéjét átírja az átnevezés mezőben lébő szövegre
            panel.repaint();			
        }
        
        if(button == beallit){  //Beállít gombra kattintva
            if(sebessegmezo.getText().substring(sebessegmezo.getText().length()-1).equals(".")){    //ha a sebességmezőben .-ra végződne a beírt sebesség
                sebessegmezo.setText(sebessegmezo.getText().substring(0, sebessegmezo.getText().length()-1));   //kitörli a .-ot
            }
            sebesseg = sebessegmezo.getText();  //beállítja a sebességet a sebesség mezőben lévő értékre
            aktSeb = Double.parseDouble(sebesseg);
            adatokSeb.setText("Sebesség: " + sebesseg);
            panel.repaint();
        }
        
        if(button == atnevez && kijeloltEl != null && elSelected()){    //Átnevez gombra kattintva, ha van kijelölve él és élmódban vagyunk
            panel.getGraf().setKoltseg(kijeloltEl, Integer.parseInt(atnevezmezo.getText()));    //átírja a kijelölt él költségét az átnevez mezőben lévő értékre
            panel.repaint();
        }
        
        if(krusAlgSelected() && button == futtatas){    //Futtatás gombra kattintva, ha a Kruskal-algoritmus van kiválasztva
            panel.getGraf().kruskalAlgoritmus();    //elindítja a Kruskal-algoritmust		
            panel.repaint();
            JOptionPane.showMessageDialog(this, "Befejeződött a Kruskal-algoritmus!");			
            panel.getGraf().jelolesTorles(panel.getGraf().getOsszesEl());
            panel.getGraf().feherre();
            kijeloltCsucsKijeloltsegTorles();
            futasUtaniTiltas();
            panel.repaint();
        }
        
        if(krusAlgSelected() && button == lepesfuttatas){   //Futtatás lépésenként gombra kattintva, ha a Kruskal-algoritmus van kiválasztva
            class RemindTask extends TimerTask {						
                public void run(){
                    mindenLetiltva();
                    if(panel.getGraf().kruskalAlgoritmusLepesenkent()){ //ha véget ért a Kruskal-algoritmus lépésenként	
                        panel.repaint();
                        JOptionPane.showMessageDialog(gui, "Befejeződött a Kruskal-algoritmus!");
                        panel.getGraf().feherre();                      
                        panel.getGraf().jelolesTorles(panel.getGraf().getOsszesEl());
                        mindenEngedelyezve();
                        this.cancel();
                        timer.purge();
                    }
                    kijeloltCsucsKijeloltsegTorles();
                    futasUtaniTiltas();
                    panel.repaint();
                }
            }
            timer.schedule(new RemindTask(), 0, (int)(1000*aktSeb));    //1 másodperc(1000 ms) lesz az alapértelmezett, ezt szorozza meg a sebbeségmezőben lévő értékkel
        }      
    }
}
