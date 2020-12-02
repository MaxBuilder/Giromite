package vue_controleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    private JLabel[][] tabJLabel; // Cases graphiques

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
        setSize(500, 280);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent grilleJLabels = new JPanel(new GridLayout(tailleVueY, tailleVueX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        tabJLabel = new JLabel[tailleVueX][tailleVueY];

        for (int y = 0 ; y < tailleVueY ; y++) {
            for (int x = 0 ; x < tailleVueX ; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                tabJLabel[x][y].setSize(24, 24);
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
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

        for (int x = 0 ; x < tailleVueX; x++) {
            for (int y = 0 ; y < tailleVueY ; y++) {
                int xGrille = curseur + x;
                if (jeu.getGrille()[xGrille][y] instanceof Heros) {
                    if(((Heros) jeu.getGrille()[xGrille][y]).getEstMort())
                        tabJLabel[x][y].setIcon(icoHeroMort);
                    else if(((Heros) jeu.getGrille()[xGrille][y]).getEstSurCorde())
                        tabJLabel[x][y].setIcon(icoHeroCorde);
                    else tabJLabel[x][y].setIcon(icoHero);
                }
                else if (jeu.getGrille()[xGrille][y] instanceof Mur) {
                    tabJLabel[x][y].setIcon(icoMur);
                }
                else if (jeu.getGrille()[xGrille][y] instanceof Plateforme) {
                    if(((Plateforme) jeu.getGrille()[xGrille][y]).getType() == TypePlateforme.verticale)
                        tabJLabel[x][y].setIcon(icoPlateformeV);
                    else if(((Plateforme) jeu.getGrille()[xGrille][y]).getType() == TypePlateforme.horizontale)
                        tabJLabel[x][y].setIcon(icoPlateformeH);
                    else if(((Plateforme) jeu.getGrille()[xGrille][y]).getType() == TypePlateforme.intermediaire)
                        tabJLabel[x][y].setIcon(icoPlateformeInter);
                    else if(((Plateforme) jeu.getGrille()[xGrille][y]).getType() == TypePlateforme.supportColonneGauche)
                        tabJLabel[x][y].setIcon(icoSupportColonneGauche);
                    else if(((Plateforme) jeu.getGrille()[xGrille][y]).getType() == TypePlateforme.supportColonneDroite)
                        tabJLabel[x][y].setIcon(icoSupportColonneDroite);
                }
                else if(jeu.getGrille()[xGrille][y] instanceof CaseColonne) {
                    if(((CaseColonne) jeu.getGrille()[xGrille][y]).getCouleur() == CouleurColonne.bleue) {
                        if(((CaseColonne) jeu.getGrille()[xGrille][y]).getType() == TypeColonne.bas)
                            tabJLabel[x][y].setIcon(icoBasColonneBleue);
                        else if(((CaseColonne) jeu.getGrille()[xGrille][y]).getType() == TypeColonne.inter)
                            tabJLabel[x][y].setIcon(icoColonneBleue);
                        else if(((CaseColonne) jeu.getGrille()[xGrille][y]).getType() == TypeColonne.haut)
                            tabJLabel[x][y].setIcon(icoHautColonneBleue);
                    }
                    else {
                        if(((CaseColonne) jeu.getGrille()[xGrille][y]).getType() == TypeColonne.bas)
                            tabJLabel[x][y].setIcon(icoBasColonneRouge);
                        else if(((CaseColonne) jeu.getGrille()[xGrille][y]).getType() == TypeColonne.inter)
                            tabJLabel[x][y].setIcon(icoColonneRouge);
                        else if(((CaseColonne) jeu.getGrille()[xGrille][y]).getType() == TypeColonne.haut)
                            tabJLabel[x][y].setIcon(icoHautColonneRouge);
                    }
                }
                else if (jeu.getGrille()[xGrille][y] instanceof Bombe) {
                    tabJLabel[x][y].setIcon(icoBombe);
                }
                else if (jeu.getGrille()[xGrille][y] instanceof Bot) {
                	tabJLabel[x][y].setIcon(icoSmick);
                }
                else if (jeu.getGrille()[xGrille][y] instanceof Corde) {
                    tabJLabel[x][y].setIcon(icoCorde);
                }
                else tabJLabel[x][y].setIcon(icoVide); // Cas par défaut (vide)
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourCurseur();
        mettreAJourAffichage();
    }
}
