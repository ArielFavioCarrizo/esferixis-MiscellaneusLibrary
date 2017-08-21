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

/**
 * Iterador de concanetación
 * @param <T>
 */
public class AppendIterator<T> implements Iterator<T> {
	private final Iterator< Iterator<T> > iteratorOfIterators;
	private Iterator<T> storedEachIterator; // Iterador actual en procesamiento
	private Iterator<T> previousElementIterator; // Iterador del elemento anterior
	
	/**
	 * @post Crea un iterador con el iterador de iteradores especificado
	 */
	public AppendIterator(Iterator< Iterator<T> > iteratorOfIterators) {
		this.iteratorOfIterators = iteratorOfIterators;
		this.storedEachIterator = null;
		this.previousElementIterator = null;
	}
	
	/**
	 * @post Devuelve el iterador actual
	 */
	private Iterator<T> getEachIterator() {
		if ( this.storedEachIterator == null ) {
			if ( this.iteratorOfIterators.hasNext() ) {
				this.storedEachIterator = this.iteratorOfIterators.next();
			}
		}
		
		return this.storedEachIterator;
	}
	
	/**
	 * @post Procesa el siguiente iterador
	 */
	private void nextIterator() {
		this.storedEachIterator = null;
	}
	
	@Override
	public boolean hasNext() {
		boolean hasNext;
		
		boolean searchNextIterator;
		do {
			searchNextIterator = false;
			
			final Iterator<T> eachIterator = this.getEachIterator();
			
			if ( eachIterator != null ) { // Si hay iterador actual
				hasNext = eachIterator.hasNext();
				
				if ( !hasNext ) { // Si no hay más elementos en éste iterador
					this.nextIterator(); // Procesar el siguiente iterador
					searchNextIterator = true; // Pasar al siguiente
				}
			}
			else { // Si no hay más iteradores
				hasNext = false; // Entonces no hay más elementos
			}
		} while ( searchNextIterator ); // Si es necesario buscar el siguiente iterador
		
		return hasNext;
	}

	@Override
	public T next() {
		if ( this.hasNext() ) {
			this.previousElementIterator = this.getEachIterator();
			return this.getEachIterator().next();
		}
		else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() {
		if ( this.previousElementIterator != null ) {
			this.previousElementIterator.remove();
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	
}
