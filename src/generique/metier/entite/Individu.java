package generique.metier.entite;

import java.io.Serializable;

/**
 * EN - Generic interface representing the functional contract of an Individual in the genetic algorithm sense
 * FR - Interface générique representant le contrat fonctionnel d'un Individu au sens algorithme génétique 
 * 
 * @author Parlons Archi !
 * @see <a href="https://www.youtube.com/@christophecadet" /a>
 * 
 * @version 1.0
 *
 * @param <T> EN-Type of the value characterizing the Individual  </br> FR-Type de la valeur caractérisant l'Individu
 * 
 * <T> extends Serialisable EN-to allow the registration of Generation containing Individuals </br> FR-pour permettre l'enregistrement des Generation contenant des Individu
 */
public interface Individu <T> extends Serializable{
	
	/* Rappel, toute méthode déclarée au sein d'une interface "public" est "public" par défaut
	 * de fait, les signatures de méthode ci-dessous ne precise pas la visibilité
	 */
	
	
	/**
	 * EN - Returns the value characterizing the Individual
	 * FR - Retourne la valeur caracterisant l'Individu
	 * 
	 * @param <T> EN-Type of the value characterizing the Individual  </br> FR-Type de la valeur caractérisant l'Individu
	 * @return EN-the value characterizing the Individual FR-la valeur caractérisant l'Individu
	 */
	T getValeur();
	
	
	/**
	 * EN - method to modify the value characterizing the Individual
	 * FR - methode permettant de modifier la valeur caractérisant l'Individu
	 * 
	 * @param <T> EN-Type of the value characterizing the Individual  </br> FR-Type de la valeur caractérisant l'Individu
	 * @param valeur EN-the value characterizing the Individual </br> FR-la valeur caractérisant l'Individu
	 */
	void setValeur(T valeur);
	
	
	/**
	 * EN - Returns the score of the Individual, in the sense of evaluation of genetic algorithms,
	 * in the form of a primitive integer
	 * It is usual that a score of -1 designates an Individual that has never been evaluated
	 * In a broader way, it is the same for any negative value
	 * 
	 * FR - Renvoit le score de l'Individu, au sens évaluation des algorithmes génétiques,
	 * sous forme d'entier primitif
	 * Il est d'usage qu'un score à -1 désigne un Individu qui n'a jamais été évalué
	 * De manière plus large, il en est de même pour toute valeur négative
	 * 
	 * @return EN-a primitive integer value corresponding to the score of the Individual in the sense of evaluation of genetic algorithms </br> FR-une valeur entière primitive correspondant au score de l'Individu au sens évaluation des algorithmes génétiques
	 * 
	 * @see generic.evaluation
	 * 
	 */
	int getScore();
	
	
	/**
	 * EN - Allows to assign a score to the Individual, in the sense of evaluation of genetic algorithms,
	 * in the form of a primitive integer
	 * It is usual that a score of -1 designates an Individual that has never been evaluated
	 * In a broader way, it is the same for any negative value
	 * 
	 * FR - Permet d'attribuer un score à l'Individu, au sens évaluation des algorithmes génétiques,
	 * sous forme d'entier primitif
	 * Il est d'usage qu'un score à -1 désigne un Individu qui n'a jamais été évalué
	 * De manière plus large, il en est de même pour toute valeur négative
	 * 
	 * @param EN-score primitive integer value corresponding to the score of the Individual in the sense of evaluation of genetic algorithms </br> FR-score valeur entière primitive correspondant au score de l'Individu au sens évaluation des algorithmes génétiques
	 */
	void setScore(int score);
	
	
	/**
	 * indique si l'Individu, de par sa valeur, représente une solution acceptable, au sens évaluation des algorithmes génétiques   
	 * 
	 * @return true si l'Individu, de par sa valeur, représente une solution acceptable, au sens évaluation des algorithmes génétiques, sinon false
	 */
	boolean isSolution();

	
	/**
	 * EN - indicates if the Individual, by its value, represents an acceptable solution, in the sense of evaluation of genetic algorithms   
	 * FR - indique si l'Individu est selectionné pour participer à la phase de croisement, au sens évaluation des algorithmes génétiques   
	 * 
	 * @return true EN-if the Individual is selected to participate to the crossover phase, in the sense of genetic algorithms evaluation, otherwise false </br> FR-si l'Individu est selectionné pour participer à la phase de croisement, au sens évaluation des algorithmes génétiques, sinon false
	 */
	boolean isSelectionne();
	
	
	/**	
	 * EN - indicates if the Individual is a Child Individual, in the sense of evaluation of the genetic algorithms  
	 * A Child Individual is an Individual obtained by crossing two Individuals coming from a previous {@link generique.metier.entite.Generation} 
	 * 
	 * FR - indique si l'Individu est un individu Enfant, au sens évaluation des algorithmes génétiques  
	 * Un Individu Enfant est un Individu obtenu par croisement de deux Individus issus d'une {@link generique.metier.entite.Generation} précédente 
	 * 
	 * @return true EN-if the Individual is a Child Individual, in the sense of genetic algorithm evaluation, otherwise false </br> FR-si l'Individu est un Individu Enfant, au sens évaluation des algorithmes génétiques, sinon false
	 */	
	boolean isEnfant();
	

}
