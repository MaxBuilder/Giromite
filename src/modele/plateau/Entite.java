package modele.plateau;

public abstract class Entite {
    protected Jeu jeu;
    
    public Entite(Jeu _jeu) {
        jeu = _jeu;
    }
    public Jeu getJeu() {
        return jeu;
    }
    
    public abstract boolean peutEtreEcrase();
    public abstract boolean peutServirDeSupport();
    public abstract boolean peutPermettreDeMonterDescendre();
}
