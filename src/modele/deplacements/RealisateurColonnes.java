package modele.deplacements;

import modele.plateau.entites_dynamiques.*;
import modele.plateau.EntiteDynamique;

public class RealisateurColonnes extends RealisateurDeDeplacement {
    boolean directionBleue, directionRouge;

    public RealisateurColonnes() {
        directionBleue = true;
        directionRouge = true;
    }

    protected boolean realiserDeplacement() {
        boolean ret = false;

        // Mise à jour des évènements
        Direction directionCourante = RealisateurMouvement.getInstance().getDirectionCourante();
        if (directionCourante != null) {
            switch (directionCourante) {
                case colonneBleue -> directionBleue = !directionBleue;
                case colonneRouge -> directionRouge = !directionRouge;
            }
        }

        for (EntiteDynamique e : lstEntitesDynamiques) { // Colonnes
            Colonne col = (Colonne) e;
            if(col.getCouleur() == CouleurColonne.bleue && directionBleue || col.getCouleur() == CouleurColonne.rouge && directionRouge) { // Monter
                for(CaseColonne c : col.cases) {
                    if (c.regarderDansLaDirection(Direction.haut).peutEtreEcrase()) {
                        EntiteDynamique entiteDynamique = (EntiteDynamique) c.regarderDansLaDirection(Direction.haut);
                        if (entiteDynamique.regarderDansLaDirection(Direction.haut).peutServirDeSupport())
                            e.getJeu().supprimerEntite(entiteDynamique);
                        else entiteDynamique.avancerDirectionChoisie(Direction.haut);
                        c.avancerDirectionChoisie(Direction.haut);
                    } else if (!c.regarderDansLaDirection(Direction.haut).peutServirDeSupport() && !(c.regarderDansLaDirection(Direction.haut) instanceof Heros))
                        c.avancerDirectionChoisie(Direction.haut);
                    else break;

                    ret = true;
                }
            }
            else { // Descendre
                for(int i = col.cases.size() - 1 ; i >= 0 ; i--) {
                    CaseColonne c = col.cases.get(i);
                    if (c.regarderDansLaDirection(Direction.bas).peutEtreEcrase()) {
                        EntiteDynamique entiteDynamique = (EntiteDynamique) c.regarderDansLaDirection(Direction.bas);
                        if(entiteDynamique.regarderDansLaDirection(Direction.bas).peutServirDeSupport())
                            e.getJeu().supprimerEntite(entiteDynamique);
                    }
                    else if(!col.cases.get(i).regarderDansLaDirection(Direction.bas).peutServirDeSupport()) {
                        col.cases.get(i).avancerDirectionChoisie(Direction.bas);
                    }
                    else break;

                    ret = true;
                }
            }
        }

        return ret;
    }
}
