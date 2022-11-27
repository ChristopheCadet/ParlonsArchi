package numeric.lanceur;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogManager;

import generique.metier.evaluation.Evaluateur;
import generique.metier.mediation.Mediateur;
import lombok.extern.java.Log;
import numeric.metier.entite.Nombre;



/**
 * EN - Class allowing to launch the application whose objective is to find a mystery word
 * FR - Classe permettant de lancer l'application dont l'objectif est de trouver un mot mystère
 * 
 * @author Parlons Archi !
 * @see <a href="https://www.youtube.com/@christophecadet" /a>
 * 
 * @version 1.0
 */

/** 
 * EN - Creates private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(App.class.getName());
 * FR - Création d'une réference privée statique finale vers un Logger reprenant le nom canonique de la classe
 */
@Log
public class App {

	/** EN - Reference to global LogManager
	 *  FR - Reference au global LogManager
	 */
	private static final LogManager logManager = LogManager.getLogManager();

	/** EN - Reads and initializes the logging configuration from the given input stream.
	 *  FR - Lit et initialise la configuration de la journalisation à partir du flux d'entrée donné 
	 */
	static{
		try {
			logManager.readConfiguration( new FileInputStream("geneticLogConfig.properties") );
		} catch ( IOException exception ) {
			log.log( Level.SEVERE, "Impossible de charger le fichier de configuration", exception );
		}
	}

