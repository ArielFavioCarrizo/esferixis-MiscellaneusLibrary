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

public abstract class RestrictorCollectionDecorator<T> extends CollectionDecorator<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1642578727989508937L;

	// Iterador local
	protected final class LocalIterator extends CollectionDecorator<T>.LocalIterator {

		protected LocalIterator(Iterator<T> decoratedIterator) {
			super(decoratedIterator);
		}
		
	}
	
	/**
	 * @post Crea un decorador de colecciones restrictor decorando a la
	 * 		 colección especificada
	 */
	public RestrictorCollectionDecorator(Collection<T> decoratedCollection) {
		super(decoratedCollection);
	}
	
	/**
	 * @pre El elemento tiene que pasar la prueba
	 * @post Si el elemento ha pasado la prueba sale exitosamente
	 */
	protected abstract void checkElement(T element);
	
	/**
	 * @pre El elemento tiene que ser válido
	 * @post Agrega un elemento
	 */
	@Override
	public final boolean add(T element) {
		this.checkElement(element);
		return super.add(element);
	}
	
	@Override
	public final void clear() {
		this.decoratedCollection.clear();
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
	public final boolean remove(Object o) {
		return super.remove(o);
	}
	
	@Override
	public final boolean removeAll(Collection<?> elements) {
		return this.decoratedCollection.removeAll(elements);
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
