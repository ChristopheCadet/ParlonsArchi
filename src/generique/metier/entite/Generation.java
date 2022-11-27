package generique.metier.entite;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.java.Log;

/**
 * EN - Generic class representing a Generation in the sense of genetic algorithms
 *      A Generation gathers a set of {@link from generique.metier.entite.Individu}
 * 
 * FR - Class générique representant une Generation au sens des algorithmes génétiques
 *      Une Generation rassemble un ensemble d'{@link de generique.metier.entite.Individu}
 * 
 * @author Parlons Archi !
 * @see <a href="https://www.youtube.com/@christophecadet" /a>
 * 
 * @version 1.0
 *
 * @param <T> Type de la valeur caractérisant l'{@link de generique.metier.entite.Individu}
 */

/** 
 * EN - Creates private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Generation.class.getName()); 
 * FR - Création d'une réference privée statique finale vers un Logger reprenant le nom canonique de la classe
 */
@Log

public class Generation <T extends Individu<?>> implements Serializable{

	/**
	 * EN - Version of the class allowing serialization
	 * FR - Version de la class permettant la sérialisation
	 */
	private static final long serialVersionUID = 1L;

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
	 * EN - List of n Individuals constituting a generation at a time of treatment
	 * FR - List de n Individus constituant une génération à un instant du traitement
	 * 
	 * <T> EN-Type of the value characterizing the {@link of generique.metier.entite.Individu} </br> FR-Type de la valeur caractérisant l'{@link de generique.metier.entite.Individu}
	 */
	@Getter
	private List<T> lesIndividus = new ArrayList<T>();

	
	/**
	 * EN - number of the generation allowing to reconstitute the order of creation of the generations
	 * FR - numéro de la génération permettant de reconstituer l'ordre de création des générations
	 * 
	 */
	@Getter
	private Integer numero;	
	
	
	/**
 	 * EN - Constructor allowing to specify the order number of the generation
	 * FR - Construteur permettant de préciser le numéro d'ordre de la génération
	 * 
	 * @param numero EN-number of the generation allowing to reconstitute the order of creation of the generations </br> FR-numéro de la génération permettant de reconstituer l'ordre de création des générations
	 */
	public Generation(int numero) {
		this.numero = numero;
	}


