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
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.collection.set.FilteredSet;
import com.esferixis.misc.collection.set.TransformedSet;
import com.esferixis.misc.containablestrategy.ExplicitContainableStrategy;
import com.esferixis.misc.containablestrategy.binaryclassifier.ContainedBinaryClassifier;
import com.esferixis.misc.containablestrategy.binaryclassifier.ContainerBinaryClassifier;
import com.esferixis.misc.iterator.FilteredIterator;
import com.esferixis.misc.map.ValueFilteredMap;
import com.esferixis.misc.reference.DynamicReference;
import com.esferixis.misc.reference.InmutableReference;
import com.esferixis.misc.relation.Function;

/**
 * Éste mapa basado en estrategia de contenibles tiene un WeakHashMap
 * que almacena las asociaciones entre las claves y los valores.
 * Incluye todas las claves agregadas explícitamente y todas aquellas claves contenidas
 * descubiertas pero éstas últimas pueden ser reclamadas por el Garbage Collector
 * ya que están mantenidas con referencias débiles (WeakReferences).
 * Está pensado para que las consultas con el método getEntry y get en
 * las claves existentes sin ambigüedad sean rápidas.
 * Las entradas originales las guarda en un HashMap
 * 
 * @author Ariel Favio Carrizo
 *
 * @param <K> Clase de clave
 * @param <V> Clase de valor
 */

public final class WeakCachedHashExplicitContainableStrategyMap<K, V> extends AbstractContainableStrategyMap<K, V> {
	
	private static final int DEFAULT_INITIAL_CAPACITY = 16;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	// Cápsula de valor
	private class ValueCapsule implements Entry<K, V> {
		private final K key; // Clave original
		private V value; // Valor
		
		/**
		 * @post Crea la cápsula de valor con la clave especificada
		 */
		public ValueCapsule(K key) {
			this.key = key;
		}
		
		/**
		 * @post Devuelve la clave original
		 */
		@Override
		public K getKey() {
			return this.key;
		}
		
		/**
		 * @post Devuelve el valor
		 */
		public V getValue() {
			return this.value;
		}
		
		/**
		 * @post Asigna el valor
		 */
		@Override
		public V setValue(V value) {
			this.value = value;
			return value;
		}
		
		/**
		 * @post Borra su caché
		 */
		public void eraseCache() {
			WeakCachedHashExplicitContainableStrategyMap.this.keyToValueCapsuleMapper.values().removeAll( Collections.singleton(this) );
		}
		
		/**
		 * @post Devuelve la representación en cadena
		 */
		@Override
		public String toString() {
			return super.toString() + "=( '" + this.key + "' -> '" + this.value + "' )";
		}
		
	}
	
	private final ExplicitContainableStrategy<K> keyContainableStrategy;
	private final Map<K, ValueCapsule> keyToValueCapsuleMapper;
	private final OriginalMap originalMap;
	
	/**
	 * @pre La estrategia de contenibles no puede ser nula
	 * @post Crea el mapa basado en estrategia de contenibles con la estrategia de contenibles, 
	 * 		 la capacidad inicial y el factor de carga especificados
	 */
	public WeakCachedHashExplicitContainableStrategyMap(
			ExplicitContainableStrategy<K> keyContainableStrategy, int initialCapacity, float loadFactor) {
		super(keyContainableStrategy);
		this.keyContainableStrategy = keyContainableStrategy;
		this.keyToValueCapsuleMapper = new WeakHashMap<K, ValueCapsule>(initialCapacity, loadFactor);
		this.originalMap = new OriginalMap();
	}
	
