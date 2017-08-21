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
package com.esferixis.misc.collection.sortedSet;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * Decorador de conjuntos ordenados
 * 
 * @author ariel
 *
 * @param <T>
 */
public class SortedSetDecorator<T> implements SortedSet<T> {
	
	protected SortedSet<T> decoratedSortedSet; // Conjunto ordenado decorado
	
	/**
	 * @post Crea un decorador con el conjunto decorado sin asignar
	 */
	public SortedSetDecorator() {
		
	}
	
	/**
	 * @post Crea un decorador del conjunto
	 */
	public SortedSetDecorator(SortedSet<T> decoratedSortedSet) {
		this.decoratedSortedSet = decoratedSortedSet;
	}

	@Override
	public boolean add(T e) {
		return this.decoratedSortedSet.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return this.decoratedSortedSet.addAll(c);
	}

	@Override
	public void clear() {
		this.decoratedSortedSet.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.decoratedSortedSet.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.decoratedSortedSet.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.decoratedSortedSet.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return this.decoratedSortedSet.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return this.decoratedSortedSet.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.decoratedSortedSet.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.decoratedSortedSet.retainAll(c);
	}

	@Override
	public int size() {
		return this.decoratedSortedSet.size();
	}

	@Override
	public Object[] toArray() {
		return this.decoratedSortedSet.toArray();
	}

	@Override
	public <V> V[] toArray(V[] a) {
		return this.decoratedSortedSet.toArray(a);
	}

	@Override
	public Comparator<? super T> comparator() {
		return this.decoratedSortedSet.comparator();
	}

	@Override
	public T first() {
		return this.decoratedSortedSet.first();
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		return this.decoratedSortedSet.headSet(toElement);
	}

	@Override
	public T last() {
		return this.decoratedSortedSet.last();
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return this.decoratedSortedSet.subSet(fromElement, toElement);
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return this.decoratedSortedSet.tailSet(fromElement);
	}
	
	@Override
	public int hashCode() {
		return this.decoratedSortedSet.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return this.decoratedSortedSet.equals(o);
	}
	
	@Override
	public String toString() {
		return this.decoratedSortedSet.toString();
	}
	
}
