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
package com.esferixis.misc.iterator;

import java.util.Iterator;

/**
 * Decorador de iterador
 * @param <T>
 */
public abstract class IteratorDecorator<T> implements Iterator<T> {
	protected Iterator<T> decoratedIterator;
	
	protected T lastElement; // Último elemento obtenido
	
	/**
	 * @post Crea el decorador con el iterador a decorar sin especificar
	 */
	protected IteratorDecorator() {
		
	}
	
	/**
	 * @post Crea el decorador con el iterador a decorar especificado
	 */
	public IteratorDecorator(Iterator<T> decoratedIterator) {
		this.decoratedIterator = decoratedIterator;
	}

	@Override
	public boolean hasNext() {
		return this.decoratedIterator.hasNext();
	}

	@Override
	public T next() {
		this.lastElement = this.decoratedIterator.next();
		return this.lastElement;
	}

	@Override
	public void remove() {
		this.decoratedIterator.remove();
	}
	
	
}
