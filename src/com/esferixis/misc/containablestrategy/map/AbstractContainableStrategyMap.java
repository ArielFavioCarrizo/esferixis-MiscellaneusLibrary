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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.esferixis.misc.binaryclassifier.AndBinaryClassifier;
import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.containablestrategy.ContainableStrategy;
import com.esferixis.misc.containablestrategy.binaryclassifier.ContainedBinaryClassifier;
import com.esferixis.misc.containablestrategy.binaryclassifier.ContainerBinaryClassifier;
import com.esferixis.misc.containablestrategy.set.AbstractContainableStrategySet;
import com.esferixis.misc.containablestrategy.set.ContainableStrategySet;
import com.esferixis.misc.map.KeyFilteredMap;
import com.esferixis.misc.reference.InmutableReference;

import java.util.Set;



/**
 * 
 * @author Ariel Favio Carrizo
 *
 * @param <K> Clave
 * @param <V> Valor
 */
public abstract class AbstractContainableStrategyMap<K,V> implements ContainableStrategyMap<K,V> {
	protected final ContainableStrategy keyContainableStrategy; // Estrategia de contenibles para las claves
	protected final int keyContainableStrategyHashCodeBase;
	
	/**
	 * @pre La estrategia de contenibles no puede ser nula
	 * @post Crea el mapa basado en estrategia de contenibles
	 */
	public AbstractContainableStrategyMap(ContainableStrategy keyContainableStrategy) {
		if ( keyContainableStrategy != null ) {
			this.keyContainableStrategy = keyContainableStrategy;
			this.keyContainableStrategyHashCodeBase = keyContainableStrategy.hashCode() * 31;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	// Entrada simple
	protected static class SimpleEntry<K, V> implements Entry<K, V> {
		private final K key;
		private V value;
		
		/**
		 * @post Crea la entrada con la clave especificada
		 */
		public SimpleEntry(K key) {
			this.key = key;
		}
		
		/**
		 * @post Crea la entrada con la clave y el valor especificados
		 */
		public SimpleEntry(K key, V value) {
			this(key);
			this.setValue(value);
		}
		
		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.value;
		}

		@Override
		public V setValue(V value) {
			return this.value;
		}
		
	}
	
	/**
	 * @post Devuelve la estrategia de contenibles
	 */
	@Override
	public final ContainableStrategy getKeyContainableStrategy() {
		return this.keyContainableStrategy;
	}
	
	/**
	 * @post Devuelve el conjunto de entradas
	 */
	@Override
	public abstract Set< Entry<K, V> > entrySet();
	
	/**
	 * Mapa de entradas
	 * 
	 * Ésta implementación deriva de AbstractMap
	 * y usa los métodos implementados en el mapa basado en estrategia de contenibles
	 * para las operaciones básicas
	 */
	class EntriesMap extends AbstractMap<K,V> {
		/**
		 * @post Devuelve el conjunto de entradas
		 * 
		 * 		 Ésta implementación devuelve el conjunto de entradas del mapa basado en estrategia
		 * 	 	 de contenibles
		 */
		@Override
		public Set<Entry<K, V>> entrySet() {
			return AbstractContainableStrategyMap.this.entrySet();
		}
		
		/**
		 * @post Asocia la clave con el valor especificado
		 * 
		 * 		 Ésta implementación usa el método put del mapa basado en estrategia de contenibles
		 */
		@Override
		public V put(K key, V value) {
			return AbstractContainableStrategyMap.this.put(key, value);
		}
	}
	
	/**
	 * @post Devuelve el mapa de contenibles asociados con sus valores
	 * 
	 * 		 Ésta implementación devuelve una instancia de EntriesMap
	 */
	@Override
	public Map<K,V> getMap() {
		return new EntriesMap();
	}
	
	/**
	 * @post Verifica que la colección de estrategia no sea nula
	 * 		 y tenga una estrategia de contenibles igual
	 * 
	 *		 Si es nula lanza NullPointerException y si la estrategia de contenibles
	 *		 es distinta devuelve IllegalArgumentException
	 */
	protected final void checkContainableStrategyMap(ContainableStrategyMap<?, ?> map) {
		if ( map != null ) {
			if ( !map.getKeyContainableStrategy().equals(this.getKeyContainableStrategy()) ) {
				throw new IllegalArgumentException("Containable strategy mismatch");
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve si tiene asociaciones con las claves contenidas en la clave contenedora especificada
	 * 
	 * 		 Ésta implementación devuelve !this.containedContainableStrategyMap(containerKey).isEmpty();
	 */
	public boolean containsKey(Object containerKey) {
		return !this.containedContainableStrategyMap(containerKey).isEmpty();
	}
	
	/**
	 * @post Devuelve si hay ambiguedades con la clave especificada
	 * 
	 * 		 Ésta implementación obtiene una referencia a la entrada asociada con el método
	 * 		 getEntry, si la referencia no es nula y tiene valor null devuelve true,
	 * 		 caso contrario false
	 */
	public boolean ambiguousKey(Object containerKey) {
		final InmutableReference< Entry<K,V> > entryReference = this.getEntry(containerKey);
		return (entryReference != null) && ( entryReference.get() == null );
	}
	
	/**
	 * @post Devuelve una referencia a la entrada asociada cuya
	 * 		 clave está contenida en la clave maestra especificada.
	 * 		 Si no encuentra una entrada con una clave contenida por la clave
	 * 		 especificada devuelve null.
	 * 		 Si encuentra una entrada con una clave contenida en la clave
	 * 		 especificada que no esté contenida por ninguna otra entonces
	 * 		 devuelve una referencia apuntando a dicha entrada.
	 * 		 Caso contrario hay ambiguedad y devuelve una referencia con valor null.
	 * 
	 * 		 Ésta implementación usa el iterador de entradas del submapa contenedor de la clave especificada y
	 * 		 contenible de la clave maestra para cumplir con la postcondición
	 */
	protected InmutableReference< Entry<K,V> > getEntry(InmutableReference<Object> masterKey, Object key) {
		final Iterator< Entry<K, V> > entryIterator;
		
		final ContainableStrategyMap<K, V> subMap;
		if ( masterKey != null ) {
			subMap = this.subContainableStrategyMap(masterKey.get(), key);
		}
		else {
			subMap = this.containerContainableStrategyMap(key);
		}
		entryIterator = subMap.entrySet().iterator();
		
		Entry<K,V> deepestCandidateEntry = null; // Entrada candidata más profunda registrada
		boolean ambiguousCandidate = false;
		while ( entryIterator.hasNext() ) {
			Entry<K,V> eachEntry = entryIterator.next();
			if ( deepestCandidateEntry == null ) {
				deepestCandidateEntry = eachEntry;
				ambiguousCandidate = false;
			}
			else {
				if ( this.getKeyContainableStrategy().contains(deepestCandidateEntry.getKey(), eachEntry.getKey() ) ) {
					if ( !this.getKeyContainableStrategy().contains( eachEntry.getKey(), deepestCandidateEntry.getKey() ) ) {
						deepestCandidateEntry = eachEntry;
						ambiguousCandidate = false;
					}
					else {
						ambiguousCandidate = true;
					}
				}
			}
		}
		
		final InmutableReference< Entry<K,V> > entryReference;
		if ( deepestCandidateEntry == null ) {
			entryReference = null;
		}
		else {
			final Entry<K, V> associatedEntry;
			if ( ambiguousCandidate ) {
				associatedEntry = null;
			}
			else {
				associatedEntry = deepestCandidateEntry;
			}
			entryReference = new InmutableReference< Entry<K, V> >(associatedEntry);
		}
		
		return entryReference;
	}
	
	/**
	 * @post Devuelve una referencia a la entrada asociada
	 * 		 Si no encuentra una entrada con una clave contenida por la clave
	 * 		 especificada devuelve null.
	 * 		 Si encuentra una entrada con una clave contenida en la clave
	 * 		 especificada que no esté contenida por ninguna otra entonces
	 * 		 devuelve una referencia apuntando a dicha entrada.
	 * 		 Caso contrario hay ambiguedad y devuelve una referencia con valor null.
	 * 
	 * 		 Ésta implementación invoca this.getEntry(null, key)
	 */
	@Override
	public InmutableReference< Entry<K,V> > getEntry(Object key) {
		return this.getEntry(null, key);
	}
	
	/**
	 * @post Borra todas las asociaciones de las claves contenidas en éste contenedor
	 * 		 y devuelve si hubo algún cambio.
	 * 
	 * 		 Esta implementación obtiene un submapa contenido por la clave especificada
	 * 		 Luego evalúa si dicho submapa está vacío, si lo está
	 * 		 devuelve false, caso contrario vacía el submapa y devuelve true
	 */
	public boolean remove(Object containerKey) {
		final ContainableStrategyMap<K, V> containedMap = this.containedContainableStrategyMap(containerKey);
		final boolean notIsEmpty = !containedMap.isEmpty();
		if ( notIsEmpty ) {
			containedMap.clear();
		}
		return notIsEmpty;
	}
	
	/**
	 * @post Devuelve el valor asociado a la clave contenible más profunda,
	 * 		 si hay ambiguedad o no existe devuelve null
	 * 
	 * 		 Ésta implementación usa el método getEntry, si hay
	 * 		 entrada devuelve el valor, caso contrario null
	 */
	public V get(Object key) {
		final InmutableReference< Entry<K, V> > entryReference = this.getEntry(key);
		V value = null;
		if ( entryReference != null ) {
			final Entry<K, V> entry = entryReference.get();
			if ( entry != null ) {
				value = entry.getValue();
			}
		}
		
		return value;
	}
	
	/**
	 * @post Asigna la clave y el valor especificados y devuelve el valor anterior
	 * 		 asociado si lo hay
	 * 
	 * 	     Ésta implementación primero invoca this.get(key) y guarda el
	 * 		 valor recibido, luego invoca this.getMap().put(key, value) y luego
	 * 		 devuelve el valor guardado anteriormente
	 */
	public V put(K key, V value) {
		final V oldValue = this.get(key);
		this.getMap().put(key, value);
		return oldValue;
	}
	
	/**
	 * @post Asigna la clave y el valor especificados y devuelve una referencia al
	 * 		 valor anterior si no introduce ambigüedad, caso contrario no lo asigna
	 * 		 y devuelve null
	 * 
	 * 		 Ésta implementación usa el mapa original (Obtenido con this.getMap()) y el método put
	 */
	@Override
	public InmutableReference<V> putIfNotAmbiguous(K key, V value) {
		// Obtener si está la clave en el mapa original
		final boolean keyInOriginalMap = this.getMap().containsKey(key);
		
		// En caso afirmativo obtener el valor asociado
		final V oldValueOriginalMap;
		if ( keyInOriginalMap ) {
			oldValueOriginalMap = this.getMap().get(key);
		}
		else { 
			oldValueOriginalMap = null;
		}
		
		// Obtener el valor asociado
		final V oldValue = this.put(key, value);
		
		// Referencia de valor de retorno
		final InmutableReference<V> returnValueReference;
		
		// Si no es ambigua
		if ( !this.ambiguousKey(key) ) {
			// Retornar el valor anterior
			returnValueReference = new InmutableReference<V>(oldValue);
		}
		else { // Caso contrario
			returnValueReference = null; // Devolver null
			
			// Si había clave
			if ( keyInOriginalMap ) {
				// Reasociar el valor anterior
				this.getMap().put(key, oldValueOriginalMap);
			}
			else { // Caso contrario
				// Quitar la clave
				this.getMap().remove(key);
			}
		}
		
		return returnValueReference;
	}
	
	/**
	 * @pre El mapa no tiene que ser nulo y tiene que ser compatible
	 * @post Asigna las claves y los valores del mapa de contenibles especificado
	 * 
	 * 		 Ésta implementación primero verifica el mapa especificado
	 * 		 con el método checkContainableStrategyMap,
	 * 		 si resulta exitoso itera el conjunto de entradas agregando cada
	 * 		 asociación con el método put.
	 */
	public void putAll(ContainableStrategyMap<? extends K, ? extends V> other) {
		this.checkContainableStrategyMap(other);
		for ( Entry<? extends K, ? extends V> eachEntry : other.entrySet() ) {
			this.put(eachEntry.getKey(), eachEntry.getValue());
		}
	}
	
	/**
	 * @post Devuelve la cantidad de asociaciones
	 * 
	 * 		 Ésta implementación devuelve this.entrySet().size()
	 */
	@Override
	public int size() {
		return this.entrySet().size();
	}
	
	/**
	 * @post Devuelve si está vacío
	 * 
	 * 		 Ésta implementación devuelve this.entrySet().isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.entrySet().isEmpty();
	}
	
	/**
	 * @post Vacía el mapa
	 * 
	 * 		 Ésta implementación invoca this.entrySet().clear()
	 */
	@Override
	public void clear() {
		this.entrySet().clear();
	}
	
	/**
	 * Submapa simple
	 */
	protected static class SimpleSubMap<K, V> extends AbstractContainableStrategyMap<K, V> {
		protected final AbstractContainableStrategyMap<K, V> owner;
		
		protected final BinaryClassifier<Object> keyFilter;
		
		protected final InmutableReference<Object> mandatoryContainerKey;
		protected final InmutableReference<Object> mandatoryContainedKey;
		
		/**
		 * @pre Por lo menos tiene que haber un contenedor o un contenible
		 * @post Crea el submapa con mapa dueño, el contenedor y el contenible especificados
		 */
		public SimpleSubMap(AbstractContainableStrategyMap<K, V> owner, InmutableReference<Object> mandatoryContainerKey, InmutableReference<Object> mandatoryContainedKey) {
			super(owner.getKeyContainableStrategy());
			this.owner = owner;
			
			BinaryClassifier<Object> keyFilter;
			
			if ( mandatoryContainerKey != null ) {
				keyFilter = new ContainedBinaryClassifier(this.getKeyContainableStrategy(), mandatoryContainerKey.get());
			}
			else {
				keyFilter = null;
			}
			
			if ( mandatoryContainedKey != null ) {
				final BinaryClassifier<Object> containerBinaryClassifier = new ContainerBinaryClassifier(this.getKeyContainableStrategy(), mandatoryContainedKey.get());
				
				if ( keyFilter != null ) {
					keyFilter = new AndBinaryClassifier<Object>(keyFilter, containerBinaryClassifier);
				}
				else {
					keyFilter = containerBinaryClassifier;
				}
			}
			
			if ( keyFilter != null ) {
				this.keyFilter = keyFilter;
				this.mandatoryContainerKey = mandatoryContainerKey;
				this.mandatoryContainedKey = mandatoryContainedKey;
			}
			else {
				throw new NullPointerException();
			}
		}
		
		/**
		 * @post Devuelve el conjunto de entradas
		 * 
		 * 		 Ésta implementación devuelve this.getMap().entrySet()
		 */
		@Override
		public Set< Entry<K, V> > entrySet() {
			return this.getMap().entrySet();
		}
		
		/**
		 * @post Devuelve el mapa de contenibles asociados con sus valores
		 * 
		 * 		 Ésta implementación devuelve el mapa original filtrado
		 */
		@Override
		public Map<K,V> getMap() {
			return new KeyFilteredMap<K,V>( this.owner.getMap(), this.keyFilter);
		}
		
		/**
		 * @post Devuelve una referencia a la entrada asociada
		 * 		 Si no encuentra una entrada con una clave contenida por la clave
		 * 		 especificada devuelve null.
		 * 		 Si encuentra una entrada con una clave contenida en la clave
		 * 		 especificada que no esté contenida por ninguna otra entonces
		 * 		 devuelve una referencia apuntando a dicha entrada.
		 * 		 Caso contrario hay ambiguedad y devuelve una referencia con valor null.
		 * 
		 * 		 Ésta implementación consulta la entrada asociada a la clave
		 * 		 que contiene a la otra entre la clave especificada y la clave contenida requerida
		 * 		 si existe, caso contrario consulta con la clave especificada.
		 * 		 La clave maestra de consulta es la clave contenedora requerida.
		 */
		@Override
		public InmutableReference< Entry<K,V> > getEntry(Object key) {
			Object deepestContainedKey;
			
			if ( this.getKeyContainableStrategy().contains(key, this.mandatoryContainedKey ) ) {
				deepestContainedKey = key;
			}
			else {
				deepestContainedKey = this.mandatoryContainedKey;
			}
				
			return this.owner.getEntry(this.mandatoryContainerKey, deepestContainedKey);
		}
		
		/**
		 * @post Asigna la clave y el valor especificados
		 * 
		 * 		 Eśta implementación invoca this.getMap().put(key, value)
		 */
		public V put(K key, V value) {
			return this.getMap().put(key, value);
		}
	}
	
	/**
	 * @pre Por lo menos tiene que haber un contenedor o un contenible
	 * @post Crea el submapa con las referencias a contenedor y contenible especificados.
	 * 		 Si no hay contenedor o no hay contenible basta con dar el valor null en su lugar
	 */
	protected ContainableStrategyMap<K, V> createSubContainableStrategyMap(InmutableReference<Object> mandatoryContainerKey, InmutableReference<Object> mandatoryContainedKey) {
		return new SimpleSubMap<K, V>(this, mandatoryContainerKey, mandatoryContainedKey);
	}
	
	/**
	 * @post Devuelve una visión del mapa basado en estrategia
	 * 		 de contenibles que sólo contiene aquellas claves
	 * 		 contenidas por el elemento contenedor especificado
	 * 
	 * 		 Si se intenta agregar una clave que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 * 
	 * 		 Ésta implementación devuelve this.createSubContainableStrategyMap(new InmutableReference<Object>(mandatoryContainerKey), null)
	 */
	public ContainableStrategyMap<K, V> containedContainableStrategyMap(Object mandatoryContainerKey) {
		return this.createSubContainableStrategyMap(new InmutableReference<Object>(mandatoryContainerKey), null);
	}
	
	/**
	 * @post Devuelve una visión del mapa basado en estrategia
	 * 		 de contenibles que sólo contiene aquellas claves
	 * 		 que contienen el elemento contenible especificado
	 * 
	 * 		 Si se intenta agregar una clave que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 * 
	 * 		 Ésta implementación devuelve this.createSubContainableStrategyMap(null, new InmutableReference<Object>(mandatoryContainedKey))
	 */
	public ContainableStrategyMap<K, V> containerContainableStrategyMap(Object mandatoryContainedKey) {
		return this.createSubContainableStrategyMap(null, new InmutableReference<Object>(mandatoryContainedKey));
	}
	
	/**
	 * @post Devuelve una visión del mapa basado en estrategia
	 * 		 de contenibles que sólo contiene aquellas claves que
	 * 		 están contenidas por el elemento contenedor y que
	 * 		 contienen al elemento contenible especificados.
	 * 
	 * 		 Si se intenta agregar una clave que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 * 
	 * 		 Ésta implementación devuelve this.createSubContainableStrategyMap(new InmutableReference<Object>(mandatoryContainerKey), new InmutableReference<Object>(mandatoryContainedKey))
	 */
	public ContainableStrategyMap<K,V> subContainableStrategyMap(Object mandatoryContainerKey, Object mandatoryContainedKey) {
		return this.createSubContainableStrategyMap(new InmutableReference<Object>(mandatoryContainerKey), new InmutableReference<Object>(mandatoryContainedKey));
	}
	
	// Conjunto de claves
	protected class SimpleKeySet extends AbstractContainableStrategySet<K> {
		/**
		 * @post Crea el conjunto de claves
		 */
		public SimpleKeySet() {
			super(AbstractContainableStrategyMap.this.getKeyContainableStrategy());
		}
		
		/**
		 * @post Devuelve el conjunto de contenibles
		 * 
		 * 		 Ésta implementación devuelve AbstractContainableStrategyMap.this.getMap().keySet()
		 */
		@Override
		public Set<K> elements() {
			return AbstractContainableStrategyMap.this.getMap().keySet();
		}
		
		/**
		 * @post Devuelve la cantidad de elementos
		 * 
		 * 		 Ésta implementación devuelve el tamaño del mapa
		 */
		@Override
		public int size() {
			return AbstractContainableStrategyMap.this.size();
		}
		
		/**
		 * @post Devuelve si está vacía
		 * 
		 * 		 Ésta implementación devuelve si está vacío el mapa
		 */
		@Override
		public boolean isEmpty() {
			return AbstractContainableStrategyMap.this.isEmpty();
		}
		
		/**
		 * @post Quita todos los elementos contenidos por el elemento especificado y devuelve si ha cambiado
		 * 		 la colección
		 * 
		 * 		 Ésta implementación invoca el método remove del mapa
		 */
		@Override
		public boolean remove(Object element) {
			return AbstractContainableStrategyMap.this.remove(element);
		}
		
		/**
		 * @post Quita todos los elementos de la colección basada en estrategia de contenibles
		 * 
		 * 		 Ésta implementación vacía el mapa
		 */
		public void clear() {
			AbstractContainableStrategyMap.this.clear();
		}
		
		/**
		 * @post Devuelve el iterador de elementos
		 * 
		 * 		 Ésta implementación devuelve this.elements.iterator()
		 */
		@Override
		public Iterator<K> iterator() {
			return this.elements().iterator();
		}
		
		/**
		 * @post Devuelve una visión del conjunto basado en estrategia
		 * 		 de contenibles que sólo contiene aquellos elementos
		 * 		 contenidos por el elemento contenedor especificado
		 * 
		 * 		 Si se intenta agregar un elemento que no cumple con la
		 * 		 condición necesaria de pertenencia se lanza la excepción
		 * 		 IllegalArgumentException
		 * 
		 * 		 Ésta implementación devuelve AbstractContainableStrategyMap.this.containedContainableStrategyMap(mandatoryContainer).keySet()
		 */
		@Override
		public ContainableStrategySet<K> containedContainableStrategyCollection(Object mandatoryContainer) {
			return AbstractContainableStrategyMap.this.containedContainableStrategyMap(mandatoryContainer).keySet();
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
		 * 		 Ésta implementación devuelve AbstractContainableStrategyMap.this.containerContainableStrategyMap(mandatoryContainer).keySet()
		 */
		@Override
		public ContainableStrategySet<K> containerContainableStrategyCollection(Object mandatoryContained) {
			return AbstractContainableStrategyMap.this.containerContainableStrategyMap(mandatoryContained).keySet();
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
		 * 		 Ésta implementación devuelve AbstractContainableStrategyMap.this.subContainableStrategyMap(mandatoryContainer).keySet()
		 */
		@Override
		public ContainableStrategySet<K> subContainableStrategyCollection(Object mandatoryContainer, Object mandatoryContained) {
			return AbstractContainableStrategyMap.this.subContainableStrategyMap(mandatoryContainer, mandatoryContained).keySet();
		}
	}
	
	
	/**
	 * @post Devuelve el conjunto de claves visión basado en estrategia de contenibles
	 * 
	 * 		 Si se intenta agregar una clave a través de ésta coleccion lanza UnsupportedOperationException
	 * 
	 * 		 Ésta implementación devuelve una instancia de SimpleKeySet
	 */
	@Override
	public ContainableStrategySet<K> keySet() {
		return new SimpleKeySet();
	}
	
	/**
	 * @post Devuelve la colección de valores
	 * 
	 * 		 Si se intenta agregar un valor a través de ésta coleccion lanza UnsupportedOperationException
	 * 
	 * 		 Ésta implementación devuelve this.getMap().values()
	 */
	@Override
	public Collection<V> values() {
		return this.getMap().values();
	}
	
	/**
	 * @post Devuelve si es igual objeto especificado.
	 * 
	 * 		 Se considerará igual si el otro objeto es un mapa basado en estrategia de contenibles
	 * 		 con igual estrategia de contenibles que tenga un conjunto de entradas igual
	 */
	@Override
	public boolean equals(Object other) {
		if ( ( other != null ) && ( other instanceof ContainableStrategyMap<?, ?> ) ) {
			ContainableStrategyMap<?, ?> otherContainableStrategyMap = (ContainableStrategyMap<?, ?>) other;
			return ( otherContainableStrategyMap.equals(this.getKeyContainableStrategy()) && otherContainableStrategyMap.getMap().equals(this.getMap()) );
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Devuelve el hash
	 * 
	 * 		 El hash consiste en this.keyContainableStrategy().hashCode() * 31 ^ this.getMap().hashCode()
	 */
	@Override
	public int hashCode() {
		return this.keyContainableStrategyHashCodeBase ^ this.getMap().hashCode();
	}
}
