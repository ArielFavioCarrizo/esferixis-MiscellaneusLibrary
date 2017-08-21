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
package com.esferixis.misc.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.runner.manipulation.Filterable;

import com.esferixis.misc.binaryclassifier.BinaryClassifier;



public final class FilteredIterator<T> implements Iterator<T> {
	private final BinaryClassifier<? super T> binaryClassifier; // Clasificador binario
	private final Iterator<? extends T> sourceIterator; // Iterador origen
	
	private T nextElement; // Elemento siguiente
	
	private boolean hasNextElement; // ¿Hay elemento siguiente?
	private boolean updated;
	
	private boolean hasLastNextElement;
	
	/**
	 * @post Crea una decoración del iterador con el criterio de clasificación para filtrado especificado
	 */
	public FilteredIterator(Iterator<? extends T> sourceIterator, BinaryClassifier<? super T> binaryClassifier) {
		if ( ( sourceIterator != null ) && ( binaryClassifier != null ) ) {
			this.sourceIterator = sourceIterator;
			this.binaryClassifier = binaryClassifier;
			
			this.updated = false;
			
			this.hasLastNextElement = false;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Obtiene el siguiente elemento salteando todos aquellos que no
	 * 		 cumplen con el criterio del clasificador
	 */
	private void update() {
		while ( !this.updated ) {
			if ( this.sourceIterator.hasNext() ) {
				this.nextElement = this.sourceIterator.next();
				
				// Si cumple con el criterio del clasificador
				if ( this.binaryClassifier.evaluate(this.nextElement) ) {
					// Éste elemento será el próximo
					this.hasNextElement = true;
					this.updated = true;
				}
			}
			else { // Si no encontró elementos en el iterador origen
				this.nextElement = null;
				this.hasNextElement = false;
				this.updated = true;
			}
		}
	}

	/**
	 * @post Devuelve si hay próximo elemento
	 */
	@Override
	public boolean hasNext() {
		this.update();
		return this.hasNextElement;
	}

	/**
	 * @pre Tiene que haber próximo elemento
	 * @post Devuelve el próximo elemento
	 */
	@Override
	public T next() {
		if ( this.hasNext() ) {
			final T element = this.nextElement;
			this.updated = false;
			this.hasLastNextElement = true;
			return element;
		}
		else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * @pre Tiene que haber elemento obtenido y no tiene que estar removido
	 * @post Elimina el último elemento obtenido
	 */
	@Override
	public void remove() {
		if ( this.hasLastNextElement ) {
			this.sourceIterator.remove();
			this.hasLastNextElement = false;
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * @pre Ninguno de los dos puede ser nulo
	 * @post Filtra el iteratable con el clasificador binario especificado
	 */
	public static <T> Iterable<T> filteredIterable(final Iterable<T> iterable, final BinaryClassifier<? super T> binaryClassifier) {
		if ( ( iterable != null ) && ( binaryClassifier != null ) ) {
			return new Iterable<T>() {
	
				@Override
				public Iterator<T> iterator() {
					return new FilteredIterator<T>(iterable.iterator(), binaryClassifier);
				}
				
			};
		}
		else {
			throw new NullPointerException();
		}
	}
}
