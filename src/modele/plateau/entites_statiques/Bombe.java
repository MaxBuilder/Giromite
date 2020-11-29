package modele.plateau.entites_statiques;

import modele.plateau.EntiteStatique;
import modele.plateau.Jeu;

public class Bombe extends EntiteStatique {
    public Bombe(Jeu _jeu) { super(_jeu); }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
}
