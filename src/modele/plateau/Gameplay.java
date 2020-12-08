package modele.plateau;

import modele.plateau.entites_statiques.Bombe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Gameplay {
    private final Jeu jeu;
    private final ArrayList<String> listeNiveau;
    private final int nbNiveau;
    private int niveauActuel;
    private int nombreBombeTotal;
    private int nombreBombeActuel;
    private int vies;
    private int score;
    private int temps;

    public Gameplay(Jeu _jeu) {
        jeu = _jeu;
        listeNiveau = new ArrayList<>();
        niveauActuel = 0;
        temps = 999;
        score = 0;
        vies = 5;

        // Détermination du nombre de niveaux et chargement dans le tableau
        File fichier = new File("data/niveaux/niveaux.txt");
        Scanner scanner = null;

        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            System.out.println("Fichier de niveau introuvable");
            System.exit(0);
        }

        while(scanner.hasNext()) {
            ajouterNiveau(scanner.next());
        }
        nbNiveau = listeNiveau.size() - 1;
        System.out.println("Fin parsing niveaux, " + (nbNiveau + 1) + " niveaux trouvés");
    }

    public void ajouterNiveau(String nomNiveau) {
        listeNiveau.add("data/niveaux/" + nomNiveau + ".txt");
    }

    public void initNiveau() {
        System.out.println("Chargement du 1er niveau");
        jeu.initialisationDesEntites(listeNiveau.get(0));
        initNombreBombes();
        nombreBombeActuel = 0;
    }

    public boolean verifier(boolean dead) {
        if(dead && vies > 0) {
            resetNiveau();
            vies--;
            return true;
        }
        else {
            if(nombreBombeActuel == nombreBombeTotal) {
                changerNiveau();
                return true;
            }
        }
        if(vies == 0)
            System.exit(0);
        decrementerTemps();
        return false;
    }

    public void changerNiveau() {
        if(niveauActuel < nbNiveau)
        {
            System.out.println("Niveau suivant");
            niveauActuel++;
            jeu.viderEntites();
            jeu.initialisationDesEntites(listeNiveau.get(niveauActuel));
            initNombreBombes();
            nombreBombeActuel = 0;
        }
        else System.exit(0);
    }

    public void resetNiveau() {
        jeu.viderEntites();
        jeu.initialisationDesEntites(listeNiveau.get(niveauActuel));
        nombreBombeActuel = 0;
    }

    public void initNombreBombes() {
        nombreBombeTotal = 0;
        for(int y = 0; y < Jeu.SIZE_Y; y++) {
            for (int x = 0; x < Jeu.SIZE_X; x++) {
                if(jeu.getGrille()[x][y] instanceof Bombe)
                    nombreBombeTotal++;
            }
        }
    }

    public void recupererBombe() {
        nombreBombeActuel++;
    }

    public int getScore() { return score; }
    public int getTemps() { return temps; }
    public void incrementerScore(int val) { score += val; }
    public void decrementerTemps() { temps--; }
}
