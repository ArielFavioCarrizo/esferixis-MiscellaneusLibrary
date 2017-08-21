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
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.esferixis.misc.binaryclassifier.BinaryClassifier;
import com.esferixis.misc.collection.FilteredCollection;
import com.esferixis.misc.collection.set.FilteredSet;



public class ValueFilteredMap<K, V> extends AbstractMap<K, V> {
	public static class InvalidValueException extends IllegalArgumentException {
		private static final long serialVersionUID = 3366201997134224851L;

		/**
		 * @post Crea la excepción con la descripción especificada
		 */
		public InvalidValueException(String message) {
			super(message);
		}
		
		/**
		 * @post Crea la excepción con descripción predeterminada
		 */
		public InvalidValueException() {
			super("Invalid value because it cannot pass through the filter");
		}
	}
	
	private final Map<K, V> sourceMap;
	
	private final BinaryClassifier<Object> valueFilter;
	
	private final BinaryClassifier<Object> entryFilter;
	
	/**
	 * @pre El mapa y el filtro no pueden ser nulos
	 * @post Crea el mapa filtrado con el mapa y el filtro de valores especificados
	 */
	public ValueFilteredMap(Map<K, V> sourceMap, BinaryClassifier<Object> valueFilter) {
		if ( ( sourceMap != null ) && ( valueFilter != null ) ) {
			this.sourceMap = sourceMap;
			this.valueFilter = valueFilter;
			this.entryFilter = new BinaryClassifier<Object>(){
				@Override
				public boolean evaluate(Object element) {
					if ( element instanceof Entry<?, ?> ) {
						return ValueFilteredMap.this.valueFilter.evaluate(((Entry<?, ?>) element).getKey());
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
		if ( this.sourceMap.containsKey(key) ) {
			return this.valueFilter.evaluate( this.sourceMap.get(key) );
		}
		else {
			return false;
		}
	}
	
	/**
	 * @post Devuelve si contiene el valor especificado
	 */
	@Override
	public boolean containsValue(Object value) {
		return this.values().contains(value);
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
		V returnValue = null;
		if ( this.sourceMap.containsKey(key) ) {
			final V value = this.sourceMap.get(key);
			if ( this.valueFilter.evaluate(value) ) {
				returnValue = value;
			}
		}
		return returnValue;
	}
	
	/**
	 * @post Devuelve si está vacío
	 */
	@Override
	public boolean isEmpty() {
		return this.values().isEmpty();
	}
	
	/**
	 * @pre El valor tiene que pasar por el filtro
	 * @post Asocia la clave con el valor especificado
	 */
	@Override
	public V put(K key, V value) {
		if ( this.valueFilter.evaluate(value) ) {
			return this.sourceMap.put(key, value);
		}
		else {
			throw new InvalidValueException();
		}
	}
	
	/**
	 * @post Quita la clave especificada
	 */
	@Override
	public V remove(Object key) {
		V returnValue = null;
		if ( this.sourceMap.containsKey(key) ) {
			if ( this.valueFilter.evaluate( this.sourceMap.get(key) ) ) {
				returnValue = this.sourceMap.remove(key);
			}
		}
		return returnValue;
	}
	
	/**
	 * @post Devuelve la colección de valores
	 */
	@Override
	public Collection<V> values() {
		return new FilteredCollection<V>(this.sourceMap.values(), this.valueFilter);
	}
}
