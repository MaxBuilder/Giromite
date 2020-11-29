package modele.deplacements;

import modele.plateau.entites_dynamiques.CaseColonne;
import modele.plateau.entites_dynamiques.Colonne;
import modele.plateau.entites_dynamiques.CouleurColonne;
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

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Colonne col = (Colonne) e;
            if(col.getCouleur() == CouleurColonne.bleue) { // Colonne bleue
                if(directionBleue) { // Monter
                    for(CaseColonne c : col.cases) {
                        if(!c.regarderDansLaDirection(Direction.haut).peutServirDeSupport())
                            c.avancerDirectionChoisie(Direction.haut);
                        else break;
                    }
                }
                else { // Descendre
                    for(int i = col.cases.size() - 1 ; i >= 0 ; i--) {
                        if(!col.cases.get(i).regarderDansLaDirection(Direction.bas).peutServirDeSupport()) {
                            col.cases.get(i).avancerDirectionChoisie(Direction.bas);
                        }
                        else break;
                    }
                }
            }
            else { // Colonne rouge
                if(directionRouge) { // Monter
                    for(CaseColonne c : col.cases) {
                        if(!c.regarderDansLaDirection(Direction.haut).peutServirDeSupport())
                            c.avancerDirectionChoisie(Direction.haut);
                        else break;
                    }
                }
                else { // Descendre
                    for(int i = col.cases.size() - 1 ; i >= 0 ; i--) {
                        if(!col.cases.get(i).regarderDansLaDirection(Direction.bas).peutServirDeSupport()) {
                            col.cases.get(i).avancerDirectionChoisie(Direction.bas);
                        }
                        else break;
                    }
                }
            }
        }

        return true;
    }
}
