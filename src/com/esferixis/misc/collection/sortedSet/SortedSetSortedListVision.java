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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

public class SortedSetSortedListVision<T> extends AbstractSet<T> implements SortedSet<T> {
	private final Comparator<? super T> comparator;
	private List<T> backingList;
	
	// Visión recortada por elementos límite (Usada para headSet, subSet y tailSet)
	private class ClippedView extends AbstractSet<T> implements SortedSet<T> {

		private final T fromElement, toElement;
		
		/**
		 * @post Crea un visión con los elementos límites especificados
		 */
		public ClippedView(T fromElement, T toElement) {
			this.fromElement = fromElement;
			this.toElement = toElement;
		}
		
		@Override
		public Comparator<? super T> comparator() {
			return SortedSetSortedListVision.this.comparator;
		}
		
		/**
		 * @post Devuelve si el elemento está dentro del rango de la visión
		 */
		private boolean isInRange(Object element) {
			boolean outOfRange = false;
			if ( this.comparator() != null ) {
				if ( this.fromElement != null ) {
					if ( this.comparator().compare((T) element, this.fromElement) < 0) {
						outOfRange = true;
					}
				}
				
				if ( this.toElement != null ) {
					if ( this.comparator().compare((T) element, this.toElement) >= 0) {
						outOfRange = true;
					}
				}
			}
			else {
				if ( this.fromElement != null ) {
					if ( ( (Comparable<T>) element ).compareTo( this.fromElement ) < 0) {
						outOfRange = true;
					}
				}
				
				if ( this.toElement != null ) {
					if ( ( (Comparable<T>) element ).compareTo( this.toElement ) >= 0) {
						outOfRange = true;
					}
				}
			}
			return (!outOfRange);
		}
		
		/**
		 * @post Devuelve la sublista de elementos correspondiente a la visión
		 */
		private List<T> getElementsSubList() {
			int firstElementIndex = this.getFirstElementIndex();
			int lastElementIndex = this.getLastElementIndex();
			if ( (firstElementIndex != -1) && ( lastElementIndex != -1 ) ) {
				return SortedSetSortedListVision.this.backingList.subList(firstElementIndex, lastElementIndex+1);
			}
			else {
				return Collections.emptyList();
			}
		}
		
		/**
		 * @post Devuelve si contiene el elemento especificado
		 */
		@Override
		public boolean contains(Object element) {
			List<T> subList = this.getElementsSubList();
			int index = Collections.binarySearch(subList, (T) element, SortedSetSortedListVision.this.comparator());
			return ( index >= 0 ) && (index != subList.size());
		}
		
