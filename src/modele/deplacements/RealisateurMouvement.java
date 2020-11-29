package modele.deplacements;

import modele.plateau.*;
import modele.plateau.entites_dynamiques.Bot;
import modele.plateau.entites_dynamiques.Heros;
import modele.plateau.entites_statiques.Bombe;
import modele.plateau.entites_statiques.Corde;
import modele.plateau.entites_statiques.Vide;

/**
 * Controle4Directions permet d'appliquer une direction (connexion avec le clavier) à un ensemble d'entités dynamiques
 */
public class RealisateurMouvement extends RealisateurDeDeplacement {
    private Direction directionCourante;
    // Design pattern singleton
    private static RealisateurMouvement c4d;

    public static RealisateurMouvement getInstance() {
        if (c4d == null) {
            c4d = new RealisateurMouvement();
        }
        return c4d;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }
    public Direction getDirectionCourante() { return directionCourante; }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            // Gestion des collions :
            if(e.getEntitePrecedente() instanceof Bombe) {
                e.setEntitePrecedente(new Vide(e.getJeu()));
            }
            if(e.regarderDansLaDirection(Direction.gauche) instanceof Bot || e.regarderDansLaDirection(Direction.droite) instanceof Bot)
                ((Heros) e).setEstMort(true);

            // Mouvement :
            if (directionCourante != null && !((Heros) e).getEstMort()) {
                switch (directionCourante) {
                    case gauche, droite:
                        if (e.regarderDansLaDirection(Direction.bas).peutServirDeSupport() || ((Heros) e).getEstSurCorde()) {
                            e.avancerDirectionChoisie(directionCourante);
                            ret = true;
                        }
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
                        if (!eBas.peutServirDeSupport()) {
                            if (e.avancerDirectionChoisie(Direction.bas))
                                ret = true;
                        }
                        break;
                }
            }
            ((Heros) e).setEstSurCorde(e.getEntitePrecedente() instanceof Corde);
        }

        return ret;
    }

    public void resetDirection() {
        directionCourante = null;
    }
}
