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
package com.esferixis.misc.classextra;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.WeakHashMap;

/**
 * Indexador de instancia de clase concreta,
 * supone que las clases concretas son "final", no pueden
 * tener hijos
 * 
 * @author ariel
 *
 */
public abstract class ConcreteInstanceClassIndexer<T> {
	protected final int concreteClassesCount;
	
	private final static Map< Class<?>, ConcreteInstanceClassIndexer<?> > indexerByClass = new WeakHashMap< Class<?>, ConcreteInstanceClassIndexer<?> >();
	
	/**
	 * @pre La clase asociada no puede ser nula y la cantidad de clases concretas
	 * 		tiene que ser positiva
	 * @post Crea el indexador con la clase asociada y la cantidad de clases concretas
	 * 		 especificadas
	 */
	public ConcreteInstanceClassIndexer(Class<T> targetClass, int concreteClassesCount) {
		if ( targetClass != null ) {
			if ( concreteClassesCount > 0 ) {
				if ( indexerByClass.put(targetClass, this) != null ) {
					throw new RuntimeException("Only one class indexer per class");
				}
				
				this.concreteClassesCount = concreteClassesCount;
			}
			else {
				throw new IllegalArgumentException("Invalid concrete classes count");
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve la cantidad de clases concretas
	 */
	public int getConcreteClassesCount() {
		return this.concreteClassesCount;
	}
	
	/**
	 * @pre El elemento no puede ser nulo
	 * @post Devuelve el índice que corresponde a la
	 * 		 clase del elemento especificado
	 */
	public abstract int classIndex(T element);
	
	/**
	 * @pre La clase no puede ser nula
	 * @post Devuelve el indexador con cobertura más pequeña de la clase
	 * 		 especificada, si no existe devuelve null.
	 * 		 Si hay ambigüedad tomará el que abarque menos clases concretas
	 */
	public static <T> ConcreteInstanceClassIndexer<? super T> indexerByClass(Class<T> targetClass) {
		if ( targetClass != null ) {
			Set< Class<?> > testedClasses = new HashSet< Class<?> >();
			Stack< Class<?> > pendingClasses = new Stack< Class<?> >();
			ConcreteInstanceClassIndexer<? super T> candidate = null;
			
			pendingClasses.add(targetClass);
			
			while ( !pendingClasses.isEmpty() ) {
				Class<?> eachClass = pendingClasses.pop();
				if ( testedClasses.add(eachClass) ) {
					ConcreteInstanceClassIndexer<? super T> eachIndexer = (ConcreteInstanceClassIndexer<? super T>) indexerByClass.get(targetClass);
					
					if ( eachIndexer != null ) {
						if ( ( candidate == null ) || ( eachIndexer.getConcreteClassesCount() < candidate.getConcreteClassesCount() ) ) {
							candidate = eachIndexer;
						}
					}
					else {
						pendingClasses.addAll( Arrays.asList( eachClass.getInterfaces() ) );
						if ( eachClass.getSuperclass() != null ) {
							pendingClasses.add(eachClass.getSuperclass());
						}
					}
				}
			}
			
			return candidate;
		}
		else {
			throw new NullPointerException();
		}
	}
}
