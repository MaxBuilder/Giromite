package modele.deplacements;

import modele.plateau.Bot;
import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;
import modele.plateau.Vide;

// Intelligence artficelle basée sur la perception immédiate de l'entité
public class IA extends RealisateurDeDeplacement {
    protected boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            Bot bot = (Bot) e;
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            Entite eHaut = e.regarderDansLaDirection(Direction.haut);
            Entite eGauche = e.regarderDansLaDirection(Direction.gauche);
            Entite eDroite = e.regarderDansLaDirection(Direction.droite);
            Entite eBasGauche = e.regarderDansLaDirection(Direction.basGauche);
            Entite eBasDroite = e.regarderDansLaDirection(Direction.basDroite);

            // Comportement 1 et 2 : ne pas tomber des plateformes et changer de direction en cas de colision
            // Comportement 3 : grimper aux cordes lorsque l'entité en a l'occasion
            if(bot.getEntitePrecedente().peutPermettreDeMonterDescendre() && !(bot.getEntitePrecedente() instanceof Vide)) {
                if(bot.getEstSurCorde()) {
                    if(eBas.peutServirDeSupport()) {
                        bot.changerEstSurCorde();
                        bot.avancerDirectionChoisie(bot.getDirectionHorizontale() ? Direction.gauche : Direction.droite);
                        break;
                    }
                    if(eBasGauche.peutServirDeSupport() && !eGauche.peutServirDeSupport() && Math.random() > 0.5) {
                        bot.changerEstSurCorde();
                        bot.changerDirectionHorizontale(true);
                        bot.avancerDirectionChoisie(Direction.gauche);
                        break;
                    }
                    if(eBasDroite.peutServirDeSupport() && !eDroite.peutServirDeSupport() && Math.random() > 0.5) {
                        bot.changerEstSurCorde();
                        bot.changerDirectionHorizontale(false);
                        bot.avancerDirectionChoisie(Direction.droite);
                        break;
                    }
                }
                if(!bot.getEstSurCorde() && Math.random() > 0.5) {
                    bot.changerEstSurCorde();
                    if(!eBas.peutServirDeSupport())
                        bot.changerDirectionVerticale(Math.random() > 0.5);
                    else bot.changerDirectionVerticale(true);
                }
                if(eHaut.peutServirDeSupport())
                    bot.changerDirectionVerticale();
            }

            if((bot.getDirectionHorizontale() && eBasGauche instanceof Vide) || (!bot.getDirectionHorizontale() && eBasDroite instanceof Vide) || eGauche.peutServirDeSupport() || eDroite.peutServirDeSupport()) {
                bot.changerDirectionHorizontale();
                ret = true;
            }

            // Déplacement du bot
            if (bot.getEstSurCorde())
                bot.avancerDirectionChoisie(bot.getDirectionVerticale() ? Direction.haut : Direction.bas);
            else bot.avancerDirectionChoisie(bot.getDirectionHorizontale() ? Direction.gauche : Direction.droite);
        }
        return ret;
    }
}