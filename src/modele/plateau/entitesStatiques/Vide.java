package modele.plateau.entitesStatiques;

import modele.plateau.EntiteStatique;
import modele.plateau.Jeu;

public class Vide extends EntiteStatique {
    public Vide(Jeu _jeu) { super(_jeu); }

    public boolean peutServirDeSupport() { return false; }
}