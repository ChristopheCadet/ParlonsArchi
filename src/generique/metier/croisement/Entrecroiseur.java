package generique.metier.croisement;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

import generique.metier.entite.Generation;
import generique.metier.entite.Individu;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * 
 * @author Parlons Archi !
 * @see <a href="https://www.youtube.com/@christophecadet" /a>
 * 
 * @version 1.0
 * 
 * EN - Class allowing to launch the application whose objective is to find a mystery word
 * FR - Classe permettant de lancer l'application dont l'objectif est de trouver un mot mystère
 * 
 */

public class Entrecroiseur<T extends Individu<?>> {

	/** EN - Behavioral parameterization of the function in charge of the generation of children
	 *      x : First parent individual to be crossed.
	 *      y : Second parent individual to be crossed
	 * FR - Paramétrage comportemental de la fonction chargée de la génération d'individus enfants
	 *      x : Premier individu parent devant être croisé
	 *      y : Second individu parent devant être croisé */
	@Setter
	@NonNull
	private BiFunction<T, T, List<T>> fabriqueDescendants;
	
	/**
	 * EN - Number of individuals per generation
	 * FR - Nombre d'individu par génération
	 */
	@Setter
	private int nbIndividuParGeneration; 
	
	public Generation<T> getGeneration(Generation<T> generationParent) {
		
		Generation<T> nouvelleGeneration = new Generation<T>(generationParent.getNumero()+1);
			
		/*
		 * EN - Sorting of individuals by score
		 * FR - Tri des individus par score
		 */
		generationParent.getLesIndividus().sort(new Comparator<T>() {

			@Override
			public int compare(T motA, T motB) {
				return motB.getScore() - motA.getScore();
			}
			
		});
		
		int index = 0;
		
		while(nouvelleGeneration.nbIndividus() < nbIndividuParGeneration && index < generationParent.nbIndividus()-1) {
						
			nouvelleGeneration.ajouterGroupeIndividu(fabriqueDescendants.apply(	generationParent.getLesIndividus().get(index), 
																				generationParent.getLesIndividus().get(index+1)
																			  )
													);
			
			index+=2;
		}
		
		return nouvelleGeneration;
	}

	public Entrecroiseur(@NonNull BiFunction<T, T, List<T>> fabriqueDescendants, int nbIndividuParGeneration) {
		setFabriqueDescendants(fabriqueDescendants);
		setNbIndividuParGeneration(nbIndividuParGeneration);
	}
	
	
	
	
}
