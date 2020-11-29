package vue_controleur;

import java.awt.GridLayout;
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

import modele.deplacements.RealisateurDeplacement;
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

    private final int sizeX;
    private final int sizeY;

    // Icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoHeroCorde;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoColonneV;
    private ImageIcon icoColonneH;
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

    private JLabel[][] tabJLabel; // Cases graphiques


    public VueControleur(Jeu _jeu) {
        sizeX = Jeu.SIZE_X;
        sizeY = Jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Q -> RealisateurDeplacement.getInstance().setDirectionCourante(Direction.gauche);
                    case KeyEvent.VK_D -> RealisateurDeplacement.getInstance().setDirectionCourante(Direction.droite);
                    case KeyEvent.VK_S -> RealisateurDeplacement.getInstance().setDirectionCourante(Direction.bas);
                    case KeyEvent.VK_Z -> RealisateurDeplacement.getInstance().setDirectionCourante(Direction.haut);
                    case KeyEvent.VK_F -> RealisateurDeplacement.getInstance().setDirectionCourante(Direction.colonneBleue);
                    case KeyEvent.VK_R -> RealisateurDeplacement.getInstance().setDirectionCourante(Direction.colonneRouge);
                }
            }
        });
    }

    private void chargerLesIcones() {
        icoHero = chargerIcone("data/Hector.png");
        icoMur = chargerIcone("data/Mur.png");
        icoSmick = chargerIcone("data/Smick.png");
        icoVide = chargerIcone("data/Vide.png");
        icoColonneH = chargerIcone("data/PlateformeH.png");
        icoColonneV = chargerIcone("data/PlateformeV.png");
        icoCorde = chargerIcone("data/Corde.png");
        icoHeroCorde = chargerIcone("data/HectorCorde.png");
        icoBombe = chargerIcone("data/Bombe.png");
        icoSupportColonneDroite = chargerIcone("data/supportColonneDroite.png");
        icoSupportColonneGauche = chargerIcone("data/supportColonneGauche.png");
        icoColonneBleue = chargerIcone("data/colonneBleue.png");
        icoColonneRouge = chargerIcone("data/colonneRouge.png");
        icoHautColonneBleue = chargerIcone("data/hautColonneBleue.png");
        icoBasColonneBleue = chargerIcone("data/basColonneBleue.png");
        icoHautColonneRouge = chargerIcone("data/hautColonneRouge.png");
        icoBasColonneRouge = chargerIcone("data/basColonneRouge.png");
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

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                tabJLabel[x][y].setSize(24, 24);
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Heros) {
                    if(((Heros) jeu.getGrille()[x][y]).getEstSurCorde())
                        tabJLabel[x][y].setIcon(icoHeroCorde);
                    else tabJLabel[x][y].setIcon(icoHero);
                }
                else if (jeu.getGrille()[x][y] instanceof Mur) {
                    tabJLabel[x][y].setIcon(icoMur);
                }
                else if (jeu.getGrille()[x][y] instanceof Plateforme) {
                    if(((Plateforme) jeu.getGrille()[x][y]).getType() == TypePlateforme.verticale)
                        tabJLabel[x][y].setIcon(icoColonneV);
                    else if(((Plateforme) jeu.getGrille()[x][y]).getType() == TypePlateforme.horizontale)
                        tabJLabel[x][y].setIcon(icoColonneH);
                    else if(((Plateforme) jeu.getGrille()[x][y]).getType() == TypePlateforme.supportColonneGauche)
                        tabJLabel[x][y].setIcon(icoSupportColonneGauche);
                    else if(((Plateforme) jeu.getGrille()[x][y]).getType() == TypePlateforme.supportColonneDroite)
                        tabJLabel[x][y].setIcon(icoSupportColonneDroite);
                }
                else if(jeu.getGrille()[x][y] instanceof CaseColonne) {
                    if(((CaseColonne) jeu.getGrille()[x][y]).getCouleur() == CouleurColonne.bleue) {
                        if(((CaseColonne) jeu.getGrille()[x][y]).getType() == TypeColonne.bas)
                            tabJLabel[x][y].setIcon(icoBasColonneBleue);
                        else if(((CaseColonne) jeu.getGrille()[x][y]).getType() == TypeColonne.inter)
                            tabJLabel[x][y].setIcon(icoColonneBleue);
                        else if(((CaseColonne) jeu.getGrille()[x][y]).getType() == TypeColonne.haut)
                            tabJLabel[x][y].setIcon(icoHautColonneBleue);
                    }
                    else {
                        if(((CaseColonne) jeu.getGrille()[x][y]).getType() == TypeColonne.bas)
                            tabJLabel[x][y].setIcon(icoBasColonneRouge);
                        else if(((CaseColonne) jeu.getGrille()[x][y]).getType() == TypeColonne.inter)
                            tabJLabel[x][y].setIcon(icoColonneRouge);
                        else if(((CaseColonne) jeu.getGrille()[x][y]).getType() == TypeColonne.haut)
                            tabJLabel[x][y].setIcon(icoHautColonneRouge);
                    }
                }
                else if (jeu.getGrille()[x][y] instanceof Bombe) {
                    tabJLabel[x][y].setIcon(icoBombe);
                }
                else if (jeu.getGrille()[x][y] instanceof Bot) {
                	tabJLabel[x][y].setIcon(icoSmick);
                }
                else if (jeu.getGrille()[x][y] instanceof Corde) {
                    tabJLabel[x][y].setIcon(icoCorde);
                }
                else tabJLabel[x][y].setIcon(icoVide); // Cas par défaut
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
    }
}
