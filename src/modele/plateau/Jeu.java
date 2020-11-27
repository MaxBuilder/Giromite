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

    private Heros hector;
    private Bot smick;

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
    
    public Heros getHector() {
        return hector;
    }
    
    public Bot getSmick() {
    	return smick;
    }
    
    private void initialisationDesEntites() { // A CHANGER (serialization)
        hector = new Heros(this);
        addEntite(hector, 2, 1);

        smick = new Bot(this);
        addEntite(smick, 8, 1);

        // Component gravité
        Gravite g = new Gravite();
        g.addEntiteDynamique(hector);
        g.addEntiteDynamique(smick);
        ordonnanceur.add(g);

        // Mouvement des bots
        IA ia = new IA();
        ia.addEntiteDynamique(smick);
        ordonnanceur.add(ia);

        // Movement des personnages
        Controle4Directions.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Controle4Directions.getInstance());

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
        addEntite(new Plateforme(this, TypePlateforme.horizontal), 6, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontal), 7, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontal), 8, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontal), 9, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontal), 10, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontal), 11, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontal), 12, 5);
        addEntite(new Plateforme(this, TypePlateforme.horizontal), 13, 5);
        addEntite(new Plateforme(this, TypePlateforme.vertical), 13, 4);
        addEntite(new Plateforme(this, TypePlateforme.vertical), 13, 3);
        addEntite(new Plateforme(this, TypePlateforme.vertical), 10, 6);
        addEntite(new Plateforme(this, TypePlateforme.vertical), 10, 7);
        addEntite(new Plateforme(this, TypePlateforme.vertical), 10, 8);
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
    }

    private void addEntite(Entite e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }
    
    /** Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
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
        };
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, EntiteDynamique e) {
        grilleEntites[pCourant.x][pCourant.y] = e.getEntitePrecedente();
        e.setEntitePrecedente(grilleEntites[pCible.x][pCible.y]);
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    /** Indique si p est contenu dans la grille
     */
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
