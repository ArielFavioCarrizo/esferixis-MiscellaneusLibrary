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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sun.font.CreatedFontTracker;

/**
 * Mapa basado en array
 *
 */
public final class ArrayMap<K,V> extends AbstractMap<K, V> {
	private List<java.util.Map.Entry<K, V>> entries;
	
	public ArrayMap() {
		this.entries = new ArrayList<java.util.Map.Entry<K, V>>();
	}
	
	/**
	 * @post Crea el mapa con las entradas el mapa especificado
	 * @param map
	 */
	public ArrayMap(Map<? extends K, ? extends V> map) {
		this();
		if ( map != null ) {
			for ( Map.Entry<? extends K, ? extends V> eachEntry : map.entrySet() ) {
				this.entries.add( new SimpleEntry<K, V>( eachEntry.getKey(), eachEntry.getValue() ));
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve la entrada asociada con la clave,
	 * 		 si no existe devuelve null
	 */
	private java.util.Map.Entry<K, V> getEntryByKey(K key) {
		final Iterator<java.util.Map.Entry<K, V>> entryIterator = this.entries.iterator();
		java.util.Map.Entry<K, V> foundedEntry = null;
		
		while ( entryIterator.hasNext() && (foundedEntry == null) ) {
			final java.util.Map.Entry<K, V> eachEntry = entryIterator.next();
			
			if ( eachEntry.getKey().equals(key) ) {
				foundedEntry = eachEntry;
			}
		}
		
		return foundedEntry;
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.AbstractMap#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new AbstractSet<java.util.Map.Entry<K, V>>() {

			@Override
			public Iterator<java.util.Map.Entry<K, V>> iterator() {
				return ArrayMap.this.entries.iterator();
			}

			@Override
			public int size() {
				return ArrayMap.this.entries.size();
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		final V oldValue;
		
		final java.util.Map.Entry<K, V> associatedEntry = this.getEntryByKey(key);
		if ( associatedEntry == null ) {
			this.entries.add( new SimpleEntry<K,V>(key, value) );
			oldValue = null;
		}
		else {
			oldValue = associatedEntry.getValue();
			associatedEntry.setValue(value);
		}
		
		return oldValue;
	}
}
