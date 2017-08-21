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

final class ConcreteInstanceClassIndexerPolymorphicVisitor<P, T, R> extends PolymorphicVisitor<P, T, R> {
	private final ConcreteInstanceClassIndexer<? super T> concreteInstanceClassIndexer;
	private Case<P, ? extends T, R> casesByClassIndex[];
	
	/**
	 * @pre El indexador de clases de instancia concretas no puede ser nulo.
	 * 		Los casos no pueden ser nulos y por cada tipo sólo puede haber
	 * 		uno.
	 * @post Crea el visitor polimórfico con el indexador de clases de instancia
	 * 		 concretas y los casos especificados
	 */
	ConcreteInstanceClassIndexerPolymorphicVisitor(ConcreteInstanceClassIndexer<? super T> concreteInstanceClassIndexer, Case<P, ? extends T, R>... cases) {
		super(cases);
		if ( concreteInstanceClassIndexer != null ) {
			this.concreteInstanceClassIndexer = concreteInstanceClassIndexer;
			this.casesByClassIndex = new Case[this.concreteInstanceClassIndexer.getConcreteClassesCount()];
		}
		else {
			throw new NullPointerException();
		}
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.multimethod.PolymorphicVisitor#getCaseByInstanceClass(java.lang.Object)
	 */
	@Override
	protected com.esferixis.misc.multimethod.PolymorphicVisitor.Case<P, ? extends T, R> getCaseByInstanceClass(
			T element) {
		return this.casesByClassIndex[this.concreteInstanceClassIndexer.classIndex(element)];
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.multimethod.PolymorphicVisitor#setCaseByInstanceClass(java.lang.Object, com.esferixis.misc.multimethod.PolymorphicVisitor.Case)
	 */
	@Override
	protected void setCaseByInstanceClass(
			T element,
			com.esferixis.misc.multimethod.PolymorphicVisitor.Case<P, ? extends T, R> targetCase) {
		this.casesByClassIndex[this.concreteInstanceClassIndexer.classIndex(element)] = targetCase;
	}
	
}
