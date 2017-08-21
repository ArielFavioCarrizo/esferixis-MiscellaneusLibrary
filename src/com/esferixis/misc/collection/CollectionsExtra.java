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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.esferixis.misc.GenericComparator;
import com.esferixis.misc.collection.set.BinarySet;



public final class CollectionsExtra {
	private CollectionsExtra() {
		
	}
	
	/**
	 * @post Devuelve las permutaciones entre colecciones de pares de objetos
	 */
	public static <T> Set< BinarySet<T> > permuteCollectionPair(BinarySet< Collection<T> > collectionPair) {
		Set< BinarySet<T> > elementPairs = new HashSet< BinarySet<T> >();
		for ( T eachObject1 : collectionPair.getElement1() ) {
			for ( T eachObject2 : collectionPair.getElement2() ) {
				elementPairs.add( new BinarySet<T>(eachObject1, eachObject2) );
			}
		}
		return elementPairs;
	}
	
	/**
	 * @post Busca el elemento en la lista ordenada especificada con la mayor velocidad posible
	 * 		 para los primeros elementos devolviendo el índice usando el formato de
	 * 		 Collections.binarySearch
	 */
	public static <T> int quadraticSearch(List<T> list, T key, Comparator<T> comparator) {
		final int lastIndex = list.size() - 1;
		int foundedIndex = -list.size() - 1;
		
		int startIndex=0;
		int toIndex=1;
		
		boolean endSearch = false;
		do {
			if ( toIndex >= lastIndex ) {
				toIndex = lastIndex;
				endSearch = true;
			}
			int keyElementComparison = (new GenericComparator<T>(comparator)).compare(key, list.get(toIndex));
			
			if ( keyElementComparison == 0 ) {
				foundedIndex = toIndex;
				endSearch = true;
			}
			else if ( keyElementComparison < 0 ) {
				List<T> subList = list.subList(startIndex, toIndex);
				foundedIndex = Collections.binarySearch(subList, key, comparator);
				if ( foundedIndex < 0 ) {
					foundedIndex -= startIndex;
				}
				else {
					foundedIndex += startIndex;
				}
				endSearch = true;
			}
			
			startIndex = toIndex;
			toIndex *= 2;
		} while ( !endSearch );
		return foundedIndex;
	}
	
	public static <T> int quadraticSearch(List<T> list, T key) {
		return quadraticSearch(list, key, null);
	}
}
