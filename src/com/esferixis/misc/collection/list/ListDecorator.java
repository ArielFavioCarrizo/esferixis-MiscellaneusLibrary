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
package com.esferixis.misc.collection.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Cabecera de un decorador de listas
 * 
 * @author ariel
 *
 * @param <T>
 */
public class ListDecorator<T> implements List<T> {

	protected final List<T> decoratedList; // Conjunto decorado
	
	/**
	 * @post Crea un decorador de la lista
	 */
	public ListDecorator(List<T> decoratedList) {
		this.decoratedList = decoratedList;
	}
	
	@Override
	public boolean add(T arg0) {
		return this.decoratedList.add(arg0);
	}

	@Override
	public void add(int arg0, T arg1) {
		this.decoratedList.add(arg0, arg1);
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		return this.decoratedList.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		return this.decoratedList.addAll(arg0, arg1);
	}

	@Override
	public void clear() {
		this.decoratedList.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return this.decoratedList.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return this.decoratedList.contains(arg0);
	}

	@Override
	public T get(int arg0) {
		return this.decoratedList.get(arg0);
	}

	@Override
	public int indexOf(Object arg0) {
		return this.decoratedList.indexOf(arg0);
	}

	@Override
	public boolean isEmpty() {
		return this.decoratedList.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return this.decoratedList.iterator();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return this.decoratedList.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<T> listIterator() {
		return this.decoratedList.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		return this.decoratedList.listIterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return this.decoratedList.remove(arg0);
	}

	@Override
	public T remove(int arg0) {
		return this.decoratedList.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return this.decoratedList.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return this.decoratedList.retainAll(arg0);
	}

	@Override
	public T set(int arg0, T arg1) {
		return this.decoratedList.set(arg0, arg1);
	}

	@Override
	public int size() {
		return this.decoratedList.size();
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		return this.decoratedList.subList(arg0, arg1);
	}

	@Override
	public Object[] toArray() {
		return this.decoratedList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return this.decoratedList.toArray(arg0);
	}
	
}
