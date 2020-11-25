
import VueControleur.VueControleurGyromite;
import modele.plateau.Jeu;

/*
    Projet Gyromine
    Pierre DAMEZ-FONTAINE
    Raphaël RICHARD
    LIFAP7 - 2020
 */


public class Main {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        
        VueControleurGyromite vc = new VueControleurGyromite(jeu);

        jeu.getOrdonnanceur().addObserver(vc);
        
        vc.setVisible(true);
        jeu.start(300);
    }
}
