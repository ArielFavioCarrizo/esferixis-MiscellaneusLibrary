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

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.esferixis.misc.ElementCallback;

/**
 * Administrador de observadores.
 * Si es serializado, no puede llevar referencias "weak".
 * 
 * @param <O> Tipo de observer
 */
public final class ObserverManager<A, O extends Observer<A>> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1061415519574986315L;
	
	private final Class<O> observerClass;
	private final A observable;
	private List<O> strongObservers;
	
	private final transient List< WeakReference<O> > weakObservers;
	
	/**
	 * @pre El observable ni la clase observadores puede ser nula
	 * @post Crea el administrador de observadores con el observable
	 * 		 especificado
	 */
	public ObserverManager(A observable, Class<O> observerClass) {
		if ( ( observerClass != null ) && ( observable != null ) ) {			
			this.observerClass = observerClass;
			this.observable = observable;
			this.strongObservers = new ArrayList<O>();
			if ( SerializableObserver.class.isAssignableFrom(observerClass) ) {
				this.weakObservers = null;
			}
			else {
				this.weakObservers = new ArrayList< WeakReference<O> >();
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve el observable
	 */
	public A getObservable() {
		return this.observable;
	}
	
	/**
	 * @pre La llamada no puede ser nula
	 * @post Efectúa la notificación con la llamada especificada
	 */
	public synchronized void notifyObservers(ElementCallback<? super O> elementCallBack) {
		for ( O eachObserver : this.strongObservers ) {
			elementCallBack.run(eachObserver);
		}
		
		if ( this.weakObservers != null ) {
			final Iterator< WeakReference<O> > referenceIterator = this.weakObservers.iterator();
			while ( referenceIterator.hasNext() ) {
				final WeakReference<O> eachReference = referenceIterator.next();
				final O eachObservable = eachReference.get();
				
				if ( eachObservable != null ) {
					elementCallBack.run(eachObservable);
				}
				else {
					referenceIterator.remove();
				}
			}
		}
	}
	
	/**
	 * @post Agrega un observador
	 * @param observer
	 */
	synchronized void addObserver(Observer<?> observer) {
		if ( this.observerClass.isInstance(observer) ) {
			switch (observer.getType()) {
			case STRONG:
				this.strongObservers.add( (O) observer);
				break;
			case WEAK:
				this.weakObservers.add( new WeakReference<O>((O) observer) );
				break;
			default:
				throw new RuntimeException("Unexpected inconsistency");
			}
		}
		else {
			throw new ClassCastException();
		}
	}
	
	/**
	 * @post Quita un observador
	 * @param observer
	 */
	synchronized void removeObserver(Observer<?> observer) {
		this.strongObservers.remove(observer);
	}
}
