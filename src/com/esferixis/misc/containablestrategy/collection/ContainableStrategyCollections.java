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
package com.esferixis.misc.containablestrategy.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.esferixis.misc.containablestrategy.ContainableStrategy;

/**
 * Métodos diversos para colecciones desde la estrategia de contenibles, de forma análoga a
 * la clase java.util.Collections
 */
public final class ContainableStrategyCollections {
	
	// Para que no pueda ser instanciada
	private ContainableStrategyCollections() {
		
	}
	
	/**
	 * @pre La estrategia de contenibles no puede ser nula
	 * @post Devuelve una colección basada en estrategia de contenibles inmutable con la estrategia de contenibles y el elemento
	 * 		 especificados
	 */
	public static <T> ContainableStrategyCollection<T> singleton(ContainableStrategy containableStrategy, T element) {
		return new CollectionContainableStrategyCollection<T>(containableStrategy, Collections.singleton(element));
	}
	
	/**
	 * @pre La colección basada en estrategia de contenibles no puede ser nula
	 * @post Crea una visión inmodificable de la colección basada en estrategia de contenibles especificada.
	 * 		 Todo intento de modificación provocará el lanzamiento de la excepción UnsupportedOperationException.
	 */
	public static <T> ContainableStrategyCollection<T> unmodifiableContainableStrategyCollection(ContainableStrategyCollection<T> targetCollection) {
		class UnmodifiableCollection extends AbstractContainableStrategyCollection<T> {
			private final ContainableStrategyCollection<T> sourceCollection;
			
			/**
			 * @pre La colección basada en estrategia de contenibles no puede ser nula
			 * @post Crea la visión inmodificable
			 */
			public UnmodifiableCollection(ContainableStrategyCollection<T> targetCollection) {
				super(targetCollection.getContainableStrategy());
				
				if ( targetCollection != null ) {
					this.sourceCollection = targetCollection;
				}
				else {
					throw new NullPointerException();
				}
			}

			/**
			 * @post Devuelve si contiene el elemento especificado
			 */
			@Override
			public boolean contains(Object containable) {
				return this.sourceCollection.contains(containable);
			}
			
			/**
			 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
			 * @post Devuelve si contiene todos los elementos de la colección especificada
			 */
			@Override
			public boolean containsAll(ContainableStrategyCollection<?> elements) {
				return this.sourceCollection.containsAll(elements);
			}
			
			/**
			 * @post Devuelve la colección de contenibles
			 */
			@Override
			public Collection<T> elements() {
				return Collections.unmodifiableCollection( this.sourceCollection.elements() );
			}
			
			/**
			 * @post Devuelve la cantidad de elementos
			 */
			@Override
			public int size() {
				return this.sourceCollection.size();
			}
			
			/**
			 * @post Devuelve si está vacía
			 */
			@Override
			public boolean isEmpty() {
				return this.sourceCollection.isEmpty();
			}
			
			/**
			 * @post Devuelve un array con los elementos
			 */
			@Override
			public Object[] toArray() {
				return this.sourceCollection.toArray();
			}
			
			/**
			 * @post Devuelve el array con los elementos de la colección de contenibles si tiene tamaño suficiente
			 * 		 y si tiene un elemento que le sigue inmediatamente al fin de la colección lo deja en "null".
			 * 		 Si los elementos no entran en el array crea un nuevo array.
			 */
			@Override
			public <V> V[] toArray(V[] array) {
				return this.sourceCollection.toArray(array);
			}

			/**
			 * @post Devuelve el iterador de elementos
			 */
			@Override
			public Iterator<T> iterator() {
				return this.elements().iterator();
			}
			
			/**
			 * @post Devuelve el hash
			 * 
			 * 		 Ésta implementación invoca el método original
			 */
			@Override
			public int hashCode() {
				return this.sourceCollection.hashCode();
			}
			
			/**
			 * @post Devuelve si es igual al objeto especificado
			 * 
			 * 		 Ésta implementación invoca el método original
			 */
			@Override
			public boolean equals(Object other) {
				return this.sourceCollection.equals(other);
			}
			
			/**
			 * @post Devuelve una visión de la colección basada en estrategia
			 * 		 de contenibles que sólo contiene aquellos elementos
			 * 		 contenidos por el elemento contenedor especificado
			 * 
			 * 		 Si se intenta agregar un elemento que no cumple con la
			 * 		 condición necesaria de pertenencia se lanza la excepción
			 * 		 IllegalArgumentException
			 * 
			 * 		 Ésta implementación devuelve una visión inmodificable
			 * 		 de la colección devuelta por la colección original
			 */
			@Override
			public ContainableStrategyCollection<T> containedContainableStrategyCollection(Object mandatoryContainer) {
				return unmodifiableContainableStrategyCollection( this.sourceCollection.containedContainableStrategyCollection(mandatoryContainer) );
			}
			
			/**
			 * @post Devuelve una visión del conjunto basado en estrategia
			 * 		 de contenibles que sólo contiene aquellos elementos
			 * 		 que contienen el elemento contenible especificado
			 * 
			 * 		 Si se intenta agregar un elemento que no cumple con la
			 * 		 condición necesaria de pertenencia se lanza la excepción
			 * 		 IllegalArgumentException
			 * 
			 * 		 Ésta implementación devuelve una visión inmodificable
			 * 		 de la colección devuelta por la colección original
			 */
			public ContainableStrategyCollection<T> containerContainableStrategyCollection(Object mandatoryContained) {
				return unmodifiableContainableStrategyCollection( this.sourceCollection.containerContainableStrategyCollection(mandatoryContained) );
			}
			
			/**
			 * @post Devuelve una visión del conjunto basado en estrategia
			 * 		 de contenibles que sólo contiene aquellos elementos
			 * 		 que están contenidos por el elemento contenedor y que
			 * 		 contienen al elemento contenible especificados.
			 * 
			 * 		 Si se intenta agregar un elemento que no cumple con la
			 * 		 condición necesaria de pertenencia se lanza la excepción
			 * 		 IllegalArgumentException
			 * 
			 * 		 Ésta implementación devuelve una visión inmodificable
			 * 		 de la colección devuelta por la colección original
			 */
			public ContainableStrategyCollection<T> subContainableStrategyCollection(Object mandatoryContainer, Object mandatoryContained) {
				return unmodifiableContainableStrategyCollection( this.sourceCollection.subContainableStrategyCollection(mandatoryContainer, mandatoryContained) );
			}
			
		}
		
		if ( targetCollection != null ) {
			return new UnmodifiableCollection(targetCollection);
		}
		else {
			throw new NullPointerException();
		}
	}
}
