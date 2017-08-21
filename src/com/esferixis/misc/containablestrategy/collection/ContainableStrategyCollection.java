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
import java.util.Iterator;

import com.esferixis.misc.containablestrategy.ContainableStrategy;

/**
 * Colección desde la estrategia de contenibles
 * 
 * @param <T>
 */
public interface ContainableStrategyCollection<T> extends Iterable<T> {
	/**
	 * @post Devuelve la estrategia de contenibles
	 */
	public ContainableStrategy getContainableStrategy();
	
	/**
	 * @post Devuelve la colección de contenibles
	 */
	public Collection<T> elements();
	
	/**
	 * @post Devuelve una visión de la colección de elementos que sólo contiene aquellos
	 * 		 elementos que no están contenidos por ningún otro,
	 * 		 o sea los elementos raices
	 * 		 Si se intenta agregar un elemento que está contenido por otros lanza
	 * 		 IllegalArgumentException
	 */
	public Collection<T> getRootElements();
	
	/**
	 * @post Devuelve si contiene el elemento especificado
	 */
	public boolean contains(Object element);
	
	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Devuelve si contiene todos los elementos de la colección especificada
	 */
	public boolean containsAll(ContainableStrategyCollection<?> elements);
	
	/**
	 * @post Devuelve la cantidad de elementos
	 */
	public int size();
	
	/**
	 * @post Devuelve si está vacía
	 */
	public boolean isEmpty();
	
	/**
	 * @post Agrega el elemento especificado y devuelve si hubo cambio
	 */
	public boolean add(T element);
	
	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Agrega todos los elementos de la colección de estrategia de contenibles especificada y devuelve
	 * 		 si hubo algún cambio
	 */
	public boolean addAll(ContainableStrategyCollection<? extends T> elements);
	
	/**
	 * @post Quita todos los elementos contenidos por el elemento especificado y devuelve si ha cambiado
	 * 		 la colección
	 */
	public boolean remove(Object element);
	
	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Quita todos los elementos contenidos por los elementos de la colección de estrategia especificada y devuelve
	 * 		 si hubo cambio
	 */
	public boolean removeAll(ContainableStrategyCollection<?> elements);
	
	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Elimina todos aquellos elementos que no están contenidos en los elementos de la colección
	 * 		 especificada y devuelve si hubo cambio
	 */
	public boolean retainAll(ContainableStrategyCollection<?> elements);
	
	/**
	 * @post Quita todos los elementos de la colección basada en estrategia de contenibles
	 */
	public void clear();
	
	/**
	 * @post Devuelve un array con los elementos
	 */
	public Object[] toArray();
	
	/**
	 * @post Devuelve el array con los elementos de la colección de contenibles si tiene tamaño suficiente
	 * 		 y si tiene un elemento que le sigue inmediatamente al fin de la colección lo deja en "null".
	 * 		 Si los elementos no entran en el array crea un nuevo array.
	 */
	public <V> V[] toArray(V[] array);
	
	/**
	 * @post Devuelve el iterador de elementos
	 */
	@Override
	public Iterator<T> iterator();
	
	/**
	 * @post Devuelve una visión de la colección basada en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 contenidos por el elemento contenedor especificado
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	public ContainableStrategyCollection<T> containedContainableStrategyCollection(Object mandatoryContainer);
	
	/**
	 * @post Devuelve una visión del conjunto basado en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 que contienen el elemento contenible especificado
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	public ContainableStrategyCollection<T> containerContainableStrategyCollection(Object mandatoryContained);
	
	/**
	 * @post Devuelve una visión del conjunto basado en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 que están contenidos por el elemento contenedor y que
	 * 		 contienen al elemento contenible especificados.
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	public ContainableStrategyCollection<T> subContainableStrategyCollection(Object mandatoryContainer, Object mandatoryContained);
	
	/**
	 * @post Devuelve una representación en cadena de carácteres de la colección basada
	 * 		 en estrategia de contenibles
	 */
	@Override
	public String toString();
}