	/**
	 * @pre La estrategia de contenibles no puede ser nula
	 * @post Crea el mapa basado en estrategia de contenibles con la estrategia de contenibles
	 * 		 y la capacidad inicial especificada
	 */
	public WeakCachedHashExplicitContainableStrategyMap(
			ExplicitContainableStrategy<K> keyContainableStrategy, int initialCapacity) {
		this(keyContainableStrategy, initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * @pre La estrategia de contenibles no puede ser nula
	 * @post Crea el mapa basado en estrategia de contenibles con la estrategia de contenibles
	 * 		 especificada
	 */
	public WeakCachedHashExplicitContainableStrategyMap(
			ExplicitContainableStrategy<K> keyContainableStrategy) {
		super(keyContainableStrategy);
		this.keyContainableStrategy = keyContainableStrategy;	
		this.keyToValueCapsuleMapper = new WeakHashMap<K, ValueCapsule>(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
		this.originalMap = new OriginalMap();
	}
	
	/**
	 * @pre La estrategia de contenibles ni el mapa pueden ser nulos
	 * @post Crea el mapa basado en estrategia de contenibles con la estrategia de contenibles
	 * 		 y el mapa especificado
	 */
	public <P extends K, Q extends V> WeakCachedHashExplicitContainableStrategyMap(
			ExplicitContainableStrategy<K> keyContainableStrategy, final Map<P, Q> map) {
		super(keyContainableStrategy);
		this.keyContainableStrategy = keyContainableStrategy;
		
		// Implementación similar al HashMap de JDK
		this.keyToValueCapsuleMapper = new WeakHashMap<K, ValueCapsule>( Math.max((int) (map.size() / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR );
		
		this.originalMap = new OriginalMap(map);
	}
	
	/**
	 * @post Devuelve si la caché está vacía
	 */
	public boolean cacheIsEmpty() {
		return this.keyToValueCapsuleMapper.isEmpty();
	}
	
	/**
	 * @post Borra la caché
	 */
	public void clearCache() {
		this.keyToValueCapsuleMapper.clear();
	}
	
	/**
	 * @post Devuelve el conjunto de entradas.
	 * 		 Éste conjunto no permite agregar elementos.
	 */
	public Set< Entry<K, V> > entrySet() {
		return this.getMap().entrySet();
	}
	
	/**
	 * @post Suprime la caché de todas las claves contenidas por la clave
	 * 		 especificada que sean de claves que contengan la clave especificada
	 */
	private void clearContainedKeysCacheFromContainers(K key) {
		final BinaryClassifier<Object> containedBinaryClassifier = new ContainedBinaryClassifier(this.keyContainableStrategy, key);
		final BinaryClassifier<Object> containerBinaryClassifier = new ContainerBinaryClassifier(this.keyContainableStrategy, key);
		final Iterator< Entry<K, ValueCapsule> > entryIterator = this.keyToValueCapsuleMapper.entrySet().iterator();
		while ( entryIterator.hasNext() ) {
			final Entry<K, ValueCapsule> eachEntry = entryIterator.next();
			if ( containedBinaryClassifier.evaluate(eachEntry.getKey()) ) {
				final ValueCapsule eachValueCapsule = eachEntry.getValue();
				if ( containerBinaryClassifier.evaluate(eachValueCapsule.getKey()) ) {
					entryIterator.remove();
				}
			}
		}
	}
	
	private class OriginalMap extends AbstractMap<K, V> {
		private Map<K, ValueCapsule> backingMap;
		
		/**
		 * @post Crea el mapa vacío
		 */
		public OriginalMap() {
			this.backingMap = new HashMap<K, ValueCapsule>();
		}
		
		/**
		 * @post Crea el mapa con el mapa especificado
		 * @param map
		 */
		public OriginalMap(Map<? extends K, ? extends V> map) {
			this();
			this.putAll(map);
		}

		@Override
		public void clear() {
			WeakCachedHashExplicitContainableStrategyMap.this.keyToValueCapsuleMapper.clear();
			this.backingMap.clear();
		}
		
		// Conjunto de entradas
		private class EntrySet extends AbstractSet< Entry<K, V> > {
			private final class EntrySetIterator implements Iterator< Entry<K, V> > {
				private final Iterator< Entry<K, ValueCapsule > > backingIterator;
				private Entry<K, ValueCapsule> lastEntry;
				
				/**
				 * @post Crea el iterador
				 */
				public EntrySetIterator() {
					this.backingIterator = OriginalMap.this.backingMap.entrySet().iterator();
				}
				
				/**
				 * @post Devuelve si hay elemento siguiente
				 */
				@Override
				public boolean hasNext() {
					return this.backingIterator.hasNext();
				}

				/**
				 * @post Devuelve la próxima entrada
				 */
				@Override
				public Entry<K, V> next() {
					this.lastEntry = this.backingIterator.next();
					return this.lastEntry.getValue();
				}

				@Override
				public void remove() {
					final ValueCapsule valueCapsule = this.lastEntry.getValue();
					this.backingIterator.remove();
					valueCapsule.eraseCache();
				}
				
			}

			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new EntrySetIterator();
			}

			@Override
			public int size() {
				return OriginalMap.this.backingMap.size();
			}
		}
		
		@Override
		public Set<Entry<K, V>> entrySet() {			
			return new EntrySet();
		}

		/**
		 * @post Devuelve la cápsula de valor asociada
		 */
		public ValueCapsule getValueCapsule(Object key) {
			return this.backingMap.get(key);
		}
		
		@Override
		public V get(Object key) {
			final ValueCapsule valueCapsule = this.getValueCapsule(key);
			final V value;
			if ( (valueCapsule != null) && valueCapsule.getKey().equals(key) ) {
				value = valueCapsule.getValue();
			}
			else {
				value = null;
			}
			return value;
		}
		
		@Override
		public V put(K key, V value) {
			ValueCapsule valueCapsule = this.backingMap.get(key);
			final V oldValue;
			
			if ( valueCapsule == null ) {
				// Es una nueva clave
				
				// Borrar la caché de valor de todas las claves
				// contenidas por la clave especificada que sean de
				// contenedoras de la clave especificada
				WeakCachedHashExplicitContainableStrategyMap.this.clearContainedKeysCacheFromContainers(key);
				//WeakCachedHashExplicitContainableStrategyMap.this.clearCache();
				
				// Crear la cápsula de valor
				valueCapsule = new ValueCapsule(key);
				
				// Asociarla con la clave
				this.backingMap.put(key, valueCapsule);
				
				oldValue = null;
			}
			else {
				oldValue = valueCapsule.getValue();
			}
			
			// Asignar el valor especificado
			valueCapsule.setValue(value);
			
			return oldValue;
		}
		
		@Override
		public V remove(Object key) {
			V returnValue;
			final ValueCapsule valueCapsule = this.backingMap.remove(key);
			if ( valueCapsule != null ) { // Si la cápsula de valor no es nula
				// Borrar la caché
				valueCapsule.eraseCache();
				
				// Devolver el valor
				returnValue = valueCapsule.getValue();
			}
			else { // Caso contrario
				returnValue = null;
			}
			
			return returnValue;
		}

		/**
		 * @post Devuelve el tamaño
		 * @return
		 */
		@Override
		public int size() {
			return this.backingMap.size();
		}

		/**
		 * @post Devuelve si contiene la clave especificada
		 */
		@Override
		public boolean containsKey(Object key) {
			return this.backingMap.containsKey(key);
		}

		/**
		 * @post Devuelve si está vacío
		 */
		@Override
		public boolean isEmpty() {
			return this.backingMap.isEmpty();
		}
		
	}
	
	/**
	 * @post Devuelve el mapa de contenibles asociados con sus valores
	 */
	@Override
	public Map<K,V> getMap() {
		return this.originalMap;
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
	 */
	@Override
	protected InmutableReference< Entry<K,V> > getEntry(final InmutableReference<Object> masterKey, final Object key) {
		InmutableReference< Entry<K, V> > entryReference;
		// Si clave es nula o es instancia de la clase de elementos
		if ( (key == null) || this.keyContainableStrategy.getElementClass().isInstance(key) ) {
			// Si no hay clave maestra especificada o pertenece a ella
			if ( (masterKey == null) || (this.keyContainableStrategy.contains(masterKey, key)) ) {
				final Map<K, ValueCapsule> readFilteredKeyToValueCapsuleMapperVision;
				if ( masterKey != null ) {
					class ValueCapsuleFilter implements BinaryClassifier<Object> {
	
						@Override
						public boolean evaluate(Object element) {
							boolean returnValue;
							if ( element != null ) {
								returnValue = WeakCachedHashExplicitContainableStrategyMap.this.keyContainableStrategy.contains(masterKey, ((ValueCapsule) element).getKey());
							}
							else {
								returnValue = true;
							}
							return returnValue;
						}
						
					}
					
					readFilteredKeyToValueCapsuleMapperVision = Collections.unmodifiableMap( new ValueFilteredMap<K, ValueCapsule>(this.keyToValueCapsuleMapper, new ValueCapsuleFilter()) );
				}
				else {
					readFilteredKeyToValueCapsuleMapperVision = Collections.unmodifiableMap( this.keyToValueCapsuleMapper );
				}
				
				ValueCapsule cachedKeyValueCapsule = readFilteredKeyToValueCapsuleMapperVision.get(key);
				final boolean cached;
				
				// Si no está en la caché
				if ( cachedKeyValueCapsule == null ) {
					final boolean useSuperImplementation = true;
					/**
					 * CORREGIR: Por problemas de implementación, problema
					 * 		     de grafos dificil, la implementación
					 * 			 temporal consiste en usar la implementación de la superclase
					 * 			 si se indica que debe usarse
					 */
					if ( useSuperImplementation ) {
						entryReference = super.getEntry(masterKey, key);
						
						if ( masterKey == null ) {
							// Si se encontró entrada, guardar en la caché
							if ( (entryReference != null) && (entryReference.get() != null) ) {
								this.keyToValueCapsuleMapper.put((K) key, this.originalMap.getValueCapsule(entryReference.get().getKey()));
							}
						}
						
						cached = false;
					}
					else {
						/**
						 * Puede haber tres posibilidades:
						 * 1) Que no esté contenida por una clave original
						 * 2) Que esté contenida por una clave original pero hay ambigüedad
						 * 3) Que esté contenida por una clave original y no haya ambigüedad
						 */

						class TreeContainersNode {
							private final K key;
							private DynamicReference<TreeContainersNode> thisReference;
							private DynamicReference<TreeContainersNode> containedNodeReference;
							private final ValueCapsule valueCapsule; // Cápsula de valor
							private final boolean cached; // ¿Es de la caché?
							
							/**
							 * @post Crea el nodo con la clave y el nodo
							 * 		 de la clave contenida especificada
							 * @param key
							 * @param contained
							 */
							public TreeContainersNode(K key, TreeContainersNode containedNode) {
								this.key = key;
								this.thisReference = new DynamicReference<TreeContainersNode>(this);
								if ( containedNode != null ) {
									this.containedNodeReference = containedNode.thisReference;
								}
								else {
									this.containedNodeReference = new DynamicReference<TreeContainersNode>(null);
								}
								
								// Intentar obtener cápsula de valor de la caché
								ValueCapsule valueCapsule = readFilteredKeyToValueCapsuleMapperVision.get(key);
								
								// Si no la pudo obtener de la caché
								if ( valueCapsule == null ) {
									// Intentar obtenerla del mapa original
									valueCapsule = WeakCachedHashExplicitContainableStrategyMap.this.originalMap.getValueCapsule(key);
									this.cached = false; // No está en caché
								}
								else { // Caso contrario
									this.cached = true; // Está en caché
								}
								this.valueCapsule = valueCapsule;
							}
							
							/**
							 * @post Devuelve la clave
							 */
							public K getKey() {
								return this.key;
							}
							
							/**
							 * @post Devuelve el nodo de la contenida
							 */
							public TreeContainersNode getContainedNode() {
								return this.containedNodeReference.get();
							}
							
							/**
							 * @post Devuelve si es la caché
							 */
							public boolean isCached() {
								return this.cached;
							}
							
							/**
							 * @post Devuelve la cápsula de valor
							 */
							public ValueCapsule getValueCapsule() {
								return this.valueCapsule;
							}
							
							/**
							 * @post Hace una "poda" suprimiendo todas las referencias a éste nodo,
							 * 		 o sea suprime como contenida a todos los nodos contenedores
							 */
							public void prune() {
								this.thisReference.set(null);
							}
								
							/**
							 * @post Devuelve una representación en cadena de carácteres
							 */
							@Override
							public String toString() {
								return super.toString() + "{" + this.valueCapsule + "}";
							}
						}
						
						Set<K> noProcessKeys = new HashSet<K>(); // Claves que no deben ser procesadas
						
						TreeContainersNode firstFoundedValuedNode = null; // Primer nodo encontrado con cápsula de valor
						
						// Pila de procesamiento de nodos
						Stack<TreeContainersNode> processingNodesStack = new Stack<TreeContainersNode>();
						
						boolean ambiguous=false; // ¿Es ambiguo?
							
						List<TreeContainersNode> nodesToBeCached = new ArrayList<TreeContainersNode>(); // Nodos a ser agregados en la caché
							
						// ¿Clave del primer nodo encontrado con cápsula de valor en procesamiento de contenedores?
						boolean firstFoundedValuedNodeKeyInContainersProcessing = false;
						
						// Armar el nodo raiz con la clave especificada y empujarlo
						processingNodesStack.push( new TreeContainersNode((K) key, null) );
							
						// Procesar hasta que la pila de procesamiento quede vacía o hasta que se descubra ambigüedad
						while ( !processingNodesStack.isEmpty() && (!ambiguous) ) {
							final TreeContainersNode eachNode = processingNodesStack.pop();
								
							// Si la clave del nodo puede ser procesada
							if ( !noProcessKeys.contains(eachNode.getKey()) ) {
								final boolean noRepeatKey; // ¿No repetir clave?
								boolean browseInContainers; // ¿Buscar en contenedores?
										
								// Si tiene cápsula de valor
								if ( eachNode.getValueCapsule() != null ) {
									boolean processPath; // ¿Procesar camino?
											
									// Si no se había encontrado anteriormente un nodo con cápsula de valor
									if ( firstFoundedValuedNode == null ) {
										firstFoundedValuedNode = eachNode; // Registrarlo
												
										// Si hay clave maestra
										if ( ( masterKey != null ) ) {
											// No buscar en contenedores si se trata de la clave maestra
											browseInContainers = !eachNode.getKey().equals(masterKey.get());
											processPath = false; // No procesar el camino
										}
										else { // Caso contrario
											// Buscar en contenedores si no está en caché
											browseInContainers = !eachNode.isCached();
													
											// Procesar camino si está en caché
											processPath = eachNode.isCached();
										}
									}
									else { // Caso contrario
										// Si no se trata del primer nodo encontrado con cápsula de valor
										if ( eachNode != firstFoundedValuedNode ) {
											
											// Si éste nodo está en caché
											if ( eachNode.isCached() ) {
												// Hay ambigüedad si la cápsula de valor no coincide
												ambiguous = (eachNode.getValueCapsule() != firstFoundedValuedNode.getValueCapsule());
													
												browseInContainers = false; // No buscar en contenedores
												processPath = (masterKey == null); // Procesar por fin de camino sólo si no hay clave maestra especificada
											}
											else { // Si no está en caché
												// Si la cápsula de valor coincide
												if ( ( eachNode.getValueCapsule() == firstFoundedValuedNode.getValueCapsule() ) ) {
													browseInContainers = false; // No buscar en contenedores
													processPath = true; // Por fin de camino
												}
												else { // Si la cápsula de valor no coincide
													/**
													 * Y se están procesando los contenedores de la clave del primer nodo
													 * encontrado con cápsula de valor
													 */
													if ( firstFoundedValuedNodeKeyInContainersProcessing ) {
														browseInContainers = true; // Buscar en contenedores
														processPath = false; // No es fin de camino
													}
													else { // Caso contrario
														/**
														 * Hay ambigüedad si son claves mutualmente contenidas
														 * Con total seguridad ésta clave contiene a la primer encontrada.
														 * Si no está contenida hay ambigüedad
														 */
														ambiguous = this.getKeyContainableStrategy().contains(firstFoundedValuedNode.getKey(), eachNode.getKey());
														browseInContainers = false; // No buscar en contenedores
														processPath = false; // No es fin de camino
													}
												}
											}
										}
										else { // Caso contrario
											// Reportar que ya no se procesan sus contenedores
											firstFoundedValuedNodeKeyInContainersProcessing = false;
											browseInContainers = false; // No procesar sus contenedores
											processPath = false; // No es fin de camino
										}
									}
											
									// Si hay que procesar el camino
									if ( processPath ) {
										// Procesar la rama
										boolean leaf=true; // ¿Es hoja?
										final TreeContainersNode leafNode = eachNode; // Nodo hoja
										TreeContainersNode pathEachNode = leafNode; // Cada nodo de camino
												
										// Procesar hasta que no queden más nodos o hasta que se descubra ambigüedad
										while ( (pathEachNode != null) && (!ambiguous) ) {
											// Si no es la hoja (Primero) y el nodo no está en caché
											if ( !leaf && (!pathEachNode.isCached()) ) {
												// Si el nodo en procesamiento tiene cápsula de valor
												if ( pathEachNode.getValueCapsule() != null ) {
													// Hay ambigüedad si la cápsula de valor no coincide
													// con la primer encontrada
													ambiguous = (pathEachNode.getValueCapsule() != firstFoundedValuedNode.getValueCapsule());
												}
														
												// Agregarlo para ser asociado en caché
												nodesToBeCached.add(pathEachNode);
												/**
												 *  Nota: Si está en hoja y no está en caché
												 *   	  con total seguridad se lo puede encontrar
												 * 		  en el principio del bucle, es para
												 * 		  evitar repeticiones.
												 */
											}
													
											// Obtener el nodo en procesamiento actual
											final TreeContainersNode containerNode = pathEachNode;
											// Obtener nodo contenido
											pathEachNode = containerNode.getContainedNode();
											// Hacerle una "poda" al nodo procesado
											containerNode.prune();
													
											// El siguiente elemento si existiese con total seguridad no es hoja
											leaf = false;
										}
									}
								}
								else { // Si no tiene cápsula de valor
									browseInContainers = true; // Buscar en contenedores
								}
										
								// Si hay que buscar en contenedores
								if ( browseInContainers ) {
									// Si es el primer nodo encontrado con cápsula de valor
									if ( eachNode == firstFoundedValuedNode ) {
										// Indicar que su clave está en procesamiento de contenedores
										firstFoundedValuedNodeKeyInContainersProcessing = true;
										
										/**
										 * Empujar el nodo para detectar fin de
										 * procesamiento de contenedores, implica
										 * necesariamente que puede descubrirse
										 * un nodo con la misma clave (Existencia de bucle)
										 */
										processingNodesStack.push(eachNode);
										
										// Y repetir la clave
										noRepeatKey = false;
									}
									else { // Caso contrario
										noRepeatKey = true; // No repetir la clave
									}
										
									/**
									 * Probar los contenedores por orden de contenidos,
									 * o sea que un contenedor siempre precede a otro contenedor
									 * que no está contenido.
									 * En la lista se almacenan los elementos en orden inverso para
									 * que en la pila se empujen los nodos en el orden esperado
									 */
									List<TreeContainersNode> sortedContainerNodes = new LinkedList<TreeContainersNode>();
									for ( K eachContainerKey : this.keyContainableStrategy.getContainers(eachNode.getKey()) ) {
										boolean noContainedFound = false;
										final TreeContainersNode eachContainerNode = new TreeContainersNode(eachContainerKey, eachNode);
										ListIterator<TreeContainersNode> containersNodesIterator = sortedContainerNodes.listIterator(sortedContainerNodes.size());
										while ( containersNodesIterator.hasPrevious() && !noContainedFound ) {
											noContainedFound = !this.keyContainableStrategy.contains(eachContainerKey, containersNodesIterator.previous().getKey() );
										}
											
										/**
										 * Para que el contenedor
										 * esté delante del que no
										 * está contenido
										 */
										if ( noContainedFound ) {
											containersNodesIterator.next();
										}
											
										// Agrega el nodo del contenedor
										containersNodesIterator.add(eachContainerNode);
									}
										
									// Agrega la lista de contenedores
									processingNodesStack.addAll( sortedContainerNodes );
								}
								else { // Caso contrario
									noRepeatKey = true; // No repetir la clave
								}
								
								// Si no hay que repetir la clave
								if ( noRepeatKey ) {
									// Agregarla
									noProcessKeys.add(eachNode.getKey());
								}
							}
						}
						
						// Si se encontró un nodo con valor
						if (firstFoundedValuedNode != null) {
							final ValueCapsule associatedValueCapsule;
							// Si no hay ambigüedad
							if (!ambiguous) {
								// Almacenar en caché los nodos correspondientes
								for ( TreeContainersNode eachNode : nodesToBeCached ) {
									this.keyToValueCapsuleMapper.put(eachNode.getKey(), firstFoundedValuedNode.getValueCapsule());
								}
								
								// Devolver la cápsula de valr
								associatedValueCapsule = firstFoundedValuedNode.getValueCapsule();
							}
						else { // Si hay ambigüedad
								associatedValueCapsule = null; // Por indefinido
							}
								
							entryReference = new InmutableReference< Entry<K, V> >(associatedValueCapsule);
								
							cached = firstFoundedValuedNode.isCached();
						}
						else { // Si no se encontró
							entryReference = null;
							cached = false;
						}
					}
				}
				else { // Si está en la caché
					entryReference = new InmutableReference< Entry<K,V> >(cachedKeyValueCapsule);
					cached = true;
				}
			}
			else {
				entryReference = null; // Por no pertenecer a la clave maestra
			}
		}
		else {
			entryReference = null; // Por no ser instancia de la clase de claves
		}
		
		return entryReference;
	}
}
