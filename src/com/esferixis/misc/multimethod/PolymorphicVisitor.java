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
package com.esferixis.misc.multimethod;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.esferixis.misc.classextra.ConcreteInstanceClassIndexer;
import com.esferixis.misc.exception.NotImplementedException;
import com.esferixis.misc.multimethod.PolymorphicVisitor.Case;

/**
 * @param <P> La clase de parámetros, puede ser void
 * @param <T> La clase o interface que abarca los objetos procesables
 * @param <R> El valor de retorno, puede ser void
 */
public abstract class PolymorphicVisitor<P,T,R> {
	
	public static abstract class Case<P, V, R> {
		private final Class<V> elementClass;
		
		/**
		 * @pre La clase de elemento no puede ser nula
		 * @post Crea el caso con la clase de elemento especificada
		 */
		public Case(Class<V> elementClass) {
			if ( elementClass != null ) {
				this.elementClass = elementClass;
			}
			else {
				throw new NullPointerException();
			}
		}
		
		/**
		 * @post Devuelve la clase de elemento
		 */
		public final Class<V> getElementClass() {
			return this.elementClass;
		}
	
		/**
		 * @pre El objetivo tiene que pertenecer a la clase de elemento
		 * @post Procesa el objeto especificado
		 */
		private R processWithCasting(P parameters, Object target) {
			return this.process(parameters, this.elementClass.cast(target));
		}
		
		/**
		 * @post Procesa el objeto especificado
		 */
		public abstract R process(P parameters, V target);
	}
	
	private final Case<P, ? extends T, R>[] cases;
	
	/**
	 * @pre Los casos no pueden ser nulos y por cada tipo sólo puede haber
	 * 		uno.
	 * @post Crea un visitor con los casos especificados
	 */
	PolymorphicVisitor(Case<P, ? extends T, R>... cases) {
		if ( cases != null ) {
			Set< Class<?> > classes = new HashSet< Class<?> >();
			for ( Case<P, ? extends T, R> eachCase : cases ) {
				if ( classes.add(eachCase.getClass()) ) {
					throw new IllegalArgumentException("Cases with same type");
				}
			}
			this.cases = cases.clone();
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre La clase base no puede ser nula.
	 * 		Los casos no pueden ser nulos y por cada tipo
	 * 		sólo puede haber uno.
	 * @post Crea un visitor con los casos especificados.
	 * 		 Es recomendable que la clase base tenga un indexador de clases concretas
	 * 		 de instancia, especialmente si el rendimiento es importante
	 */
	public static <P, T, R> PolymorphicVisitor<P, T, R> make(Class<T> baseClass, Case<P, ? extends T, R>... cases) {
		if ( ( baseClass != null ) && ( cases != null ) ) {
			ConcreteInstanceClassIndexer<? super T> indexer = ConcreteInstanceClassIndexer.indexerByClass(baseClass);
			if ( indexer != null ) {
				return new ConcreteInstanceClassIndexerPolymorphicVisitor<P, T, R>(indexer, cases);
			}
			else {
				return new HashPolymorphicVisitor<P, T, R>(cases);
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve el caso según la clase concreta de la instancia
	 */
	protected abstract Case<P, ? extends T, R> getCaseByInstanceClass(T element);
	
	/**
	 * @post Asigna el caso según la clase concreta de la instancia
	 */
	protected abstract void setCaseByInstanceClass(T element, Case<P, ? extends T, R> targetCase);
	
	/**
	 * @pre Tiene que haber un caso que cubra la clase objetivo.
	 * 		No puede haber ambigüedad, la clase objetivo tiene que estar
	 * 		abarcada por un caso cuya clase no esté abarcada por otro y al mismo
	 * 		éste otro abarque a éste.
	 * @post Procesa el elemento especificado con el elemento de parámetros
	 * 		 y el elemento objetivo especificado y devuelve el resultado
	 * 		 Si no hay parámetros puede pasarse "null" como parámetro,
	 * 		 se ignorará completamente
	 * @throws NotImplementedException, UnsupportedOperationException
	 */
	public R process(P parameters, T target) {
		Case<P, ? extends T, R> targetCase = this.getCaseByInstanceClass(target);
		
		if ( targetCase == null ) {
			/**
			 * Busca el caso más específico
			 */
			Case<P, ? extends T, R> candidateCase = null;
			for ( Case<P, ? extends T, R> eachCase : this.cases ) {
				if ( eachCase.getElementClass().isAssignableFrom(target.getClass()) ) {
					if ( ( candidateCase == null ) || ( candidateCase.getElementClass().isAssignableFrom(eachCase.getElementClass()) ) ) {
						candidateCase = eachCase;
					}
				}
			}
			
			if ( candidateCase != null ) {
				/**
				 * Busca si hay un caso que lo contenga y sea otro,
				 * o sea si hay ambigüedad actúa en consecuencia
				 */
				for ( Case<P, ? extends T, R> eachCase : this.cases ) {
					if ( eachCase != candidateCase ) {
						if ( candidateCase.getElementClass().isAssignableFrom(eachCase.getElementClass()) ) {
							throw new UnsupportedOperationException("Ambiguous cases");
						}
					}
				}
				
				// Agregar candidato al mapa asociativo y tomarlo como caso asociado
				this.setCaseByInstanceClass(target, candidateCase);
				targetCase = candidateCase;
			}
			else {
				throw new NotImplementedException("Missing case");
			}
		}
		
		return targetCase.processWithCasting(parameters, target);
	}
}
