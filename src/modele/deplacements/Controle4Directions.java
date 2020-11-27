package modele.deplacements;

import modele.plateau.Corde;
import modele.plateau.Entite;

import modele.plateau.EntiteDynamique;
import modele.plateau.Heros;

/**
 * Controle4Directions permet d'appliquer une direction (connexion avec le clavier) à un ensemble d'entités dynamiques
 */
public class Controle4Directions extends RealisateurDeDeplacement {
    private Direction directionCourante;
    // Design pattern singleton
    private static Controle4Directions c3d;

    public static Controle4Directions getInstance() {
        if (c3d == null) {
            c3d = new Controle4Directions();
        }
        return c3d;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null)
                switch (directionCourante) {
                    case gauche, droite:
                        if (e.avancerDirectionChoisie(directionCourante))
                            ret = true;
                        break;

                    case haut:
                        Entite eBas = e.regarderDansLaDirection(Direction.bas);
                        if (eBas != null && (eBas.peutServirDeSupport() || eBas.peutPermettreDeMonterDescendre())) {
                            if (e.avancerDirectionChoisie(Direction.haut))
                                ret = true;
                        }
                        break;

                    case bas:
                        eBas = e.regarderDansLaDirection(Direction.bas);
                        if(!eBas.peutServirDeSupport()) {
                            if (e.avancerDirectionChoisie(Direction.bas))
                                ret = true;
                        }
                        break;
                }
        }
        Heros h = (Heros) lstEntitesDynamiques.get(0);
        h.setEstSurCorde(h.getEntitePrecedente() instanceof Corde);

        return ret;
    }

    public void resetDirection() {
        directionCourante = null;
    }
}
