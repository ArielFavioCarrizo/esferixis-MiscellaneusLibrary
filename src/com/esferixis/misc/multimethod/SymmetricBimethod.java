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
import java.util.Set;

import com.esferixis.misc.classextra.ClassExtra;
import com.esferixis.misc.classextra.ConcreteInstanceClassIndexer;
import com.esferixis.misc.collection.set.BinarySet;
import com.esferixis.misc.exception.NotImplementedException;

/**
 * Bimétodo simétrico
 */
public abstract class SymmetricBimethod<P, T, R> extends Bimethod<P, T, R> {
	private final Case<P, ? extends T, ? extends T, R>[] cases;
	
	/**
	 * @pre Los casos no pueden ser nulos y no pueden abarcar los mismos pares de clases
	 * @post Crea el bimétodo simétrico con los casos especificados
	 */
	SymmetricBimethod(Case<P, ? extends T, ? extends T, R>... cases) {
		if ( cases != null ) {
			Set<  BinarySet< Class<? extends T> > > classesPair = new HashSet<  BinarySet< Class<? extends T> > >();
			for ( Case<P, ? extends T, ? extends T, R> eachCase : cases ) {
				if ( !classesPair.add( new BinarySet< Class<? extends T> >(eachCase.getElementClass1(), eachCase.getElementClass2())) ) {
					throw new IllegalArgumentException("Cases with same class pair");
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
	 * 		Los casos no pueden ser nulos y no pueden abarcar los mismos pares de clases
	 * @post Crea el bimétodo simétrico con los casos especificados.
	 * 		 Es recomendable que la clase base tenga un indexador de clases concretas
	 * 		 de instancia, especialmente si el rendimiento es importante.
	 */
	public static <P, T, R> SymmetricBimethod<P, T, R> make(Class<T> baseClass, Case<P, ? extends T, ? extends T, R>... cases) {
		if ( ( baseClass != null ) && ( cases != null ) ) {
			ConcreteInstanceClassIndexer<? super T> indexer = ConcreteInstanceClassIndexer.indexerByClass(baseClass);
			
			cases = cases.clone();
			
			if ( indexer != null ) {
				return new ConcreteInstanceClassIndexerSymmetricBimethod<P, T, R>(indexer, cases);
			}
			else {
				return new HashSymmetricBimethod<P, T, R>(cases);
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
			final BinarySet< Class<? extends T> > targetClassPair = new BinarySet< Class<? extends T> >( (Class<? extends T>) target1.getClass(), (Class<? extends T>) target2.getClass());
			Case<P, ? extends T, ? extends T, R> targetCase = this.getCaseByClassElementsPair(target1, target2);
			
			if ( targetCase == null ) {
				/**
				 * Busca el caso más específico
				 */
				Case<P, ? extends T, ? extends T, R> candidateCase = null;
				BinarySet< Class<? extends T> > candidateCaseClassPair = null;
				
				for ( Case<P, ? extends T, ? extends T, R> eachCase : this.cases ) {
					final BinarySet< Class<? extends T> > eachCaseClassPair = new BinarySet< Class<? extends T> >(eachCase.getElementClass1(), eachCase.getElementClass2());
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
					for ( Case<P, ? extends T, ? extends T, R> eachCase : this.cases ) {
						if ( eachCase != candidateCase ) {
							final BinarySet< Class<? extends T> > eachCaseClassPair = new BinarySet< Class<? extends T> >(eachCase.getElementClass1(), eachCase.getElementClass2());
							
							if ( ClassExtra.isAssignableFrom(eachCaseClassPair, candidateCaseClassPair) && ClassExtra.isAssignableFrom(candidateCaseClassPair, eachCaseClassPair) ) {
								throw new NotImplementedException("Ambiguous cases: '" + eachCaseClassPair + "', '" + candidateCaseClassPair + "'");
							}
						}
					}
					
					// Agregar candidato al mapa asociativo y tomarlo como caso asociado
					this.setCaseByClassElementsPair(target1, target2, candidateCase);
					targetCase = candidateCase;
				}
				else {
					throw new NotImplementedException("Missing case: '" + targetClassPair + "'");
				}
			}
			
			/**
			 * Como es seguro que es asignable basta con conocer que
			 * el primer elemento es asignable con la clase del primer
			 * elemento del caso para conocer que también lo es con el segundo.
			 * Y si no lo es, se deduce que están invertidos los operandos.
			 */
			if ( targetCase.getElementClass1().isAssignableFrom(target1.getClass()) ) {
				return targetCase.processWithCasting(parameters, target1, target2);
			}
			else {
				return targetCase.processWithCasting(parameters, target2, target1);
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
}