		/**
		 * @post Agrega un elemento
		 */
		@Override
		public boolean add(T element) {
			if ( this.isInRange(element) ) {
				return SortedSetSortedListVision.this.add(element);
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		
		/**
		 * @post Quita un elemento
		 */
		@Override
		public boolean remove(Object element) {
			if ( this.isInRange(element) ) {
				return SortedSetSortedListVision.this.remove(element);
			}
			else {
				return false;
			}
		}
		
		/**
		 * @post Quita todos los elementos que están dentro de la visión
		 */
		public void clear() {
			this.getElementsSubList().clear();
		}

		/**
		 * @post Devuelve el índice del primer elemento en la lista de elementos
		 */
		private int getFirstElementIndex() {
			int firstElementCandidateIndex;
			
			// Obtener el primer elemento que está a partir del elemento límite inferior si lo hay
			if ( this.fromElement != null ) {
				int index = Collections.binarySearch(SortedSetSortedListVision.this.backingList, this.fromElement, this.comparator());
				if ( index != SortedSetSortedListVision.this.backingList.size() ) {
					if ( index < 0 ) {
						index = -(index+1);
					}
					firstElementCandidateIndex = index;
				}
				else {
					firstElementCandidateIndex = -1;
				}
			}
			else {
				firstElementCandidateIndex = 0;
			}
			
			// Si el elemento candidato es igual o superior al límite superior no hay elemento primero
			if ( ( this.toElement != null ) && ( firstElementCandidateIndex != -1 ) ) {
				if ( this.comparator() != null ) {
					if ( this.comparator().compare(SortedSetSortedListVision.this.backingList.get( firstElementCandidateIndex ), this.toElement) >= 0 ) {
						firstElementCandidateIndex = -1;
					}
				}
				else {
					if ( ( (Comparable<T>) SortedSetSortedListVision.this.backingList.get( firstElementCandidateIndex ) ).compareTo(toElement) >= 0) {
						firstElementCandidateIndex = -1;
					}
				}
			}
			return firstElementCandidateIndex;
		}
		
		/**
		 * @post Devuelve el índice del último elemento en la lista de elementos
		 */
		private int getLastElementIndex() {
			int lastElementCandidateIndex;
			
			// Obtener el último elemento que es menor al elemento límite superior si lo hay
			if ( this.toElement != null ) {
				int index = Collections.binarySearch(SortedSetSortedListVision.this.backingList, this.toElement, this.comparator());
				if ( index != SortedSetSortedListVision.this.backingList.size() ) {
					if ( index < 0 ) {
						index = -(index + 1);
					}
					index--;
					
					if ( index >= 0 ) {
						lastElementCandidateIndex = index;
					}
					else {
						lastElementCandidateIndex = -1;
					}
				}
				else {
					lastElementCandidateIndex = SortedSetSortedListVision.this.size()-1;
				}
			}
			else {
				lastElementCandidateIndex = SortedSetSortedListVision.this.size()-1;
			}
			
			// Si el elemento candidato es menor al límite inferior no hay elemento último
			if ( ( this.toElement != null ) && ( lastElementCandidateIndex != -1 ) ) {
				if ( this.comparator() != null ) {
					if ( this.comparator().compare(SortedSetSortedListVision.this.backingList.get( lastElementCandidateIndex ), this.fromElement) < 0 ) {
						lastElementCandidateIndex = -1;
					}
				}
				else {
					if ( ( (Comparable<T>) SortedSetSortedListVision.this.backingList.get( lastElementCandidateIndex ) ).compareTo(fromElement) < 0) {
						lastElementCandidateIndex = -1;
					}
				}
			}
			return lastElementCandidateIndex;
		}
		
		@Override
		public T first() {
			int index = this.getFirstElementIndex();
			if ( index != -1 ) {
				return SortedSetSortedListVision.this.backingList.get(index);
			}
			else {
				return null;
			}
		}

		@Override
		public SortedSet<T> headSet(T toElement) {
			return this.subSet(null, toElement);
		}

		@Override
		public T last() {
			int index = this.getLastElementIndex();
			if ( index != -1 ) {
				return SortedSetSortedListVision.this.backingList.get(index);
			}
			else {
				return null;
			}
		}

		@Override
		public SortedSet<T> subSet(T fromElement, T toElement) {
			if ( ( fromElement != null ) && ( this.fromElement != null ) ) {
				if ( this.comparator() != null ) {
					if ( this.comparator().compare(fromElement, this.fromElement) < 0 ) {
						fromElement = this.fromElement;
					}
				}
				else {
					if ( ( (Comparable<T>) fromElement ).compareTo(fromElement) < 0) {
						fromElement = this.fromElement;
					}
				}
			}
			else {
				if ( fromElement == null ) {
					fromElement = this.fromElement;
				}
			}
			
			if ( ( toElement != null ) && ( this.toElement != null ) ) {
				if ( this.comparator() != null ) {
					if ( this.comparator().compare(toElement, this.toElement) > 0 ) {
						toElement = this.toElement;
					}
				}
				else {
					if ( ( (Comparable<T>) toElement ).compareTo(toElement) > 0) {
						toElement = this.toElement;
					}
				}
			}
			else {
				if ( toElement == null ) {
					toElement = this.toElement;
				}
			}
			
			return new ClippedView(fromElement, toElement);
		}

		@Override
		public SortedSet<T> tailSet(T fromElement) {
			return this.subSet(fromElement, null);
		}

		@Override
		public Iterator<T> iterator() {
			return this.getElementsSubList().iterator();
		}

		@Override
		public int size() {
			return this.getElementsSubList().size();
		}
		
		/**
		 * @post Transforma en array
		 * @return
		 */
		@Override
		public Object[] toArray() {
			return this.getElementsSubList().toArray();
		}
		
		/**
		 * @post Transforma en array
		 * @return
		 */
		@Override
		public <V> V[] toArray(V[] array) {
			return this.getElementsSubList().toArray(array);
		}
		
	}
	
	/**
	 * @pre La lista tiene que permanecer ordenada
	 * @post Crea una visión de conjunto de la lista ordenada asumiendo que está en el orden natural
	 */
	public SortedSetSortedListVision(List<T> backingList) {
		this(null, backingList);
	}
	
	/**
	 * @pre La lista tiene que permanecer ordenada
	 * @post Crea una visión de conjunto de la lista ordenada con el comparador especificado
	 */
	public SortedSetSortedListVision(Comparator<? super T> comparator, List<T> backingList) {
		this.comparator = comparator;
		this.backingList = new ArrayList<T>(backingList);
	}
	
	@Override
	public Comparator<? super T> comparator() {
		return this.comparator;
	}
	
	/**
	 * @post Devuelve si está el elemento especificado
	 * @return
	 */
	public boolean contains(Object element) {
		int index = Collections.binarySearch(this.backingList, (T) element, this.comparator());
		return ( index >= 0 ) && (index != this.backingList.size());
	}
	
	/**
	 * @post Agrega el elemento especificado
	 * @return
	 */
	@Override
	public boolean add(T element) {
		int index = Collections.binarySearch(this.backingList, element, this.comparator());
		if ( index < 0 ) {
			index = -(index + 1);
		}
		else if ( index != this.backingList.size() ) {
			index = -1;
		}
		if ( index != -1 ) {
			this.backingList.add(index, element);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Quita el elemento especificado
	 * @return
	 */
	@Override
	public boolean remove(Object element) {
		int index = Collections.binarySearch(this.backingList, (T) element, this.comparator());
		if ( ( index >= 0 ) && ( index < this.backingList.size() ) ) {
			this.backingList.remove(index);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Vacía el conjunto
	 * @return
	 */
	@Override
	public void clear() {
		this.backingList.clear();
	}
	
	/**
	 * @post Transforma en array
	 * @return
	 */
	@Override
	public Object[] toArray() {
		return this.backingList.toArray();
	}
	
	/**
	 * @post Transforma en array
	 * @return
	 */
	@Override
	public <V> V[] toArray(V[] array) {
		return this.backingList.toArray(array);
	}
	

	@Override
	public T first() {
		if ( !this.isEmpty() ) {
			return this.backingList.get(0);
		}
		else {
			return null;
		}
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		return new ClippedView(null, toElement);
	}

	@Override
	public T last() {
		if ( !this.isEmpty() ) {
			return this.backingList.get(this.backingList.size()-1);
		}
		else {
			return null;
		}
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return new ClippedView(fromElement, toElement);
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return new ClippedView(fromElement, null);
	}

	@Override
	public Iterator<T> iterator() {
		return this.backingList.iterator();
	}

	@Override
	public int size() {
		return this.backingList.size();
	}
	
}
