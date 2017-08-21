/**
 * Atributo de esencia no nulo
 */

package com.esferixis.misc.Essence;

public class NotNullEssenceAttribute<T> extends EssenceAttributeProxy<T> {

	/**
	 * @pre El atributo no puede ser nulo
	 * @post Crea el proxy con el atributo especificado
	 */
	public NotNullEssenceAttribute(EssenceAttribute<T> essenceAttribute) {
		super(essenceAttribute);
	}
	
	/**
	 * @pre El valor no puede ser nulo
	 * @post Asigna el valor especificado
	 */
	@Override
	public void init(T value) {
		if ( value != null ) {
			this.essenceAttribute.init(value);
		}
		else {
			throw new NullPointerException();
		}
	}
}
