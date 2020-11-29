/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.*;

import java.awt.Point;
import java.util.HashMap;

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu {

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    // Compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private final HashMap<Entite, Integer> cmptDeplH = new HashMap<>();
    private final HashMap<Entite, Integer> cmptDeplV = new HashMap<>();

    private final HashMap<Entite, Point> map = new HashMap<>(); // permet de récupérer la position d'une entité à partir de sa référence
    private final Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private final Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu() {
        initialisationDesEntites();
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    private void initialisationDesEntites() { // A CHANGER (serialization)
        Heros hector = new Heros(this);
        addEntite(hector, 2, 1);

        Bot smick = new Bot(this);
        addEntite(smick, 18, 5);

        CaseColonne case1 = new CaseColonne(this, CouleurColonne.bleue, TypeColonne.haut);
        CaseColonne case2 = new CaseColonne(this, CouleurColonne.bleue, TypeColonne.inter);
        CaseColonne case3 = new CaseColonne(this, CouleurColonne.bleue, TypeColonne.inter);
        CaseColonne case4 = new CaseColonne(this, CouleurColonne.bleue, TypeColonne.bas);
        Colonne colonne1 = new Colonne(this, CouleurColonne.bleue);
        colonne1.ajouterColonne(case1);
        colonne1.ajouterColonne(case2);
        colonne1.ajouterColonne(case3);
        colonne1.ajouterColonne(case4);

        // Component gravité
        RealisateurGravite g = new RealisateurGravite();
        g.addEntiteDynamique(hector);
        g.addEntiteDynamique(smick);
        ordonnanceur.add(g);

        // Mouvement des bots
        RealisateurIA ia = new RealisateurIA();
        //ia.addEntiteDynamique(smick); // Désactivé pour le moment
        ordonnanceur.add(ia);

        // Mouvement du personnage
        RealisateurDeplacement.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(RealisateurDeplacement.getInstance());

        // Mouvement des colonnes
        RealisateurColonnes c = new RealisateurColonnes();
        c.addEntiteDynamique(colonne1);
        ordonnanceur.add(c);

        // Placement des entités de la map (à changer) :
        for(int x = 0 ; x < 20 ; x++) {
            for(int y = 0 ; y < 10 ; y++)
                addEntite(new Vide(this), x, y);
        }

        // Murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addEntite(new Mur(this), x, 0);
            addEntite(new Mur(this), x, 9);
        }

        // Murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), 19, y);
        }

        // Reste de la map (Plateformes etc)
        addEntite(new Plateforme(this, TypePlateforme.horizontale), 6, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontale), 7, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontale), 8, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontale), 9, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontale), 10, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontale), 11, 5);
        addEntite(new Plateforme(this, TypePlateforme.supportColonneGauche), 12, 5);
        //addEntite(new Plateforme(this, TypePlateforme.horizontale), 13, 5);
        addEntite(new Plateforme(this, TypePlateforme.supportColonneDroite), 14, 5);
        //addEntite(new Plateforme(this, TypePlateforme.horizontale), 12, 5);
        //addEntite(new Plateforme(this, TypePlateforme.horizontale), 13, 5);
        //addEntite(new Plateforme(this, TypePlateforme.verticale), 13, 4);
        //addEntite(new Plateforme(this, TypePlateforme.verticale), 13, 3);
        addEntite(new Plateforme(this, TypePlateforme.verticale), 10, 6);
        addEntite(new Plateforme(this, TypePlateforme.verticale), 10, 7);
        addEntite(new Plateforme(this, TypePlateforme.verticale), 10, 8);
        addEntite(new Plateforme(this, TypePlateforme.horizontale), 13, 1);
        addEntite(case1, 13, 2);
        addEntite(case2, 13, 3);
        addEntite(case3, 13, 4);
        addEntite(case4, 13, 5);
        addEntite(new Corde(this), 5, 1);
        addEntite(new Corde(this), 5, 2);
        addEntite(new Corde(this), 5, 3);
        addEntite(new Corde(this), 5, 4);
        addEntite(new Corde(this), 5, 5);
        addEntite(new Corde(this), 5, 6);
        addEntite(new Corde(this), 5, 7);
        addEntite(new Corde(this), 5, 8);
        addEntite(new Corde(this), 9, 6);
        addEntite(new Corde(this), 9, 7);
        addEntite(new Corde(this), 9, 8);
        addEntite(new Bombe(this), 4, 8);
    }

    private void addEntite(Entite e, int x, int y) {
        if(e instanceof EntiteDynamique)
            ((EntiteDynamique) e).setEntitePrecedente(grilleEntites[x][y]);
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }

    public boolean deplacerEntite(EntiteDynamique e, Direction d) {
        boolean retour = false;
        
        Point pCourant = map.get(e);
        
        Point pCible = calculerPointCible(pCourant, d);
        
        if (contenuDansGrille(pCible) && (objetALaPosition(pCible) == null || !objetALaPosition(pCible).peutServirDeSupport())) { // Adapter
            // Compteur de déplacements
            switch (d) {
                case bas, haut:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);
                        retour = true;
                    }
                    break;
                case gauche, droite:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;
                    }
                    break;
            }
        }

        if (retour)
            deplacerEntite(pCourant, pCible, e);

        return retour;
    }

    private Point calculerPointCible(Point pCourant, Direction d) {
        return switch (d) {
            case haut -> new Point(pCourant.x, pCourant.y - 1);
            case bas -> new Point(pCourant.x, pCourant.y + 1);
            case gauche -> new Point(pCourant.x - 1, pCourant.y);
            case droite -> new Point(pCourant.x + 1, pCourant.y);
            case basGauche -> new Point(pCourant.x - 1, pCourant.y + 1);
            case basDroite -> new Point(pCourant.x + 1, pCourant.y + 1);
            default -> new Point(pCourant.x, pCourant.y);
        };
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, EntiteDynamique e) {
        grilleEntites[pCourant.x][pCourant.y] = e.getEntitePrecedente();
        e.setEntitePrecedente(grilleEntites[pCible.x][pCible.y]);
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }

    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }
    
    private Entite objetALaPosition(Point p) {
        Entite retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
}
