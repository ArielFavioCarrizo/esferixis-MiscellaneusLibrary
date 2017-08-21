/**
 * Copyright (c) 2017 Ariel Favio Carrizo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'esferixis' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Primitiva del del conjunto de elementos ordenado infinitesimal
 */

package com.esferixis.misc.infinitesimalSortedSet;

import java.util.Comparator;

import com.esferixis.misc.GenericComparator;

public final class SetPrimitive<T> implements Comparable< SetPrimitive<T> >{
	private final GenericComparator<? super T> genericComparator; // Comparador genérico
	private final T referenceElement;
	private final boolean isClosed;
	
	/**
	 * Orientación.
	 * Si es mayor a cero indica hacia los mayores del elemento
	 * Si es igual es un punto ubicado en el elemento
	 * Si es menor a cero indica hacia los menores del elemento
	 */
	private final int orientation;
	
	/**
	 * @pre El elemento no puede ser nulo
	 * @post Crea una primitiva con el orden natural y un sólo elemento
	 */
	public SetPrimitive(T element) {
		this(null, element );
	}
	
	/**
	 * @pre El elemento no puede ser nulo
	 * @post Crea una primitiva de conjunto con el comparador y un sólo elemento
	 */
	public SetPrimitive(Comparator<? super T> comparator, T element) {
		this(comparator, element, true, 0);
	}
	
	/**
	 * @pre El elemento si el nulo, el intervalo tiene que ser abierto y si no hay comparador especificado
	 * 		tiene que implementar la interface Comparable<T>
	 * 		Si la orientación es cero tiene que ser cerrado
	 * @post Crea una primitiva de conjunto con el orden natural de los elementos, el elemento especificado, si es cerrado y con la orientación especificada.
	 * 		 Si no se especifica el elemento se considerará que tiene frontera infinita
	 */
	public SetPrimitive(T element, boolean isClosed, int orientation) {
		this(null, element, isClosed, orientation);
	}
	
	/**
	 * @pre El elemento si es nulo, el intervalo tiene que ser abierto y si no hay comparador especificado
	 * 		y el elemento no es nulo tiene que implementar la interface Comparable<T>
	 * 		Si la orientación es cero tiene que ser cerrado
	 * @post Crea una primitiva de conjunto con el comparador, el elemento especificado, si es cerrado y con la orientación especificada
	 */
	public SetPrimitive(Comparator<? super T> comparator, T element, boolean isClosed, int orientation) {
		this.genericComparator = new GenericComparator<T>(comparator);
		
		if ( this.genericComparator.getTargetComparator() == null ) {
			if ( element != null ) {
				if (!( element instanceof Comparable<?> )) {
					throw new ClassCastException();
				}
			}
		}
		
		if ( element == null ) {
			if ( isClosed ) {
				throw new IllegalArgumentException("Expected open set primitive because element is null.");
			}
		}
		
		if ( orientation == 0 ) {
			if ( !isClosed ) {
				throw new IllegalArgumentException("Expected closed set primitive because orientation is zero.");
			}
		}
		
		this.referenceElement = element;
		
		this.isClosed = isClosed;
		
		this.orientation = Integer.signum(orientation);
	}
	
	/**
	 * @post Devuelve el comparador
	 */
	public Comparator<? super T> getComparator() {
		return this.genericComparator.getTargetComparator();
	}

	/**
	 * @post Devuelve el elemento de referencia
	 */
	public T getReferenceElement() {
		return this.referenceElement;
	}
	
