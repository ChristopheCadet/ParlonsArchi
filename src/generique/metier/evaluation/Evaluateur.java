package generique.metier.evaluation;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import generique.metier.entite.Individu;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * EN - Generic class representing the evaluator.
 *      The evaluator is asked to evaluate an Individual against a reference that he is the only one to hold
 *      This reference is created by a method received in parameter
 *      The evaluation of an individual is delegated to a method received in parameter 
 * FR - Classe générique représentant l'évaluateur.
 *      L'évaluateur est sollicité pour évaluer un Individu par rapport à une référence qu'il est le seul à détenir
 *      Cette réference est créée par une méthode reçue en paramètre
 *      L'évaluation d'un individu est déléguée à une méthode reçue en paramètre 
 * 
 * @author Parlons Archi !
 * @see <a href="https://www.youtube.com/@christophecadet" /a>
 * 
 * @version 1.0
 */
public class Evaluateur<Individu, V> {

	/**
	 * En - Manufacture of the reference allowing the evaluator to evaluate an individual
	 * FR - Fabrique de la référence permettant à l'évaluateur d'évaluer un individu
	 */
	@Setter
	@NonNull
	private Supplier<V> fabriqueMotMystere;
	
	/**
	 * En - Reference allowing the evaluator to evaluate an individual
	 * FR - Référence permettant à l'évaluateur d'évaluer un individu
	 */
	@Getter
	private V motMystere;
	

	/**
	 * EN - evaluation method called to evaluate an individual
	 * FR - méthode d'évaluation appelée pour évaluer un individu
	 */
	@Setter
	@NonNull
	private BiConsumer<Individu, V> evaluateurIndividu;
	

	/**
	 * EN - Constructor
	 * FR - Constructeur
	 * 
	 * @param fabriqueMotMystere @see {@link #fabriqueMotMystere}
	 * @param evaluateurIndividu @see {@link #evaluateurIndividu}
	 */
	public Evaluateur(Supplier<V> fabriqueMotMystere, BiConsumer<Individu, V> evaluateurIndividu) {

		this.motMystere = fabriqueMotMystere.get();
	
		setFabriqueMotMystere(fabriqueMotMystere);
		
		setEvaluateurIndividu(evaluateurIndividu);
	
	}
	
	
	/**
	 * EN - method of requesting an evaluation of an individual
	 * FR - méthode permettant de demander l'évaluation d'un individu
	 * 
	 * @param individu EN-individual to evaluate </br> FR-individu à évaluer
	 */
	public void evaluer(Individu individu) {
		
		this.evaluateurIndividu.accept(individu, motMystere);
		
	}


	
}
