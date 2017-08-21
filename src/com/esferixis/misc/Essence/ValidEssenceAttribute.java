/**
 * Atributo de esencia válido
 */

package com.esferixis.misc.Essence;

import com.esferixis.misc.reference.UnmodifiableReference;

public abstract class ValidEssenceAttribute<T> implements EssenceAttribute<T> {
	protected final Essence essence;
	protected final UnmodifiableReference<T> value;
	
	/**
	 * @pre La esencia no puede ser nula
	 * @post Crea el atributo con la esencia
	 */
	ValidEssenceAttribute(Essence essence) {
		if ( essence != null ) {
			this.essence = essence;
			this.value = new UnmodifiableReference<T>();
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Valida el valor
	 * 	
	 * 		 En ésta implementación no hace nada
	 */
	protected void validateValue(T value) {
		
	}
	
	/**
	 * @pre No tiene que estar inicializado
	 * @post Inicializa el atributo
	 */
	public void init(T value) {
		if ( !this.essence.isValidated() ) {
			if ( !this.value.isInitialized() ) {
				this.validateValue(value);
				this.value.init(value);
			}
			else {
				throw new IllegalStateException("Attemped to initialize attribute when it has been initialized");
			}
		}
		else {
			throw new IllegalStateException("Cannot modify value when the essence has been used");
		}
	}
	
	/**
	 * @pre Tiene que estar inicializado
	 * @post Devuelve el valor del atributo
	 */
	public final T get() {
		this.initializationValidation();
		return this.value.get();
	}
	
	/**
	 * @post Valida la inicialización
	 */
	public final void initializationValidation() {
		if ( !this.value.isInitialized() ) {
			throw new IllegalStateException("Unitialized attribute");
		}
	}
	
	/**
	 * @post Devuelve si está inicializado
	 */
	public final boolean isInitialized() {
		return this.value.isInitialized();
	}
}