	/**
	 * @pre La referencia a la otra primitiva no puede ser nula
	 * @post Devuelve si tiene límite igual a la otra primitiva
	 */
	public boolean equalsLimit(SetPrimitive<T> other) {
		if ( other != null ) {
			final boolean equals;
			if ( this.getReferenceElement() != null ) {
				if ( other.getReferenceElement() != null ) {
					equals = (this.genericComparator.compare(this.getReferenceElement(), other.getReferenceElement()) == 0);
				}
				else {
					equals = other.equalsLimit(this);
				}
			}
			else {
				equals = (other.getReferenceElement() == null) && (this.getOrientation() == other.getOrientation());
			}
			return equals;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve si es cerrado
	 */
	public boolean isClosed() {
		return this.isClosed;
	}
	
	/**
	 * @post Devuelve la orientación
	 */
	public int getOrientation() {
		return this.orientation;
	}
	
	/**
	 * @pre El elemento no puede ser nulo
	 * @post Devuelve si contiene el elemento especificado
	 */
	public boolean contains(T element) {
		if ( element == null ) {
			throw new NullPointerException();
		}
		
		boolean contained;
		if ( this.getReferenceElement() != null ) {
			int elementReferenceComparison = this.genericComparator.compare(element, this.referenceElement);
		
			if ( elementReferenceComparison == 0 ) {
				contained = this.isClosed();
			}
			else {
				contained = (elementReferenceComparison > 0) == (this.getOrientation() > 0);
			}
		}
		else {
			contained = true;
		}
		return contained;
	}
	

	/**
	 * @post Devuelve si es menor, igual o mayor
	 */
	@Override
	public int compareTo(SetPrimitive<T> other) {
		if ( other != null ) {
			if ( this.genericComparator.equals( other.genericComparator ) ) {
				int borderComparison;
				if ( ( this.getReferenceElement() != null ) && ( other.getReferenceElement() != null ) ) { // Si ninguna de las dos primitivas tienen frontera infinita
					int elementComparison = this.genericComparator.compare(this.getReferenceElement(), other.getReferenceElement());
					if ( elementComparison == 0 ) { // Si los elementos son iguales
						// Si ambos son cerrados
						if ( this.isClosed() && other.isClosed() ) {
							borderComparison = 0; // La frontera es la misma, resulta igual
						}
						else { // Caso contrario por lo menos uno es abierto
							// Si las orientaciones son distintas
							if ( ( this.getOrientation() != other.getOrientation() ) ) {
								borderComparison = this.getOrientation() - other.getOrientation(); // La comparación será igual a la diferencia entre orientaciones
							}
							else { // Caso contrario
								final int commonOrientation = this.getOrientation(); // Orientación común
								// Si ambos son abiertos
								if ( !this.isClosed() && !other.isClosed() ) {
									borderComparison = 0; // La frontera es la misma, resulta igual
								}
								else if ( !this.isClosed() ) { // Si la primitiva 1 es abierta
									borderComparison = commonOrientation; // La comparación es igual a la orientación común
								}
								else { // Caso contrario, la primitiva 2 es abierta
									borderComparison = 1 - commonOrientation; // La comparación es opuesta a la orientación común
								}
							}
						}
					}
					else { // Si los elementos son distintos
						borderComparison = elementComparison; // Resultará igual que la comparación de los elementos
					}
				}
				else { // Caso contrario
					if ( other.getReferenceElement() != null ) { // Si la primitiva 2 no tiene frontera infinita
						borderComparison = -this.getOrientation(); // La comparación de borde es opuesta a la orientación de la primitiva 1
					}
					else if ( this.getReferenceElement() != null ) { // Si a primitiva 1 no tiene frontera infinita
						borderComparison = other.getOrientation(); // La comparación de borde es igual a la orientación de la primitiva 2
					}
					else { // Caso contrario
						borderComparison = other.getOrientation() - this.getOrientation(); // Es igual a la diferencia
					}
				}
				return borderComparison;
			}
			else {
				throw new IllegalArgumentException("Comparator mismatch");
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve la representación en cadena de carácteres
	 */
	@Override
	public String toString() {
		final String elementRepresentation;
		if ( this.getReferenceElement() != null ) {
			elementRepresentation = "'" + this.getReferenceElement().toString() + "'";
		}
		else {
			if ( this.getOrientation() > 0 ) {
				elementRepresentation = "-INFINITE";
			}
			else {
				elementRepresentation = "INFINITE";
			}
		}
		
		if ( this.getOrientation() == 0 ) {
			return "{ " + elementRepresentation + " }";
		}
		else if ( this.getOrientation() > 0 ) {
			if ( this.isClosed() ) {
				return "{ [" + elementRepresentation +" }";
			}
			else {
				return "{ (" + elementRepresentation +" }";
			}
		}
		else {
			if ( this.isClosed() ) {
				return "{ " + elementRepresentation +"] }";
			}
			else {
				return "{ " + elementRepresentation +") }";
			}
		}
	}
	
	/**
	 * @post Devuelve el hash
	 */
	public int hashCode() {
		final int elementHash;
		if ( this.getReferenceElement() != null ) {
			elementHash = this.getReferenceElement().hashCode();
		}
		else {
			elementHash = 0;
		}
		return this.genericComparator.hashCode() + this.getOrientation() * 31 + elementHash * 31 * 31;
	}
	
	/**
	 * @post Devuelve si es igual al objeto especificado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		if ( other != null ) {
			if ( other instanceof SetPrimitive ) {
				@SuppressWarnings("rawtypes")
				final SetPrimitive otherSetPrimitive = (SetPrimitive<?>) other;
				return this.genericComparator.equals(otherSetPrimitive.genericComparator) && (this.isClosed() == otherSetPrimitive.isClosed() )
						&& ( this.getOrientation() == otherSetPrimitive.getOrientation() ) && this.equalsLimit(otherSetPrimitive);
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}
