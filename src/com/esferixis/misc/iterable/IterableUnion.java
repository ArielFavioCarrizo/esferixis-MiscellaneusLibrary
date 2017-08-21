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
package com.esferixis.misc.iterable;

import java.util.Arrays;
import java.util.Iterator;

import com.esferixis.misc.iterator.AppendIterator;

public final class IterableUnion<T> implements Iterable<T> {
	private final Iterable< Iterable<T> > iterablesOfIterables;

	/**
	 * @pre El array de ierables no puede ser nulo
	 * @post Crea la unión de iterables especificada
	 */
	public IterableUnion(Iterable<T>... iterables) {
		this(Arrays.asList(iterables));
	}
	
	/**
	 * @pre Los iterables de iterables no pueden ser nulos
	 * @post Crea la unión de iterables especificada
	 */
	public IterableUnion(Iterable< Iterable<T> > iterablesOfIterables) {
		if ( iterablesOfIterables != null ) {
			this.iterablesOfIterables = iterablesOfIterables;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new AppendIterator<T>( new Iterator< Iterator<T> >() {
			private final Iterator< Iterable<T> > iterablesIterator = IterableUnion.this.iterablesOfIterables.iterator();

			@Override
			public boolean hasNext() {
				return this.iterablesIterator.hasNext();
			}

			@Override
			public Iterator<T> next() {
				return this.iterablesIterator.next().iterator();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		} );
	}
	
}
