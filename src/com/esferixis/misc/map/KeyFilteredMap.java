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
import java.util.Map;
import java.util.Set;

import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.collection.set.FilteredSet;


/**
 * Mapa filtrado por clave
 *
 * @param <K>
 * @param <V>
 */
public final class KeyFilteredMap<K, V> extends AbstractMap<K, V> {
	public static class InvalidKeyException extends IllegalArgumentException {
		private static final long serialVersionUID = -5393066586476849243L;
		
		/**
		 * @post Crea la excepción con la descripción especificada
		 */
		public InvalidKeyException(String message) {
			super(message);
		}
		
		/**
		 * @post Crea la excepción con descripción predeterminada
		 */
		public InvalidKeyException() {
			super("Invalid key because it cannot pass through the filter");
		}
	}
	
	private final Map<K, V> sourceMap;
	
	private final BinaryClassifier<Object> keyFilter;
	
	private final BinaryClassifier<Object> entryFilter;
	
	/**
	 * @pre El mapa y el filtro no pueden ser nulos
	 * @post Crea el mapa filtrado con el mapa y el filtro de claves especificados
	 */
	public KeyFilteredMap(Map<K, V> sourceMap, BinaryClassifier<Object> keyFilter) {
		if ( ( sourceMap != null ) && ( keyFilter != null ) ) {
			this.sourceMap = sourceMap;
			this.keyFilter = keyFilter;
			this.entryFilter = new BinaryClassifier<Object>(){
				@Override
				public boolean evaluate(Object element) {
					if ( element instanceof Entry<?, ?> ) {
						return KeyFilteredMap.this.keyFilter.evaluate(((Entry<?, ?>) element).getKey());
					}
					else {
						return false;
					}
				}
			};
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve si contiene la clave especificada
	 */
	@Override
	public boolean containsKey(Object key) {
		if ( this.keyFilter.evaluate(key) ) {
			return this.sourceMap.containsKey(key);
		}
		else {
			return false;
		}
	}	

	/**
	 * @post Devuelve el conjunto de entradas
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new FilteredSet< Entry<K, V> >(this.sourceMap.entrySet(), this.entryFilter);
	}
	
	/**
	 * @post Devuelve el valor asociado a la clave especificada
	 */
	@Override
	public V get(Object key) {
		if ( this.keyFilter.evaluate(key) ) {
			return this.sourceMap.get(key);
		}
		else {
			return null;
		}
	}
	
	/**
	 * @post Devuelve si está vacío
	 */
	@Override
	public boolean isEmpty() {
		return this.keySet().isEmpty();
	}
	
	/**
	 * @post Devuelve el conjunto de claves
	 */
	@Override
	public Set<K> keySet() {
		return new FilteredSet<K>(this.sourceMap.keySet(), this.keyFilter);
	}
	
	/**
	 * @post Asocia la clave con el valor especificado
	 */
	@Override
	public V put(K key, V value) {
		if ( this.keyFilter.evaluate(key) ) {
			return this.sourceMap.put(key, value);
		}
		else {
			throw new InvalidKeyException();
		}
	}
	
	/**
	 * @post Quita el valor especificado
	 */
	@Override
	public V remove(Object key) {
		if ( this.keyFilter.evaluate(key) ) {
			return this.sourceMap.remove(key);
		}
		else {
			return null;
		}
	}
}
