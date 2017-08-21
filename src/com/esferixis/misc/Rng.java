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
package com.esferixis.misc;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Cabecera de métodos diversos para generación de números aleatorios
 * 
 * @author ariel
 *
 */
public class Rng {
	
	/**
	 * @post Devuelve la cantidad de combinaciones especificadas de los elementos especificados
	 */
	public static <T> Set< Set<T> > generateCombinations(Random random, Set<T> elements, int quantityOfCombinations) {
		if ( quantityOfCombinations < 0 ) {
			throw new IllegalStateException("Negative combination quantity");
		}
		Set< Set<T> > combinations = new HashSet< Set<T> >();
		while ( combinations.size() < quantityOfCombinations ) { // Mientras no haya combinaciones suficientes
			// Genera una combinación
			Set<T> combination = new HashSet<T>();
			List<T> remainingElements = new LinkedList<T>(elements);
			int elementsQuantity=random.nextInt(elements.size());
			for ( int i=0;i<elementsQuantity;i++) {
				T element = remainingElements.remove( random.nextInt(remainingElements.size()));
				combination.add( element);
			}
			// Agrega la combinación al conjunto
			combinations.add(combination);
		}
		return combinations;
	}
}
