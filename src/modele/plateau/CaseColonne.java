package modele.plateau;

public class CaseColonne extends EntiteDynamique {
    private final CouleurColonne color;
    private final TypeColonne type;

    public CaseColonne(Jeu _jeu, CouleurColonne _color, TypeColonne _type) {
        super(_jeu);
        color = _color;
        type = _type;
    }

    public CouleurColonne getCouleur() { return color; }
    public TypeColonne getType() { return type; }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; };
}
