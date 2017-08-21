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
package com.esferixis.misc.containablestrategy.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.esferixis.misc.containablestrategy.ContainableStrategy;
import com.esferixis.misc.containablestrategy.set.ContainableStrategySet;
import com.esferixis.misc.reference.InmutableReference;



/**
 * 
 * @author Ariel Favio Carrizo
 *
 * @param <K> Clave
 * @param <V> Valor
 */
public interface ContainableStrategyMap<K,V> {
	/**
	 * @post Devuelve la estrategia de contenibles
	 */
	public ContainableStrategy getKeyContainableStrategy();
	
	/**
	 * @post Devuelve el conjunto de entradas.
	 * 		 Éste conjunto no permite agregar elementos.
	 */
	public Set< Entry<K, V> > entrySet();
	
	/**
	 * @post Devuelve el mapa de contenibles asociados con sus valores
	 */
	public Map<K,V> getMap();
	
	/**
	 * @post Devuelve si tiene asociaciones con las claves contenidas en la clave contenedora especificada
	 */
	public boolean containsKey(Object containerKey);
	
	/**
	 * @post Devuelve si hay ambiguedades con la clave especificada
	 */
	public boolean ambiguousKey(Object containerKey);
	
	/**
	 * @post Devuelve una referencia a la entrada asociada.
	 * 		 Si no encuentra una entrada con una clave contenida por la clave
	 * 		 especificada devuelve null.
	 * 		 Si encuentra una entrada con una clave contenida en la clave
	 * 		 especificada que no esté contenida por ninguna otra entonces
	 * 		 devuelve una referencia apuntando a dicha entrada.
	 * 		 Caso contrario hay ambiguedad y devuelve una referencia con valor null.
	 */
	public InmutableReference< Entry<K,V> > getEntry(Object key);
	
	/**
	 * @post Borra todas las asociaciones de las claves contenidas en éste contenedor
	 * 		 y devuelve si hubo algún cambio.
	 */
	public boolean remove(Object containerKey);
	
	/**
	 * @post Devuelve el valor asociado a la clave contenible más profunda,
	 * 		 si hay ambiguedad o no existe devuelve null
	 */
	public V get(Object key);
	
	/**
	 * @post Asigna la clave y el valor especificados y devuelve el valor anterior
	 * 		 asociado si lo hay
	 */
	public V put(K key, V value);
	
	/**
	 * @post Asigna la clave y el valor especificados y devuelve una referencia al
	 * 		 valor anterior si no introduce ambigüedad, caso contrario no lo asigna
	 * 		 y devuelve null
	 */
	public InmutableReference<V> putIfNotAmbiguous(K key, V value);
	
	/**
	 * @pre El mapa no tiene que ser nulo y tiene que ser compatible
	 * @post Asigna las claves y los valores del mapa de contenibles especificado
	 */
	public void putAll(ContainableStrategyMap<? extends K, ? extends V> other);
	
	/**
	 * @post Devuelve la cantidad de asociaciones
	 */
	public int size();
	
	/**
	 * @post Devuelve si está vacío
	 */
	public boolean isEmpty();
	
	/**
	 * @post Vacía el mapa
	 */
	public void clear();
	
	/**
	 * @post Devuelve una visión del mapa basado en estrategia
	 * 		 de contenibles que sólo contiene aquellas claves
	 * 		 contenidas por el elemento contenedor especificado
	 * 
	 * 		 Si se intenta agregar una clave que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	public ContainableStrategyMap<K, V> containedContainableStrategyMap(Object mandatoryContainerKey);
	
	/**
	 * @post Devuelve una visión del mapa basado en estrategia
	 * 		 de contenibles que sólo contiene aquellas claves
	 * 		 que contienen el elemento contenible especificado
	 * 
	 * 		 Si se intenta agregar una clave que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	public ContainableStrategyMap<K, V> containerContainableStrategyMap(Object mandatoryContainedKey);
	
	/**
	 * @post Devuelve una visión del mapa basado en estrategia
	 * 		 de contenibles que sólo contiene aquellas claves que
	 * 		 están contenidas por el elemento contenedor y que
	 * 		 contienen al elemento contenible especificados.
	 * 
	 * 		 Si se intenta agregar una clave que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	public ContainableStrategyMap<K,V> subContainableStrategyMap(Object mandatoryContainerKey, Object mandatoryContainedKey);
	
	/**
	 * @post Devuelve el conjunto de claves visión basado en estrategia de contenibles
	 * 
	 * 		 Si se intenta agregar una clave a través de ésta coleccion lanza UnsupportedOperationException
	 */
	public ContainableStrategySet<K> keySet();
	
	/**
	 * @post Devuelve la colección de valores
	 * 
	 * 		 Si se intenta agregar un valor a través de ésta coleccion lanza UnsupportedOperationException
	 */
	public Collection<V> values();
	
	/**
	 * @post Devuelve si es igual objeto especificado.
	 * 
	 * 		 Se considerará igual si el otro objeto es un mapa basado en estrategia de contenibles
	 * 		 con igual estrategia de contenibles que tenga un conjunto de entradas igual
	 */
	@Override
	public boolean equals(Object other);
	
	/**
	 * @post Devuelve el hash
	 * 
	 * 		 El hash consiste en this.keyContainableStrategy().hashCode() * 31 ^ this.getMap().hashCode()
	 */
	@Override
	public int hashCode();
}
