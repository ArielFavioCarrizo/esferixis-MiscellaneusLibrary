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
package com.esferixis.misc.collection;

import java.util.Collection;
import java.util.Iterator;

public abstract class AddRemoveListenerCollectionDecorator<T> extends CollectionDecorator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2879475382698979622L;

	// Iterador local
	protected final class LocalIterator extends CollectionDecorator<T>.LocalIterator {
		
		protected LocalIterator(Iterator<T> decoratedIterator) {
			super(decoratedIterator);
		}
		
		/**
		 * @post Quita el elemento y reporta su remoción
		 */
		public void remove() {
			super.remove();
			AddRemoveListenerCollectionDecorator.this.removeEvent(this.lastElement);
		}
		
	}
	
	/**
	 * @post Crea un decorador sensible a cambios del conjunto, dispara los eventos
	 * 		 de agregado por cada elemento que aparece en la colección
	 * @param decoratedSet
	 */
	public AddRemoveListenerCollectionDecorator(Collection<T> decoratedCollection) {
		super(decoratedCollection);
		// Dispara el evento de agregado para todos los elementos
		for (T eachElement : decoratedCollection) {
			this.addEvent( eachElement );
		}
	}

	/**
	 * @post Acciones a realizar para el agregado del
	 * 		 elemento especificado
	 */
	protected abstract void addEvent(T element);
	
	/**
	 * @post Acciones a realizar para el quitado del
	 * 		 elemento especificado
	 */
	protected abstract void removeEvent(T element);

	/**
	 * @post Agrega un elemento
	 */
	@Override
	public final boolean add(T element) {
		boolean returnValue = super.add(element);
		// Si fue agregado
		if ( returnValue ) {
			// Disparar el evento relacionado
			this.addEvent( element );
		}
		return returnValue;
	}

	/**
	 * @post Quita todos los objetos
	 */
	@Override
	public final void clear() {
		if ( !this.isEmpty() ) {
			// Disparar el evento relacionado por cada elemento
			for ( T eachElement : this ) {
				this.removeEvent(eachElement);
			}
		}
		super.clear();
	}
	
	/**
	 * @post Quita el objeto especificado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final boolean remove(Object o) {
		boolean returnValue = super.remove(o);
		// Si fue quitado
		if ( returnValue ) {
			// Disparar el evento relacionado
			this.removeEvent( (T) o );
		}
		return returnValue;
	}
	
	@Override
	public final boolean contains(Object o) {
		return super.contains(o);
	}
	
	@Override
	public final boolean containsAll(Collection<?> elements) {
		return this.decoratedCollection.containsAll(elements);
	}

	@Override
	public final boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	public final Iterator<T> iterator() {
		return super.iterator();
	}
	
	@Override
	public final int size() {
		return super.size();
	}

	@Override
	public final Object[] toArray() {
		return super.toArray();
	}

	@Override
	public final <V> V[] toArray(V[] a) {
		return super.toArray(a);
	}
	
	@Override
	public final int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public final boolean equals(Object o) {
		return super.equals(o);
	}
	
	@Override
	public final String toString() {
		return super.toString();
	}
}
