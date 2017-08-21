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

import com.esferixis.misc.classextra.ConcreteInstanceClassIndexer;

final class ConcreteInstanceClassIndexerSymmetricBimethod<P, T, R> extends SymmetricBimethod<P, T, R> {
	private final ConcreteInstanceClassIndexer<? super T> concreteInstanceClassIndexer;
	private Case<P, ? extends T, ? extends T, R> casesByClassIndexPair[][];
	
	/**
	 * @post Crea el bimétodo simétrico con el indexador de clases de instancia
	 * 		 concretas y los casos especificados
	 */
	ConcreteInstanceClassIndexerSymmetricBimethod(ConcreteInstanceClassIndexer<? super T> concreteInstanceClassIndexer, Case<P, ? extends T, ? extends T, R>... cases) {
		super(cases);
		if ( concreteInstanceClassIndexer != null ) {
			this.concreteInstanceClassIndexer = concreteInstanceClassIndexer;
			
			final int concreteClassesCount = this.concreteInstanceClassIndexer.getConcreteClassesCount();
			
			this.casesByClassIndexPair = new Case[this.concreteInstanceClassIndexer.getConcreteClassesCount()][];
			for ( int i=0; i<concreteClassesCount; i++) {
				this.casesByClassIndexPair[i] = new Case[i+1];
			}
		}
		else {
			throw new NullPointerException();
		}
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.multimethod.SymmetricBimethod#getCaseByClassElementsPair(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected com.esferixis.misc.multimethod.Bimethod.Case<P, ? extends T, ? extends T, R> getCaseByClassElementsPair(
			T target1, T target2) {
		int index1 = this.concreteInstanceClassIndexer.classIndex(target1);
		int index2 = this.concreteInstanceClassIndexer.classIndex(target2);
		
		Case<P, ? extends T, ? extends T, R> associatedCase;
		
		if ( index1 >= index2 ) {
			associatedCase = this.casesByClassIndexPair[index1][index2];
		}
		else {
			associatedCase = this.casesByClassIndexPair[index2][index1];
		}
		
		return associatedCase;
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.multimethod.SymmetricBimethod#setCaseByClassElementsPair(java.lang.Object, java.lang.Object, com.esferixis.misc.multimethod.Bimethod.Case)
	 */
	@Override
	protected void setCaseByClassElementsPair(
			T target1,
			T target2,
			com.esferixis.misc.multimethod.Bimethod.Case<P, ? extends T, ? extends T, R> targetCase) {
		int index1 = this.concreteInstanceClassIndexer.classIndex(target1);
		int index2 = this.concreteInstanceClassIndexer.classIndex(target2);
		
		if ( index1 >= index2 ) {
			this.casesByClassIndexPair[index1][index2] = targetCase;
		}
		else {
			this.casesByClassIndexPair[index2][index1] = targetCase;
		}
	}
}
