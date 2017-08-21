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

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * Conjunto ordenado basado en ArrayList
 * 
 * @author ariel
 *
 * @param <T>
 */
public final class ArraySortedSet<T> extends AbstractSet<T> implements SortedSet<T> {
	private SortedSet<T> backingSortedSetSortedListVision;
	private ArrayList<T> elements;
	
	/**
	 * @post Crea un conjunto vacío con ordenamiento natural
	 */
	public ArraySortedSet() {
		this( (Comparator<? super T>) null);
	}
	
	/**
	 * @pre La capacidad inicial no puede ser negativa
	 * @post Crea un conjunto vacío con ordenamiento natural con la capacidad inicial especificada
	 */
	public ArraySortedSet(int initialCapacity) {
		this( (Comparator<? super T>) null, initialCapacity);
	}
	
	/**
	 * @post Crea un conjunto con los elementos especificados en orden natural
	 */
	public ArraySortedSet(Collection<? extends T> elements) {
		this( (Comparator<? super T>) null, elements );
	}
	
	/**
	 * @post Crea un conjunto vacío con el comparador especificado (Si se especifica nulo
	 * 		 se usará el orden natural)
	 */
	public ArraySortedSet(Comparator<? super T> comparator) {
		this.elements = new ArrayList<T>();
		this.backingSortedSetSortedListVision = new SortedSetSortedListVision<T>(comparator, this.elements);
	}
	
	/**
	 * @pre La capacidad inicial no puede ser negativa
	 * @post Crea un conjunto vacío con el comparador especificado (Si se especifica nulo
	 * 		 se usará el orden natural) y con la capacidad inicial especificada
	 */
	public ArraySortedSet(Comparator<? super T> comparator, int initialCapacity) {
		this.elements = new ArrayList<T>(initialCapacity);
		this.backingSortedSetSortedListVision = new SortedSetSortedListVision<T>(comparator, this.elements);
	}
	
	/**
	 * @post Crea un conjunto con el comparador y con los elementos especificados, si se
	 * 		 especifica nulo se usará el orden natural
	 */
	public ArraySortedSet(Comparator<? super T> comparator, Collection<? extends T> elements) {
		this(comparator);
		this.addAll(elements);
	}
	
	/**
	 * @post Crea un conjunto con el conjunto ordenado especificado de acuerdo con el orden del mismo
	 */
	public ArraySortedSet(SortedSet<T> sortedSet) {
		this.elements = new ArrayList<T>(sortedSet);
		this.backingSortedSetSortedListVision = new SortedSetSortedListVision<T>(sortedSet.comparator(), this.elements);
	}
	
	@Override
	public Comparator<? super T> comparator() {
		return this.backingSortedSetSortedListVision.comparator();
	}
	
	/**
	 * @post Devuelve si está el elemento especificado
	 * @return
	 */
	public boolean contains(Object element) {
		return this.backingSortedSetSortedListVision.contains(element);
	}
	
	/**
	 * @post Agrega el elemento especificado
	 * @return
	 */
	@Override
	public boolean add(T element) {
		return this.backingSortedSetSortedListVision.add(element);
	}
	
	/**
	 * @post Quita el elemento especificado
	 * @return
	 */
	@Override
	public boolean remove(Object element) {
		return this.backingSortedSetSortedListVision.remove(element);
	}
	
	/**
	 * @post Vacía el conjunto
	 * @return
	 */
	@Override
	public void clear() {
		this.backingSortedSetSortedListVision.clear();
	}
	
	/**
	 * @post Transforma en array
	 * @return
	 */
	@Override
	public Object[] toArray() {
		return this.backingSortedSetSortedListVision.toArray();
	}
	
	/**
	 * @post Transforma en array
	 * @return
	 */
	@Override
	public <V> V[] toArray(V[] array) {
		return this.backingSortedSetSortedListVision.toArray(array);
	}
	

	@Override
	public T first() {
		return this.backingSortedSetSortedListVision.first();
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		return this.backingSortedSetSortedListVision.headSet(toElement);
	}

	@Override
	public T last() {
		return this.backingSortedSetSortedListVision.last();
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return this.backingSortedSetSortedListVision.subSet(fromElement, toElement);
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return this.backingSortedSetSortedListVision.tailSet(fromElement);
	}

	@Override
	public Iterator<T> iterator() {
		return this.backingSortedSetSortedListVision.iterator();
	}

	@Override
	public int size() {
		return this.backingSortedSetSortedListVision.size();
	}
	
	/**
	 * @post Reduce la capacidad de éste conjunto ordenado basado en ArrayList al tamaño actual del conjunto
	 */
	public void trimToSize() {
		this.elements.trimToSize();
	}
}
