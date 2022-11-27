package generique.metier.entite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import lombok.Getter;

/**
 * 
 * EN - Generic class representing a Population
 *      A Population gathers all the Generation of Individuals created for historical purposes
 * FR - Classe générique représentant une Population
 *      Une Population rassemble toutes les Generation d'Individu créées à des fins d'historique
 * 
 * @author Parlons Archi !
 * @see <a href="https://www.youtube.com/@christophecadet" /a>
 * 
 * @version 1.0
 */
public class Population<T extends Generation<?>> implements Serializable{

	/**
	 * EN - Version of the class allowing serialization
	 * FR - Version de la class permettant la sérialisation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * EN - List of historicized Generation
	 * FR - Liste des Generation historisées
	 */
	@Getter
	private List<T> lesGenerations = new ArrayList<T>();
	
	/**
	 * EN - Map allowing to follow the evolution of the generations in terms of score
	 * FR - Map permettant le suivi de l'évolution des générations en termes de score
	 */
	private NavigableMap<Integer, Integer> recensement = new TreeMap<Integer, Integer>();
	
	/**
	 * EN - History a generation
	 * FR - Historise une génération
	 * 
	 * @param generation EN - generation to historicize </br> FR - génération à historiser
	 */
	public void ajouterGeneration(T generation) {
		this.lesGenerations.add(generation);
	}

	
	/**
	 * EN - Provides the number of historical generations
	 * FR - Fournit le nombre de génération historisées
	 * 
	 * @return EN-the number of historical generations </br> FR-le nombre de génération historisées
	 */
	public int nbGenerations() {
		return this.lesGenerations.size();
	}
	
	
	/**
	 * EN - Surveys a generation
	 * FR - Recense une génération
	 * 
	 * @param generation EN-generation to census </br> FR-génération à recenser
	 */
	public void recenser(T generation) {
		
		this.recensement.put(generation.getNumero(), generation.getSommeScore());
	
	}
	
	/**
	 * EN - indicates whether a population is decreasing
	 *      A population is said to be decreasing when the score of the last 5 generations continuously decreases
	 * FR - indique si une population est decroissante
	 *      Une population est dite decroissante lorque le score des 5 dernières générations décroit continuellement
	 *      
	 * @return true EN-if the population is decreasing</br> FR-si la population est decroissante
	 */
	public boolean isPopulationDecroissante() {
		
		int nbDeGenerationDecroissante = 0;
		
		if (recensement.size() > 10 ) 
			for (int i = 0; i < 5; i++) 
				if (recensement.descendingMap().get(1) < recensement.descendingMap().get(i+1))
					nbDeGenerationDecroissante++;
			
		return (nbDeGenerationDecroissante == 5);
	}
}
