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

import java.util.Set;

import com.esferixis.misc.containablestrategy.collection.ContainableStrategyCollection;



public interface ContainableStrategySet<T> extends ContainableStrategyCollection<T> {
	/**
	 * @post Devuelve el conjunto de elementos
	 */
	@Override
	public Set<T> elements();
	
	/**
	 * @post Devuelve una visión del conjunto de elementos que sólo contiene aquellos
	 * 		 elementos que ni están contenidos por ningún otro,
	 * 		 o sea los elementos raices
	 * 		 Si se intenta agregar un elemento que está contenido por otros lanza
	 * 		 IllegalArgumentException
	 */
	@Override
	public Set<T> getRootElements();
	
	/**
	 * @post Devuelve una visión del conjunto basado en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 contenidos por el elemento contenedor especificado
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	@Override
	public ContainableStrategySet<T> containedContainableStrategyCollection(Object mandatoryContainer);
	
	/**
	 * @post Devuelve una visión del conjunto basado en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 que contienen el elemento contenible especificado
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	@Override
	public ContainableStrategySet<T> containerContainableStrategyCollection(Object mandatoryContained);
	
	/**
	 * @post Devuelve una visión del conjunto basado en estrategia
	 * 		 de contenibles que sólo contiene aquellos elementos
	 * 		 que están contenidos por el elemento contenedor y que
	 * 		 contienen al elemento contenible especificados.
	 * 
	 * 		 Si se intenta agregar un elemento que no cumple con la
	 * 		 condición necesaria de pertenencia se lanza la excepción
	 * 		 IllegalArgumentException
	 */
	@Override
	public ContainableStrategySet<T> subContainableStrategyCollection(Object mandatoryContainer, Object mandatoryContained);
}
