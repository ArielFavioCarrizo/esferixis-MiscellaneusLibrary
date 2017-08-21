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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.iterator.FilteredIterator;



public class FilteredCollection<T> extends AbstractCollection<T> {
	public static class InvalidElementException extends IllegalArgumentException {
		private static final long serialVersionUID = -5393066586476849243L;
		
		/**
		 * @post Crea la excepción con la descripción especificada
		 */
		public InvalidElementException(String message) {
			super(message);
		}
		
		/**
		 * @post Crea la excepción con descripción predeterminada
		 */
		public InvalidElementException() {
			super("Invalid element because it cannot pass through the filter");
		}
	}
	
	protected final Collection<T> sourceCollection;
	protected final BinaryClassifier<Object> filter;
	
	/**
	 * @pre La colección origen y el filtro no pueden ser nulos
	 * @post Crea la visión con la colección origen y el filtro especificados
	 */
	public FilteredCollection(Collection<T> sourceCollection, BinaryClassifier<Object> filter) {
		if ( ( sourceCollection != null ) && ( filter != null ) ) {
			this.sourceCollection = sourceCollection;
			this.filter = filter;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Crea una excepción para cuando se intenta agregar el elemento filtrado
	 * 		 especificado
	 * 
	 * 		 Ésta implementación devuelve InvalidElementException con la descripción
	 * 		 predeterminada
	 */
	protected InvalidElementException createInvalidElementException(T element) {
		return new InvalidElementException();
	}
	
	/**
	 * @pre El elemento tiene que pasar por el filtro
	 * @post Agrega el elemento especificado
	 */
	@Override
	public boolean add(T element) {
		if ( this.filter.evaluate(element) ) {
			return this.sourceCollection.add(element);
		}
		else {
			throw this.createInvalidElementException(element);
		}
	}
	
	/**
	 * @post Devuelve si el elemento especificado pertenece
	 * 
	 * 		 Ésta implementación primero verifica si el elemento pasa por
	 * 		 el filtro, si no pasa devuelve false.
	 * 		 Caso contrario devuelve si pertenece en la colección origen.
	 */
	@Override
	public boolean contains(Object other) {
		if ( this.filter.evaluate(other) ) {
			return this.sourceCollection.contains(other);
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Devuelve el iterador
	 * 
	 * 		 Ésta implementación devuelve el iterador filtrado
	 */
	@Override
	public Iterator<T> iterator() {
		return new FilteredIterator<T>(this.sourceCollection.iterator(), this.filter);
	}
	
	/**
	 * @post Elimina el objeto especificado
	 * 
	 * 		 Ésta implementación borra el elemento especificado sólo si el
	 * 		 elemento pasa a través del filtro
	 */
	public boolean remove(Object element) {
		if ( this.filter.evaluate(element) ) {
			return this.sourceCollection.remove(element);
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Devuelve si está vacío
	 * 
	 * 		 Ésta implementación devuelve !this.iterator().hasNext()
	 */
	@Override
	public boolean isEmpty() {
		return !this.iterator().hasNext();
	}
	
	/**
	 * @post Devuelve la cantidad de elementos de la colección
	 * 
	 * 		 Ésta implementación itera la colección contando la cantidad de elementos
	 * 
	 * 		 Es de orden O(n)
	 */
	public int size() {
		int quantity=0;
		Iterator<T> iterator = this.iterator();
		while ( iterator.hasNext() ) {
			iterator.next();
			quantity++;
		}
		return quantity;
	}
}
