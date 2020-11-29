import vue_controleur.*;
import modele.plateau.*;

public class Main {

	public static void main(String[] args) {
		Jeu jeu = new Jeu();
        
			VueControleur vc = new VueControleur(jeu);

			jeu.getOrdonnanceur().addObserver(vc);
        
			vc.setVisible(true);
			jeu.start(300);
	}

}
