package generique.metier.selection;

import java.util.function.BiConsumer;

import generique.metier.entite.Individu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * EN - Generic class in charge of the selection of individuals from a method received in parameter
 * FR - Classe générique chargée de la sélection des individus à partir d'une méthode reçue en paramètre
 * 
 * @author Parlons Archi !
 * @see <a href="https://www.youtube.com/@christophecadet" /a>
 * 
 * @version 1.0
 */
@AllArgsConstructor
public class Selecteur<T extends Individu<?>> {

	/**
	 * EN - selection method used to evaluate an individual
	 * FR - méthode de sélection appelée pour évaluer un individu
	 */
	@Setter
	@NonNull
	private BiConsumer<T, Integer> selecteurIndividu;
	
	/**
	 * EN - Threshold score above which an individual is selected
	 * FR - Score seuil au delà duquel un individu est sélectionné
	 */
	@Getter
	@Setter
	private Integer seuil;
	
	/**
	 * EN - method to request the selection of an individual
	 * FR - méthode permettant de demander la sélection d'un individu
	 * 
	 * @param individu EN-individual to select </br> FR-individu à sélectionner
	 */
	public void selectionner(T individu) {
		
		this.selecteurIndividu.accept(individu, this.seuil);
		
	}
	
}
