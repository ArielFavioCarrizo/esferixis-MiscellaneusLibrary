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
package com.esferixis.misc.reference;

public class UnmodifiableReference<T> {
	private T element;
	private boolean initialized;
	
	/**
	 * @post Crea una referencia no modificable sin inicializar
	 */
	public UnmodifiableReference() {
		this.initialized = false;
	}
	
	/**
	 * @post Crea una referencia no modificable del elemento especificado
	 */
	public UnmodifiableReference(T element) {
		this();
		this.init(element);
	}
	
	/**
	 * @post Inicializa la referencia
	 */
	public void init(T element) {
		if ( !this.initialized ) {
			this.element = element;
			this.initialized = true;
		}
		else {
			throw new IllegalStateException("Attemped to reinitialize a unmodifiable reference");
		}
	}
	
	/**
	 * @pre La referencia tiene que estar inicializada
	 * @post Devuelve el elemento
	 */
	public T get() {
		if ( this.initialized ) {
			return this.element;
		}
		else {
			throw new IllegalStateException("Unitialized unmodifiable reference");
		}
	}
	
	/**
	 * @post Devuelve si está inicializada
	 */
	public boolean isInitialized() {
		return this.initialized;
	}
	
	/**
	 * @post Devuelve el hash de identidad del elemento
	 */
	@Override
	public int hashCode() {
		return System.identityHashCode(this.get());
	}
	
	/**
	 * @post Devuelve si la referencia es igual a la otra
	 */
	@Override
	public boolean equals(Object other) {
		if ( other != null ) {
			if ( other instanceof UnmodifiableReference<?> ) {
				UnmodifiableReference<?> otherReference = (UnmodifiableReference<?>) other;
				return ( otherReference.get() == (Object) this.get() );
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}
