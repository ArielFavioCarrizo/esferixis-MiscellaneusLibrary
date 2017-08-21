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

package com.esferixis.misc.collection.list;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.esferixis.misc.collection.set.BinarySet;
import com.esferixis.misc.containablestrategy.ExplicitContainableStrategy;



/**
 * Lista binaria del juego de Super Esferix 3D
 * 
 * @author Ariel Favio Carrizo
 *
 * @param <T> Tipo de elemento
 * @see BinarySet
 */
public final class BinaryList<T> extends AbstractList<T> {
	private final T element1, element2;
	
	/**
	 * @post Crea una lista binaria con los elementos especificados
	 */
	public BinaryList(T element1, T element2) {
		this.element1 = element1;
		this.element2 = element2;
	}
	
	/**
	 * @pre La colección tiene que tener longitud 2
	 * @post Crea una lista binaria con los elementos de la colección especificada
	 */
	public BinaryList(Collection<? extends T> elements) {
		Iterator<? extends T> elementIterator = elements.iterator();
		if ( elementIterator.hasNext() ) {
			this.element1 = elementIterator.next();
			if ( elementIterator.hasNext() ) {
				this.element2 = elementIterator.next();
				if ( !elementIterator.hasNext() ) {
					return;
				}
			}
		}
	
		throw new IllegalArgumentException("Invalid collection length");
	}
	
	/**
	 * @post Devuelve el elemento 1
	 */
	public T getElement1() {
		return this.element1;
	}
	
	/**
	 * @post Devuelve el elemento 2
	 */
	public T getElement2() {
		return this.element2;
	}
	
	/**
	 * @post Devuelve un conjunto binario de los elementos
	 */
	public BinarySet<T> getElementsSet() {
		return new BinarySet<T>(this.element1, this.element2);
	}
	
	/**
	 * @post Devuelve la lista binaria con el orden inverso
	 */
	public BinaryList<T> opposite() {
		return new BinaryList<T>(this.element2, this.element1);
	}
	
	/**
	 * @post Devuelve la lista binaria de las clases de los elementos
	 */
	@SuppressWarnings("unchecked")
	public BinaryList< Class<? extends T> > getClassSortedPair() {
		return new BinaryList< Class<? extends T> >( (Class<? extends T>) this.element1.getClass(), (Class<? extends T>) this.element2.getClass() );
	}

	/**
	 * @pre El índice tiene que ser 0 o 1
	 * @post Devuelve el elemento con el índice especificado
	 */
	@Override
	public T get(int index) {
		T element;
		switch ( index ) {
		case 0:
			element = this.element1;
			break;
		case 1:
			element = this.element2;
			break;
		default:
			throw new IndexOutOfBoundsException();
		}
		return element;
	}
	
	/**
	 * @post Devuelve la longitud
	 */
	@Override
	public int size() {
		return 2;
	}
	
	// Estrategia de contenibles
	public static class ElementContainersExplicitContainableStrategy<T> extends ExplicitContainableStrategy< BinaryList<T> > {
		private final ExplicitContainableStrategy<T> elementContainableStrategy;
		
		/**
		 * @pre La estrategia de contenibles explícita de elementos no puede ser nula
		 * @post Crea la estrategia de contenibles
		 */
		public ElementContainersExplicitContainableStrategy(ExplicitContainableStrategy<T> elementContainableStrategy) {
			super( (Class< BinaryList<T> >) (Class<?>) BinaryList.class);
			if ( elementContainableStrategy != null ) {
				this.elementContainableStrategy = elementContainableStrategy;
			}
			else {
				throw new NullPointerException();
			}
		}

		/**
		 * @post Devuelve los contenedores de la lista binaria especificada en un conjunto de sólo lectura
		 */
		@Override
		public Set<BinaryList<T>> getContainers(BinaryList<T> containable) {
			final Set<T> element1containers = this.elementContainableStrategy.getContainers(containable.getElement1());
			final Set<T> element2containers = this.elementContainableStrategy.getContainers(containable.getElement2());
			
			final Set< BinaryList<T> > containers = new HashSet< BinaryList<T> >();
			
			// Con los contenedores del 1
			for ( T eachContainer1 : element1containers ) {
				containers.add( new BinaryList<T>(eachContainer1, containable.getElement2()) );
			}
			
			// Con los contenedores del 2
			for ( T eachContainer2 : element2containers ) {
				containers.add( new BinaryList<T>(containable.getElement1(), eachContainer2) );
			}
			
			return Collections.unmodifiableSet(containers);
		}
		
	}
	
}
