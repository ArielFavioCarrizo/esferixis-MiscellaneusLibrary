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
package com.esferixis.misc.observer;

import java.io.Serializable;

/**
 * Observador
 * 
 * @param <O> Tipo de observable
 */
public abstract class Observer<O> {
	public static enum Type {
		STRONG,
		WEAK
	};
	
	private O observable;
	private final Type type;
	
	/**
	 * @pre El observable y el tipo de observador no pueden ser nulos.
	 * 		Si es un observer serializable, tiene que heredar de SerializableObserver
	 * @post Crea el observador con el tipo especificado
	 */
	public Observer(Type type) {
		if ( ( type != null ) ) {
			if ( this instanceof Serializable ) {
				if ( ! (this instanceof SerializableObserver) ) {
					throw new RuntimeException("Expected that observer subclass SerializableObserver");
				}
			}
			
			this.observable = null;
			this.type = type;
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Crea el observador de tipo STRONG
	 */
	public Observer() {
		this(Type.STRONG);
	}
	
	/**
	 * @post Devuelve el administrador de observadores
	 */
	protected abstract ObserverManager<O, ?> getObserverManager();
	
	/**
	 * @post Devuelve el observable
	 */
	protected final O getObservable() {
		return this.observable;
	}
	
	/**
	 * @post Devuelve el tipo de observador
	 */
	Type getType() {
		return this.type;
	}
	
	/**
	 * @post Asigna el observable
	 */
	void setObservable(O observable) {
		this.observable = observable;
	}
	
	/**
	 * @pre El observador no tiene que estar asociado
	 * @post Asocia el observador con el observable especificado
	 */
	public final void attach(O observable) {
		if ( this.observable == null ) {
			this.observable = observable;
			this.getObserverManager().addObserver(this);
		}
		else {
			throw new IllegalStateException("Attemped to attach an attached observer");
		}
	}
	
	/**
	 * @pre El observador tiene que estar asociado
	 * @post Desasocia el observador
	 */
	public final void detach() {
		if ( this.observable != null ) {
			this.getObserverManager().removeObserver(this);
			this.observable = null;
		}
		else {
			throw new IllegalStateException("Attemped to detach an detached observer");
		}
	}
}
