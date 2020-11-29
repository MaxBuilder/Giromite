/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.deplacements.*;
import modele.plateau.entites_dynamiques.*;
import modele.plateau.entites_statiques.*;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu {

    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    // Compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    //private final HashMap<Entite, Integer> cmptDeplH = new HashMap<>();
    //private final HashMap<Entite, Integer> cmptDeplV = new HashMap<>();
    private final HashMap<Entite, Integer> cmptDepl = new HashMap<>();

    private final HashMap<Entite, Point> map = new HashMap<>(); // permet de récupérer la position d'une entité à partir de sa référence
    private final Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité  d à partir de ses coordonnées

    private final Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu() {
        initialisationDesEntites();
    }

    public void resetCmptDepl() {
        //cmptDeplH.clear();
        //cmptDeplV.clear();
        cmptDepl.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    private void initialisationDesEntites() {
        // Ouverture fichier
        File fichier = new File("data/niveaux/niveau_test.txt");
        Scanner scanner = null;

        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            System.out.println("Fichier de niveau introuvable.");
            System.exit(0);
        }

        // Déclaration des réalisateurs de mouvement
        RealisateurGravite g = new RealisateurGravite();
        RealisateurIA ia = new RealisateurIA();
        RealisateurColonnes c = new RealisateurColonnes();

        // Table pour les colonnes
        HashMap<Integer, Colonne> TableColonne = new HashMap<>();

        // Traitement du fichier texte
        for(int y = 0 ; y < SIZE_Y ; y++) {
            for(int x = 0 ; x < SIZE_X ; x++) {
                String it = scanner.next();         // PE arranger les conditons par ordre d'occurence
                // Statiques
                if(it.equals("M"))
                    addEntite(new Mur(this), x, y);
                else if(it.equals("V"))
                    addEntite(new Vide(this), x, y);
                else if(it.equals("C"))
                    addEntite(new Corde(this), x, y);
                else if(it.equals("B"))
                    addEntite(new Bombe(this), x, y);
                else if(it.equals("PH"))
                    addEntite(new Plateforme(this, TypePlateforme.horizontale), x, y);
                else if(it.equals("PV"))
                    addEntite(new Plateforme(this, TypePlateforme.verticale), x, y);
                else if(it.equals("PG"))
                    addEntite(new Plateforme(this, TypePlateforme.supportColonneGauche), x, y);
                else if(it.equals("PD"))
                    addEntite(new Plateforme(this, TypePlateforme.supportColonneDroite), x, y);
                // Dynamiques
                else if(it.equals("H")) {
                    Heros hector = new Heros(this);
                    RealisateurMouvement.getInstance().addEntiteDynamique(hector);
                    g.addEntiteDynamique(hector);
                    addEntite(hector, x, y);
                }
                else if(it.equals("S")) {
                    Bot smick = new Bot(this);
                    ia.addEntiteDynamique(smick);
                    g.addEntiteDynamique(smick);
                    addEntite(smick, x, y);
                }
                else if(it.matches("C[BR][HBI][1-9]")) {
                    CouleurColonne couleur = null;
                    TypeColonne type = null;
                    if(it.matches("CB[HBI][1-9]"))
                        couleur = CouleurColonne.bleue;
                    else if(it.matches("CR[HBI][1-9]"))
                        couleur = CouleurColonne.rouge;
                    if(it.matches("C[BR]H[1-9]"))
                        type = TypeColonne.haut;
                    else if(it.matches("C[BR]I[1-9]"))
                        type = TypeColonne.inter;
                    else if(it.matches("C[BR]B[1-9]"))
                        type = TypeColonne.bas;
                    CaseColonne caseColonne = new CaseColonne(this, couleur, type); // Création de la case
                    addEntite(caseColonne, x, y);

                    // Ajout de la case à sa colonne si elle existe, sinon création
                    int numColonne = Character.getNumericValue(it.charAt(3));
                    if(TableColonne.get(numColonne) != null)
                        TableColonne.get(numColonne).ajouterColonne(caseColonne);
                    else {
                        TableColonne.put(numColonne, new Colonne(this, couleur));
                        TableColonne.get(numColonne).ajouterColonne(caseColonne);
                    }
                }

                // Mise de l'entité précédente à vide
                if(grilleEntites[x][y] instanceof EntiteDynamique)
                    ((EntiteDynamique) grilleEntites[x][y]).setEntitePrecedente(new Vide(this));
            }
        }

        // Ajout des colonnes à leur réalisateur
        TableColonne.forEach((id, col) -> c.addEntiteDynamique(col));

        // Ajout des réalisateurs à l'ordonnanceur
        ordonnanceur.add(g);
        ordonnanceur.add(ia);
        ordonnanceur.add(RealisateurMouvement.getInstance());
        ordonnanceur.add(c);
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
            /*switch (d) {
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
            }*/
            if (cmptDepl.get(e) == null) {
                cmptDepl.put(e, 1);
                retour = true;
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
        
        if (contenuDansGrille(p))
            retour = grilleEntites[p.x][p.y];
        
        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
}
