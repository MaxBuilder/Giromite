package modele.plateau;

public class Plateforme extends EntiteStatique {
    private final TypePlateforme type;

    public Plateforme(Jeu _jeu, TypePlateforme _type) {
        super(_jeu);
        type = _type;
    }

    public TypePlateforme getType() { return type; }
}
