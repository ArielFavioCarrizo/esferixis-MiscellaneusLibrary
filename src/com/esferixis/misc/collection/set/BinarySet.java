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
package com.esferixis.misc.collection.set;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.esferixis.misc.collection.list.BinaryList;
import com.esferixis.misc.containablestrategy.ContainableStrategy;
import com.esferixis.misc.containablestrategy.ExplicitContainableStrategy;

/**
 * Conjunto binario
 * 
 * @author ariel
 *
 * @param <T>
 */

public final class BinarySet<T> extends AbstractSet<T> {
	private final T element1, element2;
	
	/**
	 * @pre Ninguno de los elementos puede ser nulo
	 * @post Crea un conjunto binario con los elementos especificados
	 */
	public BinarySet(T element1, T element2) {
		if ( ( element1 != null) && ( element2 != null ) ) {
			this.element1 = element1;
			this.element2 = element2;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre La colección tiene que ser de tamaño 1 o 2
	 * @post Crea un conjunto binario con la colección especificada
	 */
	public BinarySet(Collection<T> elements) {
		Iterator<T> iterator = elements.iterator();
		if ( iterator.hasNext() ) {
			this.element1 = iterator.next();
			if ( iterator.hasNext() ) {
				this.element2 = iterator.next();
				if ( !iterator.hasNext() ) {
					return;
				}
			}
			else {
				this.element2 = this.element1;
			}
		}
		
		throw new IllegalArgumentException("Invalid size");
	}

	/**
	 * @post Crea un conjunto binario del elemento especificado
	 */
	public BinarySet(T element) {
		this(element, element);
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
	 * @post Devuelve el conjunto binario de clases de elementos
	 */
	@SuppressWarnings("unchecked")
	public BinarySet< Class<? extends T> > getClassSet() {
		return new BinarySet< Class<? extends T> >( (Class<? extends T>) this.element1.getClass(), (Class<? extends T>) this.element2.getClass() );
	}

	/**
	 * @post Devuelve el iterador
	 */
	@Override
	public Iterator<T> iterator() {
		class LocalIterator implements Iterator<T> {
			private T nextElement;
			public LocalIterator() {
				this.nextElement = BinarySet.this.element1;
			}
			@Override
			public boolean hasNext() {
				return ( this.nextElement != null );
			}
			@Override
			public T next() {
				if ( this.hasNext() ) {
					T element = this.nextElement;
					if ( this.nextElement.equals(BinarySet.this.element1 ) && ( !this.nextElement.equals( BinarySet.this.element2 ) ) ) {
						this.nextElement = BinarySet.this.element2;
					}
					else {
						this.nextElement = null;
					}
					return element;
				}
				else {
					throw new NoSuchElementException();
				}
			}
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new LocalIterator();
	}

	@Override
	public int size() {
		return ( this.element1.equals(this.element2) ? 1 : 2 );
	}
	
	// Estrategia de contenibles de contenedores de elementos
	public static class ElementContainersExplicitContainableStrategy<T> extends ExplicitContainableStrategy< BinarySet<T> > {
		private final ExplicitContainableStrategy<T> elementContainableStrategy;
		
		/**
		 * @pre La estrategia de contenibles explícita de elementos no puede ser nula
		 * @post Crea la estrategia de contenibles
		 */
		public ElementContainersExplicitContainableStrategy(ExplicitContainableStrategy<T> elementContainableStrategy) {
			super( (Class<BinarySet<T> >) (Class<?>) BinarySet.class);
			if ( elementContainableStrategy != null ) {
				this.elementContainableStrategy = elementContainableStrategy;
			}
			else {
				throw new NullPointerException();
			}
		}

		/**
		 * @post Devuelve los contenedores del conjunto binario especificado en un conjunto de sólo lectura
		 */
		@Override
		public Set<BinarySet<T>> getContainers(BinarySet<T> containable) {
			final Set<T> element1containers = this.elementContainableStrategy.getContainers(containable.getElement1());
			final Set<T> element2containers = this.elementContainableStrategy.getContainers(containable.getElement2());
			
			final Set< BinarySet<T> > containers = new HashSet< BinarySet<T> >();
			
			// Con los contenedores del 1
			for ( T eachContainer1 : element1containers ) {
				containers.add( new BinarySet<T>(eachContainer1, containable.getElement2() ) );
			}
			
			// Si hay 2 elementos
			if ( containable.size() == 2 ) {
				// Con los contenedores del 2
				for ( T eachContainer2 : element2containers ) {
					containers.add( new BinarySet<T>(containable.getElement1(), eachContainer2 ) );
				}
			}
			
			return Collections.unmodifiableSet(containers);
		}
		
	}
}
