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
package com.esferixis.misc.test.containableStrategy.collection;

import java.util.Collection;
import java.util.Iterator;

import com.esferixis.misc.containablestrategy.ContainableStrategy;
import com.esferixis.misc.containablestrategy.collection.AbstractContainableStrategyCollection;

/**
 * 
 * Colección usada para hacer pruebas sobre la colección abstracta desde la estrategia de contenibles
 * 
 * @param <T>
 */
abstract class DummyContainableStrategyCollection<T> extends AbstractContainableStrategyCollection<T> {
	private final Collection<T> testElementsCollection;
	
	/**
	 * @pre La estrategia de contenibles y la colección no pueden ser nulos
	 * @post Crea la colección de estrategia de prueba con la colección de elementos especificada
	 * @param containableStrategy
	 */
	public DummyContainableStrategyCollection(
			ContainableStrategy containableStrategy, Collection<T> testElementsCollection) {
		super(containableStrategy);
		if ( testElementsCollection != null ) {
			this.testElementsCollection = testElementsCollection;
		}
		else {
			throw new NullPointerException();
		}
	}

	/**
	 * @post Agrega el elemento especificado
	 */
	@Override
	public boolean add(T element) {
		return this.testElementsCollection.add(element);
	}
	
	/**
	 * @post Devuelve el iterador de elementos
	 */
	@Override
	public Iterator<T> iterator() {
		return this.testElementsCollection.iterator();
	}

	/**
	 * @post Devuelve la cantidad de elementos
	 */
	@Override
	public int size() {
		return this.testElementsCollection.size();
	}

}
