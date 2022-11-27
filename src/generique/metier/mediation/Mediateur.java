package generique.metier.mediation;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogManager;

import generique.metier.croisement.Entrecroiseur;
import generique.metier.entite.Generation;
import generique.metier.entite.Individu;
import generique.metier.entite.Population;
import generique.metier.evaluation.Evaluateur;
import generique.metier.initialisation.Generateur;
import generique.metier.selection.Selecteur;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.java.Log;

/** 
 * EN - Creates private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Mediateur.class.getName());
 * FR - Création d'une réference privée statique finale vers un Logger reprenant le nom canonique de la classe
 */
@Log
public class Mediateur<T extends Individu<?>, R> implements Runnable {

	
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
            log.log( Level.SEVERE, "Cannot read configuration file", exception );
        }
    }
	
	/**
	 * EN - Number of individuals per generation
	 * FR - Nombre d'individu par génération
	 */
	private int nbIndividusParGeneration; 
	
	/**
	 * EN - Maximum allowable treatment time
	 * FR - Durée maximale de traitement autorisée
	 */	
	private long dureeTraitement;
	
	/**
	 * EN - Generator in charge of the creation of a generation of individuals
	 * FR - Générateur chargé de la creation d'une génération d'individus
	 */
	private Generateur<T> gs;
	
	/**
	 * EN - Generation of individuals being processed
	 * FR - Génération d'individus en cours de traitement
	 */
	private Generation<T> g; 
	
	
	/**
	 * EN - Selector allowing the selection of an individual
	 * FR - Sélecteur permettant la sélection d'un individu
	 */
	private Selecteur<T> selecteurIndividu;
	
	/**
	 * EN - Intercrosser allowing the creation of child individuals from parent individuals
	 * FR - Entrecroiseur permettant la création d'individus enfant à partir d'individus parents
	 */
	private Entrecroiseur<T> entrecroiseurIndividus;
	
	/**
	 * EN - Population allowing generation historization
	 * FR - Population permettant l'historisation des générations
	 */
	private Population<Generation<T>> population = new Population<Generation<T>>();
	
	/**
	 * EN - LocalDateTime of treatment start allowing the treatment to be stopped when the maximum allocated time has elapsed
	 * FR - LocalDateTime de début de traitement permettant l'arrêt du traitement lorsque le temps maximum alloué est écoulé
	 */
	private LocalDateTime startAt;
	
	
    /**
     * EN - Method of reference generation to be found
     * FR - Méthode de génération de référence à trouver
     */
	@NonNull	
	@Setter
	private Supplier<R> fonctionGeneratriceMotMystereAleatoire;
	
    /**
     * EN - Method of random generation of an individual
     * FR - Méthode de génération aleatoire d'un individu
     */
	@NonNull	
	@Setter
	private Supplier<T> fonctionGeneratriceIndividuAleatoire;
	
    /**
     * EN - Method of evaluating an individual
     * FR - Méthode d'évaluation d'un individu
     */
	@NonNull	
	@Setter
	private BiConsumer<T, R> fonctionEvaluatriceIndividu;
	
    /** 
     * @see Evaluateur
     */	
	@NonNull	
	@Setter
	private Evaluateur<T, R> evaluateurIndividu;
	
	
    /**
     * EN - Method of selecting an individual
     * FR - Méthode de sélection d'un individu
     */
	@NonNull	
	@Setter
	private BiConsumer<T, Integer> fonctionSelectriceIndividu;
	
	/**
     * EN - Method of generating an individual by crossing parent individuals
     * FR - Méthode de génération d'un individu par croisement d'individus parents
     */	
	@NonNull	
	@Setter
	private BiFunction<T, T, List<T>> fonctionFabriqueDeDescendants;



	private void init() {
		
	try {
		
		/*
		 * EN - Configuration loading
		 * FR - Chargement de la configuration
		 */
		Properties properties = new Properties();
	    properties.load(new FileInputStream("geneticConfig.properties"));

	
		this.nbIndividusParGeneration = Integer.valueOf(properties.getProperty("nbIndividusParGeneration")); 
		this.dureeTraitement = Integer.valueOf(properties.getProperty("dureeExecution"));
		
		} catch (IOException e) {
			if (log.isLoggable(Level.SEVERE)) {
				log.log(Level.SEVERE, "Erreur à l'execution", e.getMessage());
				log.log(Level.SEVERE, "Cause : ", e.getCause());
			}
			throw new RuntimeException(e);
		}
		
		
		this.selecteurIndividu = new Selecteur<T> (fonctionSelectriceIndividu, 0);
		
		this.entrecroiseurIndividus = new Entrecroiseur<T> (fonctionFabriqueDeDescendants, nbIndividusParGeneration);
		
		this.gs = new Generateur<T>(fonctionGeneratriceIndividuAleatoire);
		
		this.startAt = LocalDateTime.now();
		
		
	}
	
	
	/**
	 * 
	 * @param fonctionGeneratriceMotMystereAleatoire @see {@link #fonctionGeneratriceMotMystereAleatoire}
	 * @param fonctionGeneratriceIndividuAleatoire   @see {@link #fonctionGeneratriceIndividuAleatoire}
	 * @param fonctionEvaluatriceIndividu            @see {@link #fonctionEvaluatriceIndividu}
	 * @param evaluateurIndividu                     @see {@link #evaluateurIndividu}
	 * @param fonctionSelectriceIndividu             @see {@link #fonctionSelectriceIndividu}
	 * @param fonctionFabriqueDeDescendants          @see {@link #fonctionFabriqueDeDescendants}
	 */
	public Mediateur(	Supplier<R> fonctionGeneratriceMotMystereAleatoire,
						Supplier<T> fonctionGeneratriceIndividuAleatoire,
						BiConsumer<T, R> fonctionEvaluatriceIndividu, 
						Evaluateur<T, R> evaluateurIndividu,
						BiConsumer<T, Integer> fonctionSelectriceIndividu,
						BiFunction<T, T, List<T>> fonctionFabriqueDeDescendants) {
		
		setFonctionGeneratriceMotMystereAleatoire(fonctionGeneratriceMotMystereAleatoire);
		setFonctionGeneratriceIndividuAleatoire(fonctionGeneratriceIndividuAleatoire);
		setFonctionEvaluatriceIndividu(fonctionEvaluatriceIndividu);
		setEvaluateurIndividu(evaluateurIndividu);
		setFonctionSelectriceIndividu(fonctionSelectriceIndividu);
		setFonctionFabriqueDeDescendants(fonctionFabriqueDeDescendants);
	}



	@Override
	public void run() {
		
		init();
		
		/*
		 *  EN - Random initialization of the first generation
		 *  FR - Initialisation aléatoire de la première génération
		 */
		g = gs.getGeneration(nbIndividusParGeneration);
		
		
		/*
		 * EN - Assessment of individuals in the initial generation
		 * FR - Evaluation des individus de la génération initiale
		 */
		for (T individu : g.getLesIndividus()) {
			evaluateurIndividu.evaluer(individu);
		}
		
		if (log.isLoggable(Level.FINE)) {
			log.log(Level.FINE,"Generation initiale évaluée\n");
		}
		
		/*
		 * EN - Stopping condition
		 * 		Processing stops if the allotted time has elapsed or if the generation being processed contains the desired solution or if the population is decreasing
		 * FR - Condition d'arrêt
		 *      Le traitement s'arrête si le temps alloué est écoulé oue si la génération en cours de traitement contient la solution recherchée ou si la population est decroissante
		 */
		while(Duration.between(startAt, LocalDateTime.now()).toMinutes() < dureeTraitement && !g.contienSolution() && !population.isPopulationDecroissante()) {
			
			if (log.isLoggable(Level.INFO)) {

				log.log(Level.INFO, "Generation {0} en cours de traitement", g.getNumero());
				

				if (log.isLoggable(Level.FINE)) {
					
					log.log(Level.FINE, "Duree de traitement : {0} minutes", Duration.between(startAt, LocalDateTime.now()).toMinutes());
					log.log(Level.FINE, "Mot mystere : {0}",evaluateurIndividu.getMotMystere());
					log.log(Level.FINE, "Score cumulé : {0}", g.getSommeScore());
					log.log(Level.FINE, "Score de Sélection : {0}", g.getScoreDeSelection());

				}
				
				log.log(Level.INFO, "Meilleure solution : {0}", g.getMeilleurIndividu());
			}
			
			
			/*
			 * EN - Reconfiguration of the selection threshold
			 * FR - Reconfiguration du seuil de sélection
			 */
			selecteurIndividu.setSeuil(g.getScoreDeSelection());
			
			/*
			 * EN - Selection of individuals from the generation being processed
			 * FR - Sélection des individus de la génération en cours de traitement
			 */
			for (T individu : g.getLesIndividus()) {
				selecteurIndividu.selectionner(individu);
			}
			
			
			/*
			 * EN - Deletion of unselected individuals from the generation being processed
			 * FR - Suppression des individus non sélectionnés de la génération en cours de traitement
			 */
			
			g.eliminerNonSelectionnes();
			
			/*
			 * EN - Crossbreeding of the remaining individuals to create the new generation
			 * FR - Croisement des individus restant pour créer la nouvelle génération
			 */
			
			g = entrecroiseurIndividus.getGeneration(g);
			
			
			/*
			 * EN - Add additional individuals if necessary
			 * FR - Ajout d'individus supplementaires si necessaire
			 */

			if (g.nbIndividus() < nbIndividusParGeneration) {
				g.ajouterGroupeIndividu(gs.getGeneration(nbIndividusParGeneration-g.nbIndividus()).getLesIndividus());
			}
			

			/*
			 *  En - Evaluation of the new generation of individuals
			 *  FR - Evaluation des individus de la nouvelle generation
			 */
			
			for (T individu : g.getLesIndividus()) {
				evaluateurIndividu.evaluer(individu);
			}
			
			/*
			 *  En - Generation census
			 *  FR - Recensement de la génération
			 */
			population.recenser(g);
			
		}
		
		if (log.isLoggable(Level.INFO)) { 

			log.log(Level.INFO, "END !");
			log.log(Level.INFO, "le mot Mystère est ? : {0}", evaluateurIndividu.getMotMystere());
			log.log(Level.INFO, "La meilleure solution trouvée est : {0}", g.getMeilleurIndividu());
			log.log(Level.INFO, "Population décroissante ? : {0}", population.isPopulationDecroissante());
			log.log(Level.INFO, "Temps écoulé : {0} minutes", Duration.between(startAt, LocalDateTime.now()).toMinutes());

		}


		
		
	}
	
	
	

}
