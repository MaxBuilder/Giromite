package modele.deplacements;

import modele.plateau.entites_dynamiques.Bot;
import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.entites_statiques.Vide;

// Intelligence artficielle basée sur la perception immédiate de l'entité
public class RealisateurIA extends RealisateurDeDeplacement {
    protected boolean realiserDeplacement() {
        for (EntiteDynamique e : lstEntitesDynamiques) {
            Bot bot = (Bot) e;

            if(bot.getCompteur() < 2) {
                bot.incrementerCompteur();
                break;
            }
            else bot.razCompteur();

            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            Entite eHaut = e.regarderDansLaDirection(Direction.haut);
            Entite eGauche = e.regarderDansLaDirection(Direction.gauche);
            Entite eDroite = e.regarderDansLaDirection(Direction.droite);
            Entite eBasGauche = e.regarderDansLaDirection(Direction.basGauche);
            Entite eBasDroite = e.regarderDansLaDirection(Direction.basDroite);

            if(bot.getEntitePrecedente().peutPermettreDeMonterDescendre() && !(bot.getEntitePrecedente() instanceof Vide)) {
                // Gestion des évènements liés à la corde :
                if(bot.getEstSurCorde()) {
                    if(eBas.peutServirDeSupport()) { // Descente de la corde (arrivé en bas)
                        bot.changerEstSurCorde();
                        bot.avancerDirectionChoisie(bot.getDirectionHorizontale() ? Direction.gauche : Direction.droite);
                        break;
                    }
                    if(eBasGauche.peutServirDeSupport() && !eGauche.peutServirDeSupport() && Math.random() > 0.5) { // Descente à gauche (intermédiaire)
                        bot.changerEstSurCorde();
                        bot.changerDirectionHorizontale(true);
                        bot.avancerDirectionChoisie(Direction.gauche);
                        break;
                    }
                    if(eBasDroite.peutServirDeSupport() && !eDroite.peutServirDeSupport() && Math.random() > 0.5) { // Descente à droite (intermédiaire)
                        bot.changerEstSurCorde();
                        bot.changerDirectionHorizontale(false);
                        bot.avancerDirectionChoisie(Direction.droite);
                        break;
                    }
                }
                if(!bot.getEstSurCorde() && Math.random() > 0.5) { // Mise sur la corde et choix de la direction (haut/bas)
                    bot.changerEstSurCorde();
                    if(!eBas.peutServirDeSupport())
                        bot.changerDirectionVerticale(Math.random() > 0.5);
                    else bot.changerDirectionVerticale(true);
                }
                if(eHaut.peutServirDeSupport()) // Si arrivé tout en haut de la corde, inversion de la direction
                    bot.changerDirectionVerticale();
            }

            // Gestion des évènements liés à l'environnement : changement de direction si risque de chute ou obstacle
            if((bot.getDirectionHorizontale() && eBasGauche instanceof Vide) || (!bot.getDirectionHorizontale() && eBasDroite instanceof Vide) || eGauche.peutServirDeSupport() || eDroite.peutServirDeSupport()) {
                bot.changerDirectionHorizontale();
            }

            // Déplacement du bot :
            if (bot.getEstSurCorde())
                bot.avancerDirectionChoisie(bot.getDirectionVerticale() ? Direction.haut : Direction.bas);
            else bot.avancerDirectionChoisie(bot.getDirectionHorizontale() ? Direction.gauche : Direction.droite);
        }
        return true;
    }
}