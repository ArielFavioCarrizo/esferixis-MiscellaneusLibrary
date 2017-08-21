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

import java.util.Iterator;
import java.util.Set;

import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.collection.FilteredCollection;

/**
 * Conjunto filtrado.
 * Brinda una visión de un conjunto con sólo aquellos elementos que pasan por un filtro
 * 
 * @author ariel
 *
 * @param <T>
 */

public class FilteredSet<T> extends FilteredCollection<T> implements Set<T> {
	protected final Set<T> sourceSet;
	
	/**
	 * @pre La colección origen y el filtro no pueden ser nulos
	 * @post Crea la visión con la colección origen y el filtro especificados
	 */
	public FilteredSet(Set<T> sourceSet,
			BinaryClassifier<Object> filter) {
		super(sourceSet, filter);
		this.sourceSet = sourceSet;
	}
	
	/**
	 * @post Devuelve si el objeto especificado es igual a éste
	 * 
	 * 		 Ésta implementación primero evalúa si el otro objeto
	 * 		 no es nulo y es conjunto, si no lo es devuelve false.
	 * 		 Caso contrario recorre con el iterador contando los elementos,
	 * 		 si encuentra que hay menos que el conjunto especificado
	 * 		 o alguno de los elementos no está presente en la otra
	 * 		 devuelve false, caso contrario true.
	 */
	@Override
	public boolean equals(Object other) {
		if ( ( other != null ) && ( other instanceof Set<?> ) ) {
			Set<?> otherSet = (Set<?>) other;
			final int otherSize = otherSet.size();
			final Iterator<T> thisIterator = this.iterator();
			int countedThisElements=0;
			boolean containsAll=true;
			
			while ( thisIterator.hasNext() && ( countedThisElements < otherSize ) && containsAll ) {
				if ( !otherSet.contains( thisIterator.next() ) ) {
					containsAll = false;
				}
				countedThisElements++;
			}
			
			return (countedThisElements == otherSize) && containsAll;
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Devuelve el hash
	 * 		 Consiste en la suma de los "hashes" de todos los elementos
	 * 		 considerando que los elementos nulos tienen hash neutro (0)
	 * 
	 * 		 Ésta implementación lo hace con un iterador y por lo tanto la complejidad es O(n)
	 */
	@Override
	public int hashCode() {
		int hash=0;
		for ( T eachElement : this ) {
			if ( eachElement != null ) {
				hash += eachElement.hashCode();
			}
		}
		
		return hash;
	}
}
