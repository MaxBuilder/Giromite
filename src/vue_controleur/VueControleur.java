package vue_controleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import modele.deplacements.RealisateurMouvement;
import modele.deplacements.Direction;
import modele.plateau.*;
import modele.plateau.entites_dynamiques.*;
import modele.plateau.entites_statiques.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private final Jeu jeu;

    private final int tailleVueX = 20;
    private final int tailleVueY = 10;
    private int curseur;

    private JLabel[][] tabJLabel; // Cases graphiques

    // Icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoHeroCorde;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoPlateformeV;
    private ImageIcon icoPlateformeH;
    private ImageIcon icoSmick;
    private ImageIcon icoCorde;
    private ImageIcon icoBombe;
    private ImageIcon icoSupportColonneGauche;
    private ImageIcon icoSupportColonneDroite;
    private ImageIcon icoColonneBleue;
    private ImageIcon icoColonneRouge;
    private ImageIcon icoHautColonneBleue;
    private ImageIcon icoBasColonneBleue;
    private ImageIcon icoHautColonneRouge;
    private ImageIcon icoBasColonneRouge;
    private ImageIcon icoHeroMort;
    private ImageIcon icoPlateformeInter;

    // Icones du compteur
    private final ArrayList<ImageIcon> icoChiffres = new ArrayList<>();
    private ImageIcon icoPoints;
    private ImageIcon icoP;
    private ImageIcon icoT;
    private ImageIcon icoCompteurInter;
    private ImageIcon icoCompteurGauche;
    private ImageIcon icoCompteurDroite;

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
        mettreAJourAffichage(); // Première initiation pour ne pas avoir de frame blanche
        }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Q -> RealisateurMouvement.getInstance().setDirectionCourante(Direction.gauche);
                    case KeyEvent.VK_D -> RealisateurMouvement.getInstance().setDirectionCourante(Direction.droite);
                    case KeyEvent.VK_S -> RealisateurMouvement.getInstance().setDirectionCourante(Direction.bas);
                    case KeyEvent.VK_Z -> RealisateurMouvement.getInstance().setDirectionCourante(Direction.haut);
                    case KeyEvent.VK_F -> RealisateurMouvement.getInstance().setDirectionCourante(Direction.colonneBleue);
                    case KeyEvent.VK_R -> RealisateurMouvement.getInstance().setDirectionCourante(Direction.colonneRouge);
                }
            }
        });
    }

    private void chargerLesIcones() {
        icoHero = chargerIcone("data/Hector.png");
        icoMur = chargerIcone("data/Mur.png");
        icoSmick = chargerIcone("data/Smick.png");
        icoVide = chargerIcone("data/Vide.png");
        icoPlateformeH = chargerIcone("data/PlateformeH.png");
        icoPlateformeV = chargerIcone("data/PlateformeV.png");
        icoCorde = chargerIcone("data/Corde.png");
        icoHeroCorde = chargerIcone("data/HectorCorde.png");
        icoBombe = chargerIcone("data/Bombe.png");
        icoSupportColonneDroite = chargerIcone("data/SupportColonneDroite.png");
        icoSupportColonneGauche = chargerIcone("data/SupportColonneGauche.png");
        icoColonneBleue = chargerIcone("data/ColonneBleue.png");
        icoColonneRouge = chargerIcone("data/ColonneRouge.png");
        icoHautColonneBleue = chargerIcone("data/HautColonneBleue.png");
        icoBasColonneBleue = chargerIcone("data/BasColonneBleue.png");
        icoHautColonneRouge = chargerIcone("data/HautColonneRouge.png");
        icoBasColonneRouge = chargerIcone("data/BasColonneRouge.png");
        icoHeroMort = chargerIcone("data/HectorMort.png");
        icoPlateformeInter = chargerIcone("data/PlateformeI.png");

        // Icones du compteur
        icoCompteurDroite = chargerIcone("data/compteur/droite.png");
        icoCompteurGauche = chargerIcone("data/compteur/gauche.png");
        icoCompteurInter = chargerIcone("data/compteur/inter.png");
        icoP = chargerIcone("data/compteur/P.png");
        icoT = chargerIcone("data/compteur/T.png");
        icoPoints = chargerIcone("data/compteur/points.png");
        icoChiffres.add(chargerIcone("data/compteur/zero.png"));
        icoChiffres.add(chargerIcone("data/compteur/un.png"));
        icoChiffres.add(chargerIcone("data/compteur/deux.png"));
        icoChiffres.add(chargerIcone("data/compteur/trois.png"));
        icoChiffres.add(chargerIcone("data/compteur/quatre.png"));
        icoChiffres.add(chargerIcone("data/compteur/cinq.png"));
        icoChiffres.add(chargerIcone("data/compteur/six.png"));
        icoChiffres.add(chargerIcone("data/compteur/sept.png"));
        icoChiffres.add(chargerIcone("data/compteur/huit.png"));
        icoChiffres.add(chargerIcone("data/compteur/neuf.png"));
    }

    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleur.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setSize(496, 303);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent grilleJLabels = new JPanel(new GridLayout(tailleVueY + 1, tailleVueX));
        tabJLabel = new JLabel[tailleVueX][tailleVueY + 1];

        for (int y = 0 ; y < tailleVueY + 1; y++) {
            for (int x = 0 ; x < tailleVueX ; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab;
                tabJLabel[x][y].setSize(24, 24);
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    private void mettreAJourAffichageScores() {
        tabJLabel[0][0].setIcon(icoCompteurInter);
        tabJLabel[1][0].setIcon(icoCompteurInter);
        tabJLabel[2][0].setIcon(icoCompteurGauche);
        tabJLabel[3][0].setIcon(icoP);
        tabJLabel[4][0].setIcon(icoPoints);
        int score = jeu.getGameplay().getScore();
        int a = score / 100;
        int b = score / 10 % 10;
        int c = score % 10;
        tabJLabel[5][0].setIcon(icoChiffres.get(a));
        tabJLabel[6][0].setIcon(icoChiffres.get(b));
        tabJLabel[7][0].setIcon(icoChiffres.get(c));
        tabJLabel[8][0].setIcon(icoCompteurDroite);
        tabJLabel[9][0].setIcon(icoCompteurInter);
        tabJLabel[10][0].setIcon(icoCompteurInter);
        tabJLabel[11][0].setIcon(icoCompteurGauche);
        tabJLabel[12][0].setIcon(icoT);
        tabJLabel[13][0].setIcon(icoPoints);
        int temps = jeu.getGameplay().getTemps();
        a = temps / 100;
        b = temps / 10 % 10;
        c = temps % 10;
        tabJLabel[14][0].setIcon(icoChiffres.get(a));
        tabJLabel[15][0].setIcon(icoChiffres.get(b));
        tabJLabel[16][0].setIcon(icoChiffres.get(c));
        tabJLabel[17][0].setIcon(icoCompteurDroite);
        tabJLabel[18][0].setIcon(icoCompteurInter);
        tabJLabel[19][0].setIcon(icoCompteurInter);
    }

    private void mettreAJourCurseur() {
        int positionX = jeu.getPositionPersonnage();
        if(positionX < 10)
            curseur = 0;
        else if(positionX >= Jeu.SIZE_X - 10)
            curseur = Jeu.SIZE_X - 20;
        else curseur = positionX - 9;
    }

    private void mettreAJourAffichage() {
        for (int x = 0 ; x < tailleVueX ; x++) {
            for (int y = 0 ; y < tailleVueY ; y++) {
                int xGrille = curseur + x;
                Entite e = jeu.getGrille()[xGrille][y];
                if (e instanceof Heros) {
                    if(((Heros) e).getEstMort())
                        tabJLabel[x][y + 1].setIcon(icoHeroMort);
                    else if(((Heros) e).getEstSurCorde())
                        tabJLabel[x][y + 1].setIcon(icoHeroCorde);
                    else tabJLabel[x][y + 1].setIcon(icoHero);
                }
                else if (e instanceof Mur) {
                    tabJLabel[x][y + 1].setIcon(icoMur);
                }
                else if (e instanceof Plateforme) {
                    if(((Plateforme) e).getType() == TypePlateforme.verticale)
                        tabJLabel[x][y + 1].setIcon(icoPlateformeV);
                    else if(((Plateforme) e).getType() == TypePlateforme.horizontale)
                        tabJLabel[x][y + 1].setIcon(icoPlateformeH);
                    else if(((Plateforme) e).getType() == TypePlateforme.intermediaire)
                        tabJLabel[x][y + 1].setIcon(icoPlateformeInter);
                    else if(((Plateforme) e).getType() == TypePlateforme.supportColonneGauche)
                        tabJLabel[x][y + 1].setIcon(icoSupportColonneGauche);
                    else if(((Plateforme) e).getType() == TypePlateforme.supportColonneDroite)
                        tabJLabel[x][y + 1].setIcon(icoSupportColonneDroite);
                }
                else if(e instanceof CaseColonne) {
                    if(((CaseColonne) e).getCouleur() == CouleurColonne.bleue) {
                        if(((CaseColonne) e).getType() == TypeColonne.bas)
                            tabJLabel[x][y + 1].setIcon(icoBasColonneBleue);
                        else if(((CaseColonne) e).getType() == TypeColonne.inter)
                            tabJLabel[x][y + 1].setIcon(icoColonneBleue);
                        else if(((CaseColonne) e).getType() == TypeColonne.haut)
                            tabJLabel[x][y + 1].setIcon(icoHautColonneBleue);
                    }
                    else {
                        if(((CaseColonne) e).getType() == TypeColonne.bas)
                            tabJLabel[x][y + 1].setIcon(icoBasColonneRouge);
                        else if(((CaseColonne) e).getType() == TypeColonne.inter)
                            tabJLabel[x][y + 1].setIcon(icoColonneRouge);
                        else if(((CaseColonne) e).getType() == TypeColonne.haut)
                            tabJLabel[x][y + 1].setIcon(icoHautColonneRouge);
                    }
                }
                else if (e instanceof Bombe) {
                    tabJLabel[x][y + 1].setIcon(icoBombe);
                }
                else if (e instanceof Bot) {
                	tabJLabel[x][y + 1].setIcon(icoSmick);
                }
                else if (e instanceof Corde) {
                    tabJLabel[x][y + 1].setIcon(icoCorde);
                }
                else tabJLabel[x][y + 1].setIcon(icoVide); // Cas par défaut (vide)
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourCurseur();
        mettreAJourAffichageScores();
        mettreAJourAffichage();
    }
}
