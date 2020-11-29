package modele.plateau.entites_dynamiques;

import modele.plateau.EntiteDynamique;
import modele.plateau.Jeu;

/**
 * Héros du jeu
 */
public class Heros extends EntiteDynamique {
    private boolean estSurCorde;
    private boolean estMort;

    public Heros(Jeu _jeu) {
        super(_jeu);
        estSurCorde = false;
        estMort = false;
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean getEstSurCorde() { return estSurCorde; }
    public void setEstSurCorde(boolean val) { estSurCorde = val; }

    public boolean getEstMort() { return estMort; }
    public void setEstMort(boolean val) { estMort = val; }
}
