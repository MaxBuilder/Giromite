package modele.plateau;

import modele.deplacements.RealisateurMouvement;
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

    public Gameplay(Jeu _jeu) {
        jeu = _jeu;
        listeNiveau = new ArrayList<>();
        niveauActuel = -1;

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

    public boolean verifier(boolean dead) {
        if(dead) {
            resetNiveau();
            return true;
        }
        else {
            if(nombreBombeActuel == nombreBombeTotal)
                changerNiveau();
        }
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
}
