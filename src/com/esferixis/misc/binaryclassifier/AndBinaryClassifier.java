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

package com.esferixis.misc.binaryclassifier;

/**
 * Clasificador binario
 * 
 * @param <T>
 */

public final class AndBinaryClassifier<T> implements BinaryClassifier<T> {
	private final BinaryClassifier<? super T> binaryClassifier1;
	private final BinaryClassifier<? super T> binaryClassifier2;
	
	/**
	 * @pre Los clasificadores binarios especificados no pueden ser nulos
	 * @post Crea el clasificador binario con los dos clasificadores binarios especificados
	 */
	public AndBinaryClassifier(BinaryClassifier<? super T> binaryClassifier1, BinaryClassifier<? super T> binaryClassifier2) {
		if ( ( binaryClassifier1 != null ) && ( binaryClassifier2 != null ) ) {
			this.binaryClassifier1 = binaryClassifier1;
			this.binaryClassifier2 = binaryClassifier2;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Evalúa el elemento especificado
	 * 
	 * 		 Devuelve true si ambos clasificadores devuelven true,
	 * 		 caso contrario false
	 */
	@Override
	public boolean evaluate(T element) {
		return ( this.binaryClassifier1.evaluate(element) && this.binaryClassifier2.evaluate(element) );
	}
}