	/**
	 * EN - Allows you to add a {@link from generique.metier.entite.Individu} to the generation 
	 * FR - Permet d'ajouter un {@link de generique.metier.entite.Individu} à la génération 
	 * 
	 * @param nouvelIndividu EN-new Individual to add to the Generation </br> FR-nouvel Individu à ajouter à la Génération
	 */
	public void ajouterIndividu(@NonNull T nouvelIndividu) {
		this.lesIndividus.add(nouvelIndividu);
	}

	
	/**
	 * EN - Allows you to add a {@link from generique.metier.entite.Individu} to the generation 
	 * FR - Permet d'ajouter un {@link de generique.metier.entite.Individu} à la génération 
	 * 
	 * @param nouvelIndividu EN-new Individual to add to the Generation </br> FR-nouvel Individu à ajouter à la Génération
	 */
	public void ajouterGroupeIndividu(@NonNull List<T> groupeIndividus) {
		if(! groupeIndividus.contains(null))
			this.lesIndividus.addAll(groupeIndividus);
	}
	
	
	/**
 	 * EN - Allows to remove a {@link from generique.metier.entite.Individu} to the generation 
	 * FR - Permet d'enlever un {@link de generique.metier.entite.Individu} à la génération 
	 * 
	 * @param nouvelIndividu EN-Individual to be removed from the Generation </br> FR-Individu à enlever de la Génération
	 */	
	public void enleverIndividu(@NonNull T individu) {
			this.lesIndividus.remove(individu);
	}

	
	/**
	 * EN - Indicates the number of individuals referenced within the Generation
	 * FR - Indique le nombre d'individus référencés au sein de la Generation
	 * 
	 * @return EN-An Integer whose value represents the number of Individuals referenced within the Generation </br> FR-Un Integer dont la valeur representant le nombre d'Individus référencés au sein de la Generation
	 */
	public Integer nbIndividus() {
		return this.lesIndividus.size();
	}

	
	/**
	 * EN - Trigger the deletion of unselected individuals
	 * FR - Déclenche la suppression des individus non selectionnés
	 * 
	 * {@link generique.metier.Individu#isSelectionne} 
	 */
	public void eliminerNonSelectionnes() {

		/*
		 * EN - needed to override the locks set by the foreach on the List<T> theIndividuals
		 * FR - necessaire pour outrepasser les verrous positionnés par le foreach sur la List<T> lesIndividus */
		List<T> lesIndividusLocaux = new ArrayList<>();
		
		lesIndividusLocaux.addAll(lesIndividus);
		
		
		for (T individu : lesIndividusLocaux) {
			
			if (!individu.isSelectionne()) {
				this.lesIndividus.remove(individu);
			}
		}

	}

	
	/**
	 * EN - Gives the sum of the individual scores of each Individual
	 * FR - Donne la somme des scores individuels de chaque Individu
	 * 
	 * @return EN-an Integer whose value represents the sum of the individual scores of each Individual </br> FR-un Integer dont la valeur représente la somme des scores individuels de chaque Individu
	 */
	public Integer getSommeScore() {
		
		int somme = 0;
		
		for (T individu : lesIndividus) {
			somme += individu.getScore();
		}
		
		return somme ;
	}
	
	
	/**
	 * EN - Gives the optimum selection threshold to be used during the selection phase in order to protect the selections. 
	 * 
	 * The selection threshold is calculated on the basis of a poderate average
	 * The selection threshold is calculated on the basis of an averaged score, the calculation of which is based on a table of distribution of individuals by score
	 * 
	 *        Notes :    0 1 3 3 4 5 6
	 * Nb Individus :	|0|4|0|2|1|0|0|
	 * --------------------------------
	 * 
	 * FR - Donne le seuil de sélection optimum à utiliser lors de la phase de sélection de manière à prémunir les sélections. 
	 * 
	 * Le seuil de selection est calculé sur la base d'une moyenne podérée
	 * dont le calcule repose sur un tableau de repartition des Individu par score
	 * 
	 *        Notes :    0 1 3 3 4 5 6
	 * Nb Individus :	|0|4|0|2|1|0|0|
	 * 
	 * @return EN-an Integer whose value represents the optimum selection threshold of the generation </br> FR-un Integer dont la valeur représente le seuil de sélection optimum de la génération
	 */
	public Integer getScoreDeSelection() {
		
		int scoreMax = 0;
		
		for (T individu : lesIndividus) {
			if (individu.getScore() > scoreMax )
				scoreMax = individu.getScore();
		}
		
		int[] repartitionScores = new int[scoreMax+1];
		
		for (T individu : lesIndividus) {
			repartitionScores[individu.getScore()]+=1;
		}
		
		
		int sommePonderee = 0;

		if (log.isLoggable(Level.FINE))
			log.log(Level.FINE,"Répartition :");
		
		for (int i = 1; i < repartitionScores.length; i++) {

			if (log.isLoggable(Level.FINE))
				log.log(Level.FINE,String.valueOf(repartitionScores[i]));

			sommePonderee+=repartitionScores[i] * i + 1;
		}

		return (int) sommePonderee / lesIndividus.size() ;
	}
	
	
	/**
	 * EN - Indicates whether the generation contains an acceptable solution
	 * FR - Indique si la generation contient une solution acceptable
	 * 
	 * @return true EN-if the generation contains an acceptable solution, false otherwise </br> FR-si la generation contient une solution acceptable, false dans le cas contraire
	 */
	public boolean contienSolution() {
		
		for (T individu : lesIndividus) {
			if (individu.isSolution())
				return true;
		}
		
		return false;
	}

	
	/**
	 * EN - Returns the acceptable solution with the highest score
	 * FR - Renvoit la solution acceptable ayant le meilleur score
	 * 
	 * @return EN-the acceptable solution with the highest score </br> FR-la solution acceptable ayant le meilleur score
	 */
	public T getMeilleurIndividu() {
		
		T meilleureSolution = null;
		
		for (T individu : lesIndividus) {
				if (meilleureSolution ==null || individu.getScore() > meilleureSolution.getScore())
						meilleureSolution = individu;
		}
		
		return meilleureSolution;
	}
	
}