	public static void main(String[] args) {

		try {


			/* EN - Loading the configuration as Properties
			 * FR - Chargement de la configuration sous forme de Properties */	
			Properties properties = new Properties();
			properties.load(new FileInputStream("geneticConfig.properties"));


			/* EN - Behavioral parameterization of the function in charge of generating the mystery number.
			 * FR - Paramétrage comportemental de la fonction chargée de générer le nombre mystère. */
			Supplier<String> fonctionGeneratriceNombreMystereAleatoire = () -> {

				int mystery = new Random().nextInt(Integer.MAX_VALUE);

				String nb = Integer.toBinaryString(mystery);

				while(nb.length() < 32 )
					nb = new String("0").concat(nb);

				return nb;
			};



			/* EN - Behavioral parameterization of the function in charge of generating an individual randomly.
			 * FR - Paramétrage comportemental de la fonction chargée de générer un individu aléatoirement. */	
			Supplier<Nombre> fonctionGeneratriceIndividuAleatoire = () -> {

				String nb = Integer.toBinaryString(new Random().nextInt(Integer.MAX_VALUE));
				while(nb.length() < 32 )
					nb = new String("0").concat(nb);

				return new Nombre(nb);
			};		


			/* EN - Behavioral parameterization of the function responsible for evaluating an individual
			 *      x : Mystery number to find
			 *      y: Candidate individual to score 
			 * FR - Paramétrage comportemental de la fonction chargée de l'évaluation d'un individu. 
			 *      x : Nombre mystère à trouver
			 *      y : Individu candidat à scrorer */			
			BiConsumer<Nombre, String> fonctionEvaluatriceIndividu = (x,y) ->{

				/* EN - Table of characters constituting the mystery number
				 * FR - Tableau des caractères constituant le nombre mystère */
				char[] elementsMotMystere = y.toCharArray();

				/* EN - Table of characters constituting an individual
				 * FR - Tableau des caractères constituant un individu */
				char[] elementsIndividu = x.getValeur().toCharArray();

				/* EN - Score obtained by the individual
				 * FR - Score obtenu par l'individu */
				int scoreIndividu = 0;

				/* EN - Individual scoring loop
				 * FR - Boucle de scoring de l'individu */
				for (int i = 0; i < elementsIndividu.length; i++) {
					if (elementsIndividu[i] == elementsMotMystere[i])
						scoreIndividu++;
				}

				/* EN - Assignment of the score to the individual
				 * FR - Affectation du score à l'individu */
				x.setScore(scoreIndividu);

				/* EN - Designation of the individual as the solution where appropriate
				 * FR - Désignation de l'individu comme solution le cas échéant */
				x.setSolution(x.getScore() == elementsMotMystere.length);

			};


			/* EN - Evaluator Instantiation
			 * FR - Instanciation de l'évaluateur */	
			Evaluateur<Nombre, String> evaluateurIndividu = new Evaluateur<Nombre, String> (fonctionGeneratriceNombreMystereAleatoire, fonctionEvaluatriceIndividu);


			/* EN - Behavioral parameterization of the function in charge of the selection of the individuals
			 *      x : Individual candidate for selection
			 *      y : Selection threshold score
			 * FR - Paramétrage comportemental de la fonction chargée de la sélection des individus.			
			 *      x : Individu candidat à la sélection
			 *      y : Score seuil de sélection */	
			BiConsumer<Nombre, Integer> fonctionSelectriceIndividu = (x,y) ->{

				x.setSelectionne(x.getScore() >= y);

			};



			/* EN - Behavioral parameterization of the function in charge of the generation of children
			 *      x : First parent individual to be crossed.
			 *      y : Second parent individual to be crossed
			 * FR - Paramétrage comportemental de la fonction chargée de la génération d'individus enfants
			 *      x : Premier individu parent devant être croisé
			 *      y : Second individu parent devant être croisé */	
			BiFunction<Nombre, Nombre, List<Nombre>> fonctionFabriqueDeDescendants = (x,y) -> { 

				/* EN - Collection of individual children
				 * FR - Collection d'individus enfants */
				List<Nombre> leGroupeEnfants = new ArrayList<Nombre>();

				/* EN - Table of characteristics constituting the first parent individual
				 * FR - Tableau des caractères constituant le premier individu parent */
				char[] elementsParentUn = x.getValeur().toCharArray();

				/* EN - Table of characteristics constituting the second parent individual
				 * FR - ableau des caractères constituant le second individu parent */
				char[] elementsParentDeux = y.getValeur().toCharArray();

				
				/* EN - Table of characters constituting the first child individual
				 * FR - Tableau des caractères constituant le premier individu enfant */
				char[] elementsEnfantUn = new char[elementsParentUn.length];
				
				/* EN - Table of characters constituting the second child individual
				 * FR - Tableau des caractères constituant le second individu enfant */
				char[] elementsEnfantDeux = new char[elementsParentDeux.length];


				/* EN - Generation loop of the two children individuals
				 * FR - Boucle de génération des deux individus enfants */			
				for (int i = 0; i < elementsParentUn.length; i++) {

					/* EN - Random selection of the mode of transmission of traits from parent to offspring
					 * FR - Choix aléatoire du mode de transmission des caractères des individus parents aux individus enfants */			
					int choix = (int) ((Math.random() * 10) % 2);

					/* EN - Transmission of the characters from the parents to the children according to the determined mode
					 * FR - Transmission des caractères des individus parents aux individus enfants selon le mode déterminé */			
					if (choix == 1) {
						elementsEnfantUn[i] = elementsParentUn[i];
						elementsEnfantDeux[i] = elementsParentDeux[i];
					} else {
						elementsEnfantUn[i] = elementsParentDeux[i];
						elementsEnfantDeux[i] = elementsParentUn[i];
					}

				}

				/* EN - Instantiation of the first child individual from its characters
				 * FR - Instanciation du premier individu enfant à partir de ses caractères */						
				Nombre enfantUn = new Nombre(new String(elementsEnfantUn));
				
				/* EN - Instantiation of the second child individual from its characters
				 * FR - Instanciation du second individu enfant à partir de ses caractères */						
				Nombre enfantDeux = new Nombre(new String(elementsEnfantDeux));

				/* EN - Designation of new individuals as children
				 * FR - Désignation des nouveaux individus comme individu enfant */						
				enfantUn.setEnfant(true);
				enfantDeux.setEnfant(true);

				/* EN - Adding new individuals to the collection of children individuals
				 * FR - Ajout des nouveaux individus à la collection des individus enfants */
				leGroupeEnfants.add(enfantUn);
				leGroupeEnfants.add(enfantDeux);

				return leGroupeEnfants; 

			}; 



			/* EN - Instantiation of the Mediator
			 * FR - Instanciation du Mediateur. */			
			Mediateur<Nombre, String> mediateur = new Mediateur<Nombre, String>(	
					fonctionGeneratriceNombreMystereAleatoire,
					fonctionGeneratriceIndividuAleatoire,
					fonctionEvaluatriceIndividu, 
					evaluateurIndividu,
					fonctionSelectriceIndividu,
					fonctionFabriqueDeDescendants
					);

			/* EN - Triggering the system by calling the Mediator's run() method
			 * FR - Déclenchement du système par appel à la méthode run() du Mediateur. */			
			mediateur.run();



		} catch (IOException e) {
			log.log(Level.SEVERE, "Erreur à l'execution", e.getMessage());
			log.log(Level.SEVERE, "Cause : ", e.getCause());
		}
	}		



}
