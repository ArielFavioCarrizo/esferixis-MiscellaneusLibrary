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

/**
 * Iterador transformado
 */

package com.esferixis.misc.iterator;

import java.util.Iterator;

import com.esferixis.misc.relation.Function;

/**
 * @param <S> Tipo del origen
 * @param <D> Tipo del destino
 */
public final class TransformedIterator<S, D> implements Iterator<D> {
	private final Iterator<S> sourceIterator;
	private final Function<? super S, D> transformer;
	
	/**
	 * @pre El iterador origen y el transformador no pueden ser nulos
	 * @post Crea la transformación del iterador especificado
	 * 		 con el transformador de origen a destino especificado
	 */
	public TransformedIterator(Iterator<S> sourceIterator, Function<? super S, D> transformer) {
		if ( ( sourceIterator != null ) && ( transformer != null ) ) {
			this.sourceIterator = sourceIterator;
			this.transformer = transformer;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve si hay elemento siguiente
	 */
	@Override
	public boolean hasNext() {
		return this.sourceIterator.hasNext();
	}

	/**
	 * @pre Tiene que haber elemento siguiente
	 * @post Devuelve el siguiente elemento
	 */
	@Override
	public D next() {
		return this.transformer.evaluate(this.sourceIterator.next());
	}

	/**
	 * @pre Tiene que haber último elemento obtenido y no tiene que haber sido eliminado
	 * 		a través de éste método
	 * @post Elimina el último elemento obtenido
	 */
	@Override
	public void remove() {
		this.sourceIterator.remove();
	}
	
}
