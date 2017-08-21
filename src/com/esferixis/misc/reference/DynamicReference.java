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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import com.esferixis.misc.ElementCallback;
import com.esferixis.misc.observer.ObserverManager;

public final class DynamicReference<T> {
	final ObserverManager<DynamicReference<T>, DynamicReferenceObserver<T>> observerManager = new ObserverManager<DynamicReference<T>, DynamicReferenceObserver<T>>(this, (Class) DynamicReferenceObserver.class);
	
	private T value;
	
	/**
	 * @post Crea una referencia sin inicializar
	 */
	public DynamicReference() {
		
	}
	
	/**
	 * @post Crea una referencia dinámica con la referencia al elemento especificado
	 */
	public DynamicReference(T element) {
		this.value = element;
	}
	
	/**
	 * @post Especifica el valor
	 */
	public void set(final T value) {
		this.observerManager.notifyObservers(new ElementCallback<DynamicReferenceObserver<T>>() {

			@Override
			public void run(final DynamicReferenceObserver<T> observer) {
				observer.notifyValueChange(value);
			}
			
		});
		
		this.value = value;
	}
	
	/**
	 * @post Devuelve el elemento
	 */
	public T get() {
		return this.value;
	}
	
	/**
	 * @post Crea una colección con los datos de la colección de referencias
	 * 		 especificada (Inmutable)
	 */
	public static <T> Collection<T> createElementsCollection(final Collection<DynamicReference<T>> dynamicReferences) {
		return new AbstractCollection<T>() {

			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {
					final Iterator<DynamicReference<T>> backingIterator = dynamicReferences.iterator();

					@Override
					public boolean hasNext() {
						return backingIterator.hasNext();
					}

					@Override
					public T next() {
						return backingIterator.next().get();
					}
					
				};
			}

			@Override
			public int size() {
				return dynamicReferences.size();
			}
			
		};
	}
}
