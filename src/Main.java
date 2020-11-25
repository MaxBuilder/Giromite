import VueControleur.*;
import modele.plateau.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Jeu jeu = new Jeu();
        
			VueControleurGyromite vc = new VueControleurGyromite(jeu);

			jeu.getOrdonnanceur().addObserver(vc);
        
			vc.setVisible(true);
			jeu.start(300);
		

	}

}
