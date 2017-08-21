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

import com.esferixis.misc.exception.NotImplementedException;

/**
 * Bimétodo
 *
 * @param <P> La clase de parámetros, puede ser void
 * @param <T> La clase o interface que abarca los objetos procesables
 * @param <R> El valor de retorno, puede ser void
 */
public abstract class Bimethod<P, T, R> {
	public static abstract class Case<P, V, W, R> {
		private final Class<V> elementClass1;
		private final Class<W> elementClass2;
		
		/**
		 * @pre Ninguna de las dos clases de elementos puede ser nula
		 * @post Crea el caso con las clases de elementos especificadas
		 */
		public Case(Class<V> elementClass1, Class<W> elementClass2) {
			if ( ( elementClass1 != null ) && ( elementClass2 != null ) ) {
				this.elementClass1 = elementClass1;
				this.elementClass2 = elementClass2;
			}
			else {
				throw new NullPointerException();
			}
		}
		
		/**
		 * @post Devuelve la clase del elemento 1
		 */
		public Class<V> getElementClass1() {
			return this.elementClass1;
		}
		
		/**
		 * @post Devuelve la clase del elemento 2
		 */
		public Class<W> getElementClass2() {
			return this.elementClass2;
		}
		
		/**
		 * @pre Los objetivos tienen que pertenecer a las clases especificadas
		 * @post Procesa los objetivos especificados
		 * @param parameters
		 * @param target1
		 * @param target2
		 * @return
		 */
		protected final R processWithCasting(P parameters, Object target1, Object target2) {
			return this.process(parameters, this.elementClass1.cast(target1), this.elementClass2.cast(target2) );
		}
		
		public abstract R process(P parameters, V target1, W target2);
		
		/**
		 * @post Devuelve la representación en cadena de carácteres
		 */
		@Override
		public String toString() {
			return "Case( " + this.elementClass1 + ", " + this.elementClass2 + " )";
		}
	}
	
	/**
	 * @post Crea el bimétodo especificado
	 */
	Bimethod() {
		
	}
	
	/**
	 * @post Procesa los objetivos especificados con los parámetros especificados
	 */
	public abstract R process(P parameters, T target1, T target2) throws NullPointerException, NotImplementedException;
}
