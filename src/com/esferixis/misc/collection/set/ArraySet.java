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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Conjunto basado en ArrayList
 * 
 * @author ariel
 *
 * @param <T>
 */
public class ArraySet<T> extends AbstractSet<T> {
	private List<T> elements;

	/**
	 * @post Crea el conjunto vacío
	 */
	public ArraySet() {
		this.elements = new ArrayList<T>();
	}
	
	/**
	 * @post Crea el conjunto con los elementos especificados
	 */
	public ArraySet(Collection<? extends T> elements) {
		this();
		this.addAll(elements);
	}
	
	/**
	 * @post Crea un conjunto vacío con la capacidad inicial especificada
	 */
	public ArraySet(int initialCapacity) {
		this.elements = new ArrayList<T>(initialCapacity);
	}
	
	/**
	 * @post Devuelve la cantidad de elementos
	 */
	public int size() {
		return this.elements.size();
	}
	
	/**
	 * @post Devuelve el iterador
	 */
	public Iterator<T> iterator() {
		return this.elements.iterator();
	}
	
	/**
	 * @post Devuelve si contiene el elemento especificado
	 */
	@Override
	public boolean contains(Object element) {
		return this.elements.contains(element);
	}
	
	/**
	 * @post Devuelve si contiene los elementos especificados
	 */
	@Override
	public boolean containsAll(Collection<?> elements) {
		return this.elements.containsAll(elements);
	}
	
	/**
	 * @post Agrega el elemento especificado
	 */
	@Override
	public boolean add(T element) {
		boolean changed;
		if ( !this.contains(element) ) {
			changed = this.elements.add(element);
		}
		else {
			changed = false;
		}
		return changed;
	}
	
	/**
	 * @post Quita el elemento especificado
	 */
	@Override
	public boolean remove(Object element) {
		return this.elements.remove(element);
	}
	
	/**
	 * @post Quita los elementos especificados
	 */
	@Override
	public boolean removeAll(Collection<?> elements) {
		return this.elements.removeAll(elements);
	}
	
	/**
	 * @post Quita todos los elementos que no pertenezca a la colección especificada
	 */
	@Override
	public boolean retainAll(Collection<?> elements) {
		return this.elements.retainAll(elements);
	}
	
	/**
	 * @post Quita todos los elementos
	 */
	@Override
	public void clear() {
		this.elements.clear();
	}
	
	/**
	 * @post Convierte a array
	 */
	@Override
	public Object[] toArray() {
		return this.elements.toArray();
	}
	
	/**
	 * @post Convierte a array
	 */
	@Override
	public <V> V[] toArray(V[] array) {
		return this.elements.toArray(array);
	}
	
	/**
	 * @post Convierte a String
	 */
	@Override
	public String toString() {
		return this.elements.toString();
	}
}
