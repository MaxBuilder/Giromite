package modele.plateau.entites_dynamiques;

import modele.plateau.EntiteDynamique;
import modele.plateau.Jeu;

import java.util.ArrayList;

public class Colonne extends EntiteDynamique {
    private final CouleurColonne couleur;
    public final ArrayList<CaseColonne> cases;

    public Colonne(Jeu _jeu, CouleurColonne _couleur) {
        super(_jeu);
        couleur = _couleur;
        cases = new ArrayList<>();
    }

    public CouleurColonne getCouleur() { return couleur; }
    public void ajouterColonne(CaseColonne _case) {
        cases.add(_case);
    }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
}
