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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.esferixis.misc.iterator.AppendIterator;

public class UnionCollection<T> extends AbstractCollection<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7991262466883826022L;
	protected final List< Collection<T> > collections; // Colecciones
	
	/**
	 * @post Crea la unión de colecciones con la lista de colecciones especificada
	 */
	public UnionCollection(List< Collection<T> > collections) {
		this.collections = collections;
	}
	
	/**
	 * @post Crea la unión de colecciones con la lista de colecciones especificada
	 */
	public UnionCollection(Collection<T>... collections) {
		this.collections = Arrays.asList(collections);
	}
	
	/**
	 * @post Devuelve la lista de colecciones
	 */
	public final List< ? extends Collection<T> > getCollections() {
		return this.collections;
	}

	@Override
	public final void clear() {
		for ( Collection<T> eachCollection : this.collections ) {
			eachCollection.clear();
		}
	}

	/**
	 * @post Devuelve si contiene el elemento especificado
	 */
	@Override
	public final boolean contains(Object element) {
		for ( Collection<T> eachCollection : this.collections ) {
			if ( eachCollection.contains(element) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @post Devuelve el iterador
	 */
	@Override
	public final Iterator<T> iterator() {
		final class IteratorOfIterators implements Iterator< Iterator<T> > {
			private Iterator< ? extends Collection<T> > collectionIterator;
			
			/**
			 * @post Crea el iterador
			 */
			public IteratorOfIterators() {
				this.collectionIterator = UnionCollection.this.collections.iterator();
			}
			
			@Override
			public boolean hasNext() {
				return collectionIterator.hasNext();
			}

			@Override
			public Iterator<T> next() {
				return collectionIterator.next().iterator();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		}
		
		return new AppendIterator<T>(new IteratorOfIterators());
	}

	@Override
	public final boolean remove(Object element) {
		boolean changed = false;
		for ( Collection<T> eachCollection : this.collections ) {
			changed |= eachCollection.remove(element);
		}
		return changed;
	}

	@Override
	public final int size() {
		int totalSize=0;
		for ( Collection<T> eachCollection : this.collections ) {
			totalSize += eachCollection.size();
		}
		return totalSize;
	}

	@Override
	public final Object[] toArray() {
		List<T> unionList = new ArrayList<T>();
		for ( Collection<T> eachCollection : this.collections ) {
			unionList.addAll(eachCollection);
		}
		return unionList.toArray();
	}

	@Override
	public final <V> V[] toArray(V[] arg0) {
		List<T> unionList = new ArrayList<T>();
		for ( Collection<T> eachCollection : this.collections ) {
			unionList.addAll(eachCollection);
		}
		return unionList.toArray(arg0);
	}
	
	@Override
	public final int hashCode() {
		int hash=0;
		for ( Collection<T> eachCollection : this.collections ) {
			hash += eachCollection.hashCode();
		}
		return hash;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final boolean equals(Object other) {
		if ( other != null ) {
			if ( other instanceof UnionCollection ) {
				return ( (UnionCollection<T>) other ).collections.equals(this.collections);
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
}
