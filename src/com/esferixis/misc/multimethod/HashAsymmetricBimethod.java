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

import java.util.Map;
import java.util.WeakHashMap;

import com.esferixis.misc.collection.list.BinaryList;
import com.esferixis.misc.collection.set.BinarySet;
import com.esferixis.misc.multimethod.Bimethod.Case;

final class HashAsymmetricBimethod<P, T, R> extends AsymmetricBimethod<P, T, R> {

	private Map< BinaryList< Class<? extends T> >, Case<P, ? extends T, ? extends T, R> > casesByClassPairs;
	
	/**
	 * @param cases
	 * @param oppositeCases
	 */
	HashAsymmetricBimethod(
			com.esferixis.misc.multimethod.Bimethod.Case<P, ? extends T, ? extends T, R>[] cases,
			com.esferixis.misc.multimethod.Bimethod.Case<P, ? extends T, ? extends T, R>[] oppositeCases) {
		super(cases, oppositeCases);
		this.casesByClassPairs = new WeakHashMap< BinaryList< Class<? extends T> >, Case<P, ? extends T, ? extends T, R> >();
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.multimethod.AsymmetricBimethod#getCaseByClassElementsPair(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected com.esferixis.misc.multimethod.Bimethod.Case<P, ? extends T, ? extends T, R> getCaseByClassElementsPair(
			T target1, T target2) {
		final BinaryList< Class<? extends T> > targetClassPair = new BinaryList< Class<? extends T> >( (Class<? extends T>) target1.getClass(), (Class<? extends T>) target2.getClass());
		return this.casesByClassPairs.get(targetClassPair);
	}

	/* (non-Javadoc)
	 * @see com.esferixis.misc.multimethod.AsymmetricBimethod#setCaseByClassElementsPair(java.lang.Object, java.lang.Object, com.esferixis.misc.multimethod.Bimethod.Case)
	 */
	@Override
	protected void setCaseByClassElementsPair(
			T target1,
			T target2,
			com.esferixis.misc.multimethod.Bimethod.Case<P, ? extends T, ? extends T, R> targetCase) {
		final BinaryList< Class<? extends T> > targetClassPair = new BinaryList< Class<? extends T> >( (Class<? extends T>) target1.getClass(), (Class<? extends T>) target2.getClass());
		this.casesByClassPairs.put(targetClassPair, targetCase);
	}
	
}
