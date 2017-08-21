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
package com.esferixis.misc.containableStrategy.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.esferixis.misc.containablestrategy.ContainableStrategy;
import com.esferixis.misc.containablestrategy.map.AbstractContainableStrategyMap;

/**
 * Mapa de prueba desde la estrategia de contenibles
 * 
 * @param <K>
 * @param <V>
 */
class DummyContainableStrategyMap<K, V> extends AbstractContainableStrategyMap<K, V> {
	private Map<K, V> backingMap;
	
	/**
	 * @pre La estrategia de contenibles no puede ser nula
	 * @post Crea el mapa de pruebas con la estrategia de contenibles de clave especificada
	 * @param keyContainableStrategy
	 */
	public DummyContainableStrategyMap(
			ContainableStrategy keyContainableStrategy) {
		super(keyContainableStrategy);
		this.backingMap = new HashMap<K, V>();
	}

	/**
	 * @post Devuelve el conjunto de elementos
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return this.backingMap.entrySet();
	}
	
	/**
	 * @post Asigna la clave y el valor especificados
	 */
	@Override
	public V put(K key, V value) {
		return this.backingMap.put(key, value);
	}
}
