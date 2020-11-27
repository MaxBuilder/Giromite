package modele.plateau;

public class Vide extends EntiteStatique {
    public Vide(Jeu _jeu) { super(_jeu); }

    public boolean peutServirDeSupport() { return false; }
}