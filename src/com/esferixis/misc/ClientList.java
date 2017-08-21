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
package com.esferixis.misc;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author ariel
 *
 */
public final class ClientList<T> {
	public static final class Element<T> {
		private ClientList<T> clientRegistrar;
		
		private Element<T> beforeClient;
		private Element<T> afterClient;
		
		private final T value;
		
		/**
		 * @post Crea el cliente con el valor especificado
		 */
		public Element(T value) {
			this.value = value;
		}
		
		/**
		 * @post Devuelve el valor
		 */
		public T getValue() {
			return this.value;
		}
	}
	
	private final class LocalIterator implements Iterator<T> {
		private int modificationCount;
		private Element<T> nextClient;
		
		/**
		 * @post Crea el iterador
		 */
		public LocalIterator() {
			this.modificationCount = ClientList.this.modificationCount;
			this.nextClient = ClientList.this.firstClient;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			if ( this.modificationCount != ClientList.this.modificationCount ) {
				throw new ConcurrentModificationException();
			}
			
			return (this.nextClient != null);
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next() {
			if ( this.hasNext() ) {
				final T value = this.nextClient.value;
				
				this.nextClient = this.nextClient.afterClient;
				
				return value;
			}
			else {
				throw new NoSuchElementException();
			}
		}
	}
	
	private Element<T> firstClient;
	
	private int modificationCount;
	
	private final Collection<T> values;
	
	private int sizeCount;
	
	/**
	 * @post Crea el registrar
	 */
	public ClientList() {
		this.firstClient = null;
		this.sizeCount = 0;
		
		this.values = new AbstractCollection<T>() {

			@Override
			public Iterator<T> iterator() {
				return new LocalIterator();
			}

			@Override
			public int size() {
				return ClientList.this.sizeCount;
			}
			
		};
	}
	
	/**
	 * @post Se adosa el cliente especificado
	 */
	public void attach(Element<T> element) {
		if ( element != null ) {
			if ( element.clientRegistrar == null ) {
				element.clientRegistrar.modificationCount++;
				
				element.clientRegistrar = this;
				
				element.afterClient = this.firstClient;
				element.beforeClient = null;
				
				this.firstClient.beforeClient = element;
				this.firstClient = element;
				
				this.sizeCount++;
			}
			else {
				throw new IllegalStateException("Has been attached to a client registrar");
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Se desasigna el elemento especificado, tiene
	 * 		 que pertenecer a ésta lista
	 */
	public void detach(Element<T> element) {
		if ( element != null ) {
			if ( element.clientRegistrar == this ) {
				element.clientRegistrar.modificationCount++;
				
				if ( element.beforeClient != null ) {
					element.beforeClient.afterClient = element.afterClient;
				}
				
				if ( element.afterClient != null ) {
					element.afterClient.beforeClient = element.beforeClient;
				}
				
				element.beforeClient = null;
				element.afterClient = null;
				
				element.clientRegistrar = null;
				
				element.clientRegistrar.sizeCount--;
			}
			else {
				throw new IllegalStateException("Client list mismatch or element hasn't been attached to a client registrar");
			}
		}
		else {
			throw new NullPointerException();
		}
	}
	
	/**
	 * @post Devuelve la colección de valores
	 */
	public Collection<T> values() {
		return this.values;
	}
}
