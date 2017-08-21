/**
 * Atributo de esencia opcional
 */

package com.esferixis.misc.Essence;

public class OptionalEssenceAttribute<T> extends ValidEssenceAttribute<T> {
	private final boolean allowInitializationAfterValidation;
	
	/**
	 * @pre La esencia no puede ser nula
	 * @post Crea el atributo de esencia opcional con la esencia especificada y
	 * 		 si permite ser inicializado luego de la validación
	 */
	OptionalEssenceAttribute(Essence essence, boolean allowInitializationAfterValidation) {
		super(essence);
		this.allowInitializationAfterValidation = allowInitializationAfterValidation;
	}
	
	/**
	 * @pre El atributo debe inicializarse antes de la validación
	 * @post Inicializa el atributo
	 */
	@Override
	public void init(T value) {
		if ( allowInitializationAfterValidation || ( !this.essence.isValidated() ) ) {
			super.init(value);
		}
		else {
			throw new IllegalStateException("Attemped to initialize an optional attribute when the essence has been validated");
		}
	}
	
}
