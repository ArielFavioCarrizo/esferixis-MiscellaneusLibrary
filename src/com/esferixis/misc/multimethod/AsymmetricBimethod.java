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

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import com.esferixis.misc.classextra.ClassExtra;
import com.esferixis.misc.classextra.ConcreteInstanceClassIndexer;
import com.esferixis.misc.collection.list.BinaryList;
import com.esferixis.misc.exception.NotImplementedException;

public abstract class AsymmetricBimethod<P, T, R> extends Bimethod<P, T, R> {
	private final Case<P, ? extends T, ? extends T, R>[][] casesLists;
	
	/**
	 * @pre Los casos no pueden ser nulos ni pueden abarcar los mismos pares
	 * 		de clases, también vale entre los casos de oposición
	 * @post Crea un bimétodo asimétrico con los casos y los casos de opuestos
	 * 		 especificados.
	 * 		 Si no hay casos de opuestos se puede dejar como null
	 */
	AsymmetricBimethod(Case<P, ? extends T, ? extends T, R>[] cases, Case<P, ? extends T, ? extends T, R>[] oppositeCases) {
		if ( cases != null ) {
			this.casesLists = (Case<P, ? extends T, ? extends T, R>[][]) Array.newInstance(Array.class, ( oppositeCases == null ) ? 1 : 2);
			
			Set<  BinaryList< Class<? extends T> > > classesPair = new HashSet< BinaryList< Class<? extends T> > >();
			for ( Case<P, ? extends T, ? extends T, R> eachCase : cases ) {
				if ( !classesPair.add( new BinaryList< Class<? extends T> >(eachCase.getElementClass1(), eachCase.getElementClass2())) ) {
					throw new IllegalArgumentException("Cases with same class pair");
				}
			}
			this.casesLists[0] = cases.clone();
			
			if ( oppositeCases != null ) {
				Set< BinaryList< Class<? extends T> > > oppositeCasesClassPairs = new HashSet< BinaryList< Class<? extends T> > >();
				for ( Case<P, ? extends T, ? extends T, R> eachCase : oppositeCases ) {
					if ( !oppositeCasesClassPairs.add( new BinaryList< Class<? extends T> >(eachCase.getElementClass1(), eachCase.getElementClass2()) ) ) {
						throw new IllegalArgumentException("Opposite cases with same class pair");
					}
				}
				
				this.casesLists[1] = oppositeCases.clone();
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @pre La clase base y los casos no pueden ser nulos ni pueden abarcar los mismos pares
	 * 		de clases, también vale entre los casos de oposición.
	 * @post Crea el bimétodo asimétrico con los casos y los casos de opuestos
	 * 		 especificados.
	 * 		 Es recomendable que la clase base tenga un indexador de clases concretas
	 * 		 de instancia, especialmente si el rendimiento es importante.
	 */
	public static <P, T, R> AsymmetricBimethod<P, T, R> make(Class<T> baseClass, Case<P, ? extends T, ? extends T, R>[] cases, Case<P, ? extends T, ? extends T, R>[] oppositeCases) {
		if ( ( baseClass != null ) && ( cases != null ) && ( oppositeCases != null ) ) {
			ConcreteInstanceClassIndexer<? super T> indexer = ConcreteInstanceClassIndexer.indexerByClass(baseClass);
			if ( indexer != null ) {
				return new ConcreteInstanceClassIndexerAsymmetricBimethod<P, T, R>(indexer, cases, oppositeCases);
			}
			else {
				return new HashAsymmetricBimethod<P, T, R>(cases, oppositeCases);
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve el caso asociado con los elementos especificados
	 */
	protected abstract Case<P, ? extends T, ? extends T, R> getCaseByClassElementsPair(T target1, T target2);
	
	/**
	 * @post Asigna el caso asociado con las clases de los elementos especificados
	 */
	protected abstract void setCaseByClassElementsPair(T target1, T target2, Case<P, ? extends T, ? extends T, R> targetCase);
	
	/* (non-Javadoc)
	 * @see com.esferixis.misc.multimethod.Bimethod#process(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public R process(P parameters, T target1, T target2) throws NullPointerException, NotImplementedException {
		if ( ( target1 != null ) && ( target2 != null ) ) {
			final BinaryList< Class<? extends T> > targetClassPair = new BinaryList< Class<? extends T> >( (Class<? extends T>) target1.getClass(), (Class<? extends T>) target2.getClass());
			Case<P, ? extends T, ? extends T, R> targetCase = this.getCaseByClassElementsPair(target1, target2);
			
			if ( targetCase == null ) {
				Case<P, ? extends T, ? extends T, R> candidateCase = null;
				BinaryList< Class<? extends T> > candidateCaseClassPair = null;
				
				for ( int i = 0 ; (i < this.casesLists.length) && ( candidateCase == null ) ; i++ ) {
				
					/**
					 * Busca el caso más específico
					 */
					for ( Case<P, ? extends T, ? extends T, R> eachCase : this.casesLists[i] ) {
						final BinaryList< Class<? extends T> > eachCaseClassPair = new BinaryList< Class<? extends T> >(eachCase.getElementClass1(), eachCase.getElementClass2());
						if ( ClassExtra.isAssignableFrom(eachCaseClassPair, targetClassPair) ) {
							if ( ( candidateCase == null ) || ( ClassExtra.isAssignableFrom(candidateCaseClassPair, eachCaseClassPair) ) ) {
								candidateCase = eachCase;
								candidateCaseClassPair = eachCaseClassPair;
							}
						}
					}
					
					if ( candidateCase != null ) {
						/**
						 * Busca si hay un caso que lo contenga y sea otro,
						 * o sea si hay ambigüedad actúa en consecuencia
						 */
						for ( Case<P, ? extends T, ? extends T, R> eachCase : this.casesLists[i] ) {
							if ( eachCase != candidateCase ) {
								final BinaryList< Class<? extends T> > eachCaseClassPair = new BinaryList< Class<? extends T> >(eachCase.getElementClass1(), eachCase.getElementClass2());
								
								if ( ClassExtra.isAssignableFrom(candidateCaseClassPair, eachCaseClassPair) ) {
									throw new NotImplementedException("Ambiguous cases");
								}
							}
						}
					}
				}
				
				if ( candidateCase != null ) {
					// Agregar candidato al mapa asociativo y tomarlo como caso asociado
					this.setCaseByClassElementsPair(target1, target2, candidateCase);
					targetCase = candidateCase;
				}
				else {
					throw new NotImplementedException("Missing cases");
				}
			}
			
			return targetCase.processWithCasting(parameters, target1, target2);
		}
		else {
			throw new NullPointerException();
		}
	}
	
	
}
