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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.esferixis.misc.collection.TransformedCollection;
import com.esferixis.misc.relation.Function;

/**
 * Es una visión de conjunto con otro tipo de datos sostenida por un conjunto original
 * 
 * @author ariel
 *
 * @param <S> Origen
 * @param <D> Destino
 */

public class TransformedSet<S, D> implements Set<D> {
	private final Collection<D> transformedCollection;
	
	/**
	 * @pre Ninguno de los parámetros puede ser nulo
	 * @post Crea el conjunto transformado con el conjunto origen, el transformador de origen a destino,
	 * 		 y el transformador de destino a origen especificados
	 * @param 
	 * @return
	 */
	public TransformedSet(Set<S> sourceSet, Function<? super S, D> sourceToDestination, Function<Object, S> destinationToSource) {
		this.transformedCollection = new TransformedCollection<S, D>(sourceSet, sourceToDestination, destinationToSource);
	}

	@Override
	public boolean add(D e) {
		return this.transformedCollection.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends D> c) {
		return this.transformedCollection.addAll(c);
	}

	@Override
	public void clear() {
		this.transformedCollection.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.transformedCollection.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.transformedCollection.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.transformedCollection.isEmpty();
	}

	@Override
	public Iterator<D> iterator() {
		return this.transformedCollection.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return this.transformedCollection.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.transformedCollection.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.transformedCollection.retainAll(c);
	}

	@Override
	public int size() {
		return this.transformedCollection.size();
	}

	@Override
	public Object[] toArray() {
		return this.transformedCollection.toArray();
	}

	@Override
	public <V> V[] toArray(V[] array) {
		return this.transformedCollection.toArray(array);
	}
	
}
