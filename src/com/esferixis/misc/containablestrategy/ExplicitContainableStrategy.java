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
package com.esferixis.misc.containablestrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.esferixis.misc.reference.DynamicReference;



public abstract class ExplicitContainableStrategy<T> implements ContainableStrategy {
	protected final Class<T> elementClass; // Clase de elemento
	
	/**
	 * @pre La clase de elemento no puede ser nula
	 * @post Crea la estrategia con la clase de elemento especificada
	 */
	public ExplicitContainableStrategy(Class<T> elementClass) {
		if ( elementClass != null ) {
			this.elementClass = elementClass;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve la clase de los elementos
	 */
	public Class<T> getElementClass() {
		return this.elementClass;
	}
	
	/**
	 * @post Devuelve los contenedores del contenible especificado.
	 * 		 Si el contenible es nulo entonces devuelve un conjunto con sólo el contenedor nulo.
	 * 		 Los contenibles que no están cubiertos por ésta estrategia
	 * 		 por definición no tienen ningún contenedor
	 */
	public abstract Set<T> getContainers(T containable);
	
	/**
	 * @post Devuelve si el contenedor contiene el contenible especificado
	 * 		 Si el contenedor es nulo supone que sólo el contenible nulo está contenido
	 * 		 Los contenedores que no están cubiertos por ésta estrategia por definición
	 * 		 no contienen ningún contenible.
	 * 		 Y los contenibles que no están cubiertos por ésta estrategia no están contenidos
	 * 		 por ningún contenedor.
	 * 
	 * 	 	 Si la referencia a conjunto de contenedores encontrados no es nula entonces
	 * 		 asigna un conjunto de sólo lectura con los contenedores que encontró en el proceso.
	 * 
	 * 		 Ésta implementación crea un conjunto de contenibles encontrados.
	 * 		 Luego ve si el contenedor especificado es nulo,  si es así devuelve ( container == null )
	 * 		 y agrega el elemento null al conjunto de elementos.
	 * 		 Caso contrario crea una pila de procesamiento y empuja el contenible especificado.
	 * 		 Si no hay elementos en la pila extrae el elemento de la cima considera
	 * 		 que el contenible no está contenido por el contenedor especificados.
	 * 		 Caso contrario, si no está en el conjunto de contenibles encontrados lo agrega
	 * 		 y si es igual al contenedor especificado considera que es el contenedor,
	 * 		 y termina la búsqueda.
	 * 		 Caso contrario empuja los contenedores de dicho elemento a la pila,
	 * 		 y vuelve a tratar de la misma manera a la pila de procesamiento.
	 */
	public boolean contains(DynamicReference< Set<T> > foundedContainers, T container, T containable) {
		final Set<T> foundedContainables = new HashSet<T>();
		
		if ( foundedContainers != null ) {
			foundedContainers.set(Collections.unmodifiableSet(foundedContainables));
		}
		
		if ( ( container != null ) ) {
			boolean contained=false; // Se considerará no contenido hasta que no se demuestre lo contrario
			
			// Pila de elementos para procesar
			Stack<T> elementsProcessingStack = new Stack<T>();
			
			// Empujar el contenible especificado
			elementsProcessingStack.add(containable);
			
			// Mientras haya elementos a procesar o no se descubrió todavía si el contenible está contenido
			while ( !elementsProcessingStack.isEmpty() && !contained ) {
				final T eachElement = elementsProcessingStack.pop();
				
				// Si no fue procesado
				if ( !foundedContainables.contains(eachElement) ) {
					// Agregarlo al conjunto de contenibles encontrados
					foundedContainables.add(eachElement);
					
					// Si es igual al contenedor
					if ( eachElement.equals(container) ) {
						// El contenible está contenido
						contained = true;
					}
					else { // Caso contrario
						// Procesar los contenedores, para tal fin empujarlos a la pila de procesamiento
						elementsProcessingStack.addAll( this.getContainers(eachElement) );
					}
				}
			}
			
			return contained;
		}
		else {
			return (containable == null);
		}
	}
	
	/**
	 * @post Devuelve si el contenedor contiene el contenible especificado
	 * 		 Si el contenedor es nulo supone que sólo el contenible nulo está contenido
	 * 		 Los contenedores que no están cubiertos por ésta estrategia por definición
	 * 		 no contienen ningún contenible.
	 * 		 Los contenibles que no están cubiertos por ésta estrategia por definición no están contenidos
	 * 		 por ningún contenedor.
	 * 
	 * 		 Ésta implementación invoca el método contains con la referencia a conjunto de contenedores
	 * 		 encontrados nula.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object container, Object containable) {
		if ( container != null ) {
			if ( this.elementClass.isInstance(container) ) {
				if ( ( ( containable != null ) && this.elementClass.isInstance(containable) ) || ( containable == null ) ) {
					return this.contains(null, (T) container, (T) containable);
				}
			}
			
			return false;
		}
		else {
			return ( containable == null );
		}
	}
	
}
