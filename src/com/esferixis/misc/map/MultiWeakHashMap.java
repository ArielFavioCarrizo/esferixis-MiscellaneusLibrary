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
package com.esferixis.misc.map;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.esferixis.misc.reference.InmutableReference;

import java.util.AbstractSet;
import java.util.ArrayList;

/**
 * Mapa que borra las entradas de aquellas claves de las que no se tiene
 * ninguna instancia.
 * Reemplaza las claves que han sido borradas por el garbage collector, por otras
 * iguales que aún no han sido borradas.
 * 
 * No acepta claves nulas
 * 
 * No es "thread-safe", tiene que usarse un decorador "thread-safe" para tal fin
 */
public final class MultiWeakHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
	private class InternalEntry {
		private final Set< WeakReference<K> > keyReferences;
		private V value;
		
		private final int hashCode;
		
		/**
		 * @post Crea un elemento con el hash especificado
		 */
		public InternalEntry(int hashCode) {
			this.keyReferences = new HashSet< WeakReference<K> >();
			this.hashCode = hashCode;
			this.value = null;
		}
		
		/**
		 * @post Devuelve la clave, si
		 * 		 no encuentra, devuelve null
		 */
		public K getKey() {
			final Iterator< WeakReference<K> > iterator = this.keyReferences.iterator();
			K foundedKey = null;
			while ( iterator.hasNext() && ( foundedKey == null ) ) {
				foundedKey = iterator.next().get();
			}
			
			return foundedKey;
		}
		
		/**
		 * @post Devuelve si es de la clave especificada
		 */
		public boolean isKey(Object key) {
			K thisKey = this.getKey();
			return ( thisKey != null ) && ( thisKey.equals(key) );
		}
		
		/**
		 * @post Devuelve si contiene la instancia de clave especificada
		 */
		public boolean containsKeyInstance(Object key) {
			final Iterator< WeakReference<K> > iterator = this.keyReferences.iterator();
			
			boolean containsInstance = false;
			
			while ( iterator.hasNext() && ( !containsInstance ) ) {
				containsInstance = ( iterator.next().get() == key );
			}
			
			return containsInstance;
		}
		
		/**
		 * @post Devuelve el hash
		 */
		@Override
		public int hashCode() {
			return this.hashCode;
		}
		
		/**
		 * @post Devuelve si es igual al elemento
		 * 		 especificado
		 */
		@Override
		public boolean equals(Object other) {
			if ( other != null ) {
				if ( other == this ) {
					return true;
				}
				else if ( other instanceof MultiWeakHashMap.InternalEntry ) {
					K thisKey = this.getKey();
					Object otherKey = ( (MultiWeakHashMap<?,?>.InternalEntry) other ).getKey();
					
					return thisKey.equals(otherKey);
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	
	private final ReferenceQueue<K> keysReferenceQueue;
	
	private final Map< WeakReference<K>, InternalEntry > entryPerKeyReference;
	
	private final Map<Integer, List<InternalEntry>> entriesPerHash;
	
	private int keysCount;
	
	private final Set<java.util.Map.Entry<K, V>> entrySet = new AbstractSet<java.util.Map.Entry<K, V>>() {
		
		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			final Iterator< List<InternalEntry> > entriesListsIterator = MultiWeakHashMap.this.entriesPerHash.values().iterator();
			
			return new Iterator<java.util.Map.Entry<K, V>>() {

				private Iterator<InternalEntry> entriesIterator = null;
				private InternalEntry nextInternalEntry = null;
				private boolean obtainNextEntry = true;
				private K nextKey = null;
				
				private void updateNextInternalEntry() {
					if ( this.obtainNextEntry ) {
						this.nextInternalEntry = null;
						this.nextKey = null;
						
						boolean stop = false;
						
						while ( !stop ) {
							if ( entriesListsIterator.hasNext() ) {
								this.entriesIterator = entriesListsIterator.next().iterator();
								
								while ( this.entriesIterator.hasNext() && (!stop) ) {
									final InternalEntry eachInternalEntry = this.entriesIterator.next();
									
									final K eachKey = eachInternalEntry.getKey();
									
									if ( eachKey != null ) {
										this.nextKey = eachKey;
										this.nextInternalEntry = eachInternalEntry;
										stop = true;
									}
								}
							}
							else {
								stop = true;
							}
						}
						
						this.obtainNextEntry = false;
					}
				}
				
				@Override
				public boolean hasNext() {
					this.updateNextInternalEntry();
					return (this.nextInternalEntry != null);
				}

				@Override
				public java.util.Map.Entry<K, V> next() {
					if ( this.hasNext() ) {
						final InternalEntry actualInternalEntry = this.nextInternalEntry;
						final K actualKey = this.nextKey;
						
						this.obtainNextEntry = true;
						
						return new java.util.Map.Entry<K, V>() {

							@Override
							public K getKey() {
								return actualKey;
							}

							@Override
							public V getValue() {
								return actualInternalEntry.value;
							}

							@Override
							public V setValue(V value) {
								final V oldValue = actualInternalEntry.value;
								actualInternalEntry.value = value;
								
								return oldValue;
							}
							
						};
					}
					else {
						throw new NoSuchElementException();
					}
				}

				@Override
				public void remove() {
					if ( this.entriesIterator != null ) {
						this.entriesIterator.remove();
					}
					else {
						throw new IllegalStateException();
					}
				}
				
			};
		}

		@Override
		public int size() {
			return MultiWeakHashMap.this.entriesPerHash.size();
		}
		
	};
	
	public MultiWeakHashMap() {
		this.keysReferenceQueue = new ReferenceQueue<K>();
		this.entryPerKeyReference = new HashMap< WeakReference<K>, InternalEntry >();
		this.entriesPerHash = new HashMap<Integer, List<InternalEntry>>();
		this.keysCount = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		this.entryPerKeyReference.clear();
		this.entriesPerHash.clear();
		this.keysCount = 0;
	}
	
	/**
	 * @post Devuelve la entrada correspondiente a la clave especificada,
	 * 		 si no la encuentra devuelve null.
	 * 		 
	 * @param key
	 * @return
	 */
	private InternalEntry getEntry(Object key) {
		int hash = key.hashCode();
		
		final List<InternalEntry> entriesList = this.entriesPerHash.get(hash);
		InternalEntry entry = null;
		
		if ( key != null ) {
		
			if ( entriesList != null ) {
				final Iterator< InternalEntry > iterator = entriesList.iterator();
				while ( iterator.hasNext() && ( entry == null )) {
					InternalEntry eachEntry = iterator.next();
					
					if ( eachEntry.isKey(key) ) {
						
						if ( !eachEntry.containsKeyInstance(key) ) {
							eachEntry.keyReferences.add(new WeakReference<K>( (K) key, this.keysReferenceQueue));
						}
						
						entry = eachEntry;
					}
				}
			}
			
		}
		
		return entry;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return (this.getEntry(key) != null);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.entrySet;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {
		this.removePhantomKeyReferences(false);
		final InternalEntry entry = this.getEntry(key);
		
		return ( (entry != null) ? entry.value : null );
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.entriesPerHash.isEmpty();
	}

	/**
	 * @post Borra todas claves pendientes de eliminación
	 */
	private void removePhantomKeyReferences(boolean removeEntries) {
		Reference<? extends K> eachReference;
		while ( (eachReference = this.keysReferenceQueue.poll() ) != null ) {
			final InternalEntry internalEntry = this.entryPerKeyReference.remove(eachReference);
			internalEntry.keyReferences.remove(eachReference);
			
			if ( removeEntries && internalEntry.keyReferences.isEmpty() ) {
				this.keysCount--;
				final List<InternalEntry> entries = this.entriesPerHash.get(internalEntry.hashCode);
				entries.remove(internalEntry);
				
				if ( entries.isEmpty() ) {
					this.entriesPerHash.remove(internalEntry.hashCode);
				}
			}
		}
	}
	
	/**
	 * @pre La clave no puede ser nula
	 */
	@Override
	public V put(K key, V value) {
		if ( key != null ) { 
			final int keyHash = key.hashCode();
			
			this.removePhantomKeyReferences(true);
			
			final WeakReference<K> keyReference = new WeakReference<K>(key, this.keysReferenceQueue);
			
			List<InternalEntry> entriesList = this.entriesPerHash.get(keyHash);
			
			if ( entriesList == null ) {
				entriesList = new LinkedList<InternalEntry>();
				this.entriesPerHash.put(keyHash, entriesList);
			}
			
			InternalEntry internalEntry = null;
			Iterator<InternalEntry> internalEntriesIterator = entriesList.iterator();
			
			while ( internalEntriesIterator.hasNext() && ( internalEntry == null ) ) {
				final InternalEntry eachInternalEntry = internalEntriesIterator.next();
				final K entryKey = eachInternalEntry.getKey();
				
				if ( ( entryKey != null ) && ( entryKey.equals(key) ) ) {
					internalEntry = eachInternalEntry;
				}
			}
			
			if ( internalEntry == null ) {
				internalEntry = new InternalEntry(keyHash);
				entriesList.add(internalEntry);
				
				this.keysCount++;
			}
			
			internalEntry.keyReferences.add(keyReference);
			this.entryPerKeyReference.put(keyReference, internalEntry);
			
			final V oldValue = internalEntry.value;
			internalEntry.value = value;
			
			return oldValue;
		}
		else {
			throw new NullPointerException();
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object key) {
		final int keyHash = key.hashCode();
		
		List<InternalEntry> entriesList = this.entriesPerHash.get(keyHash);
		V value = null;
		
		if ( entriesList != null ) {
			InternalEntry internalEntry = null;
			Iterator<InternalEntry> internalEntriesIterator = entriesList.iterator();
			
			while ( internalEntriesIterator.hasNext() && ( internalEntry == null ) ) {
				final InternalEntry eachInternalEntry = internalEntriesIterator.next();
				final K entryKey = eachInternalEntry.getKey();
				
				if ( ( entryKey != null ) && ( entryKey.equals(key) ) ) {
					internalEntry = eachInternalEntry;
				}
			}
			
			if ( internalEntry != null ) {
				this.keysCount--;
				
				value = internalEntry.value;
				
				this.entryPerKeyReference.keySet().removeAll(internalEntry.keyReferences);
				entriesList.remove(internalEntry);
				
				if ( entriesList.isEmpty() ) {
					this.entriesPerHash.remove(keyHash);
				}
			}
		}
		
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return this.keysCount;
	}
	
}
