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
package com.esferixis.misc.containablestrategy.set;

import java.util.Iterator;
import java.util.Set;

import com.esferixis.misc.containablestrategy.ContainableStrategy;
import com.esferixis.misc.containablestrategy.collection.ContainableStrategyCollection;



public final class SetContainableStrategySet<T> extends AbstractContainableStrategySet<T> {
	private final Set<T> sourceSet;
	
	/**
	 * @pre La estrategia de contenibles y el conjunto no pueden ser nulos
	 * @post Crea el conjunto basado en estrategia de contenibles con el conjunto especificado
	 * @param containableStrategy
	 * @param sourceCollection
	 */
	public SetContainableStrategySet(
			ContainableStrategy containableStrategy, Set<T> sourceSet) {
		super(containableStrategy);
		if ( sourceSet != null ) {
			this.sourceSet = sourceSet;
		}
		else {
			throw new NullPointerException();
		}
	}

	/**
	 * @post Agrega el elemento especificado
	 * 
	 * 		 Ésta implementación invoca el método add del conjunto origen
	 */
	@Override
	public boolean add(T element) {
		return this.sourceSet.add(element);
	}
	
	/**
	 * @pre La colección de estrategia no puede ser nula y la estrategia de contenibles tiene que ser la misma
	 * @post Agrega todos los elementos de la colección de estrategia de contenibles especificada y devuelve
	 * 		 si hubo algún cambio
	 * 
	 * 		 Ésta implementación invoca el método addAll del conjunto
	 */
	@Override
	public boolean addAll(ContainableStrategyCollection<? extends T> elements) {
		this.checkContainableStrategyCollection(elements);
		
		return this.sourceSet.addAll(elements.elements());
	}
	
	/**
	 * @post Quita todos los elementos de la colección basada en estrategia de contenibles
	 * 
	 * 		 Ésta implementación invoca el método clear del conjunto
	 */
	@Override
	public void clear() {
		this.sourceSet.clear();
	}
	
	/**
	 * @post Devuelve el tamaño
	 * 
	 * 		 Ésta implementación devuelve el tamaño del conjunto origen
	 */
	@Override
	public int size() {
		return this.sourceSet.size();
	}

	/**
	 * @post Devuelve el iterador
	 * 
	 * 		 Ésta implementación devuelve el iterador del conjunto origen
	 */
	@Override
	public Iterator<T> iterator() {
		return this.sourceSet.iterator();
	}

	/**
	 * @post Devueve la colección de elementos
	 * 
	 * 		 Ésta implementación devuelve el conjunto origen
	 */
	@Override
	public Set<T> elements() {
		return this.sourceSet;
	}
}
