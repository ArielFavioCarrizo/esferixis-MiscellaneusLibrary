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
 * Intervalo del conjunto de elementos ordenado infinitesimal
 */

package com.esferixis.misc.infinitesimalSortedSet;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.esferixis.misc.GenericComparator;
import com.esferixis.misc.collection.list.BinaryList;

public final class Interval<T> {
	private final SetPrimitive<T> minSetPrimitive, maxSetPrimitive;
	private final Comparator<? super T> elementComparator;
	
	/**
	 * @pre Tiene que ser una primitiva punto
	 * @post Crea un intervalo con primitivas que encierran el elemento de la primitiva punto
	 */
	public Interval(SetPrimitive<T> pointSetPrimitive) {
		this(new SetPrimitive<T>(pointSetPrimitive.getComparator(), pointSetPrimitive.getReferenceElement(), true, 1), 
				new SetPrimitive<T>(pointSetPrimitive.getComparator(), pointSetPrimitive.getReferenceElement(), true, -1));
		if ( pointSetPrimitive.getOrientation() != 0 ) {
			throw new IllegalArgumentException("Expected point set primitive");
		}
	}
	
	/**
	 * @pre Ninguna de las primitivas de conjunto puede ser nula.
	 * 		Las primitivas de conjunto tienen que estar en el orden correcto.
	 * 		Los comparadores tienen que coincidir.
	 * @post Crea un intervalo con las primitivas de conjunto especificadas
	 */
	public Interval(SetPrimitive<T> minSetPrimitive, SetPrimitive<T> maxSetPrimitive) {
		if ( ( minSetPrimitive != null ) && ( maxSetPrimitive != null ) ) {
			
			// Verificar que los comparadores de los límites de intervalo no sean distintos
			if ( !( new GenericComparator(minSetPrimitive.getComparator()).equals(new GenericComparator(maxSetPrimitive.getComparator())) ) ) {
				throw new IllegalArgumentException("Interval limit comparators mismatch");
			}
			
			// Obtener comparador
			this.elementComparator = minSetPrimitive.getComparator();
			
			// Verificar que la orientación de los límites de intervalo sea correcta
			if ( !( minSetPrimitive.getOrientation() > 0 ) ) {
				throw new IllegalArgumentException("Invalid min set primitive orientation");
			}
			
			if ( !( maxSetPrimitive.getOrientation() < 0 ) ) {
				throw new IllegalArgumentException("Invalid max set primitive orientation");
			}
			
			int borderComparison = minSetPrimitive.compareTo(maxSetPrimitive);
			// Verificar que el orden de las fronteras de las primitivas geométricas sea correcta
			if ( !( borderComparison <= 0 ) ) {
				throw new IllegalArgumentException("Invalid set primitive order");
			}
			
			this.minSetPrimitive = minSetPrimitive;
			this.maxSetPrimitive = maxSetPrimitive;
		}
		else {
			throw new IllegalArgumentException("Expected interval limits");
		}
	}
	
	/**
	 * @post Devuelve la primitiva mínima
	 */
	public SetPrimitive<T> getMinSetPrimitive() {
		return this.minSetPrimitive;
	}
	
	/**
	 * @post Devuelve la primitiva máxima
	 */
	public SetPrimitive<T> getMaxSetPrimitive() {
		return this.maxSetPrimitive;
	}
	
	/**
	 * @post Devuelve el comparador del intervalo
	 */
	public Comparator<? super T> getElementComparator() {
		return this.elementComparator;
	}
	
	/**
	 * @post Devuelve si es un punto
	 */
	public boolean isPoint() {
		return ( this.getMinSetPrimitive().getReferenceElement().equals(this.getMaxSetPrimitive().getReferenceElement()) );
	}
	
	/**
	 * @pre La referencia al otro intervalo no puede ser nula
	 * @post Devuelve la unión entre dos intervalos si es un intervalo
	 */
	public Interval<T> intervalUnion(Interval<T> other) {
		if ( other == null ) {
			throw new NullPointerException();
		}
		
		Interval<T> unionInterval;
		if ( other != this ) {
			unionInterval = null;
			
			boolean outOfRange;
			outOfRange = ( this.maxSetPrimitive.compareTo( other.minSetPrimitive ) < 0);
			
			if ( !outOfRange ) {
				outOfRange = (this.minSetPrimitive.compareTo(other.maxSetPrimitive) > 0);
				
				if ( !outOfRange ) {
					unionInterval = new Interval<T>(
							( this.minSetPrimitive.compareTo(other.minSetPrimitive) <= 0) ? this.minSetPrimitive : other.minSetPrimitive,
							( this.maxSetPrimitive.compareTo(other.maxSetPrimitive) >= 0) ? this.maxSetPrimitive : other.maxSetPrimitive
					);
				}
			}
			
			return unionInterval;
		}
		else {
			return this;
		}
	}
	
	/**
	 * @post Devuelve el intervalo representado en una lista
	 */
	public List< SetPrimitive<T> > setPrimitives() {
		return new BinaryList< SetPrimitive<T> >(this.minSetPrimitive, this.maxSetPrimitive);
	}
}
