package generique.metier.initialisation;

import java.util.function.Supplier;

import generique.metier.entite.Generation;
import generique.metier.entite.Individu;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;


/**
 * EN - Generic class in charge of creating a new generation from a method received in parameter
 * FR - Classe générique chargée de la création d'une nouvelle génération à partir d'une méthode reçue en paramètre
 * 
 * @author Parlons Archi !
 * @see <a href="https://www.youtube.com/@christophecadet" /a>
 * 
 * @version 1.0
 */
@AllArgsConstructor
public class Generateur<T extends Individu<?>> {

	/**
	 * En - Factory of individual allowing the creation of an individual
	 * FR - Fabrique d'individu permettant la création d'un individu
	 */
	@Setter
	@NonNull
	private Supplier<T> fabriqueIndividu;
	
	
	/**
	 * EN - Method to obtain a new generation of x individuals
	 * FR - Méthode permettant d'obtenir une nouvelle génération de x individus
	 * 
	 * @param nbIndividu EN-number of individuals to generate </br> FR-nombre d'individus à générer
	 * @return EN-the newly created generation of individuals </br> FR-la génération d'individus nouvellement créée
	 */
	public Generation<T> getGeneration(int nbIndividu) {
		
		Generation<T> generationInitiale = new Generation<T>(0);
			
		for (int i = 0; i < nbIndividu; i++) {
			generationInitiale.ajouterIndividu(fabriqueIndividu.get());
		}
		
		return generationInitiale;
	}
}