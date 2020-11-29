/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau.entites_dynamiques;

import modele.plateau.EntiteDynamique;
import modele.plateau.Jeu;
import modele.plateau.entites_statiques.Vide;

/**
 * Ennemis (Smicks)
 */
public class Bot extends EntiteDynamique {
    public Bot(Jeu _jeu) {
        super(_jeu);
        directionV = true;
        directionH = false;
        estSurCorde = false;
        setEntitePrecedente(new Vide(_jeu));
    }

    // Fonctions / variables de changement de direction
    private boolean directionH; // true -> gauche, false -> droite
    private boolean directionV; // true -> monter, false -> descendre
    private boolean estSurCorde;
    private int compteurMouvement;

    public void changerDirectionVerticale() { directionV = !directionV; }
    public void changerDirectionVerticale(boolean val) { directionV = val; }
    public void changerDirectionHorizontale() { directionH = !directionH; }
    public void changerDirectionHorizontale(boolean val) { directionH = val; }
    public void changerEstSurCorde() { estSurCorde = !estSurCorde; }
    public void incrementerCompteur() { compteurMouvement++; }
    public void razCompteur() { compteurMouvement = 0; }
    public int getCompteur() { return compteurMouvement; }

    public boolean getDirectionVerticale() { return directionV; }
    public boolean getDirectionHorizontale() { return directionH; }
    public boolean getEstSurCorde() { return estSurCorde; }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
}
