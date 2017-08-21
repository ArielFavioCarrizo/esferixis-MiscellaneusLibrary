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

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Conjunto basado en HashSet y ArrayList
 * 
 * @author ariel
 *
 * @param <T>
 */

public class ArrayHashSet<T> extends AbstractSet<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4503415663817308951L;
	
	private transient List<T> elementList;
	private final Set<T> elementSet;
	
	private boolean pendingUpdate;
	
	/**
	 * @post Crea un conjunto vacío
	 */
	public ArrayHashSet() {
		this.elementSet = new HashSet<T>();
		this.elementList = new ArrayList<T>();
		
		this.pendingUpdate = false;
	}
	
	/**
	 * @post Crea un conjunto con los elementos especificados
	 */
	public ArrayHashSet(Collection<? extends T> elements) {
		this();
		this.addAll(elements);
	}
	
	private void readObject(java.io.ObjectInputStream stream) throws ClassNotFoundException, IOException {
		stream.defaultReadObject();
		this.elementList = new ArrayList<T>();
	}
	
	/**
	 * @post Crea un conjunto vacío con la capacidad inicial especificada
	 */
	public ArrayHashSet(int initialCapacity) {
		this.elementSet = new HashSet<T>(initialCapacity);
		this.elementList = new ArrayList<T>(initialCapacity);
	}
	
	/**
	 * @post Crea un conjunto vacío con la capacidad inicial y el factor de carga especificados
	 */
	public ArrayHashSet(int initialCapacity, int loadFactor) {
		this.elementSet = new HashSet<T>(initialCapacity, loadFactor);
		this.elementList = new ArrayList<T>(initialCapacity);
	}
	
	/**
	 * @post Devuelve si está presente el elemento especificado
	 */
	@Override
	public boolean contains(Object element) {
		return this.elementSet.contains(element);
	}
	
	/**
	 * @post Devuelve si están presentes los elementos especificados
	 */
	@Override
	public boolean containsAll(Collection<?> elements) {
		return this.elementSet.containsAll(elements);
	}
	
	/**
	 * @post Agrega un elemento
	 */
	@Override
	public boolean add(T element) {
		final boolean changed = this.elementSet.add(element);
		if ( changed ) {
			this.elementList.add(element);
		}
		return changed;
	}
	
	/**
	 * @post Quita un elemento
	 */
	@Override
	public boolean remove(Object element) {
		final boolean changed = this.elementSet.remove(element);
		
		if ( changed ) {
			if ( !this.pendingUpdate ) {
				this.elementList.clear();
				this.pendingUpdate = true;
			}
		}
		
		return changed;
	}
	
	/**
	 * @post Devuelve el tamaño del conjunto
	 */
	@Override
	public int size() {
		return this.elementSet.size();
	}
	
	/**
	 * @post Actualiza la lista
	 * @return
	 */
	private void update() {
		if ( this.pendingUpdate ) {
			this.elementList.addAll(this.elementSet);
			this.pendingUpdate = false;
		}
	}

	@Override
	public Iterator<T> iterator() {
		this.update();
		
		// Iterador local
		class LocalIterator implements Iterator<T> {
			private Iterator<T> listIterator;
			private T lastElement;
			
			/**
			 * @post Crea el iterador
			 */
			public LocalIterator() {
				this.listIterator = ArrayHashSet.this.elementList.iterator();
				this.lastElement = null;
			}
			
			@Override
			public boolean hasNext() {
				return this.listIterator.hasNext();
			}

			@Override
			public T next() {
				this.lastElement = this.listIterator.next();
				return this.lastElement;
			}

			@Override
			public void remove() {
				if ( !this.hasNext() ) {
					throw new NoSuchElementException();
				}
				
				this.listIterator.remove();
				ArrayHashSet.this.elementSet.remove(this.lastElement);
			}
			
		}
		return new LocalIterator();
	}
	
	/**
	 * @post Quita todos los elementos
	 */
	@Override
	public void clear() {
		this.elementSet.clear();
		this.elementList.clear();
		
		this.pendingUpdate = false;
	}
	
	/**
	 * @post Convierte a array
	 */
	@Override
	public Object[] toArray() {
		return this.elementList.toArray();
	}
	
	/**
	 * @post Convierte a array
	 */
	@Override
	public <V> V[] toArray(V[] array) {
		return this.elementList.toArray(array);
	}
	
	/**
	 * @post Convierte a String
	 */
	@Override
	public String toString() {
		return this.elementList.toString();
	}
}
