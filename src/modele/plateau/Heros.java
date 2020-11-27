package modele.plateau;

/**
 * Héros du jeu
 */
public class Heros extends EntiteDynamique {
    private boolean estSurCorde;

    public Heros(Jeu _jeu) {
        super(_jeu);
        estSurCorde = false;
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean getEstSurCorde() { return estSurCorde; }
    public void setEstSurCorde(boolean val) { estSurCorde = val; }
}
