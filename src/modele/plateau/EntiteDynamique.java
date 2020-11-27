package modele.plateau;

import modele.deplacements.Direction;

/**
 * Entités amenées à bouger (colonnes, ennemis)
 */
public abstract class EntiteDynamique extends Entite {
    private Entite entitePrecedente;

    public EntiteDynamique(Jeu _jeu) {
        super(_jeu);
        entitePrecedente = null;
    }

    public boolean avancerDirectionChoisie(Direction d) {
        return jeu.deplacerEntite(this, d);
    }
    public Entite regarderDansLaDirection(Direction d) {return jeu.regarderDansLaDirection(this, d);}
    public Entite getEntitePrecedente() { return entitePrecedente; }
    public void setEntitePrecedente(Entite e) { entitePrecedente = e; }
}
