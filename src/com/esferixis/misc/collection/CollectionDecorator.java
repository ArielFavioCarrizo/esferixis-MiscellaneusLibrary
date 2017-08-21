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

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import com.esferixis.misc.iterator.IteratorDecorator;



public abstract class CollectionDecorator<T> extends AbstractCollection<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6780926871350159837L;
	protected Collection<T> decoratedCollection; // Conjunto decorado
	
	// Iterador local
	protected class LocalIterator extends IteratorDecorator<T> {
		/**
		 * @post Crea el decorador con el iterador a decorar especificado
		 */
		protected LocalIterator(Iterator<T> decoratedIterator) {
			super(decoratedIterator);
		}
	}
	
	/**
	 * @post Crea un decorador vacío
	 */
	protected CollectionDecorator() {
		
	}
	
	/**
	 * @post Crea un decorador de la colección
	 */
	public CollectionDecorator(Collection<T> decoratedCollection) {
		this.decoratedCollection = decoratedCollection;
	}
	
	@Override
	public boolean add(T e) {
		return this.decoratedCollection.add(e);
	}

	@Override
	public boolean contains(Object o) {
		return this.decoratedCollection.contains(o);
	}

	@Override
	public boolean isEmpty() {
		return this.decoratedCollection.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return new LocalIterator( this.decoratedCollection.iterator() );
	}

	@Override
	public boolean remove(Object o) {
		return this.decoratedCollection.remove(o);
	}
	
	@Override
	public int size() {
		return this.decoratedCollection.size();
	}

	@Override
	public Object[] toArray() {
		return this.decoratedCollection.toArray();
	}

	@Override
	public <V> V[] toArray(V[] a) {
		return this.decoratedCollection.toArray(a);
	}
	
	@Override
	public int hashCode() {
		return this.decoratedCollection.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return this.decoratedCollection.equals(o);
	}
	
	@Override
	public String toString() {
		return this.decoratedCollection.toString();
	}
	
}
