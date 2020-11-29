package modele.plateau.entitesDynamiques;

import modele.plateau.EntiteDynamique;
import modele.plateau.Jeu;

import java.util.ArrayList;

public class Colonne extends EntiteDynamique {
    private final CouleurColonne couleur;
    private final int taille;
    public final ArrayList<CaseColonne> cases;

    public Colonne(Jeu _jeu, CouleurColonne _couleur) {
        super(_jeu);
        couleur = _couleur;
        taille = 0;
        cases = new ArrayList<>();
    }

    public CouleurColonne getCouleur() { return couleur; }
    public int getTaille() { return taille; }
    public void ajouterColonne(CaseColonne _case) {
        cases.add(_case);
    }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
}
