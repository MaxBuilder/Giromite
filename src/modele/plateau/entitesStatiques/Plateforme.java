package modele.plateau.entitesStatiques;

import modele.plateau.EntiteStatique;
import modele.plateau.Jeu;

public class Plateforme extends EntiteStatique {
    private final TypePlateforme type;

    public Plateforme(Jeu _jeu, TypePlateforme _type) {
        super(_jeu);
        type = _type;
    }

    public TypePlateforme getType() { return type; }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
}
