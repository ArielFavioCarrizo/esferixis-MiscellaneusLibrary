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
 * Colección desde la estrategia de contenibles basada en colección estandar
 *
 * @param <T>
 */
public final class CollectionContainableStrategyCollection<T> extends AbstractContainableStrategyCollection<T> {
	private final Collection<T> sourceCollection;
	
	/**
	 * @pre La estrategia de contenibles y la colección no pueden ser nulos
	 * @post Crea la colección basada en estrategia de contenibles con la colección especificada
	 * @param containableStrategy
	 * @param sourceCollection
	 */
	public CollectionContainableStrategyCollection(
			ContainableStrategy containableStrategy, Collection<T> sourceCollection) {
		super(containableStrategy);
		if ( sourceCollection != null ) {
			this.sourceCollection = sourceCollection;
		}
		else {
			throw new NullPointerException();
		}
	}

	/**
	 * @post Agrega el elemento especificado
	 * 
	 * 		 Ésta implementación invoca el método add de la colección origen
	 */
	@Override
	public boolean add(T element) {
		return this.sourceCollection.add(element);
	}
	
	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Agrega todos los elementos de la colección de estrategia de contenibles especificada y devuelve
	 * 		 si hubo algún cambio
	 * 
	 * 		 Ésta implementación invoca el método addAll de la colección
	 */
	@Override
	public boolean addAll(ContainableStrategyCollection<? extends T> elements) {
		this.checkContainableStrategyCollection(elements);
		
		return this.sourceCollection.addAll(elements.elements());
	}
	
	/**
	 * @post Quita todos los elementos de la colección basada en estrategia de contenibles
	 * 
	 * 		 Ésta implementación invoca el método clear de la colección de elementos
	 */
	@Override
	public void clear() {
		this.sourceCollection.clear();
	}
	
	/**
	 * @post Devuelve el tamaño
	 * 
	 * 		 Ésta implementación devuelve el tamaño de la colección origen
	 */
	@Override
	public int size() {
		return this.sourceCollection.size();
	}

	/**
	 * @post Devuelve el iterador
	 * 
	 * 		 Ésta implementación devuelve el iterador de la colección origen
	 */
	@Override
	public Iterator<T> iterator() {
		return this.sourceCollection.iterator();
	}

	/**
	 * @post Devueve la colección de elementos
	 * 
	 * 		 Ésta implementación devuelve la colección origen
	 */
	@Override
	public Collection<T> elements() {
		return this.sourceCollection;
	}
}
